<template>
    <el-tabs v-model="activeStatus" @tab-click="handleChangeStatus">
        <el-tab-pane label="All Bookings" name="all">
            <el-table
                :data="spaceDisplayBookings"
                stripe
                style="width: 100%; height: 100%"
            >
                <el-table-column type="expand">
                    <template #default="props">
                        <el-card>
                            <p>Booking Details</p>
                            <p>
                                Booking Date:
                                {{ props.row.bookingDate }}
                            </p>
                            <p>
                                Event Date:
                                {{ props.row.eventDate }}
                            </p>
                        </el-card>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column prop="name" label="Facilty/Event" />
                <el-table-column
                    prop="eventDate"
                    label="Event Date"
                    width="120"
                />
                <el-table-column
                    prop="status"
                    fixed="right"
                    label="Status"
                    width="100"
                    :formatter="statusFormatter"
                />
            </el-table>
        </el-tab-pane>
        <el-tab-pane label="Pending" name="pending"
            ><el-table
                :data="spaceDisplayBookings"
                stripe
                style="width: 100%; height: 100%"
            >
                <el-table-column type="expand">
                    <template #default="props">
                        <el-card>
                            <p>Booking Details</p>
                            <p>
                                Booking Date:
                                {{ props.row.bookingDate }}
                            </p>
                            <p>
                                Event Date:
                                {{ props.row.eventDate }}
                            </p>
                        </el-card>
                    </template>
                </el-table-column>

                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column prop="name" label="Facilty/Event" />

                <el-table-column
                    prop="bookingDate"
                    label="Booking Date"
                    width="120"
                />
                <el-table-column
                    prop="eventDate"
                    label="Event Date"
                    width="120"
                />
                <el-table-column
                    prop="action"
                    fixed="right"
                    label="Action"
                    width="100"
                >
                    <template #default="scope">
                        <el-button
                            link
                            title="Start chat"
                            type="primary"
                            size="small"
                            @click.prevent="startChat(scope)"
                        >
                            <SvgIcon type="mdi" :path="mdiForum" />
                        </el-button>
                        <el-button
                            link
                            title="Cancel booking"
                            type="danger"
                            size="small"
                            @click.prevent="showCancelBookingDialog(scope)"
                        >
                            <SvgIcon type="mdi" :path="mdiCancel" />
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-tab-pane>
        <el-tab-pane label="Approved" name="approved">
            <el-table
                :data="spaceDisplayBookings"
                stripe
                style="width: 100%; height: 100%"
            >
                <el-table-column type="expand">
                    <template #default="props">
                        <el-card>
                            <p>Booking Details</p>
                            <p>
                                Booking Date:
                                {{ props.row.bookingDate }}
                            </p>
                            <p>
                                Event Date:
                                {{ props.row.eventDate }}
                            </p>
                        </el-card>
                    </template>
                </el-table-column>

                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column prop="name" label="Facilty/Event" />

                <el-table-column
                    prop="bookingDate"
                    label="Booking Date"
                    width="120"
                />
                <el-table-column
                    prop="eventDate"
                    label="Event Date"
                    width="120"
                />
                <el-table-column
                    prop="paymentStatus"
                    label="Payment Status"
                    width="100"
                />
                <el-table-column
                    prop="action"
                    fixed="right"
                    label="Action"
                    width="140"
                >
                    <template #default="scope">
                        <el-button
                            v-if="scope.row.paymentAmount > 0"
                            link
                            title="Pay for booking"
                            type="primary"
                            size="small"
                            @click.prevent="payForBooking(scope)"
                        >
                            <SvgIcon type="mdi" :path="mdiCurrencyUsd" />
                        </el-button>
                        <el-button
                            link
                            title="Start chat"
                            type="primary"
                            size="small"
                            @click.prevent="startChat(scope)"
                        >
                            <SvgIcon type="mdi" :path="mdiForum" />
                        </el-button>
                        <el-button
                            link
                            title="Cancel booking"
                            type="danger"
                            size="small"
                            @click.prevent="showCancelBookingDialog(scope)"
                        >
                            <SvgIcon type="mdi" :path="mdiCancel" />
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-tab-pane>
        <el-tab-pane label="Rejected" name="rejected">
            <el-table
                :data="spaceDisplayBookings"
                stripe
                style="width: 100%; height: 100%"
            >
                <el-table-column type="expand">
                    <template #default="props">
                        <el-card>
                            <p>Booking Details</p>
                            <p>
                                Booking Date:
                                {{ props.row.bookingDate }}
                            </p>
                            <p>
                                Event Date:
                                {{ props.row.eventDate }}
                            </p>
                        </el-card>
                    </template>
                </el-table-column>

                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column prop="name" label="Facilty/Event" />
                <el-table-column
                    prop="eventDate"
                    label="Event Date"
                    width="240"
                />
            </el-table>
        </el-tab-pane>
        <el-tab-pane label="Cancelled" name="cancelled">
            <el-table
                :data="spaceDisplayBookings"
                stripe
                style="width: 100%; height: 100%"
            >
                <el-table-column type="expand">
                    <template #default="props">
                        <el-card>
                            <p>Booking Details</p>
                            <p>
                                Booking Date:
                                {{ props.row.bookingDate }}
                            </p>
                            <p>
                                Event Date:
                                {{ props.row.eventDate }}
                            </p>
                        </el-card>
                    </template>
                </el-table-column>

                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column prop="name" label="Facilty/Event" />
                <el-table-column prop="cancelDate" label="Cancelled Date" />
                <el-table-column
                    prop="refundAmount"
                    label="Refund Amount"
                    width="180"
                />
            </el-table>
        </el-tab-pane>
    </el-tabs>

    <el-dialog
        v-model="deleteDialogVisible"
        title="Warning"
        width="500"
        align-center
    >
        <span>Are you sure you want to cancel this booking?</span>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="deleteDialogVisible = false">
                    Take me back
                </el-button>
                <el-button type="primary" @click="confirmCancelBooking">
                    Confirm
                </el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { mdiCurrencyUsd, mdiForum, mdiCancel } from "@mdi/js";
