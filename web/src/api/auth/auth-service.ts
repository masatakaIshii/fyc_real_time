import { post } from "../../helper/url-helper";
import { token } from "../../stores/use-token";

interface SignInRequest {
    email: string,
    password: string
}

export async function signIn(email: string, password: string) {
    const request = {
        email,
        password,
    } as SignInRequest;

    try {
        const responsePost = await post("api/login", request);
        if (responsePost.status === 401) {
            throw Error("Email and/or password are wrong, check first if you sign up");
        }
        if (responsePost.status === 200) {
            const authorization = responsePost.headers.get("Authorization");
            if (!authorization.startsWith("Bearer ")) {
                throw "Problem jwt token";
            }
            const jwtToken = authorization.slice("Bearer ".length, authorization.length);
            token.set(jwtToken);
            sessionStorage.setItem("jwt-token", jwtToken);
        }

    } catch (err) {
        throw Error(err);
    }
}

export function signOut() {
    sessionStorage.removeItem("jwt-token");
}