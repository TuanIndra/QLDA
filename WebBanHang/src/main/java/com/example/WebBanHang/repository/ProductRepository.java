package com.example.WebBanHang.repository;

import com.example.WebBanHang.model.products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<products, Long> {
}
