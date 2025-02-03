package com.example.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@SpringBootApplication
public class TodoAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }
}

@Document(collection = "todos")
class Todo {
    @Id
    private String id;
    private String body;
    private boolean completed;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}

interface TodoRepository extends MongoRepository<Todo, String> {}

@RestController
@RequestMapping("/api/todos")
class TodoController {
    @Autowired
    private TodoRepository repository;

    @GetMapping
    public List<Todo> getTodos() {
        return repository.findAll();
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return repository.save(todo);
    }

    @PatchMapping("/{id}")
    public Todo updateTodo(@PathVariable String id) {
        Todo todo = repository.findById(id).orElseThrow();
        todo.setCompleted(true);
        return repository.save(todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        repository.deleteById(id);
    }
}