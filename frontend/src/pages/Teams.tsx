import { Link } from "react-router-dom";
//import { mockTeams } from "../mock/data";
import { getTeams } from "../api/teams";
import { routes } from "../router/routes";
import { useEffect, useState } from "react";
import type { Team } from "../types/team";

export default function Teams() {
  const [teams, setTeams] = useState<Team[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadTeams = async () => {
      try {
        const data = await getTeams();
        setTeams(data);
      } catch (err) {
        console.error(err);
        setError("Failed to load teams");
      } finally {
        setLoading(false);
      }
    };

    loadTeams();
  }, []);

  if (loading) {
    return <p>Loading teams...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>

      {/* HEADER */}
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h1 style={{ paddingLeft: "48px", marginBottom: "16px" }}>Teams</h1>

        <Link to={routes.createTeam()}>
          <button className="btn btn-primary">
            + Create Team
          </button>
        </Link>
      </div>

      {/* EMPTY STATE */}
      {teams.length === 0 ? (
        <div style={{ opacity: 0.7 }}>
          No teams yet. Create your first team.
        </div>
      ) : (
        /* LIST */
        <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
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
                display: "block",
              }}
            >
              <div style={{ fontWeight: 600 }}>{team.name}</div>
              {team.description && (
                <div style={{ fontSize: "13px", opacity: 0.7 }}>
                  {team.description}
                </div>
              )}
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}