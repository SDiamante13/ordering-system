package com.diamante.orderingsystem.service.order;

import com.diamante.orderingsystem.entity.Order;
import com.diamante.orderingsystem.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order findByOrderId(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public List<Order> findAllOrdersForCustomer(Long customerId) {
        return orderRepository.findOrdersByCustomer_CustomerId(customerId);
    }

    @Override
    public List<Order> findAllOrdersForCustomerAfterOrderDate(Long customerId, LocalDate orderDate) {
        return orderRepository.findOrdersByCustomer_CustomerIdAndOrderDateAfter(customerId, orderDate);
    }

    @Override
    public Order updateOrder(Order updatedOrder) {
        Optional<Order> originalOrder = orderRepository.findById(updatedOrder.getOrderId());

        if(!originalOrder.isPresent()) {
            return null;
        }

        Order order = originalOrder.get();
        order.setCustomer(updatedOrder.getCustomer());
        order.setProducts(updatedOrder.getProducts());
        order.setOrderDate(updatedOrder.getOrderDate());
        order.setTotalBalance(updatedOrder.getTotalBalance());

        return orderRepository.save(order);
    }

    @Override
    public void deleteAllOrdersForCustomer(Long customerId) {
        orderRepository.deleteAllByCustomer_CustomerId(customerId);
    }

    @Override
    public void deleteOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public void resetAllOrderIds() {
        orderRepository.resetAllOrderIds();
    }
}
