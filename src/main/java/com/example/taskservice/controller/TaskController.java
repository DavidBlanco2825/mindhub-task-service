package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskRequestDTO;
import com.example.taskservice.dto.TaskResponseDTO;
import com.example.taskservice.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/tasks")
@Tag(name = "TaskController", description = "Operations related to task management")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Create Task", description = "Creates a new task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully created.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Invalid task data."))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "An error occurred while processing your request.")))
    })
    public Mono<ResponseEntity<TaskResponseDTO>> createTask(
            @RequestBody
            @Parameter(description = "Task data for the new task", required = true) @Valid TaskRequestDTO taskRequestDTO) {
        log.info("Received request to create task: {}", taskRequestDTO);
        return taskService.createTask(taskRequestDTO)
                .map(createdTask -> ResponseEntity.status(HttpStatus.CREATED).body(createdTask))
                .doOnSuccess(response -> log.info("Task created successfully with ID: {}", Objects.requireNonNull(response.getBody()).getId()))
                .doOnError(error -> log.error("Failed to create task: {}", error.getMessage(), error));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Task by ID", description = "Returns task information based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully returned.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Task not found."))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string",
                                    example = "An error occurred while processing your request.")))
    })
    public Mono<ResponseEntity<TaskResponseDTO>> getTaskById(
            @PathVariable("id")
            @Parameter(description = "ID of the task to be retrieved", required = true, example = "1") Long id) {
        log.info("Received request to get task by ID: {}", id);
        return taskService.getTaskById(id)
                .map(existingTask -> ResponseEntity.status(HttpStatus.OK).body(existingTask))
                .doOnSuccess(response -> log.info("Task retrieved successfully with ID: {}", id))
                .doOnError(error -> log.error("Failed to retrieve task: {}", error.getMessage(), error));
    }

    @GetMapping
    @Operation(summary = "Get All Tasks", description = "Returns all tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks successfully returned.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string",
                                    example = "An error occurred while processing your request.")))
    })
    public Mono<ResponseEntity<Flux<TaskResponseDTO>>> getAllTasks() {
        log.info("Received request to get all tasks");
        return taskService.getAllTasks()
                .collectList()
                .filter(tasks -> !tasks.isEmpty())
                .map(tasks -> ResponseEntity.ok(Flux.fromIterable(tasks)))
                .defaultIfEmpty(ResponseEntity.noContent().build())
                .doOnSuccess(response -> log.info("All tasks retrieved successfully"))
                .doOnError(error -> log.error("Failed to retrieve all tasks: {}", error.getMessage(), error));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Task", description = "Updates an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully updated.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Task not found."))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string",
                                    example = "An error occurred while processing your request.")))
    })
    public Mono<ResponseEntity<TaskResponseDTO>> updateTask(
            @PathVariable("id")
            @Parameter(description = "ID of the task to be updated", required = true, example = "1") Long id,
            @RequestBody
            @Parameter(description = "Updated task data", required = true) @Valid TaskRequestDTO taskRequestDTO) {
        log.info("Received request to update task with ID: {}", id);
        return taskService.updateTask(id, taskRequestDTO)
                .map(updatedTask -> ResponseEntity.status(HttpStatus.OK).body(updatedTask))
                .doOnSuccess(response -> log.info("Task updated successfully with ID: {}", id))
                .doOnError(error -> log.error("Failed to update task: {}", error.getMessage(), error));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Task", description = "Deletes an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted."),
            @ApiResponse(responseCode = "404", description = "Task not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Task not found."))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string",
                                    example = "An error occurred while processing your request.")))
    })
    public Mono<ResponseEntity<Object>> deleteTask(
            @PathVariable("id")
            @Parameter(description = "ID of the task to be deleted", required = true, example = "1") Long id) {
        log.info("Received request to delete task with ID: {}", id);
        return taskService.deleteTask(id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()))
                .doOnSuccess(response -> log.info("Task deleted successfully with ID: {}", id))
                .doOnError(error -> log.error("Failed to delete task: {}", error.getMessage(), error));
    }
}
