<template>
    <BaseLayout>
        <template #title>Welcome to UMPoint very long title</template>
        <!-- <template #subtitle>UMPoint is a web application that provides information about Universiti Malaya.</template> -->

        <div id="index-wrap" class="container">
            <div id="index-content">
                <p>Welcome to UMPoint</p>
            </div>
        </div>

        <el-card>
            <div slot="header" class="clearfix">
                <h4>Test facility</h4>
            </div>
            <div>
                <p>Auditorium - 2</p>
            </div>
            <RouterLink :to="`/facility/2`">Visit test location</RouterLink>
        </el-card>

        <el-card v-for="facility in facilities" :key="facility.id">
            <div slot="header" class="clearfix">
                <h4>{{ facility.name }}</h4>
            </div>
            <div>
                <p>{{ facility.category }} - {{  facility.deptName }}</p>
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
