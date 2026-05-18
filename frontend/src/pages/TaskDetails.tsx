import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import { getTaskById, updateTask, deleteTask } from "../api/tasks";
import { getMe } from "../api/auth";

import type { Task } from "../types/task";
import type { User } from "../types/user";

export default function TaskDetails() {
  const params = useParams();

  const taskId = Number(params.taskId);
  const projectId = Number(params.projectId);

  const navigate = useNavigate();

  const [task, setTask] = useState<Task | null>(null);
  const [loading, setLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [editTitle, setEditTitle] = useState("");
  const [editDescription, setEditDescription] = useState("");
  const [currentUser, setCurrentUser] = useState<User | null>(null);

  useEffect(() => {
    if (isNaN(taskId) || isNaN(projectId)) {
      setLoading(false);
      return;
    }

    Promise.all([getMe(), getTaskById(projectId, taskId)])
      .then(([user, taskData]) => {
        setCurrentUser(user);

        if (!taskData) return;

        setTask(taskData);

        setEditTitle(taskData.name ?? "");
        setEditDescription(taskData.description ?? "");
      })
      .finally(() => setLoading(false));
  }, [taskId, projectId]);

  const handleUpdate = async () => {
    if (!task) return;

    try {
      await updateTask(projectId, task.id, {
        name: editTitle,
        description: editDescription,
      });

      setTask((prev) =>
        prev
          ? {
              ...prev,
              name: editTitle,
              description: editDescription,
            }
          : prev,
      );

      setIsEditing(false);
    } catch (err) {
      console.error(err);
      alert("Failed to update task");
    }
  };

  const handleDelete = async () => {
    if (!task) return;

    const confirmed = confirm("Are you sure you want to delete this task?");

    if (!confirmed) return;

    try {
      await deleteTask(task.id);

      navigate(-1);
    } catch (err) {
      console.error(err);
      alert("Failed to delete task");
    }
  };

  if (loading) return <div>Loading...</div>;

  if (!task) return <div>Task not found</div>;

  const canDelete =
    currentUser &&
    (task.createdById === currentUser.id ||
      task.assignedUser === currentUser.id);

  return (
    <div className="page-column">
      <section className="section-card">
        {isEditing ? (
          <>
            <input
              className="input"
              value={editTitle}
              onChange={(e) => setEditTitle(e.target.value)}
            />

            <textarea
              className="textarea"
              value={editDescription}
              onChange={(e) => setEditDescription(e.target.value)}
            />

            <div className="flex-row-gap">
              <button className="btn btn-primary" onClick={handleUpdate}>
                Save
              </button>

              <button className="btn" onClick={() => setIsEditing(false)}>
                Cancel
              </button>
            </div>
          </>
        ) : (
          <>
            <h1>{task.name}</h1>

            <p>{task.description || "No description"}</p>

            <div className="task-meta">
              <p>
                <strong>Status:</strong> {task.status || "OPEN"}
              </p>

              <p>
                <strong>Priority:</strong> {task.priority || "MEDIUM"}
              </p>
            </div>

            <div className="flex-row-gap">
              {canDelete && (
                <button className="btn" onClick={() => setIsEditing(true)}>
                  Edit
                </button>
              )}

              {canDelete && (
                <button className="btn btn-danger" onClick={handleDelete}>
                  Delete
                </button>
              )}
            </div>
          </>
        )}
      </section>
    </div>
  );
}
