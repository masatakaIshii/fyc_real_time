import { post } from "../../helper/url-helper";
import { push } from "svelte-spa-router";
import { isLoggedIn } from "../../stores/use-is-logged-in";

interface SignInRequest {
    email: string,
    password: string
}
const JWT_TOKEN: string = "jwt-token";
const USERNAME: string = "username";

export async function signIn(email: string, password: string) {
    const request: SignInRequest = {
        email,
        password,
    };

    try {
        const responsePost = await post("api/login", request);
        if (responsePost.status === 401) {
            throw "Email and/or password are wrong, check first if you sign up";
        }
        if (responsePost.status === 200) {
            const authorization = responsePost.headers.get("Authorization");
            if (!authorization.startsWith("Bearer ")) {
                throw "Problem jwt token";
            }
            const jwtToken = authorization.slice("Bearer ".length, authorization.length);
            isLoggedIn.set(true);
            sessionStorage.setItem(JWT_TOKEN, jwtToken);
            sessionStorage.setItem(USERNAME, email);
        }

    } catch (err) {
        throw Error(err);
    }
}

export function logout() {
    sessionStorage.removeItem(JWT_TOKEN);
    sessionStorage.removeItem(USERNAME);
    isLoggedIn.reset();
    push("/");
}