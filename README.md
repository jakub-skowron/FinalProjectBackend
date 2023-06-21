# Conference Room Reservation System
The Conference Room Reservation System is a final project of the course. Program involves managing room reservations and utilizing simple CRUD operations.
This repository contains backend of the application. Link to the frontend can you find below:

### [Final Project Frontend](https://github.com/jakub-skowron/FinalProjectFrontend)

## Installation:
- Docker (version 24.0.2 or later)
- Docker Compose (version v2.15.1 or later)

Clone the repository:

- Open a terminal or command prompt window.
Navigate to the directory where you want to store your application.
Clone the repository by running the following: 
```
$ git clone https://github.com/jakub-skowron/FinalProjectBackend.git
```

Use docker-compose to create and run containers:

- Open a terminal or command prompt window.
Navigate to the root directory of your application.
Use commands as following:
```
$ docker compose up --build
```

 - To stop the app and its containers, press Ctrl + C in the terminal where docker-compose up is running. You can also stop and remove containers by following:
```
$ docker compose down
```
## API Endpoints

### Organization

#### Get all organizations

```http
  GET /organizations
```
Example of API response:
```json
[
    {
        "id": 2,
        "name": "BMW",
        "rooms": [
            {
                "id": 1,
                "reservations": [
                    {
                        "id": 1,
                        "identifier": "R3",
                        "startReservationDateTime": "2023-06-06T01:43:00",
                        "endReservationDateTime": "2023-06-06T02:42:00",
                        "roomId": 0
                    }
                ],
                "name": "Room1",
                "identifier": "R1",
                "level": 6,
                "availability": true,
                "places": {
                    "SITTING": 5,
                    "STANDING": 2
                },
                "organizationId": 0
            }
        ]
    }
]
```
#### Get organization by id

```http
  GET /organizations/${id}
```
Example of API response:
```json
    {
        "id": 2,
        "name": "BMW",
        "rooms": [
            {
                "id": 1,
                "reservations": [
                    {
                        "id": 1,
                        "identifier": "R3",
                        "startReservationDateTime": "2023-06-06T01:43:00",
                        "endReservationDateTime": "2023-06-06T02:42:00",
                        "roomId": 0
                    }
                ],
                "name": "Room1",
                "identifier": "R1",
                "level": 6,
                "availability": true,
                "places": {
                    "SITTING": 5,
                    "STANDING": 2
                },
                "organizationId": 0
            }
        ]
    }
```
#### Add organization

```http
  POST /organizations
```
Example of API input:
```json
{
    "name": "BMW"
}
```
#### Update organization by id

```http
  PUT /organizations}/${id}
```
Example of API input:
```json
{
    "name": "Mercedes"
}
```

#### Delete organization by Id

```http
  DELETE /organizations/${id}
```

### Room

#### Get all rooms

```http
  GET /rooms
```

Example of API response:
```json
[
    {
        "id": 3,
        "reservations": [],
        "name": "Room1",
        "identifier": "R1",
        "level": 1,
        "availability": true,
        "places": {
            "STANDING": 2,
            "SITTING": 2
        },
        "organizationId": 0
    }
]
```

#### Get room by id

```http
  GET /rooms/${id}
```

Example of API response:
```json
{
    "id": 3,
    "reservations": [],
    "name": "Room1",
    "identifier": "R1",
    "level": 1,
    "availability": true,
    "places": {
        "STANDING": 2,
        "SITTING": 2
    },
    "organizationId": 0
}
```
#### Add room

```http
  POST /rooms
```
Example of API input:
```json
{
    "name": "Room1",
    "identifier": "R1",
    "level": 1,
     "places": {
    "SITTING": 5,
    "STANDING": 6
  },
  "organizationId": 3
}
```
#### Update room by id

```http
  PUT /rooms/${id}
```
Example of API input:
```json
{
    "name": "Room2",
    "identifier": "R2",
    "level": 1,
     "places": {
    "SITTING": 5,
    "STANDING": 6
  },
  "organizationId": 3
}
```

#### Delete room by id

```http
  Delete /rooms/${id}
```

### Reservation

#### Get all reservations

```http
  GET /reservations
```

Example of API response:
```json
[
    {
        "id": 3,
        "identifier": "R1",
        "startReservationDateTime": "2023-06-06T10:14:00",
        "endReservationDateTime": "2023-06-06T11:13:00",
        "roomId": 0
    }
]
```

#### Get reservations by id

```http
  GET /reservations/${id}
```

Example of API response:
```json
{
    "id": 3,
    "identifier": "R1",
    "startReservationDateTime": "2023-06-06T10:14:00",
    "endReservationDateTime": "2023-06-06T11:13:00",
    "roomId": 0
}
```
#### Add reservation

```http
  POST /reservations
```
Example of API input:
```json
{
    "identifier": "R5",
    "startReservationDateTime": "2023-06-06T13:14:00",
    "endReservationDateTime": "2023-06-06T13:34:00",
    "roomId": 3
}
```
#### Update reservation by id

```http
  PUT /reservations/${id}
```
Example of API input:
```json
{
    "id": 3,
    "identifier": "R1",
    "startReservationDateTime": "2023-06-06T10:14:00",
    "endReservationDateTime": "2023-06-06T11:13:00",
    "roomId": 3
}
```

#### Delete reservation by id

```http
  Delete /reservations/${id}
```

## What I want to add in future?
Priority:
- Api documentation (Endpoints in Readme.md or Swagger), 
- Integration tests,
- Connect frontend and backend using docker-compose,
- Equipment entity.







