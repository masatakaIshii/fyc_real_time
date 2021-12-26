import HomePage from "./components/HomePage/HomePage.svelte"
import SignIn from "./components/SignIn/SignIn.svelte"
import SignUp from "./components/SignUp/SignUp.svelte"
import ListMeeting from "./components/Meeting/ListMeeting.svelte"

export const routes = {
    '/': HomePage,
    '/sign-in': SignIn,
    '/sign-up': SignUp,
    '/meeting': ListMeeting
}
