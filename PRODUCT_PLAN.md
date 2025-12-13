# Tarot Stack Trace - MVP Product & Technical Plan

## 1. MVP Scope Summary

**Vision**: A simple, beginner-friendly tarot reading application supporting digital card draws, assisted physical readings, and learning modes.

**Core Value Props**:
- Digital tarot readings without needing a physical deck
- Assistance for users with physical decks
- Educational tools for learning card meanings
- Zero barrier to entry (no authentication, no payment)

**Explicit Non-Goals for MVP**:
- AI-powered interpretations
- User accounts or authentication
- Social features or sharing
- Payment processing
- Reading journals or history
- Advanced spreads (Celtic Cross, etc.)
- Mobile native apps

**Tech Stack**:
- **Backend**: Java 25, Spring Boot 4, Maven, REST API
- **Frontend**: React 18, TypeScript, Vite, Tailwind CSS
- **Infrastructure**: Docker, GitHub Actions, DockerHub
- **Data**: In-memory or JSON file (no database for MVP)

---

## 2. User Requirements

### 2.1 Digital Card Drawing

**User Story 1**: Single Card Draw
> As a user, I want to draw a single tarot card digitally so I can get quick guidance on a question.

**Acceptance Criteria**:
- User clicks "Draw Single Card" button
- System randomly selects one card from 78-card deck
- Card displays with image, name, and upright/reversed orientation
- Card meaning is shown (upright and reversed)
- User can draw again to get a new card

**User Story 2**: Three-Card Spread
> As a user, I want to draw a three-card spread (Past-Present-Future) so I can explore a situation more deeply.

**Acceptance Criteria**:
- User clicks "Three Card Spread" button
- System randomly selects three unique cards
- Cards display in sequence: Past, Present, Future
- Each card shows orientation (upright/reversed)
- Meanings for all three cards are visible
- User can start a new spread

### 2.2 Assisted Physical Deck Reading

**User Story 3**: Manual Card Selection
> As a user with a physical deck, I want to manually select cards I've drawn so the app can show me their meanings.

**Acceptance Criteria**:
- User indicates they're using a physical deck
- User can search or browse to select cards
- User can mark each card as upright or reversed
- User can build a 1-card or 3-card spread manually
- Meanings display once cards are selected
- User can clear and start over

### 2.3 Learning Mode

**User Story 4**: Browse Card Library
> As a beginner, I want to browse all 78 tarot cards so I can familiarize myself with the deck.

**Acceptance Criteria**:
- User can view all cards in a grid or list
- Cards are organized by suit (Major Arcana, Cups, Wands, Swords, Pentacles)
- Clicking a card shows its full details and meanings
- Navigation between cards is intuitive

**User Story 5**: Flashcard Learning
> As a learner, I want to practice card meanings with a flashcard interface so I can memorize them.

**Acceptance Criteria**:
- User enters "Learning Mode"
- System shows a random card image
- User attempts to recall the meaning
- User clicks "Reveal" to see the correct meaning
- User can mark "Got it" or "Review again"
- User can continue through multiple cards

**User Story 6**: Simple Quiz
> As a learner, I want to test my knowledge with a simple quiz so I can track my progress.

**Acceptance Criteria**:
- System shows a card image
- User selects from multiple-choice meanings
- Immediate feedback (correct/incorrect)
- Score is displayed at the end
- No persistent progress tracking in MVP

### 2.4 General UX Requirements

- Clean, minimalist interface
- Mobile-responsive design
- Fast load times
- Clear navigation between modes
- Accessible card images (alt text)
- No account required to use any feature

---

