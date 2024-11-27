<template>
    <el-upload
        v-model:file-list="fileList"
        :http-request="uploadFile"
        :before-remove="beforeRemove"
        multiple
        :limit="props.limit"
        :on-exceed="onExceed"
        style="width: 100%"
        :onSuccess="props.afterUpload"
    >
        <template #trigger>
            <el-button plain>
                <template #icon>
                    <svg-icon type="mdi" :path="mdiTrayArrowUp"></svg-icon>
                </template>
                Click to upload
            </el-button>
        </template>
        <template #tip>
            <div class="el-upload__tip">{{ props.tipText }}</div>
        </template>
    </el-upload>
</template>

<script setup>
import { mdiTrayArrowUp } from "@mdi/js";
import { ElMessageBox, ElMessage } from "element-plus";
import { uploadFile as uploadFileApi } from "@/helpers/api-upload";

const props = defineProps({
    limit: Number,
    tipText: String,
    afterUpload: Function,
});

const fileList = defineModel();

async function uploadFile(options) {
    try {
        return await uploadFileApi(options.file, {
            onUploadProgress: (progressEvent) => {
                if (progressEvent.lengthComputable) {
                    let percentCompleted = Math.round(
                        (progressEvent.loaded * 100) / progressEvent.total
                    );
                    options.onProgress({ percent: percentCompleted });
                }
            },
        });
    } catch (error) {
        return options.onError(error);
    }
}

const beforeRemove = (file) => {
    return ElMessageBox.confirm(`Are you sure to remove ${file.name}?`).then(
        () => true,
        () => false
    );
};

function onExceed(files) {
    ElMessage.warning(`You can only upload up to ${props.limit} files.`);
}
</script>

<style scoped>
.el-upload__tip {
    margin-top: 0;
    line-height: 1.2;
}
</style>
