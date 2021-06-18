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

[Query](RestAPIExample.http)

#### Get all available dates

```bash
curl -X GET --location "http://localhost:8080/api/v1/availabilities?startDate=2021-06-17&endDate=2021-06-30"
```

#### Create new booking

```bash
curl -X POST --location "http://localhost:8080/api/v1/bookings/" \
    -H "Content-Type: application/json" \
    -d "{
          \"email\" : \"ltlamontagne@gmail.com\",
          \"fullName\": \"Louis-Thomas Lamontagne\",
          \"startDate\": \"2021-06-23\",
          \"endDate\": \"2021-06-26\"
        }"
```

#### Update booking

```bash
curl -X PUT --location "http://localhost:8080/api/v1/bookings/3f46333f-c668-4177-9c3c-0c3e35613a52" \
    -H "Content-Type: application/json" \
    -d "{
          \"email\" : \"ltlamontagne@gmail.com\",
          \"fullName\": \"Tony Amonte\",
          \"startDate\": \"2021-09-12\",
          \"endDate\": \"2021-09-15\"
        }"
```

#### Get Booking

```bash
curl -X GET --location "http://localhost:8080/api/v1/bookings/3f46333f-c668-4177-9c3c-0c3e35613a52" \
    -H "Accept: application/json"
```

#### Delete booking

```bash
curl -X DELETE --location "http://localhost:8080/api/v1/bookings/3544e65d-f46f-4894-b543-b8c1fd94fdd2"
```


## Authors

- [@louisthomas](https://www.github.com/louisthomas)
