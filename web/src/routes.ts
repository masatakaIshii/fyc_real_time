import Home from "../src/components/Home/Home.svelte"
import SignIn from "./components/SignIn/SignIn.svelte"
import SignUp from "./components/SignUp/SignUp.svelte"

export const routes = {
    '/': Home,
    '/sign-in': SignIn,
    '/sign-up': SignUp
}
