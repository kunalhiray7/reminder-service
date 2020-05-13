# Reminder Service

A simple microservice that manages user's reminders.

### Building project and Running tests
* Run `./gradlew clean build` for building the project and running the unit test cases.

### How to run locally??
* Run the `reminder-service` by executing the script `./run.sh` or running manually by `./gradlew bootrun`.
* The server should be running on `http://localhost:8080`.

### API Documentation
* Visit `http://localhost:8080/swagger-ui.html` for API documentation and testing the API.

### Dockerization
* Run `./gradlew clean build`
* Build the image by `docker build . -t reminderservice`
* You should see the image `reminderservice` when executed `docker images`
* Run the docker image by `docker run -p 8080:8080 reminderservice`

### Notes
* API `/api/registrations` and `/api/authentications` are public API. Use them to register the user and get the access token(follow API documentation for more details).
* Other APIs are protected by JWT access token.