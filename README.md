# Yamb

## Overview

Yamb is a variant of the traditional dice game yahtzee in the form of a web application. The system supports features like user registration, game session management, score tracking, and log management, offering a RESTful API for interaction.

The API is backed by a PostgreSQL database for player information and a MongoDB database for managing game sessions. It also implements role-based access control (RBAC) to ensure that certain features, such as log management, are only accessible to admins.

## Features

-   **User Authentication**: Supports registration, login, and password changes with JWT-based authentication.
-   **Game Management**: Players can create and manage multiple game sessions.
-   **Score Tracking**: Players' scores are recorded and tracked across multiple games.
-   **Player Preferences**: Customizable themes and language settings.
-   **Logging**: Full log management for admins to track system behavior and user actions.

## Project Architecture

Yamb is divided into the following layers:

1.  **Domain**: Interface with the database to store and retrieve data.
2.  **Business**: Contain the business logic, managing domain objects like players, games, and scores.
3.  **API**: Handles HTTP requests and responses and defines the structure of data exchanged between the client and server.

## Technologies Used

-   **Java 11**
-   **Spring Boot 2.5.2**
-   **MongoDB**
-   **PostgreSQL**
-   **JWT**
-   **Hibernate**
-   **ModelMapper**
-   **React.js**
-   **TypeScript**

## API Documentation

The OpenAPI specification for this project is available [here](src/main/resources/static/api-docs/openapi.yaml). It defines all available endpoints and their request/response formats.

You can view the interactive Swagger UI for detailed API documentation:

-   Swagger UI: https://jamb.com.hr/api/swagger-ui.html

## Installation

To run this project locally, follow these steps:

### Prerequisites

-   Java 11+
-   Maven
-   PostgreSQL
-   MongoDB

### Setup Instructions

1.  Clone the repository:

    `git clone https://github.com/MatejDanic/yamb.git
    cd yamb`

2.  Install dependencies:

    `mvn clean install`

3.  Configure databases:

    -   Set up a PostgreSQL and MongoDB instances and update the connection properties in `application.properties`.

4.  Run the application:

    `mvn spring-boot:run`

5.  The API should now be accessible at `http://localhost:8080`.


## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.

## Contact

For any questions or inquiries, please contact me at <matej.danic@gmail.com>