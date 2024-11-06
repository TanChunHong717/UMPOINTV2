<template>
  <el-dialog v-model="visible" :title="'Configure Default Booking Rule'" width="900" :close-on-click-modal="false" :close-on-press-escape="false" align-center>
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="300px">
      <el-divider content-position="left">Permissions</el-divider>
      <el-form-item label="Prior Contact with Admin" prop="contactRequired">
        <el-radio-group v-model="dataForm.contactRequired">
          <el-radio :value="Number(1)">Required</el-radio>
          <el-radio :value="Number(0)">Not Needed</el-radio>
          <el-tooltip
            class="box-item"
            placement="bottom-end"
          >
            <template #content>
              Only used to inform if the user needs to contact the manager before booking. <br>
              No checks are performed during booking.
            </template>
            <el-button tabindex="-1" size="small" :icon="InfoFilled" circle />
          </el-tooltip>
        </el-radio-group>
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

      <el-divider content-position="left">Venue Time and Booking Window</el-divider>
      <el-form-item label="Booking Mode">
        <el-radio-group v-model="dataForm.bookingMode">
          <el-radio label="Free time selection" :value="0">Free time selection</el-radio>
          <el-radio label="Limited to preset slots" :value="1">Limited to preset slots</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="Booking Unit" prop="bookingUnit">
        <el-col :span="14">
          <el-input-number v-model="dataForm.bookingUnit" controls-position="right" :min="1" style="width: 100%"/>
        </el-col>
        <el-col :span="10" style="padding-left: 6px;">
          <el-tooltip
            class="box-item"
            placement="bottom-end"
          >
            <template #content>Minimum increment for booking duration, e.g., 30 means bookings can be made in 30-minute intervals</template>
            <el-button tabindex="-1" size="small" :icon="InfoFilled" circle />
          </el-tooltip>
        </el-col>
      </el-form-item>
      <el-form-item label="Start time" prop="startTime">
        <el-time-select
          v-model="dataForm.startTime"
          placeholder="Start time"
          start="00:00"
          end="23:59"
          step="00:30"
        />
      </el-form-item>
      <el-form-item label="End time" prop="endTime">
        <el-time-select
          v-model="dataForm.endTime"
          placeholder="End time"
          :start="dataForm.startTime"
          end="23:59"
          :step="getEndTimeStep()"
        />
      </el-form-item>
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
      <el-form-item label="Max booking hour" prop="maxBookingHour">
        <el-input-number v-model="dataForm.maxBookingHour" controls-position="right" :min="0.5" :step="0.5"/>
      </el-form-item>
      <el-form-item label="Min booking hour" prop="minBookingHour">
        <el-input-number v-model="dataForm.minBookingHour" controls-position="right" :min="0.5" :step="0.5"/>
      </el-form-item>
      <el-form-item label="Technicians available" prop="maxTechnicianNumber">
        <el-col :span="2" :xs="4">
          <el-switch
            v-model="hasTechnician"
            inline-prompt
            :active-icon="Check"
            :inactive-icon="Close"
          />
        </el-col>
        <el-col :span="20" :xs="18" offset="1">
          <el-input-number v-if="hasTechnician" v-model="dataForm.maxTechnicianNumber" controls-position="right" style="width: 100%" :min="0"/>
        </el-col>
        <el-col :span="1" style="padding-left: 6px;">
          <el-tooltip
            class="box-item"
            placement="bottom-end"
          >
            <template #content>
              Each booking includes 1 technician by default if technicians are provided. If set to 1, users cannot add any additional technicians.
            </template>
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
import {computed, reactive, ref} from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
import {Check, Close, InfoFilled} from "@element-plus/icons-vue";
const emit = defineEmits(["refreshDataList"]);

const idList = ref([]);
const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  contactRequired: null,
  approvalRequired: null,
  openForStaff: null,
  openForStudent: null,
  openForPublic: null,
  holidayAvailable: null,
  bookingMode: null,
  startTime: null,
  endTime: null,
  bookingUnit: null,
  maxBookingAdvanceDay: null,
  minBookingAdvanceDay: null,
  maxReservationDay: null,
  minReservationDay: null,
  maxBookingHour: null,
  minBookingHour: null,
  maxTechnicianNumber: 0
});

const rules = ref({
  contactRequired: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  approvalRequired: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  holidayAvailable: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  bookingMode: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  startTime: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  endTime: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  bookingUnit: [
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
  maxBookingHour: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  minBookingHour: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ],
  maxTechnicianNumber: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
  ]
});

const addSecond = (timeString: any): any => {
  return timeString + ':00';
}

const getEndTimeStep = () => {
  if (dataForm.bookingMode == 1 && dataForm.bookingUnit) {
    let hour = Math.floor(dataForm.bookingUnit / 60);
    let minute = dataForm.bookingUnit % 60;

    const formattedHour = hour.toString().padStart(2, '0');
    const formattedMinute = minute.toString().padStart(2, '0');

    return `${formattedHour}:${formattedMinute}`;
  }
  return "00:30";
};

// switch to set technicians available
const hasTechnician = computed({
  get: () => dataForm.maxTechnicianNumber > 0,
  set: (hasTechnicianValue) => {
    // only set if maxTechnicianNumber is not set
    if (hasTechnicianValue && !dataForm.maxTechnicianNumber) dataForm.maxTechnicianNumber = 1;
    // no technicians available, set to 0
    else dataForm.maxTechnicianNumber = 0;
  }
});

const init = (dataListSelections: any) => {
  if (dataListSelections) {
    visible.value = true;
    idList.value = dataListSelections.map((data: any)  => data.bookingRuleId);
  }

  if (dataFormRef.value)
    dataFormRef.value.resetFields();
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    if (dataForm.bookingMode == 1) {
      const start = new Date(`1970-01-01T${dataForm.startTime}:00`);
      const end = new Date(`1970-01-01T${dataForm.endTime}:00`);

      const diffInMin = (end.getTime() - start.getTime()) / 60000;
      if (!dataForm.bookingUnit || diffInMin % dataForm.bookingUnit != 0) {
        ElMessage.error("The diff between start time and end time must be multiple of booking unit.");
        return false;
      }
    }
    dataForm.startTime = addSecond(dataForm.startTime);
    dataForm.endTime = addSecond(dataForm.endTime);
    const idListString = idList.value.join(',');
    baseService.put(`/space/booking-rule?idList=${idListString}`, dataForm).then((res) => {
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
