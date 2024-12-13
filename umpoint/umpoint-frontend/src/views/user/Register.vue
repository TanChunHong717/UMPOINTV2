<template>
    <BaseLayout>
        <template #title>Register</template>

        <el-form ref="formNode" label-position="top" :model="formData" :rules>
            <el-form-item label="Username" prop="username">
                <el-input v-model="formData.username" />
            </el-form-item>
            <el-form-item label="Password" prop="password">
                <el-input type="password" v-model="formData.password" />
            </el-form-item>
            <el-form-item label="Confirm Password" prop="confirmPassword">
              <el-input type="password" v-model="formData.confirmPassword" />
            </el-form-item>
            <el-form-item label="Mobile Number" prop="mobile">
              <el-input type="tel" v-model="formData.mobile" />
            </el-form-item>
            <el-form-item label="Email" prop="email">
              <el-input type="email" v-model="formData.email" />
            </el-form-item>
            <el-divider content-position="left">For Billing</el-divider>
            <el-form-item label="Real Name" prop="realName">
              <el-input v-model="formData.realName" />
            </el-form-item>
            <el-form-item label="Address" prop="address">
              <el-input type="textarea" v-model="formData.address" />
            </el-form-item>
            <el-divider content-position="left">Account Type</el-divider>
            <el-form-item label="Type" prop="type">
              <el-radio-group v-model="formData.type">
                <el-radio value="Student">Student</el-radio>
                <el-radio value="Staff">Staff</el-radio>
                <el-radio value="Other">Other</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item v-if="formData.type === 'Student'" label="Matric Number" prop="matricNumber">
              <el-input v-model="formData.matricNumber" />
            </el-form-item>
            <el-form-item>
                <el-col :span="8" :offset="16">
                    <el-button
                        type="primary"
                        style="float: inline-end"
                        @click="submitForm"
                    >
                        Register
                    </el-button>
                </el-col>
            </el-form-item>
        </el-form>

        <RouterLink to="/login">
            <el-button text>Login</el-button>
        </RouterLink>
    </BaseLayout>
</template>

<script setup>
import {ElMessage} from "element-plus";
import {reactive, useTemplateRef} from "vue";
import {useRouter} from "vue-router";
import api from "@/utils/api.ts";

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
    matricNumber: null
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
          const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
          if (!passwordRegex.test(value)) {
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
          const passwordValue = form.password; // Replace 'form' with your actual reactive form data object
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
    ],
    email: [
      {
        required: true,
        message: "Please input your email",
        trigger: "blur",
      },
      {
        validator: (rule, value, callback) => {
          const emailRegex =
              /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
          if (!emailRegex.test(value)) {
            callback(new Error("Please input a valid email address"));
          } else {
            callback(); // Validation passed
          }
        },
        trigger: ["blur", "change"], // Optional: Validate on both blur and change
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
      }
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
          if (typeValue !== "Student") {
            callback();
          } else if (!value) {
            callback(new Error("Matric number is required"));
          }
        },
        trigger: "blur",
      },
    ]
});

async function submitForm() {
    // validate form
    try {
        await formNode.value.validate();
    } catch (error) {
        return;
    }

    // Register
    api.post("/client/user/register").then(res => {
      if (res.status === 200)
          router.push({name: "login"});
      ElMessage.error( "Error register");
    })
}
</script>
