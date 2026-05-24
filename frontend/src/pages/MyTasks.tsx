import { useEffect, useState } from "react";
import { getMyTasks, updateTask, deleteTask } from "../api/tasks";
import type { Task } from "../types/task";

const STATUS_OPTIONS = ["TODO", "IN_PROGRESS", "DONE"];
const PRIORITY_OPTIONS = ["LOW", "MEDIUM", "HIGH"];

export default function MyTasks() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const data = await getMyTasks();
      setTasks(data);
    } catch (err) {
      setError("Failed to load tasks");
    } finally {
      setLoading(false);
    }
  };

  const updateField = async (
    taskId: number,
    field: "status" | "priority" | "dueDate",
    value: string,
  ) => {
    const task = tasks.find((t) => t.id === taskId);
    if (!task) return;

    const updated = { ...task, [field]: value };

    setTasks((prev) => prev.map((t) => (t.id === taskId ? updated : t)));

    try {
      const projectId = (task as any).projectId ?? (task as any).project?.id;

      await updateTask(projectId, taskId, {
        [field]: value,
      });
    } catch (err) {
      console.error(err);
      load();
    }
  };

  const handleDelete = async (taskId: number) => {
    setTasks((prev) => prev.filter((t) => t.id !== taskId));

    try {
      await deleteTask(taskId);
    } catch (err) {
      console.error(err);
      load();
    }
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div style={{ padding: 24 }}>
      <h1>My Tasks</h1>

      {tasks.length === 0 ? (
        <p>No tasks assigned to you</p>
      ) : (
        <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
          {tasks.map((task) => (
            <div key={task.id} className="section-card">
              <h3>{task.title}</h3>
              <p style={{ opacity: 0.7 }}>{task.description}</p>

              <div style={{ display: "flex", gap: 12 }}>
                {/* STATUS */}
                <select
                  value={task.status ?? ""}
                  onChange={(e) =>
                    updateField(task.id, "status", e.target.value)
                  }
                >
                  {STATUS_OPTIONS.map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </select>

                {/* PRIORITY */}
                <select
                  value={task.priority ?? ""}
                  onChange={(e) =>
                    updateField(task.id, "priority", e.target.value)
                  }
                >
                  {PRIORITY_OPTIONS.map((p) => (
                    <option key={p} value={p}>
                      {p}
                    </option>
                  ))}
                </select>

                {/* DUE DATE */}
                <input
                  type="date"
                  value={task.dueDate ?? ""}
                  onChange={(e) =>
                    updateField(task.id, "dueDate", e.target.value)
                  }
                />

                <button onClick={() => handleDelete(task.id)}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
