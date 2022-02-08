package com.luv2code.springbootecommerce.service;

import com.luv2code.springbootecommerce.dao.CustomerRepository;
import com.luv2code.springbootecommerce.dto.PaymentInfo;
import com.luv2code.springbootecommerce.dto.Purchase;
import com.luv2code.springbootecommerce.dto.PurchaseResponse;
import com.luv2code.springbootecommerce.entity.Customer;
import com.luv2code.springbootecommerce.entity.Order;
import com.luv2code.springbootecommerce.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private final CustomerRepository customerRepository;

    /* @Autowired is optional since we only have one constructor */
    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               @Value("${stripe.key.secret}") String secretKey){
        this.customerRepository = customerRepository;
        /* initialize stripe API with secret key */
        Stripe.apiKey = secretKey;
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

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {

        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        /* generate a rando UUID number (UUID ver-4) */
        return UUID.randomUUID().toString();
    }
}
