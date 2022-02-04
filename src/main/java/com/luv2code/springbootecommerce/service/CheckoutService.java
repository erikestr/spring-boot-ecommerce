package com.luv2code.springbootecommerce.service;

import com.luv2code.springbootecommerce.dto.Purchase;
import com.luv2code.springbootecommerce.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}
