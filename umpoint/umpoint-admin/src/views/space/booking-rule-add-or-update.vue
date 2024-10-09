<template>
  <el-dialog v-model="visible" :title="'Configure Booking Rule'" width="900" :close-on-click-modal="false" :close-on-press-escape="false" align-center>
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="300px">
      <el-form-item label="Manager" prop="manager">
        <el-select
          v-model="dataForm.manager"
          placeholder="Select manager"
        >
          <el-option
            v-for="user in userList"
            :key="user.id"
            :label="user.username"
            :value="user.id"
          />
        </el-select>
      </el-form-item>
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
      <el-form-item label="Price for an hour" prop="hourPrice">
        <el-input-number v-model="dataForm.hourPrice" controls-position="right" :precision="2" :step="0.5" :min="0"/>
      </el-form-item>
      <el-form-item label="Price for 4 hours" prop="fourHoursPrice">
        <el-input-number v-model="dataForm.fourHoursPrice" controls-position="right" :precision="2" :step="0.5" :min="0"/>
      </el-form-item>
      <el-form-item label="Price for one day" prop="dayPrice">
        <el-input-number v-model="dataForm.dayPrice" controls-position="right" :precision="2" :step="0.5" :min="0"/>
      </el-form-item>
      <el-form-item label="Open days prior booking" prop="closeDaysBeforeBooking">
        <el-input-number v-model="dataForm.openDaysPriorBooking" controls-position="right" :min="0"/>
      </el-form-item>
      <el-form-item label="Close days after booking" prop="closeDaysAfterBooking">
        <el-input-number v-model="dataForm.closeDaysAfterBooking" controls-position="right" :min="0"/>
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
      <el-button type="info" @click="Object.assign(dataForm, defaultBookingRule)">Apply Default Booking Rule</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">Confirm</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshData"]);

const visible = ref(false);
const dataFormRef = ref();
const userList = ref<{id: number; username: string}[]>([]);
const defaultBookingRule = ref();

const dataForm = reactive({
  id: null,
  bookingRuleId: null,
  manager: null,
  approvalRequired: 1,
  openForStaff: null,
  openForStudent: null,
  openForPublic: null,
  holidayAvailable: null,
  startTime: null,
  endTime: null,
  hourPrice: null,
  fourHoursPrice: null,
  dayPrice: null,
  openDaysPriorBooking: null,
  closeDaysAfterBooking: null,
  maxReservationDays: null,
  minBookingHours: null
});

const rules = ref({
  manager: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
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
  hourPrice: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  openDaysPriorBooking: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  closeDaysAfterBooking: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  maxReservationDays: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  minBookingHours: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ]
});

const getDefaultBookingRule = async () => {
  return baseService.get("/space/booking-rule/default").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    defaultBookingRule.value = res.data;
  });
}

const getUserList = async () => {
  return baseService.get("/sys/user/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    userList.value = res.data;
  });
};

const timeStringToDate = (timeString:any):any => {
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

const dateToTimeString = (date:any):any => {
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${hours}:${minutes}:${seconds}`;
}

const init = (space?: any) => {
  getDefaultBookingRule();
  getUserList();

  visible.value = true;
  dataForm.id = null;

  if (dataFormRef.value)
    dataFormRef.value.resetFields();

  Object.assign(dataForm, space, space.spcBookingRuleDTO);
  dataForm.id = space.id;
  dataForm.bookingRuleId = space.bookingRuleId;
  dataForm.startTime = timeStringToDate(dataForm.startTime);
  dataForm.endTime = timeStringToDate(dataForm.endTime);
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    const space = {
      id: dataForm.id,
      bookingRuleId: dataForm.bookingRuleId,
      manager: dataForm.manager,
      hourPrice: dataForm.hourPrice,
      fourHoursPrice: dataForm.fourHoursPrice,
      dayPrice: dataForm.dayPrice,
      spcBookingRuleDTO: {
        id: dataForm.bookingRuleId,
        approvalRequired: dataForm.approvalRequired,
        openForStaff: dataForm.openForStaff,
        openForStudent: dataForm.openForStudent,
        openForPublic: dataForm.openForPublic,
        holidayAvailable: dataForm.holidayAvailable,
        startTime: dateToTimeString(dataForm.startTime),
        endTime: dateToTimeString(dataForm.endTime),
        openDaysPriorBooking: dataForm.openDaysPriorBooking,
        closeDaysAfterBooking: dataForm.closeDaysAfterBooking,
        maxReservationDays: dataForm.maxReservationDays,
        minBookingHours: dataForm.minBookingHours
      }
    };
    baseService.put("/space/space", space).then((res) => {
      ElMessage.success({
        message: 'Success',
        duration: 500,
        onClose: () => {
          visible.value = false;
          emit("refreshData");
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
