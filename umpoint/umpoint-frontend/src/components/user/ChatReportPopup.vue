<template>
    <ElDialog
        v-model="isDialogVisible"
        title="Report User"
        @close="closeDialog"
    >
        <p>
            Please provide a reason for reporting user. The room will be closed
            after reporting.
        </p>
        <ElForm ref="formRef" :model="form" :rules="formRules" label-width="0">
            <ElFormItem prop="selectedReason">
                <ElSelect
                    v-model="form.selectedReason"
                    required
                    prop="selectedReason"
                    placeholder="Select a reason"
                >
                    <ElOption v-for="reason in reportReasons" :value="reason" />
                    <ElOption value="Other" />
                </ElSelect>
            </ElFormItem>

            <ElFormItem
                v-if="form.selectedReason === 'Other'"
                prop="typedReason"
            >
                <ElInput
                    v-model="form.typedReason"
                    required
                    placeholder="Please specify reason"
                />
            </ElFormItem>
        </ElForm>

        <template #footer>
            <ElButton @click="closeDialog">Cancel</ElButton>
            <ElButton type="primary" @click="submitReport">Submit</ElButton>
        </template>
    </ElDialog>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import {
    ElDialog,
    ElForm,
    ElSelect,
    ElOption,
    ElInput,
    ElButton,
    ElFormItem,
} from "element-plus";

const formRef = ref(null);
const form = reactive({
    selectedReason: null,
    typedReason: "",
});
const formRules = {
    selectedReason: [{ required: true, message: "Please select a reason" }],
    typedReason: [{ required: true, message: "Please specify a reason" }],
};
const emit = defineEmits(["close", "submit"]);

const reportReasons = [
    "Spam",
    "Harassment",
    "Impersonation",
    "Scamming or fraud",
    "Inappropriate behaviour",
    "Privacy violation",
];

const resetForm = () => {
    form.selectedReason = null;
    form.typedReason = "";
    if (formRef.value) {
        formRef.value.resetFields();
    }
};

// Control the visibility of the modal
const isDialogVisible = defineModel("visible", {
    defaultValue: false,
});
// Watch the visible prop to reset the form when the dialog is shown
watch(
    () => isDialogVisible.value,
    (newVal) => {
        if (newVal) {
            resetForm();
        }
    }
);

// dialog functions
const closeDialog = () => {
    emit("close");
};
const submitReport = () => {
    formRef.value.validate((valid) => {
        if (valid) {
            let reason =
                form.selectedReason === "Other"
                    ? form.typedReason
                    : form.selectedReason;
            emit("submit", reason);
            closeDialog(); // Close the dialog on submit
        }
    });
};
</script>
