package com.example.QLDA.Service;

import com.example.QLDA.entity.Category;
import com.example.QLDA.entity.Product;
import com.example.QLDA.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryRepository;



    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);  // Return null if not found
    }

    // Get products by category
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // Search products by name
    public List<Product> searchProductsByName(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    // Add or update product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Delete product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Update product
    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setImageUrl(updatedProduct.getImageUrl());
            product.setCategory(updatedProduct.getCategory());
            return productRepository.save(product);
        }

        return null;
    }
    public Page<Product> getProductsPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);

        // Kiểm tra xem productPage có bị null không
        System.out.println("Service: ProductPage is null: " + (productPage == null));

        return productPage;
    }
    // Tìm kiếm sản phẩm theo tên với phân trang
    public Page<Product> searchProductsByName(String name, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }
    // Lấy sản phẩm theo danh mục với phân trang
    public Page<Product> getProductsByCategoryPage(Long categoryId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category != null) {
            return productRepository.findByCategory(category, pageable);
        } else {
            return Page.empty();
        }
    }
}
