<template>
  <div class="mod-booking__spc_closure">
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
        <el-button v-if="state.hasPermission('space:closure:save')" type="primary" @click="batchUpdateHandle()">Add</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('space:closure:delete')" type="danger" @click="state.deleteHandle()">Delete</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" @sort-change="state.dataListSortChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="name" label="Space" header-align="center" align="center" sortable="custom" width="150"></el-table-column>
      <el-table-column label="Booking Period" header-align="center" align="center" width="200">
        <template v-slot="scope">
          {{scope.row.startDay.split(' ')[0]}} to {{scope.row.endDay.split(' ')[0]}}<br>
          {{scope.row.startTime.substring(0, 5)}} to {{scope.row.endTime.substring(0, 5)}}
        </template>
      </el-table-column>
      <el-table-column label="Recurring">
        <el-table-column prop="recurMonday" label="Mon" header-align="center" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.recurMonday == 1" type="primary">Yes</el-tag>
            <el-tag v-else type="info">No</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recurTuesday" label="Tue" header-align="center" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.recurTuesday == 1" type="primary">Yes</el-tag>
            <el-tag v-else type="info">No</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recurWednesday" label="Wed" header-align="center" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.recurWednesday == 1" type="primary">Yes</el-tag>
            <el-tag v-else type="info">No</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recurThursday" label="Thu" header-align="center" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.recurThursday == 1" type="primary">Yes</el-tag>
            <el-tag v-else type="info">No</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recurFriday" label="Fri" header-align="center" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.recurFriday == 1" type="primary">Yes</el-tag>
            <el-tag v-else type="info">No</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recurSaturday" label="Sat" header-align="center" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.recurSaturday == 1" type="primary">Yes</el-tag>
            <el-tag v-else type="info">No</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recurSunday" label="Sun" header-align="center" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.recurSunday == 1" type="primary">Yes</el-tag>
            <el-tag v-else type="info">No</el-tag>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('space:closure:update')" type="primary" link @click="updateHandle(scope.row.id, scope.row.spaceId)">Update</el-button>
          <el-button v-if="state.hasPermission('space:closure:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- Popup, Add / Edit -->
    <update ref="updateRef" @refreshData="state.getDataList">Confirm</update>
    <closure-batch-add ref="batchUpdateRef" @refreshData="state.getDataList">Confirm</closure-batch-add>
  </div>
</template>
<script lang="ts" setup>
import useView from "@/hooks/useView";
import {reactive, ref, toRefs} from "vue";
import {useAppStore} from "@/store";
import Update from "@/views/space/closure-add-or-update.vue";
import ClosureBatchAdd from "@/views/space/closure-batch-add.vue";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/space/closure/page",
  getDataListIsPage: true,
  deleteURL: "/space/closure",
  dataForm: {
    showPast: 0
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const updateRef = ref();
const updateHandle = (id: number, spaceId: number) => {
  updateRef.value.init(id, {}, spaceId);
};

const batchUpdateRef = ref();
const batchUpdateHandle = () => {
  batchUpdateRef.value.init();
}
</script>
