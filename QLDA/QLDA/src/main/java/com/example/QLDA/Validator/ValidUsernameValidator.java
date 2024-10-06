package com.example.QLDA.Validator;

import com.example.QLDA.Repository.UserRepository;
import com.example.QLDA.Validator.annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername,String> {
    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean isValid(String username,ConstraintValidatorContext context){
        if(userRepository ==null){
            return true;
        }
        return userRepository.findByUsername(username)==null;
    }
}
