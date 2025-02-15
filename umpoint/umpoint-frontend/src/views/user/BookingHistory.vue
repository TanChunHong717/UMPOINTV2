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
                    @start-chat="startChatAction"
                    @pay-for-booking="payForBookingAction"
                    @cancel-booking="cancelBookingAction"
                    v-model:totalBookings="totalBookings"
                />
                <el-pagination
                    v-model:current-page="currentPage"
                    @current-change="handlePageChange"
                    :hide-on-single-page="false"
                    :total="totalBookings"
                    :page-size="pageSize"
                >
                </el-pagination>
            </el-tab-pane>

            <el-tab-pane label="Service" name="service"></el-tab-pane>
            <el-tab-pane
                label="Accommodation"
                name="accommodation"
            ></el-tab-pane>
        </el-tabs>
    </BaseLayout>
</template>

<script setup>
import { ref, onMounted, onActivated, h, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import BookingList from "@/components/booking-table/BookingList.vue";
import { bookingStatus, paymentStatus } from "@/constants/app.js";
import {
    getCurrentUserBookings,
    cancelBooking,
    payBooking,
} from "@/helpers/api-facility";

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
    router.push({
        path: route.path,
        query: {
            [facilityTypeParamName]: activeType.value,
            [statusParamName]: tabName,
        },
    });
};
const handlePageChange = (page) => {
    if (page < 1 || page > totalBookings.value) {
        return;
    }
    router.push({
        path: route.path,   
        query: {
            ...route.query,
            page: page,
        },
    });
    currentPage.value = page;
    getBookings();
}

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
const pageSize = 10;
const currentPage = ref(1);
const totalBookings = ref(0);
const bookings = ref([]);
const getBookings = async () => {
    const response = await getCurrentUserBookings(activeType.value, {
        page: currentPage.value,
        limit: pageSize
    });
    if (response.status !== 200 || response.data.code !== 0) {
        console.error("Error fetching bookings", response);
        return;
    }
    totalBookings.value = response.data.data.total;
    bookings.value = response.data.data.list.map((booking) => {
        let bookingDateStr = `${booking.startDay}`;
        if (booking.startDay != booking.endDay) {
            bookingDateStr += ` to ${booking.endDay}`;
        }
        bookingDateStr += `, ${formatResponseTimeWithoutSeconds(
            booking.startTime
        )} to ${formatResponseTimeWithoutSeconds(booking.endTime)}`;

        return {
            id: booking.id,
            invoiceno: booking.id,
            eventName: booking.event,
            facility: booking[activeType.value],
            status: booking.status,
            bookingDate: booking.createDate.split(" ")[0],
            eventDate: bookingDateStr,
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
            // additional info
            meta: {
                facilityId:
                    activeType.value == "space"
                        ? booking.spaceId
                        : activeType.value == "service"
                        ? booking.serviceId
                        : activeType.value == "accommodation"
                        ? booking.accommodationId
                        : booking.spaceId,
                startDate: booking.startDay,
                endDate: booking.endDay,
                startTime: booking.startTime,
                endTime: booking.endTime,
            },
        };
    });
};

// handle actions for booking
const startChatAction = (booking) => {
    router.push({
        name: "chat",
        query: {
            facilityType: activeType.value,
            facilityId: booking.meta.facilityId,
        },
    });
};
const cancelBookingAction = async (bookingId) => {
    let response = await cancelBooking(activeType.value, bookingId);
    if (response.status != 200 || response.data.code != 0) {
        ElMessage.error("Error cancelling booking");
        return;
    }
    ElMessage({ type: "success", message: "Booking cancelled successfully" });
    // refresh bookings
    getBookings();
};
const payForBookingAction = async (bookingId) => {
    let response = await payBooking(activeType.value, bookingId);
    if (response.status != 200 || response.data.code != 0) {
        ElMessage.error("Error paying for booking");
        return;
    }
    ElMessage({
        type: "success",
        message: "Booking paid for successfully",
    });
    // refresh bookings
    getBookings();
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
