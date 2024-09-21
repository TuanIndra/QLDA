package com.example.WebBanHang.service;

import com.example.WebBanHang.model.products;
import com.example.WebBanHang.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<products> getAllProducts() {
        return productRepository.findAll();
    }

    public products saveProduct(products product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Optional<products> getProductById(Long id) {
        return productRepository.findById(id);}

    public products updateProduct(Long id, products updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            product.setBrand(updatedProduct.getBrand());
            return productRepository.save(product);
        }).orElseThrow(() -> new IllegalArgumentException("Product not found with id " + id));
    }
}

