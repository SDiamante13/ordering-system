package com.diamante.orderingsystem.service.order;

import com.diamante.orderingsystem.entity.*;
import com.diamante.orderingsystem.repository.order.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataRetrievalFailureException;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void saveOrder_savesOrderToDatabase() {
        // Arrange
        when(orderRepository.save(any())).thenReturn(order1);

        // Act
        Order saveOrder = orderService.saveOrder(order1);

        // Assert
        assertThat(saveOrder).isEqualToComparingFieldByField(order1);

    }

    @Test
    public void findAllOrdersForCustomer_returnsListOfOrdersForGivenCustomer() {
        when(orderRepository.findOrdersByCustomer_CustomerId(1L)).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.findAllOrdersForCustomer(1L);
        assertThat(orders.size()).isEqualTo(2);
        assertThat(orders.get(0)).isEqualToComparingFieldByField(order1);
        assertThat(orders.get(1)).isEqualToComparingFieldByField(order2);
    }

    @Test
    public void findAllOrdersForCustomer_returnsNull_whenCustomerDoesNotExistInDatabase() {
        when(orderRepository.findOrdersByCustomer_CustomerId(2L)).thenReturn(Collections.emptyList());

        List<Order> orders = orderService.findAllOrdersForCustomer(2L);
        assertThat(orders.isEmpty()).isTrue();
    }

    @Test
    public void findAllOrdersForCustomerAfterOrderDate_returnsListOfOrdersFilteredByOrderDate() {
        when(orderRepository.findOrdersByCustomer_CustomerIdAndOrderDateAfter(any(), any())).thenReturn(Arrays.asList(order1, order3));

        List<Order> orders = orderService.findAllOrdersForCustomerAfterOrderDate(1L, LocalDate.of(2018, 5, 4));
        assertThat(orders.size()).isEqualTo(2);
        assertThat(orders.get(0)).isEqualToComparingFieldByField(order1);
        assertThat(orders.get(1)).isEqualToComparingFieldByField(order3);
    }

    @Test
    public void findByOrderId_returnsCorrectOrder() {
        when(orderRepository.findById(2L)).thenReturn(Optional.of(order2));

        Order actualOrder = orderService.findByOrderId(2L);

        assertThat(actualOrder).isEqualToComparingFieldByField(order2);
    }

    @Test
    public void updateOrder_updatesExistingOrderInTheDatabase_returnsUpdatedOrder() {
        Order updateOrder = Order.builder()
                .orderId(1L)
                .customer(customer1)
                .products(productSet2)
                .orderDate(LocalDate.of(2019, 10, 25))
                .totalBalance(totalBalance2)
                .build();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order1));
        when(orderRepository.save(any())).thenReturn(updateOrder);

        Order actualOrder = orderService.updateOrder(updateOrder);
        assertThat(actualOrder).isEqualTo(updateOrder);
    }

    @Test
    public void updateOrder_returnsNull_whenOrderIdDoesNotExistInDatabase() {
        Order updateOrder = Order.builder()
                .orderId(5L)
                .customer(customer1)
                .products(productSet2)
                .orderDate(LocalDate.of(2019, 10, 25))
                .totalBalance(totalBalance2)
                .build();
        when(orderRepository.findById(5L)).thenReturn(Optional.empty());

        Order actualOrder = orderService.updateOrder(updateOrder);

        assertThat(actualOrder).isNull();
    }

    @Test(expected = DataRetrievalFailureException.class)
    public void deleteOrderById_ThrowsDataRetrievalFailureException_whenOrderIdDoesNotExistInDatabase() {
        doThrow(new DataRetrievalFailureException("Nothing")).when(orderRepository).deleteById(5L);

        orderService.deleteOrderById(5L);

        verify(orderRepository).deleteById(5L);
    }

    @Test
    public void deleteOrderById_removesOrderFromDatabase() {
        doNothing().when(orderRepository).deleteById(anyLong());

        orderService.deleteOrderById(1L);

        verify(orderRepository).deleteById(eq(order1.getOrderId()));
    }

    @Test
    public void deleteAllOrdersForCustomer_removesAllOrdersFromDatabaseForTheGivenCustomer() {
        doNothing().when(orderRepository).deleteAllByCustomer_CustomerId(1L);

        orderService.deleteAllOrdersForCustomer(1L);

        verify(orderRepository).deleteAllByCustomer_CustomerId(eq(1L));
    }

    @Test
    public void resetAllOrderIds_callsResetAllOrderIds() {
        orderService.resetAllOrderIds();

        verify(orderRepository).resetAllOrderIds();
    }

    private void setUpTestStubs() {
        customer1 = Customer.builder()
                .customerId(1L)
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
                .customerId(2L)
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
                .productId(1L)
                .productName("Ipod")
                .description("Music player")
                .manufacturer("Apple")
                .category(Category.ELECTRONICS)
                .price(120.00)
                .build();

        product2 = Product.builder()
                .productId(2L)
                .productName("Merlin")
                .description("Novel about the adventures of King Arthur and a wizard named Merlin.")
                .manufacturer("Penguin Books")
                .category(Category.BOOKS)
                .price(6.99)
                .build();

        product3 = Product.builder()
                .productId(3L)
                .productName("Men's Black Watch")
                .description("A black watch")
                .manufacturer("IZOD")
                .category(Category.CLOTHING_SHOES_JEWELERY_WATCHES)
                .price(45.79)
                .build();

        product4 = Product.builder()
                .productId(4L)
                .productName("Flat Screen TV")
                .description("OLED High Definition TV")
                .manufacturer("Samsung")
                .category(Category.ELECTRONICS)
                .price(599.99)
                .build();

        product5 = Product.builder()
                .productId(5L)
                .productName("Refrigerator")
                .description("Black Refrigerator with water dispenser")
                .manufacturer("Black & Decker")
                .category(Category.HOME_LIVING)
                .price(499.99)
                .build();

        product6 = Product.builder()
                .productId(6L)
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
                .orderId(1L)
                .customer(customer1)
                .products(productSet1)
                .orderDate(LocalDate.of(2018, 6, 15))
                .totalBalance(totalBalance1)
                .build();

        order2 = Order.builder()
                .orderId(2L)
                .customer(customer1)
                .products(productSet2)
                .orderDate(LocalDate.of(2017, 2, 8))
                .totalBalance(totalBalance2)
                .build();

        order3 = Order.builder()
                .orderId(3L)
                .customer(customer1)
                .products(productSet3)
                .orderDate(LocalDate.of(2019, 1, 17))
                .totalBalance(totalBalance2)
                .build();
    }
}