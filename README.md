# Asset Management System API

Backend REST API for managing company assets, assignments, and maintenance using **Spring Boot**.

The system allows administrators to manage assets, technicians to handle maintenance tasks, and employees to view assigned assets.

---

# Architecture

```mermaid
flowchart TD

Client[Client / Postman]
Client --> API[Spring Boot API]

API --> DB[(MySQL Database)]
API --> CACHE[(Redis Cache)]

subgraph Docker Environment
API
DB
CACHE
end
```
---
# Tech Stack
### Backend
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA

### Database
- MySQL

### Cache
- Redis

### DevOps
- Docker
- Docker Compose

### Tools
- Postman
- GitHub
---
# Features
### Authentication
- Register
- Login
- JWT Token Authentication

### Asset Management
- Create Asset
- View Asset
- Update Asset Status
- Delete Asset

### Asset Assignment
- Assign Asset to Employee
- View Assignment History
  
### Maintenance System
- Create Maintenance Request
- Technician Maintenance Queue
- Complete Maintenance

### Monitoring
- Dashboard Monitoring Summary
- Asset Status Tracking

---

# Database ERD
<img width="1014" height="1126" alt="Untitled" src="https://github.com/user-attachments/assets/0cf96a64-59d7-49ee-a30f-65e30bfa7ab8" />

### Relationship
#### Role → Users
```
One Role
can have many Users
```
#### Users → Assignments
```
One User
can have many Assignments
```
#### Assets → Assignments
```
One Asset
can be assigned many times
```
#### Assets → Maintenance
```
One Asset
can have multiple maintenance records
```
---

## Business Flow

The Asset Management System manages company assets from creation, assignment, monitoring, and maintenance.

<img width="1571" height="2490" alt="mermaid-diagram (1)" src="https://github.com/user-attachments/assets/3d4052d6-57cc-401b-824a-57281e8fed80" />

---

# Author
### Muhammad Arsyad Giri
### Backend Developer

---
