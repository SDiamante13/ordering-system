package com.diamante.orderingsystem.service;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerServiceImplTest {

    @TestConfiguration
    static class CustomerServiceImplTestContextConfiguration {

        @Bean
        public CustomerService customerService() {
            return new CustomerServiceImpl();
        }
    }

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    private Customer customer1 = new Customer("Simon");

    @Test
    public void findById_shouldReturnCorrectCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        Customer actualCustomer = customerService.findById(1L);
        assertThat(actualCustomer).isEqualToComparingFieldByField(customer1);
    }
}