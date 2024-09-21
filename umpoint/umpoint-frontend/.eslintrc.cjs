/* eslint-env node */
module.exports = {
    root: true,
    extends: ["plugin:vue/vue3-essential", "eslint:recommended"],
    parserOptions: {
        ecmaVersion: "latest",
    },
    "parser": "@babel/eslint-parser",
    rules: {
        "no-unused-vars": "warn",
        "@typescript-eslint/no-unused-vars": ["warn"],
        'vue/multi-word-component-names': 'off',
    },
};
