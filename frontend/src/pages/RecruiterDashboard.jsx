import React, { useEffect, useState } from "react";
import SectionHeader from "../components/SectionHeader.jsx";
import { createJob, createProject, getJobApplicants, getProjectApplicants, getRecruiterAnalytics, getRecruiterJobs, deleteJob, updateJob } from "../api/recruiter.js";
import TagList from "../components/TagList.jsx";
import StatCard from "../components/StatCard.jsx";
import { updateApplicationStatus } from "../api/applications.js";

const RecruiterDashboard = () => {
  const recruiterId = localStorage.getItem("skillmerge_userId");
  const [jobForm, setJobForm] = useState({ title: "", description: "", skills: "", experienceRequired: 0 });
  const [projectForm, setProjectForm] = useState({ title: "", description: "", skills: "", experienceRequired: 0 });
  const [analytics, setAnalytics] = useState(null);
  const [statusUpdates, setStatusUpdates] = useState({});
  
  // Jobs table state
  const [jobs, setJobs] = useState([]);
  const [editingJobId, setEditingJobId] = useState(null);
  const [expandedJobId, setExpandedJobId] = useState(null);
  const [jobApplicants, setJobApplicants] = useState({});

  const handleJobSubmit = async (event) => {
    event.preventDefault();
    if (editingJobId) {
      // Update existing job
      await updateJob(editingJobId, {
        title: jobForm.title,
        description: jobForm.description,
        skillsRequired: jobForm.skills.split(",").map((skill) => skill.trim()),
        experienceRequired: Number(jobForm.experienceRequired),
        recruiterId: Number(recruiterId)
      });
      setEditingJobId(null);
    } else {
      // Create new job
      await createJob({
        title: jobForm.title,
        description: jobForm.description,
        skillsRequired: jobForm.skills.split(",").map((skill) => skill.trim()),
        experienceRequired: Number(jobForm.experienceRequired),
        recruiterId: Number(recruiterId)
      });
    }
    setJobForm({ title: "", description: "", skills: "", experienceRequired: 0 });
    await loadJobs();
    await loadAnalytics();
  };

  const handleProjectSubmit = async (event) => {
    event.preventDefault();
    await createProject({
      title: projectForm.title,
      description: projectForm.description,
      skillsRequired: projectForm.skills.split(",").map((skill) => skill.trim()),
      experienceRequired: Number(projectForm.experienceRequired),
      recruiterId: Number(recruiterId)
    });
    setProjectForm({ title: "", description: "", skills: "", experienceRequired: 0 });
    await loadAnalytics();
  };

  const loadJobs = async () => {
    const data = await getRecruiterJobs(recruiterId);
    setJobs(data);
  };

  const loadAnalytics = async () => {
    const data = await getRecruiterAnalytics(recruiterId);
    setAnalytics(data);
  };

  const handleDeleteJob = async (jobId) => {
    if (confirm("Delete this job posting? This action cannot be undone.")) {
      await deleteJob(jobId);
      await loadJobs();
      await loadAnalytics();
      // Clear applicants if this job was expanded
      if (expandedJobId === jobId) {
        setExpandedJobId(null);
      }
    }
  };

  const handleEditJob = (job) => {
    setEditingJobId(job.id);
    setJobForm({
      title: job.title,
      description: job.description,
      skills: job.skillsRequired.join(", "),
      experienceRequired: job.experienceRequired
    });
    // Scroll to form
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleCancelEdit = () => {
    setEditingJobId(null);
    setJobForm({ title: "", description: "", skills: "", experienceRequired: 0 });
  };

  const handleViewApplicants = async (jobId) => {
    if (expandedJobId === jobId) {
      // Collapse if already expanded
      setExpandedJobId(null);
    } else {
      // Expand and load applicants
      setExpandedJobId(jobId);
      if (!jobApplicants[jobId]) {
        const data = await getJobApplicants(jobId);
        setJobApplicants({ ...jobApplicants, [jobId]: data });
      }
    }
  };

  const handleStatusChange = (applicationId, status) => {
    setStatusUpdates({ ...statusUpdates, [applicationId]: status });
  };

  const handleStatusSave = async (applicationId, jobId) => {
    const status = statusUpdates[applicationId] || "APPLIED";
    await updateApplicationStatus(applicationId, { status, recruiterId: Number(recruiterId) });
    // Reload applicants for this job
    const data = await getJobApplicants(jobId);
    setJobApplicants({ ...jobApplicants, [jobId]: data });
    await loadAnalytics();
  };

  useEffect(() => {
    if (recruiterId) {
      loadAnalytics();
      loadJobs();
    }
  }, [recruiterId]);

  return (
    <div className="grid gap-8">
      <SectionHeader title="Recruiter workspace" subtitle="Post new roles and see applicants in one place." />
      {analytics && (
        <div className="grid md:grid-cols-4 gap-4">
          <StatCard label="Jobs" value={analytics.totalJobs} accent="border-l-ink" />
          <StatCard label="Projects" value={analytics.totalProjects} accent="border-l-amber-500" />
          <StatCard label="Job apps" value={analytics.totalJobApplications} accent="border-l-accent" />
          <StatCard label="Project apps" value={analytics.totalProjectApplications} accent="border-l-mint" />
        </div>
      )}
      <div className="grid lg:grid-cols-2 gap-6">
        <form onSubmit={handleJobSubmit} className="card p-6 grid gap-3">
          <h3 className="text-lg font-semibold">{editingJobId ? "Edit job" : "Post a job"}</h3>
          <input
            placeholder="Title"
            value={jobForm.title}
            onChange={(event) => setJobForm({ ...jobForm, title: event.target.value })}
            className="rounded-lg border p-2"
            required
          />
          <textarea
            placeholder="Description"
            value={jobForm.description}
            onChange={(event) => setJobForm({ ...jobForm, description: event.target.value })}
            className="rounded-lg border p-2"
            required
          />
          <input
            placeholder="Skills (comma separated)"
            value={jobForm.skills}
            onChange={(event) => setJobForm({ ...jobForm, skills: event.target.value })}
            className="rounded-lg border p-2"
            required
          />
          <input
            placeholder="Experience required"
            type="number"
            value={jobForm.experienceRequired}
            onChange={(event) => setJobForm({ ...jobForm, experienceRequired: event.target.value })}
            className="rounded-lg border p-2"
            required
          />
          <div className="flex gap-2">
            <button className="flex-1 bg-ink text-white rounded-lg py-2">
              {editingJobId ? "Update job" : "Publish job"}
            </button>
            {editingJobId && (
              <button
                type="button"
                onClick={handleCancelEdit}
                className="px-4 bg-slate/20 text-ink rounded-lg py-2"
              >
                Cancel
              </button>
            )}
          </div>
        </form>
        <form onSubmit={handleProjectSubmit} className="card p-6 grid gap-3">
          <h3 className="text-lg font-semibold">Post a project</h3>
          <input
            placeholder="Title"
            value={projectForm.title}
            onChange={(event) => setProjectForm({ ...projectForm, title: event.target.value })}
            className="rounded-lg border p-2"
            required
          />
          <textarea
            placeholder="Description"
            value={projectForm.description}
            onChange={(event) => setProjectForm({ ...projectForm, description: event.target.value })}
            className="rounded-lg border p-2"
            required
          />
          <input
            placeholder="Skills (comma separated)"
            value={projectForm.skills}
            onChange={(event) => setProjectForm({ ...projectForm, skills: event.target.value })}
            className="rounded-lg border p-2"
            required
          />
          <input
            placeholder="Experience required"
            type="number"
            value={projectForm.experienceRequired}
            onChange={(event) => setProjectForm({ ...projectForm, experienceRequired: event.target.value })}
            className="rounded-lg border p-2"
            required
          />
          <button className="bg-ink text-white rounded-lg py-2">Publish project</button>
        </form>
      </div>

      {/* My Posted Jobs Section */}
      <div className="card p-6">
        <h3 className="text-lg font-semibold mb-4">My posted jobs</h3>
        {jobs.length === 0 ? (
          <p className="text-slate/60 text-sm">No jobs posted yet. Create your first job above!</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b">
                  <th className="text-left p-3 text-sm font-semibold">Job title</th>
                  <th className="text-left p-3 text-sm font-semibold">Skills</th>
                  <th className="text-left p-3 text-sm font-semibold">Experience</th>
                  <th className="text-left p-3 text-sm font-semibold">Actions</th>
                </tr>
              </thead>
              <tbody>
                {jobs.map((job) => (
                  <React.Fragment key={job.id}>
                    <tr className="border-b hover:bg-slate/5">
                      <td className="p-3">
                        <div className="font-medium">{job.title}</div>
                        <div className="text-xs text-slate/60 mt-1">{job.description}</div>
                      </td>
                      <td className="p-3">
                        <TagList items={job.skillsRequired || []} />
                      </td>
                      <td className="p-3 text-sm">{job.experienceRequired} years</td>
                      <td className="p-3">
                        <div className="flex flex-wrap gap-2">
                          <button
                            onClick={() => handleViewApplicants(job.id)}
                            className="px-3 py-1 text-xs bg-accent text-white rounded-lg"
                          >
                            {expandedJobId === job.id ? "Hide" : "View"} applicants
                          </button>
                          <button
                            onClick={() => handleEditJob(job)}
                            className="px-3 py-1 text-xs bg-ink text-white rounded-lg"
                          >
                            Edit
                          </button>
                          <button
                            onClick={() => handleDeleteJob(job.id)}
                            className="px-3 py-1 text-xs bg-red-600 text-white rounded-lg"
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                    {expandedJobId === job.id && (
                      <tr>
                        <td colSpan="4" className="bg-sand/10 p-4">
                          <h4 className="font-semibold text-sm mb-3">
                            Applicants for {job.title} ({jobApplicants[job.id]?.length || 0})
                          </h4>
                          {jobApplicants[job.id] && jobApplicants[job.id].length > 0 ? (
                            <div className="grid gap-3">
                              {jobApplicants[job.id].map((app) => (
                                <div key={app.id} className="border rounded-lg p-3 bg-white">
                                  <p className="font-medium">Candidate #{app.candidate.id}</p>
                                  <TagList items={app.candidate.skills || []} />
                                  <p className="text-xs text-slate/60 mt-2">
                                    Experience: {app.candidate.experience} years | Status: {app.status}
                                  </p>
                                  <div className="flex flex-wrap gap-2 mt-3">
                                    <select
                                      value={statusUpdates[app.id] || app.status}
                                      onChange={(event) => handleStatusChange(app.id, event.target.value)}
                                      className="border rounded-lg px-2 py-1 text-xs"
                                    >
                                      <option value="APPLIED">Applied</option>
                                      <option value="SHORTLISTED">Shortlisted</option>
                                      <option value="REJECTED">Rejected</option>
                                      <option value="HIRED">Hired</option>
                                    </select>
                                    <button
                                      onClick={() => handleStatusSave(app.id, job.id)}
                                      className="px-3 py-1 text-xs bg-ink text-white rounded-lg"
                                    >
                                      Update status
                                    </button>
                                  </div>
                                </div>
                              ))}
                            </div>
                          ) : (
                            <p className="text-sm text-slate/60">No applicants yet.</p>
                          )}
                        </td>
                      </tr>
                    )}
                  </React.Fragment>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default RecruiterDashboard;

