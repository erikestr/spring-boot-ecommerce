package com.luv2code.springbootecommerce.service;

import com.luv2code.springbootecommerce.dao.CustomerRepository;
import com.luv2code.springbootecommerce.dto.Purchase;
import com.luv2code.springbootecommerce.dto.PurchaseResponse;
import com.luv2code.springbootecommerce.entity.Customer;
import com.luv2code.springbootecommerce.entity.Order;
import com.luv2code.springbootecommerce.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private CustomerRepository customerRepository;

    /* @Autowired is optional since we only have one constructor */
    public CheckoutServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        /* retrieve the order info from dto */
        Order order = purchase.getOrder();

        /*generate tracking number */
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        /* populate order with orderItems */
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        /* populate order with billingAddress and shippingAddress */
        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        /* populate customer with order */
        Customer customer = purchase.getCustomer();

        /*check if this is an existing customer*/
        String theEmail = customer.getEmail();

        Customer customerFromDB = customerRepository.findByEmail(theEmail);

        if(customerFromDB != null){
            customer = customerFromDB;
        }

        customer.add(order);

        /* save to the database */
        customerRepository.save(customer);

        /* return a response */
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        /* generate a rando UUID number (UUID ver-4) */
        return UUID.randomUUID().toString();
    }
}
