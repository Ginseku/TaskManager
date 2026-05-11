import Select from 'react-select';
import { useState, useEffect } from 'react';
import { getAllUsers } from '../api/users';

const SelectMenu = () => {

    const [options, setOptions] = useState<any>(null);        // For dropdown options
    const [selectedOptions, setSelectedOptions] = useState<any>(null);     // for options which are already selected
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                setLoading(true);
                const response = await getAllUsers();

                const formattedOptions = response.data.map((user: any) => ({
                    value: user.id,
                    label: user.name,
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
        <Select
            isMulti
            name="team_users"
            options={ options }
            onChange={ setSelectedOptions }
            className="basic-multi-select"
            classNamePrefix="select"
        />
    );
}

export default SelectMenu;