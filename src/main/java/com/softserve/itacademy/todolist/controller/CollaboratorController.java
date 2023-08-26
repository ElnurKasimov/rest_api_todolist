package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class CollaboratorController {
    private final UserService userService;
    private final ToDoService toDoService;

    @GetMapping("/{uId}/todos/{tId}/collaborators")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #uId and @task.ownerOrCollaborator(principal, #tId)")
    List<UserResponse> getAllCollaborators(@PathVariable long uId, @PathVariable long tId){
        return toDoService.readById(tId).getCollaborators().stream()
                .map(UserTransformer::fromEntity)
                .toList();
    }

    @GetMapping("/{uId}/todos/{tId}/collaborators/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #uId and @task.ownerOrCollaborator(principal, #tId)")
    UserResponse getCollaborator(@PathVariable long uId, @PathVariable long tId, @PathVariable long id){
        return toDoService.readById(tId).getCollaborators().stream()
                .filter(user -> user.getId() == id)
                .map(UserTransformer::fromEntity)
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/{uId}/todos/{tId}/collaborators/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #uId and @task.ownerOrCollaborator(principal, #tId)")
    ResponseEntity<Void> addCollaborator(@PathVariable long uId, @PathVariable long tId, @PathVariable long id){
        log.info("CONTROLLER POST /API/USERS/" + uId + "/TODOS/" + tId + "/COLLABORATORS");
        User collaborator = userService.readById(id);
        ToDo toDo = toDoService.readById(tId);
        collaborator.getOtherTodos().add(toDo);
        userService.update(collaborator);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/users/" + uId + "/todos/" + tId + "/collaborators/" + collaborator.getId())
                .build();
    }

    @DeleteMapping("/{uId}/todos/{tId}/collaborators/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #uId and @task.ownerOrCollaborator(principal, #tId)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeCollaborator(@PathVariable long uId, @PathVariable long tId, @PathVariable long id) {
        log.info("CONTROLLER DELETE /API/USERS/" + uId + "/TODOS/" + tId + "/COLLABORATORS/" + id);
        User collaborator = userService.readById(id);
        ToDo toDo = toDoService.readById(tId);
        collaborator.getOtherTodos().remove(toDo);
        userService.update(collaborator);
    }
}
