<script setup lang="ts">
import { VueCalEvent } from "@/types/interface";
import {
    ref,
    onActivated,
    onMounted,
    shallowRef,
    computed,
    ShallowRef,
} from "vue";
import "vue-cal/dist/vuecal.css";
import VueCal from "vue-cal";
import {
    formatDateToTimezoneDateStr,
    addDays,
    formatDateToTimezoneTimeStr,
} from "@/utils/date.js";
import baseService from "@/utils/api.js";

// form data
const formData: any = defineModel("formData");
const emit = defineEmits(["validateForm"]);
// to store current event
const currentEvent = computed<VueCalEvent>({
    get() {
        if (
            !(
                formData.value.date &&
                formData.value.startTime &&
                formData.value.endTime
            )
        )
            return null;

        // reconstruct date object from form
        let startDateObj = new Date(formData.value.date[0]);
        let endDateObj = new Date(formData.value.date[1]);
        startDateObj.setHours(
            Number(formData.value.startTime.split(":")[0]),
            Number(formData.value.startTime.split(":")[1]),
            0
        );
        endDateObj.setHours(
            Number(formData.value.endTime.split(":")[0]),
            Number(formData.value.endTime.split(":")[1]),
            0
        );

        return {
            start: startDateObj,
            end: endDateObj,
            class: "selecting",
        };
    },
    set(eventDTO) {
        // save data to formData
        // set date without time
        let startDate = formatDateToTimezoneDateStr(eventDTO.start);
        let endDate = formatDateToTimezoneDateStr(eventDTO.end);
        formData.value.date = [startDate, endDate];

        // set time without seconds
        let startTime = formatDateToTimezoneTimeStr(eventDTO.start).slice(
            0,
            -3
        );
        let endTime = formatDateToTimezoneTimeStr(eventDTO.end).slice(0, -3);
        formData.value.startTime = startTime;
        formData.value.endTime = endTime;
    },
});

// ensure using UTC+8 date (might show wrong time if not using),
const today = new Date(
    formatDateToTimezoneDateStr(new Date(), "Asia/Kuala_Lumpur")
);
today.setHours(0, 0, 0, 0); // reset to start of day

// facility information
const space: any = defineModel("facilityInfo");
const minDate = defineModel("minDate");
const maxDate = defineModel("maxDate");

// calendar setup
// fixed information from system
const holidays = ref([]);
const specialHours = ref({});
const bookedEvents: ShallowRef<VueCalEvent[]> = shallowRef([]);
const startTime = ref();
const endTime = ref();
const weekendDays = [6, 7];

const displayedEvents = computed<VueCalEvent[]>(() => {
    if (!currentEvent.value) return bookedEvents.value;
    return bookedEvents.value.concat([currentEvent.value]);
});

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
    const startTimeArray = space.value.spcBookingRuleDTO.startTime.split(
        ":"
    ) ?? ["08", "00"];
    startTime.value =
        Number(startTimeArray[0]) * 60 + Number(startTimeArray[1]);
    const endTimeArray = space.value.spcBookingRuleDTO.endTime.split(":") ?? [
        "18",
        "00",
    ];
    endTime.value = Number(endTimeArray[0]) * 60 + Number(endTimeArray[1]);

    onViewChange(getMondayAndSunday(today));
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
    const holidayClass =
        space.value.spcBookingRuleDTO.holidayAvailable == 1 ? "close" : "info";

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

    if (space.value.spcBookingRuleDTO.holidayAvailable == 1) {
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
    baseService
        .get("/space/event", {
            params: {
                spaceId: space.value.id,
                startTime: formatDateToTimezoneTimeStr(
                    startDate,
                    "Asia/Kuala_Lumpur"
                ),
                endTime: formatDateToTimezoneTimeStr(
                    endDate,
                    "Asia/Kuala_Lumpur"
                ),
            },
        })
        .then((res) => {
            // if (!res.data || typeof res.data !== "object") return;

            // TODO: Remove fake data
            res.data = [
                {
                    startTime: new Date(2024, 8, 23, 9, 0),
                    endTime: new Date(2024, 8, 25, 9, 0),
                    title: "Upcoming event",
                    type: 0,
                    bookingId: 1,
                    closureId: 2,
                },
            ];

            bookedEvents.value = res.data.map((eventDTO: any) => {
                return {
                    start: new Date(eventDTO.startTime),
                    end: new Date(eventDTO.endTime),
                    title: eventDTO.title,
                    class:
                        eventDTO.type == "0"
                            ? "booking"
                            : eventDTO.type == "1"
                            ? "close"
                            : "closure",
                    type: eventDTO.type,

                    // default events that came from system are non-editable
                    resizable: false,
                    draggable: false,
                };
            });
        });
};

const onEventDragCreate = (event: VueCalEvent) => {
    // only allow one event to be booked
    currentEvent.value = event;
};
const onEventChange = ({
    event,
}: {
    event: VueCalEvent;
    originalEvent: VueCalEvent;
}) => {
    // only allow one event to be booked
    currentEvent.value = event;
};

const notAllowedDates = (date: Date) => {
    return date <= minDate.value || date > maxDate.value;
};

const setTomorrow = () => {
    formData.value.date = [addDays(today, 1), addDays(today, 1)];
    // trigger validation to clear error on this input
    emit("validateForm", "bookingDate");
};

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
                    v-model="formData.date"
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
                    start="08:30"
                    step="00:30"
                    end="18:30"
                    :max-time="
                        formData.endTime &&
                        formData.date[0] === formData.date[1]
                            ? formData.endTime
                            : ''
                    "
                ></el-time-select
                >&nbsp;&nbsp; → &nbsp;&nbsp;
                <el-time-select
                    v-model="formData.endTime"
                    placeholder="End time"
                    start="08:30"
                    step="00:30"
                    end="18:30"
                    :min-time="
                        formData.startTime &&
                        formData.date[0] === formData.date[1]
                            ? formData.startTime
                            : ''
                    "
                ></el-time-select>
            </el-form-item>
        </el-col>
    </el-row>

    <vue-cal
        small
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
        @event-drag-create="onEventDragCreate"
        @event-drop="onEventChange"
        @event-duration-change="onEventChange"
        @view-change="onViewChange"
        style="height: 580px"
    ></vue-cal>
    <small><em>* Please use the date picker for multi-day booking.</em></small>
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

    .vuecal__view-btn {
        font-size: var(--el-font-size-base);
        font-weight: 500;
        height: 40px;

        &.vuecal__view-btn--active {
            color: var(--el-color-primary);
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
            background-color: #192f59; /* Blue */
            border: 1px solid var(--el-color-info);
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
