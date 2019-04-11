package com.diamante.orderingsystem.service.order;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.Order;

import java.sql.Date;
import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);

    List<Order> findAllOrders();

    List<Order> findAllOrdersForCustomer(Customer customer);

    List<Order> findAllOrdersBeforeDate(Date date);

//    List<Order> findAllOrdersForCustomer(Long id);

    Order updateOrder(Order updatedOrder);

    void deleteAllOrders();

    void deleteOrderById(Long id);

    void deleteAllOrdersForCustomer(Customer customer);

    void resetAllOrderIds();
}
