<template>
  <div class="mod-booking__spc-booking">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.id" placeholder="ID" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="state.dataForm.status"
          placeholder="Status"
          clearable
        >
          <el-option label="Pending" value="0"/>
          <el-option label="Reject" value="1"/>
          <el-option label="Approve" value="2"/>
          <el-option label="Complete" value="3"/>
          <el-option label="Cancel" value="4"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="state.dataForm.event" placeholder="Event" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="expand">
        <template #default="props">
          <div class="expand-row" v-if="props.row.spcPaymentDTOList && props.row.spcPaymentDTOList.length > 0">
            <el-table :data="props.row.spcPaymentDTOList">
              <el-table-column prop="id" label="ID" header-align="center" align="center" sortable="custom"></el-table-column>
              <el-table-column prop="status" label="Status" header-align="center" align="center">
                <template v-slot="scope">
                  <el-tag v-if="scope.row.status == 0" type="danger">Pending</el-tag>
                  <el-tag v-if="scope.row.status == 1" type="info">Success</el-tag>
                  <el-tag v-if="scope.row.status == 2" type="primary">Failed</el-tag>
                  <el-tag v-if="scope.row.status == 3" type="success">Refunded</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="method" label="Payment Method" header-align="center" align="center"></el-table-column>
              <el-table-column prop="amount" label="Amount(RM)" header-align="center" align="center" sortable="custom"></el-table-column>
              <el-table-column prop="date" label="Payment date" header-align="center" align="center" sortable="custom"></el-table-column>
              <el-table-column label="Actions" fixed="right" header-align="center" align="center">
                <template v-slot="scope">
                  <el-button v-if="state.hasPermission('payment:space:refund')" type="primary" link @click="refundHandle(scope.row.id)">Refund</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="expand-row" v-else>No payment for this booking.</div>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="event" label="Event" header-align="center" align="center"></el-table-column>
      <el-table-column label="Status" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status == 0" type="danger">Pending</el-tag>
          <el-tag v-if="scope.row.status == 1" type="info">Reject</el-tag>
          <el-tag v-if="scope.row.status == 2" type="primary">Approve</el-tag>
          <el-tag v-if="scope.row.status == 3" type="success">Complete</el-tag>
          <el-tag v-if="scope.row.status == 3" type="warning">Cancel</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="space" label="Space" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="username" label="User" header-align="center" align="center"></el-table-column>
      <el-table-column prop="paymentAmount" label="Amount(RM)" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Booking Period" header-align="center" align="center">
        <template v-slot="scope">
          {{scope.row.startDay.split(' ')[0]}} to {{scope.row.endDay.split(' ')[0]}}<br>
          {{scope.row.startTime.substring(0, 5)}} to {{scope.row.endTime.substring(0, 5)}}
        </template>
      </el-table-column>
      <el-table-column prop="technicianNumber" label="Technician require" header-align="center" align="center"></el-table-column>
      <el-table-column prop="createDate" label="Create date" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="left">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('space:booking:approve') && scope.row.status == 0" type="primary" link @click="approveHandle(scope.row.id, scope.row.technicianNumber)">Approve</el-button>
          <el-button style="margin-left: 0" v-if="state.hasPermission('space:booking:reject') && scope.row.status == 0" type="primary" link @click="rejectHandle(scope.row.id)">Reject</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
  </div>
  <spc-approve ref="spcApproveRef" @refreshDataList="state.getDataList">Confirm</spc-approve>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import baseService from "@/service/baseService";
import {useRoute} from "vue-router";
import SpcApprove from "@/views/booking/spc-approve.vue";

const route = useRoute();
const view = reactive({
  getDataListURL: "/space/booking/page",
  getDataListIsPage: true,
  exportURL: "/space/booking/export",
  dataForm: {
    id: route.query.id? route.query.id: null,
    status: null,
    event: ''
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const spcApproveRef = ref();
const approveHandle = (id: number, maxTechnician: number) => {
  if (maxTechnician > 0)
    spcApproveRef.value.init(id, maxTechnician);
  else
    ElMessageBox.confirm("Confirm to approve this booking?", "Warning", {
      confirmButtonText: "Confirm",
      cancelButtonText: "Cancel",
      type: "warning"
    })
      .then(() => {
        baseService
          .put("/space/booking/approve/" + id)
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

const rejectHandle = (id: number) => {
  ElMessageBox.confirm("Confirm to reject this booking?", "Warning", {
    confirmButtonText: "Confirm",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      baseService
        .put("/space/booking/reject/" + id)
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

const refundHandle = (id: number) => {
  ElMessageBox.confirm("Confirm to refund this payment?", "Warning", {
    confirmButtonText: "Confirm",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      baseService
        .put("/payment/space/refund/" + id)
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
<style>
.expand-row {
  padding: 0 85px 0 47px;
}
</style>
