<template>
  <el-dialog v-model="visible" :title="'Configure Booking Rule'" width="900" :close-on-click-modal="false" :close-on-press-escape="false" align-center>
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="300px">
      <el-form-item label="Manager" prop="manager">
        <el-select
          v-model="dataForm.manager"
          filterable
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

      <el-divider content-position="left">Permissions</el-divider>
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

      <el-divider content-position="left">Venue Time and Booking Window</el-divider>
      <el-form-item label="Open for booking after" prop="minBookingAdvanceDay">
        <el-col :span="14">
          <el-input-number v-model="dataForm.minBookingAdvanceDay" controls-position="right" style="width: 100%" :min="0"/>
        </el-col>
        <el-col :span="10" style="padding-left: 6px;">
          <span class="text-gray-500">days from present day</span>&nbsp;
          <el-tooltip
            class="box-item"
            placement="bottom-end"
          >
            <template #content>How many days after current day will the space be available for booking,<br> e.g. '3' means bookings cannot be made for the next 3 days</template>
            <el-button tabindex="-1" size="small" :icon="InfoFilled" circle />
          </el-tooltip>
        </el-col>
      </el-form-item>
      <el-form-item label="Allow booking until" prop="maxBookingAdvanceDay">
        <el-col :span="14">
          <el-input-number v-model="dataForm.maxBookingAdvanceDay" controls-position="right" style="width: 100%" :min="0"/>
        </el-col>
        <el-col :span="10" style="padding-left: 6px;">
          <span class="text-gray-500">days from present day</span>&nbsp;
          <el-tooltip
            class="box-item"
            placement="bottom-end"
          >
            <template #content>How far in the future a booking can be made,<br> e.g. '60' means bookings can be made up to 60 days from today</template>
            <el-button tabindex="-1" size="small" :icon="InfoFilled" circle />
          </el-tooltip>
        </el-col>
      </el-form-item>
      <el-form-item label="Available in public holiday (Include weekend)" prop="holidayAvailable">
        <el-radio-group v-model="dataForm.holidayAvailable">
          <el-radio :value="Number(1)">Yes</el-radio>
          <el-radio :value="Number(0)">No</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-divider content-position="left">Booking requirements</el-divider>
      <el-form-item label="Max reservation day" prop="maxReservationDay">
        <el-input-number v-model="dataForm.maxReservationDay" controls-position="right" :min="1"/>
      </el-form-item>
      <el-form-item label="Min reservation day" prop="minReservationDay">
        <el-input-number v-model="dataForm.minReservationDay" controls-position="right" :min="1"/>
      </el-form-item>
      <el-form-item label="Max technician number" prop="maxTechnicianNumber">
        <el-col :span="22">
          <el-input-number v-model="dataForm.maxTechnicianNumber" controls-position="right" style="width: 100%" :min="0"/>
        </el-col>
        <el-col :span="2" style="padding-left: 6px;">
          <el-tooltip
            class="box-item"
            placement="bottom-end"
          >
            <template #content>
              • Set to 0 if the space does not provide technicians.<br>
              • 1 technician is included in every booking by default.<br>
              • If the number of technicians is set to 1, users may not choose any additional technicians.
            </template>
            <el-button tabindex="-1" size="small" :icon="InfoFilled" circle />
          </el-tooltip>
        </el-col>
      </el-form-item>

      <el-divider content-position="left">Pricing</el-divider>
      <el-form-item label="Price for one day" prop="dayPrice">
        <el-input-number v-model="dataForm.dayPrice" controls-position="right" :precision="2" :step="0.5" :min="0"/>
      </el-form-item>
      <el-form-item label="Price for a week" prop="weekPrice">
        <el-input-number v-model="dataForm.weekPrice" controls-position="right" :precision="2" :step="0.5" :min="0"/>
      </el-form-item>
      <el-form-item label="Price per technician" prop="technicianPrice">
        <el-input-number v-model="dataForm.technicianPrice" controls-position="right" :precision="2" :step="0.5" :min="0"/>
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
import {InfoFilled} from "@element-plus/icons-vue";
const emit = defineEmits(["refreshData"]);

const visible = ref(false);
const dataFormRef = ref();
const userList = ref<{id: number; username: string}[]>([]);
const defaultBookingRule = ref();

const accommodationId = ref(null);
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
  maxBookingAdvanceDay: null,
  minBookingAdvanceDay: null,
  maxReservationDay: null,
  minReservationDay: null,
  maxTechnicianNumber: null,
  technicianPrice: null
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
  maxBookingAdvanceDay: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  minBookingAdvanceDay: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  maxReservationDay: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  minReservationDay: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  maxTechnicianNumber: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  technicianPrice: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ]
});

const getUserList = async () => {
  return baseService.get("/sys/user/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    userList.value = res.data;
  });
};

const init = (accommodation?: any) => {
  getUserList();

  visible.value = true;
  dataForm.id = null;

  if (dataFormRef.value)
    dataFormRef.value.resetFields();

  Object.assign(dataForm, accommodation, accommodation.accBookingRuleDTO);
  accommodationId.value = accommodation.id;
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    const accommodation = {
      id: accommodationId.value,
      bookingRuleId: dataForm.bookingRuleId,
      manager: dataForm.manager,
      dayPrice: dataForm.dayPrice,
      weekPrice: dataForm.weekPrice,
      accBookingRuleDTO: {
        id: dataForm.bookingRuleId,
        approvalRequired: dataForm.approvalRequired,
        holidayAvailable: dataForm.holidayAvailable,
        openForStaff: dataForm.openForStaff ?? 0,
        openForStudent: dataForm.openForStudent ?? 0,
        openForPublic: dataForm.openForPublic ?? 0,
        maxBookingAdvanceDay: dataForm.maxBookingAdvanceDay,
        minBookingAdvanceDay: dataForm.minBookingAdvanceDay,
        maxReservationDay: dataForm.maxReservationDay,
        minReservationDay: dataForm.minReservationDay,
        maxTechnicianNumber: dataForm.maxTechnicianNumber,
        technicianPrice: dataForm.technicianPrice
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
