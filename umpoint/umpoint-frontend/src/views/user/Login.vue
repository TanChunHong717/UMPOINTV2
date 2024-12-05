<template>
    <BaseLayout>
        <template #title>Login</template>

        <el-form ref="formNode" label-position="top" :model="formData" :rules>
            <el-form-item label="Username" prop="username">
                <el-input v-model="formData.username" />
            </el-form-item>
            <el-form-item label="Password" prop="password">
                <el-input type="password" v-model="formData.password" />
            </el-form-item>
            <el-form-item>
                <el-col :span="16">
                    <el-switch
                        v-model="formData.rememberMe"
                        size="large"
                        active-text="Remember Me"
                    />
                </el-col>
                <el-col :span="8">
                    <el-button
                        type="primary"
                        style="float: inline-end"
                        @click="submitForm"
                    >
                        Login
                    </el-button>
                </el-col>
            </el-form-item>
        </el-form>

        <RouterLink to="/register">
            <el-button text>Register Account</el-button>
        </RouterLink>
    </BaseLayout>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { nextTick, reactive, useTemplateRef } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";

const store = useStore();
const router = useRouter();
const route = useRoute();

const formData = reactive({
    username: null,
    password: null,
    rememberMe: false,
});

const formNode = useTemplateRef("formNode");

// submit
const rules = reactive({
    username: [
        {
            required: true,
            message: "Please input your username",
            trigger: "blur",
        },
    ],
    password: [
        {
            required: true,
            message: "Please input your password",
            trigger: "blur",
        },
    ],
});
async function submitForm() {
    // validate form
    try {
        await formNode.value.validate();
    } catch (error) {
        return;
    }

    // login
    try {
        await store.dispatch("auth/login", formData);

        if (route.query.to) {
            router.push(decodeURIComponent(route.query.to));
        } else {
            router.push({ name: "home" });
        }
    } catch (error) {
        ElMessage.error(
            error.response.data?.msg ?? error.response.msg ?? "Error logging in"
        );
        formNode.value.fields.forEach((element) => {
            console.log(element);
            element.setCustomValidity = "error";
        });
    }
}
</script>
