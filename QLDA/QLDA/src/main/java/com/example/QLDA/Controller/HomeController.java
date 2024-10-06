package com.example.QLDA.Controller;

import com.example.QLDA.Service.ProductService;
import com.example.QLDA.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    // Home page that displays all products in a card layout
    @GetMapping("/home")
    public String showHomePage(Model model) {
        List<Product> products = productService.getAllProducts();  // Fetch all products from the service
        model.addAttribute("products", products);  // Add the list of products to the model
        return "home";  // Return the home.html view
    }
}
