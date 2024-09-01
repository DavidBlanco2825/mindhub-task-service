package com.example.taskservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.taskservice.commons.Constants.DESCRIPTION_IS_REQUIRED;
import static com.example.taskservice.commons.Constants.STATUS_IS_REQUIRED;
import static com.example.taskservice.commons.Constants.TITLE_IS_REQUIRED;
import static com.example.taskservice.commons.Constants.USER_EMAIL_IS_NOT_VALID;
import static com.example.taskservice.commons.Constants.USER_EMAIL_IS_REQUIRED;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = TITLE_IS_REQUIRED)
    private String title;

    @NotBlank(message = DESCRIPTION_IS_REQUIRED)
    private String description;

    @NotBlank(message = STATUS_IS_REQUIRED)
    private String status;

    @NotBlank(message = USER_EMAIL_IS_REQUIRED)
    @Email(message = USER_EMAIL_IS_NOT_VALID)
    private String userEmail;
}
