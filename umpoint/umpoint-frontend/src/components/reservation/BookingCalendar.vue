<script setup lang="ts">
import {
    ref,
    Ref,
    computed,
    shallowRef,
    ShallowRef,
    onActivated,
    onMounted,
} from "vue";
import "vue-cal/dist/vuecal.css";
import VueCal from "vue-cal";
import { VueCalEvent } from "@/types/interface";
import { ElMessage } from "element-plus";
import {
    formatDateToTimezoneDateStr,
    formatDateToTimezoneTimeStr,
    addDays,
    diffDays,
    sameDay,
    formatDateToTimezoneDateTimeStr,
} from "@/utils/date";
import baseService from "@/utils/api.js";
import { getFacilityBookings } from "@/helpers/api.js";
import { breakBookingByDays } from "@/helpers/pricing";

const isCalendarLoading = ref(true);

// form data
const formData: Ref<{
    eventName: string | null;
    numberOfParticipants: number;
    additionalTechnicians: number;
    startDate: Date | null;
    endDate: Date | null;
    startTime: string | null;
    endTime: string | null;
}> = defineModel("formData");
const emit = defineEmits(["validateForm"]);

// store curent event for calendar
// note: the return event list is separated by days, i.e. length of array is the number of available days
const currentEvent = computed<VueCalEvent[]>({
    get() {
        const isHolidayAvailable =
            facilityInfo.value.bookingRule?.holidayAvailable === 1;
        if (
            formData.value.startDate &&
            formData.value.endDate &&
            formData.value.startTime &&
            formData.value.endTime
        ) {
            return breakBookingByDays(
                formData.value.startDate,
                formData.value.endDate,
                formData.value.startTime,
                formData.value.endTime,
                isHolidayAvailable
            ).map((event) => {
                event.class = "selecting";
                return event;
            });
        } else return null;
    },
    set(event: VueCalEvent[]) {
        if (!event[0] || !event[0].start || !event[0].end) return;

        // two Date object given
        formData.value.startDate = event[0].start;
        formData.value.endDate = event[0].end;
        formData.value.startTime = formatDateToTimezoneTimeStr(
            event[0].start
        ).slice(0, -3);
        formData.value.endTime = formatDateToTimezoneTimeStr(
            event[0].end
        ).slice(0, -3);
    },
});

/* COMPUTED FOR INPUT ELEMENTS */
// prepare computed for input element
const dateInput = computed({
    get() {
        return formData.value.startDate && formData.value.endDate
            ? [formData.value.startDate, formData.value.endDate]
            : [];
    },
    set(value: Date[]) {
        let newStartDate = value[0];
        let newEndDate = value[1];

        let canSetDate = true;
        // if all time is set
        if (formData.value.startTime && formData.value.endTime) {
            // validate events overlap
            canSetDate = validateBeforeSetNewEvent(
                breakBookingByDays(
                    newStartDate,
                    newEndDate,
                    formData.value.startTime,
                    formData.value.endTime,
                    facilityInfo.value.bookingRule?.holidayAvailable === 1
                )
            );
        }
        // else just set date without validate

        if (canSetDate) {
            formData.value.startDate = newStartDate;
            formData.value.endDate = newEndDate;
            // trigger validation to clear error on input
            emit("validateForm", "bookingDate");
        }
    },
});
const setTomorrow = () => {
    dateInput.value = [addDays(today, 1), addDays(today, 1)];
};
const startTimeMaxTime = () => {
    // only limit for same day booking
    if (!formData.value.startDate || !formData.value.endDate) return "";
    if (!sameDay(formData.value.startDate, formData.value.endDate)) return "";

    return formData.value.endTime;
};
const endTimeMinTime = () => {
    // only limit for same day booking
    if (!formData.value.startDate || !formData.value.endDate) return "";
    if (!sameDay(formData.value.startDate, formData.value.endDate)) return "";

    return formData.value.startTime;
};
/* END COMPUTED FOR INPUT ELEMENTS */

