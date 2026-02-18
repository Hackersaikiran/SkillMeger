# 🚀 SkillMerge

> **Connect Talent with Opportunity Through Intelligent Skill-Based Matching**

![SkillMerge Landing Page](./frontend/assests/Screenshot%202026-02-18%20204205.png)

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/Hackersaikiran/SkillMeger)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](#-license)
[![Stack](https://img.shields.io/badge/stack-Spring%20Boot%20|%20React%20|%20PostgreSQL-blueviolet.svg)](#-technology-stack)

SkillMerge is a modern, full-stack job and talent matching platform powered by AI. It connects candidates with their ideal opportunities, helps recruiters find the perfect talent, and provides admins with comprehensive analytics—all through an intelligent skill-based matching engine.

---

## 🎯 Table of Contents

- [Features](#-features)
- [Quick Start](#-quick-start)
- [Technology Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [API Documentation](#-api-documentation)
- [Setup Guide](#-setup-guide)
- [Deployment](#-deployment)
- [Contributing](#-contributing)

---

## ✨ Features

### For Candidates
- 🎯 **AI-Powered Recommendations** - Get jobs and projects perfectly matched to your skills
- 📝 **Easy Application** - Apply to opportunities with one click
- 📄 **Resume Management** - Upload and update your resume anytime
- 🔔 **Smart Notifications** - Get notified about matching opportunities
- 📊 **Application Tracking** - Monitor all your applications in real-time

### For Recruiters
- 📢 **Post Opportunities** - Create jobs and projects instantly
- 👥 **Smart Candidate Search** - Find perfect candidates by skills, experience, or availability
- ✅ **Application Management** - Review, shortlist, and manage applicants efficiently
- 📈 **Recruitment Analytics** - Track hiring metrics and recruitment performance
- 🎯 **AI Matching** - Let AI find the most suitable candidates for your roles

### For Admins
- 📊 **Platform Analytics** - Monitor key metrics and platform performance
- 👤 **User Management** - Manage users, roles, and permissions
- 🔍 **System Overview** - View all jobs, projects, applications, and users
- 📈 **Insights & Reports** - Track matching performance and platform health
- ⚙️ **Configuration** - Control platform settings and features

---

## ⚡ Quick Start

### Prerequisites
Ensure you have these installed:
- **PostgreSQL** 13+ 
- **Java** 17+ 
- **Node.js** 16+
- **Docker & Docker Compose** (optional but recommended)

### 🐳 Quick Start with Docker (Recommended)

**Fastest way to get SkillMerge running locally in minutes:**

```bash
# Clone the repository
git clone https://github.com/Hackersaikiran/SkillMeger.git
cd SkillMerge

# Start all services
docker-compose up --build
```

**Then open your browser:**
- **Frontend:** http://localhost:5173
- **Backend API:** http://localhost:8080
- **ML Service:** http://localhost:9000

### 🚀 Manual Setup (Without Docker)

**Terminal 1: Start Backend**
```bash
cd backend
mvn spring-boot:run
# Backend runs at http://localhost:8080
```

**Terminal 2: Start ML Service** (optional)
```bash
cd ml-service
pip install -r requirements.txt
python main.py
# Service runs at http://localhost:9000
```

**Terminal 3: Start Frontend**
```bash
cd frontend
npm install
npm run dev
# Frontend runs at http://localhost:5173
```

### 📝 Test Login Credentials

| Role | Email | Password |
|------|-------|----------|
| **Candidate** | alex@skillmerge.dev | password123 |
| **Recruiter** | recruiter@skillmerge.dev | password123 |
| **Admin** | admin@skillmerge.dev | admin123 |

---

## 🏗️ Project Structure

## 🏗️ Project Structure

```
SkillMerge/
├── 📁 backend/                      # Spring Boot REST API (Java)
│   ├── src/main/java/com/skillmerge/
│   │   ├── controllers/             # REST API endpoints
│   │   ├── services/                # Business logic
│   │   ├── entities/                # Database models
│   │   ├── repositories/            # Data access layer
│   │   ├── security/                # JWT & authentication
│   │   ├── dto/                     # Data transfer objects
│   │   ├── exceptions/              # Custom exceptions
│   │   └── config/                  # Application configuration
│   ├── src/main/resources/
│   │   ├── application.yml          # Main configuration
│   │   └── application-dev.yml      # Development config
│   ├── pom.xml                      # Maven dependencies
│   └── Dockerfile                   # Docker build file
│
├── 📁 frontend/                     # React + Vite App
│   ├── src/
│   │   ├── pages/                   # Page components
│   │   │   ├── Landing.jsx          # Landing page
│   │   │   ├── Login.jsx            # Login page
│   │   │   ├── Register.jsx         # Registration page
│   │   │   ├── CandidateDashboard.jsx
│   │   │   ├── RecruiterDashboard.jsx
│   │   │   ├── AdminDashboard.jsx
│   │   │   └── JobSearch.jsx
│   │   ├── components/              # Reusable components
│   │   ├── api/                     # API client modules
│   │   ├── App.jsx                  # Main app component
│   │   └── main.jsx                 # Entry point
│   ├── index.html                   # HTML template
│   ├── package.json                 # Node dependencies
│   ├── vite.config.js               # Vite build config
│   ├── tailwind.config.js           # Tailwind CSS config
│   ├── nginx.conf                   # Nginx configuration
│   └── Dockerfile                   # Docker build file
│
├── 📁 ml-service/                   # FastAPI Python Service
│   ├── main.py                      # ML matching algorithm
│   ├── requirements.txt             # Python dependencies
│   ├── Dockerfile                   # Docker build file
│   └── Procfile                     # Process configuration
│
├── 📁 database/                     # Database setup
│   └── seed.sql                     # Initial test data
│
├── 📄 docker-compose.yml            # Docker Compose orchestration
├── 📄 SETUP_GUIDE.md                # Detailed setup instructions
├── 📄 COMPREHENSIVE_FIXES.md        # Major fixes documentation
└── 📄 README.md                     # This file

```

---

## 💻 Technology Stack

| Component | Technologies |
|-----------|--------------|
| **Frontend** | React 18, Vite, Tailwind CSS, React Router, Axios |
| **Backend** | Spring Boot 3, Spring Security, JPA/Hibernate, PostgreSQL JDBC |
| **Database** | PostgreSQL 13+, SQL |
| **Authentication** | JWT Tokens, Spring Security, Role-based Access (RBAC) |
| **ML/Matching** | FastAPI (Python), Machine Learning algorithms |
| **DevOps** | Docker, Docker Compose, Nginx |
| **Build Tools** | Maven, npm, Vite |

---

## 🔌 API Overview

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/auth/register` | POST | User registration |
| `/api/auth/login` | POST | User authentication |
| `/api/candidates` | GET, PUT | Candidate profile management |
| `/api/recruiters` | GET, PUT | Recruiter profile management |
| `/api/jobs` | GET, POST | Jobs CRUD operations |
| `/api/projects` | GET, POST | Projects CRUD operations |
| `/api/applications` | GET, POST, PATCH | Application tracking |
| `/api/matching/recommend` | POST | AI-powered recommendations |
| `/api/admin` | GET | Admin analytics & management |
| `/api/notifications` | GET | Notification retrieval |

**📚 Full API Documentation:** See [SETUP_GUIDE.md](./SETUP_GUIDE.md#-api-endpoints)

---

## 🤖 AI Matching Algorithm

Our intelligent matching engine evaluates candidates based on:

## 🤖 AI Matching Algorithm

Our intelligent matching engine evaluates candidates based on:

- **Skill Matching** (70% weight) - Technical skill overlap between candidate and opportunity
- **Experience Matching** (30% weight) - Experience level alignment with role requirements

**Example:** A candidate with `React, Python, 5 years` experience gets a high match score for a `React Developer` role requiring `2+ years`.

---

## ⚙️ Configuration

### Backend Configuration
**File:** `backend/src/main/resources/application.yml`

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/skillmerge
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update

app:
  jwt:
    secret: ${JWT_SECRET:your-secret-key}
    expiration-ms: 86400000
  matching:
    use-remote: true
    service-url: http://localhost:9000/match

cors:
  allowed-origins: http://localhost:5173
```

### Frontend Configuration
**File:** `frontend/.env`

```
VITE_API_URL=http://localhost:8080/api
```

### Docker Configuration
**File:** `docker-compose.yml` - Define services, ports, and environment variables

---

## 🧪 Testing the Platform

### Test Scenario 1: Job Search & Application
1. Go to http://localhost:5173
2. Click **"Get Started"** or **"Sign In"**
3. Login as candidate: `alex@skillmerge.dev` / `password123`
4. Browse job recommendations
5. Click **Apply** on any job
6. Check your applications in the dashboard

### Test Scenario 2: Recruiter Workflow
1. Login as recruiter: `recruiter@skillmerge.dev` / `password123`
2. Go to Recruiter Dashboard
3. Create a new job posting with required skills
4. View applicants as they apply
5. Update application status (Shortlist/Offer/Reject)

### Test Scenario 3: Admin Monitoring
1. Login as admin: `admin@skillmerge.dev` / `admin123`
2. View platform-wide analytics
3. Monitor user growth and application metrics
4. See top skills in demand

---

## 📖 Setup & Documentation

For more detailed setup instructions and advanced configuration, check out:

| Document | Purpose |
|----------|---------|
| **[SETUP_GUIDE.md](./SETUP_GUIDE.md)** | Complete step-by-step installation guide |
| **[COMPREHENSIVE_FIXES.md](./COMPREHENSIVE_FIXES.md)** | Documentation of major fixes and improvements |
| **GitHub Issues** | Report bugs and feature requests |

---

## 📝 Development Workflow

**Option 1: Using Docker (Recommended)**
```bash
git clone https://github.com/Hackersaikiran/SkillMeger.git
cd SkillMerge
docker-compose up --build
```

**Option 2: Manual Setup (Multiple Terminals)**

**Terminal 1: Backend**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**Terminal 2: ML Service** (optional)
```bash
cd ml-service
pip install -r requirements.txt
uvicorn main:app --reload --port 9000
```

**Terminal 3: Frontend**
```bash
cd frontend
npm install
npm run dev
```

---

## � Troubleshooting

### Backend Issues

**Problem:** Backend won't start
```bash
# Check if port 8080 is already in use
lsof -i :8080
# Kill the process using port 8080
kill -9 <PID>
```

**Problem:** Database connection error
```bash
# Verify PostgreSQL is running
psql -U postgres -c "SELECT 1;"

# Check SkillMerge database exists
psql -U postgres -c "\l" | grep skillmerge

# Create database if missing
createdb skillmerge
psql -U postgres -d skillmerge < database/seed.sql
```

### Frontend Issues

**Problem:** Frontend won't load
```bash
# Clear node dependencies
rm -rf frontend/node_modules frontend/package-lock.json

# Reinstall and start
cd frontend
npm install
npm run dev
```

**Problem:** API calls failing (CORS errors)
- Ensure backend is running on `http://localhost:8080`
- Verify `.env` has correct `VITE_API_URL`
- Check backend CORS configuration in `application.yml`

### Docker Issues

**Problem:** Containers won't start
```bash
# View error logs
docker-compose logs -f

# Rebuild containers
docker-compose down
docker-compose up --build

# Remove all containers and images
docker-compose down -v
docker system prune -a
```

**Problem:** Database persistence
- Verify `pgdata` volume exists: `docker volume ls`
- Check volume mounts in `docker-compose.yml`

### Authentication Issues

**Problem:** Can't login with test credentials
```bash
# Verify test users exist in database
psql -U postgres -d skillmerge -c "SELECT id, email, role FROM users LIMIT 5;"

# Reseed database with test data
psql -U postgres -d skillmerge < database/seed.sql
```

**Problem:** JWT token expired
- Clear browser cookies and login again
- Tokens expire after 24 hours (configurable in `application.yml`)

### More Help

📖 **Full Documentation:** [SETUP_GUIDE.md](./SETUP_GUIDE.md)  
🐛 **Report Bug:** [GitHub Issues](https://github.com/Hackersaikiran/SkillMeger/issues)  
💬 **Ask Question:** Open a discussion on GitHub

---

## �📚 Useful Commands

```bash
# Frontend
npm run dev                 # Start dev server (http://localhost:5173)
npm run build              # Build for production
npm run preview            # Preview production build

# Backend
mvn clean install          # Build with dependencies
mvn spring-boot:run        # Start Spring Boot
mvn test                   # Run tests
mvn clean package          # Create JAR file

# Database
createdb skillmerge                                    # Create database
psql -U postgres -d skillmerge < database/seed.sql   # Load sample data

# Docker
docker-compose up --build                    # Start all services
docker-compose down                          # Stop all services
docker-compose logs -f                       # View logs
docker-compose ps                            # Show running containers
```

---

## 🤝 Contributing

We love contributions! Here's how to help:

1. **Fork the Repository**
   ```bash
   git clone https://github.com/YOUR-USERNAME/SkillMeger.git
   ```

2. **Create Feature Branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Make Changes & Test**
   - Ensure code follows project standards
   - Test thoroughly on all user roles
   - Update documentation if needed

4. **Commit & Push**
   ```bash
   git add .
   git commit -m "Add amazing feature"
   git push origin feature/amazing-feature
   ```

5. **Open Pull Request**
   - Describe your changes clearly
   - Reference any related issues

---

## 🎯 Roadmap

- ✅ Core platform (Candidates, Recruiters, Admin)
- ✅ AI-powered job matching
- ✅ Landing page
- 🔄 Email notifications
- 🔄 Advanced analytics dashboard
- 🔄 Video interview integration
- 🔄 Mobile app

---

## 📄 License

This project is open source and available under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 👥 Author

**Hackersaikiran**  
💻 GitHub: [@Hackersaikiran](https://github.com/Hackersaikiran/)

---

## 🎉 Getting Started

Ready to explore SkillMerge? Here's your journey:

1. **Start here:** [SETUP_GUIDE.md](./SETUP_GUIDE.md)
2. **Quick test:** Run `docker-compose up --build`
3. **Open browser:** http://localhost:5173
4. **Login with test credentials** (see table below)
5. **Explore features** for your user role
6. **Customize & Deploy** when ready

| Role | Email | Password | Start at |
|------|-------|----------|----------|
| 👥 **Candidate** | alex@skillmerge.dev | password123 | /candidate |
| 🤝 **Recruiter** | recruiter@skillmerge.dev | password123 | /recruiter |
| 📊 **Admin** | admin@skillmerge.dev | admin123 | /admin |

---

**🌟 Questions or Issues?** Open an issue on [GitHub Issues](https://github.com/Hackersaikiran/SkillMeger/issues)

**Built with ❤️ by [Hackersaikiran](https://github.com/Hackersaikiran/)**
