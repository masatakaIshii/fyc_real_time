import { post } from "../../helper/url-helper";
import { push } from "svelte-spa-router";
import { isLoggedIn } from "../../stores/use-is-logged-in";

interface SignInRequest {
    email: string;
    password: string;
}

interface SubscribeRequest {
    name: string;
    email: string;
    password: string;
}

const JWT_TOKEN: string = "jwt-token";
const USERNAME: string = "username";
const ROLES: string = "roles";
const ROLE_ADMIN = "ROLE_ADMIN";

export function isAdmin() {
    return localStorage.getItem(ROLES).includes(ROLE_ADMIN);
}

export function getAuthUsername() {
    return localStorage.getItem(USERNAME);
}

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
            localStorage.setItem(JWT_TOKEN, jwtToken);
            localStorage.setItem(USERNAME, email);

            const roles = responsePost.headers.get("Roles");
            if (roles !== null && roles.length > 0) {
                localStorage.setItem(ROLES, roles);
            }
        }

    } catch (err) {
        throw Error(err);
    }
}

export async function subscribe(name: string, email: string, password: string) {
    const request: SubscribeRequest = {
        name,
        email,
        password
    }
    try {
        const response = await post("api/auth/register", request);
        if (response.ok !== true) {
            const errorText = await response.text();
            if (response.status === 403 && errorText.includes("USER_EMAIL_ALREADY_EXIST")) {
                throw `User with email '${email}' already exists`;
            } else {
                throw errorText;
            }
        }

    } catch(err) {
        throw Error(err);
    }
}

export function logout() {
    localStorage.removeItem(JWT_TOKEN);
    localStorage.removeItem(USERNAME);
    localStorage.removeItem(ROLES);
    isLoggedIn.reset();
    push("/home");
}