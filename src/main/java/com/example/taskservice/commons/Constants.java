package com.example.taskservice.commons;

public class Constants  {

    // Validation Messages
    public static final String TITLE = "Title";
    public static final String DESCRIPTION = "Description";
    public static final String STATUS = "Status";
    public static final String USER_EMAIL = "User's Email";

    public static final String IS_REQUIRED = " is required and cannot be empty or blank.";
    public static final String IS_NOT_VALID = " is not valid.";

    public static final String TITLE_IS_REQUIRED = TITLE + IS_REQUIRED;
    public static final String DESCRIPTION_IS_REQUIRED = DESCRIPTION + IS_REQUIRED;
    public static final String STATUS_IS_REQUIRED = STATUS + IS_REQUIRED;
    public static final String USER_EMAIL_IS_REQUIRED = USER_EMAIL + IS_REQUIRED;

    public static final String USER_EMAIL_IS_NOT_VALID = USER_EMAIL + IS_NOT_VALID;

    // Exception messages
    public static final String TASK_NOT_FOUND_ID = "Task not found with Id: ";
}
