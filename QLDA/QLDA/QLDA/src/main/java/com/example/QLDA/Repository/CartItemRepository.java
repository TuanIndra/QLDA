package com.example.QLDA.Repository;

import com.example.QLDA.entity.OrderDetail;
import com.example.QLDA.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderUser(User user);
    void deleteByOrderUser(User user);
}
