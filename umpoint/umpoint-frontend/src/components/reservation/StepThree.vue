<script setup>
import { computed } from "vue";
import EventInfo from "./EventInfo.vue";
import { itemiseDailyEventPrices } from "@/helpers/pricing";

const props = defineProps(["formData", "facilityInfo"]);
const emit = defineEmits(["previousStep", "submitForm"]);
defineOptions({
    inheritAttrs: false,
});

/**
 * STEP 3: ORDER CONFIRMATION
 * */
const pricingDetails = computed(() => {
    let itemisedPrice = itemiseDailyEventPrices(
        props.facilityInfo,
        props.formData.startDate,
        props.formData.endDate,
        props.formData.startTime,
        props.formData.endTime,
    );
    if (props.facilityInfo.bookingRule?.maxTechnicianNumber > 0) {
        let technicianCount = (props.formData.additionalTechnicians ?? 0) + 1;
        itemisedPrice.push({
            item: "Technician",
            amount: technicianCount,
            price: props.facilityInfo.bookingRule.technicianPrice,
            total:
                technicianCount *
                props.facilityInfo.bookingRule.technicianPrice,
        });
    }

    return itemisedPrice;
});
function formatCurrency(row) {
    return row.price.toFixed(2);
}

const totalPrice = computed(() => {
    return pricingDetails.value.reduce((acc, item) => {
        return acc + item.total;
    }, 0);
});

// form submit
async function submitForm() {
    emit("submitForm");
}
</script>

<template>
    <EventInfo :formData="props.formData" />

    <div>
        <el-divider content-position="left">
            <h2>Order Confirmation</h2>
        </el-divider>

        <el-table :data="pricingDetails">
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
