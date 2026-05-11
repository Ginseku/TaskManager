import Select from "react-select";
import { useState, useEffect } from "react";
import { getAllUsers } from "../api/users";
import type { Option } from "../types/selectOption";
import type { PublicUser } from "../types/publicUser";

type Props = {
  onAddUsers: (users: Option[]) => void;
  resetKey?: number;
  existingMembers?: number[];
};

const SelectMenu = ({ onAddUsers, resetKey, existingMembers }: Props) => {
  //const [options, setOptions] = useState<any>(null); // For dropdown options
  //const [selectedOptions, setSelectedOptions] = useState<any>(null); // for options which are already selected
  const [selectedOptions, setSelectedOptions] = useState<Option[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [allUsers, setAllUsers] = useState<PublicUser[]>([]);

  const options: Option[] = allUsers
  .filter((user) => !(existingMembers ?? []).includes(user.id))
  .map((user) => ({
    value: user.id,
    label: user.name,
  }));

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        setLoading(true);

        const users = await getAllUsers();
         setAllUsers(users);
      } catch (err: any) {
        setError(
          err.response?.data?.message || err.message || "Failed to fetch users",
        );
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, [existingMembers, resetKey]);

  useEffect(() => {
    setSelectedOptions([]);
  }, [resetKey]);

  return (
    <Select<Option, true>
      isMulti
      name="team_users"
      options={options}
      value={selectedOptions}
      onChange={(newValue) => {
        const selected = newValue as Option[];
        setSelectedOptions(selected);
        onAddUsers(selected);
      }}
      className="basic-multi-select"
      classNamePrefix="select"
      styles={{
        container: (base) => ({
          ...base,
          backgroundColor: "transparent",
        }),

        control: (base, state) => ({
          ...base,
          backgroundColor: "var(--bg)",
          borderColor: state.isFocused ? "var(--accent)" : "var(--border)",
          boxShadow: "none",
          outline: "none",
          borderRadius: "10px",
        }),

        valueContainer: (base) => ({
          ...base,
          backgroundColor: "var(--bg)",
        }),

        singleValue: (base) => ({
          ...base,
          color: "var(--text-h)",
        }),

        input: (base) => ({
          ...base,
          color: "var(--text-h)",
          backgroundColor: "transparent",
        }),

        menu: (base) => ({
          ...base,
          backgroundColor: "var(--bg)",
          border: "1px solid var(--border)",
          borderRadius: "10px",
        }),

        menuList: (base) => ({
          ...base,
          backgroundColor: "var(--bg)",
        }),
      }}
    />
  );
};

export default SelectMenu;
