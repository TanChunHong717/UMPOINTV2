<template>
  <div class="mod-booking__acc-payment">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.id" placeholder="ID" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="state.dataForm.status"
          placeholder="Approval Required"
          clearable
        >
          <el-option label="Pending" value="0"/>
          <el-option label="Success" value="1"/>
          <el-option label="Failed" value="2"/>
          <el-option label="Refunded" value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column prop="id" label="ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="status" label="Status" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status == 0" type="danger">Pending</el-tag>
          <el-tag v-if="scope.row.status == 1" type="info">Success</el-tag>
          <el-tag v-if="scope.row.status == 2" type="primary">Failed</el-tag>
          <el-tag v-if="scope.row.status == 3" type="success">Refunded</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="resourceType" label="Type" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.resourceType == 0">Space</el-tag>
          <el-tag v-if="scope.row.resourceType == 1">Service</el-tag>
          <el-tag v-if="scope.row.resourceType == 2">Accommodation</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Resource" header-align="center" align="center">
        <template v-slot="scope">
          <el-button v-if="scope.row.resourceType == 0" :disabled="state.hasPermission('accommodation:accommodation:update')" type="primary" link @click="refundHandle(scope.row.id)">{{scope.row.resourceId}}</el-button>
          <el-button v-if="scope.row.resourceType == 1" :disabled="state.hasPermission('service:service:update')" type="primary" link @click="refundHandle(scope.row.id)">{{scope.row.resourceId}}</el-button>
          <el-button v-if="scope.row.resourceType == 2" :disabled="state.hasPermission('space:space:update')" type="primary" link @click="refundHandle(scope.row.id)">{{scope.row.resourceId}}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="user" label="User" header-align="center" align="center"></el-table-column>
      <el-table-column prop="method" label="Payment Method" header-align="center" align="center"></el-table-column>
      <el-table-column prop="amount" label="Amount(RM)" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="date" label="Payment date" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('booking:payment:update')" type="primary" link @click="refundHandle(scope.row.id)">Refund</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, toRefs } from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import baseService from "@/service/baseService";

const view = reactive({
  getDataListURL: "/booking/payment/page",
  getDataListIsPage: true,
  exportURL: "/booking/payment/export",
  dataForm: {
    id: null,
    status: null
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const refundHandle = (id: number) => {
  ElMessageBox.confirm("Confirm to refund this payment?", "Warning", {
    confirmButtonText: "Confirm",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      baseService
        .put("/booking/payment/refund/" + id)
        .then((res) => {
          state.getDataList();
          ElMessage.success({
            message: "Success",
            duration: 500,
          });
        });
    })
    .catch(() => {});
}
</script>
