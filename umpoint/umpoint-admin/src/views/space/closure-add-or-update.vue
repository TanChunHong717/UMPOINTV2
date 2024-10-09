<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="Date Range" prop="startDay">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          unlink-panels
          range-separator="To"
          start-placeholder="Start date"
          end-placeholder="End date"
        />
      </el-form-item>
      <el-form-item label="Time Range" prop="startTime">
        <el-time-picker
          v-model="timeRange"
          is-range
          :disabled-minutes="disabledMinutes"
          :disabled-seconds="disabledSeconds"
          range-separator="To"
          start-placeholder="Start time"
          end-placeholder="End time"
        />
      </el-form-item>
      <el-form-item label="Recurring" prop="recur">
        <el-checkbox
          v-model="checkAll"
          @change="handleCheckAllChange"
        >
          Check all
        </el-checkbox>
        <el-checkbox-group
          v-model="checkedDays"
          @change="handleCheckedDaysChange"
        >
          <el-checkbox v-for="day in week" :key="day" :label="day" :value="day">
            {{ day }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-row justify="end" style="padding-bottom: 10px">
        <el-col class="warning-text" span="24">
          Warning: All conflict booking will be reject
        </el-col>
      </el-row>
      <el-row justify="end">
        <el-col span="14">
          <el-button type="danger" @click="deleteHandle()" v-if="dataForm.id != '' && state.hasPermission('space:closure:delete')">Delete</el-button>
          <el-button @click="cancelHandle()">Cancel</el-button>
          <el-button
            type="primary"
            @click="dataFormSubmitHandle()"
            v-if="
            (state.hasPermission('space:closure:save') && dataForm.id == '') ||
            (state.hasPermission('space:closure:update') && dataForm.id != '')"
          >Confirm</el-button>
        </el-col>
      </el-row>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {reactive, ref, toRefs} from "vue";
import baseService from "@/service/baseService";
import {ElMessage} from "element-plus";
import {formatDateToDateStr, formatDateToTimeStr} from "@/utils/date";
import useView from "@/hooks/useView";

const emit = defineEmits(["refreshData", "cancel"]);

const view = reactive({});
const state = reactive({ ...useView(view), ...toRefs(view) });

let event: any;
let spaceId: any;
const visible = ref(false);
const dateRange = ref<[Date, Date]>([new Date(), new Date()]);
const timeRange = ref<[Date, Date]>([new Date(), new Date()]);
const checkAll = ref(true);
const week = ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'];
const checkedDays = ref(week);
const dataFormRef = ref();

const dataForm: any = reactive({
  id: '',
  spaceId: '',
  startDay: '',
  endDay: '',
  startTime: '',
  endTime: '',
  recurMonday: '',
  recurTuesday: '',
  recurWednesday: '',
  recurThursday: '',
  recurFriday: '',
  recurSaturday: '',
  recurSunday: ''
});

const handleCheckAllChange = (val: boolean) => {
  checkedDays.value = week;
  checkAll.value = true;
}
const handleCheckedDaysChange = (value: string[]) => {
  const checkedCount = value.length
  if (checkedCount == 0) {
    checkedDays.value = week;
    checkAll.value = true;
  } else  {
    checkAll.value = checkedCount === week.length
  }
}

const makeRange = (start: number, end: number) => {
  const result: number[] = []
  for (let i = start; i <= end; i++) {
    result.push(i)
  }
  return result
}
const disabledMinutes = (hour: number) => {
  return makeRange(1,29).concat(makeRange(31,59));
}
const disabledSeconds = (hour: number, minute: number) => {
  return makeRange(1,29).concat(makeRange(31,59));
}

const init = (id: number, e: any, sid: number) => {
  visible.value = true;
  dataForm.id = "";

  spaceId = sid;
  event = e;

  if (id)
    getInfo(id);
  else
    setDateAndTimeFromEvent();
};

const setDateAndTimeFromEvent = () => {
  const startDate = new Date(event.start);
  const endDate = new Date(event.end);
  dateRange.value = [startDate, endDate];

  const roundedStartDate = roundToNearestHalfHour(new Date(startDate));
  const roundedEndDate = roundToNearestHalfHour(new Date(endDate));
  timeRange.value = [roundedStartDate, roundedEndDate];
}

const roundToNearestHalfHour = (date: Date): Date => {
  const minutes = date.getMinutes();
  const roundedMinutes = Math.round(minutes / 30) * 30;
  date.setMinutes(roundedMinutes);
  date.setSeconds(0);
  date.setMilliseconds(0);
  return date;
};

const getInfo = (id: number) => {
  baseService.get("/space/closure/" + id).then((res) => {
    Object.assign(dataForm, res.data);
    setDateAndTimeFromDTO(res.data);
  });
};

const setDateAndTimeFromDTO = (dto: any) => {
  const startDay = new Date(dto.startDay);
  const endDay = new Date(dto.endDay);
  dateRange.value = [startDay, endDay];

  const startParsedTime = parseTimeString(dto.startTime);
  startDay.setHours(startParsedTime.hours);
  startDay.setMinutes(startParsedTime.minutes);
  startDay.setSeconds(startParsedTime.seconds)

  const endParsedTime = parseTimeString(dto.endTime);
  endDay.setHours(endParsedTime.hours);
  endDay.setMinutes(endParsedTime.minutes);
  endDay.setSeconds(endParsedTime.seconds)

  timeRange.value = [startDay, endDay];
}

const parseTimeString = (timeString: string) => {
  const [hours, minutes, seconds] = timeString.split(':').map(Number);
  return { hours, minutes, seconds };
}

const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    updateDataForm();
    (!dataForm.id ? baseService.post : baseService.put)("/space/closure", dataForm).then((res) => {
      ElMessage.success({
        message: 'Success',
        duration: 500,
        onClose: () => {
          visible.value = false;
          emit("refreshData", event);
        }
      });
    });
  });
};

const updateDataForm = () => {
  if (dateRange.value.length === 2) {
    const [startDate, endDate] = dateRange.value;
    dataForm.startDay = formatDateToDateStr(startDate);
    dataForm.endDay = formatDateToDateStr(endDate);
  }

  if (timeRange.value.length === 2) {
    const [startDateTime, endDateTime] = timeRange.value;
    dataForm.startTime = formatDateToTimeStr(startDateTime);
    dataForm.endTime = formatDateToTimeStr(endDateTime);
  }

  week.forEach(day => {
    dataForm[`recur${day}`] = checkedDays.value.includes(day) ? 1 : 0;
  });

  if (!dataForm.id)
    dataForm.spaceId = spaceId;
}

const deleteHandle = () => {
  baseService.delete("/space/closure/" + dataForm.id, {}).then((res) => {
    ElMessage.success({
      message: 'Success',
      duration: 500,
      onClose: () => {
        visible.value = false;
        emit("refreshData", event);
      }
    });
  });
}

const cancelHandle = () => {
  visible.value = false;
  emit("cancel", event);
}

defineExpose({
  init
});
</script>
<style>
.warning-text {
  padding-top: 15px;
  color: #c24545;
}
</style>
