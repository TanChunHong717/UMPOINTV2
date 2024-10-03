import {
    login as loginApi,
    logout as logoutApi,
    getUserInformation,
} from "@/helpers/credentials.js";
import { setCache, getCache } from "@/utils/cache";
import { CacheToken } from "@/constants/app";

const auth = {
    namespaced: true,
    state() {
        return {
            userId: null,
            username: null,
            permissions: {},
            token: null,
        };
    },
    getters: {
        isLoggedIn(state) {
            return state.token !== null;
        },
    },
    mutations: {
        setUserId(state, userId) {
            state.userId = userId;
        },
        setUsername(state, username) {
            state.username = username;
        },
        setPermissions(state, permissions) {
            state.permissions = permissions;
        },
        setToken(state, token) {
            state.token = token;
        },
    },
    actions: {
        async loginRememberMe({ commit, dispatch }) {
            //     dispatch("login", {
            //         username: "test",
            //         password: "1234",
            //     });
            let tokenInfo = getCache(
                CacheToken,
                { isSessionStorage: false },
                {}
            );

            if (!tokenInfo.token) {
                return;
            }
            if (new Date(tokenInfo.expiry) < Date.now()) {
                dispatch("logout");
                return;
            }

            // store in vuex
            commit("setToken", tokenInfo.token);

            // get user info
            const userInfo = await getUserInformation();
            if (userInfo.status !== 200 || userInfo.data.code !== 0) {
                if (userInfo.data.code === 401) {
                    dispatch("logout");
                    return;
                }
                throw new Error("Server error");
            }
            commit("setUserId", userInfo.data.id);
            commit("setPermissions", {
                space: userInfo.data.spacePermission,
                service: userInfo.data.servicePermission,
                accommodation: userInfo.data.accommodationPermission,
            });
        },
        async login({ commit }, { username, password }) {
            // login logic
            const response = await loginApi(username, password);
            if (response.status !== 200 || response.data.code !== 0) {
                throw new Error("Invalid credentials");
            }

            commit("setUsername", response.data.username);
            commit("setToken", response.data.token);
            // store in browser
            let expiryDate = new Date();
            expiryDate.setSeconds(
                expiryDate.getSeconds() + response.data.data.expire
            );
            setCache(
                CacheToken,
                {
                    token: response.data.data.token,
                    expiry: expiryDate,
                },
                false
            );

            // get user info
            const userInfo = await getUserInformation();
            if (response.status !== 200 || response.data.code !== 0) {
                throw new Error("Server error");
            }
            commit("setUserId", userInfo.data.id);
            commit("setPermissions", {
                space: userInfo.data.spacePermission,
                service: userInfo.data.servicePermission,
                accommodation: userInfo.data.accommodationPermission,
            });
        },
        async logout({ commit }) {
            logoutApi();
            commit("setUserId", null);
            commit("setUsername", null);
            commit("setPermissions", {});
            commit("setToken", null);
            setCache(CacheToken, null, false);
        },
    },
};

export default auth;
