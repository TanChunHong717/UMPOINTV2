<script setup>
import { computed } from "vue";
import EventInfo from "./EventInfo.vue";

const props = defineProps(["eventInfo", "pricingDetails"]);
const emit = defineEmits(["previousStep", "submitForm"]);
defineOptions({
    inheritAttrs: false,
});

/**
 * STEP 3: ORDER CONFIRMATION
 * */
function formatCurrency(row) {
    return row.price.toFixed(2);
}

const totalPrice = computed(() => {
    return props.pricingDetails.reduce((acc, item) => {
        return acc + item.total;
    }, 0);
});

// form submit
async function submitForm() {
    emit("submitForm");
}
</script>

<template>
    <EventInfo :event-info="props.eventInfo" />

    <div>
        <el-divider content-position="left">
            <h2>Order Confirmation</h2>
        </el-divider>

        <el-table :data="props.pricingDetails">
            <el-table-column type="index" label="#" />
            <el-table-column prop="item" label="Item" />
            <el-table-column prop="amount" label="Amount" width="80" />
            <el-table-column
                prop="price"
                label="Price (RM)"
                align="right"
                min-width="35%"
                :formatter="formatCurrency"
            />
            <el-table-column
                prop="total"
                label="Total (RM)"
                align="right"
                min-width="35%"
                :formatter="formatCurrency"
            />
        </el-table>

        <div class="total-price">
            <el-text style="margin-inline-end: 10%;">Total:</el-text>
            <el-text size="large" tag="strong">RM {{ totalPrice.toFixed(2) }}</el-text>
        </div>

        <div class="end-buttons">
            <el-button plain @click="emit('previousStep')">
                Previous
            </el-button>
            <el-button type="primary" @click="submitForm">
                Confirm Booking
            </el-button>
        </div>
    </div>
</template>

<style scoped>
.el-upload__tip {
    margin-top: 0;
    line-height: 1.2;
}

.total-price {
    display: flex;
    justify-content: flex-end;
    padding: 1em 12px;
}
</style>
