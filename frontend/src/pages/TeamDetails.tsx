import { useParams, Link } from "react-router-dom";
import { routes } from "../router/routes";
import { useEffect, useState } from "react";
import { getTeamById } from "../api/teams";
import { getProjectsByTeam } from "../api/projects";
import type { Team } from "../types/team";
import type { Project } from "../types/project";
import { addMemberToTeam } from "../api/teams";
import type { User } from "../types/user";

export default function TeamDetails() {
  const { teamId } = useParams<{ teamId: string }>();

  const [team, setTeam] = useState<Team | null>(null);
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);

  //Placeholder until we get BE
  const [members, setMembers] = useState<User[]>([]);

  const handleAddMember = async (user: User) => {
    if (!teamId) return;
    try {
      await addMemberToTeam(Number(teamId), user.id);
      // avoid duplicates
      setMembers((prev) => {
        const exists = prev.some((m) => m.id === user.id);
        if (exists) return prev;
        return [...prev, user];
      });
    } catch (err) {
      console.error(err);
      alert("Failed to add member");
    }
  };

  useEffect(() => {
    if (!teamId) return;

    Promise.all([
      getTeamById(Number(teamId)),
      getProjectsByTeam(Number(teamId)),
      // TODO (backend not ready):
      // - GET /teams/{id}/members
      // - integrate SearchBar results
    ])
      .then(([teamData, projectData]) => {
        setTeam(teamData);
        setProjects(projectData);
        // TODO (backend not ready):
        // - GET /teams/{id}/members
        // - integrate SearchBar results
      })
      .finally(() => setLoading(false));
  }, [teamId]);

  if (loading) return <div>Loading...</div>;

  if (!team) return <div>Team not found</div>;

  return (
  <div style={{ display: "flex", flexDirection: "column", gap: "24px" }}>
    
    {/* TEAM HEADER */}
    <section>
      <h1>{team.name}</h1>
      <p>{team.description}</p>
    </section>

    {/* MEMBERS */}
    <section>
      <h2>Members</h2>

      {/* TODO: SearchBar and ADD MEMBERS */}
      <div
  style={{
    padding: "12px",
    border: "1px dashed #aaa",
    borderRadius: "8px",
    marginBottom: "12px",
    background: "#fafafa",
  }}
>
  <strong>Add users to team</strong>

  <p style={{ margin: "6px 0 0", fontSize: "13px", opacity: 0.7 }}>
    Search and invite users to this team (SearchBar coming soon)
  </p>
</div>

      {members.length === 0 ? (
        <p>No members yet</p>
      ) : (
        <div style={{ display: "flex", flexDirection: "column", gap: "6px" }}>
          {members.map((m) => (
            <div key={m.id}>
              {m.name} <span style={{ opacity: 0.6 }}>({m.email})</span>
            </div>
          ))}
        </div>
      )}
    </section>

    {/* PROJECTS */}
    <section>
      <h2>Projects</h2>

      {projects.length === 0 ? (
        <p>No projects yet</p>
      ) : (
        <div style={{ display: "flex", flexDirection: "column", gap: "6px" }}>
          {projects.map((project) => (
            <div key={project.id}>
              <Link to={routes.project(team.id, project.id)}>
                {project.name}
              </Link>
            </div>
          ))}
        </div>
      )}
    </section>

  </div>
);
}
