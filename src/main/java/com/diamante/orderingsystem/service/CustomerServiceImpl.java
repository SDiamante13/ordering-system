package com.diamante.orderingsystem.service;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public List<Customer> findAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    public Customer findByCustomerId(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.isPresent() ? optionalCustomer.get() : null;
    }

    @Override
    public Customer findByLastName(String name) {
        Optional<Customer> optionalCustomer = customerRepository.findByLastName(name);
        return optionalCustomer.isPresent() ? optionalCustomer.get() : null;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer updatedCustomer) {
        Optional<Customer> originalCustomer = customerRepository.findById(updatedCustomer.getCustomerId());

        if (!originalCustomer.isPresent()){
            return null;
        }

        Customer customer = originalCustomer.get();
        customer.setFirstName(updatedCustomer.getFirstName());
        customer.setLastName(updatedCustomer.getLastName());
        customer.setPaymentInfo(updatedCustomer.getPaymentInfo());

        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }
}
