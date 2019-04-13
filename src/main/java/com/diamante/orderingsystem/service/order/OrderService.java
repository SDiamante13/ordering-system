package com.diamante.orderingsystem.service.order;

import com.diamante.orderingsystem.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);

    Order findByOrderId(Long orderId);

    List<Order> findAllOrdersForCustomer(Long customerId);

    List<Order> findAllOrdersForCustomerAfterOrderDate(Long customerId, LocalDate orderDate);

    Order updateOrder(Order updatedOrder);

    void deleteAllOrdersForCustomer(Long customerId);

    void deleteOrderById(Long orderId);

    void resetAllOrderIds();
}
