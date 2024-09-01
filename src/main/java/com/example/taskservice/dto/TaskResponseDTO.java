package com.example.taskservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String status;
}
