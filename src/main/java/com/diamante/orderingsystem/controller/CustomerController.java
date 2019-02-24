package com.diamante.orderingsystem.controller;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Api(value = "Customer and Payment API", description = "Store new customers and retrieve customer info of existing customers.")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @ApiOperation(value = "View a list of all customers")
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.findAllCustomers();
    }

    @ApiOperation(value = "Retrieves customer by last name")
    @GetMapping("/{lastName}")
    public Customer getCustomerByLastName(@PathVariable("lastName") String lastName) {
        Customer customer = customerService.findByLastName(lastName);
        if (customer == null) {
            // TODO once controller advice with exception handling is implemented change to CustomerNotFoundException
            throw new RuntimeException("Customer not found.");
        }
        return customer;
    }

    @ApiOperation(value = "Creates a new customer")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }

    @ApiOperation(value = "Updates the customer given correct id")
    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer){
        return customerService.updateCustomer(customer);
    }

    @ApiOperation(value = "Deletes the customer given an id")
    @ApiResponse(code = 204, message = "Customer deleted, no content.")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Long id){
        customerService.deleteCustomerById(id);
    }


}
