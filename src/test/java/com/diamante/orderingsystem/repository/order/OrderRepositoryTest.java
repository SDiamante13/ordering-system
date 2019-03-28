package com.diamante.orderingsystem.repository.order;

import com.diamante.orderingsystem.TestDatabaseSetup;
import com.diamante.orderingsystem.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("default")
@DataJpaTest
public class OrderRepositoryTest extends TestDatabaseSetup {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    // region test variables
    private Order order1;
    private Order order2;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private double totalBalance1;
    private double totalBalance2;
    private Set<Product> productSet1;
    private Set<Product> productSet2;

    private Customer customer1;
    private Customer customer2;
    // endregion test variables

    @Before
    public void setUp() throws Exception {
        setUpCustomersAndProducts();
        addOrdersToDatabase();
    }

    @After
    public void tearDown() throws Exception {
        orderRepository.deleteAll();
    }

    /**
     * Test Cases
     */
    // save_savesOrderToDatabase
    // findById_returnsCorrectOrder
    // count_returnsCorrectNumberOfOrders
    // deleteById_deletesCorrectOrderFromDatabase
    // findOrdersByCustomer_returnsListOfOrdersForGivenCustomer

    private void setUpCustomersAndProducts() {
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

        productSet1 = new HashSet<>(Arrays.asList(product1, product2));

        productSet2 = new HashSet<>(Arrays.asList(product3, product4));

        totalBalance1 = product1.getPrice() + product2.getPrice();
        totalBalance2 = product3.getPrice() + product4.getPrice();
    }

    private void addOrdersToDatabase() {
        orderRepository.resetAllOrderIds();

        order1 = Order.builder()
                .customer(customer1)
                .products(productSet1)
                .orderDate(LocalDate.of(2018, 6, 15))
                .totalBalance(totalBalance1)
                .build();

        order2 = Order.builder()
                .customer(customer2)
                .products(productSet2)
                .orderDate(LocalDate.of(2019, 2, 8))
                .totalBalance(totalBalance2)
                .build();


        testEntityManager.persist(order1);
        testEntityManager.persist(order2);
    }
}
