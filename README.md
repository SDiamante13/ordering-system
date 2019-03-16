## Ordering System Rest API built using the art of TDD!!!

#### Commands to start up postgres docker container for test environment
docker pull postgres:alpine
docker stop postgres-dev
docker rm postgres-dev
docker run --name postgres-dev -e POSTGRES_PASSWORD=password -e POSTGRES_DB=orderingsystem -d -p 5432:5432  postgres:alpine

#### Commands to query PostgreSQL database

`Execute into running container`
>docker exec -it postgres-dev bash

`Sign in to postgres as the root user: postgres`
>psql -U postgres

`List of user roles for current user`
>\du

`Connect to database: orderingsystem`
>\c orderingsystem

`List all tables in current database schema`
>\dt

`List all from the table customers`
>SELECT * FROM CUSTOMERS;
