package com.diamante.orderingsystem.controller;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.PaymentInfo;
import com.diamante.orderingsystem.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    private Customer customer1 = Customer.builder()
            .customerId(1L)
            .firstName("Tom")
            .lastName("Green")
            .paymentInfo(PaymentInfo.builder().build())
            .build();

    private Customer customer2 = Customer.builder()
            .customerId(1L)
            .firstName("Shiela")
            .lastName("Johnson")
            .paymentInfo(PaymentInfo.builder().build())
            .build();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getAllCustomers_returnsListOfCustomers() throws Exception {
        when(customerService.findAllCustomers())
                .thenReturn(Arrays.asList(customer1, customer2));

        mockMvc.perform(get("/customer")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is(customer1.getFirstName())));
    }
}