# RelayBase Backend

Spring Boot backend for RelayBase, an internal knowledge base manager with JWT authentication, role-based access, article lifecycle management, and MongoDB persistence.

## Live URLs

- Backend API: `https://internal-working.onrender.com`
- Versioned API base: `https://internal-working.onrender.com/api/v1`
- Swagger UI: `https://internal-working.onrender.com/swagger-ui.html`
- OpenAPI JSON: `https://internal-working.onrender.com/v3/api-docs`

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- MongoDB
- JWT Authentication

## Features

- User registration and login
- Password hashing with JWT-based auth
- Role-based access for `ADMIN` and `USER`
- Admin CRUD for articles
- Publish, unpublish, and archive article workflow
- Bookmark APIs for user accounts
- Versioned API support through `/api/v1/...`

Legacy `/api/...` routes are still supported for backward compatibility.

## Important API Groups

- `/api/v1/auth`
- `/api/v1/articles`
- `/api/v1/admin/articles`
- `/api/v1/bookmarks`

## Local Setup

### 1. MongoDB

Run MongoDB locally at:

```bash
mongodb://localhost:27017/internal_knowledge_base
```

### 2. Environment Variables

Use values from `.env.example`.

Important keys:

- `MONGODB_URI`
- `JWT_SECRET`
- `FRONTEND_URLS`

### 3. Run the Backend

```bash
mvn spring-boot:run
```

Local server:

```bash
http://localhost:8080
```

## Role Assignment

- First registered user becomes `ADMIN`
- Later registered users become `USER`

## Deliverables Covered

- Working authentication APIs
- Working CRUD APIs
- Swagger/OpenAPI documentation
- Postman collection in `postman/RelayBase.postman_collection.json`
- README and env example

## Postman Collection

Import:

```bash
postman/RelayBase.postman_collection.json
```

Collection includes:

- Register
- Login
- Current user
- Published articles
- Article details
- Admin article list
- Create article
- Update article
- Update article status
- Delete article
- User bookmarks

## Scalability Note

Current backend is structured to scale cleanly:

- Separation across controllers, services, repositories, DTOs, and security
- Stateless JWT authentication for easier horizontal scaling
- Versioned routes through `/api/v1`
- Query-based filtering that can be extended to pagination, caching, and indexing

Future improvements:

- Redis caching
- Centralized logging and monitoring
- Load balancing for stateless API instances
- Dockerized local and deployment workflows
- Service-level split if traffic grows significantly
