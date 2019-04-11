package com.diamante.orderingsystem.controller.order;

import com.diamante.orderingsystem.controller.product.ProductNotFoundException;
import com.diamante.orderingsystem.entity.Order;
import com.diamante.orderingsystem.service.customer.CustomerService;
import com.diamante.orderingsystem.service.order.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

import static com.diamante.orderingsystem.utils.ExceptionUtils.createErrorMessageAndThrowEntityValidationException;

@RestController
@RequestMapping("/order")
@Api(value = "Orders API", description = "Store new orders containing products purchased by existing customers.")
public class OrderController {
    //TODO then write the tests for controller
    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @ApiOperation(value = "View a list of all Orders")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAllOrders();
    }

//    @ApiOperation(value = "Retrieves orders by customer id", notes = "Optional Parameters: ?id=1")
//    @GetMapping("/list")
//    public List<Order> getOrderByCustomerId(@RequestParam(required = false) Long id) {
//        List<Order> orders = orderService.findAllOrdersForCustomer(id);
//         if (orders == null) {
//            throw new OrderNotFoundException("Orders with customer id "+id+" not found");
//        }
//        return orders;
//    }

    @ApiOperation(value = "Retrieves orders by customer id", notes = "Optional Parameters: ?id=1")
    @GetMapping("/list")
    public List<Order> getAllOrdersByCustomerId(@RequestParam(required = false) Long customerid) {
        List<Order> orders = orderService.findAllOrdersForCustomer(customerService.findByCustomerId(customerid));
        if (orders == null) {
            throw new OrderNotFoundException("Orders with customer id "+customerid+" not found");
        }
        return orders;
    }

    @ApiOperation(value = "Retrieves orders prior to dates")
    @GetMapping("/list")
    public List<Order> getAllOrdersBeforeDate(@RequestParam(required = false) Date date) {

        List<Order> orders = orderService.
    }

    @ApiOperation(value = "Updates the order given the correct id.")
    @PutMapping()
    public Order updateProduct(@Valid @RequestBody Order order, BindingResult result) {
        if (result.hasErrors()) {
            createErrorMessageAndThrowEntityValidationException(result);
        }

        Order updatedOrder;

        try {
            updatedOrder = orderService.updateOrder(order);
        } catch (TransactionSystemException ex) {
            throw new TransactionSystemException("Product was not updated. Error while committing the transaction.");
        }

        if (updatedOrder == null) {
            throw new ProductNotFoundException("Product with id " + order.getOrderId() + " was not found.");
        }

        return updatedOrder;
    }

    @ApiOperation(value = "Deletes the order when given an id")
    @ApiResponse(code = 204, message = "Order deleted, no content.")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {
        try {
            orderService.deleteOrderById(id);
        } catch (DataRetrievalFailureException ex) {
            throw new DataRetrievalFailureException("There is no order with id " + id + " in the database");
        }
    }


}
