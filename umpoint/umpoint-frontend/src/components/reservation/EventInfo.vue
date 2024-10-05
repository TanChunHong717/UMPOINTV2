<template>
    <el-card class="information-card" title="Event Information">
        <div class="grid max-2-col">
            <el-text class="info-item">
                <svg-icon class="info-icon" type="mdi" :path="mdiPound" />
                {{ eventInfoDisplay.eventName }}
            </el-text>
            <el-text class="info-item">
                <svg-icon
                    class="info-icon"
                    type="mdi"
                    :path="mdiCalendarClock"
                />
                {{ eventInfoDisplay.datetimeStr }}
            </el-text>
            <el-text class="info-item">
                <svg-icon
                    class="info-icon"
                    type="mdi"
                    :path="mdiAccountGroup"
                />
                {{ eventInfoDisplay.numberOfParticipants }} person(s)
            </el-text>
            <el-text class="info-item">
                <svg-icon
                    class="info-icon"
                    type="mdi"
                    :path="mdiAccountHardHat"
                />
                {{ eventInfoDisplay.totalTechnicians }} technician(s)
            </el-text>
        </div>
    </el-card>
</template>

<script setup>
import {
    mdiPound,
    mdiAccountGroup,
    mdiCalendarClock,
    mdiAccountHardHat,
} from "@mdi/js";
import { defineProps } from "vue";
import { formatDateToTimezoneDateTimeStr } from "@/utils/date";

const props = defineProps(["formData"]);
const eventInfoDisplay = computed(() => {
    if (!props.formData.value || !props.formData.value.eventName) return null;
    return {
        eventName: props.formData.value.eventName ?? "No event name",
        numberOfParticipants: props.formData.value.numberOfParticipants,
        totalTechnicians: 1 + props.formData.value.additionalTechnicians,
        datetimeStr: `${formatDateToTimezoneDateTimeStr(
            props.formData.value.startDate
        )} to ${formatDateToTimezoneDateTimeStr(
            props.formData.value.startDate
        )}`,
    };
});
</script>

<style scoped>
.max-2-col {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;

    @media screen and (max-width: 1000px) {
        grid-template-columns: 1fr;
    }
}
</style>
