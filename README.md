# Booking service application


## Description

A simple application, which you can use to manage bookings for services of companies


## Appliaction

- Spring Boot application
- MySql database
- KeyCloak IAM server


## Requirements

- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Docker](https://www.docker.com/products/docker-desktop/)


## Running the application locally

The application is dependent on both the KeyCloak server and the database.
The easiest way to run the application is using the `docker-compose.yml`.
For easier access, you can run `make run` from the terminal.
Also, you can run the database and KeyCloak server using Docker and start the application locally, it will connect to these.


### Help

You can find a Postman collection in the `postman` folder, which has some helpful endpoints listed in it
