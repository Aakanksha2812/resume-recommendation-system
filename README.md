# 🎯 Resume Recommendation System

An AI-powered resume recommendation system built with event-driven microservices architecture.
Upload your resume and get top job matches powered by RAG (Retrieval Augmented Generation).

---

## 🏗️ Architecture

```
Candidate → ResumeService → Kafka → ResumeParserService
         → Kafka → RecommendationService (RAG) → Kafka → NotificationService
```

### Flow:
1. Candidate uploads resume via React frontend
2. **ResumeService** extracts text from PDF and publishes event to Kafka
3. **ResumeParserService** consumes event, extracts skills using keyword matching
4. **RecommendationService** uses RAG to find top 3 matching jobs from ChromaDB and generates explanation using Ollama (LLM)
5. **NotificationService** consumes final event and notifies candidate
6. React frontend polls for recommendations and displays results

---

## 🛠️ Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 17 | Primary language |
| Spring Boot 3.3 | Microservices framework |
| Apache Kafka | Async event-driven communication |
| MongoDB | Data persistence |
| Spring AI 1.0.1 | RAG implementation |
| ChromaDB | Vector store for job descriptions |
| Ollama (llama3.2) | Local LLM for explanation generation |
| Apache PDFBox | PDF text extraction |
| Docker | Infrastructure containerization |

### Frontend
| Technology | Purpose |
|---|---|
| React | UI framework |
| JavaScript | Primary language |
| CSS3 | Styling |

---

## 🚀 Microservices

| Service | Port | Responsibility |
|---|---|---|
| resume-service | 8081 | PDF upload, text extraction, Kafka producer |
| resume-parser-service | 8082 | Skill extraction, Kafka consumer + producer |
| recommendation-service | 8083 | RAG-based job matching, Ollama LLM |
| notification-service | 8084 | Kafka consumer, candidate notification |

---

## 📦 Prerequisites

- Java 17
- Docker Desktop
- Node.js 20+
- Ollama

---

## ⚙️ Setup & Run

### 1. Clone the repository
```bash
git clone https://github.com/Aakanksha2812/resume-recommendation-system.git
cd resume-recommendation-system
```

### 2. Start infrastructure
```bash
docker-compose up -d
```

### 3. Pull Ollama models
```bash
ollama pull llama3.2:1b
ollama pull nomic-embed-text
```

### 4. Create ChromaDB collection
```powershell
Invoke-RestMethod -Uri "http://localhost:8000/api/v2/tenants/SpringAiTenant/databases/SpringAiDatabase/collections" -Method POST -ContentType "application/json" -Body '{"name": "job-descriptions"}'
```

### 5. Start all services
```bash
# Terminal 1
cd resume-service && ./gradlew bootRun

# Terminal 2
cd resume-parser-service && ./gradlew bootRun

# Terminal 3
cd recommendation-service && ./gradlew bootRun

# Terminal 4
cd notification-service && ./gradlew bootRun
```

### 6. Start frontend
```bash
cd frontend
npm install
npm start
```

### 7. Open browser
```
http://localhost:3000
```

---

## 🔄 Kafka Topics

| Topic | Producer | Consumer |
|---|---|---|
| resume-uploaded | resume-service | resume-parser-service |
| resume-parsed | resume-parser-service | recommendation-service |
| recommendation-ready | recommendation-service | notification-service |

---

## 🗄️ MongoDB Collections

| Collection | Service | Data |
|---|---|---|
| resumes | resume-service | Raw resume data + PDF text |
| parsed_resumes | resume-parser-service | Extracted skills, education |
| recommendations | recommendation-service | Job matches + AI explanation |

---

## 🤖 RAG Implementation

1. **Job descriptions** loaded into ChromaDB as vector embeddings on startup
2. **Candidate skills** converted to embedding using `nomic-embed-text` model
3. **Vector similarity search** finds top 3 matching jobs
4. **Ollama LLM** generates intelligent explanation of why jobs match
5. Results stored in MongoDB and returned to frontend

---

## 📡 API Endpoints

### Resume Service (port 8081)
```
POST /resume/upload
  - candidateName (String)
  - candidateEmail (String)
  - file (PDF)
```

### Recommendation Service (port 8083)
```
GET /recommendations/{email}     — Get recommendations by email
GET /recommendations             — Get all recommendations
```

---

