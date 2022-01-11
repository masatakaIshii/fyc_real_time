import { writable } from "svelte/store";
import type { DtoMeeting } from "../types/meeting";

function useListMeeting() {
    const data: DtoMeeting[] = []
    const { subscribe, set, update } = writable(data);

    return {
        subscribe,
        set: (value: DtoMeeting[]) => update(list => list = value),
        removeOne: (value: DtoMeeting) => update(list => list = list.filter(meeting => meeting.id !== value.id)),
        reset: () => set([])
    }
}

export const listMeeting = useListMeeting();