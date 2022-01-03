import { writable } from "svelte/store";
import { getToken } from "../helper/url-helper";

function userIsLoggedIn() {
    const isTokenNotNull = getToken() !== null
    const { subscribe, set, update } = writable(isTokenNotNull);

    return {
        subscribe,
        set: (value: boolean) => update(isLoggedIn => isLoggedIn = value),
        reset: () => set(false),
    }
}

export const isLoggedIn = userIsLoggedIn()
