<template>
  <el-dialog v-model="visible" :title="'Configure Default Booking Rule'" width="700" :close-on-click-modal="false" :close-on-press-escape="false" align-center>
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="180px">
      <el-form-item label="Required Approve" prop="approvalRequired">
        <el-radio-group v-model="dataForm.approvalRequired">
          <el-radio :value="Number(1)">Require Admin Approve</el-radio>
          <el-radio :value="Number(0)">Automatic Approve</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="Open Booking">
        <div>
          <el-checkbox label="Staff" v-model="dataForm.openForStaff" :true-value="Number(1)" :false-value="Number(0)"/>
          <el-checkbox label="Student" v-model="dataForm.openForStudent" :true-value="Number(1)" :false-value="Number(0)"/>
          <el-checkbox label="Public" v-model="dataForm.openForPublic" :true-value="Number(1)" :false-value="Number(0)"/>
        </div>
      </el-form-item>
      <el-form-item label="Available in Public Holiday(Include weekend)" prop="holidayAvailable">
        <el-radio-group v-model="dataForm.holidayAvailable">
          <el-radio :value="Number(1)">Yes</el-radio>
          <el-radio :value="Number(0)">No</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="Start time" prop="startTime">
        <el-time-picker
          v-model="dataForm.startTime"
          placeholder="Start time"
        />
      </el-form-item>
      <el-form-item label="End time" prop="endTime">
        <el-time-picker
          v-model="dataForm.endTime"
          placeholder="End time"
        />
      </el-form-item>
      <el-form-item label="Close days before event" prop="closeDaysBeforeEvent">
        <el-input-number v-model="dataForm.closeDaysBeforeEvent" controls-position="right" :min="0"/>
      </el-form-item>
      <el-form-item label="Close days after event" prop="closeDaysAfterEvent">
        <el-input-number v-model="dataForm.closeDaysAfterEvent" controls-position="right" :min="0"/>
      </el-form-item>
      <el-form-item label="Max reservation days" prop="maxReservationDays">
        <el-input-number v-model="dataForm.maxReservationDays" controls-position="right" :min="1"/>
      </el-form-item>
      <el-form-item label="Min booking hours" prop="minBookingHours">
        <el-input-number v-model="dataForm.minBookingHours" controls-position="right" :min="1"/>
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
  id: 0,
  approvalRequired: 1,
  openForStaff: null,
  openForStudent: null,
  openForPublic: null,
  holidayAvailable: null,
  startTime: null,
  endTime: null,
  closeDaysBeforeEvent: null,
  closeDaysAfterEvent: null,
  maxReservationDays: null,
  minBookingHours: null
});

const rules = ref({
  approvalRequired: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  holidayAvailable: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  startTime: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  endTime: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  closeDaysBeforeEvent: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  closeDaysAfterEvent: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  maxReservationDays: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  minBookingHours: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ]
});

const timeStringToDate = (timeString: any):any => {
  if (!timeString)
    return null;

  const [hours, minutes, seconds] = timeString.split(':').map(Number);
  if (
    isNaN(hours) || hours < 0 || hours > 23 ||
    isNaN(minutes) || minutes < 0 || minutes > 59 ||
    isNaN(seconds) || seconds < 0 || seconds > 59
  )
    return null;

  const date = new Date();
  date.setHours(hours, minutes, seconds, 0); // Set hours, minutes, and seconds
  return date;
}

const dateToTimeString = (date: any):any => {
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${hours}:${minutes}:${seconds}`;
}

const getInfo = () => {
  baseService.get("/space/booking-rule/default").then((res) => {
    Object.assign(dataForm, res.data);
    dataForm.startTime = timeStringToDate(dataForm.startTime);
    dataForm.endTime = timeStringToDate(dataForm.endTime);
  });
};

const init = () => {
  visible.value = true;

  if (dataFormRef.value)
    dataFormRef.value.resetFields();

  getInfo()
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    dataForm.startTime = dateToTimeString(dataForm.startTime);
    dataForm.endTime = dateToTimeString(dataForm.endTime);
    baseService.put("/space/booking-rule/default", dataForm).then((res) => {
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
<style>
div.el-form-item__content > div {
  width: 100% !important;
}
</style>
