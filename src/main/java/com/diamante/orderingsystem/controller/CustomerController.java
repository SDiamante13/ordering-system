package com.diamante.orderingsystem.controller;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