import { ref, computed, h } from "vue";
import { bookingStatus, paymentStatus } from "@/constants/app";
import { ElMessage } from "element-plus";
import { cancelBooking } from "@/helpers/api.js";

const activeStatus = defineModel("activeStatus");
const emit = defineEmits(["changeStatus", "refreshBookings"]);
const bookings = defineModel("bookings");

const handleChangeStatus = (tab) => {
    emit("changeStatus", tab.paneName);
};

const spaceDisplayBookings = computed(() => {
    if (activeStatus.value == "all") {
        return bookings.value;
    }

    let displayedBookings = bookings.value.filter(
        (booking) =>
            bookingStatus[booking.status].toLowerCase() == activeStatus.value
    );

    return displayedBookings;
});
const statusFormatter = (row, column) => {
    if (activeStatus.value == "all") {
        return h('span', {class:"status-"+row.status}, bookingStatus[row.status]);
    }
    return bookingStatus[row.status];
};

// *** pending actions ***
const payForBooking = (row) => {
    console.log("Pay for booking", row);
};
const startChat = (row) => {
    console.log("Start chat", row);
};

const deleteDialogVisible = ref(false);
const selectedCancelBooking = ref(null);
const showCancelBookingDialog = ({ row }) => {
    deleteDialogVisible.value = true;
    selectedCancelBooking.value = row.id;
};
const confirmCancelBooking = async () => {
    deleteDialogVisible.value = false;
    let response = await cancelBooking(selectedCancelBooking.value);
    if (response.status != 200 || response.data.code != 0) {
        ElMessage.error("Error cancelling booking");
        return;
    }
    ElMessage({ type: "success", message: "Booking cancelled successfully" });
    emit("refreshBookings");
};
</script>


<style>
/* Sync with constants/app.js:bookingStatus */
span.status-0 {
    color: var(--el-color-primary-dark-2);
}
span.status-1 {
    color: var(--el-color-error);
}
span.status-2 {
    color: var(--el-color-warning);
}
span.status-3 {
    color: var(--el-table-text-color);
}
span.status-4 {
    color: var(--el-table-text-color);
}
</style>