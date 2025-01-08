<template>
    <vue-cal
        small
        v-loading="isCalendarLoading"
        v-model:events="bookedEvents"
        :min-date="today"
        :disable-views="['years', 'year', 'day']"
        :time-from="facilityStartTimeMinutes"
        :time-to="facilityEndTimeMinutes"
        :time-step="30"
        :editable-events="false"
        :drag-to-create-event="false"
        :min-event-width="100"
        @view-change="onViewChange"
        style="height: 600px"
    ></vue-cal>
</template>

<script setup lang="ts">
import { onMounted, ref, ShallowRef, shallowRef, computed, watch } from "vue";
import { ElMessage } from "element-plus";
import VueCal from "vue-cal";
import "vue-cal/dist/vuecal.css";
import { VueCalEvent } from "@/types/interface";
import {
    formatDateToTimezoneDateTimeStr,
    formatDateToTimezoneDateStr,
} from "@/utils/date";
import { JavaId } from "@/types/interface";
import { eventStatus, facilityTypes } from "@/constants/app";
import { getFacilityBookings } from "@/helpers/api-facility";

const props = defineProps<{
    facilityType: keyof typeof facilityTypes;
    facilityIds: JavaId;
    facilityProps: Record<string, any>;
}>();

// variables
const isCalendarLoading = ref(true);
const bookedEvents: ShallowRef<VueCalEvent[]> = shallowRef([]);

// facility Info
watch(props.facilityProps, (newVal) => {
    if (newVal && newVal.length == 0) {
        isCalendarLoading.value = true;
    }
});
function findFacility(facilityId: JavaId) {
    const index = props.facilityProps.findIndex(
        (facility: any) => facility.id == facilityId
    );
    if (index !== -1) {
        return { index, facility: props.facilityProps[index] };
    }
    return null; // or handle the case where the facility is not found
}

// calendar minutes
const facilityStartTime = ref("06:00");
const facilityEndTime = ref("22:00");
const facilityStartTimeMinutes = computed(() => {
    if (!facilityStartTime.value) return 0;
    return (
        Number(facilityStartTime.value.split(":")[0]) * 60 +
        Number(facilityStartTime.value.split(":")[1])
    );
});
``;
const facilityEndTimeMinutes = computed(() => {
    if (!facilityStartTime.value) return 60 * 24;
    return (
        Number(facilityEndTime.value.split(":")[0]) * 60 +
        Number(facilityEndTime.value.split(":")[1])
    );
});

// ensure using UTC+8 date (might show wrong time if not using),
const today = new Date(
    formatDateToTimezoneDateStr(new Date(), "Asia/Kuala_Lumpur")
);
today.setHours(0, 0, 0, 0); // reset to start of day

// Initialise calendar
const initialize = () => {
    //getHoliday(today.getFullYear());
    initializeTimeTable();
};
const initializeTimeTable = () => {
    isCalendarLoading.value = true;

    onViewChange(getMondayAndSunday(today));

    isCalendarLoading.value = false;
};
const getMondayAndSunday = (currentDate: Date) => {
    const currentDay = currentDate.getDay();
    const dayDifferenceFromMonday = currentDay === 0 ? 6 : currentDay - 1;
    const monday = new Date(currentDate);
    monday.setDate(currentDate.getDate() - dayDifferenceFromMonday);
    const sunday = new Date(monday);
    sunday.setDate(monday.getDate() + 6);

    return { startDate: monday, endDate: sunday };
};
const onViewChange = (object: any) => {
    updateEvents(object.startDate, object.endDate);
};

const updateEvents = async (startDate: Date, endDate: Date) => {
    try {
        const facilityEvents = await getFacilityBookings(
            props.facilityType as "space" | "service" | "accommodation",
            props.facilityIds,
            formatDateToTimezoneDateTimeStr(startDate),
            formatDateToTimezoneDateTimeStr(endDate)
        );
        bookedEvents.value = facilityEvents.data.data.map((event: any) => {
            let { index, facility: thisFacilityInfo } = findFacility(
                event[`${props.facilityType}Id`]
            );
            return {
                start: new Date(event.startTime),
                end: new Date(event.endTime),
                title: thisFacilityInfo.name,
                class:
                    event.type == eventStatus.CLOSURE
                        ? "closure"
                        : `booking${index}`,
                type: event.type,

                // default events that came from system are non-editable
                resizable: false,
                draggable: false,
            };
        });
    } catch (e) {
        ElMessage({
            message: e.message ?? "Failed to get facility events",
            type: "error",
        });
        throw e;
    }
};

onMounted(() => {
    initialize();
});
</script>

<style>
.close {
    background: #fff7f0
        repeating-linear-gradient(
            -45deg,
            #f29d1940,
            #f29d1940 5px,
            rgba(255, 255, 255, 0) 5px,
            rgba(255, 255, 255, 0) 15px
        );
    color: color-mix(in lab, #f29d19 80%, black 20%);
    font-weight: 600;
    .special-hours-label {
        padding-top: 1em;
    }
}
.vuecal {
    /* merge element plus design */
    font-size: var(--el-font-size-base);
    border: none;

    .vuecal__menu {
        background-color: #00000000;
    }

    .vuecal__view-btn {
        color: var(--el-table-header-text-color);
        font-size: var(--el-font-size-base);
        font-weight: 400;
        height: 40px;

        &.vuecal__view-btn--active {
            color: var(--el-color-primary);
            font-weight: 700;
        }
    }

    .vuecal__event {
        .vuecal__event-title {
            font-weight: 600;
            margin: 4px 0 8px;
        }

        .vuecal__event-time {
            font-size: 0.9em;
            opacity: 85%;

            .days-to-end {
                /* force +1d for cross-day to new line */
                display: block;
            }
        }

        &.closure {
            background-color: rgba(255, 102, 102, 0.9);
            border: 1px solid rgb(235, 82, 82);
            color: #fff;
        }
        &.selecting {
            background-color: color-mix(
                in srgb,
                white,
                var(--el-color-primary) 3%
            );
            border: 1px solid var(--el-color-primary);
            border-radius: var(--el-border-radius-base);
        }
    }

    .vuecal__cell--disabled {
        background-color: #eee;
    }
}
</style>
