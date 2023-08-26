package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.model.Priority;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
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
@RequestMapping("/api/users")
public class TaskController {
    private final TaskService taskService;
    private final ToDoService toDoService;
    private final StateService stateService;

    @GetMapping("/{uId}/todos/{tId}/tasks")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #uId and @task.ownerOrCollaborator(principal, #tId)")
    List<TaskResponse> getAllTasks(@PathVariable long uId, @PathVariable long tId){
        return taskService.getByTodoId(tId).stream().map(TaskTransformer::fromEntity).toList();
    }

    @GetMapping("/{uId}/todos/{tId}/tasks/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #uId and @task.ownerOrCollaborator(principal, #tId)")
    TaskResponse getTask(@PathVariable long uId, @PathVariable long tId, @PathVariable long id){
        return new TaskResponse(taskService.readById(id));
    }

    @PostMapping("/{uId}/todos/{tId}/tasks")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #uId and @task.ownerOrCollaborator(principal, #tId)")
    ResponseEntity<Void> createTask(@PathVariable long uId, @PathVariable long tId, @RequestBody TaskRequest taskRequest){
        log.info("CONTROLLER POST /API/USERS/" + uId + "/TODOS/" + tId + "/TASKS");
        Task newTask = TaskTransformer.toEntity(taskRequest);
        newTask.setState(stateService.readById(taskRequest.getStateId()));
        newTask.setTodo(toDoService.readById(taskRequest.getTodoId()));
        taskService.create(newTask);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/users/" + uId + "/todos/" + tId + "/tasks/" + newTask.getId())
                .build();
    }

    @PutMapping("/{uId}/todos/{tId}/tasks/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #uId and @task.ownerOrCollaborator(principal, #tId)")
    ResponseEntity<Void> updateTask(@PathVariable long uId, @PathVariable long tId, @PathVariable long id, @RequestBody TaskRequest taskRequest) {
        log.info("CONTROLLER PUT /API/USERS/" + uId + "/TODOS/" + tId + "/TASKS/" + id);
        Task fromDb = taskService.readById(id);
        if (toDoService.readById(tId).getOwner().getId() == uId) {
            fromDb.setName(taskRequest.getName());
            fromDb.setPriority(Priority.valueOf(taskRequest.getPriority()));
        }
        fromDb.setState(stateService.readById(taskRequest.getStateId()));
        taskService.update(fromDb);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/users/" + uId + "/todos/" + tId + "/tasks/" + fromDb.getId())
                .build();
    }

    @DeleteMapping("/{uId}/todos/{tId}/tasks/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #uId and @task.ownerOrCollaborator(principal, #tId)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTask(@PathVariable long uId, @PathVariable long tId, @PathVariable long id) {
        log.info("CONTROLLER DELETE /API/USERS/" + uId + "/TODOS/" + tId + "/TASKS/" + id);
        taskService.delete(id);
    }
}
