package com.softserve.itacademy.todolist.dto;

import com.softserve.itacademy.todolist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


public class UserTransformer {

    public static User toEntity(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        return user;
    }

    public static UserResponse fromEntity(User user) {
        return new UserResponse(user);
    }
}
