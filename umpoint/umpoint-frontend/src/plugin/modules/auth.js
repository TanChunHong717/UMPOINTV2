import {
    login as loginApi,
    logout as logoutApi,
    getUserInformation,
} from "@/helpers/api-credentials";
import { setCache, getCache, removeCache } from "@/utils/cache";
import { CacheToken } from "@/constants/app";
import router from "../router";

const auth = {
    namespaced: true,
    state() {
        return {
            userId: null,
            username: null,
            permissions: {},
            token: null,
            isRememberMe: false,
        };
    },
    getters: {
        isAuthenticated(state) {
            return state.token !== null;
        },
        isRememberMe(state) {
            return state.isRememberMe;
        }
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
        setRememberMe(state, isRememberMe) {
            state.isRememberMe = isRememberMe;
        }
    },
    actions: {
        async loginRememberMe({ commit, dispatch }) {
            let tokenInfo = getCache(
                CacheToken,
                { isSessionStorage: false },
                null
            );

            if (!tokenInfo || !tokenInfo.token) {
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
            commit("setUserId", userInfo.data.data.id);
            commit("setPermissions", {
                space: !!userInfo.data.data.spacePermission,
                service: !!userInfo.data.data.servicePermission,
                accommodation: !!userInfo.data.data.accommodationPermission,
            });
        },
        async login({ commit }, { username, password, rememberMe }) {
            // login logic
            const response = await loginApi(username, password);
            if (response.status !== 200 || response.data.code !== 0) {
                throw new Error("Invalid credentials");
            }

            commit("setUsername", response.data.username);
            commit("setToken", response.data.token);
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
                !rememberMe // use session storage if rememberMe is false
            );
            commit("setRememberMe", rememberMe);

            // get user info
            const userInfo = await getUserInformation();
            if (userInfo.status !== 200 || userInfo.data.code !== 0) {
                throw new Error("Server error");
            }
            commit("setUserId", userInfo.data.id);
            commit("setPermissions", {
                space: !!userInfo.data.spacePermission,
                service: !!userInfo.data.servicePermission,
                accommodation: !!userInfo.data.accommodationPermission,
            });

            return true;
        },
        async logout({ commit }) {
            try {
                await logoutApi();
            } catch (error) {
                // do nothing
                console.error(error);
            }
            commit("setToken", null);
            commit("setUserId", null);
            commit("setUsername", null);
            commit("setPermissions", {});
            removeCache(CacheToken, false);

            // redirect to home page if user is on a page that requires login
            if (router.currentRoute.value.meta.requiresAuth) {
                router.push({ name: "home" });
            }
        },
    },
};

export default auth;
