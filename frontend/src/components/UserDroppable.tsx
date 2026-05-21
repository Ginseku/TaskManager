import { useDroppable } from '@dnd-kit/core';
import { type ProjectMember } from "../api/projects";

export const UserDroppable = ({ member, tasks }: { member: ProjectMember; tasks : String[]}) => {
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
      <div style={{width: "50%"}}>{member.name}</div>
      <div style={{width: "50%"}}>
        {tasks.map((task) => (
        <div className="user-tasks"> { task } </div>
      ))}
      </div>
    </div>
  );
}