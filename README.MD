# Live Player Tracking Event Pipeline

## Overview

This project is a small Java Spring Boot backend application that practises an event-driven tracking data pipeline.

The system accepts athlete tracking data through a REST API, transforms the request into an internal event contract, publishes the event to Kafka, and consumes the event asynchronously.

The purpose of the project is to practise the basic structure of a Java + Kafka backend pipeline.

---

## Project Status

Current status:

```text
Conceptually structured, but not fully working end-to-end yet.
```

Current review grade:

```text
6.81 / 10
```

The project shows a reasonable first attempt at:

* Spring Boot REST ingestion
* DTO to event transformation
* Kafka producer usage
* Kafka consumer usage
* Docker-based local Kafka setup
* Basic event-driven architecture structure

The project does not yet prove:

* Low-latency processing
* Production-grade Kafka usage
* Reliable message delivery
* Consumer lag handling
* Retry/dead-letter handling
* Observability
* Distributed tracing
* Schema evolution
* Load testing
* Cloud deployment
* AWS integration

---

## Purpose

The purpose of this project is to practise a small backend slice similar in shape to event-driven systems used in data platforms.

The project focuses on:

* Receiving data through a REST API
* Separating external request DTOs from internal event contracts
* Transforming incoming data inside a service layer
* Publishing an event to Kafka
* Consuming an event from Kafka
* Running Kafka locally with Docker Compose

This is a learning project, not an advanced backend platform.

---

## Architecture

```text
Client
  ↓
POST /api/tracking-events
  ↓
TrackingController
  ↓
TrackingService
  ↓
AthletePositionEvent
  ↓
Kafka topic: athlete-position-events
  ↓
Kafka Consumer
  ↓
Console/log output
```

---

## Tech Stack

* Java
* Spring Boot
* Spring Web
* Spring Kafka
* Jakarta Validation
* Apache Kafka
* Docker Compose
* Gradle

---

## API Endpoint

### Publish Tracking Event

```http
POST /api/tracking-events
```

Example request body:

```json
{
  "athleteId": "athlete-001",
  "sessionId": "session-001",
  "x": 12.5,
  "y": 7.3
}
```

Example response:

```text
Player tracking sent to Kafka
```

---

## Request DTO

```java
public record TrackingEventRequest(
        String athleteId,
        String sessionId,
        double x,
        double y
) {}
```

The request DTO represents data received from the external API.

Current limitation:

```text
The request does not currently include the original event timestamp.
```

That means the system cannot distinguish between:

* when the athlete position was actually recorded
* when the backend received and processed the request

This matters in real tracking systems.

---

## Internal Event Contract

```java
public record AthletePositionEvent(
        String athleteId,
        String sessionId,
        double x,
        double y,
        Instant timestamp,
        String source
) {}
```

The internal event contract represents the message sent to Kafka.

The service currently adds:

```text
source = REST_API
timestamp = Instant.now()
```

Current limitation:

```text
timestamp currently means server-created time, not original tracking event time.
```

A better later version may use:

```java
Instant eventTimestamp;
Instant receivedAt;
```

---

## Kafka Topic

```text
athlete-position-events
```

This topic represents a stream of athlete position events.

The producer publishes events to this topic, and the consumer listens for events from it.

Current limitation:

```text
The project does not yet prove correct Kafka behaviour end-to-end.
```

---

## Running Kafka Locally

Kafka is intended to run through Docker Compose.

Start Kafka:

```bash
docker compose up -d
```

Check the Kafka container:

```bash
docker ps
```

View Kafka logs:

```bash
docker logs kafka
```

Stop Kafka:

```bash
docker compose down
```

---

## Application Configuration

Correct core Kafka configuration should look like this:

```properties
spring.application.name=live-player-tracking

spring.kafka.bootstrap-servers=localhost:9092

spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer

spring.kafka.consumer.group-id=tracking
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer

spring.kafka.consumer.properties.spring.json.trusted.packages=com.independence.player.*
```

Important correction:

```text
Producer should serialize JSON.
Consumer should deserialize JSON.
```

The producer should use:

```properties
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
```

