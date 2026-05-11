import { api } from "./client";


export const getAllUsers = async () => {
  const res = await api.get("/user/getAll");

  return res.data.data;
};