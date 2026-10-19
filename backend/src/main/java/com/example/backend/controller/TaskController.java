package com.example.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.example.backend.dto.TaskRequest;
import com.example.backend.dto.TaskResponse;
import com.example.backend.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000")
@SecurityRequirement(name = "bearerAuth")

public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/completed")
    public List<TaskResponse> getCompletedTask() {
        return taskService.getCompletedTasks();
    }

    @GetMapping("/incomplete")
    public List<TaskResponse> getInCompleteTask() {
        return taskService.getIncompleteTasks();
    }

    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody TaskRequest request
    ) {
        return taskService.createTask(request);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request
    ) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/search")
    public List<TaskResponse> searchTasks(
            @RequestParam String title
    ) {
        return taskService.searchByTitle(title);
    }

    @GetMapping("/completed/search")
    public List<TaskResponse> searchCompletedTasks(
            @RequestParam String title
    ) {
        return taskService.searchCompletedTasks(title);
    }

    @GetMapping("/sorted")
    public List<TaskResponse> getTasksSortedByTitle(
            @RequestParam String direction
    ) {
        return taskService.getTasksSortedByTitle(direction);
    }

    @GetMapping("/completed/custom")
    public List<TaskResponse> getCompletedTasksWithQuery() {
        return taskService.getCompletedTasksWithQuery();
    }

    @GetMapping("/page")
    public Page<TaskResponse> getTasksPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return taskService.getTasksPage(
                page,
                size,
                sortBy,
                direction
        );
    }

    @PostMapping("/user/{userId}")
    public TaskResponse createTaskForUser(
            @PathVariable Long userId,
            @Valid @RequestBody TaskRequest request
    ) {
        return taskService.createTaskForUser(userId, request);
    }
    @GetMapping("/user/{userId}")
    public List<TaskResponse> getTasksByUser(
        @PathVariable Long userId) {
    return taskService.getTasksByUser(userId);
    }
    @PostMapping("/me")
public TaskResponse createMyTask(
        @Valid @RequestBody TaskRequest request
) {
    return taskService.createTaskForCurrentUser(request);
    }
    @GetMapping("/me")
    public List<TaskResponse> getMyTasks() {
    return taskService.getCurrentUserTasks();
    }

}