export type Team = {
    id: number;
    name: string;
    canManageMembers: boolean;
    description?: string;
    createdBy: number;
}