package com.diamante.orderingsystem.repository.customer;

import com.diamante.orderingsystem.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByLastName(String lastName);
}
