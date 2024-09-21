<script setup>
import { reactive, useTemplateRef, h } from "vue";

const emit = defineEmits(["nextStep"]);
defineOptions({
    inheritAttrs: false,
});

const formData = reactive({
    eventName: null,
    numberOfParticipants: 1,
    additionalTechnicians: 0,
    date: null,
    startTime: null,
    endTime: null,
    termsAndConditions: false,
});

// ensure using UTC+8 date, use sv locale to get ISO format date
const today = new Date(new Date().toLocaleDateString("sv", { timeZone: "Asia/Kuala_Lumpur" }));
today.setHours(0, 0, 0, 0);

function addDays(date, days) {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
}

/**
 * STEP 1: EVENT INFORMATION
 * */
const formNode = useTemplateRef("formNode");
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
                if (!formData.date) {
                    callback(new Error("Please select a date range"));
                    return;
                }
                let [startDate, endDate] = formData.date;
                let allow =
                    startDate <= endDate &&
                    startDate >= today &&
                    endDate <= addDays(today, 30);
                if (allow) {
                    callback();
                } else {
                    callback(new Error("Please select a valid date range"));
                }
            },
            trigger: "blur",
        },
    ],
    bookingTime: [
        {
            required: true,
            validator: (rule, value, callback) => {
                let { startTime, endTime } = formData;
                if (!startTime || !endTime) {
                    callback(new Error("Please select a time range"));
                    return;
                }
                let allow = startTime <= endTime;
                if (allow) {
                    callback();
                } else {
                    callback(new Error("Please select a valid time range"));
                }
            },
            trigger: "blur",
        },
    ],
});
const notAllowedDates = (date) => {
    return date <= today || date > addDays(today, 30);
};
const setTomorrow = () => {
    formData.date = [addDays(today, 1), addDays(today, 1)];
    // trigger validation to clear error
    formNode.value.validateField("bookingDate");
};

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
                <el-alert type="info" show-icon :closable="false">
                    <ul>
                        <li>
                            This reservation request will be sent for approval
                            from the space administrator.
                        </li>
                        <li>
                            The space will be open for booking 30 day(s) prior
                            the event and will be closed 3 day(s) before the
                            selected booking date.
                        </li>
                        <li>
                            Maximum Reservation day is 30 day(s) per
                            reservation.
                        </li>
                        <li>Minimum hour for booking is 1 hour(s) per date.</li>
                    </ul>
                </el-alert>

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
        <el-form-item label="Terms and Conditions" prop="termsAndConditions">
            <el-checkbox v-model="formData.termsAndConditions">
                Yes, I agree with Faculty of Law's terms and conditions.
            </el-checkbox>
        </el-form-item>

        <el-divider content-position="left">
            <h2>Time Selection</h2>
        </el-divider>

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
                    ></el-time-select
                    >&nbsp;&nbsp;→&nbsp;&nbsp;
                    <el-time-select
                        v-model="formData.endTime"
                        placeholder="End time"
                        start="08:30"
                        step="00:30"
                        end="18:30"
                    ></el-time-select>
                </el-form-item>
            </el-col>
        </el-row>

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

.time-select-col > .el-form-item__content {
    flex: 1;
    display: flex;
    flex-direction: row;
    > .el-select {
        flex: 1;
    }
}
</style>
