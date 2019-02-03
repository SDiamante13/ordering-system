package com.diamante.orderingsystem.repository;

import com.diamante.orderingsystem.entity.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1 = new Customer("Paul");
    private Customer customer2 = new Customer("Jim");
    private Customer customer3 = new Customer("Chintu");


    @Before
    public void setUp() throws Exception {
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
    }

    @Test
    public void save_savesCustomerToDatabase() {
        Customer customer4 = new Customer("Timmy");
        Customer savedCustomer = customerRepository.save(customer4);
        assertThat(savedCustomer).isEqualToComparingFieldByField(customer4);
    }

    @Test
    public void findById_returnsCorrectCustomer() {
        Optional<Customer> foundCustomer = customerRepository.findById(3L);
        assertThat(foundCustomer.isPresent()).isTrue();
        assertThat(foundCustomer.get()).isEqualToComparingFieldByField(customer3);
    }

    @Test
    public void findAll_returnListofThreeCustomers() {
        List<Customer> expectedCustomerList = Arrays.asList(customer1,customer2,customer3);
        List<Customer> actualCustomerList = (List<Customer>) customerRepository.findAll();
        assertThat(actualCustomerList.size()).isEqualTo(3);
        assertThat(actualCustomerList.get(0)).isEqualToComparingFieldByField(expectedCustomerList.get(0));
    }

    @Test
    public void count_returnThree() {
        assertThat(customerRepository.count()).isEqualTo(3);
    }

    @Test
    public void deleteById_deletesCorrectCustomer() {
        Optional<Customer> foundCustomer = customerRepository.findById(2L);
        assertThat(foundCustomer.isPresent()).isTrue();
        customerRepository.deleteById(2L);
        foundCustomer = customerRepository.findById(2L);
        assertThat(foundCustomer.isPresent()).isFalse();
    }
}
