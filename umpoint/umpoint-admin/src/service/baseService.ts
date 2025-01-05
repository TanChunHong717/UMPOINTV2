import { IHttpResponse, IObject } from "@/types/interface";
import http from "../utils/http";
import {ElMessage} from "element-plus";
import error from "@/views/error.vue";

export default {
  delete(path: string, params: IObject): Promise<IHttpResponse> {
    return new Promise((resolve, reject) => {
      http({
        url: path,
        data: params,
        method: "DELETE"
      })
        .then(resolve)
        .catch((error) => {
          ElMessage.error(error?.message);
          reject(error);
        });
    })
  },

  get(path: string, params?: IObject, headers?: IObject): Promise<IHttpResponse> {
    return new Promise((resolve, reject) => {
      http({
        url: path,
        params,
        headers,
        method: "GET"
      })
        .then(resolve)
        .catch((error) => {
          ElMessage.error(error.message)
          reject(error);
        });
    });
  },

  put(path: string, params?: IObject, headers?: IObject): Promise<IHttpResponse> {
    return new Promise((resolve, reject) => {
      http({
        url: path,
        data: params,
        headers: {
          "Content-Type": "application/json;charset=UTF-8",
          ...headers
        },
        method: "PUT"
      })
        .then(resolve)
        .catch((error) => {
        ElMessage.error(error.message);
        reject(error);
      });
    })
  },

  post(path: string, body?: IObject, headers?: IObject): Promise<IHttpResponse> {
    return new Promise((resolve, reject) => {
      http({
        url: path,
        method: "POST",
        headers: {
          "Content-Type": "application/json;charset=UTF-8",
          ...headers
        },
        data: body
      })
        .then(resolve)
        .catch((error) => {
        ElMessage.error(error.message);
        reject(error);
      });
    })
  }
};
