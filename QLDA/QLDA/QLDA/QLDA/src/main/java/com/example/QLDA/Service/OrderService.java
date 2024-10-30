package com.example.QLDA.Service;

import com.example.QLDA.entity.Order;
import com.example.QLDA.entity.OrderDetail;
import com.example.QLDA.entity.User;
import com.example.QLDA.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Tạo đơn hàng mới
    public Order createOrder(User user, List<OrderDetail> orderDetails, String status) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(status);
        order.setOrderDetails(orderDetails);

        // Thiết lập mối quan hệ hai chiều
        orderDetails.forEach(detail -> detail.setOrder(order));

        return orderRepository.save(order);
    }

    // Lấy danh sách đơn hàng của người dùng
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    // Lấy đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Cập nhật trạng thái đơn hàng
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Xử lý thanh toán
    public Order processPayment(Long orderId, String paymentStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if ("Paid".equals(paymentStatus)) {
            order.setStatus("Completed");
        }
        return orderRepository.save(order);
    }
}
