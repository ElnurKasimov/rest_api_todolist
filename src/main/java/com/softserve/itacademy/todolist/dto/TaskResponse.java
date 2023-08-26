package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskResponse {
    private long id;
    private String name;
    private String priority;
    private long todoId;  // Assuming you want to include the todoId in the response
    private long stateId; // Assuming you want to include the stateId in the response

    public TaskResponse(Task task){
        id = task.getId();
        name = task.getName();
        priority = task.getPriority().name();
        todoId = task.getTodo().getId();
        stateId = task.getState().getId();
    }

}