package com.example.taskservice.service.impl;

import com.example.taskservice.dto.TaskRequestDTO;
import com.example.taskservice.dto.TaskResponseDTO;
import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.taskservice.commons.Constants.TASK_NOT_FOUND_ID;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Mono<TaskResponseDTO> createTask(TaskRequestDTO taskRequestDTO) {
        Task task = taskMapper.toEntity(taskRequestDTO);
        return taskRepository.save(task)
                .map(taskMapper::toResponseDto);
    }

    @Override
    public Mono<TaskResponseDTO> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toResponseDto)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(TASK_NOT_FOUND_ID + id)));
    }

    @Override
    public Flux<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .map(taskMapper::toResponseDto);
    }

    @Override
    public Mono<TaskResponseDTO> updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(TASK_NOT_FOUND_ID + id)))
                .flatMap(existingTask -> updateExistingTask(existingTask, taskRequestDTO))
                .map(taskMapper::toResponseDto);
    }

    private Mono<Task> updateExistingTask(Task existingTask, TaskRequestDTO newTaskData) {
        existingTask.setTitle(newTaskData.getTitle());
        existingTask.setDescription(newTaskData.getDescription());
        existingTask.setStatus(existingTask.getStatus());
        return taskRepository.save(existingTask);
    }

    @Override
    public Mono<Void> deleteTask(Long id) {
        return taskRepository.existsById(id)
                .flatMap(exists -> exists
                        ? taskRepository.deleteById(id)
                        : Mono.error(new TaskNotFoundException(TASK_NOT_FOUND_ID + id)));
    }
}
