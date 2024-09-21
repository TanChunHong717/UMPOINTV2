<template>
    <el-card class="information-card" title="Event Information">
        <div class="grid max-2-col">
            <el-text class="info-item">
                <svg-icon class="info-icon" type="mdi" :path="mdiPound" />
                {{ props.eventInfo.eventName }}
            </el-text>
            <el-text class="info-item">
                <svg-icon
                    class="info-icon"
                    type="mdi"
                    :path="mdiCalendarClock"
                />
                {{ props.eventInfo.datetimeStr }}
            </el-text>
            <el-text class="info-item">
                <svg-icon
                    class="info-icon"
                    type="mdi"
                    :path="mdiAccountGroup"
                />
                {{ props.eventInfo.numberOfParticipants }} person(s)
            </el-text>
            <el-text class="info-item">
                <svg-icon
                    class="info-icon"
                    type="mdi"
                    :path="mdiAccountHardHat"
                />
                {{ props.eventInfo.totalTechnicians }} technician(s)
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

const props = defineProps(["eventInfo"]);
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
