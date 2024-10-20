package com.example.QLDA.Service;

import com.example.QLDA.Repository.CartItemRepository;
import com.example.QLDA.Repository.OrderRepository;
import com.example.QLDA.Repository.ProductRepository;
import com.example.QLDA.entity.Order;
import com.example.QLDA.entity.OrderDetail;
import com.example.QLDA.entity.Product;
import com.example.QLDA.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Thêm sản phẩm vào giỏ hàng
    public void addProductToCart(User user, Product product, int quantity) {
        // Kiểm tra xem người dùng đã có đơn hàng PENDING chưa
        Order order = getActiveOrder(user);
        if (order == null) {
            // Nếu chưa, tạo đơn hàng mới
            order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("PENDING");
            order.setOrderDetails(new ArrayList<>());
        }

        // Kiểm tra xem sản phẩm đã có trong OrderDetail chưa
        Optional<OrderDetail> existingOrderDetail = order.getOrderDetails().stream()
                .filter(od -> od.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingOrderDetail.isPresent()) {
            // Nếu có, cập nhật số lượng và giá
            OrderDetail orderDetail = existingOrderDetail.get();
            orderDetail.setQuantity(orderDetail.getQuantity() + quantity);
            orderDetail.setPrice(orderDetail.getQuantity() * product.getPrice());
        } else {
            // Nếu chưa, thêm OrderDetail mới
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(product.getPrice() * quantity);
            order.getOrderDetails().add(orderDetail);
        }

        // Lưu đơn hàng
        saveOrder(order);
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public void updateProductQuantity(User user, Long productId, int quantity) {
        Order order = getActiveOrder(user);
        if (order != null) {
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (orderDetail.getProduct().getId().equals(productId)) {
                    orderDetail.setQuantity(quantity);
                    orderDetail.setPrice(orderDetail.getProduct().getPrice() * quantity);
                    break;
                }
            }
            // Lưu đơn hàng
            saveOrder(order);
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeProductFromCart(User user, Long productId) {
        Order order = getActiveOrder(user);
        if (order != null) {
            order.getOrderDetails().removeIf(orderDetail -> orderDetail.getProduct().getId().equals(productId));

            // Kiểm tra nếu giỏ hàng trống, xóa đơn hàng
            if (order.getOrderDetails().isEmpty()) {
                deleteOrder(order.getId());
            } else {
                // Lưu đơn hàng
                saveOrder(order);
            }
        }
    }

    // Xóa toàn bộ giỏ hàng
    public void clearCart(User user) {
        Order order = getActiveOrder(user);
        if (order != null) {
            order.getOrderDetails().clear();
            orderRepository.save(order);
        }
    }
    public void clearCart1(User user) {
        cartItemRepository.deleteByOrderUser(user); // Xóa qua Order
    }
    // Tính tổng giá
    public double calculateTotalPrice(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                .sum();
    }

    // Tính tổng số tiền của giỏ hàng
    public double getTotalPrice(User user) {
        Order order = getActiveOrder(user);
        if (order != null) {
            return order.getOrderDetails().stream()
                    .mapToDouble(OrderDetail::getPrice)
                    .sum();
        }
        return 0.0;
    }

    // Lấy tất cả đơn hàng cho một người dùng cụ thể
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    // Lấy một đơn hàng active (pending) cho một người dùng
    public Order getActiveOrder(User user) {
        return orderRepository.findByUserAndStatus(user, "PENDING");
    }

    // Lưu hoặc cập nhật một đơn hàng
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Lấy danh sách OrderDetail trong giỏ hàng
    public List<OrderDetail> getCartItems(User user) {
        return cartItemRepository.findByOrderUser(user);
    }

    // Lấy một đơn hàng cụ thể theo ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    // Lấy tất cả các đơn hàng (chức năng Admin)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Xóa một đơn hàng theo ID
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
