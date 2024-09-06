<template>
  <div class="mod-accommodation__booking-rule">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()" class="header-form">
      <div>
        <el-form-item>
          <el-input v-model="state.dataForm.name" placeholder="Name" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="state.getDataList()">Search</el-button>
        </el-form-item>
      </div>
      <div>
        <el-form-item>
          <el-button
            v-if="state.hasPermission('service:default-booking-rule:update')"
            @click="defaultBookingRuleUpdateHandle"
            type="primary"
          >Update Default Booking Rule</el-button>
        </el-form-item>
        <el-form-item>
          <el-button
            v-if="state.hasPermission('accommodation:booking-rule:update')"
            :disabled="!(state.dataListSelections && state.dataListSelections.length > 0)"
            @click="applyDefaultBookingRuleHandle"
            type="primary"
          >Apply Default Booking Rule</el-button>
        </el-form-item>
      </div>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="name" label="Name" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Booking Rule" header-align="center" align="center">
        <el-table-column prop="manager" label="Manager" header-align="center" align="center" sortable="custom"></el-table-column>
        <el-table-column label="Approve Required" header-align="center" align="center">
          <template v-slot="scope">
            <div v-if="scope.row.spcBookingRuleDTO">
              <el-tag v-if="scope.row.spcBookingRuleDTO.approvalRequired == 1" type="primary">Yes</el-tag>
              <el-tag v-else type="info">No</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Open To" header-align="center" align="center">
          <template v-slot="scope">
            <div v-if="scope.row.spcBookingRuleDTO">
              <el-checkbox label="Staff" v-model="scope.row.spcBookingRuleDTO.openForStaff" :true-value="Number(1)" :false-value="Number(0)" disabled/>
              <el-checkbox label="Student" v-model="scope.row.spcBookingRuleDTO.openForStudent" :true-value="Number(1)" :false-value="Number(0)" disabled/>
              <el-checkbox label="Public" v-model="scope.row.spcBookingRuleDTO.openForPublic" :true-value="Number(1)" :false-value="Number(0)" disabled/>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="Price" header-align="center" align="center" sortable="custom"></el-table-column>
      </el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('service:service:info')" type="primary" link @click="$router.push({name:`service-info`, params: {id:scope.row.id}})">Details</el-button>
          <el-button v-if="state.hasPermission('service:booking-rule:update')" type="primary" link @click="bookingRuleUpdateHandle(scope.row)">Update</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
  </div>
  <!-- Popup, Add / Edit -->
  <update-booking-rule ref="bookingRuleUpdateRef" @refreshData="state.getDataList">Confirm</update-booking-rule>
  <update-default-booking-rule ref="defaultBookingRuleUpdateRef" @refreshDataList="state.getDataList">Confirm</update-default-booking-rule>
</template>
<script lang="ts" setup>
import useView from "@/hooks/useView";
import {onActivated, reactive, ref, toRefs} from "vue";
import UpdateBookingRule from "@/views/service/booking-rule-add-or-update.vue";
import UpdateDefaultBookingRule from "@/views/service/default-booking-rule-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/service/service/page",
  getDataListIsPage: true,
  exportURL: "/service/service/export",
  deleteURL: "/service/service",
  dataForm: {
    name: "",
    deptId: null,
    catId: null,
    tagId: null
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const defaultBookingRuleUpdateRef = ref();
const defaultBookingRuleUpdateHandle = () => {
  defaultBookingRuleUpdateRef.value.init();
};

const bookingRuleUpdateRef = ref();
const bookingRuleUpdateHandle = (service: any) => {
  bookingRuleUpdateRef.value.init(service);
};

const applyDefaultBookingRuleHandle = () => {
  baseService.post(
    "/service/service/apply",
    state.dataListSelections.map(
      (item: IObject) => item["id"]
    )
  ).then((res) => {
    ElMessage.success({
      message: 'Success',
      duration: 500,
      onClose: () => {
        visible.value = false;
        state.getDataList();
      }
    });
  });
}

onActivated(() => {
  state.getDataList();
})
</script>
<style>
.header-form {
  display:flex;
  justify-content: space-between;
}
</style>
