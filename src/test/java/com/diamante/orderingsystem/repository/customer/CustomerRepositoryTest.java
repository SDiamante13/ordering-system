package com.diamante.orderingsystem.repository.customer;

import com.diamante.orderingsystem.TestDatabaseSetup;
import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.PaymentInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("default")
@DataJpaTest
public class CustomerRepositoryTest extends TestDatabaseSetup {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private PaymentInfo paymentInfo = PaymentInfo.builder()
            .cardNumber("1234574638")
            .expirationDate("11/23")
            .securityCode("873")
            .zipCode("50892")
            .build();

    private Customer customer1;
    private Customer customer2;
    private Customer customer3;


    @Before
    public void setUp() throws Exception {
        storeCustomersInDatabase();
    }

    @After
    public void tearDown() throws Exception {
        customerRepository.deleteAll();
    }

    @AfterClass
    public static void closeTestContainer() throws Exception {
        postgreSQLContainer.close();
    }

    @Test
    public void save_savesCustomerToDatabase() {
        Customer customer4 = Customer.builder()
                .firstName("Timmy")
                .lastName("Duncan")
                .build();
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
    public void findByLastName_returnsCorrectCustomer() {
        Optional<Customer> foundCustomer = customerRepository.findByLastName("Jefferies");
        assertThat(foundCustomer.isPresent()).isTrue();
        assertThat(foundCustomer.get()).isEqualToComparingFieldByField(customer2);
    }

    @Test
    public void findAll_returnListOfThreeCustomers() {
        List<Customer> expectedCustomerList = Arrays.asList(customer1, customer2, customer3);
        List<Customer> actualCustomerList = (List<Customer>) customerRepository.findAll();
        assertThat(actualCustomerList.size()).isEqualTo(3);
        assertThat(actualCustomerList.get(0)).isEqualToComparingFieldByField(expectedCustomerList.get(0));
    }

    @Test
    public void count_returnThreeCustomers() {
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

    private void storeCustomersInDatabase() {
        customerRepository.resetAllCustomerIds();

        customer1 = Customer.builder()
                .firstName("Paul")
                .lastName("Ryan")
                .paymentInfo(paymentInfo)
                .build();
        customer2 = Customer.builder()
                .firstName("Jim")
                .lastName("Jefferies")
                .paymentInfo(paymentInfo)
                .build();
        customer3 = Customer.builder()
                .firstName("Chintu")
                .lastName("Maddineni")
                .paymentInfo(paymentInfo)
                .build();

        testEntityManager.persist(customer1);
        testEntityManager.persist(customer2);
        testEntityManager.persist(customer3);
    }
}
