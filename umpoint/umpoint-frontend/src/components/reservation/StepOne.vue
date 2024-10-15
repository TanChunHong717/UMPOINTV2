<script setup lang="ts">
import { ref, reactive, useTemplateRef, h, Reactive } from "vue";
import BookingCalendar from "./BookingCalendar.vue";
import { addDays } from "@/utils/date.js";

const emit = defineEmits(["nextStep"]);
const props = defineProps(["facilityInfo"]);
defineOptions({
    inheritAttrs: false,
});

const formData = reactive({
    eventName: null,
    numberOfParticipants: 1,
    additionalTechnicians: 0,
    startDate: null,
    endDate: null,
    isTimeSet: [false, false], // [start, end] time is set
    termsAndConditions: false,
});

// ensure using UTC+8 date, use sv locale to get ISO format date
const today = new Date(
    new Date().toLocaleDateString("sv", { timeZone: "Asia/Kuala_Lumpur" })
);
today.setHours(0, 0, 0, 0);

/**
 * STEP 1: EVENT INFORMATION
 * */

// form validation
const formNode = useTemplateRef("formNode");
const calendarNode = useTemplateRef("calendarNode");
const rulesMessage = reactive({
    eventName: [
        {
            required: true,
            message: "Please enter the event name",
            trigger: "blur",
        },
    ],
    numberOfParticipants: [
        {
            required: true,
            message: "Please enter the number of participants",
            trigger: "blur",
        },
    ],
    termsAndConditions: [
        {
            required: true,
            validator: (rule, value, callback) => {
                if (value) {
                    callback();
                } else {
                    callback(
                        new Error("Please agree to the terms and conditions")
                    );
                }
            },
            trigger: "blur",
        },
    ],
    bookingDate: [
        {
            required: true,
            validator: (rule, value, callback) => {
                calendarNode.value.validateDate(formData, callback);
            },
            trigger: "blur",
        },
    ],
    bookingTime: [
        {
            required: true,
            validator: (rule, value, callback) => {
                calendarNode.value.validateTime(formData, callback);
            },
            trigger: "blur",
        },
    ],
});

// price calculation
const priceEstimate = reactive([
    { item: "Facility (2 hours)", price: 1100 },
    { item: "Technician Service", price: 65 },
]);
const getTotal = (param) => {
    const { columns, data } = param;
    const sums = [];
    columns.forEach((column, index) => {
        if (index === 0) {
            sums[index] = h("span", { class: ["price-sum-row"] }, "Total");
            return;
        }
        const values = data.map((item) => Number(item[column.property]));

        let total = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!Number.isNaN(value)) {
                return prev + curr;
            } else {
                return prev;
            }
        }, 0);
        sums[index] = h(
            "span",
            { class: ["price-sum-row"] },
            `RM ${total.toFixed(2)}`
        );
    });

    return sums;
};
function formatCurrency(row) {
    return row.price.toFixed(2);
}
function validateForm(field) {
    // only validate specific field
    if (field) {
        formNode.value.validateField(field);
        return;
    }
    // validate all fields
    formNode.value.validate();
}

// form submit
async function returnFormInfo(formEl) {
    if (!formEl) return;
    await formEl.validate((valid, fields) => {
        if (valid) {
            emit("nextStep", formData);
        } else {
            console.log("error submit!", fields);
        }
    });
}
</script>

<template>
    <el-form
        ref="formNode"
        label-position="top"
        :model="formData"
        :rules="rulesMessage"
    >
        <el-divider content-position="left">
            <h2>Event Information</h2>
        </el-divider>

        <el-row :gutter="8">
            <el-col :sm="24" :md="16">
                <div class="grid">
                    <el-form-item label="Event Name" prop="eventName">
                        <el-input
                            v-model="formData.eventName"
                            required
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="Number of Participants"
                        prop="numberOfParticipants"
                    >
                        <el-input-number
                            v-model="formData.numberOfParticipants"
                            required
                            style="width: 100%"
                            :max="facilityInfo.capacity"
                        ></el-input-number>
                    </el-form-item>
                    <el-form-item label="Technician">
                        <el-input
                            value="Space include 1 technician"
                            disabled
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="Additional Technicians"
                        prop="additionalTechnicians"
                    >
                        <el-input-number
                            v-model="formData.additionalTechnicians"
                            required
                            style="width: 100%"
                        ></el-input-number>
                    </el-form-item>
                </div>

                <el-form-item
                    label="Terms and Conditions"
                    prop="termsAndConditions"
                >
                    <el-checkbox v-model="formData.termsAndConditions">
                        Yes, I agree with Faculty of Law's terms and conditions.
                    </el-checkbox>
                </el-form-item>
            </el-col>
            <el-col :sm="24" :md="8">
                <el-card class="info" shadow="hover">
                    <template #header>
                        <div class="card-header card-header-action-call">
                            <span>Payment Summary</span>
                        </div>
                    </template>
                    <el-table
                        :data="priceEstimate"
                        show-summary
                        :summary-method="getTotal"
                    >
                        <el-table-column prop="item" label="Item" />
                        <el-table-column
                            prop="price"
                            label="Price RM"
                            align="right"
                            min-width="50%"
                            :formatter="formatCurrency"
                        />
                    </el-table>
                </el-card>
            </el-col>
        </el-row>

        <el-divider content-position="left">
            <h2>Time Selection</h2>
        </el-divider>

        <el-alert type="info" show-icon :closable="false">
            <ul>
                <li v-if="!!props.facilityInfo.bookingRule?.approvalRequired">
                    This reservation request will be sent for approval from the
                    space administrator.
                </li>
                <li>
                    The space will be available for booking from
                    <b>{{ props.facilityInfo.bookingRule?.closeDaysAfterBooking }}</b>
                    day(s) after today, up to
                    <b>{{ props.facilityInfo.bookingRule?.openDaysPriorBooking }}</b>
                    day(s) in advance.
                </li>
                <li>
                    Minimum duration for booking is
                    <b>{{ props.facilityInfo.bookingRule?.minBookingHours }}</b>
                    hour(s) per day.
                </li>
                <li>
                    Maximum reservation days is
                    <b>{{ props.facilityInfo.bookingRule?.maxReservationDays }}</b>
                    day(s) per reservation.
                </li>
            </ul>
        </el-alert>

        <booking-calendar
            ref="calendarNode"
            :facilityInfo="props.facilityInfo"
            v-model:formData="formData"
            @validate-form="validateForm"
        ></booking-calendar>

        <div class="end-buttons">
            <el-button type="primary" @click="returnFormInfo(formNode)">
                Next
            </el-button>
        </div>
    </el-form>
</template>

<style scoped>
:global(.price-sum-row) {
    font-weight: 800;
    font-size: 1.15em;
}
</style>
