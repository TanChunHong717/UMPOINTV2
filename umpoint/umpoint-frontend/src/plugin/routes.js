export default [
    {
        path: "/",
        name: "home",
        component: () => import("@/views/MainPage.vue"),
    },
    {
        path: "/facility",
        name: "facility",
        meta: {
            title: "Facility",
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
            },
            {
                path: ":id/reserve",
                name: "facility-reserve",
                component: () =>
                    import("@/views/facility/FacilityReservationForm.vue"),
                meta: {
                    title: "Reservation",
                },
            },
        ],
    },
    {
        path: "/profile",
        name: "profile",
        component: () => import("@/views/user/ProfileSettings.vue"),
        meta: {
            title: "Profile",
        },
    },
    {
        path: "/bookings",
        name: "bookings",
        component: () => import("@/views/user/BookingHistory.vue"),
        meta: {
            title: "Bookings",
        },
    },
    {
        path: "/chat",
        name: "chat",
        component: () => import("@/views/user/ChatHistory.vue"),
        meta: {
            title: "Chat History",
        },
    },
    {
        path: "/test2",
        name: "test",
        component: () => import("@/views/test.vue"),
        meta: {
            title: "Chat History",
        },
    },

    // catch all at end of page
    {
        path: "/:pathMatch(.*)*",
        name: "NotFound",
        component: () => import("@/views/404.vue"),
    },
];
