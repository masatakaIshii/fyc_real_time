import { get, post } from "../../helper/url-helper";
import type { CreateMeetingRequest, DtoMeeting } from "../../types/meeting";

export async function getAllMeetings(): Promise<DtoMeeting[]> {
    try {
        const response = await get("api/meeting");
        return await response.json()
    } catch (err) {
        throw Error(err);
    }
}

export async function createMeeting(name: string, description :string): Promise<number> {
    try {
        const body : CreateMeetingRequest = {
            name,
            description
        }
        const response = await post("api/meeting", body)
        if (response.status !== 201) {
            throw Error(`Problem to create new meeting : ${response.text()}`);
        }
        const newMeetingLocation = response.headers.get("Location");
        const lastSlash = newMeetingLocation.lastIndexOf("/");
        const newMeetingId = +newMeetingLocation.slice(lastSlash + 1);
        if (newMeetingId === NaN) {
            throw Error(`Problem new meeting id : ${response.text()}`);
        }
        return newMeetingId;
    } catch(err) {
        throw Error(err);
    }
}