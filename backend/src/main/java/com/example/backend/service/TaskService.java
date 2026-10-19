package com.example.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.TaskRequest;
import com.example.backend.dto.TaskResponse;
import com.example.backend.entity.Task;
import com.example.backend.entity.User;
import com.example.backend.exception.TaskNotFoundException;
import com.example.backend.repository.TaskRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    String email = authentication.getName();

    return userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found")
            );
}

    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ))
                .toList();
    }

    public TaskResponse createTask(TaskRequest request) {

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        Task savedTask = taskRepository.save(task);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.isCompleted()
        );
    }

    @Transactional
    public TaskResponse updateTask(
            Long id,
            TaskRequest request
    ) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found")
                );

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setCompleted(request.isCompleted());

        Task updatedTask = taskRepository.save(existingTask);

        return new TaskResponse(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.isCompleted()
        );
    }

    @Transactional
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found")
                );

        taskRepository.delete(task);
    }

    public List<TaskResponse> getCompletedTasks() {

        return taskRepository.findByCompleted(true)
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ))
                .toList();
    }

    public List<TaskResponse> getIncompleteTasks() {

        return taskRepository.findByCompleted(false)
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ))
                .toList();
    }

    public List<TaskResponse> searchByTitle(String title) {

        return taskRepository.findByTitleContaining(title)
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ))
                .toList();
    }

    public List<TaskResponse> searchCompletedTasks(String title) {

        return taskRepository
                .findByCompletedAndTitleContaining(true, title)
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ))
                .toList();
    }

    public List<TaskResponse> getTasksSortedByTitle(
            String direction
    ) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(
                    Sort.Direction.DESC,
                    "title"
            );
        } else {
            sort = Sort.by(
                    Sort.Direction.ASC,
                    "title"
            );
        }

        return taskRepository.findAll(sort)
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ))
                .toList();
    }

    public List<TaskResponse> getCompletedTasksWithQuery() {

        return taskRepository.findAllCompletedTasks()
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ))
                .toList();
    }

    public Page<TaskResponse> getTasksPage(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort.Direction sortDirection;

        if (direction.equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        } else {
            sortDirection = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return taskRepository.findAll(pageable)
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ));
    }

    public TaskResponse createTaskForUser(
            Long userId,
            TaskRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.isCompleted()
        );
    }

    public List<TaskResponse> getTasksByUser(Long userId){
            return taskRepository.findByUserId(userId)
            .stream()
            .map(task -> new TaskResponse(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.isCompleted()
            ))
            .toList();
    }
    public TaskResponse createTaskForCurrentUser(
        TaskRequest request
) {

    User user = getCurrentUser();

    Task task = new Task();

    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setCompleted(request.isCompleted());
    task.setUser(user);

    Task savedTask = taskRepository.save(task);

    return new TaskResponse(
            savedTask.getId(),
            savedTask.getTitle(),
            savedTask.getDescription(),
            savedTask.isCompleted()
    );
}
        public List<TaskResponse> getCurrentUserTasks() {

    User user = getCurrentUser();

    return taskRepository.findByUserId(user.getId())
            .stream()
            .map(task -> new TaskResponse(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.isCompleted()
            ))
            .toList();
}
}