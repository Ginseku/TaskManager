import { useParams, Link, useNavigate } from "react-router-dom";
//import { mockProjects } from "../mock/data";
import { routes } from "../router/routes";
import { useEffect, useState } from "react";
import { getTasksByProject } from "../api/tasks";
import {
  getProjectsByTeam,
  deleteProject,
  getProjectMembers,
  type ProjectMember,
} from "../api/projects";
import { getTeamById } from "../api/teams";
import type { Team } from "../types/team";

import type { Task } from "../types/task";
import type { Project } from "../types/project";
import type { PublicUser } from "../types/publicUser";
import { getProjectById } from "../api/projects";
import { updateProject } from "../api/projects";

export default function ProjectDetails() {
  //const { projectId, teamId } = useParams<{
  //  projectId: string;
  //  teamId: string;
  //}>();
  const params = useParams();

  const projectId = Number(params.projectId);
  const teamId = Number(params.teamId);

  if (!params.projectId || !params.teamId) {
  return <div>Invalid URL</div>;
}

  const navigate = useNavigate();

  const [project, setProject] = useState<Project | null>(null);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [members, setMembers] = useState<ProjectMember[]>([]);
  const [team, setTeam] = useState<Team | null>(null);
  const canManageMembers = team?.canManageMembers ?? false;
  const [isEditing, setIsEditing] = useState(false);
  const [editName, setEditName] = useState("");
  const [editDescription, setEditDescription] = useState("");

  useEffect(() => {
    if (isNaN(projectId) || isNaN(teamId)) {
      setLoading(false);
      return;
    }

    console.log("projectId:", projectId, "teamId:", teamId);
    Promise.all([
      getTeamById(Number(teamId)),
      getProjectById(Number(projectId)),
      //getTasksByProject(Number(projectId)),
      getProjectMembers(Number(projectId)),
    ])
      .then(([teamData, projectData, membersData]) => {
        setTeam(teamData);
        setProject(projectData);
        //setTasks(tasksData);
        setMembers(membersData as any);

        if (projectData) {
          setEditName(projectData.name ?? "");
          setEditDescription(projectData.description ?? "");
        }
      })
      .finally(() => setLoading(false));
  }, [projectId, teamId]);

  const handleDelete = async () => {
    if (!projectId || !teamId) return;

    const confirmed = confirm("Are you sure you want to delete this project?");

    if (!confirmed) return;

    try {
      await deleteProject(Number(projectId));

      navigate(routes.team(Number(teamId)));
    } catch (err) {
      console.error(err);
      alert("Failed to delete project");
    }
  };

  const handleUpdate = async () => {
    if (!projectId) return;

    try {
      await updateProject(Number(projectId), {
        name: editName,
        description: editDescription,
      });

      setProject((prev) =>
        prev ? { ...prev, name: editName, description: editDescription } : prev,
      );

      setIsEditing(false);
    } catch (err) {
      console.error(err);
      alert("Failed to update project");
    }
  };

  if (loading) return <div>Loading...</div>;

  if (!project) {
    return <div>Project not found</div>;
  }

  return (
    <div
      style={{ display: "flex", flexDirection: "column", gap: 24, padding: 24 }}
    >
      {/* PROJECT HEADER */}
      <section className="section-card">
        {isEditing ? (
          <>
            <input
              value={editName}
              onChange={(e) => setEditName(e.target.value)}
              style={{
                padding: "8px 10px",
                borderRadius: 8,
                border: "1px solid var(--border)",
                width: "100%",
              }}
            />

            <textarea
              value={editDescription}
              onChange={(e) => setEditDescription(e.target.value)}
              style={{
                marginTop: 8,
                padding: "8px 10px",
                borderRadius: 8,
                border: "1px solid var(--border)",
                width: "100%",
                minHeight: 80,
              }}
            />

            <div style={{ display: "flex", gap: 8, marginTop: 10 }}>
              <button className="btn btn-primary" onClick={handleUpdate}>
                Save
              </button>

              <button className="btn" onClick={() => setIsEditing(false)}>
                Cancel
              </button>
            </div>
          </>
        ) : (
          <>
            <h1>{project.name}</h1>
            <p>{project.description}</p>

            {canManageMembers && (
              <div style={{ display: "flex", gap: 8 }}>
                <button className="btn" onClick={() => setIsEditing(true)}>
                  Edit
                </button>

                <button className="btn btn-danger" onClick={handleDelete}>
                  Delete project
                </button>
              </div>
            )}
          </>
        )}
      </section>

      {/* MEMBERS */}
      <section className="section-card">
        <h2>Members</h2>

        {members.length === 0 ? (
          <p>No members</p>
        ) : (
          <div style={{ display: "flex", flexDirection: "column", gap: 8 }}>
            {members.map((m) => (
              <div
                key={m.name}
                style={{
                  padding: "8px 10px",
                  border: "1px solid var(--border)",
                  borderRadius: 8,
                  background: "var(--bg)",
                }}
              >
                {m.name}
              </div>
            ))}
          </div>
        )}
      </section>

      {/* TASKS */}
      <section className="section-card">
        <h2>Tasks</h2>

        {tasks.length === 0 ? (
          <p>No tasks yet</p>
        ) : (
          <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
            {tasks.map((task) => (
              <Link
                key={task.id}
                to={routes.task(Number(teamId), project.id, task.id)}
                style={{
                  display: "flex",
                  alignItems: "center",
                  padding: "10px 12px",
                  border: "1px solid var(--border)",
                  borderRadius: 8,
                  textDecoration: "none",
                  color: "var(--text-h)",
                  background: "var(--bg)",
                  transition: "0.2s",
                }}
              >
                {task.title}
              </Link>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
