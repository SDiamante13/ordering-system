package com.diamante.orderingsystem.integration.customer;

import com.diamante.orderingsystem.OrderingSystemApplication;
import com.diamante.orderingsystem.TestDatabaseSetup;
import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.PaymentInfo;
import com.diamante.orderingsystem.service.customer.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderingSystemApplication.class)
public class CustomerControllerIntegrationTest extends TestDatabaseSetup {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    CustomerService customerService;

    private MockMvc mockMvc;

    private Customer customer4 = Customer.builder()
            .customerId(4L)
            .firstName("Shiela")
            .lastName("Johnson")
            .paymentInfo(PaymentInfo.builder().build())
            .build();

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        addCustomersToDatabase();
    }

    @After
    public void tearDown() throws Exception {
        customerService.deleteAllCustomers();
    }

    @Test
    public void getAllCustomers_returnsAllCustomers() throws Exception {
        mockMvc.perform(get("/customer"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Tom"))
                .andExpect(jsonPath("$[0].lastName").value("Green"))
                .andExpect(jsonPath("$[1].firstName").value("George"))
                .andExpect(jsonPath("$[1].lastName").value("Jefferson"))
                .andExpect(jsonPath("$[2].firstName").value("Frank"))
                .andExpect(jsonPath("$[2].lastName").value("Wright"));
    }

    @Test
    public void getCustomerByLastName_returnsSingleCustomer() throws Exception {
        mockMvc.perform(get("/customer/{lastName}", "Wright"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lastName").value("Wright"));
    }

    @Test
    public void getCustomerByLastName_returns404NotFoundCustomer() throws Exception {
        mockMvc.perform(get("/customer/{lastName}", "Srikar"))
                .andExpect(content().contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Customer with last name Srikar not found.")));
    }

    @Test
    public void createCustomer_CreatesCustomer_andReturns201() throws Exception {
        String json = mapper.writeValueAsString(customer4);
        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    public void deleteCustomerById_removesCustomerById_andReturns204() throws Exception {
        mockMvc.perform(delete("/customer/1"))
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
        mockMvc.perform(delete("/customer/9"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("There is no customer with id 9 in the database")));
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

    private void addCustomersToDatabase() {
        customerService.resetAllCustomerIds();

        Customer customer1 = Customer.builder()
                .customerId(1L)
                .firstName("Tom")
                .lastName("Green")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("4372762")
                        .expirationDate("7/21")
                        .securityCode("345")
                        .zipCode("87262")
                        .build())
                .build();

        Customer customer2 = Customer.builder()
                .customerId(2L)
                .firstName("George")
                .lastName("Jefferson")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("97244142")
                        .expirationDate("5/18")
                        .securityCode("782")
                        .zipCode("60443")
                        .build())
                .build();

        Customer customer3 = Customer.builder()
                .customerId(3L)
                .firstName("Frank")
                .lastName("Wright")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("37286611")
                        .expirationDate("7/23")
                        .securityCode("230")
                        .zipCode("30276")
                        .build())
                .build();

        customerService.saveCustomer(customer1);
        customerService.saveCustomer(customer2);
        customerService.saveCustomer(customer3);
    }
}
