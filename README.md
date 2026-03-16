# 🛒 Basket Service API

A RESTful API for basket and order management, built with Spring Boot and secured with JWT authentication.

---

## 🚀 Technologies

- **Java 17+**
- **Spring Boot 3**
- **Spring Security** — JWT-based stateless authentication
- **Redis** — Basket session/cache storage
- **MongoDB** — Order persistence
- **Docker** — Containerization
- **Springdoc OpenAPI** — API documentation (Swagger UI)

---

## 📋 Prerequisites

- [Docker](https://www.docker.com/) & Docker Compose
- Java 17+
- Maven 3.8+

---

## ⚙️ Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/basket-service.git
cd basket-service
```

### 2. Configure environment variables

Create a `.env` file in the root directory:

```env
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000

MONGO_URI=mongodb://localhost:27017/basketservice
MONGO_DATABASE=basketservice

REDIS_HOST=localhost
REDIS_PORT=6379
```

### 3. Run with Docker Compose

```bash
docker-compose up -d
```

### 4. Run locally (without Docker)

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

---

## 📚 API Documentation

After starting the application, access the Swagger UI at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 Authentication

This API uses **JWT Bearer Token** authentication.

### Register

```http
POST /api/v1/basketservice/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123"
}
```

### Login

```http
POST /api/v1/basketservice/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "secret123"
}
```

Use the returned token in subsequent requests:

```http
Authorization: Bearer <your_token>
```

---

## 🛍️ Endpoints

### Basket

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| `POST` | `/api/v1/basketservice/basket/items` | USER | Add item to basket |
| `PUT` | `/api/v1/basketservice/basket/items` | USER | Update item quantity |
| `DELETE` | `/api/v1/basketservice/basket/items` | USER | Remove item from basket |
| `GET` | `/api/v1/basketservice/basket/me` | USER | Get authenticated user's basket |
| `GET` | `/api/v1/basketservice/basket/admin` | ADMIN | Get all baskets |

### Order

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| `POST` | `/api/v1/basketservice/order/checkout` | USER | Checkout current basket |
| `GET` | `/api/v1/basketservice/order/me` | USER | Get authenticated user's orders |
| `GET` | `/api/v1/basketservice/order/admin` | ADMIN | Get all orders |

---

## 🐳 Docker Compose

```yaml
spring:
  application:
    name: basketservice

  data:
    mongodb:
      host: localhost
      port: 27017
      database: basket-service

    redis:
      host: localhost
      port: 6379
      password: sa
      time-to-live: 60000

  cache:
    type: redis

basket:
  client:
    platzi: https://api.escuelajs.co/api/v1/
  security:
    secret: "random string"
```

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/basketservice/
│   │   ├── client/          # External service clients (Feign/RestTemplate)
│   │   ├── configuration/   # Security, OpenAPI, Redis configurations
│   │   ├── controller/      # REST controllers & Swagger doc interfaces
│   │   ├── domain/          # Domain entities/models
│   │   ├── dto/             # Request/Response DTOs
│   │   ├── Enum/            # Enumerations
│   │   ├── exception/       # Custom exceptions & handlers
│   │   ├── mapper/          # Object mappers (MapStruct/ModelMapper)
│   │   ├── repository/      # MongoDB repositories
│   │   ├── security/        # JWT filter, entry points, access handlers
│   │   ├── service/         # Business logic
│   │   ├── swagger/         # OpenAPI/Swagger configuration
│   │   └── BasketserviceApplication.java
│   └── resources/
│       └── application.properties
```

---

## 📄 License

This project is licensed under the MIT License.
