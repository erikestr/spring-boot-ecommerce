package com.luv2code.springbootecommerce.dto;

import lombok.Data;

@Data
public class PurchaseResponse {

    /* Lombok generate constructors only for finals */
    /* Another option is using @NonNull annotation on the field instead of final */
    private final String orderTrackingNumber;
}
