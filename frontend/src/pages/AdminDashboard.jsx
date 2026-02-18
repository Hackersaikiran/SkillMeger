import React, { useEffect, useState } from "react";
import SectionHeader from "../components/SectionHeader.jsx";
import StatCard from "../components/StatCard.jsx";
import { getAnalytics, getAdminJobs, getAdminProjects, getAdminCandidates, getAdminRecruiters } from "../api/admin.js";
import { Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Tooltip,
  Legend
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, BarElement, Tooltip, Legend);

const AdminDashboard = () => {
  const [analytics, setAnalytics] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [projects, setProjects] = useState([]);
  const [candidates, setCandidates] = useState([]);
  const [recruiters, setRecruiters] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadData = async () => {
    try {
      setLoading(true);
      setError(null);
      const analyticsData = await getAnalytics();
      setAnalytics(analyticsData);
      const jobsData = await getAdminJobs();
      setJobs(jobsData || []);
      const projectsData = await getAdminProjects();
      setProjects(projectsData || []);
      const candidatesData = await getAdminCandidates();
      setCandidates(candidatesData || []);
      const recruitersData = await getAdminRecruiters();
      setRecruiters(recruitersData || []);
    } catch (err) {
      setError(err.message || "Failed to load admin data. Ensure backend is running.");
      console.error("Error loading admin data:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  if (loading) {
    return (
      <div className="max-w-6xl mx-auto p-6">
        <p className="text-center text-gray-600">Loading admin dashboard...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-6xl mx-auto p-6">
        <div className="card p-6 border-l-4 border-l-red-500 bg-red-50">
          <h3 className="font-semibold text-red-700 mb-2">Error loading dashboard</h3>
          <p className="text-red-600 text-sm">{error}</p>
          <button
            onClick={() => loadData()}
            className="mt-4 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 text-sm"
          >
            Retry
          </button>
        </div>
      </div>
    );
  }

  if (!analytics) {
    return (
      <div className="max-w-6xl mx-auto p-6">
        <p className="text-center text-gray-600">No analytics data available.</p>
      </div>
    );
  }

  const chartData = {
    labels: ["Candidates", "Recruiters", "Jobs", "Projects"],
    datasets: [
      {
        label: "Totals",
        data: [analytics.totalCandidates, analytics.totalRecruiters, analytics.totalJobs, analytics.totalProjects],
        backgroundColor: ["#f97316", "#10b981", "#111827", "#f59e0b"]
      }
    ]
  };

  return (
    <div className="grid gap-8">
      <SectionHeader title="Admin analytics" subtitle="Platform-wide usage insights." />
      <div className="grid md:grid-cols-4 gap-4">
        <StatCard label="Candidates" value={analytics.totalCandidates} accent="border-l-accent" />
        <StatCard label="Recruiters" value={analytics.totalRecruiters} accent="border-l-mint" />
        <StatCard label="Jobs" value={analytics.totalJobs} accent="border-l-ink" />
        <StatCard label="Projects" value={analytics.totalProjects} accent="border-l-amber-500" />
      </div>
      <div className="grid lg:grid-cols-2 gap-6">
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Totals overview</h3>
          <Bar data={chartData} />
        </div>
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Match averages</h3>
          <div className="mt-6 grid gap-3">
            <p className="text-sm">Average job match: {analytics.averageJobMatch}%</p>
            <p className="text-sm">Average project match: {analytics.averageProjectMatch}%</p>
            <p className="text-sm">Total applications: {analytics.totalApplications}</p>
          </div>
        </div>
      </div>
      <div className="grid lg:grid-cols-2 gap-6">
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Jobs</h3>
          <ul className="mt-4 space-y-2 text-sm">
            {jobs.map((job) => (
              <li key={job.id} className="border rounded-lg p-3">
                {job.title}
              </li>
            ))}
          </ul>
        </div>
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Projects</h3>
          <ul className="mt-4 space-y-2 text-sm">
            {projects.map((project) => (
              <li key={project.id} className="border rounded-lg p-3">
                {project.title}
              </li>
            ))}
          </ul>
        </div>
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Candidates</h3>
          <ul className="mt-4 space-y-2 text-sm">
            {candidates.map((candidate) => (
              <li key={candidate.id} className="border rounded-lg p-3">
                {candidate.name}
              </li>
            ))}
          </ul>
        </div>
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Recruiters</h3>
          <ul className="mt-4 space-y-2 text-sm">
            {recruiters.map((recruiter) => (
              <li key={recruiter.id} className="border rounded-lg p-3">
                {recruiter.name}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
