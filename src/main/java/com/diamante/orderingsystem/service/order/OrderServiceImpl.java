package com.diamante.orderingsystem.service.order;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.Order;
import com.diamante.orderingsystem.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

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
    public List<Order> findAllOrdersForCustomer(Customer customer) {
        List<Order> ordersByCustomer = orderRepository.findOrdersByCustomer(customer);
        if (ordersByCustomer.isEmpty()) {
            return null;
        }
        return ordersByCustomer;
    }

    @Override
    public Order updateOrder(Order updatedOrder) {
        Optional<Order> originalOrder = orderRepository.findById(updatedOrder.getOrderId());

        if(!originalOrder.isPresent()) {
            return null;
        }

        Order order = originalOrder.get();
        order.setCustomer(updatedOrder.getCustomer());
        order.setOrderDate(updatedOrder.getOrderDate());
        order.setProducts(updatedOrder.getProducts());
        order.setTotalBalance(updatedOrder.getTotalBalance());

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteAllOrdersForCustomer(Customer customer) {
        orderRepository.deleteAllByCustomer(customer);
    }

    @Override
    public void resetAllOrderIds() {
        orderRepository.resetAllOrderIds();
    }
}
