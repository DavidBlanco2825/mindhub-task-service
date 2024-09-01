package com.example.taskservice.service.impl;

import com.example.taskservice.dto.TaskRequestDTO;
import com.example.taskservice.dto.TaskResponseDTO;
import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskRequestDTO taskRequestDTO;
    private TaskResponseDTO taskResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize your test data
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("Pending");

        taskRequestDTO = new TaskRequestDTO("Test Task", "Test Description", "Pending", "user@example.com");
        taskResponseDTO = new TaskResponseDTO(1L, "Test Task", "Test Description", "Pending");
    }

    @Test
    void createTask_ShouldReturnTaskResponseDTO() {
        when(taskMapper.toEntity(any(TaskRequestDTO.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(task));
        when(taskMapper.toResponseDto(any(Task.class))).thenReturn(taskResponseDTO);

        Mono<TaskResponseDTO> result = taskService.createTask(taskRequestDTO);

        StepVerifier.create(result)
                .expectNext(taskResponseDTO)
                .verifyComplete();
    }


    @Test
    void getTaskById_ShouldReturnTaskResponseDTO_WhenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Mono.just(task));
        when(taskMapper.toResponseDto(task)).thenReturn(taskResponseDTO);

        Mono<TaskResponseDTO> result = taskService.getTaskById(1L);

        StepVerifier.create(result)
                .expectNext(taskResponseDTO)
                .verifyComplete();
    }

    @Test
    void getTaskById_ShouldReturnError_WhenTaskDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<TaskResponseDTO> result = taskService.getTaskById(1L);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof TaskNotFoundException &&
                        throwable.getMessage().equals("Task not found with Id: 1"))
                .verify();
    }

    @Test
    void getAllTasks_ShouldReturnTaskResponseDTOs() {
        when(taskRepository.findAll()).thenReturn(Flux.just(task));
        when(taskMapper.toResponseDto(task)).thenReturn(taskResponseDTO);

        Flux<TaskResponseDTO> result = taskService.getAllTasks();

        StepVerifier.create(result)
                .expectNext(taskResponseDTO)
                .verifyComplete();
    }

    @Test
    void updateTask_ShouldReturnUpdatedTaskResponseDTO() {
        when(taskRepository.findById(1L)).thenReturn(Mono.just(task));
        when(taskRepository.save(task)).thenReturn(Mono.just(task));
        when(taskMapper.toResponseDto(task)).thenReturn(taskResponseDTO);

        Mono<TaskResponseDTO> result = taskService.updateTask(1L, taskRequestDTO);

        StepVerifier.create(result)
                .expectNext(taskResponseDTO)
                .verifyComplete();
    }

    @Test
    void updateTask_ShouldReturnError_WhenTaskDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<TaskResponseDTO> result = taskService.updateTask(1L, taskRequestDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof TaskNotFoundException &&
                        throwable.getMessage().equals("Task not found with Id: 1"))
                .verify();
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        when(taskRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(taskRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = taskService.deleteTask(1L);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void deleteTask_ShouldReturnError_WhenTaskDoesNotExist() {
        when(taskRepository.existsById(1L)).thenReturn(Mono.just(false));

        Mono<Void> result = taskService.deleteTask(1L);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof TaskNotFoundException &&
                        throwable.getMessage().equals("Task not found with Id: 1"))
                .verify();
    }

}