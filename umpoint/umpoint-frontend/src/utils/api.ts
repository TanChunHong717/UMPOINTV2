import constants from "@/constants/app";
import axios from "axios";
import { getToken } from "./cache";
import qs from "qs";
import { ElMessage } from "element-plus";

const api = axios.create({
    baseURL: constants.apiUrl,
    timeout: constants.requestTimeout
});

api.interceptors.request.use(
    function (config) {
        config.headers["X-Requested-With"] = "XMLHttpRequest";
        config.headers["Request-Start"] = new Date().getTime();
        const token = getToken();
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
            console.log(response);
            throw new Error(response.data.message);
        }
        return response;
    }, function (error) {
        ElMessage.error(error.message);
        return Promise.reject(error);
    }
)

export default api;
