<template>
  <div class="mod-job__schedule">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.beanName" placeholder="bean名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:schedule:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:schedule:delete')" type="danger" @click="state.deleteHandle()">Delete</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:schedule:pause')" type="danger" @click="pauseHandle()">Pause</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:schedule:resume')" type="danger" @click="resumeHandle()">Resume</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:schedule:run')" type="danger" @click="runHandle()">Execute</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:schedule:log')" type="success" @click="logHandle()">Log</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="beanName" label="bean name" header-align="center" align="center"></el-table-column>
      <el-table-column prop="params" label="Params" show-overflow-tooltip header-align="center" align="center"></el-table-column>
      <el-table-column prop="cronExpression" label="cron expression" header-align="center" align="center"></el-table-column>
      <el-table-column prop="remark" label="Remark" show-overflow-tooltip header-align="center" align="center"></el-table-column>
      <el-table-column prop="status" label="Status" sortable="custom" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status === 1" size="small">Normal</el-tag>
          <el-tag v-else size="small" type="danger">Stop</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="248">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('sys:schedule:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">Update</el-button>
          <el-button v-if="state.hasPermission('sys:schedule:pause')" type="primary" link @click="pauseHandle(scope.row.id)">Pause</el-button>
          <el-button v-if="state.hasPermission('sys:schedule:resume')" type="primary" link @click="resumeHandle(scope.row.id)">Resume</el-button>
          <el-button v-if="state.hasPermission('sys:schedule:run')" type="primary" link @click="runHandle(scope.row.id)">Run</el-button>
          <el-button v-if="state.hasPermission('sys:schedule:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList"></add-or-update>
    <!-- 弹窗, 日志列表 -->
    <log ref="logRef"></log>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import baseService from "@/service/baseService";
import { IObject } from "@/types/interface";
import AddOrUpdate from "./schedule-add-or-update.vue";
import Log from "./schedule-log.vue";
import { ElMessage, ElMessageBox } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const logRef = ref();

const view = reactive({
  getDataListURL: "/sys/schedule/page",
  getDataListIsPage: true,
  deleteURL: "/sys/schedule",
  deleteIsBatch: true,
  dataForm: {
    beanName: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

// 暂停
const pauseHandle = (id?: string) => {
  if (!id && state.dataListSelections && state.dataListSelections.length <= 0) {
    return ElMessage({
      message: "Please select data",
      type: "warning",
      duration: 500
    });
  }
  ElMessageBox.confirm("Confirm to pause?", "Hint", {
    confirmButtonText: "Confirm",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      baseService.put("/sys/schedule/pause", id ? [id] : state.dataListSelections && state.dataListSelections.map((item: IObject) => item.id)).then((res) => {
        ElMessage.success({
          message: "Success",
          duration: 500,
          onClose: () => {
            state.getDataList();
          }
        });
      });
    })
    .catch(() => {
      //
    });
};

// 恢复
const resumeHandle = (id?: string) => {
  if (!id && state.dataListSelections && state.dataListSelections.length <= 0) {
    return ElMessage({
      message: "Please select data",
      type: "warning",
      duration: 500
    });
  }
  ElMessageBox.confirm("Confirm to resume?", "Hint", {
    confirmButtonText: "Confirm",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      baseService.put("/sys/schedule/resume", id ? [id] : state.dataListSelections && state.dataListSelections.map((item: IObject) => item.id)).then((res) => {
        ElMessage.success({
          message: "Success",
          duration: 500,
          onClose: () => {
            state.getDataList();
          }
        });
      });
    })
    .catch(() => {
      //
    });
};

// 执行
const runHandle = (id?: string) => {
  if (!id && state.dataListSelections && state.dataListSelections.length <= 0) {
    return ElMessage({
      message: "Please select data",
      type: "warning",
      duration: 500
    });
  }
  ElMessageBox.confirm("Confirm to run?", "Hint", {
    confirmButtonText: "Confirm",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      baseService.put("/sys/schedule/run", id ? [id] : state.dataListSelections && state.dataListSelections.map((item: IObject) => item.id)).then((res) => {
        ElMessage.success({
          message: "Success",
          duration: 500,
          onClose: () => {
            state.getDataList();
          }
        });
      });
    })
    .catch(() => {
      //
    });
};

// 日志列表
const logHandle = () => {
  logRef.value.init();
};

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
