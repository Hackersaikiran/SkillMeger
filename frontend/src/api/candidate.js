import api from "./client";

export const fetchCandidate = async (id) => {
  const response = await api.get(`/candidates/${id}`);
  return response.data;
};

export const updateCandidate = async (id, payload) => {
  const response = await api.put(`/candidates/${id}`, payload);
  return response.data;
};

export const uploadResume = async (id, file) => {
  const formData = new FormData();
  formData.append("file", file);
  const response = await api.post(`/candidates/${id}/resume`, formData, {
    headers: { "Content-Type": "multipart/form-data" }
  });
  return response.data;
};

export const getCandidateApplications = async (id) => {
  const response = await api.get(`/applications/candidate/${id}`);
  return response.data;
};

export const getRecommendations = async (payload) => {
  const response = await api.post("/matching/recommendations", payload);
  return response.data;
};
