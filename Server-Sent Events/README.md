# Push Notification Using Server-Sent Events

This project is a spring boot application that demonstrate the usage of server-sent events (SSE). SSE allows the server to push real-time updates to clients over a single HTTP connection, providing a convenient way to implement server side push notification.

## Features

1. **SSE Endpoints**: The project provides an endpoint in spring boot that creates an SSE connection with client and returns an `SseEmitter` object. This connection allows the server to send events to the connected clients.

2. **Client Connection**: Clients can connect to the application using the `EventSource` JavaScript API. By establishing a connection to the SSE endpoint, clients can receive server-sent events and hande them accordingly.

3. **Event Broadcasting**: Once a client is connected, the server can send events to the client over the established SSE connection. These events can contain relevant data or updates that the client can process and display in real-time.

## Getting Started

To run this proect locally, follow these steps:

1. Clone the repository:

```shell
git clone https://github.com/dilipthakkar/notification-service-java.git
```

2. Navigate to the project directory:

```shell
cd server-sent-events
```

3. Build the project using Maven:

```shell
mvn clean install
```

4. Run the application:

```shell
mvn spring-boot:run
```

You can run this project using favorite IDE also.

5. Access the application in your web browser:

```shell
http://localhost:8080
```

## Usage

1. Open the application in your web browser.
2. Enter your name in input box and click on `connect` button. this will connect you with our server-sent event stream.
3. You will see a toast message upon a successful connection. Additionally you will receive real-time updated on all other connected users.
4. You will get a notification as a toast message upon every successful connection of other users.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvement, please open an issue or submit a pull request in the respective sub-project repository.

## Acknowledgements

- [Spring Boot](https://spring.io/projects/spring-boot): Used for creating the server-side applications.
- [EventSource](https://developer.mozilla.org/en-US/docs/Web/API/EventSource): JavaScript API for establishing a server-sent events connection.
- [Dilip Thakar](https://github.com/dilipthakkar/): Author
