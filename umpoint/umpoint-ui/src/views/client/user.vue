<template>
  <div class="mod-booking__cliuser">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.username" placeholder="Username" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('client:user:delete')" type="danger" @click="state.deleteHandle()">Delete</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="id" label="ID" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="username" label="Username" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="mobile" label="Mobile" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="password" label="Password" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="email" label="Email" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="type" label="" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="spacePermission" label="" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="servicePermission" label="" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="accommodationPermission" label="" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="createDate" label="Create date" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button type="primary" link @click="addOrUpdateHandle(scope.row.id)">Update</el-button>
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
import AddOrUpdate from "./user-add-or-update.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/client/user/page",
  getDataListIsPage: true,
  exportURL: "/client/user/export",
  deleteURL: "/client/user",
  dataForm: {
    username: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
