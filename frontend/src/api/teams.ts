import { api } from "./client";
import type { Team } from "../types/team";
import { mockTeams } from "../mock/data";
import type { CreateTeamRequest } from "../types/createTeamRequest";

const USE_MOCK = true;

export const getTeams = async (): Promise<Team[]> => {
  if (USE_MOCK) {
    return mockTeams;
  }

  const res = await api.get<Team[]>("/teams");
  return res.data;
};

export const getTeamById = async (
  teamId: number
): Promise<Team | null> => {
  if (USE_MOCK) {
    return mockTeams.find((t) => t.id === teamId) || null;
  }

  const res = await api.get<Team>(`/teams/${teamId}`);
  return res.data;
};

export const addMemberToTeam = async (
  teamId: number,
  userId: number
): Promise<string> => {
  const res = await api.post<string>(
    `/teams/${teamId}/members`,
    {
      userId,
    }
  );

  return res.data;
};

export const createTeam = async (
  data: CreateTeamRequest
): Promise<Team> => {
  const res = await api.post<Team>(
    "/teams/createTeam",
    data
  );

  return res.data;
};