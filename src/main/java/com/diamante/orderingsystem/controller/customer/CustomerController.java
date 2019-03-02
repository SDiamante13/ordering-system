package com.diamante.orderingsystem.controller;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
            throw new CustomerNotFoundException("Customer with last name, " + lastName + " not found.");
        }
        return customer;
    }

    @ApiOperation(value = "Creates a new customer")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Customer createCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMessage = "The field " + fieldError.getField() +
                    " with value " + fieldError.getRejectedValue() + " does not meet requirements. ";
            throw new IllegalArgumentException(errorMessage + fieldError.getDefaultMessage());
        }
        return customerService.saveCustomer(customer);
    }

    @ApiOperation(value = "Updates the customer given correct id")
    @PutMapping
    public Customer updateCustomer(@Valid @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(customer);

        if (updatedCustomer == null) {
            throw new CustomerNotFoundException("Customer with id: " + customer.getCustomerId() + " not found.");
        }
        return updatedCustomer;
    }

    @ApiOperation(value = "Deletes the customer given an id")
    @ApiResponse(code = 204, message = "Customer deleted, no content.")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        try {
            customerService.deleteCustomerById(id);
        } catch (DataRetrievalFailureException ex) {
            throw new DataRetrievalFailureException("There is no customer with id " + id + " in the database");
        }
    }


}
