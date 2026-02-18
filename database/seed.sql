-- Example seed data for local development.
-- Replace password hashes with bcrypt hashes if needed.

INSERT INTO candidates (name, email, password, experience, role)
VALUES ('Sam Candidate', 'sam@skillmerge.dev', '$2a$10$2bL2B8Yt1sFz0CwFz9iKFeE9XQvYZn7bLrKXn7aYdpF9QVKq3pQ6O', 4, 'CANDIDATE');

INSERT INTO recruiters (name, email, password, company, role)
VALUES ('Rita Recruiter', 'rita@skillmerge.dev', '$2a$10$2bL2B8Yt1sFz0CwFz9iKFeE9XQvYZn7bLrKXn7aYdpF9QVKq3pQ6O', 'SkillMerge Labs', 'RECRUITER');

INSERT INTO admin_users (name, email, password, role)
VALUES ('Seed Admin', 'seed-admin@skillmerge.dev', '$2a$10$2bL2B8Yt1sFz0CwFz9iKFeE9XQvYZn7bLrKXn7aYdpF9QVKq3pQ6O', 'ADMIN');

INSERT INTO jobs (title, description, experience_required, recruiter_id)
VALUES ('Full Stack Engineer', 'Build APIs and UI features.', 3, 1);

INSERT INTO job_skills (job_id, skill)
VALUES (1, 'Java'), (1, 'Spring'), (1, 'React');

INSERT INTO projects (title, description, experience_required, recruiter_id)
VALUES ('Data Dashboard', 'Build analytics visualizations.', 2, 1);

INSERT INTO project_skills (project_id, skill)
VALUES (1, 'React'), (1, 'Chart.js');

-- Extra seed data for web search results (safe inserts)
INSERT INTO jobs (title, description, experience_required, recruiter_id)
SELECT 'Web Developer', 'Build responsive web apps and landing pages.', 2, r.id
FROM recruiters r
WHERE r.email IN ('recruiter@skillmerge.dev', 'rita@skillmerge.dev')
	AND NOT EXISTS (SELECT 1 FROM jobs WHERE title = 'Web Developer')
LIMIT 1;

INSERT INTO job_skills (job_id, skill)
SELECT j.id, 'Web'
FROM jobs j
WHERE j.title = 'Web Developer'
	AND NOT EXISTS (SELECT 1 FROM job_skills js WHERE js.job_id = j.id AND js.skill = 'Web');

INSERT INTO job_skills (job_id, skill)
SELECT j.id, 'HTML'
FROM jobs j
WHERE j.title = 'Web Developer'
	AND NOT EXISTS (SELECT 1 FROM job_skills js WHERE js.job_id = j.id AND js.skill = 'HTML');

INSERT INTO job_skills (job_id, skill)
SELECT j.id, 'CSS'
FROM jobs j
WHERE j.title = 'Web Developer'
	AND NOT EXISTS (SELECT 1 FROM job_skills js WHERE js.job_id = j.id AND js.skill = 'CSS');

INSERT INTO job_skills (job_id, skill)
SELECT j.id, 'JavaScript'
FROM jobs j
WHERE j.title = 'Web Developer'
	AND NOT EXISTS (SELECT 1 FROM job_skills js WHERE js.job_id = j.id AND js.skill = 'JavaScript');

INSERT INTO projects (title, description, experience_required, recruiter_id)
SELECT 'Marketing Website Revamp', 'Refresh a marketing site with modern UI.', 1, r.id
FROM recruiters r
WHERE r.email IN ('recruiter@skillmerge.dev', 'rita@skillmerge.dev')
	AND NOT EXISTS (SELECT 1 FROM projects WHERE title = 'Marketing Website Revamp')
LIMIT 1;

INSERT INTO project_skills (project_id, skill)
SELECT p.id, 'Web'
FROM projects p
WHERE p.title = 'Marketing Website Revamp'
	AND NOT EXISTS (SELECT 1 FROM project_skills ps WHERE ps.project_id = p.id AND ps.skill = 'Web');

INSERT INTO project_skills (project_id, skill)
SELECT p.id, 'HTML'
FROM projects p
WHERE p.title = 'Marketing Website Revamp'
	AND NOT EXISTS (SELECT 1 FROM project_skills ps WHERE ps.project_id = p.id AND ps.skill = 'HTML');

INSERT INTO project_skills (project_id, skill)
SELECT p.id, 'CSS'
FROM projects p
WHERE p.title = 'Marketing Website Revamp'
	AND NOT EXISTS (SELECT 1 FROM project_skills ps WHERE ps.project_id = p.id AND ps.skill = 'CSS');

INSERT INTO project_skills (project_id, skill)
SELECT p.id, 'JavaScript'
FROM projects p
WHERE p.title = 'Marketing Website Revamp'
	AND NOT EXISTS (SELECT 1 FROM project_skills ps WHERE ps.project_id = p.id AND ps.skill = 'JavaScript');
