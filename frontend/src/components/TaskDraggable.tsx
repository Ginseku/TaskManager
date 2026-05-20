import { useDraggable } from '@dnd-kit/core';
import { CSS } from '@dnd-kit/utilities';
import type { Task } from "../types/task";
import { Link } from "react-router-dom";
import { routes } from "../router/routes";

type LinkDetails = {
  teamId: number,
  projectId: number,
  taskId: number
}

export const TaskDraggable = ({ task, link_details }: { task: Task, link_details: LinkDetails }) => {
  const { attributes, listeners, setNodeRef, transform } = useDraggable({
    id: task.id,
    data: { type: 'task', task }, // Pass data for onDragEnd
  });

  const style = { transform: CSS.Translate.toString(transform) };

  return (
    <div ref={setNodeRef} style={style} {...attributes} {...listeners} className="task-card">
      <Link
        to={routes.task(Number(link_details.teamId), link_details.projectId, link_details.taskId)}
        style={{ "textDecoration": "none" }}
      >
        {task.name ?? task.title ?? "Unnamed Task"}
      </Link>
    </div>
  );
}