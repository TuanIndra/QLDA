package com.example.QLDA.Service;

import com.example.QLDA.Repository.RoleRepository;
import com.example.QLDA.Repository.UserRepository;
import com.example.QLDA.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    public void save(User user){
        userRepository.save(user);
        Long userId = userRepository.getUserIdByUsername(user.getUsername());
        Long roleId = roleRepository.getRoleIdByName("USER");
        if(roleId!=0&&userId!=0){
            userRepository.addRoleToUser(userId,roleId);
        }
    }
}
