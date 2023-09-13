# Sum API

Solución para sumar dos valores y aplicar una suba de un porcentaje.

## Herramientas y tecnologías implementadas

- Docker
- Docker compose
- Java 17
- JUnit5
- Spring Boot
- Spring Framework
- Kafka
- Spring Cloud OpenFeign
- Spring Retry
- Redis
- Bucket4j
- Lombok
- Base de datos PostgreSQL
- IntelliJ IDEA
- Postman

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- [Docker](https://docs.docker.com/get-docker/) - Para contenerizar la aplicación.
- [Postman](https://www.postman.com/) - Herramienta para probar las APIs.

## Despliegue de la Aplicación

Para poner en marcha la aplicación, sigue estos pasos:

1. Inicia Docker en tu equipo.
2. Ejecuta el siguiente comando (en la raíz del proyecto) para desplegar la aplicación:

```sh
docker compose up
```

El repositorio de Docker Hub se encuentra
en: [germanrodriguez04/sum_api](https://hub.docker.com/repository/docker/germanrodriguez04/sum_api)

## Características

### 1. Suma

Realiza una suma sencilla aplicando una suba de porcentaje aleatorio con los siguientes parámetros:

#### Request

```sh
GET /sum?addendOne={addendOne}&addendTwo={addendTwo}
```

Los valores de `addendOne` y `addendTwo` deben ser enteros positivos.

#### Response

- `HttpStatus: 200 OK`

```json
{
  "result": 36.75
}
```

- `HttpStatus: 400 Bad Request`

```json
{
  "reason": "BAD_REQUEST",
  "description": "[getSumWithPercentage.addendOne: must be greater than or equal to 0]"
}
```

### 2. Historial de Llamadas

Historial de las solicitudes realizadas:

#### Request

```sh
GET /call-history?page={page}&size={size}
```

#### Ejemplos de Filtros de Paginación:

- page: número de la página a consultar.
- size: cantidad de registros por página.
- sort: orden de visualización de los registros (ejemplo: sort=requestPath,desc).

#### Response

Respuesta detallada que incluye el estado de la solicitud, el método solicitado, y más.

- `HttpStatus: 200 OK`

```json
{
  "content": [
    {
      "methodName": "getSumWithPercentage",
      "requestMethod": "GET",
      "requestPath": "/sum?addendOne=20&addendTwo=15",
      "responseStatus": 200,
      "responseBody": "SumResponseDto(result=36.75)",
      "creationDatetime": "2023-09-13T08:10:47.949033"
    }
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 8,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 3,
  "last": true,
  "size": 8,
  "number": 0,
  "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
  },
  "numberOfElements": 3,
  "first": true,
  "empty": false
}
```