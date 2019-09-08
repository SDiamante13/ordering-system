## Ordering System Rest API built using the art of TDD!!!

#### Commands to start up postgres docker container for test environment
docker pull postgres:alpine
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

docker stop postgres-dev
docker rm postgres-dev
docker start postgres-dev

## Jenkins CI/CD


docker run -d -v jenkins_home:/var/jenkins_home --name jenkins -p 8080:8080 -p 50000:50000 jenkins/jenkins:alpine

## GIT HOOKS
There is pre-commit that will run all tests before pushing.

## Adding images to database

1) Find and download the image you want
2) Navigate to the base64 encoder tool: https://www.base64-image.de/
3) Drag and drop your image in the tool
4) Click the copy image button to add the base64 encoded string to your clipboard
5) Update an existing product by using the following command:

        UPDATE products SET product_image='/9j/4AAQSkZJRgABAQEASABIAAD...' WHERE product_id=1; 

