<template>
    <el-select v-model="selectedId" :clearable="true">
        <el-option
            v-for="item in tagsOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
    </el-select>
</template>
<script setup>
import { ref, watch } from "vue";
import {getFacilityTags} from "@/helpers/api-facility.js";

const selectedId = defineModel({
    prop: "modelValue",
    event: "change",
});
const props = defineProps({
    tag: String,
});

watch(
    () => props.tag,
    (tag) => {
        if (tag) updateTags(tag);
    },
    { immediate: true }
);

// tag dropdown
const tagsOptions = ref([]);
function updateTags(tag) {
    getFacilityTags(tag).then((res) => {
      tagsOptions.value = res.data.data.map((item) => ({
            label: item.name,
            value: item.id,
        }));
    });
}
</script>
