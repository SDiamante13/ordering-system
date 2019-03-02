package com.diamante.orderingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentInfo {

    private String cardNumber;

    private String expirationDate;

    @Size(min= 3, max = 3, message = "Your security code must be exactly 3 digits.")
    private String securityCode;

    @Size(min= 5, max = 5, message = "Your zip code must be exactly 5 digits.")
    private String zipCode;

}
