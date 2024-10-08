<template>
    <BaseLayout :loading="loading">
        <template #title>Facility Information</template>

        <h2>{{ facilityInfo.name }}</h2>
        <el-carousel arrow="always" trigger="click" :autoplay="false">
            <el-carousel-item v-for="item in 5" :key="item">
                <img :src="getImgUrl(item - 1)" />
            </el-carousel-item>
        </el-carousel>

        <br />

        <el-row :gutter="8">
            <el-col :xs="24" :sm="16">
                <el-card class="info" shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span class="el-descriptions__title"
                                >Facilities</span
                            >
                        </div>
                    </template>
                    <el-text>
                        <el-icon aria-label="Location">
                            <svg-icon
                                type="mdi"
                                :path="mdiMapMarkerOutline"
                            ></svg-icon>
                        </el-icon>
                        {{ facilityInfo.deptName }}
                    </el-text>
                    <el-text>
                        <el-icon aria-label="Category">
                            <svg-icon
                                type="mdi"
                                :path="mdiOfficeBuildingOutline"
                            ></svg-icon>
                        </el-icon>
                        {{ facilityInfo.category }}
                    </el-text>
                    <el-text>
                        <el-icon aria-label="Maximum capacity">
                            <svg-icon
                                type="mdi"
                                :path="mdiAccountGroup"
                            ></svg-icon>
                        </el-icon>
                        {{ facilityInfo.capacity }} persons
                    </el-text>
                    <el-text>
                        <el-icon aria-label="Facilities">
                            <svg-icon
                                type="mdi"
                                :path="mdiToolboxOutline"
                            ></svg-icon>
                        </el-icon>
                        {{ facilityInfo.facilities }}
                    </el-text>
                </el-card>
            </el-col>
            <el-col :xs="24" :sm="8">
                <el-card class="info" shadow="hover">
                    <template #header>
                        <div class="card-header card-header-action-call">
                            <span class="el-descriptions__title">Pricing</span>
                            <RouterLink :to="bookingUrl">
                                <el-button type="primary">Book now</el-button>
                            </RouterLink>
                        </div>
                    </template>
                    <el-descriptions border size="small" :column="1">
                        <el-descriptions-item
                            label="Per hour"
                            v-if="facilityInfo.hourPrice"
                        >
                            <el-text size="large" tag="b"
                                >RM
                                {{
                                    Math.round(facilityInfo.hourPrice)
                                }}</el-text
                            >
                        </el-descriptions-item>
                        <el-descriptions-item
                            label="Per 4 hour"
                            v-if="facilityInfo.fourHoursPrice"
                        >
                            <el-text size="large" tag="b"
                                >RM
                                {{
                                    Math.round(facilityInfo.fourHoursPrice)
                                }}</el-text
                            >
                        </el-descriptions-item>
                        <el-descriptions-item
                            label="Per day"
                            v-if="facilityInfo.dayPrice"
                        >
                            <el-text size="large" tag="b"
                                >RM
                                {{ Math.round(facilityInfo.dayPrice) }}</el-text
                            >
                        </el-descriptions-item>
                    </el-descriptions>
                </el-card>
            </el-col>
        </el-row>
        <el-row>
            <el-card class="info" shadow="hover">
                <template #header>
                    <div class="card-header card-header-action-call">
                        <span
                            >Person In Charge @
                            <strong>{{ facilityInfo.deptName }}</strong></span
                        >
                        <el-button type="primary" plain>
                            <template #icon>
                                <svg-icon
                                    type="mdi"
                                    :path="mdiMessageTextOutline"
                                ></svg-icon>
                            </template>
                            Chat
                        </el-button>
                    </div>
                </template>
                <el-text type="danger">
                    <el-icon aria-label="Warning">
                        <svg-icon
                            type="mdi"
                            :path="mdiExclamationThick"
                            color="red"
                        ></svg-icon>
                    </el-icon>
                    This facility requires prior contact before booking
                </el-text>
                <el-text>
                    <el-icon aria-label="Contact person">
                        <svg-icon
                            type="mdi"
                            :path="mdiCardAccountDetailsOutline"
                        ></svg-icon>
                    </el-icon>
                    {{ facilityInfo.managerName }}
                </el-text>
                <el-text>
                    <el-icon aria-label="Phone number">
                        <svg-icon type="mdi" :path="mdiPhone"></svg-icon>
                    </el-icon>
                    +6012-3456789
                </el-text>
                <el-text>
                    <el-icon aria-label="Email">
                        <svg-icon
                            type="mdi"
                            :path="mdiEmailArrowRightOutline"
                        ></svg-icon>
                    </el-icon>
                    sample@test.um.com.my
                </el-text>
            </el-card>
        </el-row>
        <el-row>
            <el-card shadow="never">
                <template #header>
                    <div class="card-header">
                        <span class="el-descriptions__title">Details</span>
                    </div>
                </template>
                <el-text
                    style="white-space: pre-wrap"
                    v-html="facilityInfo.description"
                >
                </el-text>
            </el-card>
        </el-row>
        <el-row>
            <el-card shadow="never">
                <template #header>
                    <div class="card-header">
                        <span class="el-descriptions__title">Map</span>
                    </div>
                </template>
                Map here
            </el-card>
        </el-row>
        <el-row>
            <el-card shadow="never">
                <template #header>
                    <div class="card-header">
                        <span class="el-descriptions__title">Schedule</span>
                    </div>
                </template>
                Map here
            </el-card>
        </el-row>
    </BaseLayout>
</template>

<script setup>
import {
    mdiMapMarkerOutline,
    mdiOfficeBuildingOutline,
    mdiAccountGroup,
    mdiToolboxOutline,
    mdiExclamationThick,
    mdiCardAccountDetailsOutline,
    mdiEmailArrowRightOutline,
    mdiPhone,
    mdiMessageTextOutline,
} from "@mdi/js";
import { ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getFacilityInformation } from "@/helpers/api.js";

const router = useRouter();
const route = useRoute();

const loading = ref(false);
const facilityInfo = ref({});

// watch the params of the route to fetch the data on change
// must run once
watch(() => route.params.id, fetchData, { immediate: true });

async function fetchData(id) {
    loading.value = true;

    try {
        let response = await getFacilityInformation(id);
        if (response.data.code !== 0) {
            throw new Error(response.data.message);
        }
        facilityInfo.value = response.data.data;
    } catch (err) {
        console.error(err);
        router.push({ name: "NotFound" });
    } finally {
        loading.value = false;
    }
}

// build booking form page url
const bookingUrl = `/facility/${route.params.id}/reserve`;

/** temp fetch image url */
const baseUrl =
    "https://raw.githubusercontent.com/vueComponent/ant-design-vue/main/components/carousel/demo/";
const getImgUrl = (i) => {
    return `${baseUrl}abstract0${i + 1}.jpg`;
};
</script>

<style scoped>
.el-card {
    width: 100%;
    height: calc(100% - 12px);
    margin-block-end: 8px;
}

.el-card .el-text {
    display: block;
    margin-block-start: 0;
    margin-block-end: 0.75em;
    line-height: 1.4;

    > .el-text {
        display: inline;
    }

    &:last-child {
        margin-block-end: 0;
    }
}

.card-header-action-call {
    display: flex;
    align-items: center;

    > span {
        flex: 1;
    }

    > button {
        text-align: right;
    }
}

.el-icon {
    display: inline-block;
    font-size: 1.2em;
    margin-inline-end: 0.75em;
}
</style>
