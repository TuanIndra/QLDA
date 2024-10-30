package com.example.QLDA.Service;

import com.example.QLDA.entity.Category;
import com.example.QLDA.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy tất cả danh mục
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Lấy danh mục theo ID
    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);  // Nếu không tìm thấy trả về null
    }
    // Lấy danh sách danh mục với phân trang
    public Page<Category> getCategoriesPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        return categoryRepository.findAll(pageable);
    }
    // Lấy danh mục theo tên
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    // Thêm hoặc cập nhật danh mục
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Xóa danh mục
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // Cập nhật thông tin danh mục
    public Category updateCategory(Long id, Category updatedCategory) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setName(updatedCategory.getName());
            return categoryRepository.save(category);
        }
        return null;
    }
}
