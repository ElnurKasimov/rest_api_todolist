package com.softserve.itacademy.todolist.dto;

import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;


public class ToDoTransformer {

    public static ToDo toEntity(ToDoRequest toDoRequest) {
        ToDo toDo = new ToDo();
        toDo.setTitle(toDoRequest.getTitle());
        toDo.setCreatedAt(toDoRequest.getCreatedAt());
        return toDo;
    }

    public static ToDoResponseGetAll fromEntity(ToDo toDo) {
        return new ToDoResponseGetAll(toDo);
    }

    public static ToDoResponseGetAll fromEntityWithTasks(ToDo toDo) {
        return new ToDoResponseGetAll(toDo);
    }

}
