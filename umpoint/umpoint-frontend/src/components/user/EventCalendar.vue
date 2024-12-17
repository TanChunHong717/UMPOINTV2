<template>
    <vue-cal
        small
        v-loading="isCalendarLoading"
        v-model:events="bookedEvents"
        :min-date="today"
        :special-hours="specialHours"
        :disable-views="['years', 'year', 'day']"
        :time-from="facilityStartTimeMinutes"
        :time-to="facilityEndTimeMinutes"
        :time-step="props.bookingRule?.bookingUnit ?? 30"
        :editable-events="false"
        :drag-to-create-event="false"
        :min-event-width="100"
        @view-change="onViewChange"
        style="height: 600px"
    ></vue-cal>
</template>

<script setup lang="ts">
import { onMounted, ref, ShallowRef, shallowRef, computed } from "vue";
import { ElMessage } from "element-plus";
import VueCal from "vue-cal";
import "vue-cal/dist/vuecal.css";
import { VueCalEvent } from "@/types/interface";
import {
    addDays,
    formatDateToTimezoneDateTimeStr,
    formatDateToTimezoneDateStr,
} from "@/utils/date";
import baseService from "@/utils/api.js";
import { JavaId } from "@/types/interface";
import { facilityTypes } from "@/constants/app";
import { getFacilityBookings } from "@/helpers/api-facility";

const props = defineProps<{
    facilityType: keyof typeof facilityTypes;
    facilityId: JavaId;
    bookingRule: any;
}>();

// variables
const isCalendarLoading = ref(true);
const holidays = ref([]);
const specialHours = ref();
const bookedEvents: ShallowRef<VueCalEvent[]> = shallowRef([]);

// calendar minutes
const facilityStartTime = ref();
const facilityEndTime = ref();
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

// default value
const minDate = ref(null);
const maxDate = ref(null);
const weekendDays = [6, 7];

// Initialise calendar
const initialize = () => {
    //getHoliday(today.getFullYear());
    initializeTimeTable();
};
const getHoliday = (year: number) => {
    baseService
        .get(
            "https://calendarific.com/api/v2/holidays?&api_key=bjgsLdWRYGYUsxteQAwtxx7uxf9AqKOz&country=MY&year=" +
                year
        )
        .then((res: any) => {
            holidays.value = res.response.holidays;
            initializeTimeTable();
        });
};
const initializeTimeTable = () => {
    isCalendarLoading.value = true;

    // set allowed start and end date
    if (props.bookingRule?.minBookingAdvanceDay) {
        minDate.value = addDays(
            today,
            props.bookingRule?.minBookingAdvanceDay + 1 // today doesnt count
        );
    } else {
        minDate.value = today;
    }
    if (props.bookingRule?.maxBookingAdvanceDay) {
        maxDate.value = addDays(today, props.bookingRule?.maxBookingAdvanceDay);
    } else {
        maxDate.value = addDays(today, 30);
    }

    // set allowed start and end time
    const startTimeArray = props.bookingRule?.startTime.split(":") ?? [
        "07",
        "00",
    ];
    facilityStartTime.value = startTimeArray.join(":");
    const endTimeArray = props.bookingRule?.endTime.split(":") ?? ["19", "00"];
    facilityEndTime.value = endTimeArray.join(":");

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
    if (object.view != "month") {
        specialHours.value = generateWeekendAndHoliday(
            object.startDate,
            object.endDate
        );
    }

    updateEvents(object.startDate, object.endDate);
};
const generateWeekendAndHoliday = (startDate: Date, endDate: Date) => {
    const holidayObject: Record<number, any> = {};
    const isHolidayAvailable = props.bookingRule?.holidayAvailable === 1;
    const holidayClass = isHolidayAvailable ? "close" : "info";

    holidays.value.forEach((holiday: any) => {
        const holidayDate = new Date(holiday.date.iso);

        if (holidayDate >= startDate && holidayDate <= endDate) {
            const dayOfWeek = ((holidayDate.getDay() + 6) % 7) + 1;
            holidayObject[dayOfWeek] = {
                from: 0,
                to: 24 * 60,
                label: holiday.name,
                class: holidayClass,
            };
        }
    });

    if (!isHolidayAvailable) {
        weekendDays.forEach((day) => {
            if (!holidayObject[day]) {
                holidayObject[day] = {
                    from: 0,
                    to: 24 * 60,
                    label: "Weekend",
                    class: "close",
                };
            }
        });
    }

    return holidayObject;
};

const updateEvents = async (startDate: Date, endDate: Date) => {
    try {
        const facilityEvents = await getFacilityBookings(
            props.facilityType as "space" | "service" | "accommodation",
            props.facilityId,
            formatDateToTimezoneDateTimeStr(startDate),
            formatDateToTimezoneDateTimeStr(endDate)
        );
        bookedEvents.value = facilityEvents.data.data.map((event: any) => {
            return {
                start: new Date(event.startTime),
                end: new Date(event.endTime),
                title: event.title,
                class:
                    event.type == "0"
                        ? "booking"
                        : event.type == "1"
                        ? "close"
                        : "closure",
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
        return;
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

        &.booking {
            background-color: var(--el-color-info); /* Blue */
            border: 1px solid #192f59;
            color: #fff;
        }
        &.close {
            background-color: rgba(108, 117, 125, 0.9); /* Gray */
            border: 1px solid rgb(88, 97, 104);
            color: #fff;
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