// ensure using UTC+8 date (might show wrong time if not using),
const today = new Date(
    formatDateToTimezoneDateStr(new Date(), "Asia/Kuala_Lumpur")
);
today.setHours(0, 0, 0, 0); // reset to start of day

// facility information
const facilityInfo: any = defineModel("facilityInfo");
// default value
const minDate = ref(null);
const maxDate = ref(null);

// calendar setup
// fixed information from system
const holidays = ref([]);
const specialHours = ref({});
const bookedEvents: ShallowRef<VueCalEvent[]> = shallowRef([]);
const facilityStartTime = ref();
const facilityEndTime = ref();
const facilityStartTimeMinutes = computed(() => {
    if (!facilityStartTime.value) return 0;
    return (
        Number(facilityStartTime.value.split(":")[0]) * 60 +
        Number(facilityStartTime.value.split(":")[1])
    );
});
const facilityEndTimeMinutes = computed(() => {
    if (!facilityStartTime.value) return 60 * 24;
    return (
        Number(facilityEndTime.value.split(":")[0]) * 60 +
        Number(facilityEndTime.value.split(":")[1])
    );
});
const weekendDays = [6, 7];

const triggerUpdateDisplayEvents = ref(0);
const displayedEvents = computed<VueCalEvent[]>(() => {
    triggerUpdateDisplayEvents.value;
    if (!currentEvent.value) return bookedEvents.value;
    return bookedEvents.value.concat(currentEvent.value);
});

/* INITIALISE VUE-CAL */
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
    if (facilityInfo.value.bookingRule?.minBookingAdvanceDay) {
        minDate.value = addDays(
            today,
            facilityInfo.value.bookingRule?.minBookingAdvanceDay
        );
    } else {
        minDate.value = today;
    }
    if (facilityInfo.value.bookingRule?.maxBookingAdvanceDay) {
        maxDate.value = addDays(
            today,
            facilityInfo.value.bookingRule?.maxBookingAdvanceDay
        );
    } else {
        maxDate.value = addDays(today, 30);
    }

    // set allowed start and end time
    const startTimeArray = facilityInfo.value.bookingRule?.startTime.split(
        ":"
    ) ?? ["07", "00"];
    facilityStartTime.value = startTimeArray.join(":");
    const endTimeArray = facilityInfo.value.bookingRule?.endTime.split(":") ?? [
        "19",
        "00",
    ];
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
    if (object.view == "month") return;

    specialHours.value = generateWeekendAndHoliday(
        object.startDate,
        object.endDate
    );

    updateEvents(object.startDate, object.endDate);
};
const generateWeekendAndHoliday = (startDate: Date, endDate: Date) => {
    const holidayObject: Record<number, any> = {};
    const isHolidayAvailable =
        facilityInfo.value.bookingRule?.holidayAvailable === 1;
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
    const facilityEvents = await getFacilityBookings(
        facilityInfo.value.id,
        formatDateToTimezoneDateTimeStr(startDate),
        formatDateToTimezoneDateTimeStr(endDate)
    );

    if (!facilityEvents || facilityEvents.data.code !== 0) {
        ElMessage({
            message: "Failed to get facility events",
            type: "error",
        });
        return;
    }

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
};

const onCalendarDragCreate = (event: VueCalEvent) => {
    validateBeforeSetNewEvent([event]);
};
const onCalendarEventChange = ({
    event,
    originalEvent,
}: {
    event: VueCalEvent;
    originalEvent: VueCalEvent;
}) => {
    // bug: vue-cal event-drag-create originalEvent startdate is string instead of Date
    // https://github.com/antoniandre/vue-cal-v4/issues/588
    if (sameDay(event.start, new Date(originalEvent.start))) {
        // only time changed, copy date range from saved input
        event.start.setFullYear(formData.value.startDate.getFullYear());
        event.start.setMonth(formData.value.startDate.getMonth());
        event.start.setDate(formData.value.startDate.getDate());
        event.end.setFullYear(formData.value.endDate.getFullYear());
        event.end.setMonth(formData.value.endDate.getMonth());
        event.end.setDate(formData.value.endDate.getDate());
    }
    validateBeforeSetNewEvent([event]);
};
/* END INITIALISE VUE-CAL */

