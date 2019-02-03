package com.diamante.orderingsystem.service;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        return null;
    }

    @Override
    public Customer findById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.isPresent() ? optionalCustomer.get() : null;
    }

    @Override
    public Customer findByName(String name) {
        return null;
    }

    @Override
    public Customer updateCustomer(Customer updatedCustomer) {
        return null;
    }

    @Override
    public void deleteCustomerById(Long id) {

    }
}
