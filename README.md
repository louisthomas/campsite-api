# campsite-api

[![Java CI with Maven](https://github.com/louisthomas/campsite-api/actions/workflows/ci.yml/badge.svg)](https://github.com/louisthomas/campsite-api/actions/workflows/ci.yml)
[![OpenSSF Scorecard](https://api.securityscorecards.dev/projects/github.com/louisthomas/campsite-api/badge)](https://api.securityscorecards.dev/projects/github.com/louisthomas/campsite-api)

REST API service that manage the campsite reservations

An underwater volcano formed a new small island in the Pacific Ocean last month. All the conditions on the island seems perfect and it was
decided to open it up for the general public to experience the pristine uncharted territory.
The island is big enough to host a single campsite so everybody is very excited to visit. In order to regulate the number of people on the island, it
was decided to come up with an online web application to manage the reservations. You are responsible for design and development of a REST
API service that will manage the campsite reservations.
To streamline the reservations a few constraints need to be in place -

* The campsite will be free for all.
* The campsite can be reserved for max 3 days.
* The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
* Reservations can be cancelled anytime.
* For sake of simplicity assume the check-in & check-out time is 12:00 AM

## System Requirements

* The users will need to find out when the campsite is available. So the system should expose an API to provide information of the
availability of the campsite for a given date range with the default being 1 month.
* Provide an end point for reserving the campsite. The user will provide his/her email & full name at the time of reserving the campsite
along with intended arrival date and departure date. Return a unique booking identifier back to the caller if the reservation is successful.
* The unique booking identifier can be used to modify or cancel the reservation later on. Provide appropriate end point(s) to allow
modification/cancellation of an existing reservation
* Due to the popularity of the island, there is a high likelihood of multiple users attempting to reserve the campsite for the same/overlapping
date(s). Demonstrate with appropriate test cases that the system can gracefully handle concurrent requests to reserve the campsite.
* Provide appropriate error messages to the caller to indicate the error cases.
* In general, the system should be able to handle large volume of requests for getting the campsite availability.
* There are no restrictions on how reservations are stored as as long as system constraints are not violated.

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

[Query examples](rest-api-request.http)

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

## GraphQL reference

[Query examples](graphql-request.http)

### Get all bookings
```bash
curl -X POST --location "http://localhost:8080/graphql" \
    -H "Content-Type: application/graphql" \
    -d "query{
  booking {
    id
    email
    fullName
    startDate
    endDate
  }
}"
```

## Deployment

It uses Heroku to deploy, for details see [deploy pipeline](.github/workflows/deploy.yml) and [application 
manifest](app.json).

URL: [https://campsite-reservation-ltl.herokuapp.com/actuator/health](https://campsite-reservation-ltl.herokuapp.com/)

Actuator health check endpoint: [https://campsite-reservation-ltl.herokuapp.com/actuator/health](https://campsite-reservation-ltl.herokuapp.com/actuator/health)
## Authors

- [@louisthomas](https://www.github.com/louisthomas)
