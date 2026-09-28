package com.example.backend.repository;

import com.example.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepsitory extends JpaRepository<Task, Long> {
}
