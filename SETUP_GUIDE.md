# SkillMerge - Complete Setup Guide

A skill-based job and project recommendation platform built with Spring Boot, React, PostgreSQL, and FastAPI.

---

## 📋 Prerequisites

### Required
- **Java 17+** (for backend)
- **Node.js 16+** (for frontend)
- **PostgreSQL 13+** (for database)
- **Python 3.9+** (for ML service - optional)
- **Git** (for version control)

### Optional
- **Docker & Docker Compose** (for containerized deployment)

---

## 🚀 Quick Start (Development Mode)

### 1. Database Setup

#### Using PostgreSQL Locally

```bash
# Create database
createdb skillmerge

# Set default user/password
# Default: postgres / postgres

# Import seed data (optional, for sample data)
psql -U postgres -d skillmerge -f database/seed.sql
```

#### Using Docker

```bash
docker-compose up -d postgres
# This starts PostgreSQL on port 5432 with default credentials
```

---

### 2. Backend Setup (Spring Boot)

```bash
cd backend

# Install dependencies (Maven will download automatically)

# Run the backend
mvn spring-boot:run

# The backend will start on http://localhost:8080
# JSON output shows "Tomcat started on port(s): 8080"
```

**Default Seed Users:**
- **Candidate**: alex@skillmerge.dev / password123
- **Recruiter**: recruiter@skillmerge.dev / password123
- **Admin**: admin@skillmerge.dev / admin123

---

### 3. ML Service Setup (Optional but Recommended)

The ML service provides advanced matching algorithms.

```bash
cd ml-service

# Create virtual environment
python -m venv venv

# Activate virtual environment
# On Windows:
venv\Scripts\activate
# On Mac/Linux:
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Start the service
uvicorn main:app --host 0.0.0.0 --port 9000 --reload

# Service will be available at http://localhost:9000
```

**Enable ML Service in Backend:**

Edit `backend/src/main/resources/application.yml`:
```yaml
app:
  matching:
    use-remote: true
    service-url: http://localhost:9000/match
```

---

### 4. Frontend Setup (React + Vite)

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev

# Frontend will be available at http://localhost:5173
```

**Configure API URL (if backend is on different host):**

Create `.env.local` in `frontend` directory:
```
VITE_API_URL=http://localhost:8080/api
```

---

##  Project Structure

```
SkillMerge/
├── backend/                 # Spring Boot REST API
│   ├── src/main/java/      # Java source code
│   ├── pom.xml             # Maven configuration
│   └── src/main/resources/ # Configuration files
├── frontend/                # React + Vite
│   ├── src/
│   │   ├── pages/          # Page components
│   │   ├── components/     # Reusable components
│   │   ├── api/            # API clients
│   │   └── styles.css      # Tailwind CSS
│   └── vite.config.js
├── ml-service/              # FastAPI matching service
│   ├── main.py             # FastAPI application
│   └── requirements.txt
├── database/
│   └── seed.sql            # Sample data
└── docker-compose.yml      # Docker orchestration
```

---

## 🔐 Features by Role

### Candidate Dashboard
- ✅ View personalized job/project recommendations
- ✅ Apply to jobs and projects
- ✅ Upload and manage resume
- ✅ Track application status
- ✅ Receive notifications
- ✅ Verify skills and experience

### Recruiter Dashboard
- ✅ Post jobs and projects
- ✅ View applicants for postings
- ✅ Update application status (Applied → Shortlisted → Hired/Rejected)
- ✅ Track analytics (total jobs, applicants, etc.)
- ✅  Manage company profiles

### Admin Dashboard
- ✅ View platform-wide analytics
- ✅ Monitor all users, jobs, projects, applications
- ✅ Analyze matching percentages
- ✅ System overview and metrics

---

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/register/candidate` - Register candidate
- `POST /api/auth/register/recruiter` - Register recruiter
- `POST /api/auth/register/admin` - Register admin
- `POST /api/auth/login` - Login

### Candidates
- `GET /api/candidates/{id}` - Get candidate profile
- `PUT /api/candidates/{id}` - Update candidate
- `POST /api/candidates/{id}/resume` - Upload resume

### Jobs & Projects
- `GET /api/jobs` - List all jobs
- `POST /api/jobs` - Create new job
- `GET /api/jobs/search` - Search jobs by skill
- `GET /api/projects` - List all projects
- `POST /api/projects` - Create new project
- `GET /api/projects/search` - Search projects

### Applications
- `POST /api/applications` - Submit application
- `GET /api/applications/candidate/{id}` - Get candidate's applications
- `PATCH /api/applications/{id}/status` - Update application status

### Recommendations
- `POST /api/matching/recommendations` - Get personalized matches

### Admin
- `GET /api/admin/analytics` - Get platform analytics
- `GET /api/admin/jobs` - List all jobs
- `GET /api/admin/projects` - List all projects
- `GET /api/admin/candidates` - List all candidates
- `GET /api/admin/recruiters` - List all recruiters

---

## 📊 ML Matching Algorithm

The matching service calculates compatibility scores based on:

- **Skill Match (70% weight)**: Overlap between candidate skills and required skills
- **Experience Match (30% weight)**: Candidate experience vs. required experience

Score calculation:
```python
skill_score = matching_skills / required_skills
experience_score = min(candidate_experience / required_experience, 1.0)
match_percent = (skill_score * 0.7 + experience_score * 0.3) * 100
```

---

## 🐳 Docker Deployment

Run entire stack with Docker Compose:

