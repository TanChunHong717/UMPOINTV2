<template>
  <div class="mod-space__dept-booking-rule">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.deptName" placeholder="Department" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column prop="name" label="Department" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="maxUserDailyBooking" label="Maximum booking number" header-align="center" align="center"></el-table-column>
      <el-table-column prop="maxUserDailyBookingHour" label="Maximum booking hour" header-align="center" align="center"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('space:dept-booking-rule:update')" type="primary" link @click="updateHandle(scope.row.id, scope.row.deptId, scope.row.deptName)">Update</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- Popup, Add / Edit -->
    <update ref="updateRef" @refreshDataList="state.getDataList">Confirm</update>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import Update from "./dept-booking-rule-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/space/dept-booking-rule/page",
  getDataListIsPage: true,
  dataForm: {
    deptName: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const updateRef = ref();
const updateHandle = (id: any, deptId: any, name: any) => {
  updateRef.value.init(id, deptId, name);
};
</script>
