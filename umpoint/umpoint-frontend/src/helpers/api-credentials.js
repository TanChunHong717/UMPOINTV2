import api from "@/utils/api";

function login(email, password) {
    // login logic
    return api.post(`/cli/login`, {
        username: email,
        password: password,
        captcha: null,
        uuid: null,
    });
}

function logout() {
    return api.post(`/cli/logout`);
}

function getUserInformation() {
    return api.get(`/client/user/info`);
}

export { login, logout, getUserInformation };
