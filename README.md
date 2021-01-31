# RoomFix - Spring Boot API

## Context



<p align="center">
<img width="300" height="300" src="https://i.ibb.co/tKtb8mv/roomfix8-logo-light.png" alt="roomfix8-logo-light">
</p>



This project is a Spring Boot API with Hibernate / JPA as ORM.
The database used is a MySQL.

## Configuration

The first thing to do to launch the application is to configure the parameters specific to your machine.
All parameters have default values made to make it easier to get started.

| Type  | Format               | Example                | Environment Variable |
|-------|----------------------|------------------------|----------------------|
| MySQL | user:mdp@host/dbname | root@localhost/roomfix | DB_URL               |
| SMTP  | smtp.xxxx.xxx        | smtp.gmail.com         | SMTP_HOST            |
| SMTP  | integer              | 587                    | SMTP_PORT            |
| SMTP  | email                | roomfix@gmail.com      | SMTP_USERNAME        |
| SMTP  |                      | abc123                 | SMTP_PASSWORD        ||


[Easily edit this table on TablesGenerator](https://www.tablesgenerator.com/markdown_tables)


## Development Profile

We are using [Spring profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html)
to configure variables specific to each environment (dev, production, etc.).

To launch the application locally, you must run it with the `dev` profile

* wil IntelliJ : [follow tutorial on StackOverflow](https://stackoverflow.com/a/39775038/7248759)
* wil Maven + springplugin : `./mvnw spring-boot:run`

## Recommended Intellij plugins

- Lombok: for POJOs (java beans) we use @data to auto-generate getters, setters and constructors. This plugin to add
à Intellij allows the IDE to understand that these functions exist but are not explicitly written.
(Prevent the IDE from saying it can't find the functions)


## API Endpoints

Every endpoint are within a collection postman.


## Docker

Docker here is used to run the API in production.

To build the docker image.

```
docker build . -t roomfix-api:v1
```

Run the container on port 8080 of the machine (accessible on port 8080 of localhost for example).

```
docker run <image-roomfix-api> -p 8080:8000 -e DB_URL=<database-user>:<database-password>@<database-url>:3306/roomfix
```

Example:
```
docker run -d -p 8000:8000 -e "DB_URL=roomfix:PASSWORD@databases.pouretadev.com:3306/roomfix_master" roomfix-api:v1
```


If you are running MySQL with Docker, remember to change `localhost` to` <mysql-container-name> `so that the containers can communicate (MacOS and Windows network card do not allow Docker to communicate with each other on the localhost).


It is also possible to run the image with the docker-compose which contains a MySQL docker and what to run the API (you need to build it with the instructions above)

To run API + MySQL :
```
docker-compose up -d
```

to run MySQL only
```
docker-compose up mysqldb -d
```










