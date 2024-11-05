<template>
    <el-result title="404" sub-title="Sorry, seems like the place is missing.">
        <template #icon>
            <el-image
                src="/images/404.png"
            />
        </template>
        <template #extra>
            <el-button type="primary" @click="back">Back</el-button>
        </template>
    </el-result>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();
function back() {
    router.go(-1);
}
</script>

<style>
.el-image {
    max-width: 500px; width: 100%;
}
.el-result__title p{
    font-size: 4em;
    font-weight: 900;
}
</style>