/** VALIDATORS **/
const notAllowedDates = (date: Date) => {
    let allow = true;

    if (date <= minDate.value) allow = false;
    if (date > maxDate.value) allow = false;
    if (!facilityInfo.value.bookingRule?.allowWeekend) {
        if (date.getDay() == 0 || date.getDay() == 6) allow = false;
    }

    return !allow;
};
const validateDate = (formData, callback: Function) => {
    if (!formData.startDate || !formData.endDate) {
        callback(new Error("Please select the date"));
        return;
    }
    if (formData.startDate > formData.endDate) {
        callback(new Error("End date must be after start date"));
        return;
    }
    if (formData.startDate < today) {
        callback(new Error("Start date must be after today"));
        return;
    }
    // check if booking length is within length limit
    let events = breakBookingByDays(
        formData.startDate,
        formData.endDate,
        formData.startTime,
        formData.endTime,
        facilityInfo.value.bookingRule?.holidayAvailable === 1
    );
    let bookingLength = events.length;
    if (
        facilityInfo.value.bookingRule?.maxReservationDays &&
        bookingLength > facilityInfo.value.bookingRule?.maxReservationDays
    ) {
        throw new Error(
            `Booking length is more than ${facilityInfo.value.bookingRule?.maxReservationDays} day(s)`
        );
    }
    // additional check if time is set
    if (formData.startTime && formData.endTime) {
        for (let event of breakBookingByDays(
            formData.startDate,
            formData.endDate,
            formData.startTime,
            formData.endTime,
            facilityInfo.value.bookingRule?.holidayAvailable === 1
        )) {
            try {
                validateEventInTimeRange(event.start, event.end);
                callback();
            } catch (e) {
                callback(e);
                return;
            }
        }
    }
};
const validateTime = (formData, callback: Function) => {
    if (!formData.startTime || !formData.endTime) {
        callback(new Error("Please select the time"));
        return;
    }
    if (formData.startTime > formData.endTime) {
        callback(new Error("End time must be after start time"));
        return;
    }
    callback();
};
/**
 * Validate if the booking time range is valid, startDate and endDate must be on the same day
 * @param startDateTime start date and time
 * @param endDateTime end date and time
 */
const validateEventInTimeRange = (startDateTime: Date, endDateTime: Date) => {
    if (!startDateTime || !endDateTime) {
        throw new Error("Please select a date range");
    }

    // check if booking on holiday
    if (!facilityInfo.value.bookingRule?.holidayAvailable) {
        for (let holiday of holidays.value) {
            if (sameDay(startDateTime, new Date(holiday.date.iso))) {
                throw new Error("Booking on holiday is not allowed");
            }
        }
    }
    // check if time reach minimum length
    if (
        facilityInfo.value.bookingRule?.minBookingHours &&
        endDateTime.getTime() - startDateTime.getTime() <
            facilityInfo.value.bookingRule?.minBookingHours * 60 * 60 * 1000
    ) {
        throw new Error(
            `Booking length is less than ${facilityInfo.value.bookingRule?.minBookingHours} hour(s)`
        );
    }
    // check if booking on weekend
    if (!facilityInfo.value.bookingRule?.holidayAvailable) {
        if (
            startDateTime.getDay() == 0 ||
            startDateTime.getDay() == 6 ||
            endDateTime.getDay() == 0 ||
            endDateTime.getDay() == 6
        ) {
            throw new Error(
                "Booking on weekend is not allowed for this facility"
            );
        }
    }
    // check if booking overlapped
    for (let bookedEvent of bookedEvents.value) {
        if (
            (startDateTime >= bookedEvent.start &&
                startDateTime < bookedEvent.end) ||
            (endDateTime > bookedEvent.start &&
                endDateTime <= bookedEvent.end) ||
            (startDateTime <= bookedEvent.start &&
                endDateTime >= bookedEvent.end)
        ) {
            throw new Error("Booking overlapped");
        }
    }
    return true;
};
function validateBeforeSetNewEvent(events: VueCalEvent[]) {
    try {
        for (let event of events) {
            validateEventInTimeRange(event.start, event.end);
        }
        // only allow one event to be booked
        currentEvent.value = events;
        return true;
    } catch (e) {
        // pop notification
        ElMessage({
            message: e.message,
            type: "error",
        });
        // trigger display event list update
        triggerUpdateDisplayEvents.value++;
        return false;
    }
}
defineExpose({
    validateDate,
    validateTime,
});
/** END VALIDATORS **/

