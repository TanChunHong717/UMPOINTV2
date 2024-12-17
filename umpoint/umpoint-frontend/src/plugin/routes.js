import store from "./store";

export default [
    {
        path: "/",
        name: "home",
        component: () => import("@/views/MainPage.vue"),
    },
    {
        path: "/search",
        name: "search",
        component: () => import("@/views/Search.vue"),
        meta: {
            title: "Search",
        },
    },
    {
        path: "/:type(space|service|accommodation|facility)",
        name: "facility",
        meta: {
            // set title based on the type of facility
            title: (route) =>
                route.params.type.charAt(0).toUpperCase() +
                route.params.type.slice(1),
        },
        children: [
            {
                path: ":id",
                name: "facility-information",
                component: () =>
                    import("@/views/facility/FacilityInformation.vue"),
                meta: {
                    title: "Information",
                },
                props: true,
            },
            {
                path: ":id/reserve",
                name: "facility-reserve",
                component: () =>
                    import("@/views/facility/FacilityReservationForm.vue"),
                meta: {
                    title: "Reservation",
                    requiresAuth: true,
                },
                props: true,
            },
        ],
    },
    {
        path: "/profile",
        name: "profile",
        component: () => import("@/views/user/ProfileSettings.vue"),
        meta: {
            title: "Profile",
            requiresAuth: true,
        },
    },
    {
        path: "/bookings",
        name: "bookings",
        component: () => import("@/views/user/BookingHistory.vue"),
        meta: {
            title: "Bookings",
            requiresAuth: true,
        },
    },
    {
        path: "/chat",
        name: "chat",
        component: () => import("@/views/user/ChatHistory.vue"),
        meta: {
            title: "Chat History",
            requiresAuth: true,
        },
    },
    {
        path: "/login",
        name: "login",
        component: () => import("@/views/user/Login.vue"),
        meta: {
            title: "Login",
        },
        beforeEnter: (to, from) => {
            // redirect to home if already logged in
            if (store.getters["auth/isAuthenticated"]) {
                return { name: "home" };
            }
            return true;
        },
    },
    {
        path: "/register",
        name: "register",
        component: () => import("@/views/user/Register.vue"),
        meta: {
            title: "Register",
        },
        beforeEnter: (to, from) => {
            if (store.getters["auth/isAuthenticated"]) {
                return { name: "home" };
            }
            return true;
        },
    },
    // catch all at end of page
    {
        path: "/:pathMatch(.*)*",
        name: "NotFound",
        component: () => import("@/views/404.vue"),
    },
];
