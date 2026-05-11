import { api } from "./client";
import type { Team } from "../types/team";
import { mockTeams } from "../mock/data";
import type { CreateTeamRequest } from "../types/createTeamRequest";
import type { UserTeam } from "../types/userTeam";
import type { TeamCreatedResponse } from "../types/teamCreatedResponse";
import type { PublicUser } from "../types/publicUser";

const USE_MOCK = false;

export const getUserTeams = async (): Promise<UserTeam[]> => {
  const res = await api.get<UserTeam[]>("/teams");
  return res.data;
};

export const getTeams = async (): Promise<Team[]> => {
  if (USE_MOCK) {
    return mockTeams;
  }

  const res = await api.get<Team[]>("/teams");
  return res.data;
};

export const getTeamById = async (teamId: number): Promise<Team | null> => {
  if (USE_MOCK) {
    return mockTeams.find((t) => t.id === teamId) || null;
  }

  const res = await api.get<Team>(`/teams/${teamId}`);
  return res.data;
};

export const addMemberToTeam = async (
  teamId: number,
  userId: number,
): Promise<string> => {
  const res = await api.post<string>(`/teams/${teamId}/members`, {
    userId,
  });

  return res.data;
};

export const createTeam = async (
  data: CreateTeamRequest,
): Promise<TeamCreatedResponse> => {
  const res = await api.post<TeamCreatedResponse>("/teams/createTeam", data);

  return res.data;
};

export const getTeamMembers = async (teamId: number): Promise<PublicUser[]> => {
  const res = await api.get<PublicUser[]>(`/teams/${teamId}/getMembers`);

  return res.data;
};

export const getAllUsers = async (): Promise<PublicUser[]> => {
  const res = await api.get<PublicUser[]>("/user/getAll");

  return res.data;
};

export const removeMemberFromTeam = async (
  teamId: number,
  userId: number,
): Promise<void> => {
  await api.delete(`/teams/${teamId}/members/${userId}`);
};
