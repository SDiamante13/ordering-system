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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService = new CustomerServiceImpl(customerRepository);

    private Customer customer1 = Customer.builder()
            .firstName("Sam")
            .lastName("Adams")
            .paymentInfo(PaymentInfo.builder().build())
            .build();

    @Test
    public void findById_shouldReturnCorrectCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        Customer actualCustomer = customerService.findByCustomerId(1L);
        assertThat(actualCustomer).isEqualToComparingFieldByField(customer1);
    }
}