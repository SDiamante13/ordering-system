package com.diamante.orderingsystem.controller.order;

import com.diamante.orderingsystem.service.order.OrderService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Api(value = "Orders API", description = "Store new orders containing products purchased by existing customers.")
public class OrderController {
    //TODO then write the tests for controller
    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }


}
