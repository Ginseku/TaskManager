import axios from "axios";

export const api = axios.create({
  baseURL: "https://taskmanager-api.graycliff-1a6e4b04.westus2.azurecontainerapps.io",
  headers: {
    "Content-Type": "application/json",
  },
});

//Request interceptor to attach token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("authToken");

  const publicPaths = ["/auth/registerUser", "/auth/loginUser"];
  const isPublic = publicPaths.some((path) => config.url?.includes(path));

  if (!isPublic && token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});
