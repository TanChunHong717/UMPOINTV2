<template>
    <BaseLayout>
        <template #title>Schedule Comparison</template>

        <h2>Comparing facility schedules for</h2>
        <div class="grid">
            <router-link
                class="no-underline"
                v-for="(facility, index) in facilityInfos"
                :key="facility.id"
                :to="`/${facilityType}/${facility.id}`"
            >
                <el-card :body-class="['info', `booking${index}`]">
                    <h4>{{ facility.name }}</h4>
                    <p>{{ facility.category }} - {{ facility.deptName }}</p>
                </el-card>
            </router-link>
        </div>

        <EventComparisonCalendar
            :facilityType="facilityType"
            :facilityIds="facilityIds"
            :facilityProps="facilityInfos"
        />
    </BaseLayout>
</template>

<script setup>
import { computed, reactive, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { getFacilityInformation } from "@/helpers/api-facility";
import EventComparisonCalendar from "@/components/user/EventComparisonCalendar.vue";

const route = useRoute();

const props = defineProps(["type"]);
const facilityType = ref(props.type);
const facilityIds = computed(() => {
    if (!route.query.ids) return [];
    return route.query.ids.split(",");
});
const facilityInfos = ref([]);
watch(
    facilityIds,
    async (ids) => {
        if (ids.length === 0) {
            console.error("No facility ids provided");
        }
        try {
            facilityInfos.value = await Promise.all(
                facilityIds.value.map((id) =>
                    getFacilityInformation(facilityType.value, id)
                )
            ).then((res) => res.map((r) => r.data.data));
            console.log("Facility information fetched", facilityInfos.value);
        } catch (e) {
            console.error("Error fetching facilities information", e);
        }
    },
    { immediate: true }
);
</script>

<style>
.no-underline {
    text-decoration: none;
    color: inherit;
}
.el-card {
    margin-bottom: 1em;
    .info {
        padding: 1em;
        h4 {
            margin-block: 0.5em;
        }
        p {
            margin-block-start: 1em;
            margin-block-end: 0.5em;
        }
    }
}

.booking0 {
    background-color: var(--el-color-info-light-7); /* Blue */
    border: 1px solid #5363a2;
    color: #000;
}
.booking1 {
    background-color: #f8ba77;
    border: 1px solid #e97e0c;
    color: #000;
}
.booking2 {
    background-color: #5fb0b7;
    border: 1px solid #3f868d;
    color: #000;
}
.booking3 {
    background-color: #c7cb85;
    border: 1px solid #999d43;
    color: #000;
}
.booking4 {
    background-color: #ffc2e2;
    border: 1px solid #f5007e;
    color: #000;
}
.booking5 {
    background-color: #c8ffbe;
    border: 1px solid #52ff33;
    color: #000;
}
</style>
