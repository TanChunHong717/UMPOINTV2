<template>
    <BaseLayout>
        <template #title>Your Bookings</template>

        <el-tabs
            v-model="activeName"
            class="demo-tabs"
            @tab-click="handleChangeTab"
        >
            <el-tab-pane label="All Bookings" name="all"> </el-tab-pane>
            <el-tab-pane label="Pending" name="pending"> </el-tab-pane>
            <el-tab-pane label="Confirmed" name="confirmed"> </el-tab-pane>
            <el-tab-pane label="Refunded" name="refunded"> </el-tab-pane>
            <el-tab-pane label="Cancelled" name="cancelled"> </el-tab-pane>
        </el-tabs>

        <el-table :data="displayBookings" style="width: 100%; height: 100%">
            <el-table-column type="index" label="#" />
            <el-table-column prop="invoiceno" label="Invoice No." width="160" />
            <el-table-column prop="name" label="Facilty/Event" />

            <template
                v-if="activeName == 'refunded' || activeName == 'cancelled'"
            >
                <el-table-column prop="cancelDate" label="Cancelled Date" />
                <el-table-column
                    v-if="activeName == 'refunded'"
                    prop="refundAmount"
                    label="Refund Amount"
                    width="180"
                />
                <el-table-column prop="reason" label="Reason" />
            </template>

            <template
                v-if="activeName == 'pending' || activeName == 'confirmed'"
            >
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
                    width="140"
                >
                    <template #default="scope">
                        <el-button
                            v-if="activeName == 'pending'"
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
                            @click.prevent="cancelBooking(scope)"
                        >
                            <SvgIcon type="mdi" :path="mdiCancel" />
                        </el-button> </template
                ></el-table-column>
            </template>

            <template v-if="activeName == 'all'">
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
            </template>
        </el-table>
    </BaseLayout>
</template>

<script setup>
import { mdiCurrencyUsd, mdiForum, mdiCancel } from "@mdi/js";
import { ref, onMounted, onActivated, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getCurrentUserBookings } from "@/helpers/api.js";

const router = useRouter();
const route = useRoute();

const activeName = ref("pending");

const handleChangeTab = (tab) => {
    router.push({ path: route.path, query: { view: tab.paneName } });
};

const initialize = () => {
    if (route.query.view) {
        activeName.value = route.query.view;
    }
    getBookings();
};

// get all bookings for this user
const bookings = ref([]);
const getBookings = async () => {
    const response = await getCurrentUserBookings();
    bookings.value = response.data;

    bookings.value = [
        {
            id: 1,
            invoiceno: "20240101sfafds",
            name: "Facility 1",
            status: "pending",
            bookingDate: "2024-01-01",
            eventDate: "2024-01-01",
        },
    ];
};
const displayBookings = computed(() => {
    if (activeName.value == "all") {
        return bookings.value;
    }

    let displayedBookings = bookings.value.filter(
        (booking) => booking.status.toLowerCase() == activeName.value
    );

    if (activeName.value == "pending" || activeName.value == "confirmed") {
        displayedBookings = displayedBookings.map((booking) => {
            booking.action = "View";
            return booking;
        });
    }

    return displayedBookings;
});
const statusFormatter = (row, column) => {
    return row.status[0].toUpperCase() + row.status.slice(1);
};

// *** pending actions ***
const payForBooking = (row) => {
    console.log("Pay for booking", row);
};
const startChat = (row) => {
    console.log("Start chat", row);
};
const cancelBooking = (row) => {
    console.log("Cancel booking", row);
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
