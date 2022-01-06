import { get } from "../../helper/url-helper";
import type { DtoMeeting } from "../../types/meeting";

export async function getAllMeetings(): Promise<DtoMeeting[]> {
    try {
        const response = await get("api/meeting");
        return await response.json()
    } catch (err) {
        throw Error(err);
    }
}