The consumer should use:

```properties
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
```

---

## Important Implementation Note

The service should publish to the Kafka topic explicitly:

```java
kafkaTemplate.send("athlete-position-events", event.athleteId(), event);
```

Using `sendDefault(...)` only works if a default topic is configured separately.

Creating a `NewTopic` bean does not automatically make that topic the default topic for `KafkaTemplate`.

---

## Current Review Score

```text
Docker Compose: 8/10
Kafka Topic config: 6.5/10
Kafka Consumer: 7/10
TrackingController: 8/10
TrackingEventRequest DTO: 6.5/10
AthletePositionEvent: 8/10
TrackingService: 6.5/10
application.properties: 4/10

Overall: 6.81/10
```

---

## What This Project Demonstrates

This project demonstrates beginner-to-intermediate practice with:

* Structuring a Spring Boot backend
* Creating a REST controller
* Using DTOs and records
* Separating API input from internal events
* Mapping request data into an event object
* Injecting and using `KafkaTemplate`
* Creating a Kafka listener
* Running Kafka locally with Docker Compose

---

## What This Project Does Not Yet Demonstrate

This project does not yet demonstrate:

* A working end-to-end Kafka flow
* Production-ready Kafka configuration
* Low-latency guarantees
* Measured latency
* Throughput testing
* Backpressure handling
* Retry strategy
* Dead-letter topics
* Schema registry
* Versioned event contracts
* Observability with metrics/tracing
* Cloud deployment
* AWS services
* Secure API design
* Load testing
* Horizontal scaling

---

## Known Issues

* Kafka producer and consumer serializer configuration must be corrected
* Topic config class should include `@Configuration`
* Service should use explicit topic publishing instead of `sendDefault(...)`
* Consumer should use a logger instead of `System.out.printf`
* Consumer log formatting needs cleanup
* Request DTO is missing a client/event timestamp
* `@NotNull` on primitive `double` fields does not add useful validation
* Class names such as `Consumer` and `Topic` should be renamed for clarity
* The project has not yet been proven to run end-to-end

---

## Next Improvements

Recommended next steps:

* Fix Kafka serializer/deserializer configuration
* Add `@Configuration` to Kafka topic config
* Rename `Topic` to `KafkaTopicConfig`
* Rename `Consumer` to `AthletePositionConsumer`
* Replace `System.out.printf` with structured logging
* Add timestamp to `TrackingEventRequest`
* Send Kafka events using an explicit topic name
* Get a full end-to-end flow running locally
* Add a controller test for `POST /api/tracking-events`
* Add a service test verifying request-to-event mapping

---

## Next Ticket

### Make Live Athlete Tracking Pipeline Run End-to-End Locally

The next goal is not to add more features.

The next goal is to prove the current slice works.

Win condition:

```text
POST /api/tracking-events
        ↓
TrackingService maps request to AthletePositionEvent
        ↓
Kafka publishes to athlete-position-events
        ↓
Consumer receives event
        ↓
Consumer logs event clearly
```

Do not add:

* AWS
* S3
* Lambda
* WebSockets
* Database
* Authentication
* Extra event types
* Extra services

---

## Future Advanced Work

Only after the basic end-to-end flow works, future improvements could include:

* Measuring request-to-consumer latency
* Adding event timestamps and received timestamps
* Logging latency between ingestion and consumption
* Adding retry handling
* Adding a dead-letter topic
* Adding structured logging
* Adding metrics
* Adding OpenTelemetry tracing
* Adding Testcontainers integration tests
* Adding schema versioning
* Adding WebSocket delivery
* Adding S3 or data lake storage
* Adding AWS Lambda or SQS integration

These would move the project closer to advanced backend practice, but they are not currently implemented.

---

## Final Judgement

This project is a useful first Kafka/Spring Boot event-driven practice project.

It has a reasonable structure, but it is not yet a working or advanced system.

Final score:

```text
6.81 / 10
```

Current level:

```text
Good learning project.
Not production-ready.
Not low-latency.
Not advanced yet.
```

The correct next step is to make the basic local Kafka flow work end-to-end before adding anything else.
