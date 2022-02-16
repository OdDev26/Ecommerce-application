package com.example.safariwebstore008.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class ProcessPaymentDTO {
    private String firstName;
    private String lastName;
    private String email;
    private BigInteger amount;
}
