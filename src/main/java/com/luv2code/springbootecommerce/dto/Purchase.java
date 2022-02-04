package com.luv2code.springbootecommerce.dto;

import com.luv2code.springbootecommerce.entity.Address;
import com.luv2code.springbootecommerce.entity.Customer;
import com.luv2code.springbootecommerce.entity.Order;
import com.luv2code.springbootecommerce.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}

