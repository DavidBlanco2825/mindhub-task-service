package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskRequestDTO;
import com.example.taskservice.dto.TaskResponseDTO;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.taskservice.commons.Constants.TASK_NOT_FOUND_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(TaskController.class)
class TaskControllerTest {


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TaskService taskService;

    private TaskRequestDTO taskRequestDTO;
    private TaskResponseDTO taskResponseDTO;

    @BeforeEach
    void setUp() {
        taskRequestDTO = new TaskRequestDTO("Test Task", "Test Description", "Pending", "user@example.com");
        taskResponseDTO = new TaskResponseDTO(1L, "Test Task", "Test Description", "Pending");
    }

    @Test
    void createTask_ShouldReturnCreatedTask() {
        when(taskService.createTask(any(TaskRequestDTO.class))).thenReturn(Mono.just(taskResponseDTO));

        webTestClient.post()
                .uri("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(taskRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TaskResponseDTO.class)
                .isEqualTo(taskResponseDTO);
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
        when(taskService.getTaskById(anyLong())).thenReturn(Mono.just(taskResponseDTO));

        webTestClient.get()
                .uri("/api/tasks/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponseDTO.class)
                .isEqualTo(taskResponseDTO);
    }

    @Test
    void getTaskById_ShouldReturnNotFound_WhenTaskDoesNotExist() {
        when(taskService.getTaskById(anyLong())).thenReturn(Mono.error(new TaskNotFoundException(TASK_NOT_FOUND_ID + 999L)));

        webTestClient.get()
                .uri("/api/tasks/{id}", 999L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() {
        when(taskService.getAllTasks()).thenReturn(Flux.just(taskResponseDTO));

        webTestClient.get()
                .uri("/api/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskResponseDTO.class)
                .hasSize(1)
                .contains(taskResponseDTO);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        when(taskService.updateTask(anyLong(), any(TaskRequestDTO.class))).thenReturn(Mono.just(taskResponseDTO));

        webTestClient.put()
                .uri("/api/tasks/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(taskRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponseDTO.class)
                .isEqualTo(taskResponseDTO);
    }

    @Test
    void deleteTask_ShouldReturnNoContent_WhenTaskIsDeleted() {
        when(taskService.deleteTask(anyLong())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/tasks/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();
    }
}