```bash
docker-compose up --build

# Access:
# - Frontend: http://localhost:5173
# - Backend: http://localhost:8080
# - ML Service: http://localhost:9000
# - PostgreSQL: localhost:5432
```

---

## 🔧 Configuration

### Backend Configuration (`application.yml`)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/skillmerge
    username: postgres
    password: postgres

app:
  jwt:
    secret: ${JWT_SECRET:Y2hhbmdlLXRoaXMtc2VjcmV0LWtleS1mb3ItanV0}
    expiration-ms: 86400000  # 24 hours
  storage:
    upload-dir: uploads
    base-url: http://localhost:8080/uploads
  matching:
    use-remote: false
    service-url: http://localhost:9000/match

cors:
  allowed-origins: http://localhost:5173
```

### Frontend Configuration

**Environment Variables (.env.local):**
```
VITE_API_URL=http://localhost:8080/api
```

---

## 🧪 Testing the Application

### 1. Test Candidate Flow

```bash
# 1. Open http://localhost:5173
# 2. Click "Register here" → Select "Candidate"
# 3. Sign up with:
#    - Name: Test User
#    - Email: test@example.com
#    - Password: password123
#    - Skills: React, Node.js, Python
#    - Experience: 5 years
# 4. Sign in with test@example.com / password123
# 5. View recommendations
# 6. Apply to jobs/projects
```

### 2. Test Recruiter Flow

```bash
# 1. Register as Recruiter
#    - Company: Test Corp
#    - Email: recruiter@example.com
# 2. Sign in
# 3. Post a new job:
#    - Title: React Developer
#    - Skills: React, JavaScript
#    - Experience: 2 years
# 4. View applicants and their profiles
```

### 3. Test Admin Dashboard

```bash
# 1. Sign in as: admin@skillmerge.dev / admin123
# 2. View all analytics
# 3. See platform statistics
# 4. Monitor jobs, projects, users
```

---

## 🐛 Troubleshooting

### Frontend Issues

**Port 5173 already in use:**
```bash
lsof -i :5173  # Find process
kill -9 <PID>  # Kill it
npm run dev    # Restart
```

**npm install fails:**
```bash
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### Backend Issues

**Database connection fails:**
```bash
# Check PostgreSQL is running
psql -U postgres -c "SELECT 1"

# Verify database exists
psql -U postgres -l | grep skillmerge

# Check app.yml credentials
```

**Port 8080 already in use:**
```bash
lsof -i :8080  # Find process
kill -9 <PID>  # Kill it
mvn spring-boot:run  # Restart
```

### ML Service Issues

**Connection refused on port 9000:**
```bash
# Check if service is running
ps aux | grep uvicorn

# Restart service
cd ml-service
source venv/bin/activate
uvicorn main:app --reload
```

---

## 🔐 Security Notes

⚠️ **Important for Production:**

1. **Change JWT Secret**
   ```yaml
   app:
     jwt:
       secret: ${JWT_SECRET}  # Set via environment variable
   ```

2. **Enable HTTPS**
   ```yaml
   server:
     ssl:
       key-store: path/to/keystore.jks
       key-store-password: ${KEYSTORE_PASSWORD}
   ```

3. **Update CORS Settings**
   ```yaml
   cors:
     allowed-origins: https://yourdomain.com
   ```

4. **Secure Database**
   - Change default PostgreSQL password
   - Use strong credentials

---

## 📈 Performance Optimization

### Frontend
- Use `npm run build` for production builds
- Enable Gzip compression in reverse proxy
- Cache static assets

### Backend
- Configure connection pool (Hikari)
- Enable query result caching
- Index frequently searched columns

### Database
- Regular backups
- Monitor slow queries
- Optimize indexes

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Vite Documentation](https://vitejs.dev)
- [PostgreSQL Documentation](https://www.postgresql.org/docs)
- [FastAPI Documentation](https://fastapi.tiangolo.com)
- [Tailwind CSS Documentation](https://tailwindcss.com)

---

## 📝 Development Tips

### Hot Reload
- **Frontend**: Vite automatically reloads on file changes
- **Backend**: Use `spring-boot-devtools` for automatic restart

### Debugging
- **Frontend**: Use browser DevTools (F12)
- **Backend**: Set breakpoints in IDE or use `System.out.println()` for debugging

### Database
- View data: `psql -U postgres -d skillmerge`
- Common queries:
  ```sql
  SELECT * FROM candidates;
  SELECT * FROM jobs;
  SELECT * FROM applications;
  ```

---

## ✅ Checklist for Full Setup

- [ ] PostgreSQL installed and running
- [ ] `skillmerge` database created
- [ ] Java 17+ installed
- [ ] Node.js 16+ installed
- [ ] Backend starts successfully (`mvn spring-boot:run`)
- [ ] Frontend starts successfully (`npm run dev`)
- [ ] Can access http://localhost:5173
- [ ] Can access http://localhost:8080/api
- [ ] Can log in with default credentials
- [ ] Can register new users
- [ ] Can post jobs/projects
- [ ] Can view recommendations
- [ ] Can apply to jobs/projects

---

## 🎯 Next Steps

1. **Customize branding** - Update logo, colors in Tailwind config
2. **Add more features** - Email notifications, advanced filtering
3. **Deploy to production** - Use Docker, cloud provider (AWS, Herokulet, Azure)
4. **Set up CI/CD** - GitHub Actions, Jenkins, GitLab CI
5. **Monitor & scale** - Use APM tools, load balancing

---

**Happy coding! 🚀**
