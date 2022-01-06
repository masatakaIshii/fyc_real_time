import type { User } from "./user";

export interface DtoMeeting {
    id: number,
    name: string,
    createdDateTime: Date,
    creator: User,
    isClosed: boolean
}