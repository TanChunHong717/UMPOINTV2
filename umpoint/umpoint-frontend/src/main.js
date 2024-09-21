import "element-plus/dist/index.css";
import "./assets/bootstrap-grid.css";
import "./assets/main.css";

import { createApp } from "vue";
import App from "./App.vue";
import router from "./plugin/router.js";
import ElementPlus from "element-plus";

import svgicon from "./plugin/svgicon.js";
import components from "./layout";

const app = createApp(App);
app.use(router);
app.use(ElementPlus);
app.use(components);
app.component("SvgIcon", svgicon);

app.mount("#app");
