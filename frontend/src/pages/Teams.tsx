import { Link } from "react-router-dom";
import { mockTeams } from "../mock/data";
import { routes } from "../router/routes";
import React, { useState, useEffect } from 'react';
import { getAllUsers } from '../api/users';
import SelectMenu from "../components/Select";

export default function Teams() {

  const [options, setOptions] = useState<any[]>([]);        // For dropdown options
  const [selectedOptions, setSelectedOptions] = useState<any>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        setLoading(true);
        const response = await getAllUsers();

        // Transform to only id and name + format for React Select
        const formattedOptions = response.data.map((user: any) => ({
          value: user.id,      // required by most React Select components
          label: user.name,    // required by most React Select components
        }));

        setOptions(formattedOptions);
      } catch (err: any) {
        setError(err.response?.data?.message || err.message || "Failed to fetch users");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  return (
    <div style= {{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <h1>Teams</h1>

      <div style = {{ margin: '10px' , width: '50%'}}>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        {loading && <p>Loading users...</p>}

        <SelectMenu
          options={options}
          setSelectedOptions={setSelectedOptions}
          // Add other useful props if your SelectMenu supports them:
          // isMulti={true}
          // placeholder="Search users..."
        />

        <button onClick= { () => {
          console.log(selectedOptions);
        } } style = {{ margin: '10px' }}>hi there</button>
      </div>

      {mockTeams.length === 0 ? (
        <p>No teams yet</p>
      ) : (
        mockTeams.map((team) => (
          <div key={team.id}>
            <Link to={routes.team(team.id)}>{team.name}</Link>
          </div>
        ))
      )}
    </div>
  );
}
