package com.diamante.orderingsystem.repository;

import com.diamante.orderingsystem.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findCustomerByName(String name);
}
