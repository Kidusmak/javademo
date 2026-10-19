package com.example.backend.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompleted(boolean completed);

    List<Task> findByTitleContaining(String title);

    List<Task> findByCompletedAndTitleContaining(boolean completed, String title);

    List<Task> findByUserId(Long userId);

    List<Task> findAll(Sort sort);
    
    @Query("Select t FROM Task t WHERE t.completed = true")
    List <Task> findAllCompletedTasks();

    @Query(
        """
                SELECT t
                FROM Task t
                WHERE t.completed = true
                AND t.title LIKE %:title%
                """
    )
    List <Task> searchCompletedTasks(@Param("title") String title);
    

}
