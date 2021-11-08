# Customers microservice

## Prerequisites

Create network _rso-polnilnice_ if there is none:

```bash
docker network create rso-polnilnice
```

Running pg-customers container:

```bash
docker run -d --name pg-customer --network rso-polnilnice -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=customer -p 5432:5432 postgres:13
```


Running customers container: 

```bash
docker run --name customer-instance --network rso-polnilnice --publish 8080:8080 customer:latest
```