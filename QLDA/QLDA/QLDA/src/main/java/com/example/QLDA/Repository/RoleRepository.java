package com.example.QLDA.Repository;


import com.example.QLDA.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r.id FROM Role r WHERE r.name = ?1")
    Long getRoleIdByName (String roleName);
}
