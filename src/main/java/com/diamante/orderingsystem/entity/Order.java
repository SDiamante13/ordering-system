package com.diamante.orderingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToMany
    @JoinTable(
            name = "ORDER_PRODUCT",
            joinColumns = @JoinColumn(name = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID"))
    @NotNull(message = "Products is a required field")
    private Set<Product> products;

    @NotNull(message = "Order date is a required field")
    private LocalDate orderDate;

    @Range(min = 1, message = "Total balance must be at least $1")
    private double totalBalance;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_FK")
    @NotNull(message = "Customer is a required field")
    private Customer customer;
}
