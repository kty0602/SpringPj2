package com.sparta.second.repository;

import com.sparta.second.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.taskId = :taskId AND t.deleteStatus = false")
    Optional<Task> getTaskByTaskId(@Param("taskId") Long taskId);

}
