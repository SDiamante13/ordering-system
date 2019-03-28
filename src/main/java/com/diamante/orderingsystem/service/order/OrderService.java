package com.diamante.orderingsystem.service.order;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.Order;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);

    List<Order> findAllOrdersForCustomer(Customer customer);

    Order updateOrder(Order updatedOrder);

    void deleteOrderById();

    void deleteAllOrdersForCustomer(Customer customer);

    void resetAllOrderIds();
}
