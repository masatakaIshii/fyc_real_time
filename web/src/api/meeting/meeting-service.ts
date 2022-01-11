import { deleteRequest, getRequest, postRequest } from "../../helper/url-helper";
import type { CreateMeetingRequest, DtoMeeting } from "../../types/meeting";

const MEETING_PATH = "api/meeting"

export async function getAllMeetings(): Promise<DtoMeeting[]> {
    try {
        const response = await getRequest(MEETING_PATH);
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
        const response = await postRequest(MEETING_PATH, body)
        if (response.status !== 201) {
            throw `Problem to create new meeting : ${await response.text()}`;
        }
        const newMeetingLocation = response.headers.get("Location");
        const lastSlash = newMeetingLocation.lastIndexOf("/");
        const newMeetingId = +newMeetingLocation.slice(lastSlash + 1);
        if (newMeetingId === NaN) {
            throw `Problem new meeting id : ${await response.text()}`;
        }
        return newMeetingId;
    } catch(err) {
        throw Error(err);
    }
}

export async function deleteMeeting(meetingId: number): Promise<void> {
    try {
        const response = await deleteRequest(`${MEETING_PATH}/${meetingId}`);
        if (!response.ok) {
            throw `Problem delete meeting : ${await response.text()}`
        }
    } catch(err) {
        throw Error(err);
    }
}