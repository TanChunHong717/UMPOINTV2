<template>
    <BaseLayout>
        <template #title>Profile</template>

        <el-card>
            <!-- <el-avatar size="large" :src="circleUrl" /> -->
            <div>
                <el-text style="font-weight: bold; font-size: 1.5em">{{
                    formData.name
                }}</el-text
                ><br />
                <el-text>{{ formData.type }}</el-text
                ><br />
                <el-text>{{ formData.email }}</el-text
                ><br />
                <!-- <el-text
                    >Faculty of Computer Science and Information
                    Technology</el-text
                > -->
            </div>
        </el-card>
        <el-form ref="formNode" label-position="top" :model="formData" :rules>
            <el-divider content-position="left">User Information</el-divider>
            <div class="grid">
                <el-form-item label="Mobile Phone" prop="mobile">
                    <el-input v-model="formData.mobile" />
                </el-form-item>
                <el-form-item label="Address" prop="address">
                    <el-input v-model="formData.address" />
                </el-form-item>
                <el-form-item label="New Password" prop="newPassword">
                    <el-input type="password" v-model="formData.newPassword" />
                </el-form-item>
                <el-form-item
                    label="Confirm New Password"
                    prop="newPasswordConfirm"
                >
                    <el-input
                        type="password"
                        v-model="formData.newPasswordConfirm"
                    />
                </el-form-item>
            </div>

            <el-divider content-position="left">Notification</el-divider>
            <el-form-item
                label="Receive email"
                label-position="left"
                prop="receiveEmail"
            >
                <el-switch
                    v-model="formData.receiveEmail"
                    :active-value="1"
                    :inactive-value="0"
                />
            </el-form-item>

            <el-divider content-position="left">Security</el-divider>
            <el-form-item
                label="Current Password"
                label-position="left"
                prop="password"
            >
                <el-input type="password" v-model="formData.password" />
            </el-form-item>

            <el-button
                type="primary"
                style="float: inline-end"
                @click="submitForm"
            >
                Save
            </el-button>
        </el-form>
    </BaseLayout>
</template>

<script setup>
import { reactive, useTemplateRef, ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { mdiPlus } from "@mdi/js";
import {
    getUserInformation,
    saveUserInformation,
} from "@/helpers/api-credentials";

const formData = reactive({
    // generic info
    id: null,
    name: null,
    type: null,
    email: null,
    mobile: null,
    address: null,
    password: null,
    newPassword: null,
    newPasswordConfirm: null,
    receiveEmail: 1,

    // student/personnel info
    matricNumber: null,
    faculty: null,
});

const formNode = useTemplateRef("formNode");
onMounted(async () => {
    let res = await getUserInformation();
    let userData = res.data.data;
    formData.id = userData.id;
    formData.name = userData.username;
    formData.email = userData.email;
    formData.mobile = userData.mobile;
    formData.address = userData.address;
    formData.type = userData.type[0].toUpperCase() + userData.type.slice(1);
    formData.receiveEmail = userData.receiveEmail;
    console.log(formData);
});

// // avatar
// function handleAvatarSuccess(res, file) {
//     formData.profilePicture = URL.createObjectURL(file.raw);
// }
// const beforeAvatarUpload = (rawFile) => {
//     if (!["image/jpeg", "image/png"].includes(rawFile.type)) {
//         ElMessage.error("Avatar picture must be JPG format!");
//         return false;
//     } else if (rawFile.size / 1024 / 1024 > 2) {
//         ElMessage.error("Avatar picture size can not exceed 2MB!");
//         return false;
//     }
//     return true;
// };

// submit
const rules = reactive({
    mobile: [
        {
            required: true,
            message: "Please input your mobile phone",
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
    password: [
        {
            required: true,
            message: "Please input your password",
            trigger: "blur",
        },
    ],
    newPasswordConfirm: [
        {
            validator: (rule, value, callback) => {
                if (value !== formData.newPassword) {
                    callback(new Error("The new passwords do not match"));
                } else {
                    callback();
                }
            },
        },
    ],
});
function submitForm() {
    console.log(formNode);
    formNode.value.validate((valid) => {
        if (valid) {
            saveFormData();
        }
    });
}
async function saveFormData() {
    let submitData = {
        password: formData.password,
        mobile: formData.mobile,
        address: formData.address,
        receiveEmail: formData.receiveEmail,
    };
    if (formData.newPassword) {
        submitData.newPassword = formData.newPassword;
    }
    try {
        await saveUserInformation(formData.id, submitData);
        ElMessage.success("Profile updated successfully");
        formData.password = ""; // clear password field
    } catch (e) {
        ElMessage.error(e.message);
    }
}
</script>

<style>
div.grid {
    grid-template-columns: repeat(auto-fill, minmax(max(49%, 250px), 1fr));
}

.avatar-uploader {
    .el-upload {
        border: 1px dashed var(--el-border-color);
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: var(--el-transition-duration-fast);

        &:hover {
            border-color: var(--el-color-primary);
        }
    }
    .el-icon.avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 3em;
        height: 3em;
        text-align: center;
    }
}
</style>
