const URL = "http://localhost:8080/";


export function getToken(): string | null {
    return sessionStorage.getItem("jwt-token");
}

export async function post<T>(path: string, bodyParam: T): Promise<Response> {
    const body = JSON.stringify(bodyParam)
    const token = getToken()
    const requestInfo = {
        method: "POST",
        headers: {
            Accept: "*/*",
            "Content-Type": "application/json",
            Authorization: "Bearer " + (token || "")
        },
        mode: "cors",
        body,
    } as RequestInit;
    const request = new Request(
        URL + path,
        requestInfo
    );
    return await fetch(request)
}

export async function get(path: string): Promise<Response> {
    const token = getToken();
    const requestInfo = {
        method: "GET",
        headers: {
            Accept: "*/*",
            Authorization: "Bearer " + (token || "")
        },
        mode: "cors"
    } as RequestInit;
    const request = new Request(
        URL + path,
        requestInfo
    )
    return await fetch(request)
}