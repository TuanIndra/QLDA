package com.example.QLDA.Controller;

import com.example.QLDA.entity.Order;
import com.example.QLDA.entity.OrderDetail;
import com.example.QLDA.entity.Product;
import com.example.QLDA.entity.User;
import com.example.QLDA.Service.OrderService;
import com.example.QLDA.Service.ProductService;
import com.example.QLDA.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    // Add a product to the cart
    @PostMapping("/add")
    public String addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);

        if (user == null) {
            return "User not found!";
        }
        if (product == null) {
            return "Product not found!";
        }

        // Check if the user already has a cart (an active order with "PENDING" status)
        Order order = orderService.getActiveOrder(user);

        if (order == null) {
            // If no active order, create a new one
            order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("PENDING");
            orderService.saveOrder(order);
        }

        // Add product to the order
        boolean productExistsInOrder = false;
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            if (orderDetail.getProduct().getId().equals(productId)) {
                orderDetail.setQuantity(orderDetail.getQuantity() + quantity); // Update quantity if product already in cart
                productExistsInOrder = true;
                break;
            }
        }

        if (!productExistsInOrder) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(product.getPrice());
            order.getOrderDetails().add(orderDetail);
        }

        orderService.saveOrder(order);

        return "Product added to cart successfully!";
    }

    // View the cart
    @GetMapping("/view")
    public List<OrderDetail> viewCart(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Order order = orderService.getActiveOrder(user);
        if (order == null) {
            throw new IllegalArgumentException("No active cart found for this user");
        }

        return order.getOrderDetails();
    }

    // Remove an item from the cart
    @DeleteMapping("/remove")
    public String removeFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return "User not found";
        }

        Order order = orderService.getActiveOrder(user);
        if (order == null) {
            return "No active cart found for this user";
        }

        List<OrderDetail> orderDetails = order.getOrderDetails();
        OrderDetail orderDetailToRemove = orderDetails.stream()
                .filter(orderDetail -> orderDetail.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (orderDetailToRemove != null) {
            orderDetails.remove(orderDetailToRemove);
            orderService.saveOrder(order);
            return "Product removed from cart successfully!";
        } else {
            return "Product not found in the cart";
        }
    }

    // Checkout and finalize the order
    @PostMapping("/checkout")
    public String checkout(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return "User not found";
        }

        Order order = orderService.getActiveOrder(user);
        if (order == null) {
            return "No active cart found for this user";
        }

        // Set the order status to "COMPLETED"
        order.setStatus("COMPLETED");
        orderService.saveOrder(order);

        return "Checkout successful!";
    }
}
