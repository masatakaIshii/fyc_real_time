import HomePage from "./components/HomePage/HomePage.svelte"
import SignIn from "./components/SignIn/SignIn.svelte"
import SignUp from "./components/SignUp/SignUp.svelte"
import ListMeeting from "./components/ListMeeting/ListMeeting.svelte"
import MeetingForm from "./components/MeetingForm/MeetingForm.svelte"

export const routes = {
    '/': ListMeeting,
    '/home': HomePage,
    '/sign-in': SignIn,
    '/sign-up': SignUp,
    '/meeting': ListMeeting,
    '/meeting/create': MeetingForm
}
