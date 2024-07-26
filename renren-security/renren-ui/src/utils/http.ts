import app from "@/constants/app";
import { IHttpResponse, IObject } from "@/types/interface";
import router from "@/router";
import axios, { AxiosRequestConfig } from "axios";
import qs from "qs";
import { getToken } from "./cache";
import { getValueByKeys } from "./utils";
import { ElMessage } from "element-plus";

const http = axios.create({
  baseURL: app.api,
  timeout: app.requestTimeout
});

http.interceptors.request.use(
  function (config: any) {
    config.headers["X-Requested-With"] = "XMLHttpRequest";
    config.headers["Request-Start"] = new Date().getTime();
    const token = getToken();
    if (token) {
      config.headers["token"] = token;
    }
    if (config.method?.toUpperCase() === "GET") {
      config.params = { ...config.params, _t: new Date().getTime() };
    }
    if (Object.values(config.headers).includes("application/x-www-form-urlencoded")) {
      config.data = qs.stringify(config.data);
    }
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);
http.interceptors.response.use(
  (response) => {
    // 响应成功
    if (response.data.code === 0) {
      return response;
    }

    // 错误提示
    ElMessage.error(response.data.msg);

    if (response.data.code === 401) {
      //自定义业务状态码
      redirectLogin();
    }

    return Promise.reject(new Error(response.data.msg || "Error"));
  },
  (error) => {
    const status = getValueByKeys(error, "response.status", 500);
    const httpCodeLabel: IObject<string> = {
      400: "Request params error",
      401: "Unauthorized actions, please log in",
      403: "Access denied",
      404: `Request url error: ${getValueByKeys(error, "response.config.url", "")}`,
      408: "Request timeout",
      500: "API interface throw error 500",
      501: "Unimplement service",
      502: "Gateway error",
      503: "Service not available",
      504: "Gateway timeout",
      505: "HTTP version not support"
    };
    if (error && error.response) {
      console.error("Request error", error.response.data);
    }
    if (status === 401) {
      redirectLogin();
    }
    return Promise.reject(new Error(httpCodeLabel[status] || "Interface error"));
  }
);

const redirectLogin = () => {
  router.replace("/login");
  return;
};

export default (o: AxiosRequestConfig): Promise<IHttpResponse> => {
  return new Promise((resolve, reject) => {
    http(o)
      .then((res) => {
        return resolve(res.data);
      })
      .catch(reject);
  });
};
