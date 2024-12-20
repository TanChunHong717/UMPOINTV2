<template>
    <el-tree-select
        v-model="selectedId"
        :data="departmentsOptions"
        check-strictly
        :render-after-expand="false"
        :clearable="true"
    />
</template>

<script setup>
import { onMounted, ref } from "vue";
import { getDepartments } from "@/helpers/api-facility";

const selectedId = defineModel({
    prop: "modelValue",
    event: "change",
});

// facility dropdown
const departmentsOptions = ref([]);
function updateDepartments() {
    getDepartments().then((res) => {
        departmentsOptions.value = transformToTreeStructure(
            res.data.data[0]
        ).children;
    });
}
function transformToTreeStructure(obj) {
    // Create a new object with the transformed keys
    const transformedObj = { ...obj };

    // Recursively traverse and update each node
    if (transformedObj.children && Array.isArray(transformedObj.children)) {
        transformedObj.children = transformedObj.children.map((child) =>
            transformToTreeStructure(child)
        );
    }

    // Replace 'name' with 'label' and 'id' with 'value'
    if (transformedObj.name) {
        transformedObj.label = transformedObj.name;
    }
    if (transformedObj.id) {
        transformedObj.value = transformedObj.id;
    }

    return transformedObj;
}
onMounted(() => {
    updateDepartments();
});
</script>
