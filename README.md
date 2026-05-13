# REMKEY APPLICATION

## 1. Overview

Remkey is a specialized flashcard application designed to maximize long-term memory retention through an advanced Spaced Repetition System (SRS).

Unlike traditional applications that rely on outdated scheduling models, Remkey integrates the FSRS (Free Spaced Repetition Scheduler) algorithm. This modern mathematical model dynamically predicts your memory's "forgetting curve" and calculates the optimal time to review each card. By focusing on Stability (S), Difficulty (D), and Retrievability (R), Remkey ensures you spend less time reviewing what you already know and more time mastering new material.

What truly sets Remkey apart is its integrated social ecosystem. Beyond personal study, it features a built-in "mini social network" where you can discover, share, and import community-created flashcard decks directly into your own library—making knowledge-sharing seamless and free.

## 2. Business document

    https://drive.google.com/drive/u/1/folders/1O5JeI9eR3FV54rDBGYc0tVMPpWb_tMcu

## 3.Tech Stack

### Backend

- Java 21
- Spring Boot 4

### Database & Storage

- PostgreSQL
- Redis

### Frontend

- ReactJs

### Third party APIs/Services

- FireBase
- ResponsiveVoice


###  Security

- Jwt token
- Bucket4j
- OTP verify email

### Core algorithm

- FSRS (Free Spaced Repetition Scheduler): The core mathematical model for interval scheduling and memory optimization.


## 4. Installation & Setup

### Prerequisites

- JDK 21 or higher

- Maven 3.6+

- PostgreSQL 14+

- Firebase data from https://firebase.google.com/

- Docker engine

- Nodejs

- Npm 

### Clone the Repository

- git clone https://github.com/lovankybb/remkey_application

### Database Configuration

- At root directory run command: sudo docker compose up -d

### Backend configuration

#### 1. Create file server/src/main/resources/application.yaml (or use Environment Variables):

```
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
valid-duration: 5 #days
refreshable:  6 #days


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

```

#### 2. Add file server/src/main/resources/firebase/your_firebase_secret_key.json

  - Create a project at https://firebase.google.com/ and get your_firebase_secret_key.json

### Frontend

  - Create file web-app/.env 
  ```
#File: .env

VITE_API_BASE_URL=http://localhost:8080


#FireBase data
VITE_API_KEY=
VITE_AUTH_DOMAIN=
VITE_PROJECT_ID=
VITE_STORAGE_BUCKET=
VITE_MESSAGING_SENDER_ID=
VITE_APP_ID=
VITE_MEASUREMENT_ID=


  ```

### Postman

    Link: https://lovankydev.postman.co/workspace/lovankydev's-Workspace~053387a3-a707-44bc-8a9c-9cfdcdad1ce7/collection/46238050-1c1d4b70-bc20-45d4-84ce-d0fd5c5593db?action=share&source=copy-link&creator=46238050


## Contact

- Email: lovanky.work@gmail.com
- Phone: +8486535996
- Address: Haiphong, Vietnam.
