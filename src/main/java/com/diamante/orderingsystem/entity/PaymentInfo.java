package com.diamante.orderingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentInfo {

    private String cardNumber;

    private String expirationDate;

    private String securityCode;

    private String zipCode;

}
