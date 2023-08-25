package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.UserResponse;
import com.softserve.itacademy.todolist.exception.MethodArgumentNotValidException;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.RoleService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.beans.Encoder;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    List<UserResponse> getAllUsers() {
        return userService.getAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    UserResponse getUser(@PathVariable long id) {
        return new UserResponse(userService.readById(id));
    }

    @PostMapping("/")
    UserResponse  createUser(@RequestBody User user) {
        log.info("CONTROLLER POST /API/USERS");
        user.setRole(roleService.readById(2));
        User newUser = userService.create(user);
    return new UserResponse(newUser);
    }

}