// refresh
onMounted(() => {
    initialize();
});
onActivated(() => {
    initialize();
});
</script>

<template>
    <el-row :gutter="8">
        <el-col :sm="24" :md="12">
            <el-form-item prop="bookingDate">
                <template #label>
                    Date
                    <el-button
                        text
                        size="small"
                        type="primary"
                        @click="setTomorrow"
                        style="margin-left: 1em"
                    >
                        Use tomorrow
                    </el-button>
                </template>
                <el-date-picker
                    v-model="dateInput"
                    type="daterange"
                    placeholder="Select date"
                    range-separator="→"
                    start-placeholder="Start date"
                    end-placeholder="End date"
                    :disabled-date="notAllowedDates"
                ></el-date-picker>
            </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12">
            <el-form-item
                label="Time"
                prop="bookingTime"
                class="time-select-col"
            >
                <el-time-select
                    v-model="formData.startTime"
                    placeholder="Start time"
                    :start="facilityStartTime"
                    step="00:30"
                    :end="facilityEndTime"
                    :max-time="startTimeMaxTime()"
                    :editable="!!formData.startDate"
                ></el-time-select
                >&nbsp;&nbsp; → &nbsp;&nbsp;
                <el-time-select
                    v-model="formData.endTime"
                    placeholder="End time"
                    :start="facilityStartTime"
                    step="00:30"
                    :end="facilityEndTime"
                    :min-time="endTimeMinTime()"
                    :editable="!!formData.endDate"
                ></el-time-select>
            </el-form-item>
        </el-col>
    </el-row>

    <vue-cal
        small
        v-loading="isCalendarLoading"
        :disable-views="['years', 'year', 'day']"
        :min-date="minDate"
        :max-date="maxDate"
        :special-hours="specialHours"
        v-model:events="displayedEvents"
        :time-from="facilityStartTimeMinutes"
        :time-to="facilityEndTimeMinutes"
        :time-step="30"
        :snap-to-time="30"
        :editable-events="{
            title: false,
            drag: true,
            resize: true,
            delete: false,
            create: true,
        }"
        :drag-to-create-event="true"
        :min-event-width="100"
        @event-drag-create="onCalendarDragCreate"
        @event-drop="onCalendarEventChange"
        @event-duration-change="onCalendarEventChange"
        @view-change="onViewChange"
        style="height: 580px"
    ></vue-cal>
    <small>
        <ul>
            <li>All date and time are in Malaysian Standard Time (UTC +8).</li>
            <li>Please use the date picker for multi-day booking.</li>
        </ul>
    </small>
</template>

<style scoped>
.time-select-col > .el-form-item__content {
    flex: 1;
    display: flex;
    flex-direction: row;
    > .el-select {
        flex: 1;
    }
}
</style>

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
    border-radius: var(--el-border-radius-base);
    box-shadow: 0 0 0 1px var(--el-border-color) inset;

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
