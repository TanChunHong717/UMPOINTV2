<template>
  <div class="mod-booking__spc-booking">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.id" placeholder="ID" clearable @clear="state.dataForm.id = null"></el-input>
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
        <el-input v-model="state.dataForm.space" placeholder="Space" clearable></el-input>
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
        <template #default="scope">
          <div class="expand-row">
            <div v-if="scope.row.spcBookingAttachmentDTOList && scope.row.spcBookingAttachmentDTOList.length > 0">
              <h1>Attachment:</h1>
              <ul>
                <li v-for="attachmentDTO in scope.row.spcBookingAttachmentDTOList"><a :href="attachmentDTO.url" :download="attachmentDTO.name">{{ attachmentDTO.name }}</a></li>
              </ul>
            </div>
            <div v-else style="margin-bottom: 10px">No attachment found for this booking.</div>
            <div v-if="scope.row.spcBookingTechnicianDTOList && scope.row.spcBookingTechnicianDTOList.length > 0">
              <h1>Technician:</h1>
              <ul>
                <li v-for="technicianDTO in scope.row.spcBookingTechnicianDTOList">{{ technicianDTO.technicianName }}</li>
              </ul>
            </div>
            <div v-else>No technician found for this booking.</div>
          </div>
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
          <el-button v-if="scope.row.spcPaymentDTOList && scope.row.spcPaymentDTOList.length > 0" type="primary" link @click="showPaymentDialog(scope.row.spcPaymentDTOList)">Payment</el-button>
          <el-button style="margin-left: 0" v-if="state.hasPermission('space:booking:approve') && scope.row.status == 0" type="primary" link @click="approveHandle(scope.row.id, scope.row.technicianNumber)">Approve</el-button>
          <el-button style="margin-left: 0" v-if="state.hasPermission('space:booking:reject') && scope.row.status == 0" type="primary" link @click="rejectHandle(scope.row.id)">Reject</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
  </div>
  <spc-approve ref="spcApproveRef" @refreshDataList="state.getDataList">Confirm</spc-approve>
  <el-dialog v-model="dialogTableVisible" title="Payment" width="800">
    <el-table :data="dialogTableData" size="small" :border="true">
      <el-table-column type="expand">
        <template #default="props">
          <div class="expand-row" v-if="props.row.spcPaymentItemDTOList && props.row.spcPaymentItemDTOList.length > 0" style="padding: 10px 0 10px 47px">
            <h1 style="margin-bottom: 10px">Payment Item</h1>
            <el-table :data="props.row.spcPaymentItemDTOList" size="small" :default-sort="{ prop: 'id', order: 'ascending' }" :border="true">
              <el-table-column prop="id" label="ID" header-align="center" align="center"></el-table-column>
              <el-table-column prop="itemName" label="Name" header-align="center" align="center" width="150"></el-table-column>
              <el-table-column prop="itemAmount" label="Amount" header-align="center" align="center"></el-table-column>
              <el-table-column prop="itemPrice" label="Price" header-align="center" align="center"></el-table-column>
              <el-table-column label="Item Total(RM)" header-align="center" align="center">
                <template v-slot="scope">
                  {{scope.row.itemAmount * scope.row.itemPrice}}
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="expand-row" v-else>No payment item for this payment.</div>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="status" label="Status" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status == 0" type="danger">Pending</el-tag>
          <el-tag v-if="scope.row.status == 1" type="success">Success</el-tag>
          <el-tag v-if="scope.row.status == 2" type="warning">Failed</el-tag>
          <el-tag v-if="scope.row.status == 3" type="info">Refunded</el-tag>
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
  </el-dialog>
</template>
<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import baseService from "@/service/baseService";
import {useRoute} from "vue-router";
import SpcApprove from "@/views/booking/spc-approve.vue";

const dialogTableVisible = ref(false);
const dialogTableData = ref([]);
const route = useRoute();
const view = reactive({
  getDataListURL: "/space/booking/page",
  getDataListIsPage: true,
  exportURL: "/space/booking/export",
  dataForm: {
    id: route.query.id? route.query.id: null,
    status: null,
    space: null,
    event: null
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const showPaymentDialog = (data: any) => {
  dialogTableData.value = data;
  dialogTableVisible.value = true;
}

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
  padding: 0 30px;
  h1 {
    margin: 5px 0;
  }
}

ul {
  margin: 0;
}
</style>
