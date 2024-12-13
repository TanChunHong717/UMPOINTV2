import axios from "axios";
import qs from "qs";

import constants from "@/constants/app";
import { getToken, getRememberMe } from "./cache";

const api = axios.create({
    baseURL: constants.apiUrl,
    timeout: constants.requestTimeout
});

api.interceptors.request.use(
    function (config) {
        config.headers["X-Requested-With"] = "XMLHttpRequest";
        config.headers["Request-Start"] = new Date().getTime();
        const token = getToken(getRememberMe())
        if (token) {
            config.headers["token"] = token;
        }
        if (config.method?.toUpperCase() === "GET") {
            config.params = { ...config.params, _t: new Date().getTime() };
        }
        if (
            Object.values(config.headers).includes(
                "application/x-www-form-urlencoded"
            )
        ) {
            config.data = qs.stringify(config.data);
        }
        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    function (response) {
        if (response.data.code && response.data.code !== 0) {
            // manually throw status 200 but code 400 error
            console.log(response);
            let errorMessage = response.data.data?.msg ?? response.data.msg;
            throw new Error(errorMessage, { cause: response.data });
        }
        return response;
    }, function (error) {
        let errorMessage = error.response.data?.msg ?? error.response.data?.data?.msg ?? error.response.data?.data?.message;
        throw new Error(errorMessage, { cause: error });
    }
)

export default api;
