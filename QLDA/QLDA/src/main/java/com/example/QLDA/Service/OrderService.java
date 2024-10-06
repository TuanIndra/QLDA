package com.example.QLDA.Service;

import com.example.QLDA.entity.Order;
import com.example.QLDA.entity.User;
import com.example.QLDA.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Get all orders for a specific user
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    // Get an active (pending) order for a user (cart functionality)
    public Order getActiveOrder(User user) {
        return orderRepository.findByUserAndStatus(user, "PENDING");
    }

    // Save or update an order
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get a specific order by its ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    // Get all orders (Admin functionality)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Delete an order by ID
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