## 3. System Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                      USER BROWSER                        │
│  ┌───────────────────────────────────────────────────┐  │
│  │  React App (Vite + TypeScript + Tailwind)        │  │
│  │  - HomePage (mode selection)                     │  │
│  │  - DrawPage (digital drawing)                    │  │
│  │  - AssistedPage (physical deck)                  │  │
│  │  - LearnPage (flashcards/quiz)                   │  │
│  │  - LibraryPage (card browser)                    │  │
│  │  - React Router for navigation                   │  │
│  └───────────────────────────────────────────────────┘  │
│                          │                               │
│                          │ HTTP/REST (axios/fetch)       │
│                          ▼                               │
└─────────────────────────────────────────────────────────┘
                           │
                           │
┌──────────────────────────┼────────────────────────────┐
│                          ▼                             │
│              Spring Boot Application                   │
│  ┌─────────────────────────────────────────────────┐  │
│  │  REST Controllers                               │  │
│  │  - CardController                               │  │
│  │  - ReadingController                            │  │
│  │  - LearningController                           │  │
│  └─────────────────────────────────────────────────┘  │
│                          │                             │
│  ┌─────────────────────────────────────────────────┐  │
│  │  Service Layer                                  │  │
│  │  - CardService                                  │  │
│  │  - ReadingService                               │  │
│  │  - LearningService                              │  │
│  └─────────────────────────────────────────────────┘  │
│                          │                             │
│  ┌─────────────────────────────────────────────────┐  │
│  │  Data Layer (In-Memory)                         │  │
│  │  - CardRepository (loads from cards.json)       │  │
│  │  - 78 Rider-Waite cards with metadata           │  │
│  └─────────────────────────────────────────────────┘  │
│                                                        │
│              Port 8080 (configurable)                  │
└────────────────────────────────────────────────────────┘
                           │
                           │ Packaged as
                           ▼
                    Docker Container
                    (published to DockerHub)
```

**Key Architectural Decisions**:
1. **Stateless Backend**: Each request is independent; no session management
2. **In-Memory Data**: Cards loaded from JSON at startup (fast, simple)
3. **REST API**: Clear separation between frontend and backend
4. **Docker-First**: Backend runs in container for consistency
5. **React SPA**: Single-page application with client-side routing
6. **Vite Build Tool**: Fast development and optimized production builds

---

## 4. Backend Design

### 4.1 Package Structure

```
src/main/java/com/tarotstacktrace/
├── TarotStackTraceApplication.java
├── controller/
│   ├── CardController.java
│   ├── ReadingController.java
│   └── LearningController.java
├── service/
│   ├── CardService.java
│   ├── ReadingService.java
│   └── LearningService.java
├── model/
│   ├── TarotCard.java
│   ├── Reading.java
│   ├── Spread.java
│   └── LearningSession.java
├── repository/
│   └── CardRepository.java
└── config/
    └── CorsConfig.java

src/main/resources/
├── application.properties
├── cards.json
└── static/ (optional: serve frontend here)
    └── images/
        └── cards/ (78 card images)
