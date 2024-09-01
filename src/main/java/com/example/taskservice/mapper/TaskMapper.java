package com.example.taskservice.mapper;

import com.example.taskservice.dto.TaskRequestDTO;
import com.example.taskservice.dto.TaskResponseDTO;
import com.example.taskservice.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponseDTO toResponseDto(Task task) {
        if (task == null) {
            return null;
        }

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        );
    }

    public Task toEntity(TaskRequestDTO taskRequestDTO) {
        if (taskRequestDTO == null) {
            return null;
        }

        Task task = new Task();
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setStatus(taskRequestDTO.getStatus());
        task.setUserEmail(taskRequestDTO.getUserEmail());

        return task;
    }
}
