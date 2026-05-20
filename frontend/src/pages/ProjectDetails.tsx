import { useParams, Link, useNavigate } from "react-router-dom";
//import { mockProjects } from "../mock/data";
import { routes } from "../router/routes";
import { useEffect, useState, useRef } from "react";
import { getTasksByProject, createTask, deleteTask } from "../api/tasks";
import {
  getProjectsByTeam,
  deleteProject,
  getProjectMembers,
  type ProjectMember,
} from "../api/projects";
import { getTeamById } from "../api/teams";
import type { Team } from "../types/team";

import type { Task } from "../types/task";
import type { Project } from "../types/project";
import type { PublicUser } from "../types/publicUser";
import { getProjectById, updateProject } from "../api/projects";
import { getMe } from "../api/auth";
import type { User } from "../types/user";

import { DndContext, type DragEndEvent } from '@dnd-kit/core';

import { UserDroppable } from "../components/UserDroppable";
import { TaskDraggable } from "../components/TaskDraggable";

export default function ProjectDetails() {
  //const { projectId, teamId } = useParams<{
  //  projectId: string;
  //  teamId: string;
  //}>();
  const params = useParams();

  const projectId = Number(params.projectId);
  const teamId = Number(params.teamId);

  if (!params.projectId || !params.teamId) {
    return <div>Invalid URL</div>;
  }

  const navigate = useNavigate();

  const [project, setProject] = useState<Project | null>(null);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [members, setMembers] = useState<ProjectMember[]>([]);
  const [team, setTeam] = useState<Team | null>(null);
  const canManageMembers = team?.canManageMembers ?? false;
  const [isEditing, setIsEditing] = useState(false);
  const [editName, setEditName] = useState("");
  const [editDescription, setEditDescription] = useState("");
  const formRef = useRef<HTMLFormElement>(null);
  const [currentUser, setCurrentUser] = useState<User | null>(null);

  const [page, setPage] = useState(0);
  const [pageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    if (isNaN(projectId) || isNaN(teamId)) {
      setLoading(false);
      return;
    }

    Promise.all([
      getMe(),
      getTeamById(Number(teamId)),
      getProjectById(Number(projectId)),
      //getTasksByProject(Number(projectId)),
      getTasksByProject(Number(projectId), page, pageSize),
      getProjectMembers(Number(projectId)),
    ])
      .then(([user, teamData, projectData, tasksData, membersData]) => {
        setCurrentUser(user);
        setTeam(teamData);
        setProject(projectData);
        setTasks(tasksData.content);
        setTotalPages(tasksData.totalPages);
        setMembers(membersData as any);

        if (projectData) {
          setEditName(projectData.name ?? "");
          setEditDescription(projectData.description ?? "");
        }
      })
      .finally(() => setLoading(false));
  }, [projectId, teamId, page, pageSize]);

  const handleDelete = async () => {
    if (!projectId || !teamId) return;

    const confirmed = confirm("Are you sure you want to delete this project?");

    if (!confirmed) return;

    try {
      await deleteProject(Number(projectId));

      navigate(routes.team(Number(teamId)));
    } catch (err) {
      console.error(err);
      alert("Failed to delete project");
    }
  };

  const handleUpdate = async () => {
    if (!projectId) return;

    try {
      await updateProject(Number(projectId), {
        name: editName,
        description: editDescription,
      });

      setProject((prev) =>
        prev ? { ...prev, name: editName, description: editDescription } : prev,
      );

      setIsEditing(false);
    } catch (err) {
      console.error(err);
      alert("Failed to update project");
    }
  };

  const handleCreateTask = async () => {
    if (!projectId || !formRef.current) return;

    const form = formRef.current;

    const titleInput = form.elements.namedItem(
      "title",
    ) as HTMLInputElement | null;
    const descriptionInput = form.elements.namedItem(
      "description",
    ) as HTMLInputElement | null;

    if (!titleInput) return;

    const title = titleInput.value;
    const description = descriptionInput?.value ?? "";

    try {
      await createTask(projectId, { title, description });

      const updatedTasks = await getTasksByProject(projectId, page, pageSize);

      setTasks(updatedTasks.content);
      setTotalPages(updatedTasks.totalPages);

      form.reset(); // safe now, using ref
    } catch (err) {
      console.error("Failed to create task:", err);
      alert("Failed to create task");
    }
  };

  const handleDragEnd = (event: DragEndEvent) => {
    const { active, over } = event;

    if (over && active.data.current?.type === 'task' && over.data.current?.type === 'user') {
      const taskId = active.id as string;
      const userId = over.id as string;
      
      // Assign task to user (update your state / call API)
      // assignTaskToUser(taskId, userId);
    }
  };

  if (loading) return <div>Loading...</div>;

  if (!project) {
    return <div>Project not found</div>;
  }

  return (
    <div className="page-column">
      {/* PROJECT HEADER */}
      <section className="section-card">
        {isEditing ? (
          <>
            <input
              value={editName}
              onChange={(e) => setEditName(e.target.value)}
              className="input"
            />

            <textarea
              value={editDescription}
              onChange={(e) => setEditDescription(e.target.value)}
              className="textarea"
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
            <h1>{project.name}</h1>
            <p>{project.description}</p>

            {canManageMembers && (
              <div className="flex-row-gap">
                <button className="btn" onClick={() => setIsEditing(true)}>
                  Edit
                </button>

                <button className="btn btn-danger" onClick={handleDelete}>
                  Delete project
                </button>
              </div>
            )}
          </>
        )}
      </section>
      
      <DndContext onDragEnd={handleDragEnd}>
        {/* MEMBERS */}
        <section className="section-card">
          <h2>Members</h2>

          {members.length === 0 ? (
            <p>No members</p>
          ) : (
            <div className="member-list">
              {members.map((m) => (
                // <div key={m.name} className="member-card">
                //   {m.name}
                // </div>
                <UserDroppable key={m.name} member={m} onAssign={ () => {} } />
              ))}
            </div>
          )}
        </section>

        {/* TASKS */}
        <section className="section-card">
          <h2>Tasks</h2>

          {tasks.length === 0 ? (
            <p>No tasks yet</p>
          ) : (
            <div className="task-list">
              {tasks.map((task) => {
                const canDelete =
                  currentUser &&
                  (task.createdById === currentUser.id ||
                    task.assignedUser === currentUser.id);
                return (
                  <div key={task.id} className="task-card-container">
                    
                    {/* task.name ?? task.title ?? "Unnamed Task" */}
                    <TaskDraggable key={task.id} task={task} link_details={{ teamId: teamId, projectId: project.id, taskId: task.id }}/>

                    {canDelete && (
                      <button
                        className="btn btn-danger btn-small delete-task-btn"
                        onClick={async (e) => {
                          e.preventDefault();
                          if (
                            confirm("Are you sure you want to delete this task?")
                          ) {
                            try {
                              await deleteTask(task.id);

                              const updatedTasks = await getTasksByProject(
                                projectId,
                                page,
                                pageSize,
                              );

                              setTasks(updatedTasks.content);
                              setTotalPages(updatedTasks.totalPages);
                            } catch (err) {
                              console.error(err);
                              alert("Failed to delete task");
                            }
                          }
                        }}
                      >
                        Remove
                      </button>
                    )}
                  </div>
                );
              })}
            </div>
          )}
          <div className="pagination">
            <button
              className="btn"
              disabled={page === 0}
              onClick={() => setPage((p) => p - 1)}
            >
              Previous
            </button>

            <span>
              Page {page + 1} of {totalPages}
            </span>

            <button
              className="btn"
              disabled={page + 1 >= totalPages}
              onClick={() => setPage((p) => p + 1)}
            >
              Next
            </button>
          </div>
        </section>
      </DndContext>
      <section className="section-card">
        <h2>Create New Task</h2>
        <form
          ref={formRef}
          onSubmit={(e) => {
            e.preventDefault();
            handleCreateTask();
          }}
          className="form-column"
        >
          <input name="title" placeholder="Task title" required />
          <input name="description" placeholder="Description" />
          <button type="submit" className="btn btn-primary">
            Add Task
          </button>
        </form>
      </section>
    </div>
  );
}
