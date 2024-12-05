import { createRouter, createWebHistory } from "vue-router";
import routes from "./routes.js";
import store from "./store.js";

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from) => {
    // redirect pages that require login to login page
    if (to.meta.requiresAuth && !store.getters["auth/isAuthenticated"]) {
        return {
            name: "login",
            query: { to: encodeURIComponent(to.fullPath) },
        };
    }

    return true;
});

export default router;
