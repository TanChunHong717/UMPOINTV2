<template>
  <div class="mod-accommodation__acctag">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.name" placeholder="ID" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('accommodation:tag:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('accommodation:tag:delete')" type="danger" @click="state.deleteHandle()">Delete</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column prop="tagName" label="Tag name" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column prop="accommodationCount" label="Number of Accommodation" header-align="center" align="center" sortable="custom"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('accommodation:tag:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">Update</el-button>
          <el-button v-if="state.hasPermission('accommodation:tag:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">Delete</el-button>
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
import AddOrUpdate from "./tag-add-or-update.vue";

const view = reactive({
  deleteIsBatch: false,
  getDataListURL: "/accommodation/tag/page",
  getDataListIsPage: true,
  exportURL: "/accommodation/tag/export",
  deleteURL: "/accommodation/tag",
  dataForm: {
    name: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
