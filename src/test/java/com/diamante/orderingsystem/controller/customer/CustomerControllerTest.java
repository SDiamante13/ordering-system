package com.diamante.orderingsystem.controller.customer;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.PaymentInfo;
import com.diamante.orderingsystem.service.customer.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.TransactionSystemException;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
@ActiveProfiles("default")
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Autowired
    private ObjectMapper mapper;

    private Customer customer1 = Customer.builder()
            .firstName("Tom")
            .lastName("Green")
            .paymentInfo(PaymentInfo.builder().build())
            .build();

    private Customer customer2 = Customer.builder()
            .firstName("Shiela")
            .lastName("Johnson")
            .paymentInfo(PaymentInfo.builder().build())
            .build();

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
                .andExpect(jsonPath("$.message",
                        is("Customer with last name Thomas not found.")));    }

    @Test
    public void createCustomer_returns201ForProperInputs() throws Exception {
        when(customerService.saveCustomer(customer2)).thenReturn(customer2);

        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer2)))
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
        .content(mapper.writeValueAsString(customerToBeUpdated)))
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

    @Test
    public void updateCustomerForNonExistentId_returnsErrorMessage() throws Exception {
        Customer customerWithWrongId = Customer.builder()
                .customerId(8L)
                .firstName("Tom")
                .lastName("Green")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("4372762")
                        .expirationDate("7/12")
                        .securityCode("345")
                        .zipCode("87262")
                        .build())
                .build();

        when(customerService.updateCustomer(any())).thenReturn(null);

        mockMvc.perform(put("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerWithWrongId)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Customer with id 8 was not found.")));
    }

    @Test
    public void deleteCustomer_returnsDataRetrievalFailureErrorMessage_whenThereIsNoCustomerToDelete() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(customerService).deleteCustomerById(9L);

        mockMvc.perform(delete("/customer/9"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("There is no customer with id 9 in the database")));
    }

    @Test
    public void createCustomerReturnsValidationErrorMessage_whenSecurityCodeDoesNotContain3Digits() throws Exception {
        Customer incorrectCustomer = Customer.builder()
                .firstName("Tom")
                .lastName("Glavine")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("4372762")
                        .expirationDate("7/12")
                        .securityCode("13")
                        .zipCode("87262")
                        .build())
                .build();

        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(incorrectCustomer)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field paymentInfo.securityCode with value 13 does not meet requirements. Your security code must be exactly 3 digits.")));
    }

    @Test
    public void createCustomerReturnsValidationErrorMessage_whenZipCodeDoesNotContain5Digits() throws Exception {
        Customer incorrectCustomer = Customer.builder()
                .firstName("Sean")
                .lastName("Paul")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("4372762")
                        .expirationDate("7/12")
                        .securityCode("134")
                        .zipCode("3827")
                        .build())
                .build();

        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(incorrectCustomer)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        is("The field paymentInfo.zipCode with value 3827 does not meet requirements. " +
                                "Your zip code must be exactly 5 digits.")));
    }

    @Test
    public void createCustomerReturnsValidationErrorMessage_whenLastNameIsNull() throws Exception {
        Customer incorrectCustomer = Customer.builder()
                .firstName("Phil")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("92726731")
                        .expirationDate("7/12")
                        .securityCode("921")
                        .zipCode("38270")
                        .build())
                .build();

        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(incorrectCustomer)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field lastName with value null does not meet requirements. " +
                        "Last name is a required field")));
    }

    @Test
    public void createCustomerReturnsValidationErrorMessage_whenFirstNameIsNull() throws Exception {
        Customer incorrectCustomer = Customer.builder()
                .lastName("Collins")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("92726731")
                        .expirationDate("7/12")
                        .securityCode("921")
                        .zipCode("38270")
                        .build())
                .build();

        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(incorrectCustomer)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field firstName with value null does not meet requirements. " +
                        "First name is a required field")));
    }

    @Test
    public void updateCustomerReturnsValidationErrorMessage_whenSecurityCodeDoesNotContain3Digits() throws Exception {
        Customer incorrectCustomer = Customer.builder()
                .customerId(2L)
                .firstName("Shiela")
                .lastName("Johnson")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("4372762")
                        .expirationDate("7/12")
                        .securityCode("13")
                        .zipCode("87262")
                        .build())
                .build();

        when(customerService.updateCustomer(any())).thenReturn(incorrectCustomer);

        mockMvc.perform(put("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(incorrectCustomer)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        is("The field paymentInfo.securityCode with value 13 does not meet requirements. " +
                        "Your security code must be exactly 3 digits.")));
    }

    @Test
    public void updateCustomerReturnsTransactionSystemException_dueToDatabaseTransactionIssue() throws Exception {

        Customer customerToBeUpdated = Customer.builder()
                .customerId(1L)
                .firstName("Frank")
                .lastName("Jones")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("3782721")
                        .expirationDate("3/22")
                        .securityCode("51")
                        .zipCode("1263")
                        .build())
                .build();

        when(customerService.updateCustomer(any())).thenThrow(TransactionSystemException.class);

        mockMvc.perform(put("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerToBeUpdated)))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message",
                        is("Customer was not updated. Error while committing the transaction.")));
    }

    @Test
    public void createCustomerReturnsGenericErrorMessage_whenAnyBroadExceptionIsThrown() throws Exception {
        Customer customer = Customer.builder()
                .firstName("Tom")
                .lastName("Glavine")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("4372762")
                        .expirationDate("7/12")
                        .securityCode("130")
                        .zipCode("87262")
                        .build())
                .build();

        when(customerService.saveCustomer(customer)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message",
                        is("Your request could not be made. Please try again.")));
    }
}