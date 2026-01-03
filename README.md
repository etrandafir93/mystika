# Mystika

A simple, beginner-friendly tarot reading application supporting digital card draws, assisted physical deck readings, and interactive learning modes.

![mystika.gif](./docs/witch.gif)

## Build & push the app with Docker:
```bash
 ./mvnw versions:set -DnewVersion="x.y.z"
 
 # build the image, set a new tag version
 docker build -t mystika:x.y.z .
 docker images
 
 # run the docker image
 docker run -p 8080:8080 mystika:x.y.z

 # test the running container
 curl http://localhost:8080/api/cards
 curl http://localhost:8080/actuator/health
 curl http://localhost:8080/swagger-ui/index.html
 
 # push to docker hub
 docker login
 docker tag mystika:x.y.z emanueltrandafir/mystika:x.y.z
 docker push emanueltrandafir/mystika:x.y.z 
 

 # deploy yo gcloud
 gcloud auth login
 gcloud config set project n-vite
 gcloud auth print-access-token | docker login -u oauth2accesstoken --password-stdin https://europe-southwest1-docker.pkg.dev
 
 docker tag mystika:x.y.z europe-southwest1-docker.pkg.dev/n-vite/nvite-registry/mystika:x.y.z
 docker push europe-southwest1-docker.pkg.dev/n-vite/nvite-registry/mystika:x.y.z

# https://mystika-626515189021.europe-west1.run.app/
# https://mystika.etrandafir.com
```

## Overview

Tarot Stack Trace is an MVP designed for tarot enthusiasts and beginners who want to:
- Draw digital tarot cards for readings
- Get assistance when using a physical deck
- Learn and practice card meanings

**MVP Key Features**:
- ✅ Digital card spread (1-card and 3-card spreads)
- ✅ Assisted reading mode for physical decks
- ✅ Complete 78-card Rider-Waite deck database
- ✅ No authentication required

## Tech Stack

### Backend
- **Java 25**
- **Spring Boot 4.0.0**
- **SpringDoc OpenAPI** (Swagger UI documentation)
- **Maven** (build tool)

### Frontend
- **React 18.3.1**
- **Vite 6.0.3** (build tool & dev server)
- **Vanilla CSS** (styling)

### Infrastructure & Deployment
- **Docker** (containerization)
- **Google Cloud Platform**
  - Cloud Run (hosting)
  - Artifact Registry (container registry)
- **Node.js 20.11.0** (frontend build process)

## Project Structure

The application is organized into three main logical modules and key entities:

### 1. Cards and Decks
Contains static information about [cards](./src/main/java/com/mystika/tarot/cards/TarotCard.java)
and [decks](./src/main/java/com/mystika/tarot/cards/TarotDeck.java):
- Card images, names, symbols, and basic meanings
- Deck configurations and metadata
- Default support: **Rider-Waite deck** (78 cards)

This module serves as the foundation, providing the raw card data without interpretation.

### 2. Drawings
Manages different types of card spread patterns:
- `(WIP)` **1-card draw**: Single card for quick insights
- **[3-card spread](./src/main/java/com/mystika/tarot/reading/ThreeCardSpread.java)**: Past-Present-Future or other triadic layouts
- `(WIP)` **Celtic Cross**: Traditional 10-card spread 
- `(WIP)` Additional spread patterns 

**Dependencies**: Drawings depend on a Deck

**Responsibility**: Draws cards from the deck but does not attach meaning or interpretation. This module is purely mechanical - it selects and positions cards according to the spread pattern.

### 3. Readings
Provides interpretation and meaning to drawn cards:
- Performed by the **[seeker](./src/main/java/com/mystika/tarot/reading/Seeker.java)** entity (the person seeking guidance)
- Analyzes connections between cards in the spread
- Attaches deeper meaning beyond basic card definitions

**Dependencies**: a Reading is the interpretation of a Drawing (which uses a deck of cards)

**Interpretation Strategy**:
- `(WIP)` **AI-powered**: Uses AI to provide dynamic, contextualized interpretations
- **Static fallback**: Pre-written meanings when AI is unavailable (no I/O required)

**Flow**: Deck → Drawing → Reading
