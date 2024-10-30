package com.example.QLDA.Repository;

import com.example.QLDA.entity.Category;
import com.example.QLDA.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tìm sản phẩm theo danh mục
    List<Product> findByCategoryId(Long categoryId);

    // Tìm sản phẩm theo tên chứa từ khóa (tìm kiếm)
    List<Product> findByNameContaining(String keyword);
    // Tìm kiếm sản phẩm theo tên với phân trang
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    // Lấy sản phẩm theo danh mục với phân trang
    Page<Product> findByCategory(Category category, Pageable pageable);

}
