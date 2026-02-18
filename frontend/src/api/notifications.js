import api from "./client";

export const fetchNotifications = async (candidateId, params) => {
  const response = await api.get(`/notifications/candidate/${candidateId}`, { params });
  return response.data;
};

export const markNotificationRead = async (notificationId) => {
  const response = await api.post(`/notifications/${notificationId}/read`);
  return response.data;
};
