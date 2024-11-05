<template>
    <BaseLayout>
        <template #title>Your Bookings</template>

        <el-tabs
            type="border-card"
            v-model="activeType"
            @tab-click="handleChangeType"
        >
            <el-tab-pane label="Space" name="space">
                <BookingList
                    v-model:activeStatus="activeStatus"
                    v-model:bookings="bookings"
                    @change-status="handleChangeStatus"
                    @refresh-bookings="getCurrentUserBookings"
                />
            </el-tab-pane>

            <el-tab-pane
                label="Accommodation"
                name="accommodation"
            ></el-tab-pane>
        </el-tabs>
    </BaseLayout>
</template>

<script setup>
import { ref, onMounted, onActivated, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getCurrentUserBookings } from "@/helpers/api-facility.js";
import { bookingStatus, paymentStatus } from "@/constants/app.js";
import BookingList from "@/components/booking-table/BookingList.vue";

const router = useRouter();
const route = useRoute();

const activeType = ref("space");
const activeStatus = ref("pending");

const facilityTypeParamName = "facility";
const statusParamName = "view";

const handleChangeType = (tab) => {
    router.push({
        path: route.path,
        query: {
            [facilityTypeParamName]: tab.paneName,
            [statusParamName]: "pending",
        },
    });
};
const handleChangeStatus = (tabName) => {
    console.log("status", tabName);
    router.push({
        path: route.path,
        query: {
            [facilityTypeParamName]: activeType.value,
            [statusParamName]: tabName,
        },
    });
};

const initialize = () => {
    if (route.query.view) {
        activeType.value = route.query[facilityTypeParamName];
        activeStatus.value = route.query[statusParamName];
    }
    getBookings();
};

const formatResponseTimeWithoutSeconds = (time) => {
    return time.split(":")[0] + ":" + time.split(":")[1];
};

// get all bookings for this user
const bookings = ref([]);
const getBookings = async () => {
    const response = await getCurrentUserBookings();
    if (response.status !== 200 || response.data.code !== 0) {
        console.error("Error fetching bookings", response);
        return;
    }
    bookings.value = response.data.data.list.map((booking) => {
        return {
            id: booking.id,
            invoiceno: booking.id,
            name: booking.space + " <br> " + booking.event,
            status: booking.status,
            bookingDate: booking.createDate.split(" ")[0],
            eventDate: `${booking.startDay} to ${
                booking.endDay
            }, ${formatResponseTimeWithoutSeconds(
                booking.startTime
            )} to ${formatResponseTimeWithoutSeconds(booking.endTime)}`,
            // payment
            paymentAmount: booking.paymentAmount,
            paymentStatus: booking.spcPaymentDTOList[0]?.status ?? -1,
            // cancel and refund
            cancelDate:
                booking.status == bookingStatus.CANCELLED
                    ? booking.updateDate
                    : undefined,
            refundAmount:
                booking.status == bookingStatus.CANCELLED &&
                booking.spcPaymentDTOList[0]?.status == paymentStatus.REFUNDED
                    ? booking.spcPaymentDTOList[0]?.amount
                    : undefined,
        };
    });
    console.log("Bookings", response.data.data);
};

// refresh
onMounted(() => {
    initialize();
});
onActivated(() => {
    initialize();
});
</script>

<style scoped>
.el-button {
    + .el-button {
        margin-left: 6px;
    }
    svg {
        width: 1.8em;
        height: 1.8em;
    }
}
</style>
