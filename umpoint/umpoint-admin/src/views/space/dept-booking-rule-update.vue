<template>
  <el-dialog v-model="visible" :title="'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="240px">
      <el-form-item label="Department" prop="deptName">
        <el-input v-model="dataForm.deptName" disabled></el-input>
      </el-form-item>
      <el-form-item label="Maximum booking number" prop="maxUserDailyBooking">
        <el-col :span="22">
          <el-input-number v-model="dataForm.maxUserDailyBooking" controls-position="right" :min="0" style="width: 100%"/>
        </el-col>
        <el-col :span="2" style="padding-left: 6px;">
          <el-tooltip
            class="box-item"
            placement="bottom-end"
          >
            <template #content>Maximum booking number allow for each user in a day (0 mean no restrict)</template>
            <el-button tabindex="-1" size="small" :icon="InfoFilled" circle />
          </el-tooltip>
        </el-col>
      </el-form-item>
      <el-form-item label="Maximum booking hour" prop="maxUserDailyBookingHour">
        <el-col :span="22">
          <el-input-number v-model="dataForm.maxUserDailyBookingHour" controls-position="right" :min="0" style="width: 100%"/>
        </el-col>
        <el-col :span="2" style="padding-left: 6px;">
          <el-tooltip
            class="box-item"
            placement="bottom-end"
          >
            <template #content>Maximum booking hour allow for each user in a day (0 mean no restrict)</template>
            <el-button tabindex="-1" size="small" :icon="InfoFilled" circle />
          </el-tooltip>
        </el-col>
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
import {IObject} from "@/types/interface";
import {InfoFilled} from "@element-plus/icons-vue";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  id: '',
  deptId: '',
  maxUserDailyBooking: '',
  maxUserDailyBookingHour: '',
  deptName: ''
});

const rules = ref({
  deptName: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  maxUserDailyBooking: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  maxUserDailyBookingHour: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ]
});

const init = (id: any, deptId: any, name: any) => {
  visible.value = true;
  dataForm.id = "";

  // Reset form data
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (id) {
    getInfo(id, deptId, name);
  } else {
    dataForm.deptId = deptId;
    dataForm.deptName = name;
  }
};

// Retrieve information
const getInfo = (id: any, deptId: any, name: any) => {
  baseService.get("/space/dept-booking-rule/" + id).then((res) => {
    Object.assign(dataForm, res.data);
    dataForm.deptId = deptId;
    dataForm.deptName = name;
  });
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/space/dept-booking-rule", dataForm).then((res) => {
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
