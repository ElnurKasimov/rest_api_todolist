package com.softserve.itacademy.todolist.repository;

import com.softserve.itacademy.todolist.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    @Query(value = "select id, created_at, title, owner_id " +
            "from todos\n" +
            "    where owner_id = :userId\n" +
            "    union\n" +
            "select t.id, t.created_at, t.title, t.owner_id " +
            "from todos t inner join todo_collaborator tc\n" +
            "    on t.id = tc.todo_id and tc." +
            "collaborator_id = :userId ;", nativeQuery = true)
    List<ToDo> getByUserId(@Param("userId") long userId);
}
