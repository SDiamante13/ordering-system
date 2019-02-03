package com.diamante.orderingsystem.service;

import com.diamante.orderingsystem.entity.Customer;

public interface CustomerService {
    Customer save(Customer customer);
    Customer findById(Long id);
    Customer findByName(String name);
    Customer updateCustomer(Customer updatedCustomer);
    void deleteCustomerById(Long id);
}
