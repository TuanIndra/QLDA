package com.example.QLDA.Repository;

import com.example.QLDA.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Phương thức tùy chỉnh để tìm Category theo id
    @Query("SELECT c FROM Category c WHERE c.id = :id")
    Category findCategoryById(@Param("id") Long id);

    // Tìm kiếm danh mục theo tên
    Category findByName(String name);
}
