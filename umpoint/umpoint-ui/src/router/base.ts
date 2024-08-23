import Layout from "@/layout/layout.vue";
import Error from "@/views/error.vue";
import { RouteRecordRaw } from "vue-router";
import Login from "@/views/login.vue";
import Iframe from "@/views/iframe.vue";

/**
 * Base route
 */
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: Layout,
    redirect: "/home",
    meta: { title: "Workbench", icon: "icon-desktop" },
    children: [
      {
        path: "/home",
        component: () => import("@/views/home.vue"),
        meta: { title: "Home", icon: "icon-home" }
      }
    ]
  },
  {
    path: "/login",
    component: Login,
    meta: { title: "Login", isNavigationMenu: false }
  },
  {
    path: "/user/password",
    component: () => import("@/views/sys/user-update-password.vue"),
    meta: { title: "Change Password", requiresAuth: true, isNavigationMenu: false }
  },
  {
    path: "/iframe/:id?",
    component: Iframe,
    meta: { title: "iframe", isNavigationMenu: false }
  },
  {
    path: "/error",
    name: "error",
    component: Error,
    meta: { title: "Error", isNavigationMenu: false }
  },
  {
    path: "/sys/:syspage(.*)",
    component: Layout,
  },
  {
    path: "/job/:syspage(.*)",
    component: Layout,
  },
  {
    path: "/space-management/space-info/:id",
    name: "space-info",
    component: () => import("@/views/space/space-info.vue"),
    meta: { title: "Space Info", requiresAuth: true, isNavigationMenu: false }
  },
  {
    path: "/space-management/space/add",
    name: "space-add",
    component: () => import("@/views/space/space-add-or-update.vue"),
    meta: { title: "Space Add", requiresAuth: true, isNavigationMenu: false }
  },
  {
    path: "/space-management/space/update/:id",
    name: "space-update",
    component: () => import("@/views/space/space-add-or-update.vue"),
    meta: { title: "Space Update", requiresAuth: true, isNavigationMenu: false }
  },
  {
    path: "/service-management/service-info/:id",
    name: "service-info",
    component: () => import("@/views/service/service-info.vue"),
    meta: { title: "Service Info", requiresAuth: true, isNavigationMenu: false }
  },
  {
    path: "/service-management/service/add",
    name: "service-add",
    component: () => import("@/views/service/service-add-or-update.vue"),
    meta: { title: "Service Add", requiresAuth: true, isNavigationMenu: false }
  },
  {
    path: "/service-management/service/update/:id",
    name: "service-update",
    component: () => import("@/views/service/service-add-or-update.vue"),
    meta: { title: "Service Update", requiresAuth: true, isNavigationMenu: false }
  },
  // must be last to prevent redirecting to error page
  {
    path: "/:path(.*)*",
    redirect: { path: "/error", query: { to: 404 }, replace: true },
    meta: { isNavigationMenu: false }
  },
];

export default routes;
