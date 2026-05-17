import { api } from "./client";
import type { Task } from "../types/task";
import { mockTasks } from "../mock/data";

const USE_MOCK = false;

export const getTasksByProject = async (projectId: number): Promise<Task[]> => {
  if (USE_MOCK) {
    return mockTasks.filter((t) => t.projectId === projectId);
  }

  const res = await api.get<{content:  Task[]}>(`/tasks/${projectId}`);
  return res.data.content;
};

export const getTaskById = async (projectId: number, taskId: number): Promise<Task | null> => {
  if (USE_MOCK) {
    return mockTasks.find((t) => t.id === taskId) || null;
  }

  const res = await api.get<Task>(`/tasks//${projectId}/${taskId}`);
  return res.data;
};

export const createTask = async (projectId: number, task: Partial<Task>) => {
  return api.post(`/tasks/${projectId}`, task);
};

export const updateTask = async (projectId: number, taskId: number, task: Partial<Task>) => {
  return api.put(`/tasks/${projectId}/${taskId}`, task);
};

export const deleteTask = async (taskId: number) => {
  return api.delete(`/tasks/${taskId}`);
};