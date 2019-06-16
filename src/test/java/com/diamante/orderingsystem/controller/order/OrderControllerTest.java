package com.diamante.orderingsystem.controller.order;

import com.diamante.orderingsystem.entity.*;
import com.diamante.orderingsystem.service.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Autowired
    private ObjectMapper mapper;

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
    public void createOrderForCustomer_savesTheOrderInTheDatabase_statusIsCreated() throws Exception {
        Order order4 = Order.builder()
                .customer(customer2)
                .products(new HashSet<>(Arrays.asList(product2, product3, product5)))
                .orderDate(LocalDate.of(2017, 6, 19))
                .totalBalance(product2.getPrice() +
                        product3.getPrice() +
                        product5.getPrice())
                .build();

        when(orderService.saveOrder(order4)).thenReturn(order4);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order4)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customer.firstName", is(order4.getCustomer().getFirstName())))
                .andExpect(jsonPath("$.totalBalance", is(order4.getTotalBalance())));
    }

    @Test
    public void createOrderReturnsErrorMessage_whenOrderIdIsIncluded() throws Exception {
        Order order4 = Order.builder()
                .orderId(4L)
                .customer(customer2)
                .products(new HashSet<>(Arrays.asList(product2, product3, product5)))
                .orderDate(LocalDate.of(2017, 6, 19))
                .totalBalance(product2.getPrice() +
                        product3.getPrice() +
                        product5.getPrice())
                .build();

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order4)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", is("Order id is auto generated. Please remove this field from your request.")));
    }

    @Test
    public void createOrderReturnsValidationErrorMessage_whenCustomerIsNull() throws Exception {
        Order order4 = Order.builder()
                .products(new HashSet<>(Arrays.asList(product2, product3, product5)))
                .orderDate(LocalDate.of(2017, 6, 19))
                .totalBalance(product2.getPrice() +
                        product3.getPrice() +
                        product5.getPrice())
                .build();

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order4)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field customer with value null does not meet requirements. " +
                        "Customer is a required field")));
    }

    @Test
    public void createOrderReturnsValidationErrorMessage_whenProductsIsNull() throws Exception {
        Order order4 = Order.builder()
                .customer(customer2)
                .orderDate(LocalDate.of(2017, 6, 19))
                .totalBalance(product2.getPrice() +
                        product3.getPrice() +
                        product5.getPrice())
                .build();

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order4)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field products with value null does not meet requirements. " +
                        "Products is a required field")));
    }

    @Test
    public void createOrderReturnsValidationErrorMessage_whenOrderDateIsNull() throws Exception {
        Order order4 = Order.builder()
                .customer(customer2)
                .products(new HashSet<>(Arrays.asList(product2, product3, product5)))
                .totalBalance(product2.getPrice() +
                        product3.getPrice() +
                        product5.getPrice())
                .build();

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order4)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field orderDate with value null does not meet requirements. " +
                        "Order date is a required field")));
    }

    @Test
    public void createOrderReturnsValidationErrorMessage_whenTotalBalanceIsNull() throws Exception {
        Order order4 = Order.builder()
                .customer(customer2)
                .products(new HashSet<>(Arrays.asList(product2, product3, product5)))
                .orderDate(LocalDate.of(2017, 6, 19))
                .build();

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order4)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field totalBalance with value 0.0 does not meet requirements." +
                        " Total balance must be at least $1")));
    }

    @Test
    public void getAllOrdersForCustomer_returnsListOfOrdersForCustomer_statusIsOK() throws Exception {
        when(orderService.findAllOrdersForCustomer(anyLong()))
                .thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/customer/1/order")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].totalBalance", is(totalBalance1)))
                .andExpect(jsonPath("$[1].totalBalance", is(totalBalance2)));
    }

    @Test
    @Ignore(value = "Flaky test")
    public void updateOrder_updatesTheOrderInTheDatabase_statusIsOk() throws Exception {
        Product newProduct = Product.builder()
                .productName("James Bond")
                .description("Adventures of 007")
                .manufacturer("Bond")
                .category(Category.BOOKS)
                .price(17.99)
                .build();

        productSet2.add(newProduct);

        Order updatedOrder = Order.builder()
                .orderId(2L)
                .customer(customer1)
                .products(productSet2)
                .orderDate(LocalDate.of(2017, 2, 8))
                .totalBalance(totalBalance2)
                .build();

        when(orderService.updateOrder(updatedOrder))
                .thenReturn(updatedOrder);

        mockMvc.perform(put("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedOrder)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].manufacturer", is("Samsung")))
                .andExpect(jsonPath("$.products[1].manufacturer", is("Bond")));
    }

    @Test // In this case either a customer does not exist or the customer has no orders.
    public void getAllOrdersForCustomer_whenCustomerDoesNotExist_returnsEmptyList_statusIsOk() throws Exception {
        when(orderService.findAllOrdersForCustomer(anyLong()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/customer/7/order")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getAllOrdersForCustomer_withOptionalDateParam_returnsListOfOrdersForCustomerFilteredByGivenDate_statusIsOK() throws Exception {
        when(orderService.findAllOrdersForCustomerAfterOrderDate(anyLong(), any()))
                .thenReturn(Collections.singletonList(order1));

        mockMvc.perform(get("/customer/1/order?date=2018-02-05")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].totalBalance", is(totalBalance1)));
    }

    @Test
    public void getAllOrdersForCustomer_withPoorlyFormattedOptionalDateParam_returnsTypeMismatchErrorMessage_statusIsBadRequest() throws Exception {
        when(orderService.findAllOrdersForCustomerAfterOrderDate(anyLong(), any()))
                .thenReturn(Collections.singletonList(order1));

        mockMvc.perform(get("/customer/1/order?date=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Optional Parameter, date, must be a properly formatted date. " +
                        "Example: /customer/1/order?date=2017-02-19")));
    }

    @Test
    public void updateOrder_forNonexistentId_returnsErrorMessage() throws Exception {
        Order updatedOrderWithIncorrectId = Order.builder()
                .orderId(8L)
                .customer(customer2)
                .products(new HashSet<>(Arrays.asList(product2, product3, product5)))
                .orderDate(LocalDate.of(2017, 6, 19))
                .totalBalance(product2.getPrice() +
                        product3.getPrice() +
                        product5.getPrice())
                .build();

        when(orderService.updateOrder(any(Order.class))).thenReturn(null);

        mockMvc.perform(put("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedOrderWithIncorrectId)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Order with id 8 was not found.")));
    }

    @Test
    public void deleteAllOrdersForCustomer_removesAllOrders_statusIsNoContent() throws Exception {
        doNothing().when(orderService).deleteAllOrdersForCustomer(anyLong());

        mockMvc.perform(delete("/customer/1/order"))
                .andExpect(status().isNoContent());

        verify(orderService).deleteAllOrdersForCustomer(eq(1L));
    }

    @Test
    public void deleteOrder_removesSingleOrder_statusIsNoContent() throws Exception {
        doNothing().when(orderService).deleteOrderById(anyLong());

        mockMvc.perform(delete("/order/4"))
                .andExpect(status().isNoContent());

        verify(orderService).deleteOrderById(eq(4L));
    }

    @Test
    public void deleteOrder_returnsDataRetrievalFailureErrorMessage_whenOrderIdDoesNotExist_statusIsNotFound() throws Exception {
        doThrow(new DataRetrievalFailureException("Not here!"))
                .when(orderService).deleteOrderById(anyLong());

        mockMvc.perform(delete("/order/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("There is no order with id 42 in the database")));
    }


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