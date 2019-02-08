package com.diamante.orderingsystem.service;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.PaymentInfo;
import com.diamante.orderingsystem.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService = new CustomerServiceImpl(customerRepository);

    private Customer customer1 = Customer.builder()
            .customerId(1L)
            .firstName("Sam")
            .lastName("Adams")
            .paymentInfo(PaymentInfo.builder().build())
            .build();

    @Test
    public void findAllCustomers_shouldReturnAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1));

        List<Customer> customers = customerService.findAllCustomers();

        assertThat(customers.get(0)).isEqualTo(customer1);
    }

    @Test
    public void findById_shouldReturnCorrectCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        Customer actualCustomer = customerService.findByCustomerId(1L);
        assertThat(actualCustomer).isEqualToComparingFieldByField(customer1);
    }

    @Test
    public void findByLastName_shouldReturnCorrectCustomer() {
        when(customerRepository.findByLastName(anyString())).thenReturn(Optional.of(customer1));

        Customer actualCustomer = customerService.findByLastName("Adams");
        assertThat(actualCustomer).isEqualToComparingFieldByField(customer1);

    }

    @Test
    public void saveCustomer_storesTheCustomerIntoDatabase() {
        when(customerRepository.save(any())).thenReturn(customer1);

        Customer saveCustomer = customerService.saveCustomer(customer1);
        assertThat(saveCustomer).isEqualToComparingFieldByField(customer1);
    }

    @Test
    public void updateCustomer_updatesExistingCustomer() {
        Customer customerToBeUpdated = Customer.builder()
                .customerId(1L)
                .firstName("Sam")
                .lastName("Adams")
                .paymentInfo(PaymentInfo.builder()
                        .cardNumber("4372762")
                        .expirationDate("7/12")
                        .securityCode("345")
                        .zipCode("87262")
                        .build())
                .build();

        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.of(customer1));
        when(customerRepository.save(any())).thenReturn(customerToBeUpdated);

        Customer actualUpdatedCustomer = customerService.updateCustomer(customerToBeUpdated);

        assertThat(actualUpdatedCustomer)
                .isEqualToComparingFieldByField(customerToBeUpdated);
    }

    @Test
    public void deleteCustomer_deletesExistingCustomer() {
        doNothing().when(customerRepository).deleteById(anyLong());

        customerService.deleteCustomerById(1L);

        verify(customerRepository).deleteById(anyLong());
    }
}