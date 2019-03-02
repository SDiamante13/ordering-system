package com.diamante.orderingsystem.service.customer;

import com.diamante.orderingsystem.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    List<Customer> findAllCustomers();
    Customer findByCustomerId(Long id);
    Customer findByLastName(String name);
    Customer updateCustomer(Customer updatedCustomer);
    void deleteCustomerById(Long id);
    void deleteAllCustomers();
    void resetAllCustomerIds();
}
