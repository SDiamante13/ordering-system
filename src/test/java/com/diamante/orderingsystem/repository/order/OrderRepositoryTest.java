package com.diamante.orderingsystem.repository.order;

import com.diamante.orderingsystem.TestDatabaseSetup;
import com.diamante.orderingsystem.entity.*;
import com.diamante.orderingsystem.repository.customer.CustomerRepository;
import com.diamante.orderingsystem.repository.product.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class OrderRepositoryTest extends TestDatabaseSetup {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    // region test variables
    private Order order1;
    private Order order2;

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
        setUpCustomersAndProducts();
        addOrdersToDatabase();
    }

    @After
    public void tearDown() throws Exception {
        try {
            orderRepository.deleteAll();
        } catch (Exception ex) { }
    }

    @Test
    public void save_savesOrderToDatabase() {
        Order order3 = saveThirdOrder();

        Order actualOrder = testEntityManager.find(Order.class, 3L);
        assertThat(actualOrder).isEqualToComparingFieldByField(order3);
    }

    @Test
    public void findById_returnsCorrectOrder() {
        Optional<Order> actualOptionalOrder = orderRepository.findById(2L);
        assertThat(actualOptionalOrder.isPresent()).isTrue();

        assertThat(actualOptionalOrder.get()).isEqualToComparingFieldByField(order2);
    }

    @Test
    public void findOrdersByCustomer_CustomerId_returnsListOfOrdersForGivenCustomerId() {
        List<Order> actualOrderListForCustomer = orderRepository.findOrdersByCustomer_CustomerId(2L);

        assertThat(actualOrderListForCustomer.get(0)).isEqualToComparingFieldByField(order2);
    }

    @Test
    public void findOrdersByCustomer_returnsEmptyList_whenCustomerDoesNotExist() {
        List<Order> actualOrderListForCustomer = orderRepository.findOrdersByCustomer_CustomerId(5L);

        assertThat(actualOrderListForCustomer.isEmpty()).isTrue();
    }

    @Test
    public void findOrdersByOrderDateAfter_returnsListOfOrdersFilteredAfterGivenOrderDate() {
        Order order3 = saveThirdOrder();

        List<Order> actualOrdersAfterDate = orderRepository.findOrdersByCustomer_CustomerIdAndOrderDateAfter(2L, LocalDate.of(2018, 8, 2));

        assertThat(actualOrdersAfterDate.size()).isEqualTo(2);
        assertThat(actualOrdersAfterDate.get(0)).isEqualToComparingFieldByField(order2);
        assertThat(actualOrdersAfterDate.get(1)).isEqualToComparingFieldByField(order3);

    }

    @Test
    public void count_returnsCorrectNumberOfOrders() {
        assertThat(orderRepository.count()).isEqualTo(2);
    }

    @Test
    public void deleteById_deletesCorrectOrderFromDatabase() {
        Optional<Order> foundOrder = orderRepository.findById(1L);
        assertThat(foundOrder.isPresent()).isTrue();
        orderRepository.deleteById(1L);
        foundOrder = orderRepository.findById(1L);
        assertThat(foundOrder.isPresent()).isFalse();
    }

    @Test
    public void deleteAllByCustomer_CustomerId_removesAllOrdersForGivenCustomer() {
        // save additional order
        saveThirdOrder();

        // check that orders for customer 2 exist
        Optional<Order> order1ForCustomer2 = orderRepository.findById(2L);
        Optional<Order> order2ForCustomer2 = orderRepository.findById(3L);

        assertThat(order1ForCustomer2.isPresent()).isTrue();
        assertThat(order2ForCustomer2.isPresent()).isTrue();

        // delete orders for customer 2
        orderRepository.deleteAllByCustomer_CustomerId(2L);

        // assert that all orders were removed for customer 2
        order1ForCustomer2 = orderRepository.findById(2L);
        order2ForCustomer2 = orderRepository.findById(3L);

        assertThat(order1ForCustomer2.isPresent()).isFalse();
        assertThat(order2ForCustomer2.isPresent()).isFalse();
    }

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

        product5 = Product.builder()
                .productName("Black Jeans")
                .description("Skinny black jeans")
                .manufacturer("Levi")
                .category(Category.CLOTHING_SHOES_JEWELERY_WATCHES)
                .price(25.00)
                .build();

        product6 = Product.builder()
                .productName("Amazon Firestick")
                .description("Entertainment with multiple apps to choose from")
                .manufacturer("Amazon")
                .category(Category.ELECTRONICS)
                .price(299.99)
                .build();

        productSet1 = new HashSet<>(Arrays.asList(product1, product2));
        productSet2 = new HashSet<>(Arrays.asList(product3, product4));
        productSet3 = new HashSet<>(Arrays.asList(product5, product6));

        totalBalance1 = product1.getPrice() + product2.getPrice();
        totalBalance2 = product3.getPrice() + product4.getPrice();
        totalBalance3 = product5.getPrice() + product6.getPrice();
    }

    private void addOrdersToDatabase() {
        orderRepository.resetAllOrderIds();
        productRepository.resetAllProductIds();
        customerRepository.resetAllCustomerIds();

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

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);

        testEntityManager.persist(order1);
        testEntityManager.persist(order2);
    }

    private Order saveThirdOrder() {
        Order order3 = Order.builder()
                .customer(customer2)
                .products(productSet3)
                .orderDate(LocalDate.of(2019, 4, 10))
                .totalBalance(totalBalance3)
                .build();

        orderRepository.save(order3);
        return order3;
    }
}
