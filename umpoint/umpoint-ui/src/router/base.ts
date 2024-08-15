import Layout from "@/layout/layout.vue";
import Error from "@/views/error.vue";
import { RouteRecordRaw } from "vue-router";
import Login from "@/views/login.vue";
import Iframe from "@/views/iframe.vue";

/**
 * 框架基础路由
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
    path: "/:path(.*)*",
    redirect: { path: "/error", query: { to: 404 }, replace: true },
    meta: { isNavigationMenu: false }
  },
  {
    path: "/space-management/space-info/:id",
    name: "space-info",
    component: () => import("@/views/space-management/space-info.vue"),
    meta: { title: "Space Info", requiresAuth: true, isNavigationMenu: false }
  },
  {
    path: "/space-management/space/add",
    name: "space-add",
    component: () => import("@/views/space-management/space-add-or-update.vue"),
    meta: { title: "Space Add", requiresAuth: true, isNavigationMenu: false }
  },
  {
    path: "/space-management/space/update/:id",
    name: "space-update",
    component: () => import("@/views/space-management/space-add-or-update.vue"),
    meta: { title: "Space Update", requiresAuth: true, isNavigationMenu: false }
  }
];

export default routes;
