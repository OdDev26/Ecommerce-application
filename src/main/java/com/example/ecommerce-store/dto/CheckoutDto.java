package com.example.safariwebstore008.dto;

import com.example.safariwebstore008.models.Cart;
import lombok.Data;

import java.math.BigInteger;

@Data
public class CheckoutDto {
  private String deliveryMethod;
  private BigInteger deliveryFee;
  private BigInteger totalOrderAmount;
}
