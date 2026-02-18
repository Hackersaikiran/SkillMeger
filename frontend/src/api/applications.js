import api from "./client";

export const applyToPosting = async (payload) => {
  const response = await api.post("/applications", payload);
  return response.data;
};

export const fetchCandidateApplicationsPaged = async (candidateId, params) => {
  const response = await api.get(`/applications/candidate/${candidateId}/page`, { params });
  return response.data;
};

export const updateApplicationStatus = async (applicationId, payload) => {
  const response = await api.patch(`/applications/${applicationId}/status`, payload);
  return response.data;
};
