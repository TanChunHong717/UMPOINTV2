import api from "@/utils/api";

export function login(email, password) {
    // login logic
    return api.post(`/cli/login`, {
        username: email,
        password: password,
        captcha: null,
        uuid: null,
    });
}

export function logout() {
    return api.post(`/cli/logout`);
}

export function register(data) {
    return api.post("/client/user/register", data);
}

export function getUserInformation() {
    return api.get(`/client/user/info`);
}

export function saveUserInformation(userId, data) {
    return api.put(`/client/user/${userId}`, data);
}
