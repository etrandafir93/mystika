# Mystika

A simple, beginner-friendly tarot reading application supporting digital card draws, assisted physical deck readings, and interactive learning modes.

![mystika.gif](./docs/witch.gif)

## Build & push the app with Docker:
```bash
 # build the image, set a new tag version
 docker build -t mystika:0.1 .
 
 # check the image is created
 docker images
 
 # run the docker image
 docker run -p 8080:8080 mystika:0.1

 # test the running container
 curl http://localhost:8080/api/cards
 curl http://localhost:8080/actuator/health
 curl http://localhost:8080/swagger-ui/index.html
 
 # push to docker hub
 docker login
 docker tag mystika:0.1 emanueltrandafir/mystika:0.1
 docker push emanueltrandafir/mystika:0.1 
```

## Overview

Tarot Stack Trace is an MVP designed for tarot enthusiasts and beginners who want to:
- Draw digital tarot cards for readings
- Get assistance when using a physical deck
- Learn and practice card meanings

**MVP Key Features**:
- ✅ Digital card drawing (1-card and 3-card spreads)
- ✅ Assisted reading mode for physical decks
- ✅ Complete 78-card Rider-Waite deck database
- ✅ No authentication required

## Tech Stack

**Backend**:
- Java 21
- Spring Boot 4
- Maven
- REST API
- Docker

**Frontend**:
- React 18
- Vite
- React Router
- CSS Modules

**DevOps**:
- GitHub Actions CI/CD
- DockerHub for image hosting

### API Endpoints

**Cards**:
- `GET /api/cards` - Get all 78 cards
- `GET /api/cards/{id}` - Get card by ID

**Readings**:
- `POST /api/readings/draw/single` - Draw one card
- `POST /api/readings/draw/three` - Draw three-card spread
- `POST /api/readings/manual` - Manual card selection