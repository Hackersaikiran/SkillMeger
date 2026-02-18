import api from "./client";

export const createJob = async (payload) => {
  const response = await api.post("/jobs", payload);
  return response.data;
};

export const createProject = async (payload) => {
  const response = await api.post("/projects", payload);
  return response.data;
};

export const getJobApplicants = async (jobId) => {
  const response = await api.get(`/recruiters/jobs/${jobId}/applicants`);
  return response.data;
};

export const getProjectApplicants = async (projectId) => {
  const response = await api.get(`/recruiters/projects/${projectId}/applicants`);
  return response.data;
};

export const getRecruiterAnalytics = async (recruiterId) => {
  const response = await api.get(`/recruiters/${recruiterId}/analytics`);
  return response.data;
};

export const getRecruiterJobs = async (recruiterId) => {
  const response = await api.get(`/recruiters/${recruiterId}/jobs`);
  return response.data;
};

export const deleteJob = async (jobId) => {
  const response = await api.delete(`/jobs/${jobId}`);
  return response.data;
};

export const updateJob = async (jobId, payload) => {
  const response = await api.put(`/jobs/${jobId}`, payload);
  return response.data;
};
