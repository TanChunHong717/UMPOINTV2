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
    </el-form>
    <template #footer>
      <el-row justify="end" style="padding-bottom: 10px">
        <el-col class="warning-text" span="24">
          Warning: All conflict booking will be reject
        </el-col>
      </el-row>
      <el-row justify="end">
        <el-col span="14">
          <el-button type="danger" @click="deleteHandle()" v-if="dataForm.id != '' && state.hasPermission('accommodation:closure:delete')">Delete</el-button>
          <el-button @click="cancelHandle()">Cancel</el-button>
          <el-button
            type="primary"
            @click="dataFormSubmitHandle()"
            v-if="
            (state.hasPermission('accommodation:closure:save') && dataForm.id == '') ||
            (state.hasPermission('accommodation:closure:update') && dataForm.id != '')"
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
import {formatDateToDateStr} from "@/utils/date";
import useView from "@/hooks/useView";

const emit = defineEmits(["refreshData", "cancel"]);

const view = reactive({});
const state = reactive({ ...useView(view), ...toRefs(view) });

let event: any;
let accommodationId: any;
const visible = ref(false);
const dateRange = ref<[Date, Date]>([new Date(), new Date()]);
const dataFormRef = ref();

const dataForm: any = reactive({
  id: '',
  accommodationId: '',
  startDay: '',
  endDay: '',
});

const init = (id: number, e: any, aid: number) => {
  visible.value = true;
  dataForm.id = "";

  accommodationId = aid;
  event = e;

  if (id)
    getInfo(id);
  else
    setDateFromEvent();
};

const setDateFromEvent = () => {
  const startDate = new Date(event.start);
  const endDate = new Date(event.end);
  dateRange.value = [startDate, endDate];
}

const getInfo = (id: number) => {
  baseService.get("/space/closure/" + id).then((res) => {
    Object.assign(dataForm, res.data);
    setDateFromDTO(res.data);
  });
};

const setDateFromDTO = (dto: any) => {
  const startDay = new Date(dto.startDay);
  const endDay = new Date(dto.endDay);
  dateRange.value = [startDay, endDay];
}

const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    updateDataForm();
    (!dataForm.id ? baseService.post : baseService.put)("/accommodation/closure", dataForm).then((res) => {
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

  if (!dataForm.id)
    dataForm.accommodationId = accommodationId;
}

const deleteHandle = () => {
  baseService.delete("/accommodation/closure/" + dataForm.id, {}).then((res) => {
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
