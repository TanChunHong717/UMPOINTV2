<template>
  <div class="mod-booking__acc-booking">
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
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>

    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="expand">
        <template #default="props">
          <div class="expand-row" v-if="props.row.accPaymentDTOList && props.row.accPaymentDTOList.length > 0">
            <el-table :data="props.row.accPaymentDTOList">
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
              <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
                <template v-slot="scope">
                  <el-button v-if="state.hasPermission('booking:payment:update')" type="primary" link @click="refundHandle(scope.row.id)">Refund</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="expand-row" v-else>No payment for this booking.</div>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Status" header-align="center" align="center" width="100">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status == 0" type="danger">Pending</el-tag>
          <el-tag v-if="scope.row.status == 1" type="info">Reject</el-tag>
          <el-tag v-if="scope.row.status == 2" type="primary">Approve</el-tag>
          <el-tag v-if="scope.row.status == 3" type="success">Complete</el-tag>
          <el-tag v-if="scope.row.status == 4" type="warning">Cancel</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="accommodation" label="Accommodation" header-align="center" align="center" sortable="custom" width="175"></el-table-column>
      <el-table-column prop="username" label="User" header-align="center" align="center"></el-table-column>
      <el-table-column prop="paymentAmount" label="Amount(RM)" header-align="center" align="center" sortable="custom" width="135"></el-table-column>
      <el-table-column label="Booking Period" header-align="center" align="center" width="200">
        <template v-slot="scope">
          {{scope.row.startDay}} to {{scope.row.endDay}}
        </template>
      </el-table-column>
      <el-table-column prop="createDate" label="Create date" header-align="center" align="center" sortable="custom" width="150"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="left" width="85">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('booking:accommodation:update')" type="primary" link @click="approveOrRejectHandle(scope.row.id, true)">Approve</el-button>
          <el-button style="margin-left: 0" v-if="state.hasPermission('booking:accommodation:update')" type="primary" link @click="approveOrRejectHandle(scope.row.id, false)">Reject</el-button>
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
  getDataListURL: "/booking/accommodation/page",
  getDataListIsPage: true,
  exportURL: "/booking/accommodation/export",
  dataForm: {
    id: null,
    status: null
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const approveOrRejectHandle = (id: number, isApprove: boolean) => {
  ElMessageBox.confirm("Confirm to " + (isApprove? "approve": "reject") + " this booking?", "Warning", {
    confirmButtonText: "Confirm",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      baseService
        .put("/booking/accommodation/" + (isApprove? "approve": "reject") + "/" + id)
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
        .put("/booking/payment/accommodation/refund/" + id)
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
