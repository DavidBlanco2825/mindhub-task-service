package com.example.taskservice.service.impl;

import com.example.taskservice.dto.TaskRequestDTO;
import com.example.taskservice.dto.TaskResponseDTO;
import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.taskservice.commons.Constants.TASK_NOT_FOUND_ID;

@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Mono<TaskResponseDTO> createTask(TaskRequestDTO taskRequestDTO) {
        log.info("Creating task with title: {}", taskRequestDTO.getTitle());
        Task task = taskMapper.toEntity(taskRequestDTO);
        return taskRepository.save(task)
                .doOnSuccess(savedTask -> log.debug("Task created successfully with ID: {}", savedTask.getId()))
                .map(taskMapper::toResponseDto)
                .doOnError(error -> log.error("Error occurred while creating task: {}", error.getMessage(), error));
    }

    @Override
    public Mono<TaskResponseDTO> getTaskById(Long id) {
        log.info("Fetching task with ID: {}", id);
        return taskRepository.findById(id)
                .map(taskMapper::toResponseDto)
                .doOnSuccess(task -> log.debug("Task retrieved successfully: {}", task))
                .switchIfEmpty(Mono.error(new TaskNotFoundException(TASK_NOT_FOUND_ID + id)))
                .doOnError(error -> log.error("Error occurred while fetching task: {}", error.getMessage(), error));
    }

    @Override
    public Flux<TaskResponseDTO> getAllTasks() {
        log.info("Fetching all tasks");
        return taskRepository.findAll()
                .map(taskMapper::toResponseDto)
                .doOnComplete(() -> log.debug("All tasks fetched successfully"))
                .doOnError(error -> log.error("Error occurred while fetching all tasks: {}", error.getMessage(), error));
    }

    @Override
    public Mono<TaskResponseDTO> updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        log.info("Updating task with ID: {}", id);
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(TASK_NOT_FOUND_ID + id)))
                .flatMap(existingTask -> updateExistingTask(existingTask, taskRequestDTO))
                .map(taskMapper::toResponseDto)
                .doOnSuccess(updatedTask -> log.debug("Task updated successfully with ID: {}", updatedTask.getId()))
                .doOnError(error -> log.error("Error occurred while updating task: {}", error.getMessage(), error));
    }

    @Override
    public Mono<Void> deleteTask(Long id) {
        log.info("Deleting task with ID: {}", id);
        return taskRepository.existsById(id)
                .flatMap(exists -> exists
                        ? taskRepository.deleteById(id).doOnSuccess(aVoid -> log.debug("Task deleted successfully with ID: {}", id))
                        : Mono.error(new TaskNotFoundException(TASK_NOT_FOUND_ID + id)))
                .doOnError(error -> log.error("Error occurred while deleting task: {}", error.getMessage(), error));
    }

    private Mono<Task> updateExistingTask(Task existingTask, TaskRequestDTO newTaskData) {
        existingTask.setTitle(newTaskData.getTitle());
        existingTask.setDescription(newTaskData.getDescription());
        existingTask.setStatus(newTaskData.getStatus());
        return taskRepository.save(existingTask);
    }
}
