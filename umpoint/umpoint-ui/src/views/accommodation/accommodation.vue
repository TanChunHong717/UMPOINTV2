<template>
  <div class="mod-accommodation__accaccommodation">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <!-- Modify id to field you want -->
      <el-form-item>
        <el-input v-model="state.dataForm.id" placeholder="ID" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('accommodation:accommodation:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('accommodation:accommodation:delete')" type="danger" @click="state.deleteHandle()">Delete</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="id" label="ID" header-align="center" align="center"></el-table-column>
              <el-table-column prop="name" label="Name" header-align="center" align="center"></el-table-column>
              <el-table-column prop="catId" label="Category ID" header-align="center" align="center"></el-table-column>
              <el-table-column prop="deptId" label="Department ID" header-align="center" align="center"></el-table-column>
              <el-table-column prop="address" label="Address" header-align="center" align="center"></el-table-column>
              <el-table-column prop="description" label="Description" header-align="center" align="center"></el-table-column>
              <el-table-column prop="facilities" label="Facilities" header-align="center" align="center"></el-table-column>
              <el-table-column prop="capacity" label="Max capacity" header-align="center" align="center"></el-table-column>
              <el-table-column prop="manager" label="Manager ID" header-align="center" align="center"></el-table-column>
              <el-table-column prop="bookingRuleId" label="Booking Rule ID" header-align="center" align="center"></el-table-column>
              <el-table-column prop="status" label="Status 0:Suspend 1:Normal" header-align="center" align="center"></el-table-column>
              <el-table-column prop="creator" label="Creator" header-align="center" align="center"></el-table-column>
              <el-table-column prop="createDate" label="Create date" header-align="center" align="center"></el-table-column>
              <el-table-column prop="updater" label="Updater" header-align="center" align="center"></el-table-column>
              <el-table-column prop="updateDate" label="Update date" header-align="center" align="center"></el-table-column>
            <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('accommodation:accommodation:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">Update</el-button>
          <el-button v-if="state.hasPermission('accommodation:accommodation:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- Popup, Add / Edit -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">Confirm</add-or-update>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./accommodation-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/accommodation/accommodation/page",
  getDataListIsPage: true,
  exportURL: "/accommodation/accommodation/export",
  deleteURL: "/accommodation/accommodation"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
