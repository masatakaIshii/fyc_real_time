export async function signIn(email: string, password: string): Promise<void> {
    const body = JSON.stringify({
        email,
        password,
    });
    const requestInfo = {
        method: "POST",
        headers: {
            Accept: "*/*",
            "Content-Type": "application/json",
        },
        mode: "cors",
        body,
    } as RequestInit;
    const request = new Request(
        "http://localhost:8080/api/login",
        requestInfo
    );
    try {
        const responsePost = await fetch(request);
        if (responsePost.status === 401) {
            throw Error("Email and/or password are wrong, check first if you sign up")
        }
        if (responsePost.status === 200) {
            console.log(responsePost.headers.get("Authorization"));
            // TODO : save authorization jwt without 'Bearer' in session storage
        }

    } catch (err) {
        throw Error(err)
    }
}