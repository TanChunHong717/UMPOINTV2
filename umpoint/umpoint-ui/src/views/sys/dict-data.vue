<template>
  <div class="mod-sys__dict">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-input v-model="state.dataForm.dictValue" placeholder="Dictionary value" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="state.dataForm.dictLabel" placeholder="Dictionary label" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="state.getDataList()">Search</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:dict:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('sys:dict:delete')" type="danger" @click="state.deleteHandle()">Delete</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="dictValue" label="Dictionary value" header-align="center" align="center"></el-table-column>
      <el-table-column prop="dictLabel" label="Dictionary label" header-align="center" align="center"></el-table-column>
      <el-table-column prop="sort" label="Sort" sortable="custom" header-align="center" align="center"></el-table-column>
      <el-table-column prop="remark" label="Remark" header-align="center" align="center"></el-table-column>
      <el-table-column prop="createDate" label="Create date" sortable="custom" header-align="center" align="center" width="180"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('sys:dict:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">Update</el-button>
          <el-button v-if="state.hasPermission('sys:dict:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">Delete</el-button>
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
import AddOrUpdate from "./dict-data-add-or-update.vue";

const props = defineProps({
  dictTypeId: {
    type: String,
    required: true
  }
});

const view = reactive({
  getDataListURL: "/sys/dict/data/page",
  getDataListIsPage: true,
  deleteURL: "/sys/dict/data",
  deleteIsBatch: true,
  dataForm: {
    dictTypeId: props.dictTypeId,
    dictLabel: "",
    dictValue: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.dataForm.dictTypeId = props.dictTypeId;
  addOrUpdateRef.value.init(id);
};
</script>
