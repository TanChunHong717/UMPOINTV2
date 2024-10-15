<script setup lang="ts">
import { VueCalEvent } from "@/types/interface";
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
import { ElMessage } from "element-plus";
import {
    formatDateToTimezoneDateStr,
    formatDateToTimezoneTimeStr,
    addDays,
    diffDays,
} from "@/utils/date";
import baseService from "@/utils/api.js";
import { getFacilityBookings } from "@/helpers/api.js";

const isCalendarLoading = ref(true);

// form data
const formData: Ref<{
    eventName: string | null;
    numberOfParticipants: number;
    additionalTechnicians: number;
    startDate: Date | null;
    endDate: Date | null;
    isTimeSet: [boolean, boolean];
}> = defineModel("formData");
const emit = defineEmits(["validateForm"]);
// store curent event for calendar
const currentEvent = computed<VueCalEvent>({
    get() {
        if (
            formData.value.isTimeSet.every((bool) => bool) &&
            formData.value.startDate &&
            formData.value.endDate
        )
            return {
                start: formData.value.startDate,
                end: formData.value.endDate,
                class: "selecting",
            };
        else return null;
    },
    set(eventDTO) {
        if (!eventDTO || !eventDTO.start || !eventDTO.end) return;

        formData.value.isTimeSet = [true, true];
        // two Date object given
        formData.value.startDate = eventDTO.start;
        formData.value.endDate = eventDTO.end;
    },
});

