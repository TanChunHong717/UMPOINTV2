<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="Date Range">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          unlink-panels
          range-separator="To"
          start-placeholder="Start date"
          end-placeholder="End date"
        />
      </el-form-item>
      <el-form-item label="Start Time">
        <el-time-select
          v-model="dataForm.startTime"
          placeholder="Start time"
          start="00:00"
          end="23:59"
          step="00:30"
        />
      </el-form-item>
      <el-form-item label="End Time">
        <el-time-select
          v-model="dataForm.endTime"
          placeholder="End time"
          :start="dataForm.startTime"
          end="23:59"
          step="00:30"
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
  const now = new Date();
  const startDate = new Date(event.start);
  const endDate = new Date(event.end);
  if (startDate.getTime() < now.getTime()) {
    cancelHandle();
    ElMessage.warning({
      message: 'Closure event can not be create in past',
      duration: 3000
    });
  }

  dateRange.value = [startDate, endDate];
  dataForm.startTime = roundToNearestHalfHour(new Date(startDate));
  dataForm.endTime = roundToNearestHalfHour(new Date(endDate));
}

const roundToNearestHalfHour = (date: Date): string => {
  const minutes = date.getMinutes();
  const roundedMinutes = Math.round(minutes / 30) * 30;
  date.setMinutes(roundedMinutes);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
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

  dataForm.startTime = removeSecond(dto.startTime);
  dataForm.endTime = removeSecond(dto.endTime);
}

const removeSecond = (timeString: any): any => {
  return timeString.substring(0, timeString.lastIndexOf(":"));
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

  dataForm.startTime = dataForm.startTime + ":00";
  dataForm.endTime = dataForm.endTime + ":00";

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
