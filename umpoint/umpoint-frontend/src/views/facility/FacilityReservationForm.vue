<template>
    <BaseLayout>
        <template #title>Facility Reservation</template>
        <template #subtitle v-if="!isLoading">
            {{ facilityInfo.name }} @
            {{ facilityInfo.deptName }}
        </template>

        <el-steps
            class="form-steps"
            :active="currentStep"
            align-center
            v-show="!lastStep"
        >
            <el-step title="Event Details">
                <template #icon>
                    <svg-icon type="mdi" :path="mdiCalendarTextOutline" />
                </template>
            </el-step>
            <el-step title="Additional Information">
                <template #icon>
                    <svg-icon type="mdi" :path="mdiFilePlus" />
                </template>
            </el-step>
            <el-step title="Order Confirmation">
                <template #icon>
                    <svg-icon type="mdi" :path="mdiFormatListChecks" />
                </template>
            </el-step>
        </el-steps>

        <br />

        <el-skeleton :rows="5" animated v-if="isLoading" text="Loading..." />
        <KeepAlive v-else>
            <component
                :is="formsPage[currentStep]"
                :facilityInfo="facilityInfo"
                :formData="form"
                @nextStep="nextStep"
                @previousStep="previousStep"
                @submitForm="submitForm"
            />
        </KeepAlive>
    </BaseLayout>
</template>

<script setup>
import {
    mdiCalendarTextOutline,
    mdiFilePlus,
    mdiFormatListChecks,
} from "@mdi/js";
import { ref, reactive, computed, watch, shallowRef } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
    getFacilityInformation,
    createBooking,
    transformBookingRule,
} from "@/helpers/api-facility";
import {
    formatDateToTimezoneDateStr,
    formatDateToTimezoneTimeStr,
} from "@/utils/date";
import StepOne from "@/components/reservation/StepOne.vue";
import StepTwo from "@/components/reservation/StepTwo.vue";
import StepThree from "@/components/reservation/StepThree.vue";
import { ElMessage } from "element-plus";

const formsPage = [StepOne, StepTwo, StepThree];

const currentStep = ref(0);
const router = useRouter();
const route = useRoute();

const isLoading = ref(true);
const lastStep = computed(() => currentStep.value === formsPage.length - 1);

// first init
const facilityInfo = shallowRef({});
const form = reactive({});

// change when route facility id change
watch(() => [route.params.type, route.params.id], getFacilityInfo, {
    immediate: true,
});

async function getFacilityInfo([facilityType, facilityId]) {
    isLoading.value = true;
    // call api to get facility information and parse information
    let response = await getFacilityInformation(facilityType, facilityId);
    if (response.data.code !== 0) {
        throw new Error(response.data.message);
    }
    facilityInfo.value = transformBookingRule(facilityType, response.data.data);
    facilityInfo.value.type = facilityType;
    // save id
    form.facilityId = facilityId;
    // reset form to step 0
    currentStep.value = 0;

    isLoading.value = false;
}

// form stepping
function previousStep() {
    currentStep.value--;
}
function nextStep(componentForm) {
    // merge component form data into main data
    Object.assign(form, componentForm);

    // move to next step
    currentStep.value++;
}
async function submitForm() {
    // submit form
    let attachments = [].concat(
        form.approvalDocuments.map((file) => file.response),
        form.supportingDocuments.map((file) => file.response)
    );
    try {
        let response = await createBooking(route.params.type, {
            spaceId: form.facilityId,
            event: form.eventName,
            startDay: formatDateToTimezoneDateStr(form.startDate),
            endDay: formatDateToTimezoneDateStr(form.endDate),
            startTime: form.startTime + ":00",
            endTime: form.endTime + ":00",
            attachments,
        });

        if (response.data.code == 0) {
            ElMessage({
                type: "success",
                message: "Booking request submitted successfully",
            });
            // redirect to all bookings page
            setTimeout(() => {
                // redirect to success page
                router.push({ name: "bookings" });
            }, 2000);
        } else {
            console.error("Error submitting form", response);
            ElMessage({
                type: "error",
                message: "Error submitting form",
            });
        }
    } catch ({response}) {
        //handle error
        if (response.data.message) {
            ElMessage({
                type: "error",
                message: response.data.message,
                duration: 10000,
            });
            currentStep.value = 0;
        } else if (response.data.msg) {
            ElMessage({
                type: "error",
                message: response.data.msg,
                duration: 10000,
            });
            currentStep.value = 0;
        } else {
            ElMessage({
                type: "error",
                message: "Error submitting form",
            });
        }
    }
}
</script>

<style>
.el-steps.form-steps {
    max-width: 800px;
    margin-inline: auto;

    .el-step__line {
        top: 1.25em !important;
        height: 1px;
    }

    .el-step__icon {
        border-width: 2px;
        border-style: solid;
        border-radius: 50%;
        width: 3em;
        height: 3em;
    }

    .el-step__title {
        margin-top: 0.5em;
        line-height: 1.2;
    }
}

.el-card {
    margin-bottom: 1em;
}

.el-alert.el-alert--info {
    margin-block-end: 1em;

    ul {
        margin-block: 0.5em;
        padding-inline-start: 1em;
    }

    ul > li {
        font-size: 0.9em;
        line-height: 1.25;
    }
}

div.end-buttons {
    display: flex;
    justify-content: space-between;
    margin-block: 1em 0;

    & .el-button:only-child {
        margin-left: auto;
    }
}
</style>
