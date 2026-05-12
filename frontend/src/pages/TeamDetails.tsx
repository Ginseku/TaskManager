import { useParams, Link } from "react-router-dom";
import { routes } from "../router/routes";
import { useEffect, useState } from "react";
import { getProjectsByTeam } from "../api/projects";
import type { Team } from "../types/team";
import type { Project } from "../types/project";
import {
  addMemberToTeam,
  getTeamMembers,
  removeMemberFromTeam,
  getTeamById,
} from "../api/teams";
import type { PublicUser } from "../types/publicUser";
import SelectMenu from "../components/Select";
import type { Option } from "../types/selectOption";

export default function TeamDetails() {
  const { teamId } = useParams<{ teamId: string }>();

  const [team, setTeam] = useState<Team | null>(null);
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [members, setMembers] = useState<PublicUser[]>([]);
  const [resetSelect, setResetSelect] = useState(0);

  const canManageMembers = team?.canManageMembers ?? false;

  const handleAddMember = async (user: PublicUser) => {
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

  const handleRemoveMember = async (userId: number) => {
    if (!teamId) return;

    try {
      await removeMemberFromTeam(Number(teamId), userId);

      setMembers((prev) => prev.filter((m) => m.id !== userId));
    } catch (err) {
      console.error(err);
      alert("Failed to remove member");
    }
  };

  useEffect(() => {
    if (!teamId) return;

    Promise.all([
      getTeamById(Number(teamId)),
      getProjectsByTeam(Number(teamId)),
      getTeamMembers(Number(teamId)),
    ])
      .then(([teamData, projectData, membersData]) => {
        setTeam(teamData);
        setProjects(projectData);
        setMembers(membersData);
      })
      .finally(() => setLoading(false));
  }, [teamId]);

  if (loading) return <div>Loading...</div>;

  if (!team) return <div>Team not found</div>;

  const existingMemberIds = members.map((m) => m.id);

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: "24px" }}>
      <h1>{team.name}</h1>
      <section>
        {members.length === 0 ? (
          <p>No members yet</p>
        ) : (
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              padding: "6px 10px",
              borderRadius: "6px",
              border: "1px solid #63637c",
            }}
          >
            {members.map((m) => (
              <div
                key={m.id}
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "space-between",
                  padding: "6px 10px",
                  
                }}
              >
                <span style={{ fontWeight: 500 }}>{m.name}</span>

                {canManageMembers && (
                  <button
                    onClick={() => handleRemoveMember(m.id)}
                    style={{
                      background: "transparent",
                      border: "1px solid red",
                      color: "red",
                      borderRadius: "6px",
                      padding: "2px 8px",
                      cursor: "pointer",
                    }}
                  >
                    Remove
                  </button>
                )}
              </div>
            ))}
          </div>
        )}

        {/* ADD MEMBERS BOX */}
        {canManageMembers && (
          <div
            style={{
              padding: "12px",
            }}
          >
            <strong>Add users to team</strong>

            <div style={{ marginTop: "12px" }}>
              <SelectMenu
                existingMembers={existingMemberIds}
                resetKey={resetSelect}
                onAddUsers={(users: Option[]) => {
                  users.forEach((user) => {
                    handleAddMember({
                      id: user.value,
                      name: user.label,
                    });
                  });
                  setResetSelect((prev) => prev + 1);
                }}
              />
            </div>
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
