package com.example.QLDA.Controller;

import com.example.QLDA.Service.CartService;
import com.example.QLDA.entity.Order;
import com.example.QLDA.entity.Product;
import com.example.QLDA.entity.User;
import com.example.QLDA.Service.ProductService;
import com.example.QLDA.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/gio-hang") // Đặt mapping gốc cho tất cả các phương thức trong controller này
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Hiển thị giỏ hàng
    @GetMapping
    public String viewCart(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Order order = cartService.getActiveOrder(user); // Lấy đơn hàng PENDING

        if (order != null) {
            model.addAttribute("cartItems", order.getOrderDetails());
            model.addAttribute("totalPrice", cartService.getTotalPrice(user));
        } else {
            model.addAttribute("cartItems", List.of());
            model.addAttribute("totalPrice", 0.0);
        }

        return "cart/cart"; // Trả về view 'cart.html'
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName());
        Product product = productService.getProductById(productId);

        if (product != null) {
            cartService.addProductToCart(user, product, quantity);
            redirectAttributes.addFlashAttribute("message", "Đã thêm sản phẩm vào giỏ hàng!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
        }

        return "redirect:/gio-hang";
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PostMapping("/update")
    public String updateCartItem(@RequestParam("productId") Long productId,
                                 @RequestParam("quantity") int quantity,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName());

        cartService.updateProductQuantity(user, productId, quantity);
        redirectAttributes.addFlashAttribute("message", "Đã cập nhật giỏ hàng!");

        return "redirect:/gio-hang";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("productId") Long productId,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName());

        cartService.removeProductFromCart(user, productId);
        redirectAttributes.addFlashAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng!");

        return "redirect:/gio-hang";
    }

    // Xóa toàn bộ giỏ hàng
    @GetMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        cartService.clearCart(user);
        redirectAttributes.addFlashAttribute("message", "Đã xóa toàn bộ giỏ hàng!");

        return "redirect:/gio-hang";
    }
}
