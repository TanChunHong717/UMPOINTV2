<template>
  <div class="mod-booking__spc-booking">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.id" placeholder="ID" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="id" label="ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="status" label="Status" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="spaceId" label="Space ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="adminId" label="Admin" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="userId" label="User ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="workerId" label="Worker" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="paymentId" label="Payment ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="paymentAmount" label="Amount(RM)" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="startDay" label="Start day of booking" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="endDay" label="End day of booking" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="startTime" label="Start time of booking in a day" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="endTime" label="End time of booking in a day" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="createDate" label="Create date" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="updateDate" label="Update date" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./spcbooking-add-or-update.vue";

const view = reactive({
  getDataListURL: "/booking/space/page",
  getDataListIsPage: true,
  exportURL: "/booking/space/export",
});

const state = reactive({ ...useView(view), ...toRefs(view) });
</script>
