import { api } from "./client";
import type { DashboardStats } from "../types/dashboardStats";


export const getAllUsers = async () => {
  const res = await api.get("/user/getAll");

  return res.data.data;
};

export const getDashboardStats = async (): Promise<DashboardStats> => {
  const res = await api.get<DashboardStats>("/user/me/dashboard");

  return res.data;
};