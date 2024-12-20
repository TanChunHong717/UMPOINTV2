<template>
    <el-select v-model="selectedId" :clearable="true">
        <el-option
            v-for="item in categoriesOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
    </el-select>
</template>
<script setup>
import { ref, watch } from "vue";
import { getFacilityCategories } from "@/helpers/api-facility";

const selectedId = defineModel({
    prop: "modelValue",
    event: "change",
});
const props = defineProps({
    facilityType: String,
});

watch(
    () => props.facilityType,
    (facilityType) => {
        if (facilityType) updateCategories(facilityType);
    },
    { immediate: true }
);

// category dropdown
const categoriesOptions = ref([]);
function updateCategories(facilityType) {
    getFacilityCategories(facilityType).then((res) => {
        categoriesOptions.value = res.data.data.map((item) => ({
            label: item.name,
            value: item.id,
        }));
    });
}
</script>
