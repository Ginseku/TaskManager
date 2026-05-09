import { api } from "./client";


export const getAllUsers = async (): Promise<any> => {
  const res = await api.get<string>("/user/getAll");
  return res.data;
};