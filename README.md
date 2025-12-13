# Tarot Stack Trace

A simple, beginner-friendly tarot reading application supporting digital card draws, assisted physical deck readings, and interactive learning modes.




## Start FE App locally:
```bash
 cd ./frontend
 npm run-script dev
```

## Build Docker Image:
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

**Key Features**:
- ✅ Digital card drawing (1-card and 3-card spreads)
- ✅ Assisted reading mode for physical decks
- ✅ Learning mode with flashcards and quizzes
- ✅ Complete 78-card Rider-Waite deck database
- ✅ No authentication required

**Intentional Non-Features**:
- ❌ No AI interpretations
- ❌ No user accounts or social features
- ❌ No payment or premium features
- ❌ No reading journals or history

## Tech Stack

**Backend**:
- Java 21
- Spring Boot 4
- Maven
- REST API
- Docker

**Frontend**:
- Plain HTML
- Vanilla JavaScript
- Tailwind CSS

**DevOps**:
- GitHub Actions CI/CD
- DockerHub for image hosting

## Project Structure

```
tarot-stack-trace/
├── backend/                    # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/tarotstacktrace/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── config/
│   │   │   └── resources/
│   │   │       ├── cards.json  # 78 card database
│   │   │       └── static/
│   │   │           └── images/cards/
│   │   └── test/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                   # Static HTML/JS/CSS
│   ├── index.html
│   ├── draw.html
│   ├── assisted.html
│   ├── learn.html
│   ├── library.html
│   ├── js/
│   └── css/
├── .github/
│   └── workflows/
│       └── ci.yml
├── docker-compose.yml
├── PRODUCT_PLAN.md            # Comprehensive technical plan
└── README.md
```

## Quick Start

### Prerequisites
- Java 21
- Maven 3.9+
- Docker (optional, for containerized deployment)

### Run Backend Locally

```bash
cd backend
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

### Run Frontend Locally

```bash
cd frontend
# Using Python
python -m http.server 3000

# OR using npx
npx serve .
```

Frontend will be available at `http://localhost:3000`

### Run with Docker Compose

```bash
docker-compose up --build
```

- Backend: `http://localhost:8080`
- Frontend: `http://localhost:3000`

## Development

### Backend Commands

```bash
# Run tests
mvn test

# Build JAR
mvn clean package

# Build Docker image
docker build -t tarot-stack-trace:latest ./backend
```

### API Endpoints

**Cards**:
- `GET /api/cards` - Get all 78 cards
- `GET /api/cards/{id}` - Get card by ID
- `GET /api/cards/suit/{suit}` - Get cards by suit

**Readings**:
- `POST /api/readings/draw/single` - Draw one card
- `POST /api/readings/draw/three` - Draw three-card spread
- `POST /api/readings/manual` - Manual card selection

**Learning**:
- `GET /api/learning/flashcard/random` - Random flashcard
- `POST /api/learning/quiz/generate` - Generate quiz
- `POST /api/learning/quiz/submit` - Submit answers

## Documentation

- **[PRODUCT_PLAN.md](./PRODUCT_PLAN.md)** - Comprehensive product requirements, architecture, and implementation roadmap
- **[CLAUDE.md](./CLAUDE.md)** - Guide for AI-assisted development with Claude Code

## Roadmap

See [PRODUCT_PLAN.md - Phase 1-7](./PRODUCT_PLAN.md#7-implementation-roadmap) for detailed implementation steps.

**Current Phase**: Project Initialization

**Completed**:
- [x] Project planning and architecture design
- [x] Repository setup

**Next Steps**:
1. Initialize Spring Boot backend structure
2. Create card database (cards.json)
3. Implement card API endpoints
4. Build frontend pages
5. Set up Docker and CI/CD

## Contributing

This is an MVP project. Contributions should align with the defined scope in PRODUCT_PLAN.md.

## License

MIT License - See [LICENSE](./LICENSE)

## Contact

For questions or feedback, please open an issue on GitHub.
