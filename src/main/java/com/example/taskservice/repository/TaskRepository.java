package com.example.taskservice.repository;

import com.example.taskservice.entity.Task;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TaskRepository extends ReactiveCrudRepository<Task, Long> {

    Flux<Task> findByUserEmail(String userEmail);
}
