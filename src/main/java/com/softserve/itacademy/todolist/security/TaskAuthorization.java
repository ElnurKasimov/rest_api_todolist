package com.softserve.itacademy.todolist.security;

import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("task")
public class TaskAuthorization {
    private final ToDoService toDoService;
    public boolean ownerOrCollaborator(UserDetails principal, Long todoId) {
        ToDo toDo = toDoService.readById(todoId);
        if (toDo.getOwner().getUsername().equals(principal.getUsername()))
            return true;
        return toDo.getCollaborators().stream().anyMatch(user -> user.getUsername().equals(principal.getUsername()));
    }
}
