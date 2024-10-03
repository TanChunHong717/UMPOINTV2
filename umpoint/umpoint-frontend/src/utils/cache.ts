import { CacheToken } from "@/constants/app";
import { isNullOrUndefined } from "./utils";

const fix = "v1@";

/**
 * Storage option
 * @param isSessionStorage
 * @returns
 */
const cacheAdapter = (isSessionStorage?: boolean) => {
    return isSessionStorage ? sessionStorage : localStorage;
};

/**
 * Get Cache value
 * @param {*} key
 * @param {*} options
 */
export const getCache = (key: string, options?: any, defaultValue?: unknown): any => {
    key = fix + key;
    options = { isParse: true, isDelete: false, ...options };
    try {
        const value = cacheAdapter(options.isSessionStorage).getItem(key);
        if (options.isDelete) {
            cacheAdapter(options.isSessionStorage).removeItem(key);
        }
        return isNullOrUndefined(value)
            ? defaultValue
            : options.isParse
                ? value
                    ? JSON.parse(value)
                    : defaultValue
                : value;
    } catch (error) {
        console.error("getCache", error);
        return defaultValue;
    }
};

/**
 * Set cache value
 * @param {*} key
 * @param {*} value
 */
export const setCache = (
    key: string,
    value: string | Record<string, unknown> | Array<any>[],
    isSessionStorage?: boolean
): void => {
    key = fix + key;
    cacheAdapter(isSessionStorage).setItem(
        key,
        typeof value === "object" ? JSON.stringify(value) : value
    );
};

/**
 * Clear cache
 * @param key
 * @param isSessionStorage
 */
export const removeCache = (key: string, isSessionStorage?: boolean): void => {
    key = fix + key;
    cacheAdapter(isSessionStorage).removeItem(key);
};

export const getToken = (): string => {
    return getCache(CacheToken, { isSessionStorage: false }, {})["token"];
};
