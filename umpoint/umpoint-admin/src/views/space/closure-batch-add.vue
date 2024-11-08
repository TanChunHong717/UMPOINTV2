<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="Space">
        <el-select
          v-model="idList"
          placeholder="Select space"
          multiple
          :multiple-limit="10"
        >
          <el-option
            v-for="space in spaceList"
            :key="space.id"
            :label="space.name"
            :value="space.id"
          />
        </el-select>
      </el-form-item>
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
      <el-form-item label="Recurring">
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
          <el-button @click="cancelHandle()">Cancel</el-button>
          <el-button
            type="primary"
            @click="dataFormSubmitHandle()"
            v-if="state.hasPermission('space:closure:save')"
          >Confirm</el-button>
        </el-col>
      </el-row>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref, toRefs} from "vue";
import baseService from "@/service/baseService";
import {ElMessage} from "element-plus";
import {formatDateToDateStr} from "@/utils/date";
import useView from "@/hooks/useView";
import SockJS from "sockjs-client";
import {Client} from "@stomp/stompjs";
import {useAppStore} from "@/store";
import app from "@/constants/app";

const emit = defineEmits(["refreshData", "cancel"]);

const store = useAppStore();
const view = reactive({});
const state = reactive({ ...useView(view), ...toRefs(view) });

const idList = ref([]);
const spaceList = ref<{id: number; name: string}[]>([]);
const visible = ref(false);
const dateRange = ref([]);
const checkAll = ref(true);
const week = ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'];
const checkedDays = ref(week);
const dataFormRef = ref();

const dataForm: any = reactive({
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

const getSpaceList = async () => {
  return baseService.get("/space/space/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    spaceList.value = res.data;
  });
};

const init = () => {
  getSpaceList();
  visible.value = true;
};

const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    updateDataForm();
    const idListString = idList.value.join(',');
    baseService.post(`/space/closure/batch?spaceIdList=${idListString}`, dataForm).then((res) => {
      visible.value = false;
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
}

const cancelHandle = () => {
  visible.value = false;
  idList.value = [];
  Object.keys(dataForm).forEach((key) => {
    (dataForm as any)[key] = '';
  });
}

defineExpose({
  init
});

const connect = () => {
  const socketUrl = app.api +  "/ws";

  const socket = new SockJS(socketUrl);
  const client = new Client({
    webSocketFactory: () => socket,
    connectHeaders: {},
    debug: function (str) {
      console.log(str);
    },
    onConnect: () => {
      client.subscribe(`/topic/${store.state.user.id}/messages`, (message) => {
        ElMessage.success({
          message: message.body,
          duration: 1000
        });
        emit("refreshData");
      });
    }
  });

  client.activate();
};

onMounted(() => {
  connect();
});
</script>
<style>
.warning-text {
  padding-top: 15px;
  color: #c24545;
}
</style>
