export type TaskStatus = "TODO" | "IN_PROGRESS" | "DONE";
export type TaskPriority = "LOW" | "MEDIUM" | "HIGH";

export type Task = {
    id: number;
    title: string;
    name?: string;
    description?: string;
    status: TaskStatus;
    priority: TaskPriority;
    projectId: number;
    projectName?: string;
    assignedUser?: number;
    createdById: number;
    dueDate?: string;
}