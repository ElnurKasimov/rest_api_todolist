package com.softserve.itacademy.todolist.dto;

import com.softserve.itacademy.todolist.model.*;

public class TaskTransformer {
    public static Task toEntity(TaskRequest taskRequest) {
        Task task = new Task();
        task.setName(taskRequest.getName());
        task.setPriority(Priority.valueOf(taskRequest.getPriority()));
        return task;
    }

    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(task);
    }
}
