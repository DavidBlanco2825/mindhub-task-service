# Task Service

**Author:** [David Fernando Blanco](https://github.com/DavidBlanco2825)

## Overview

**TaskService** is a specialized microservice within the **NoteList** application, designed to manage tasks efficiently. It offers a comprehensive RESTful API to perform Create, Read, Update, and Delete (CRUD) operations on tasks, serving as a crucial component in handling task-related data within the application.

TaskService is built using **Spring WebFlux**, embracing reactive programming principles. This enables the service to handle asynchronous data streams and provide non-blocking IO operations, making it highly scalable and capable of efficiently managing large volumes of concurrent requests with minimal resource consumption.

### Key Features

- **Full CRUD Functionality**: Seamless management of task lifecycle through a robust API.
- **Reactive Programming**: Powered by Spring WebFlux for building responsive, non-blocking, and event-driven applications.
- **High Scalability**: Designed to scale effortlessly with increasing loads, suitable for high-traffic environments.
- **Integration Ready**: Seamlessly integrates with other components within the NoteList ecosystem, ensuring smooth interoperability.
- **API Documentation**: Integrated Swagger documentation provides an interactive interface to explore the API.

As an integral part of the NoteList application, TaskService ensures a responsive, reliable, and efficient backend for managing tasks, enhancing the overall user experience.

## Technologies Used

- **Spring Boot**: Provides the foundational framework for developing the TaskService application, offering ease of use and robustness.
- **Spring WebFlux**: The core framework for building the application's reactive, asynchronous web services.
- **Spring Data R2DBC**: Facilitates reactive access to relational databases, integrating seamlessly with Spring WebFlux.
- **H2 Database**: An in-memory database used for development and testing purposes, allowing rapid prototyping.
- **PostgreSQL**: A robust, production-ready relational database supported by the service for scalable data management.
- **R2DBC H2 Driver**: Enables reactive database interactions with the H2 database.
- **R2DBC PostgreSQL Driver**: Enables reactive interaction with PostgreSQL databases.
- **SpringDoc OpenAPI**: Automatically generates detailed API documentation, following OpenAPI standards.
- **Reactor Test**: Provides utilities for testing reactive streams, ensuring the correctness of reactive workflows.
- **Spring Boot Test**: Supports comprehensive testing of the application, including unit and integration tests.

## Getting Started

### Prerequisites

- **Java 17** or later
- **Maven 3.6** or later
- **Git** (for version control)

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/DavidBlanco2825/mindhub-task-service.git
    cd mindhub-task-service
    ```

2. **Build the project**:
    ```bash
    mvn clean install
    ```

3. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

4. **Access the application**:
    - The API is available at `http://localhost:8081/api/tasks`.
    - Swagger API documentation can be accessed at `http://localhost:8081/webjars/swagger-ui/index.html`.

## API Endpoints

- **`POST /api/tasks`**: Create a new task.
- **`GET /api/tasks/{id}`**: Retrieve a specific task by its ID.
- **`GET /api/tasks`**: Retrieve a list of all tasks.
- **`PUT /api/tasks/{id}`**: Update an existing task by its ID.
- **`DELETE /api/tasks/{id}`**: Delete a task by its ID.
