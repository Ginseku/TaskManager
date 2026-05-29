import { useEffect, useState } from "react";
import { getMyTasks, updateTask, deleteTask } from "../api/tasks";
import type { Task } from "../types/task";

const STATUS_OPTIONS = ["TODO", "IN_PROGRESS", "DONE"];
const PRIORITY_OPTIONS = ["LOW", "MEDIUM", "HIGH"];

export default function MyTasks() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [tasks, setTasks] = useState<Task[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const groupedTasks = tasks.reduce(
    (groups, task) => {
      const key = task.projectName ?? "Unknown Project";

      if (!groups[key]) {
        groups[key] = [];
      }

      groups[key].push(task);

      return groups;
    },
    {} as Record<string, Task[]>,
  );

  useEffect(() => {
    load(page);
  }, [page]);

  const load = async (currentPage: number) => {
    try {
      setLoading(true);
      const data = await getMyTasks(currentPage, 5);
      setTasks(data.content);
      setTotalPages(data.totalPages);
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
      load(page);
    }
  };

  const handleDelete = async (taskId: number) => {
    setTasks((prev) => prev.filter((t) => t.id !== taskId));

    try {
      await deleteTask(taskId);
    } catch (err) {
      console.error(err);
      load(page);
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
        <>
          {Object.entries(groupedTasks).map(([project, projectTasks]) => (
            <div key={project} className="project-group">
              {/* PROJECT HEADER */}
              <div className="project-header">
                <h2>{project}</h2>
                <span className="project-count">
                  {projectTasks.length} tasks
                </span>
              </div>

              {/* TASKS */}
              <div className="project-task-list">
                {projectTasks.map((task) => (
                  <div key={task.id} className="task-panel">
                    {/* TITLE */}
                    <h3>{task.title}</h3>

                    {/* DESCRIPTION */}
                    <p>{task.description}</p>

                    {/* ACTIONS */}
                    <div className="task-actions">
                      {/* STATUS BUTTONS */}
                      <div className="status-group">
                        {STATUS_OPTIONS.map((s) => (
                          <button
                            key={s}
                            className={`status-btn ${
                              task.status === s ? "active" : ""
                            }`}
                            onClick={() => updateField(task.id, "status", s)}
                          >
                            {s}
                          </button>
                        ))}
                      </div>

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

                      <button className="delete-task-btn" onClick={() => handleDelete(task.id)}>
                        Delete
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}

          {/* PAGINATION */}
          <div
            className="pagination"
            style={{
              display: "flex",
              gap: 12,
              marginTop: 24,
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <button disabled={page === 0} onClick={() => setPage((p) => p - 1)}>
              Previous
            </button>

            <span>
              Page {page + 1} of {totalPages}
            </span>

            <button
              disabled={page >= totalPages - 1}
              onClick={() => setPage((p) => p + 1)}
            >
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
}
