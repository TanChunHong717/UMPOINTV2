<template>
    <BaseLayout>
        <template #title>Profile</template>

        <el-card>
            <el-avatar size="large" :src="circleUrl" />
            <div>
                <el-text size="large">John Doe</el-text><br>
                <el-text>Student · U1234567</el-text><br>
                <el-text>Faculty of Computer Science and Information Technology</el-text>
            </div>
        </el-card>
        <el-form ref="formNode" label-position="top" :model="formData">
            <el-divider content-position="left">Generic Information</el-divider>
            <div class="grid">
                <el-form-item label="Name" prop="name">
                    <el-input v-model="formData.name" />
                </el-form-item>
                <el-form-item label="Email" prop="email">
                    <el-input v-model="formData.email" />
                </el-form-item>
                <el-form-item label="Mobile Phone" prop="mobilePhone">
                    <el-input v-model="formData.mobilePhone" />
                </el-form-item>
                <el-form-item label="Address" prop="address">
                    <el-input v-model="formData.address" />
                </el-form-item>
                <el-form-item label="Registration Date" prop="registrationDate">
                    <el-input
                        v-model="formData.registrationDate"
                        readonly
                        aria-readonly="true"
                    />
                </el-form-item>
                <el-form-item label="Profile Picture">
                    <el-upload
                        class="avatar-uploader"
                        action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload"
                    >
                        <img
                            v-if="formData.profilePicture"
                            :src="formData.profilePicture"
                            class="avatar"
                        />
                        <el-icon v-else class="avatar-uploader-icon">
                            <svg-icon type="mdi" :path="mdiPlus" />
                        </el-icon>
                    </el-upload>
                </el-form-item>
            </div>
        </el-form>
    </BaseLayout>
</template>

<script setup>
import { reactive, useTemplateRef, ref } from "vue";
import { ElMessage } from "element-plus";
import { mdiPlus } from "@mdi/js";

const formData = reactive({
    // generic info
    name: null,
    email: null,
    mobilePhone: null,
    address: null,
    registrationDate: null,
    profilePicture: null,

    // student/personnel info
    matricNumber: null,
    faculty: null,
});
const circleUrl = ref("https://i.pravatar.cc/300?img=0");

const formNode = useTemplateRef("formNode");

function handleAvatarSuccess(res, file) {
    formData.profilePicture = URL.createObjectURL(file.raw);
}
const beforeAvatarUpload = (rawFile) => {
    if (!["image/jpeg", "image/png"].includes(rawFile.type)) {
        ElMessage.error("Avatar picture must be JPG format!");
        return false;
    } else if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error("Avatar picture size can not exceed 2MB!");
        return false;
    }
    return true;
};
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
