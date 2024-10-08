package com.example.taskservice.service;

import com.example.taskservice.dto.TaskRequestDTO;
import com.example.taskservice.dto.TaskResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Mono<TaskResponseDTO> createTask(TaskRequestDTO taskRequestDTO);

    Mono<TaskResponseDTO> createTask(String userEmail, TaskRequestDTO taskRequestDTO);

    Mono<TaskResponseDTO> getTaskById(Long id);

    Flux<TaskResponseDTO> getAllTasks();

    Flux<TaskResponseDTO> getAllTasksByUserEmail(String userEmail);

    Mono<TaskResponseDTO> updateTask(Long id, TaskRequestDTO taskRequestDTO);

    Mono<Void> deleteTask(Long id);
}
