import { writable } from "svelte/store";

export function useError() {
    const {subscribe, set, update } = writable("");

    return {
        subscribe,
        set: (message: string) => update(error => error = message),
        reset: () => set(""),
        isError: () => subscribe.length > 0
    }
}

export const error = useError();