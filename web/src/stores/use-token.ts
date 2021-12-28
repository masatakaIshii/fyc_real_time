import { writable } from "svelte/store";

function createToken() {
    const { subscribe, set, update } = writable("");

    return {
        subscribe,
        set: (newToken: string) => update(token => token = newToken),
        reset: () => set(""),

    }
}

export const token = createToken()
