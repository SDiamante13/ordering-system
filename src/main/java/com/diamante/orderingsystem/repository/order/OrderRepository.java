package com.diamante.orderingsystem.repository.order;

import com.diamante.orderingsystem.entity.Customer;
import com.diamante.orderingsystem.entity.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findOrdersByCustomer(Customer customer);

//    List<Order> findOrderByCustomer_CustomerId(Long id);
    List<Order> findOrderByOrderDateBefore(Date date);

    void deleteAllByCustomer(Customer customer);

    Order findOrderByCustomerAndOrderId(Customer customer, Long id);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE orders_order_id_seq RESTART WITH 1", nativeQuery = true)
    void resetAllOrderIds();
}
