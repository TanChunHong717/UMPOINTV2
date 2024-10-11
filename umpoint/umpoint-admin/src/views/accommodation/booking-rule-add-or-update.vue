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
      <el-form-item label="Price for one day" prop="dayPrice">
        <el-input-number v-model="dataForm.dayPrice" controls-position="right" :precision="2" :step="0.5" :min="0"/>
      </el-form-item>
      <el-form-item label="Price for a week" prop="weekPrice">
        <el-input-number v-model="dataForm.weekPrice" controls-position="right" :precision="2" :step="0.5" :min="0"/>
      </el-form-item>
      <el-form-item label="Open days prior booking" prop="closeDaysBeforeBooking">
        <el-input-number v-model="dataForm.openDaysPriorBooking" controls-position="right" :min="0"/>
      </el-form-item>
      <el-form-item label="Close days before booking" prop="closeDaysBeforeBooking">
        <el-input-number v-model="dataForm.closeDaysBeforeBooking" controls-position="right" :min="0"/>
      </el-form-item>
      <el-form-item label="Max reservation days" prop="maxReservationDays">
        <el-input-number v-model="dataForm.maxReservationDays" controls-position="right" :min="1"/>
      </el-form-item>
      <el-form-item label="Min reservation days" prop="minBookingDays">
        <el-input-number v-model="dataForm.minBookingDays" controls-position="right" :min="1"/>
      </el-form-item>
      <el-form-item label="Max technician number" prop="maxTechnicianNumber">
        <el-input-number v-model="dataForm.maxTechnicianNumber" controls-position="right" :min="1"/>
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
  dayPrice: null,
  weekPrice: null,
  openDaysPriorBooking: null,
  closeDaysBeforeBooking: null,
  maxReservationDays: null,
  minBookingDays: null,
  maxTechnicianNumber: null
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
  dayPrice: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  openDaysPriorBooking: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  closeDaysBeforeBooking: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  maxReservationDays: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  minBookingDays: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  maxTechnicianNumber: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ]
});

const getDefaultBookingRule = async () => {
  return baseService.get("/accommodation/booking-rule/default").then((res) => {
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

const init = (accommodation?: any) => {
  getDefaultBookingRule();
  getUserList();

  visible.value = true;
  dataForm.id = null;

  if (dataFormRef.value)
    dataFormRef.value.resetFields();

  Object.assign(dataForm, accommodation, accommodation.accBookingRuleDTO);
  dataForm.id = accommodation.id;
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    const accommodation = {
      id: dataForm.id,
      bookingRuleId: dataForm.bookingRuleId,
      manager: dataForm.manager,
      dayPrice: dataForm.dayPrice,
      weekPrice: dataForm.weekPrice,
      accBookingRuleDTO: {
        id: dataForm.bookingRuleId,
        approvalRequired: dataForm.approvalRequired,
        openForStaff: dataForm.openForStaff,
        openForStudent: dataForm.openForStudent,
        openForPublic: dataForm.openForPublic,
        openDaysPriorBooking: dataForm.openDaysPriorBooking,
        closeDaysBeforeBooking: dataForm.closeDaysBeforeBooking,
        maxReservationDays: dataForm.maxReservationDays,
        minBookingHours: dataForm.minBookingHours,
        maxTechnicianNumber: dataForm.maxTechnicianNumber
      }
    };
    baseService.put("/accommodation/accommodation", accommodation).then((res) => {
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
