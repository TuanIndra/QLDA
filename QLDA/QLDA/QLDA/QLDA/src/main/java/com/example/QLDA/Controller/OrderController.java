package com.example.QLDA.Controller;

import com.example.QLDA.Service.CartService;
import com.example.QLDA.Service.OrderService;
import com.example.QLDA.entity.Order;
import com.example.QLDA.entity.OrderDetail;
import com.example.QLDA.entity.User;
import com.example.QLDA.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/checkout")
public class OrderController {
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // Hiển thị trang thanh toán
    @GetMapping
    public String showCheckoutPage(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<OrderDetail> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            model.addAttribute("message", "Giỏ hàng của bạn trống.");
            return "redirect:/gio-hang";
        }

        double totalPrice = cartService.calculateTotalPrice(cartItems);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "order/checkout";
    }

    // Xử lý thanh toán
    @PostMapping
    public String processCheckout(@RequestParam("shippingAddress") String shippingAddress,
                                  @RequestParam("shippingCity") String shippingCity,
                                  @RequestParam("shippingState") String shippingState,
                                  @RequestParam("shippingZip") String shippingZip,
                                  @RequestParam("paymentMethod") String paymentMethod,
                                  Authentication authentication,
                                  Model model) {
        User user = userService.findByUsername(authentication.getName());
        List<OrderDetail> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            model.addAttribute("message", "Giỏ hàng của bạn trống.");
            return "redirect:/gio-hang";
        }

        // Tạo đơn hàng mới với trạng thái "Pending"
        Order order = orderService.createOrder(user, cartItems, "Pending");

        // Xử lý thanh toán (Giả sử thanh toán thành công)
        orderService.processPayment(order.getId(), "Paid");

        // Xóa giỏ hàng sau khi thanh toán thành công
        cartService.clearCart1(user);

        model.addAttribute("order", order);
        return "order/order"; // Tên file Thymeleaf: order.html
    }

}
