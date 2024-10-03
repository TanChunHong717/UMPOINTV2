<template>
    <BaseLayout>
        <template #title>Welcome to UMPoint very long title</template>
        <!-- <template #subtitle>UMPoint is a web application that provides information about Universiti Malaya.</template> -->

        <div id="index-wrap" class="container">
            <div id="index-content">
                <p>Welcome to UMPoint</p>
            </div>
        </div>

        <el-card v-for="facility in facilities" :key="facility.id">
            <div slot="header" class="clearfix">
                <span>{{ facility.name }}</span>
            </div>
            <div>
                <p>{{ facility.description }}</p>
            </div>
            <RouterLink :to="`/facility/${facility.id}`">Visit location</RouterLink>
        </el-card>
    </BaseLayout>
</template>

<script setup>
import { getFacilities } from "@/helpers/api";
import { ref } from "vue";

const facilities = ref([]);
getFacilities("space").then((response) => {
    if (response.status !== 200 || response.data.code !== 0) {
        console.error("Failed to get facilities");
        return;
    }
    facilities.value = response.data.data.list;
});
</script>
