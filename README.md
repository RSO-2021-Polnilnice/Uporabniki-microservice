# Uporabniki microservice

## Prerequisites

Create network **rso-polnilnice** if there is none:

```bash
docker network create rso-polnilnice
```

Running **pg-uporabniki** container:

```bash
docker run -d --name pg-uporabniki --network rso-polnilnice -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=customer -p 5432:5432 postgres:13
```

## Build and run Docker containers

Building **uporabniki** container:
```bash
docker build -f .\Dockerfile_with_maven_build -t uporabniki:latest .
```


Running **uporabniki** container: 

```bash
docker run --name uporabniki-instance --network rso-polnilnice --publish 8080:8080 uporabniki:latest
```