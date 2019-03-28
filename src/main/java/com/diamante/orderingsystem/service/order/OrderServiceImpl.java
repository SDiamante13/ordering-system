package com.diamante.orderingsystem.service.order;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.Order;
import com.diamante.orderingsystem.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order saveOrder(Order order) {
        return null;
    }

    @Override
    public List<Order> findAllOrdersForCustomer(Customer customer) {
        return null;
    }

    @Override
    public Order updateOrder(Order updatedOrder) {
        return null;
    }

    @Override
    public void deleteOrderById() {

    }

    @Override
    public void deleteAllOrdersForCustomer(Customer customer) {

    }

    @Override
    public void resetAllOrderIds() {

    }
}
