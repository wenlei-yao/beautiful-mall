import axios from 'axios';

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10_000
});

http.interceptors.request.use(config => {
  const token = localStorage.getItem('admin-web-token') || sessionStorage.getItem('admin-web-token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

http.interceptors.response.use(
  response => response,
  error => {
    if (axios.isAxiosError<{ message?: string }>(error) && error.response?.data?.message) {
      error.message = error.response.data.message;
    }
    return Promise.reject(error);
  }
);
