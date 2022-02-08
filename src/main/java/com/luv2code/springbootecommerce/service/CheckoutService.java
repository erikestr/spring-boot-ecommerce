package com.luv2code.springbootecommerce.service;

import com.luv2code.springbootecommerce.dto.PaymentInfo;
import com.luv2code.springbootecommerce.dto.Purchase;
import com.luv2code.springbootecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
