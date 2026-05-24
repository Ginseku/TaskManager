import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { mockUser, mockTeams, mockProjects } from "../mock/data";
import { routes } from "../router/routes";
import { useAuth } from "../context/AuthContext";
import { getTeams } from "../api/teams";
import { getProjectsByTeam } from "../api/projects";
import { getDashboardStats } from "../api/users";

import type { Team } from "../types/team";
import type { Project } from "../types/project";

export default function Dashboard() {
  const { user } = useAuth();

  const [teams, setTeams] = useState<Team[]>([]);
  const [projects, setProjects] = useState<Project[]>([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [stats, setStats] = useState({
  teams: 0,
  projects: 0,
  assignedTasks: 0,
  createdTasks: 0,
});

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        setLoading(true);
        // Load teams first
        const [teamsData, statsData] = await Promise.all([
        getTeams(),
        getDashboardStats(),
      ]);

        setTeams(teamsData);
        setStats(statsData);

        // Load projects for every team
        const projectsPerTeam = await Promise.all(
          teamsData.map((team) =>
            getProjectsByTeam(team.id).then((projects) =>
              projects.map((p) => ({ ...p, teamId: team.id })),
            ),
          ),
        );
        // Flatten arrays
        const allProjects = projectsPerTeam.flat();

        setProjects(allProjects);
      } catch (err) {
        console.error(err);
        setError("Failed to load dashboard");
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, []);

  if (loading) {
    return <p>Loading dashboard...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        gap: "24px",
        padding: "24px",
      }}
    >
      {/* HEADER */}
      <section className="section-card">
        <h1 style={{ marginBottom: "8px" }}>Dashboard</h1>

        <p style={{ opacity: 0.8 }}>Welcome back, {user?.name || "Guest"}</p>
      </section>

      {/* STATS */}
      <section
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))",
          gap: "16px",
        }}
      >
        <div className="section-card">
          <h2>{stats.teams}</h2>

          <p style={{ opacity: 0.7 }}>Teams</p>
        </div>

        <div className="section-card">
          <h2>{stats.projects}</h2>

          <p style={{ opacity: 0.7 }}>Projects</p>
        </div>

        <div className="section-card">
          <h2>{stats.assignedTasks}</h2>

          <p style={{ opacity: 0.7 }}>Assigned Tasks</p>
        </div>
        <div className="section-card">
          <h2>{stats.createdTasks}</h2>

          <p style={{ opacity: 0.7 }}>Created Tasks</p>
        </div>
      </section>

      {/* TEAMS */}
      <section className="section-card">
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "16px",
          }}
        >
          <h2>Your Teams</h2>

          <Link to={routes.teams()}>
            <button className="btn btn-primary">View All</button>
          </Link>
        </div>

        {teams.length === 0 ? (
          <p>No teams yet</p>
        ) : (
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              gap: "10px",
            }}
          >
            {teams.map((team) => (
              <Link
                key={team.id}
                to={routes.team(team.id)}
                style={{
                  padding: "12px",
                  border: "1px solid var(--border)",
                  borderRadius: "10px",
                  textDecoration: "none",
                  color: "inherit",
                }}
              >
                <div style={{ fontWeight: 600 }}>{team.name}</div>
              </Link>
            ))}
          </div>
        )}
      </section>

      {/* PROJECTS */}
      <section className="section-card">
        <h2 style={{ marginBottom: "16px" }}>Projects</h2>

        {projects.length === 0 ? (
          <p>No projects yet</p>
        ) : (
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              gap: "10px",
            }}
          >
            {projects.map((project) => {
              return (
                <Link
                  key={project.id}
                  to={routes.project(project.teamId, project.id)}
                  style={{
                    padding: "12px",
                    border: "1px solid var(--border)",
                    borderRadius: "10px",
                    textDecoration: "none",
                    color: "inherit",
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                  }}
                >
                  <span style={{ fontWeight: 600 }}>{project.name}</span>

                  <span
                    style={{
                      fontSize: "13px",
                      opacity: 0.7,
                    }}
                  >
                    {teams.find((t) => t.id === project.teamId)?.name}
                  </span>
                </Link>
              );
            })}
          </div>
        )}
      </section>
    </div>
  );
}
