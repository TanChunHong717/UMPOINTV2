<template>
    <el-card body-class="booking-details-card">
        <h4>Booking Details</h4>
        <p>Event Name: {{ booking.eventName }}</p>
        <p>Facility: {{ booking.facility }}</p>
        <p>Booking Date: {{ booking.bookingDate }}</p>
        <p>Event Date: {{ booking.eventDate }}</p>
        <div v-if="booking.paymentAmount != 0">
            <p>Payment Status: {{ paymentStatus[booking.paymentStatus] }}</p>
            <p>Payment Amount: RM {{ booking.paymentAmount.toFixed(2) }}</p>
        </div>
    </el-card>
</template>

<script setup>
import { paymentStatus } from "@/constants/app";
const { booking } = defineProps(["booking"]);
</script>

<style>
.booking-details-card {
    h4 {
        margin-block-start: 0;
    }
    p {
        margin-block-start: 0.5em;
        margin-block-end: 0;
    }
}
</style>
