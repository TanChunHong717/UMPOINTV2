import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue({
            template: {
                compilerOptions: {
                    // whitespace: "preserve",
                    isCustomElement: (tagName) => {
                        return (
                            tagName === "vue-advanced-chat" ||
                            tagName === "emoji-picker"
                        );
                    },
                },
            },
        }),
        Components({
            resolvers: [
                ElementPlusResolver({
                    importStyle: "sass",
                }),
            ],
        }),
    ],
    resolve: {
        alias: {
            "@": fileURLToPath(new URL("./src", import.meta.url)),
        },
    },
    css: {
        preprocessorOptions: {
            scss: {
                api: "modern",
                silenceDeprecations: ["color-4-api"],
                quietDeps: true,
                additionalData: `@use "@/sass/theme.scss" as *;`,
            },
        },
    },
});
