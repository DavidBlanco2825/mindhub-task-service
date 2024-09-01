package com.example.taskservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskRequestDTO {

    private String title;
    private String description;
    private String status;
    private Long userId;
}
