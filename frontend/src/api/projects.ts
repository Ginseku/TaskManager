import { api } from "./client";
import type { Project } from "../types/project";
import { mockProjects } from "../mock/data";
import type { ProjectResponse } from "../types/ProjectResponse";

const USE_MOCK = false;

export type ProjectPayload = {
  name: string;
  description: string;
};

export type ProjectMember = {
  name: string;
  role: "OWNER" | "ADMIN" | "MEMBER" | string;
};

export const getProjectsByTeam = async (teamId: number): Promise<Project[]> => {
  if (USE_MOCK) {
    return mockProjects.filter((p) => p.teamId === teamId);
  }

  const res = await api.get<Project[]>(`/teams/${teamId}/projects`);
  return res.data;
};

export const getProjectById = async (
  projectId: number,
): Promise<Project | null> => {
  if (USE_MOCK) {
    return mockProjects.find((p) => p.id === projectId) || null;
  }

  const res = await api.get<ProjectResponse>(`/project/${projectId}/details`);

  if (!res.data) return null;

  return {
    id: res.data.id,
    name: res.data.name,
    description: res.data.description,
    teamId: 0,
    createdBy: 0,
  };
};

export const createProject = async (
  teamId: number,
  payload: ProjectPayload,
): Promise<void> => {
  if (USE_MOCK) return;

  await api.post(`/project/${teamId}`, payload);
};

export const updateProject = async (
  projectId: number,
  payload: { name?: string; description?: string },
): Promise<void> => {
  await api.patch(`/project/${projectId}`, payload);
};

export const deleteProject = async (projectId: number): Promise<void> => {
  if (USE_MOCK) return;

  await api.delete(`/project/${projectId}`);
};

export const getProjectMembers = async (
  projectId: number,
): Promise<ProjectMember[]> => {
  if (USE_MOCK) {
    return [];
  }

  const res = await api.get<ProjectMember[]>(`/project/${projectId}`);
  return res.data;
};
