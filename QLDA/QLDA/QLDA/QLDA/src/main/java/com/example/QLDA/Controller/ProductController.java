package com.example.QLDA.Controller;

import com.example.QLDA.entity.Category;
import com.example.QLDA.Service.ProductService;
import com.example.QLDA.Service.CategoryService;
import com.example.QLDA.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Hiển thị danh sách sản phẩm cho trang admin
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();  // Lấy tất cả sản phẩm
        model.addAttribute("products", products);
        return "Product/products_list";  // Trả về view dành cho admin
    }



    // Hiển thị form thêm sản phẩm mới
    @GetMapping("/add")
    public String addProductForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();  // Lấy danh mục để hiển thị trong form
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());  // Tạo một đối tượng Product rỗng để binding với form
        return "Product/products_add";  // Trả về view 'products_add.html'
    }

    // Xử lý form thêm sản phẩm mới
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);  // Lưu sản phẩm mới vào cơ sở dữ liệu
        return "redirect:/products";  // Redirect về danh sách sản phẩm
    }

    // Hiển thị form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);  // Lấy thông tin sản phẩm theo ID
        if (product == null) {
            return "redirect:/products";  // Nếu không tìm thấy sản phẩm, quay lại trang danh sách
        }
        List<Category> categories = categoryService.getAllCategories();  // Lấy danh mục để hiển thị trong form
        model.addAttribute("product", product);  // Binding thông tin sản phẩm hiện có
        model.addAttribute("categories", categories);  // Thêm danh mục vào model để hiển thị trên form
        return "Product/products_edit";  // Trả về view 'products_edit.html'
    }

    // Xử lý form sửa sản phẩm
    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);  // Cập nhật thông tin sản phẩm theo ID
        return "redirect:/products";  // Redirect về danh sách sản phẩm sau khi cập nhật thành công
    }

    // Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);  // Xóa sản phẩm theo ID
        return "redirect:/products";  // Redirect về danh sách sản phẩm
    }

    // Tìm kiếm sản phẩm theo tên
    @GetMapping("/search")
    public String searchProducts(@RequestParam("name") String name, Model model) {
        List<Product> products = productService.searchProductsByName(name);  // Tìm kiếm sản phẩm theo tên
        model.addAttribute("products", products);
        return "Product/products_list";  // Trả về view 'products_list.html'
    }
}
