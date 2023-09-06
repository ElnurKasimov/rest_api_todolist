package com.softserve.itacademy.todolist.controller;
import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.service.ToDoService;
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
@RequestMapping("/api/users/{owner_id}/todos")
public class TodoController {
    private final ToDoService toDoService;

    @GetMapping
    List<ToDoResponseGetAll> getAllTodosOfUser(@PathVariable(name = "owner_id") long userId) {
        return toDoService.getByUserId(userId).stream()
                .map(ToDoTransformer::fromEntity)
                .toList();
    }
    @GetMapping("/{id}")
    ToDoResponseGetOne getUser(@PathVariable(name = "owner_id") long userId, @PathVariable(name="id") long id) {
        log.info("CONTROLLER GET /API/USERS/" + userId + "/" + id);
        return new ToDoResponseGetOne(toDoService.readById(id));
    }

    @PostMapping("/")
    ResponseEntity<Void> createToDo(@PathVariable(name = "owner_id") long userId, @RequestBody ToDoRequest toDoRequest) {
        log.info("CONTROLLER POST /API/USERS/" + userId + "/TODOS");
        ToDo newToDo = toDoService.create(ToDoTransformer.toEntity(toDoRequest));
        System.out.println("newToDo = " + newToDo);
        return ResponseEntity.status(HttpStatus.CREATED)
            .header("Location", "/api/users/" + userId + "/todos/" + newToDo.getId() )
            .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #owner_id")
    ResponseEntity<Void>  updateUser(@PathVariable(name = "owner_id") long userId,
                                     @PathVariable(name = "id") long id,
                                     @RequestBody ToDoRequest toDoRequest) {
        log.info("CONTROLLER PUT /API/USERS/" + userId + "todos/" + id);
        ToDo fromDb = toDoService.readById(id);
        fromDb.setTitle(toDoRequest.getTitle());
        fromDb.setCreatedAt(toDoRequest.getCreatedAt());
        toDoService.create(fromDb);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/users/" + userId + "todos/" + fromDb.getId())
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #owner_id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable(name = "owner_id") long userId,
                    @PathVariable(name = "id") long id) {
        log.info("CONTROLLER DELETE /API/USERS/" + userId + "todos/" + id);
        toDoService.delete(id);
    }

}
