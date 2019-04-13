package com.diamante.orderingsystem.bootstrap;

import com.diamante.orderingsystem.entity.*;
import com.diamante.orderingsystem.service.customer.CustomerService;
import com.diamante.orderingsystem.service.order.OrderService;
import com.diamante.orderingsystem.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Component
@Profile("prod")
@Slf4j
@Transactional
public class InitialDataLoader implements CommandLineRunner {

    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderService orderService;

    public InitialDataLoader(CustomerService customerService, ProductService productService, OrderService orderService) {
        this.customerService = customerService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override
    public void run(String... args) throws Exception {
        orderService.deleteAllOrdersForCustomer(1L);
        orderService.deleteAllOrdersForCustomer(2L);
        orderService.deleteAllOrdersForCustomer(3L);
        customerService.deleteAllCustomers();
        productService.deleteAllProducts();
        customerService.resetAllCustomerIds();
        orderService.resetAllOrderIds();
        productService.resetAllProductIds();

        log.info("*************Loading orders into database*************");
        saveOrdersToDatabase();
    }

    private void saveOrdersToDatabase() {
        Customer customer1 = Customer.builder()
                .firstName("Paul")
                .lastName("Ryan")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("1234574638")
                        .expirationDate("11/23")
                        .securityCode("873")
                        .zipCode("50892")
                        .build())
                .build();
        Customer customer2 = Customer.builder()
                .firstName("Jim")
                .lastName("Jefferies")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("771761728")
                        .expirationDate("12/19")
                        .securityCode("123")
                        .zipCode("63781")
                        .build())
                .build();

        Customer customer3 = Customer.builder()
                .firstName("George")
                .lastName("Jefferson")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("97244142")
                        .expirationDate("5/18")
                        .securityCode("782")
                        .zipCode("60443")
                        .build())
                .build();

        Product product1 = Product.builder()
                .productName("Ipod")
                .description("Music player")
                .manufacturer("Apple")
                .category(Category.ELECTRONICS)
                .price(120.00)
                .build();

        Product product2 = Product.builder()
                .productName("Merlin")
                .description("Novel about the adventures of King Arthur and a wizard named Merlin.")
                .manufacturer("Penguin Books")
                .category(Category.BOOKS)
                .price(6.99)
                .build();

        Product product3 = Product.builder()
                .productName("Men's Black Watch")
                .description("A black watch")
                .manufacturer("IZOD")
                .category(Category.CLOTHING_SHOES_JEWELERY_WATCHES)
                .price(45.79)
                .build();

        Product product4 = Product.builder()
                .productName("Flat Screen TV")
                .description("OLED High Definition TV")
                .manufacturer("Samsung")
                .category(Category.ELECTRONICS)
                .price(599.99)
                .build();

        Product product5 = Product.builder()
                .productName("Refrigerator")
                .description("Black Refrigerator with water dispenser")
                .manufacturer("Black & Decker")
                .category(Category.HOME_LIVING)
                .price(499.99)
                .build();

        Product product6 = Product.builder()
                .productName("Summer Dress")
                .description("Yellow sunflower dress for 18-24 mos.")
                .manufacturer("Osh Kosh Ba Gosh")
                .category(Category.CLOTHING_SHOES_JEWELERY_WATCHES)
                .price(21.89)
                .build();

        Product product7 = Product.builder()
                .productName("Harry Potter")
                .description("A Book about harry potter tales.")
                .manufacturer("J.K Rowlings")
                .price(34.56)
                .quantity(1)
                .category(Category.BOOKS)
                .build();

        Set<Product> productSet1 = new HashSet<>(Arrays.asList(product1, product2, product3));
        Set<Product> productSet2 = new HashSet<>(Collections.singletonList(product4));
        Set<Product> productSet3 = new HashSet<>(Arrays.asList(product5, product6));
        Set<Product> productSet4 = new HashSet<>(Collections.singletonList(product7));

        double totalBalance1 = product1.getPrice() + product2.getPrice() + product3.getPrice();
        double totalBalance2 = product4.getPrice();
        double totalBalance3 = product5.getPrice() + product6.getPrice();
        double totalBalance4 = product7.getPrice();

        Order order1 = Order.builder()
                .customer(customer1)
                .products(productSet1)
                .orderDate(LocalDate.of(2018, 6, 15))
                .totalBalance(totalBalance1)
                .build();

        Order order2 = Order.builder()
                .customer(customer1)
                .products(productSet2)
                .orderDate(LocalDate.of(2017, 2, 8))
                .totalBalance(totalBalance2)
                .build();

        Order order3 = Order.builder()
                .customer(customer2)
                .products(productSet3)
                .orderDate(LocalDate.of(2019, 1, 17))
                .totalBalance(totalBalance3)
                .build();

        Order order4 = Order.builder()
                .customer(customer3)
                .products(productSet4)
                .orderDate(LocalDate.of(2018, 7, 30))
                .totalBalance(totalBalance4)
                .build();

        customerService.saveCustomer(customer1);
        customerService.saveCustomer(customer2);
        customerService.saveCustomer(customer3);

        productService.saveProduct(product1);
        productService.saveProduct(product2);
        productService.saveProduct(product3);
        productService.saveProduct(product4);
        productService.saveProduct(product5);
        productService.saveProduct(product6);
        productService.saveProduct(product7);

        orderService.saveOrder(order1);
        orderService.saveOrder(order2);
        orderService.saveOrder(order3);
        orderService.saveOrder(order4);
    }
}