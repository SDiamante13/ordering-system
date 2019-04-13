#!/usr/bin/env bash

docker stop postgres-dev
docker rm postgres-dev

docker run --name postgres-dev -e POSTGRES_PASSWORD=password -e POSTGRES_DB=orderingsystem -d -p 5432:5432  postgres:alpine
#docker exec -it postgres-dev bash