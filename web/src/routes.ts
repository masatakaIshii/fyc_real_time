import HomePage from "./components/HomePage/HomePage.svelte"
import SignIn from "./components/SignIn/SignIn.svelte"
import SignUp from "./components/SignUp/SignUp.svelte"
import ListMeeting from "./components/ListMeeting/ListMeeting.svelte"
import MeetingForm from "./components/MeetingForm/MeetingForm.svelte"
import Page404 from "./components/ErrorPages/404.svelte";
import Page401 from "./components/ErrorPages/401.svelte";
import { wrap } from "svelte-spa-router/wrap";
import { getIsLoggedIn } from "../src/stores/use-is-logged-in";
import { replace, RouteDetail } from "svelte-spa-router"

function guardUserAuth(_detail: RouteDetail) {
    if (getIsLoggedIn() === false) {
        if (_detail.location === "/") {
            replace("/home");
        } else {
            replace("/401");
        }
        return false;
    }
    return true;
}

export const routes = {
    '/': wrap({
        component: ListMeeting,
        conditions: [
            guardUserAuth
        ]
    }),
    '/home': HomePage,
    '/sign-in': SignIn,
    '/sign-up': SignUp,
    '/meeting': wrap({
        component: ListMeeting,
        conditions: [
            guardUserAuth
        ]
    }),
    '/meeting-form': wrap({
        component: MeetingForm,
        conditions: [
            guardUserAuth
        ]
    }),
    '/401':Page401,
    '*': Page404
}
