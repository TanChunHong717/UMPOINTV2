<template>
  <div class="mod-chat__chatuserreport">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-select
          v-model="state.dataForm.status"
          placeholder="Status"
          clearable
        >
          <el-option label="Unresolved" :value="0"/>
          <el-option label="Resolved" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column prop="id" label="ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Status" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status == 0" type="danger">Unresolved</el-tag>
          <el-tag v-if="scope.row.status == 1" type="primary">Resolved</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Reported" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.reportedUserType == 2" type="danger">Client</el-tag>
          <el-tag v-if="scope.row.reportedUserType == 3" type="primary">Admin</el-tag>
          :
          <el-tag type="warning">{{ scope.row.reportedUsername }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="Reason" header-align="center" align="center"></el-table-column>
      <el-table-column label="Reported By" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.reportedByType == 2" type="danger">Client</el-tag>
          <el-tag v-if="scope.row.reportedByType == 3" type="primary">Admin</el-tag>
          :
          <el-tag type="info">{{ scope.row.reportedByUsername }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="Creation Date" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('chat:report:update') && scope.row.status == 0" type="primary" link @click="resolve(scope.row.id)">Resolve</el-button>
          <el-button v-if="scope.row.reportedUserType == 2 && scope.row.status == 0" type="primary" link @click="updateUserHandle(scope.row.reportedUser)">Block Client</el-button>
          <el-button v-if="scope.row.reportedUserType == 3 && scope.row.status == 0" type="primary" link @click="updateAdminHandle(scope.row.reportedBy)">Block Admin</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <update-user ref="updateUserRef" @refreshDataList="state.getDataList">Confirm</update-user>
    <update-admin ref="updateAdminRef" @refreshDataList="state.getDataList">Confirm</update-admin>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import UpdateUser from "../client/user-add-or-update.vue";
import UpdateAdmin from "../sys/user-add-or-update.vue";
import {ElMessage, ElMessageBox} from "element-plus";
import baseService from "@/service/baseService";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/chat/report/page",
  getDataListIsPage: true,
  dataForm: {
    status: 0
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const resolve = (id?: number) => {
  ElMessageBox.confirm("Confirm to resolve this user report?", "Warning", {
    confirmButtonText: "Confirm",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      baseService
        .put("/chat/report/resolve/" + id)
        .then((res) => {
          state.getDataList();
          ElMessage.success({
            message: "Success",
            duration: 500,
          });
        });
    })
    .catch(() => {});
};

const updateUserRef = ref();
const updateUserHandle = (id?: number) => {
  updateUserRef.value.init(id);
};

const updateAdminRef = ref();
const updateAdminHandle = (id?: number) => {
  updateAdminRef.value.init(id);
};
</script>
