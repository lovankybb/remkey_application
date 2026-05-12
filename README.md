# REMKEY APPLICATION

## 1. Overview

Remkey is a specialized flashcard application designed to maximize long-term memory retention through an advanced Spaced
Repetition System (SRS).

Unlike traditional applications that rely on outdated scheduling models, Remkey integrates the FSRS (Free Spaced
Repetition Scheduler) algorithm. This modern mathematical model dynamically predicts your memory's "forgetting curve"
and calculates the optimal time to review each card. By focusing on Stability (S), Difficulty (D), and Retrievability (
R), Remkey ensures you spend less time reviewing what you already know and more time mastering new material.

## 2. System design and work flow

    Link: https://drive.google.com/drive/folders/17OF4ZbusdjAeHsf26TgEUdpG-cSS7X6k?hl=vi

## 3.Tech Stack

### Backend

- Java 21
- Spring Boot 4

### Database & Storage

- PostgreSQL
- H2 Database
- Redis

### AI Integration

- Google Gemini AI: Generate card automatically.

### Algorithm

- FSRS (Free Spaced Repetition Scheduler): The core mathematical model for interval scheduling and memory optimization.

### Tools & Others

- Maven
- Docker 14.22-trixie

## 4. Installation & Setup

### Prerequisites

- JDK 21 or higher

- Maven 3.6+

- PostgreSQL 14+

- Gemini API Key (Get it from Google AI Studio)

### Clone the Repository

- git clone https://github.com/yourusername/remkey-backend.git

### Database Configuration

- Create a database named remkey in your PostgreSQL.

### Config file application.yaml

Create file src/main/resources/application.yaml (or use Environment Variables):
   ````     
spring:
  application:
    name: remkey
#    connect to db
  datasource:
    url: jdbc:postgresql://localhost:5432/remkey
    username: #your username
    password: #your password
    driver-class-name: org.postgresql.Driver
  data:
    redis:
      host: localhost
      port: 6379
#      config jpa for postgres
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect


  flyway:
    enabled: true
    locations: classpath:db/migration
    repair-on-migrate: true
    baseline-on-migrate: true

  mail:
    host: smtp.gmail.com
    port: 587
    username: #your email
    password: #your password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true


# json web token
jwt:
  secret-key: #your secret key
  valid-duration: 5 #day
  refreshable:  3 #day


app:
  sec:
    otp-verifier:
      valid-duration: 5 #minutes
      refreshable:  1 #minutes
      request-limit: 5 #times per day
    cors:
      endpoint: "*" #your cors enpoint

server:
  servlet:
    context-path: /api/v1/remkey
    port: 8080
   
   ````

### Run application as a normal spring app

### Postman
    Link: https://lovankydev.postman.co/workspace/lovankydev's-Workspace~053387a3-a707-44bc-8a9c-9cfdcdad1ce7/collection/46238050-1c1d4b70-bc20-45d4-84ce-d0fd5c5593db?action=share&source=copy-link&creator=46238050


## 5.Security
- JWT token with hs256 and 
- OTP verification with rate limit and expiration 
- Bucket4j 

## Contact
- Email: lovanky.work@gmail.com
- Phone: +8486535996
- Address: Haiphong, Vietnam.