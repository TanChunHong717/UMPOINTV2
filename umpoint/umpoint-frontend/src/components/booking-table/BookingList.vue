<template>
    <el-tabs v-model="activeStatus" @tab-click="handleChangeStatus">
        <el-tab-pane label="All Bookings" name="all">
            <el-table
                :data="spaceDisplayBookings"
                stripe
                style="width: 100%; height: 100%"
            >
                <el-table-column type="expand">
                    <template #default="{ row }">
                        <BookingInfoCard :booking="row" />
                    </template>
                </el-table-column>
                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column
                    prop="name"
                    label="Event/Facilty"
                    :formatter="eventNameFormatter"
                />
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
                    <template #default="{ row }">
                        <BookingInfoCard :booking="row" />
                    </template>
                </el-table-column>

                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column
                    prop="name"
                    label="Event/Facilty"
                    :formatter="eventNameFormatter"
                />

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
                    <template #default="{ row }">
                        <BookingInfoCard :booking="row" />
                    </template>
                </el-table-column>

                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column
                    prop="name"
                    label="Event/Facilty"
                    :formatter="eventNameFormatter"
                />

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
                    prop="paymentAmount"
                    label="Payment Amount"
                    width="100"
                    :formatter="paymentAmountFormatter"
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
        <el-tab-pane label="Completed" name="completed">
            <el-table
                :data="spaceDisplayBookings"
                stripe
                style="width: 100%; height: 100%"
            >
                <el-table-column type="expand">
                    <template #default="{ row }">
                        <BookingInfoCard :booking="row" />
                    </template>
                </el-table-column>

                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column
                    prop="name"
                    label="Event/Facilty"
                    :formatter="eventNameFormatter"
                />

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
                    prop="paymentAmount"
                    label="Payment Amount"
                    width="100"
                    :formatter="paymentAmountFormatter"
                />
                <el-table-column
                    prop="action"
                    fixed="right"
                    label="Action"
                    width="140"
                >
                    <template #default="scope">
                        <el-dropdown
                            :hide-on-click="false"
                            class="action-dropdown"
                        >
                            <el-button
                                link
                                title="Add to calendar"
                                type="primary"
                                size="small"
                            >
                                <SvgIcon type="mdi" :path="mdiCalendarEdit" />
                            </el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item
                                        @click.prevent="
                                            addToCalendar(scope, 'Google')
                                        "
                                    >
                                        Google
                                    </el-dropdown-item>
                                    <el-dropdown-item
                                        @click.prevent="
                                            addToCalendar(scope, 'Microsoft365')
                                        "
                                    >
                                        Outlook
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
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
                    <template #default="{ row }">
                        <BookingInfoCard :booking="row" />
                    </template>
                </el-table-column>

                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column
                    prop="name"
                    label="Event/Facilty"
                    :formatter="eventNameFormatter"
                />
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
                    <template #default="{ row }">
                        <BookingInfoCard :booking="row" />
                    </template>
                </el-table-column>

                <el-table-column
                    prop="invoiceno"
                    label="Invoice No."
                    width="160"
                />
                <el-table-column
                    prop="name"
                    label="Event/Facilty"
                    :formatter="eventNameFormatter"
                />
                <el-table-column prop="cancelDate" label="Cancelled Date" />
                <el-table-column
                    prop="refundAmount"
                    label="Refund Amount"
                    width="180"
                />
            </el-table>
        </el-tab-pane>
    </el-tabs>
</template>

<script setup>
import { mdiCurrencyUsd, mdiForum, mdiCancel, mdiCalendarEdit } from "@mdi/js";
import { computed, h } from "vue";
import { bookingStatus } from "@/constants/app";
import { ElMessageBox } from "element-plus";
import { atcb_action as addToCalendarApi } from "add-to-calendar-button";
import { diffDays } from "@/utils/date";

const emit = defineEmits([
    "changeStatus",
    "startChat",
    "payForBooking",
    "cancelBooking",
]);

const activeStatus = defineModel("activeStatus");
const bookings = defineModel("bookings");
const totalBookings = defineModel("totalBookings");

const handleChangeStatus = (tab) => {
    emit("changeStatus", tab.paneName);
};

const spaceDisplayBookings = computed(() => {
    if (activeStatus.value == "all") {
        totalBookings.value = bookings.value.length;
        return bookings.value;
    }

    let displayedBookings = bookings.value.filter(
        (booking) =>
            bookingStatus[booking.status].toLowerCase() == activeStatus.value
    );
    totalBookings.value = displayedBookings.length;

    return displayedBookings;
});
// table formatters
const eventNameFormatter = (row, column) => {
    return h("div", {}, [
        h("span", {}, row.eventName),
        h("br"),
        h(
            "span",
            {
                style: "font-size: 0.85em; color: var(--el-text-color-secondary)",
            },
            row.facility
        ),
    ]);
};
const statusFormatter = (row, column) => {
    if (activeStatus.value == "all") {
        return h(
            "span",
            { class: "status-" + row.status },
            bookingStatus[row.status]
        );
    }
    return bookingStatus[row.status];
};
const paymentAmountFormatter = (row, column) => {
    return row.paymentAmount > 0
        ? `RM ${row.paymentAmount.toFixed(2)}`
        : "Free";
};

// *** pending actions ***
const startChat = ({ row }) => {
    emit("startChat", row);
};
const payForBooking = async ({ row }) => {
    let confirmPay = await confirmPayBooking(row.eventName, row.paymentAmount);
    if (confirmPay) {
        emit("payForBooking", row.id);
    }
};
const confirmPayBooking = async (eventName, amount) => {
    return ElMessageBox.confirm(
        h("div", null, [
            h("span", `You are about to pay `),
            h(
                "span",
                { style: "font-weight: bold" },
                `RM ${amount.toFixed(2)}`
            ),
            h("span", ` for "${eventName}".`),
            h("br"),
            h("span", `Continue?`),
        ]),
        "Payment",
        { confirmButtonText: "Pay", cancelButtonText: "Cancel" }
    ).then(
        () => true,
        () => false
    );
};

// cancel booking confirmation dialog
const showCancelBookingDialog = async ({ row }) => {
    let confirmCancel = await confirmCancelBooking(row.eventName);
    if (confirmCancel) {
        emit("cancelBooking", row.id);
    }
};
const confirmCancelBooking = async (eventName) => {
    return ElMessageBox.confirm(
        `Are you sure you want to cancel booking "${eventName}"?`,
        "Warning",
        {
            confirmButtonText: "Confirm",
            cancelButtonText: "Take me back",
            type: "warning",
        }
    ).then(
        () => true,
        () => false
    );
};

// *** completed actions ***
const addToCalendar = ({ row }, calendarType) => {
    let event = {
        name: row.eventName,
        startDate: row.meta.startDate,
        startTime: row.meta.startTime,
        endTime: row.meta.endTime,
        options: [calendarType],
        timeZone: "Asia/Kuala_Lumpur",
        location: `${row.facility}, UM`,
    };
    if (row.meta.startDate !== row.meta.endDate) {
        event.recurrence = "daily";
        event.recurrence_count = diffDays(new Date(row.meta.startDate), new Date(row.meta.endDate)) + 1;
        event.iCalFileName = `${row.eventName} - ${row.meta.startDate} to ${row.meta.endDate}`;
    }

    addToCalendarApi(event);
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

.action-dropdown {
    padding-right: 12px;
}
</style>
