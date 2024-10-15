<template>
    <el-card class="information-card" title="Event Information">
        <div class="grid max-2-col">
            <el-text class="info-item">
                <el-icon
                    class="info-icon"
                    title="Event Name"
                    aria-label="Event Name"
                >
                    <svg-icon type="mdi" :path="mdiPound" />
                </el-icon>
                {{ eventInfoDisplay.eventName }}
            </el-text>
            <el-text class="info-item">
                <el-icon
                    class="info-icon"
                    title="Event Date and Time"
                    aria-label="Event Date and Time"
                >
                    <svg-icon type="mdi" :path="mdiCalendarClock" />
                </el-icon>
                {{ eventInfoDisplay.datetimeStr }}
            </el-text>
            <el-text class="info-item">
                <el-icon
                    class="info-icon"
                    title="Number of Participants"
                    aria-label="Number of Participants"
                >
                    <svg-icon type="mdi" :path="mdiAccountGroup" />
                </el-icon>
                {{ eventInfoDisplay.numberOfParticipants }} person(s)
            </el-text>
            <el-text class="info-item">
                <el-icon
                    class="info-icon"
                    title="Number of Technicians"
                    aria-label="Number of Technicians"
                >
                    <svg-icon type="mdi" :path="mdiAccountHardHat" />
                </el-icon>
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
import { defineProps, computed } from "vue";
import { formatDateToTimezoneDateTimeStr } from "@/utils/date";

const props = defineProps(["formData"]);
const eventInfoDisplay = computed(() => {
    if (!props.formData || !props.formData.eventName) return {};
    return {
        eventName: props.formData.eventName ?? "No event name",
        numberOfParticipants: props.formData.numberOfParticipants,
        totalTechnicians: 1 + props.formData.additionalTechnicians,
        datetimeStr: `${formatDateToTimezoneDateTimeStr(
            props.formData.startDate
        )} to ${formatDateToTimezoneDateTimeStr(props.formData.startDate)}`,
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
