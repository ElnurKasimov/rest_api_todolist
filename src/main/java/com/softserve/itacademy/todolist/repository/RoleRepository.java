package com.softserve.itacademy.todolist.repository;

import com.softserve.itacademy.todolist.model.Role;
import com.softserve.itacademy.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
