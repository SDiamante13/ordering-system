package com.diamante.orderingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCTS")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotNull(message = "Product name is a required field")
    private String productName;

    @NotNull(message = "Description is a required field")
    @Size(max = 80, message = "The description must be no longer than 40 characters")
    private String description;

    @NotNull(message = "Manufacturer is a required field")
    private String manufacturer;

    @Lob
    private byte[] productImage;

    @NotNull(message = "Category is a required field")
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull(message = "Price is a required field")
    private Double price;

    private int quantity;
}
