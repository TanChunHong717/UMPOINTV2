<template>
    <BaseLayout>
        <template #title>Register</template>

        <el-form
            ref="formNode"
            label-position="top"
            :model="formData"
            :rules
            @submit.prevent="submitForm"
        >
            <div class="grid">
                <el-form-item label="Username" prop="username">
                    <el-input v-model="formData.username" />
                </el-form-item>
                <el-form-item label="Mobile Number" prop="mobile">
                    <el-input type="tel" v-model="formData.mobile" />
                </el-form-item>
                <el-form-item label="Email" prop="email">
                    <el-input type="email" v-model="formData.email" />
                </el-form-item>
                <el-form-item label="Password" prop="password">
                    <el-input type="password" v-model="formData.password" />
                </el-form-item>
                <el-form-item label="Confirm Password" prop="confirmPassword">
                    <el-input
                        type="password"
                        v-model="formData.confirmPassword"
                    />
                </el-form-item>
            </div>

            <el-divider content-position="left">For Billing</el-divider>
            <el-form-item label="Real Name" prop="realName">
                <el-input v-model="formData.realName" />
            </el-form-item>
            <el-form-item label="Address" prop="address">
                <el-input type="textarea" v-model="formData.address" />
            </el-form-item>

            <el-divider content-position="left">Account Type</el-divider>
            <div class="grid">
                <el-form-item label="Type" prop="type">
                    <el-radio-group v-model="formData.type">
                        <el-radio value="Student">Student</el-radio>
                        <el-radio value="Staff">Staff</el-radio>
                        <el-radio value="Other">Other</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item
                    v-if="formData.type === 'Student'"
                    label="Matric Number"
                    prop="matricNumber"
                >
                    <el-input v-model="formData.matricNumber" />
                </el-form-item>
            </div>

            <el-form-item>
                <el-col :span="16">
                    Have an account?

                    <RouterLink to="/login">
                        <el-button text>Login here</el-button>
                    </RouterLink>
                </el-col>
                <el-col :span="8">
                    <el-button
                        type="primary"
                        style="float: inline-end"
                        @click="submitForm"
                    >
                        Register
                        <input type="submit" hidden />
                    </el-button>
                </el-col>
            </el-form-item>
        </el-form>
    </BaseLayout>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { reactive, useTemplateRef } from "vue";
import { useRouter } from "vue-router";
import { register as registerUser } from "@/helpers/api-credentials";

const router = useRouter();

const formData = reactive({
    username: null,
    password: null,
    confirmPassword: null,
    mobile: null,
    email: null,
    realName: null,
    address: null,
    type: null,
    matricNumber: null,
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
        {
            validator: (rule, value, callback) => {
                const letterRegex = /[a-zA-Z]/; // Checks for at least one letter
                const numberRegex = /\d/; // Checks for at least one digit
                const specialCharRegex = /[!@#$%^&*(),.?":{}|<>]/; // Checks for at least one special character

                if (
                    value.length < 8 ||
                    ![letterRegex, numberRegex, specialCharRegex].every(
                        (regex) => regex.test(value)
                    )
                ) {
                    callback(
                        new Error(
                            "Password must be at least 8 characters long, include at least one letter, one number, and one special character"
                        )
                    );
                } else {
                    callback(); // Validation passed
                }
            },
            trigger: "blur",
        },
    ],
    confirmPassword: [
        {
            required: true,
            message: "Please confirm your password",
            trigger: "blur",
        },
        {
            validator: (rule, value, callback) => {
                const passwordValue = formData.password;
                if (value !== passwordValue) {
                    callback(new Error("Passwords do not match"));
                } else {
                    callback(); // Validation passed
                }
            },
            trigger: "blur",
        },
    ],
    mobile: [
        {
            required: true,
            message: "Please input your mobile",
            trigger: "blur",
        },
        {
            pattern: /^(\+6)?01\d-?\d{7,8}$/,
            message: "Please input a valid Malaysian mobile number",
            trigger: "blur",
        },
    ],
    email: [
        {
            required: true,
            message: "Please input your email",
            trigger: "blur",
        },
        {
            type: "email",
            message: "Please input a valid email",
            trigger: "blur",
        },
    ],
    realName: [
        {
            required: true,
            message: "Please input your real name",
            trigger: "blur",
        },
    ],
    address: [
        {
            required: true,
            message: "Please input your address",
            trigger: "blur",
        },
    ],
    type: [
        {
            required: true,
            message: "Please choose your account type",
            trigger: "blur",
        },
    ],
    matricNumber: [
        {
            required: false,
            message: "Please input your matric number",
            trigger: "blur",
        },
        {
            validator: (rule, value, callback) => {
                const typeValue = formData.type;
                if (typeValue === "Student" && !value) {
                    callback(new Error("Matric number is required"));
                } else {
                    callback(); // Validation passed
                }
            },
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

    try {
        await registerUser(formData);
        ElMessage.success("Register success");
        router.push({ name: "login" });
    } catch (error) {
        ElMessage.error(error.message ?? "Error register");
    }
}
</script>
