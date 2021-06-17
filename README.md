# campsite-api

[![Java CI with Maven](https://github.com/louisthomas/campsite-api/actions/workflows/ci.yml/badge.svg)](https://github.com/louisthomas/campsite-api/actions/workflows/ci.yml)

REST API service that manage the campsite reservations

## Run Locally

Run PostgreSQL and Start Spring Boot application with

```bash
docker-compose up -d && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Stop PostgreSQL
```bash
docker-compose down
```

## API Reference

#### Get all available dates

```http
  GET /api/v1/availabilities?startDate=2021-06-26&endDate=2021-06-30
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `startDate` | `YYYY-MM-DD` | **Required**. LocalDate |
| `endDate` | `YYYY-MM-DD` |  LocalDate. Default value 30 days |

## Authors

- [@louisthomas](https://www.github.com/louisthomas)
