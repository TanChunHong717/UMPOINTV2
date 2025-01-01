import { IHttpResponse, IObject } from "@/types/interface";
import http from "../utils/http";
import {ElMessage} from "element-plus";

export default {
  delete(path: string, params: IObject): Promise<IHttpResponse> {
    return http({
      url: path,
      data: params,
      method: "DELETE"
    }).catch((error) => {
      const errorMessage = error.response?.data?.message || "An error occurred while deleting the resource.";
      ElMessage.error(errorMessage);
      return Promise.reject(error);
    });
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
          if (error.response?.data?.message) {
            // If the backend returned a message, show it
            ElMessage.error(error.response.data.message);
          } else {
            ElMessage.error("An error occurred while fetching the data.");
          }
          reject(error);
        });
    });
  },

  put(path: string, params?: IObject, headers?: IObject): Promise<IHttpResponse> {
    return http({
      url: path,
      data: params,
      headers: {
        "Content-Type": "application/json;charset=UTF-8",
        ...headers
      },
      method: "PUT"
    }).catch((error) => {
      const errorMessage = error.response?.data?.message || "An error occurred while updating the resource.";
      ElMessage.error(errorMessage);
      return Promise.reject(error);
    });
  },

  post(path: string, body?: IObject, headers?: IObject): Promise<IHttpResponse> {
    return http({
      url: path,
      method: "POST",
      headers: {
        "Content-Type": "application/json;charset=UTF-8",
        ...headers
      },
      data: body
    }).catch((error) => {
      const errorMessage = error.response?.data?.message || "An error occurred while submitting the data.";
      ElMessage.error(errorMessage);
      return Promise.reject(error);
    });
  }
};