/* COMPUTED FOR INPUT ELEMENTS */
// prepare computed for input element
function sameDay(d1: Date, d2: Date) {
    return (
        d1.getFullYear() === d2.getFullYear() &&
        d1.getMonth() === d2.getMonth() &&
        d1.getDate() === d2.getDate()
    );
}
const dateInput = computed({
    get() {
        return formData.value.startDate && formData.value.endDate
            ? [formData.value.startDate, formData.value.endDate]
            : [];
    },
    set(value: Date[]) {
        let originalStartDate = formData.value.startDate;
        let originalEndDate = formData.value.endDate;
        let originalTimeSet = formData.value.isTimeSet;

        let newStartDate = value[0];
        let newEndDate = value[1];

        // preserve time if already set
        if (formData.value.startDate && originalTimeSet[0]) {
            newStartDate.setHours(
                originalStartDate.getHours(),
                originalStartDate.getMinutes()
            );
        }
        if (formData.value.endDate && originalTimeSet[1]) {
            newEndDate.setHours(
                originalEndDate.getHours(),
                originalEndDate.getMinutes()
            );
        }

        let canSetDate = true;
        // if all time is set
        if (originalTimeSet.every((bool) => bool)) {
            // validate event overlap
            canSetDate = validateBeforeSetNewEvent({
                start: newStartDate,
                end: newEndDate,
            });
            // maintain the original is time set (validateBeforeSetNewEvent will change it)
            formData.value.isTimeSet = originalTimeSet;
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
const startTimeInput = computed({
    get() {
        return formData.value.startDate && formData.value.isTimeSet[0]
            ? formatDateToTimezoneTimeStr(formData.value.startDate).slice(0, -3)
            : null;
    },
    set(value: string) {
        let time = value.split(":");
        try {
            formData.value.startDate.setHours(
                Number(time[0]),
                Number(time[1]),
                0
            );
            formData.value.isTimeSet[0] = true;
        } catch (e) {
            ElMessage({
                message: "Please select start date first",
                type: "error",
            });
        }
    },
});
const startTimeMaxTime = () => {
    // only limit for same day booking
    if (!formData.value.startDate || !formData.value.endDate) return "";
    if (!sameDay(formData.value.startDate, formData.value.endDate)) return "";
    // only give max time if end time is chosen
    if (!formData.value.isTimeSet[1]) return "";

    return formatDateToTimezoneTimeStr(formData.value.endDate).slice(0, -3);
};
const endTimeInput = computed({
    get() {
        return formData.value.endDate && formData.value.isTimeSet[1]
            ? formatDateToTimezoneTimeStr(formData.value.endDate).slice(0, -3)
            : null;
    },
    set(value: string) {
        let time = value.split(":");
        try {
            formData.value.endDate.setHours(Number(time[0]), Number(time[1]));
            formData.value.isTimeSet[1] = true;
        } catch (e) {
            ElMessage({
                message: "Please select end date first",
                type: "error",
            });
        }
    },
});
const endTimeMinTime = () => {
    // only limit for same day booking
    if (!formData.value.startDate || !formData.value.endDate) return "";
    if (!sameDay(formData.value.startDate, formData.value.endDate)) return "";
    // only give min time if start time is chosen
    if (!formData.value.isTimeSet[0]) return "";

    return formatDateToTimezoneTimeStr(formData.value.startDate).slice(0, -3);
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
const startTime = ref();
const endTime = ref();
const weekendDays = [6, 7];

const triggerUpdateDisplayEvents = ref(0);
const displayedEvents = computed<VueCalEvent[]>(() => {
    triggerUpdateDisplayEvents.value;
    if (!currentEvent.value) return bookedEvents.value;
    return bookedEvents.value.concat([currentEvent.value]);
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
    if (facilityInfo.value.bookingRule?.closeDaysAfterBooking) {
        minDate.value = addDays(
            today,
            facilityInfo.value.bookingRule?.closeDaysAfterBooking
        );
    } else {
        minDate.value = today;
    }
    if (facilityInfo.value.bookingRule?.openDaysPriorBooking) {
        maxDate.value = addDays(
            today,
            facilityInfo.value.bookingRule?.openDaysPriorBooking
        );
    } else {
        maxDate.value = addDays(today, 30);
    }

    // set allowed start and end time
    const startTimeArray = facilityInfo.value.bookingRule?.startTime.split(
        ":"
    ) ?? ["07", "00"];
    startTime.value =
        Number(startTimeArray[0]) * 60 + Number(startTimeArray[1]);
    const endTimeArray = facilityInfo.value.bookingRule?.endTime.split(":") ?? [
        "19",
        "00",
    ];
    endTime.value = Number(endTimeArray[0]) * 60 + Number(endTimeArray[1]);

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

    if (isHolidayAvailable) {
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
        startDate,
        endDate
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
    validateBeforeSetNewEvent(event);
};
const onCalendarEventChange = ({
    event,
}: {
    event: VueCalEvent;
    originalEvent: VueCalEvent;
}) => {
    validateBeforeSetNewEvent(event);
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
    if (formData.isTimeSet.every((bool) => bool)) {
        try {
            validateBookingTimeRange(formData.startDate, formData.endDate);
            callback();
        } catch (e) {
            callback(e);
            return;
        }
    }
};
const validateTime = (formData, callback: Function) => {
    if (!formData.isTimeSet.every((bool: boolean) => bool)) {
        callback(new Error("Please select time range"));
        return;
    }
    if (
        sameDay(formData.startDate, formData.endDate) &&
        formData.startDate > formData.endDate
    ) {
        callback(new Error("End time must be after start time"));
        return;
    }
    callback();
};
const validateBookingTimeRange = (startDate: Date, endDate: Date) => {
    if (!startDate) startDate = formData.value.startDate;
    if (!endDate) endDate = formData.value.endDate;

    if (!startDate || !endDate) {
        throw new Error("Please select a date range");
    }

    // check if booking on holiday
    if (!facilityInfo.value.bookingRule?.holidayAvailable) {
        for (let holiday of holidays.value) {
            if (
                startDate <= new Date(holiday.date.iso) &&
                endDate >= new Date(holiday.date.iso)
            ) {
                throw new Error("Booking on holiday is not allowed");
            }
        }
    }
    // check if booking length is within length limit
    if (
        facilityInfo.value.bookingRule?.maxReservationDays &&
        diffDays(startDate, endDate) >
            facilityInfo.value.bookingRule?.maxReservationDays
    ) {
        throw new Error(`Booking length is more than ${facilityInfo.value.bookingRule?.maxReservationDays} day(s)`);
    }
    if (
        facilityInfo.value.bookingRule?.minBookingHours &&
        endDate.getTime() - startDate.getTime() <
            facilityInfo.value.bookingRule?.minBookingHours * 60 * 60 * 1000
    ) {
        throw new Error(`Booking length is less than ${facilityInfo.value.bookingRule?.minBookingHours} hour(s)`);
    }
    // check if booking on weekend
    if (!facilityInfo.value.bookingRule?.allowWeekend) {
        if (
            startDate.getDay() == 0 ||
            startDate.getDay() == 6 ||
            endDate.getDay() == 0 ||
            endDate.getDay() == 6
        ) {
            throw new Error("Booking on weekend is not allowed for this facility");
        }
    }
    // check if booking overlapped
    for (let bookedEvent of bookedEvents.value) {
        if (
            (startDate >= bookedEvent.start && startDate < bookedEvent.end) ||
            (endDate > bookedEvent.start && endDate <= bookedEvent.end) ||
            (startDate <= bookedEvent.start && endDate >= bookedEvent.end)
        ) {
            throw new Error("Booking overlapped");
        }
    }
    return true;
};
function validateBeforeSetNewEvent(event: VueCalEvent) {
    try {
        validateBookingTimeRange(event.start, event.end);
        // only allow one event to be booked
        currentEvent.value = event;
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
                    v-model="startTimeInput"
                    placeholder="Start time"
                    start="08:00"
                    step="00:30"
                    end="18:00"
                    :max-time="startTimeMaxTime()"
                    :editable="!!formData.startDate"
                ></el-time-select
                >&nbsp;&nbsp; → &nbsp;&nbsp;
                <el-time-select
                    v-model="endTimeInput"
                    placeholder="End time"
                    start="08:00"
                    step="00:30"
                    end="18:00"
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
        :time-from="startTime"
        :time-to="endTime"
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
