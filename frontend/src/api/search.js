import api from "./client";

export const searchJobs = async (params) => {
  const response = await api.get("/jobs/search", { params });
  return response.data;
};

export const searchProjects = async (params) => {
  const response = await api.get("/projects/search", { params });
  return response.data;
};
