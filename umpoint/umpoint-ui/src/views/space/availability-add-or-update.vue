<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="Space ID" prop="spaceId">
        <el-input v-model="dataForm.spaceId" placeholder="Space ID"></el-input>
      </el-form-item>
          <el-form-item label="Year" prop="year">
        <el-input v-model="dataForm.year" placeholder="Year"></el-input>
      </el-form-item>
          <el-form-item label="Availability of space, consist of 366*24 bit, 1 represent available in specific hour in one year" prop="availability">
        <el-input v-model="dataForm.availability" placeholder="Availability of space, consist of 366*24 bit, 1 represent available in specific hour in one year"></el-input>
      </el-form-item>
      </el-form>
    <template #footer>
      <el-button @click="visible = false">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">Confirm</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  id: '',  spaceId: '',  year: '',  availability: ''});

const rules = ref({
          spaceId: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          year: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          availability: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ]
  });

const init = (id?: number) => {
  visible.value = true;
  dataForm.id = "";

  // Reset form data
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (id) {
    getInfo(id);
  }
};

// Retrieve information
const getInfo = (id: number) => {
  baseService.get("/space/availability/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/space/availability", dataForm).then((res) => {
      ElMessage.success({
        message: 'Success',
        duration: 500,
        onClose: () => {
          visible.value = false;
          emit("refreshDataList");
        }
      });
    });
  });
};

defineExpose({
  init
});
</script>
