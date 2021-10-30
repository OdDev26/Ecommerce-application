package com.example.safariwebstore008.models;
import com.example.safariwebstore008.common.BaseClass;
import com.example.safariwebstore008.enums.DeliveryMethod;
import com.example.safariwebstore008.enums.DeliveryStatus;
import com.example.safariwebstore008.enums.OrderAssigStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerOrder extends BaseClass {
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private Date deliveryDate;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    private BigInteger deliveryFee;
    @ManyToOne
    private ShippingAddress shippingAddress;
    @Enumerated(EnumType.STRING)
    private OrderAssigStatus status;
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;
    private BigInteger totalOrderAmount;
    @ManyToOne
    @JsonIgnore
    private User userModel;
    @OneToMany
    private List<OrderDetails> orderDetailsList;

}
