# Uporabniki microservice

## Prerequisites

Create network **rsonet** if there is none:

```bash
docker network create rsonet
```

Run Consul Docker container:
```bash
docker run -d --name consul -p 8500:8500 --network rsonet consul
```

Running **pg-uporabniki** container:

```bash
docker run -d --name pg-uporabniki -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=customer -p 5432:5432 --network rsonet postgres:13
```

## Build and run Docker containers

Building **uporabniki** container:
```bash
docker build -f .\Dockerfile_with_maven_build -t uporabniki:latest .
```

Run:
```bash
docker run -p 8080:8080 --network rsonet -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-uporabniki:5432/customer -e KUMULUZEE_CONFIG_CONSUL_AGENT=http://consul:8500 --name uporabniki-instance uporabniki
```