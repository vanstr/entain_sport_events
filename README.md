# Sport Events API

This project is a practical task based on Spring Boot application that provides a RESTful API for managing sport events. It allows you to create, retrieve events and update the status of sport events.

## Provided requirements
Create a CRUD REST API for sport events. An event should consist of id, name, sport (football,
hockey, etc), a status (inactive, active, finished) and start_time
API should cover the following functionality:
- Create a sport event 
- Get list of sport events with optional filters by status and sport type 
- Get a sport event by id
- Change sport event status
   
Event status change restrictions
- Can be changed from inactive to active
- Can be changed from From active to finished
- Cannot activate an event if start_time is in the past
- Finished could not be changed to any status
- Inactive can not be changed to finished


## Technologies Used

- Java
- Spring Boot
- Gradle
- JUnit 5
- Lombok

## Getting Started

### Prerequisites

- Java 21 or higher
- Gradle 8.11.1 or higher

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/sport-events-api.git
    cd sport-events-api
    ```

2. Build the project:
    ```sh
    ./gradlew build
    ```

3. Run the application:
    ```sh
    ./gradlew bootRun
    ```

The application will start on `http://localhost:8080`.

## Running Tests

To run the tests, use the following command:
```sh
./gradlew test