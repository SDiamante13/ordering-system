package com.diamante.orderingsystem.repository.customer;

import com.diamante.orderingsystem.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByLastName(String lastName);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE customers_customer_id_seq RESTART WITH 1", nativeQuery = true)
    void resetAllCustomerIds();

    // TODO ALTER TABLE CUSTOMERS ALTER COLUMN CUSTOMER_ID RESTART WITH 1 for H2 Database
}
