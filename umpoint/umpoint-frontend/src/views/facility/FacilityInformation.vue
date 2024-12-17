<template>
    <BaseLayout :loading="loading">
        <template #title>Facility Information</template>

        <h2>{{ facilityInfo.name }}</h2>
        <div class="tags">
            <el-tag type="info">{{ facilityInfo.category }}</el-tag>
            <el-tag type="info" v-for="tag in facilityInfo.spcTagDTOList">
                {{ tag.tagName }}
            </el-tag>
        </div>

        <el-carousel arrow="always" trigger="click" :autoplay="false">
            <el-carousel-item
                v-for="item in facilityInfo.gallery"
                :key="item.id"
            >
                <el-image
                    fit="contain"
                    :src="item.imageUrl"
                    style="width: 100%; height: 100%"
                />
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
                        {{ facilityInfo.capacity }} person(s)
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
                        <RouterLink :to="chatUrl">
                            <el-button type="primary" plain>
                                <template #icon>
                                    <svg-icon
                                        type="mdi"
                                        :path="mdiMessageTextOutline"
                                    ></svg-icon>
                                </template>
                                Chat
                            </el-button>
                        </RouterLink>
                    </div>
                </template>
                <el-text
                    type="danger"
                    v-if="facilityInfo.bookingRule?.contactRequired == 1"
                >
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
                    v-html="facilityDescription"
                >
                </el-text>
            </el-card>
        </el-row>
        <el-row>
            <el-card shadow="never" body-style="padding:0">
                <template #header>
                    <div class="card-header">
                        <span class="el-descriptions__title">Map</span>
                    </div>
                </template>
                <iframe
                    style="width: 100%"
                    height="400"
                    frameborder="0"
                    scrolling="no"
                    marginheight="0"
                    marginwidth="0"
                    id="gmap_canvas"
                    src="https://maps.google.com/maps?hl=en&amp;q=Universiti%20Malaya,%2050603%20Kuala%20Lumpur,%20Wilayah%20Persekutuan%20Kuala%20Lumpur%20Kuala%20Lumpur+(Universiti%20Malaya)&amp;t=&amp;z=15&amp;ie=UTF8&amp;iwloc=B&amp;output=embed"
                ></iframe>
                <component
                    is="script"
                    type="text/javascript"
                    src="https://embedmaps.com/google-maps-authorization/script.js?id=cbb494ed3cb0456153f19889066b28b85dcedcd5"
                ></component>
            </el-card>
        </el-row>
        <el-row>
            <el-card shadow="never" body-style="padding:0">
                <template #header>
                    <div class="card-header">
                        <span class="el-descriptions__title">Schedule</span>
                    </div>
                </template>
                <EventCalendar
                    :facility-type="route.params.type"
                    :facility-id="route.params.id"
                    :booking-rule="facilityInfo.bookingRule"
                ></EventCalendar>
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
import { ref, watch, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
    getFacilityInformation,
    transformGallery,
    transformBookingRule,
} from "@/helpers/api-facility";
import EventCalendar from "@/components/user/EventCalendar.vue";

const router = useRouter();
const route = useRoute();

const loading = ref(false);
const facilityInfo = ref({});
const facilityDescription = computed(() => {
    if (!facilityInfo.value.description) return "";
    return facilityInfo.value.description.replace(/^['"]+|['"]+$/g, "").trim();
});

// watch the params of the route to fetch the data on change
// must run once
watch(() => [route.params.type, route.params.id], fetchData, {
    immediate: true,
});

async function fetchData([facilityType, facilityId]) {
    loading.value = true;

    try {
        let response = await getFacilityInformation(facilityType, facilityId);
        facilityInfo.value = transformBookingRule(
            facilityType,
            transformGallery(facilityType, response.data.data)
        );
    } catch (err) {
        console.error(err);
        router.push({ name: "NotFound" });
    } finally {
        loading.value = false;
    }
}

// build booking form page url
const bookingUrl = computed(
    () => `/${route.params.type}/${route.params.id}/reserve`
);

// build chat url
const chatUrl = computed(
    () =>
        `/chat?facilityType=${route.params.type}&facilityId=${route.params.id}`
);
</script>

<style scoped>
h2 {
    margin-block-start: 1em;
    margin-block-end: 0.5em;
}

div.tags {
    margin-block-start: 0.5em;
    margin-block-end: 1em;

    .el-tag {
        margin-inline-end: 0.5em;
    }
}

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
