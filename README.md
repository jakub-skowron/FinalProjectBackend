# Conference Room Reservation System
The Conference Room Reservation System is a final project of the course. Program involves managing room reservations and utilizing simple CRUD operations.
This repository contains backend of the application. Link to the frontend can you find below:

### [Final Project Frontend](https://github.com/jakub-skowron/FinalProjectFrontend)

### Installation:
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
### Example of API response:

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

### What I want to add in future?
Priority:
- Integration tests,
- Connect frontend and backend using docker-compose,
- Equipment entity.







