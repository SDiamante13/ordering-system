package com.diamante.orderingsystem.controller.customer;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.service.customer.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.diamante.orderingsystem.utils.ExceptionUtils.createErrorMessageAndThrowEntityValidationException;

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
            throw new CustomerNotFoundException("Customer with last name " + lastName + " not found.");
        }
        return customer;
    }

    @ApiOperation(value = "Creates a new customer")
    @ApiResponse(code = 201, message = "Customer created")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Customer createCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            createErrorMessageAndThrowEntityValidationException(result);
        }

        return customerService.saveCustomer(customer);
    }

    @ApiOperation(value = "Updates the customer given correct id")
    @PutMapping
    public Customer updateCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            createErrorMessageAndThrowEntityValidationException(result);
        }

        Customer updatedCustomer;

        try {
            updatedCustomer = customerService.updateCustomer(customer);
        } catch (TransactionSystemException ex) {
            throw new TransactionSystemException("Customer was not updated. Error while committing the transaction.");
        }

        if (updatedCustomer == null) {
            throw new CustomerNotFoundException("Customer with id " + customer.getCustomerId() + " was not found.");
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
