# DevSecOps E-Commerce

A containerized e-commerce application built to demonstrate **Spring Boot, REST APIs, Postman, Docker, MySQL, Kubernetes, GitHub Actions and DevSecOps security scanning**.

## Technology Stack

- Frontend: HTML, CSS, JavaScript
- Backend: Java 17 + Spring Boot
- API: Spring Boot REST
- Database: MySQL 8.4
- API testing: Postman
- Containerization: Docker + Docker Compose
- Orchestration: Kubernetes
- CI/CD: GitHub Actions
- Security: Trivy + SonarQube configuration
- Testing: JUnit + Spring Boot Test

## Project Architecture

```text
Browser
   |
   v
Spring Boot REST API
   |
   +---- MySQL
   |
   +---- Docker
          |
          +---- Docker Compose
          |
          +---- Kubernetes
```

## 1. Requirements

Install:

- JDK 17
- Maven 3.9+ (or use Maven Wrapper if added)
- Docker Desktop
- Postman
- kubectl (for Kubernetes)

## 2. Run with Docker Compose

From the project root:

```bash
docker compose up --build
```

Open:

```text
http://localhost:8080
```

Health check:

```text
http://localhost:8080/api/health
```

Stop:

```bash
docker compose down
```

Remove database volume too:

```bash
docker compose down -v
```

## 3. Run locally without Docker

Start MySQL and create a database/user matching:

```text
Database: ecommerce
Username: ecommerce
Password: ecommerce123
```

Then:

```bash
cd backend
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## 4. Postman

Import:

```text
postman/DevShop-ECommerce.postman_collection.json
```

The collection contains:

- GET health
- GET all products
- GET product by ID
- POST product
- PUT product
- POST demo order
- DELETE product

Base URL:

```text
http://localhost:8080
```

## 5. Docker

Build:

```bash
docker build -t devsecops-ecommerce:latest .
```

Run:

```bash
docker run -p 8080:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/ecommerce" \
  -e DB_USERNAME="ecommerce" \
  -e DB_PASSWORD="ecommerce123" \
  devsecops-ecommerce:latest
```

For normal development, prefer Docker Compose because it starts both MySQL and the application.

## 6. Kubernetes

Build and push the image first:

```bash
docker build -t YOUR_DOCKERHUB_USERNAME/devsecops-ecommerce:latest .
```

Push it:

```bash
docker push YOUR_DOCKERHUB_USERNAME/devsecops-ecommerce:latest
```

Edit:

```text
kubernetes/deployment.yaml
```

Replace:

```text
YOUR_DOCKERHUB_USERNAME
```

with your Docker Hub username.

Apply the resources:

```bash
kubectl apply -f kubernetes/mysql.yaml
kubectl apply -f kubernetes/deployment.yaml
```

Check:

```bash
kubectl get pods
kubectl get services
```

For Docker Desktop Kubernetes, open the application using:

```bash
http://localhost:30080
```

If using another Kubernetes environment, use:

```bash
kubectl get service ecommerce-service
```

and follow the URL provided by your environment.

## 7. Kubernetes scaling

The deployment starts with 2 replicas.

Check:

```bash
kubectl get pods
```

Scale to 3:

```bash
kubectl scale deployment ecommerce-backend --replicas=3
```

Check again:

```bash
kubectl get pods
```

## 8. CI/CD

GitHub Actions performs:

1. Checkout
2. Java 17 setup
3. Maven tests
4. Docker image build
5. Trivy filesystem scan
6. Trivy image scan

The workflow intentionally stops after the Docker security scan so it can run without requiring registry credentials. You can add a Docker Hub publish stage later using repository secrets.

## 9. DevOps Demonstration Flow

For a college presentation, demonstrate:

```text
Code Push
   ↓
GitHub
   ↓
GitHub Actions
   ↓
JUnit Tests
   ↓
Docker Build
   ↓
Trivy Security Scan
   ↓
Docker Hub
   ↓
Kubernetes Deployment
   ↓
Spring Boot Application
   ↓
MySQL
```

## 10. API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | /api/health | Health check |
| GET | /api/products | Get all products |
| GET | /api/products/{id} | Get one product |
| POST | /api/products | Create product |
| PUT | /api/products/{id} | Update product |
| DELETE | /api/products/{id} | Delete product |
| POST | /api/orders | Create demo order |

## Project Objective

The objective is to demonstrate how a Java Spring Boot application can be tested, containerized, security-scanned, and deployed using modern DevOps practices.
