package com.diamante.orderingsystem.controller.customer;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.PaymentInfo;
import com.diamante.orderingsystem.service.customer.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    private JacksonTester<Customer> jsonCustomer;

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
        JacksonTester.initFields(this, new ObjectMapper());
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

    @Test
    public void getCustomerByLastName_returnsOkForFoundCustomer() throws Exception {
        when(customerService.findByLastName(anyString())).thenReturn(customer1);

        mockMvc.perform(get("/customer/Green")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",is(customer1.getFirstName())));
    }

    @Test
    public void getCustomerByLastName_returns404ForNotFoundCustomer() throws Exception {
        when(customerService.findByLastName(anyString())).thenReturn(null);
        mockMvc.perform(get("/customer/Thomas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is("customer with last name, Thomas not found.")));    }

    @Test
    public void createCustomer_returns201ForProperInputs() throws Exception {
        when(customerService.saveCustomer(customer2)).thenReturn(customer2);

        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonCustomer.write(customer2).getJson()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void updateCustomer_returns200AndUpdatedCustomer() throws Exception {
        Customer customerToBeUpdated = Customer.builder()
                .customerId(1L)
                .firstName("Tom")
                .lastName("Green")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("4372762")
                        .expirationDate("7/12")
                        .securityCode("345")
                        .zipCode("87262")
                        .build())
                .build();
        when(customerService.updateCustomer(any())).thenReturn(customerToBeUpdated);

        mockMvc.perform(put("/customer")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(jsonCustomer.write(customerToBeUpdated).getJson()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentInfo.securityCode",
                        is(customerToBeUpdated.getPaymentInfo().getSecurityCode())));
    }

    @Test
    public void deleteCustomer_removesCustomerFromDatabase_andReturns204() throws Exception {
        doNothing().when(customerService).deleteCustomerById(anyLong());

        mockMvc.perform(delete("/customer/2"))
                .andExpect(status().isNoContent());
    }

    // TODO write another put test for CustomerNotFoundException
    // TODO write another delete test for CustomerNotFoundException
}