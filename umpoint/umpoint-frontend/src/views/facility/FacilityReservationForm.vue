<template>
    <BaseLayout>
        <template #title>Facility Reservation</template>
        <template #subtitle>
            {{ facilityInfo.name }} @
            {{ facilityInfo.deptName }}
        </template>

        <el-steps class="form-steps" :active="currentStep" align-center>
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

        <KeepAlive>
            <component
                :is="formsPage[currentStep]"
                :facilityInfo="facilityInfo"
                :formData="formData"
                :pricingDetails="pricingDetails"
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
import { useRoute } from "vue-router";
import { getFacilityInformation } from "@/helpers/api.js";
import StepOne from "@/components/reservation/StepOne.vue";
import StepTwo from "@/components/reservation/StepTwo.vue";
import StepThree from "@/components/reservation/StepThree.vue";

const formsPage = [StepOne, StepTwo, StepThree];

const currentStep = ref(0);
const route = useRoute();

// first init
const facilityInfo = shallowRef({});
const form = reactive({});

// change when route facility id change
watch(() => route.params.id, getFacilityInfo, { immediate: true });

async function getFacilityInfo(facilityId) {
    let response = await getFacilityInformation(facilityId);
    if (response.data.code !== 0) {
        throw new Error(response.data.message);
    }
    facilityInfo.value = response.data.data;
    form.facilityId = facilityId;
    // reset form to step 0
    currentStep.value = 0;
}

const pricingDetails = computed(() => {
    if (!form || !form.numberOfParticipants) return [];
    return [
        {
            item: "Facility Rental",
            amount: 1,
            price: 100,
            total: 100,
        },
        {
            item: "Technician",
            amount: 1 + form.additionalTechnicians,
            price: 50,
            total: (1 + form.additionalTechnicians) * 50,
        },
    ];
});

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
function submitForm() {
    // submit form
    console.log("Form submitted", form);
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
