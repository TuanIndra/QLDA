package com.example.QLDA.Controller;

import com.example.QLDA.Service.ProductService;
import com.example.QLDA.Service.CategoryService;
import com.example.QLDA.entity.Product;
import com.example.QLDA.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Trang chủ hiển thị danh sách sản phẩm với phân trang
    @GetMapping({"/", "/home"})
    public String showHomePage(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 9; // Số sản phẩm trên mỗi trang
        Page<Product> productPage = productService.getProductsPage(page, pageSize);
        // Lấy danh sách danh mục để hiển thị trong sidebar và navbar
        List<Category> categories = categoryService.getAllCategories();
        // Kiểm tra productPage
        System.out.println("ProductPage is null: " + (productPage == null));
        if (productPage != null) {
            System.out.println("ProductPage content size: " + productPage.getContent().size());
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("categories", categories);
        return "home";  // Trả về view 'home.html'
    }
    // Xử lý tìm kiếm sản phẩm theo tên
    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {
        int pageSize = 9;
        Page<Product> productPage = productService.searchProductsByName(keyword, page, pageSize);
        model.addAttribute("productPage", productPage);
        model.addAttribute("searchKeyword", keyword); // Để giữ lại từ khóa tìm kiếm trong view
        return "home"; // Trả về view 'home.html'
    }
    // Phương thức hiển thị sản phẩm theo danh mục
    @GetMapping("/category/{id}")
    public String showProductsByCategory(@PathVariable("id") Long categoryId,
                                         @RequestParam(defaultValue = "0") int page,
                                         Model model) {
        int pageSize = 9;
        Page<Product> productPage = productService.getProductsByCategoryPage(categoryId, page, pageSize);

        // Lấy danh sách danh mục để hiển thị trong sidebar
        List<Category> categories = categoryService.getAllCategories();

        // Lấy thông tin danh mục hiện tại
        Category currentCategory = categoryService.getCategoryById(categoryId);

        model.addAttribute("productPage", productPage);
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", currentCategory);
        return "home";
    }
    // Phương thức hiển thị chi tiết sản phẩm
    @GetMapping("/san-pham/{id}")
    public String showProductDetail(@PathVariable("id") Long productId, Model model) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            model.addAttribute("product", product);
            // Lấy danh sách danh mục để hiển thị trong sidebar
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "Product/product_detail"; // Đường dẫn đúng tới template
        } else {
            return "redirect:/"; // Nếu sản phẩm không tồn tại, chuyển hướng về trang chủ
        }
    }

}
