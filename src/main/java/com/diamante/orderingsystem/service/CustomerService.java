package com.diamante.orderingsystem.service;

import com.diamante.orderingsystem.entity.Customer;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    Customer findByCustomerId(Long id);
    Customer findByLastName(String name);
    Customer updateCustomer(Customer updatedCustomer);
    void deleteCustomerById(Long id);
}
