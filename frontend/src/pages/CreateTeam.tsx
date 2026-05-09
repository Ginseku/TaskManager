import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { createTeam } from "../api/teams";
import { routes } from "../router/routes";

export default function CreateTeam() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    if (!name.trim()) {
      setError("Team name is required");
      return;
    }

    try {
      setLoading(true);

      const newTeam = await createTeam({
        name,
        description,
      });

      // redirect to new team page
      navigate(routes.team(newTeam.id));
    } catch (err) {
      console.error(err);
      setError("Failed to create team");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: "520px", margin: "0 auto" }}>
      
      <h1 style={{ marginBottom: "20px" }}>Create Team</h1>

      <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "16px" }}>

        {/* NAME */}
        <div style={{ display: "flex", flexDirection: "column", gap: "6px" }}>
          <label>Team name</label>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="e.g. Frontend Team"
            style={{
              padding: "10px",
              border: "1px solid var(--border)",
              borderRadius: "8px",
              background: "var(--bg)",
              color: "var(--text)",
            }}
          />
        </div>

        {/* DESCRIPTION */}
        <div style={{ display: "flex", flexDirection: "column", gap: "6px" }}>
          <label>Description</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Optional"
            rows={4}
            style={{
              padding: "10px",
              border: "1px solid var(--border)",
              borderRadius: "8px",
              background: "var(--bg)",
              color: "var(--text)",
              resize: "vertical",
            }}
          />
        </div>

        {/* ERROR */}
        {error && (
          <div style={{ color: "red", fontSize: "14px" }}>
            {error}
          </div>
        )}

        {/* ACTION */}
        <button
          type="submit"
          className="btn btn-primary"
          disabled={loading}
        >
          {loading ? "Creating..." : "Create Team"}
        </button>
      </form>
    </div>
  );
}