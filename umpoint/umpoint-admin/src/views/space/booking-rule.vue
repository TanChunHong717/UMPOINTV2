<template>
  <div class="mod-space__booking-rule">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()" class="header-form">
      <div>
        <el-form-item>
          <el-input v-model="state.dataForm.name" placeholder="Name" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="state.dataForm.approvalRequire"
            placeholder="Approval Required"
            clearable
          >
            <el-option label="Automatic approve" value="0"/>
            <el-option label="Require Admin Approve" value="1"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="state.getDataList()">Search</el-button>
        </el-form-item>
      </div>
      <div>
        <el-form-item>
          <el-button
            v-if="state.hasPermission('space:booking-rule:update') && state.dataListSelections && state.dataListSelections.length > 0"
            @click="batchBookingRuleUpdateHandle"
            type="primary"
          >Update Default Booking Rule</el-button>
        </el-form-item>
      </div>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="selection" :selectable="selectable" width="55" />
      <el-table-column type="expand">
        <template #default="scope">
          <div class="expand-row">
            <div v-if="scope.row.spcBookingRuleDTO">
              <el-row class="content-row">
                <el-col :span="24">
                  Available in Public Holiday(Include weekend):
                  <el-tag v-if="scope.row.spcBookingRuleDTO.holidayAvailable == 1" type="primary">Yes</el-tag>
                  <el-tag v-else type="info">No</el-tag>
                </el-col>
              </el-row>
              <el-row class="content-row">
                The space will be open for booking {{ scope.row.spcBookingRuleDTO.maxBookingAdvanceDay }} day(s) prior the event and will be closed {{  scope.row.spcBookingRuleDTO.minBookingAdvanceDay }} day(s) before space booking date.
              </el-row>
              <el-row class="content-row">
                <el-col :span="12">
                  Booking Mode:
                  <el-tag v-if="scope.row.spcBookingRuleDTO.bookingMode == 0" type="primary">Free time selection</el-tag>
                  <el-tag v-else type="info">Limited to preset slots</el-tag>
                </el-col>
                <el-col :span="12">Booking unit in hour: {{ scope.row.spcBookingRuleDTO.bookingUnit }}</el-col>
              </el-row>
              <el-row class="content-row">
                <el-col :span="12">Start Time: {{ scope.row.spcBookingRuleDTO.startTime }}</el-col>
                <el-col :span="12">End Time: {{ scope.row.spcBookingRuleDTO.endTime }}</el-col>
              </el-row>
              <el-row class="content-row">
                <el-col :span="12">Maximum reservation day: {{ scope.row.spcBookingRuleDTO.maxReservationDay }}</el-col>
                <el-col :span="12">Minimum reservation day: {{ scope.row.spcBookingRuleDTO.minReservationDay }}</el-col>
              </el-row>
              <el-row class="content-row">
                <el-col :span="12">Maximum booking hour: {{ scope.row.spcBookingRuleDTO.maxBookingHour }}</el-col>
                <el-col :span="12">Minimum booking hour: {{ scope.row.spcBookingRuleDTO.minBookingHour }}</el-col>
              </el-row>
              <el-row>
                <el-col :span="12">Maximum technician number: {{ scope.row.spcBookingRuleDTO.maxTechnicianNumber }}</el-col>
                <el-col :span="12">Price per technician: RM {{ scope.row.spcBookingRuleDTO.technicianPrice }}</el-col>
              </el-row>
            </div>
            <div v-else>
              Booking rule is not set for this space.
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="Name" header-align="center" align="center" sortable="custom" width="200"></el-table-column>
      <el-table-column label="Booking Rule" header-align="center" align="center">
        <el-table-column prop="managerName" label="Manager" header-align="center" align="center" width="110"></el-table-column>
        <el-table-column label="Approve Required" header-align="center" align="center">
          <template v-slot="scope">
            <div v-if="scope.row.spcBookingRuleDTO">
              <el-tag v-if="scope.row.spcBookingRuleDTO.approvalRequired == 1" type="primary">Yes</el-tag>
              <el-tag v-else type="info">No</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Open To" header-align="center" align="left" width="275">
          <template v-slot="scope">
            <div v-if="scope.row.spcBookingRuleDTO">
              <el-checkbox label="Staff" v-model="scope.row.spcBookingRuleDTO.openForStaff" :true-value="Number(1)" :false-value="Number(0)" disabled/>
              <el-checkbox label="Student" v-model="scope.row.spcBookingRuleDTO.openForStudent" :true-value="Number(1)" :false-value="Number(0)" disabled/>
              <el-checkbox label="Public" v-model="scope.row.spcBookingRuleDTO.openForPublic" :true-value="Number(1)" :false-value="Number(0)" disabled/>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Price(RM)" header-align="center" align="center">
          <el-table-column prop="hourPrice" label="Hour" header-align="center" align="center" sortable="custom"></el-table-column>
          <el-table-column prop="fourHoursPrice" label="4 Hours" header-align="center" align="center" width="150"></el-table-column>
          <el-table-column prop="dayPrice" label="Day" header-align="center" align="center"></el-table-column>
        </el-table-column>
      </el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="left" width="80">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('space:space:info')" type="primary" link @click="$router.push({name:`space-info`, params: {id:scope.row.id}})">Details</el-button>
          <el-button style="margin-left: 0" v-if="state.hasPermission('space:booking-rule:update')" type="primary" link @click="bookingRuleUpdateHandle(scope.row)">Update</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
  </div>
  <!-- Popup, Add / Edit -->
  <update-booking-rule ref="bookingRuleUpdateRef" @refreshData="state.getDataList">Confirm</update-booking-rule>
  <batch-update-booking-rule ref="batchBookingRuleUpdateRef" @refreshDataList="state.getDataList">Confirm</batch-update-booking-rule>
</template>
<script lang="ts" setup>
import useView from "@/hooks/useView";
import {onActivated, reactive, ref, toRefs} from "vue";
import UpdateBookingRule from "@/views/space/booking-rule-add-or-update.vue";
import BatchUpdateBookingRule from "@/views/space/booking-rule-batch-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/space/booking-rule/page",
  getDataListIsPage: true,
  dataForm: {
    name: "",
    approvalRequire: null
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const selectable = (row: any) => {
  return row.bookingRuleId;
}
const batchBookingRuleUpdateRef = ref();
const batchBookingRuleUpdateHandle = () => {
  batchBookingRuleUpdateRef.value.init(state.dataListSelections);
};

const bookingRuleUpdateRef = ref();
const bookingRuleUpdateHandle = (space: any) => {
  bookingRuleUpdateRef.value.init(space);
};

onActivated(() => {
  state.getDataList();
})
</script>
<style>
.header-form {
  display:flex;
  justify-content: space-between;
}
.content-row {
  margin-bottom: 14px;
}
.expand-row {
  padding: 0 30px;
  h1 {
    margin: 5px 0;
  }
}
</style>
