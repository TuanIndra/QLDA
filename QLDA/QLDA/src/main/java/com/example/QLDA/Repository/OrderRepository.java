package com.example.QLDA.Repository;

import com.example.QLDA.entity.Order;
import com.example.QLDA.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Order findByUserAndStatus(User user, String status);
}
