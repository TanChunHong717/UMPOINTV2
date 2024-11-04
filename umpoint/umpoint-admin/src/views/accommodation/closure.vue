<template>
  <div class="mod-booking__accclosure">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-select
          v-model="state.dataForm.showPast"
          placeholder="Show current"
          clearable
        >
          <el-option label="Show current" :value="0"/>
          <el-option label="Show All" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('accommodation:closure:save')" type="primary">Add</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('accommodation:closure:delete')" type="danger" @click="state.deleteHandle()">Delete</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="name" label="ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="startDay" label="Start day of booking" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="endDay" label="End day of booking" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('accommodation:closure:update')" type="primary" link @click="updateHandle(scope.row.id, scope.row.accommodationId)">Update</el-button>
          <el-button v-if="state.hasPermission('accommodation:closure:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- Popup, Add / Edit -->
    <update ref="updateRef" @refreshData="state.getDataList">Confirm</update>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import Update from "./closure-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/accommodation/closure/page",
  getDataListIsPage: true,
  exportURL: "/accommodation/closure/export",
  deleteURL: "/accommodation/closure",
  dataForm: {
    showPast: 0
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const updateRef = ref();
const updateHandle = (id: number, accommodationId: number) => {
  updateRef.value.init(id, {}, accommodationId);
};
</script>
