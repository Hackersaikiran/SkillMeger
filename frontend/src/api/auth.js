import api from "./client";

export const login = async (payload) => {
  const response = await api.post("/auth/login", payload);
  return response.data;
};

export const registerCandidate = async (payload) => {
  const response = await api.post("/auth/register/candidate", payload);
  return response.data;
};

export const registerRecruiter = async (payload) => {
  const response = await api.post("/auth/register/recruiter", payload);
  return response.data;
};

export const registerAdmin = async (payload) => {
  const response = await api.post("/auth/register/admin", payload);
  return response.data;
};
