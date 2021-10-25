package com.example.safariwebstore008.models;

import com.example.safariwebstore008.common.BaseClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details_table")
public class OrderDetails extends BaseClass {

    private Integer quantity;

    private BigInteger price;

    @OneToOne
    private Product product;

}
