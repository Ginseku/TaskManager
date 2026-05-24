import { api } from "./client";
import type { Task } from "../types/task";
import { mockTasks } from "../mock/data";

const USE_MOCK = false;

export type PageResponse<T> = {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // current page
  first: boolean;
  last: boolean;
};

/*
export const getTasksByProject = async (projectId: number): Promise<Task[]> => {
  if (USE_MOCK) {
    return mockTasks.filter((t) => t.projectId === projectId);
  }

  const res = await api.get<{content:  Task[]}>(`/tasks/${projectId}`);
  return res.data.content;
};
*/

export const getTasksByProject = async (
  projectId: number,
  page = 0,
  size = 5,
): Promise<PageResponse<Task>> => {
  const res = await api.get<PageResponse<Task>>(
    `/tasks/${projectId}?page=${page}&size=${size}`,
  );

  return res.data;
};

export const getTaskById = async (projectId: number, taskId: number): Promise<Task | null> => {
  if (USE_MOCK) {
    return mockTasks.find((t) => t.id === taskId) || null;
  }

  const res = await api.get<Task>(`/tasks/${projectId}/${taskId}`);
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

export const assignTask = async (taskId : number, username : string) => {
  return api.put(`/tasks/${taskId}/${username}/assign`);
}

export const unassignTask = async (task_title : String) => {
  return api.put(`/tasks/${task_title}/unassign`);
}

export const getMyTasks = async () => {
  const res = await api.get<Task[]>("/tasks/me");
  return res.data;
};