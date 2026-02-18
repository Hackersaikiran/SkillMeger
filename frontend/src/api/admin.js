import api from "./client";

export const getAnalytics = async () => {
  const response = await api.get("/admin/analytics");
  return response.data;
};

export const getAdminJobs = async () => {
  const response = await api.get("/admin/jobs");
  return response.data;
};

export const getAdminProjects = async () => {
  const response = await api.get("/admin/projects");
  return response.data;
};

export const getAdminCandidates = async () => {
  const response = await api.get("/admin/candidates");
  return response.data;
};

export const getAdminRecruiters = async () => {
  const response = await api.get("/admin/recruiters");
  return response.data;
};
