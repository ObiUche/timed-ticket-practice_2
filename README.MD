# Live Player Tracking Event Pipeline

## Overview

This project is a small Java Spring Boot backend application that simulates part of a real-time athlete tracking data platform.

The system accepts live tracking data through a REST API, transforms the request into an internal event contract, publishes the event to Kafka, and consumes the event asynchronously for downstream processing.

This project was built as practice for backend engineering work involving Java, Kafka, event-driven architecture, real-time data ingestion, and clean API contracts.

---

## Purpose

The purpose of this project is to practise a small backend slice similar to a real sports data platform.

The project focuses on:

* Java backend service design
* REST API ingestion
* DTO to event transformation
* Kafka producer/consumer flow
* Docker-based local Kafka setup
* Event-driven processing
* Basic observability through logging

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

The request DTO is separate from the internal event contract because external API input and internal event messages should not always be the same shape.

The service maps the incoming request into an event and adds backend-owned metadata such as:

```text
source = REST_API
timestamp = Instant.now()
```

---

## Kafka Topic

```text
athlete-position-events
```

This topic represents a stream of athlete position events.

The producer publishes events to this topic, and the consumer listens for events from it.

---

## Running Kafka Locally

Kafka is run through Docker Compose.

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

Core Kafka configuration should look like this:

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

---

## Important Implementation Note

The service should publish to the Kafka topic explicitly:

```java
kafkaTemplate.send("athlete-position-events", event.athleteId(), event);
```

Using `sendDefault(...)` only works if a default topic is configured separately.

Creating a `NewTopic` bean does not automatically make that topic the default topic for `KafkaTemplate`.

---

## Current Status

Current project status:

* REST controller created
* Request DTO created
* Internal event record created
* Service mapping created
* Kafka consumer created
* Docker Compose Kafka setup created
* Kafka topic configuration attempted

Current review grade:

```text
6.81 / 10
```

The project is conceptually correct, but still needs runtime wiring fixes before it can be considered complete.

---

## Known Issues

* Kafka producer/consumer serializer configuration needs to be correct
* Topic config class should include `@Configuration`
* Service should use explicit topic publishing instead of `sendDefault(...)`
* Consumer should use a logger instead of `System.out.printf`
* Consumer log formatting needs cleanup
* Request DTO is missing a client/event timestamp
* `@NotNull` on primitive `double` fields does not add useful validation
* Class names such as `Consumer` and `Topic` should be renamed for clarity

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

## End-to-End Win Condition

The project is complete when this flow works locally:

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

---

## Why This Project Matters

This project practises backend concepts used in real-time data systems.

It maps to engineering work involving:

* Low-latency data ingestion
* Kafka-based event-driven processing
* Java backend services
* Clean API contracts
* Internal event contracts
* Service boundaries
* Asynchronous downstream processing
* Docker-based local development

The project is intentionally small and focused. The goal is not to build a full platform yet, but to prove a single backend event pipeline from ingestion to consumption.
