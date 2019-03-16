package com.diamante.orderingsystem.bootstrap;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.PaymentInfo;
import com.diamante.orderingsystem.service.customer.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class InitialDataLoader implements CommandLineRunner {
//    Steps to load Customers
    private final CustomerService customerService;

    public InitialDataLoader(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("*************Loading customers into database*************");

        customerService.deleteAllCustomers();
        customerService.resetAllCustomerIds();

        customerService.saveCustomer(Customer.builder()
                .firstName("Tom")
                .lastName("Green")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("4372762")
                        .expirationDate("7/21")
                        .securityCode("345")
                        .zipCode("87262")
                        .build())
                .build());

        customerService.saveCustomer(Customer.builder()
                .firstName("George")
                .lastName("Jefferson")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("97244142")
                        .expirationDate("5/18")
                        .securityCode("782")
                        .zipCode("60443")
                        .build())
                .build());

        customerService.saveCustomer(Customer.builder()
                .firstName("Frank")
                .lastName("Wright")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("37286611")
                        .expirationDate("7/23")
                        .securityCode("230")
                        .zipCode("30276")
                        .build())
                .build());


        log.info("********Loading products into database*************");
//
//        productService.saveProduct(product.builder()
//                .productName("Harry Potter")
//                .description("A Book about harry potter tales.")
//                .manufacturer("J.K Rowlings")
//                .price(34.56)
//                .quantity(1)
//                .category(Category.BOOKS)
//                .build());
//
//        productService.saveProduct(product.builder()
//                .productName("Samsung")
//                .description("Samsung")
//                .manufacturer("J.K Rowlings")
//                .price(34.56)
//                .quantity(1)
//                .category(Category.BOOKS)
//                .build());
    }
}
