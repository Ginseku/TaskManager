import { useDroppable } from '@dnd-kit/core';
import { type ProjectMember } from "../api/projects";

export const UserDroppable = ({ member, onAssign }: { member: ProjectMember; onAssign: (taskId: string) => void }) => {
  const { isOver, setNodeRef } = useDroppable({
    id: member.name,
    data: { type: 'user', member },
  });

  return (
    <div
      ref={setNodeRef}
      className={`member-card ${isOver ? 'highlight_dnd' : ''}`}
      key={member.name}
      // You can also use onDragOver etc. for more feedback
    >
      <p>{member.name}</p>
    </div>
  );
}