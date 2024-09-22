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

        <el-table>
            <el-table-column type="index" label="#" />
            <el-table-column prop="invoiceno" label="Invoice No." width="120" />
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
                    width="150"
                />
                <el-table-column
                    prop="eventDate"
                    label="Event Date"
                    width="150"
                />
                <el-table-column
                    prop="action"
                    label="Action"
                    width="100"
                />
            </template>

            <el-table-column
                v-if="activeName == 'all'"
                prop="status"
                label="Status"
                width="100"
            />
        </el-table>
    </BaseLayout>
</template>

<script setup>
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();

const activeName = ref("pending");

const handleChangeTab = (tab) => {
    router.push({ path: route.path, query: { view: tab.paneName } });
};
</script>
