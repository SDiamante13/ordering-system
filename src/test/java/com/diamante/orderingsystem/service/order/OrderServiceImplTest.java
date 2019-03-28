package com.diamante.orderingsystem.service.order;

import com.diamante.orderingsystem.entity.*;
import com.diamante.orderingsystem.repository.order.OrderRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl(orderRepository);

    // region test variables
    private Order order1;
    private Order order2;
    private Order order3;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private Product product6;
    private double totalBalance1;
    private double totalBalance2;
    private double totalBalance3;
    private Set<Product> productSet1;
    private Set<Product> productSet2;
    private Set<Product> productSet3;

    private Customer customer1;
    private Customer customer2;
    // endregion test variables

    @Before
    public void setUp() throws Exception {
        setUpTestStubs();
    }

    /**
     * Test Cases
     **/
    // saveOrder_savesOrderToDatabase
    // findAllOrdersForCustomer_returnsListOfOrdersForGivenCustomer
    // findAllOrdersForCustomer_returnsNull_whenCustomerDoesNotExistInDatabase
    // updateOrder_updatesExistingOrderInTheDatabase_returnsUpdatedOrder
    // updateOrder_returnsNull_whenOrderIdDoesNotExistInDatabase
    // deleteOrderById_ThrowsDataRetrievalFailureException_whenOrderIdDoesNotExistInDatabase
    // deleteOrderById_removesOrderFromDatabase
    // deleteAllOrdersForCustomer_removesAllOrdersFromDatabaseForTheGivenCustomer
    // deleteAllOrdersForCustomer_returnsCustomerNotFoundException_whenCustomerDoesNotExistInDatabase
    // resetAllOrderIds_callsResetAllOrderIds

    private void setUpTestStubs() {
        customer1 = Customer.builder()
                .firstName("Paul")
                .lastName("Ryan")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("1234574638")
                        .expirationDate("11/23")
                        .securityCode("873")
                        .zipCode("50892")
                        .build())
                .build();
        customer2 = Customer.builder()
                .firstName("Jim")
                .lastName("Jefferies")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("771761728")
                        .expirationDate("12/19")
                        .securityCode("123")
                        .zipCode("63781")
                        .build())
                .build();

        product1 = Product.builder()
                .productName("Ipod")
                .description("Music player")
                .manufacturer("Apple")
                .category(Category.ELECTRONICS)
                .price(120.00)
                .build();

        product2 = Product.builder()
                .productName("Merlin")
                .description("Novel about the adventures of King Arthur and a wizard named Merlin.")
                .manufacturer("Penguin Books")
                .category(Category.BOOKS)
                .price(6.99)
                .build();

        product3 = Product.builder()
                .productName("Men's Black Watch")
                .description("A black watch")
                .manufacturer("IZOD")
                .category(Category.CLOTHING_SHOES_JEWELERY_WATCHES)
                .price(45.79)
                .build();

        product4 = Product.builder()
                .productName("Flat Screen TV")
                .description("OLED High Definition TV")
                .manufacturer("Samsung")
                .category(Category.ELECTRONICS)
                .price(599.99)
                .build();

        product5 = Product.builder()
                .productName("Refrigerator")
                .description("Black Refrigerator with water dispenser")
                .manufacturer("Black & Decker")
                .category(Category.HOME_LIVING)
                .price(499.99)
                .build();

        product6 = Product.builder()
                .productName("Summer Dress")
                .description("Yellow sunflower dress for 18-24 mos.")
                .manufacturer("Osh Kosh Ba Gosh")
                .category(Category.CLOTHING_SHOES_JEWELERY_WATCHES)
                .price(21.89)
                .build();

        productSet1 = new HashSet<>(Arrays.asList(product1, product2, product3));
        productSet2 = new HashSet<>(Collections.singletonList(product4));
        productSet3 = new HashSet<>(Arrays.asList(product5, product6));

        totalBalance1 = product1.getPrice() + product2.getPrice() + product3.getPrice();
        totalBalance2 = product4.getPrice();
        totalBalance3 = product5.getPrice() + product6.getPrice();

        order1 = Order.builder()
                .customer(customer1)
                .products(productSet1)
                .orderDate(LocalDate.of(2018, 6, 15))
                .totalBalance(totalBalance1)
                .build();

        order2 = Order.builder()
                .customer(customer1)
                .products(productSet2)
                .orderDate(LocalDate.of(2017, 2, 8))
                .totalBalance(totalBalance2)
                .build();

        order3 = Order.builder()
                .customer(customer2)
                .products(productSet3)
                .orderDate(LocalDate.of(2019, 1, 17))
                .totalBalance(totalBalance2)
                .build();
    }
}