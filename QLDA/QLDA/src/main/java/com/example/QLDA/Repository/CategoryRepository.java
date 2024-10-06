package com.example.QLDA.Repository;

import com.example.QLDA.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Tìm kiếm danh mục theo tên
    Category findByName(String name);
}
