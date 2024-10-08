<template>
  <div class="mod-sys__user">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.username" placeholder="Username" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <ren-select v-model="state.dataForm.gender" dict-type="gender" placeholder="Gender"></ren-select>
      </el-form-item>
      <el-form-item>
        <ren-dept-tree v-model="state.dataForm.deptId" placeholder="Department" :query="true"></ren-dept-tree>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:user:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:user:delete')" type="danger" @click="state.deleteHandle()">Delete</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:user:export')" type="info" @click="state.exportHandle()">Export</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="username" label="Username" sortable="custom" header-align="center" align="center"></el-table-column>
      <el-table-column prop="deptName" label="Department" header-align="center" align="center"></el-table-column>
      <el-table-column prop="email" label="Email" header-align="center" align="center"></el-table-column>
      <el-table-column prop="mobile" label="Mobile" sortable="custom" header-align="center" align="center"></el-table-column>
      <el-table-column prop="gender" label="Gender" sortable="custom" header-align="center" align="center">
        <template v-slot="scope">
          {{ state.getDictLabel("gender", scope.row.gender) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="Status" sortable="custom" header-align="center" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status === 0" size="small" type="danger">Suspend</el-tag>
          <el-tag v-else size="small" type="success">Normal</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createDate" label="Create date" sortable="custom" header-align="center" align="center" width="180"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('sys:user:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">Update</el-button>
          <el-button v-if="state.hasPermission('sys:user:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList"></add-or-update>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./user-add-or-update.vue";

const view = reactive({
  getDataListURL: "/sys/user/page",
  getDataListIsPage: true,
  deleteURL: "/sys/user",
  deleteIsBatch: true,
  exportURL: "/sys/user/export",
  dataForm: {
    username: "",
    deptId: "",
    postId: "",
    gender: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>
