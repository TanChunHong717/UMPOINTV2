import {
    login as loginApi,
    logout as logoutApi,
    getUserInformation,
} from "@/helpers/api-credentials";
import {
    setCache,
    getCache,
    removeCache,
    setRememberMe,
    setToken,
    getRememberMe,
} from "@/utils/cache";
import { CacheRememberMeToken, CacheToken } from "@/constants/app";
import router from "../router";

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
        isAuthenticated(state) {
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
        async loginRememberMe({ commit, dispatch, state }) {
            let tokenInfo = getCache(
                CacheToken,
                { isSessionStorage: !getRememberMe() },
                null
            );

            if (!tokenInfo || !tokenInfo.token) {
                return;
            }
            if (new Date(tokenInfo.expiry) < Date.now()) {
                return await dispatch("logout");
            }

            // store in vuex
            commit("setToken", tokenInfo.token);

            return await dispatch("getUserPermission");
        },
        async login({ commit, dispatch }, { username, password, rememberMe }) {
            // login logic
            const response = await loginApi(username, password);
            if (response.status !== 200 || response.data.code !== 0) {
                throw new Error("Invalid credentials");
            }

            // store if remember me (permanently in localstorage) to check use localstorage or sessionstorage
            setRememberMe(rememberMe);

            // store in vuex
            commit("setToken", response.data.data.token);

            let expiryDate = new Date();
            expiryDate.setSeconds(
                expiryDate.getSeconds() + response.data.data.expire
            );
            setToken({
                token: response.data.data.token,
                expiry: expiryDate,
            }, rememberMe);

            return await dispatch("getUserPermission");
        },
        async getUserPermission({ commit }) {
            // get user info
            const response = await getUserInformation();
            if (response.status !== 200 || response.data.code !== 0) {
                throw new Error("Server error");
            }
            let userInfo = response.data.data;

            commit("setUsername", userInfo.username);
            commit("setUserId", userInfo.id);
            commit("setPermissions", {
                space: !!userInfo.spacePermission,
                service: !!userInfo.servicePermission,
                accommodation: !!userInfo.accommodationPermission,
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

            // clear both cache for remember me (local) and session
            removeCache(CacheToken, false);
            removeCache(CacheToken, true);

            // redirect to home page if user is on a page that requires login
            if (router.currentRoute.value.meta.requiresAuth) {
                router.push({ name: "home" });
            }
        },
    },
};

export default auth;