```

### 4.2 Data Models

**TarotCard**
```java
{
  "id": "major-0",
  "name": "The Fool",
  "suit": "MAJOR_ARCANA",
  "number": 0,
  "arcana": "MAJOR",
  "uprightMeaning": "New beginnings, innocence, spontaneity...",
  "reversedMeaning": "Recklessness, taken advantage of...",
  "keywords": ["beginnings", "innocence", "spontaneity"],
  "imageUrl": "/images/cards/fool.jpg"
}
```

**Reading**
```java
{
  "id": "uuid",
  "timestamp": "2025-12-13T...",
  "spreadType": "THREE_CARD" | "SINGLE_CARD",
  "cards": [
    {
      "card": { TarotCard },
      "position": "PAST" | "PRESENT" | "FUTURE" | "SINGLE",
      "orientation": "UPRIGHT" | "REVERSED"
    }
  ]
}
```

**LearningSession** (for quiz)
```java
{
  "questions": [
    {
      "card": { TarotCard },
      "options": ["meaning1", "meaning2", "meaning3", "meaning4"],
      "correctAnswer": 0
    }
  ],
  "score": 0,
  "totalQuestions": 10
}
```

### 4.3 REST API Endpoints

**Card Management**
- `GET /api/cards` - Get all cards
- `GET /api/cards/{id}` - Get single card by ID
- `GET /api/cards/suit/{suit}` - Get cards by suit (MAJOR_ARCANA, CUPS, WANDS, SWORDS, PENTACLES)

**Digital Reading**
- `POST /api/readings/draw/single` - Draw one random card
  - Response: `{ "card": TarotCard, "orientation": "UPRIGHT|REVERSED" }`
- `POST /api/readings/draw/three` - Draw three-card spread
  - Response: `Reading` object with 3 cards

**Assisted Reading**
- `POST /api/readings/manual` - Create reading from manually selected cards
  - Request: `{ "cardIds": ["major-0", "cups-3"], "orientations": ["UPRIGHT", "REVERSED"], "spreadType": "THREE_CARD" }`
  - Response: `Reading` object

**Learning Mode**
- `GET /api/learning/flashcard/random` - Get random card for flashcard
  - Response: `{ "card": TarotCard }`
- `POST /api/learning/quiz/generate` - Generate quiz with N questions
  - Request: `{ "questionCount": 10 }`
  - Response: `LearningSession` (without correct answers exposed)
- `POST /api/learning/quiz/submit` - Submit quiz answers
  - Request: `{ "sessionId": "...", "answers": [0, 2, 1, ...] }`
  - Response: `{ "score": 7, "total": 10, "results": [...] }`

### 4.4 Key Backend Components

**CardService**
- Load 78 cards from `cards.json` at startup
- Provide card lookup by ID, suit, or random selection
- Validate card existence

**ReadingService**
- Generate random orientations (50/50 upright/reversed)
- Ensure unique cards in multi-card spreads
- Build Reading objects with metadata

**LearningService**
- Generate random flashcards
- Create quiz sessions with multiple-choice options
- Calculate quiz scores

**CardRepository**
- In-memory storage using `Map<String, TarotCard>`
- Loads from `src/main/resources/cards.json` using Jackson
- Provides fast lookup and filtering

### 4.5 Error Handling Strategy

- Use Spring `@ControllerAdvice` for global exception handling
- Return consistent error format:
  ```json
  {
    "timestamp": "2025-12-13T...",
    "status": 404,
    "error": "Not Found",
    "message": "Card with ID 'invalid-id' not found",
    "path": "/api/cards/invalid-id"
  }
  ```
- Common errors:
  - `404`: Card not found
  - `400`: Invalid request (e.g., duplicate cards, invalid spread type)
  - `500`: Server error (e.g., failed to load cards.json)

### 4.6 CORS Configuration

- Allow frontend to call backend from different origin
- Configure in `CorsConfig.java`:
  ```java
  @Configuration
  public class CorsConfig implements WebMvcConfigurer {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/api/**")
                  .allowedOrigins("*")
                  .allowedMethods("GET", "POST");
      }
  }
  ```

---

## 5. Frontend Design

### 5.1 Pages / Views

**index.html** - Landing / Mode Selection
- Hero section with app description
- Three large buttons:
  - "Digital Card Draw"
  - "Assisted Reading (Physical Deck)"
  - "Learn Tarot"
- Additional link to "Browse All Cards"

**draw.html** - Digital Card Drawing
- Tab or buttons to switch: "Single Card" | "Three Card Spread"
- Card display area (responsive grid)
- "Draw Cards" button
- Card flip animation (optional enhancement)
- Display: card image, name, orientation, meaning

**assisted.html** - Assisted Physical Reading
- Search bar to find cards by name
- Grid of all 78 cards (clickable)
- Selected cards area (shows 1-3 cards)
- Toggle for upright/reversed per card
- "Show Meanings" button
- Display meanings for selected cards

**learn.html** - Learning Mode
- Sub-navigation: "Flashcards" | "Quiz"
- **Flashcard Mode**:
  - Card image displayed
  - "Reveal Meaning" button
  - Meaning appears below
  - "Next Card" button
- **Quiz Mode**:
  - Start quiz button (e.g., "10 Questions")
  - Question counter (1/10)
  - Card image
  - 4 multiple-choice options
  - Submit button
  - Score display at end

**library.html** - Card Browser
- Filter by suit (tabs or dropdown)
- Grid of all cards
- Click card to open modal/detail view
- Detail shows: image, name, upright/reversed meanings, keywords

### 5.2 State Management

**No Framework = Vanilla JS State**
- Use simple JavaScript objects to track state:
  ```javascript
  const state = {
    currentReading: null,
    selectedCards: [],
    flashcardIndex: 0,
    quizSession: null,
    allCards: []
  };
  ```
- Fetch data from backend and update DOM directly
- Use `fetch()` API for all HTTP requests
- Store minimal state in `sessionStorage` if needed (e.g., current reading)

### 5.3 API Communication

**Example Fetch Pattern**:
```javascript
async function drawSingleCard() {
  try {
    const response = await fetch('http://localhost:8080/api/readings/draw/single', {
      method: 'POST'
    });
    const data = await response.json();
    displayCard(data);
  } catch (error) {
    showError('Failed to draw card. Please try again.');
  }
}
```

### 5.4 Styling with Tailwind CSS

- Use Tailwind CDN for MVP (simplicity)
- Responsive grid for card displays
- Card component classes (shadow, hover effects)
- Button styles (primary, secondary)
- Modal/overlay for card details

---

## 6. DevOps & CI/CD

### 6.1 Docker Strategy

**Dockerfile** (Multi-stage build)
```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/tarot-stack-trace.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**docker-compose.yml** (for local development)
```yaml
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
  frontend:
    image: nginx:alpine
    ports:
      - "3000:80"
    volumes:
      - ./frontend:/usr/share/nginx/html
```

### 6.2 GitHub Actions Pipeline

**Workflow Goals**:
1. Run tests on every push/PR
2. Build Docker image on push to `main`
3. Push image to DockerHub with version tags

**Pipeline Steps**:
- Checkout code
- Set up Java 21
- Cache Maven dependencies
- Run `mvn test`
- Build with `mvn package`
- Build Docker image
- Tag with commit SHA and `latest`
- Push to DockerHub (using secrets)

**Required GitHub Secrets**:
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`

---

## 7. Implementation Roadmap

### Phase 1: Project Setup & Foundation
**Goal**: Get basic project structure running

1. **Initialize Backend**
   - Create Spring Boot project (Spring Initializr: Web, DevTools, Lombok)
   - Set up package structure
   - Configure `application.properties`
   - Create basic health check endpoint (`GET /api/health`)

2. **Initialize Frontend**
   - Create folder structure (`frontend/`, `frontend/js/`, `frontend/css/`, `frontend/images/`)
   - Create `index.html` with Tailwind CDN
   - Set up basic navigation structure

3. **Create Card Data**
   - Research Rider-Waite card meanings (public domain)
   - Create `cards.json` with all 78 cards
   - Find/generate public domain card images
   - Store images in `src/main/resources/static/images/cards/`

4. **Docker & CI Setup**
   - Write `Dockerfile` for backend
   - Write `docker-compose.yml`
   - Create `.github/workflows/ci.yml`
   - Test local Docker build

### Phase 2: Core Backend (Card API)
**Goal**: Serve card data via REST

5. **Implement Data Layer**
   - Create `TarotCard` model
   - Create `CardRepository` with JSON loading
   - Write unit tests for repository

6. **Implement Card Endpoints**
   - `CardController`: GET all cards, get by ID, get by suit
   - `CardService`: Business logic for card retrieval
   - Test endpoints with Postman/curl

7. **Error Handling**
   - Create `@ControllerAdvice` for global errors
   - Handle 404, 400, 500 cases
   - Return consistent error JSON

### Phase 3: Digital Reading Feature
**Goal**: Users can draw cards digitally

8. **Backend: Reading Service**
   - Create `Reading`, `Spread` models
   - Implement `ReadingService`: random card selection, orientation
   - Create `ReadingController`: draw single, draw three
   - Write tests for randomness and uniqueness

9. **Frontend: Draw Page**
   - Create `draw.html` with UI for single/three-card
   - Implement JavaScript to call `/api/readings/draw/*`
   - Display cards with images and meanings
   - Add "Draw Again" functionality

10. **Integration Testing**
    - Test full flow: button click → API call → display
    - Verify no duplicate cards in three-card spread

### Phase 4: Assisted Reading Feature
**Goal**: Users can select physical cards manually

11. **Backend: Manual Reading**
    - Add endpoint `POST /api/readings/manual`
    - Validate card IDs and orientations
    - Return Reading object

12. **Frontend: Assisted Page**
    - Create `assisted.html`
    - Build card selector (search + grid)
    - Add orientation toggle (upright/reversed)
    - Submit selection and display meanings

### Phase 5: Learning Mode
**Goal**: Users can learn card meanings

13. **Backend: Learning Service**
    - Implement flashcard endpoint (random card)
    - Implement quiz generation (10 questions, multiple choice)
    - Implement quiz scoring

14. **Frontend: Learning Page**
    - Create `learn.html` with flashcard UI
    - Implement "Reveal" interaction
    - Build quiz UI (question counter, options, score)

15. **Card Library Page**
    - Create `library.html`
    - Display all 78 cards in filterable grid
    - Modal/detail view for card meanings

### Phase 6: Polish & Deploy
**Goal**: Production-ready MVP

16. **Frontend Polish**
    - Responsive design testing (mobile, tablet, desktop)
    - Loading states and error messages
    - Smooth transitions/animations

17. **Backend Optimization**
    - Enable GZIP compression
    - Add request logging
    - Performance testing (load cards.json efficiently)

18. **Documentation**
    - Write README with setup instructions
    - Document API endpoints (simple markdown or Swagger)
    - Add inline code comments

19. **CI/CD Finalization**
    - Test GitHub Actions pipeline end-to-end
    - Verify DockerHub push
    - Create release tagging strategy

20. **Deployment**
    - Deploy to cloud platform (optional: Render, Railway, AWS)
    - Test production environment
    - Share app with initial users

### Phase 7: Post-MVP (Future Enhancements)
**Not in scope for MVP, but documented for later**:
- User accounts and reading history
- Journal entries for readings
- Advanced spreads (Celtic Cross, etc.)
- AI-powered interpretations
- Mobile app (React Native / Flutter)
- Database persistence (PostgreSQL)
- Social sharing features

---

## Appendix: Development Commands

**Backend**:
```bash
# Run locally
mvn spring-boot:run

# Run tests
mvn test

# Build JAR
mvn clean package

# Build Docker image
docker build -t tarot-stack-trace:latest .

# Run Docker container
docker run -p 8080:8080 tarot-stack-trace:latest
```

**Frontend**:
```bash
# Serve with simple HTTP server (for testing)
cd frontend
python -m http.server 3000
# or
npx serve .
```

**Docker Compose**:
```bash
# Start all services
docker-compose up

# Rebuild and start
docker-compose up --build

# Stop all services
docker-compose down
```

---

## Success Metrics for MVP

- [ ] All 78 cards load correctly with images
- [ ] Digital draw works (single + three-card)
- [ ] Assisted reading allows manual card selection
- [ ] Flashcard mode displays random cards
- [ ] Quiz generates 10 questions and scores correctly
- [ ] Card library shows all cards with filtering
- [ ] Frontend is mobile-responsive
- [ ] Backend passes all unit tests
- [ ] Docker image builds and runs successfully
- [ ] GitHub Actions pipeline deploys to DockerHub
- [ ] No crashes or errors in normal usage

---

**Document Version**: 1.0
**Last Updated**: 2025-12-13
**Status**: Ready for Implementation
