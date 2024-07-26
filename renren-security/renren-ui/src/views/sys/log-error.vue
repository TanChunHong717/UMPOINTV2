<template>
  <div class="mod-sys__log-error">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button type="info" @click="state.exportHandle()">Export</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column prop="requestUri" label="Request URI" header-align="center" align="center"></el-table-column>
      <el-table-column prop="requestMethod" label="Request method" header-align="center" align="center"></el-table-column>
      <el-table-column prop="requestParams" label="Request params" header-align="center" align="center" width="150" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="ip" label="IP" header-align="center" align="center"></el-table-column>
      <el-table-column prop="userAgent" label="User agent" header-align="center" align="center" width="150" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="createDate" label="Create date" sortable="custom" header-align="center" align="center" width="180"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button type="primary" link @click="infoHandle(scope.row.errorInfo)">Error info</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, toRefs } from "vue";
import { ElMessageBox } from "element-plus";

const view = reactive({
  getDataListURL: "/sys/log/error/page",
  getDataListIsPage: true,
  exportURL: "/sys/log/error/export"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const infoHandle = (info: string) => {
  ElMessageBox.alert(info, "Error message", {
    confirmButtonText: "Confirm"
  });
};
</script>

<style lang="less" scoped>
.mod-sys__log-error {
  &-view-info {
    width: 80%;
  }
}
</style>
