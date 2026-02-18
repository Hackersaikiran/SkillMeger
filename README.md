# SkillMerge

🎯 **Skill-based job and project recommendation platform** combining AI matching with modern web technologies.

Built with: **Spring Boot** | **React** | **PostgreSQL** | **FastAPI** | **Docker**

---

## 📖 Quick Links

- **[Complete Setup Guide](./SETUP_GUIDE.md)** - Detailed instructions for all configurations
- **[Project Structure](#-project-structure)** - Overview of the codebase
- **[Features](#-features)** - What you can do here
- **[API Documentation](#-api-overview)** - Endpoint reference

---

## ✨ Features

### For Candidates
- 🎯 Get AI-powered job and project recommendations
- 📝 Apply to opportunities matching your skills
- 📄 Upload and manage your resume
- 🔔 Receive real-time notifications
- 📊 Track all your applications

### For Recruiters
- 📢 Post jobs and projects
- 👥 Review applicants and their profiles
- ✅ Manage application workflow
- 📈 View recruitment analytics
- 🎯 Find perfect skill matches

### For Admins
- 📊 Monitor platform-wide analytics
- 👤 Manage users and accounts
- 🔍 View all jobs, projects, applications
- 📈 Track matching performance

---

## 🚀 Getting Started (5 Minutes)

### Prerequisites
- PostgreSQL 13+
- Java 17+
- Node.js 16+

### Start the Full Stack

```bash
# 1. Create database
createdb skillmerge

# 2. Start backend (in backend/ folder)
mvn spring-boot:run

# 3. Start frontend (in frontend/ folder)
npm install
npm run dev

# 4. Open http://localhost:5173
```

**Test Login Credentials:**
```
Email: alex@skillmerge.dev
Password: password123
Role: Candidate
```

---

## 🐳 Docker Quick Start

```bash
docker-compose up --build
```

This starts:
- ✅ PostgreSQL (port 5432)
- ✅ Backend API (port 8080)
- ✅ Frontend (port 5173)
- ✅ ML Service (port 9000)

---

## 📁 Project Structure

```
SkillMerge/
├── backend/              # Spring Boot REST API
│   ├── src/main/java/   # Controllers, Services, Entities
│   ├── pom.xml          # Maven dependencies
│   └── resources/       # app.yml configuration
├── frontend/             # React + Vite
│   ├── src/pages/       # Login, Register, Dashboards
│   ├── src/components/  # Reusable UI components
│   ├── src/api/         # API client functions
│   └── index.html       # Entry point
├── ml-service/           # FastAPI matching service
│   ├── main.py          # Skill matching algorithm
│   └── requirements.txt
├── database/
│   └── seed.sql         # Sample data
├── SETUP_GUIDE.md       # Detailed setup instructions
└── docker-compose.yml   # Container orchestration
```

---

## 🔌 API Overview

| Resource | Methods | Description |
|----------|---------|-------------|
| `/auth` | POST | Register, Login |
| `/candidates` | GET, PUT | Profile management |
| `/jobs` | GET, POST, SEARCH | Job postings |
| `/projects` | GET, POST, SEARCH | Project postings |
| `/applications` | GET, POST, PATCH | Track applications |
| `/matching` | POST | AI recommendations |
| `/admin` | GET | Analytics & management |

**Full API documentation** in [SETUP_GUIDE.md](./SETUP_GUIDE.md#-api-endpoints)

---

## 🛠️ Configuration

### Backend (src/main/resources/application.yml)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/skillmerge
    username: postgres
    password: postgres

app:
  jwt:
    secret: your-secret-key
  matching:
    use-remote: true  # Enable AI service
    service-url: http://localhost:9000/match
```

### Frontend (.env.local)
```
VITE_API_URL=http://localhost:8080/api
```

---

## 🤖 AI Matching Engine

The platform uses intelligent matching to recommend opportunities:

- **Skill Matching** (70% weight): Overlap between your skills and job requirements
- **Experience Matching** (30% weight): Your experience level vs. requirement

Example: A candidate with `React, Python, 5 years` gets a high match score for a `React developer` role requiring `2 years`.

---

## 📊 Default Test Users

| Role | Email | Password | Purpose |
|------|-------|----------|---------|
| Candidate | alex@skillmerge.dev | password123 | Test job search |
| Recruiter | recruiter@skillmerge.dev | password123 | Post jobs |
| Admin | admin@skillmerge.dev | admin123 | View analytics |

---

## 🧪 Testing the Platform

### Test Scenario 1: Job Application
1. Log in as candidate (alex@skillmerge.dev)
2. Browse /search or go to Candidate Dashboard
3. View recommendations
4. Click "Apply" on any job
5. Check your applications in the dashboard

### Test Scenario 2: Recruiter Workflow
1. Log in as recruiter (recruiter@skillmerge.dev)
2. Post a new job with required skills
3. See applicants in the recruiter dashboard
4. Update application status (Shortlist/Offer/Reject)

### Test Scenario 3: Admin Monitoring
1. Log in as admin (admin@skillmerge.dev)
2. View platform-wide analytics
3. See all users, jobs, applications
4. Monitor matching performance

---

## 🔍 Troubleshooting

**Backend won't start?**
```bash
# Check if port 8080 is in use
lsof -i :8080
# Check database connection
psql -U postgres -d skillmerge
```

**Frontend won't start?**
```bash
# Clear node modules and reinstall
rm -rf node_modules package-lock.json
npm install
npm run dev
```

**Can't log in?**
```bash
# Verify database was seeded with test users
psql -U postgres -d skillmerge -c "SELECT * FROM candidates LIMIT 1;"
```

**More help?** See [SETUP_GUIDE.md - Troubleshooting](./SETUP_GUIDE.md#-troubleshooting)

---

## 🚀 Deployment

### Docker Compose (Recommended)
```bash
docker-compose up --build
```

### Manual Deployment
1. Set environment variables (JWT_SECRET, DB credentials, etc.)
2. Build backend: `mvn clean package`
3. Build frontend: `npm run build`
4. Deploy to your hosting (AWS, Heroku, DigitalOcean, Azure, etc.)

---

## 📚 Technology Stack

| Layer | Technologies |
|-------|--------------|
| **Frontend** | React 18, Vite, Tailwind CSS, React Router |
| **Backend** | Spring Boot 3, Spring Security, JPA/Hibernate |
| **Database** | PostgreSQL 13+, JDBC |
| **Matching** | FastAPI, Python ML algorithms |
| **Auth** | JWT tokens, Spring Security |
| **Container** | Docker, Docker Compose |

---

## 📝 Development Workflow

```bash
# Clone and enter directory
cd SkillMerge

# Terminal 1: Start backend
cd backend && mvn spring-boot:run

# Terminal 2: Start ML service (optional)
cd ml-service && ./bin/uvicorn main:app --reload

# Terminal 3: Start frontend
cd frontend && npm run dev
```

---

## 🤝 Contributing

Want to improve SkillMerge?

1. Create a feature branch: `git checkout -b feature/amazing-feature`
2. Make changes
3. Test thoroughly
4. Submit a pull request

---

## 📋 Next Steps

- [ ] **Setup**: Follow [SETUP_GUIDE.md](./SETUP_GUIDE.md)
- [ ] **Explore**: Test all user roles
- [ ] **Customize**: Update branding, colors, features
- [ ] **Deploy**: Use Docker Compose or cloud platform
- [ ] **Extend**: Add new features like email notifications

---

## 📄 License

This project is open source and available under the MIT License.

---

## 🎉 Quick Commands Reference

```bash
# Development
npm run dev                 # Start frontend
mvn spring-boot:run        # Start backend
uvicorn main:app --reload  # Start ML service

# Production Builds
npm run build              # Build React app
mvn clean package          # Build Jar file

# Database
createdb skillmerge        # Create database
psql -U postgres -d skillmerge < database/seed.sql  # Load data

# Docker
docker-compose up --build  # Start all services
docker-compose down        # Stop all services
```

---

**🌟 Now you're ready to go!** Start with [SETUP_GUIDE.md](./SETUP_GUIDE.md) for detailed instructions.
