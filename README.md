# Task Manager

A collaborative **Task & Team Management System** built with **Spring Boot** and **React**.

Developed by **Ginseku**, **gnabriola**, and **Jari1980**.

---

## Features

### Authentication & Security
- JWT Authentication
- Spring Security
- Role-based authorization
- Secure password handling

### Team Management
- Create and manage teams
- Add/remove members

### Project Management
- Create projects
- Assign projects to teams

### Task Management
- Create tasks
- Assign tasks to users
- Due dates
- Priority levels
- Status tracking

### Modern Frontend
- React + TypeScript
- Drag & Drop support
- Responsive UI
- Fast Vite development setup

---

# Tech Stack

## Backend
- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Docker
- OpenAPI / Swagger

## Frontend
- React 19
- TypeScript
- Vite
- React Router
- Axios
- DnD Kit
- React Select

---

# Project Structure

```bash
TaskManager/
│
├── backend/
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── src/main/java/com/collab/taskmanager/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entities/
│   │   ├── enums/
│   │   ├── exceptions/
│   │   ├── init/
│   │   ├── repos/
│   │   ├── security/
│   │   └── service/
│   │
│   └── src/main/resources/
│       └── application.properties
│
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── context/
│   │   ├── layouts/
│   │   ├── mock/
│   │   ├── pages/
│   │   ├── router/
│   │   ├── types/
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   └── index.css
│   │
│   └── vite.config.ts
│
└── README.md
```

---

# Database Entities

## User
```java
User
- id
- name
- email
- password
- role
```

## Team
```java
Team
- id
- name
- description
- createdBy
```

## TeamMember
```java
TeamMember
- id
- team
- member
- teamRole
- joinedAt
```

## Project
```java
Project
- id
- name
- description
- team
- createdBy
```

## Task
```java
Task
- id
- title
- description
- status
- priority
- dueDate
- assignedUser
- createdBy
- project
```

---

# Backend Setup

## Requirements

- Java 21
- Maven
- PostgreSQL
- Docker (optional)

---

## Configure Environment

Update:

```properties
backend/src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager
spring.datasource.username=postgres
spring.datasource.password=password

jwt.secret=your_secret_key
```

# Environment Variables

Create a `.env` file inside the backend directory.

Example:

```env
POSTGRES_DB=taskdb
POSTGRESQL_USERNAME=postgres
POSTGRESQL_PASSWORD=your_password

DB_PORT=5433
APP_PORT=8080

JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=3600000

ADMIN_NAME=Admin
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=your_password
```


---

## Run Backend

### Using Maven

```bash
cd backend
mvn spring-boot:run
```

Backend runs on:

```bash
http://localhost:8080
```

---

### Using Docker

```bash
cd backend
docker compose up --build
```

---

# Frontend Setup

## Requirements

- Node.js
- npm

---

## Install Dependencies

```bash
cd frontend
npm install

```

---

## Start Frontend

```bash
npm run dev
```

Frontend runs on:

```bash
http://localhost:5173
```

---

# API Documentation

Swagger/OpenAPI documentation is included.

Available at:

```bash
http://localhost:8080/swagger-ui/index.html
```
<img width="1603" height="1228" alt="TaskManagerSwagger1" src="https://github.com/user-attachments/assets/e4f6813a-71e2-40b2-b84d-f02f92419cb4" />
<img width="1600" height="1073" alt="TaskManagerSwagger2" src="https://github.com/user-attachments/assets/5dea587c-cfe2-4b40-980d-210055b72670" />
<img width="1578" height="498" alt="TaskManagerSwagger3" src="https://github.com/user-attachments/assets/ca3473ba-d99f-4c08-8d67-708a71df7fb6" />


---

# Security

Authentication and authorization are implemented using:

- Spring Security
- JWT Tokens
- Custom `UserPrincipal`
- Role-based access control

Example roles:

```java
ROLE_ADMIN
ROLE_USER
```

---

# Frontend Architecture

```bash
components/   -> reusable UI components
pages/        -> application pages
api/          -> backend communication
context/      -> global state management
router/       -> route configuration
types/        -> TypeScript interfaces/types
layouts/      -> layout wrappers
```

---

# Screenshots

Login:
<img width="1781" height="1197" alt="TaskManagerLogin" src="https://github.com/user-attachments/assets/aabe707f-cba0-4312-b257-d0e3c88bd85b" />

Main page with open sidebar:
<img width="1656" height="987" alt="TaskManagerMain" src="https://github.com/user-attachments/assets/d4737b33-3a25-4894-b538-b6c2b00c0445" />


---

# Future Improvements

- Export tasks to pdf
- Export tasks to Excel
- Implement somekind of AI
- Add and use microservices
- CI/CD pipeline
- Unit & integration testing

---

# Contributing

Contributions are welcome.

1. Fork the repository
2. Create a new branch
3. Commit your changes
4. Open a Pull Request

---

# Clone Repository

```bash
git clone https://github.com/Jari1980/TaskManager.git

cd TaskManager
```

---

# Development Team

- Ginseku
- gnabriola
- Jari1980

---

# License

This project is for educational and portfolio purposes.



---
