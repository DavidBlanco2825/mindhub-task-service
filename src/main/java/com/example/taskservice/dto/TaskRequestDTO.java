package com.example.taskservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "Title is required and cannot be empty or blank.")
    private String title;

    @NotBlank(message = "Description is required and cannot be empty or blank.")
    private String description;

    @NotBlank(message = "Status is required and cannot be empty or blank.")
    private String status;

    @NotBlank(message = "User's Email is required and cannot be empty or blank.")
    @Email(message = "User's Email is not valid.")
    private String userEmail;
}
