<template>
  <div class="mod-space__space">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()" class="header-form">
      <div>
        <el-form-item>
          <el-input v-model="state.dataForm.name" placeholder="Name" clearable></el-input>
        </el-form-item>
        <el-form-item label="Department" class="dept-list">
          <el-popover :width="400" ref="deptListPopover" placement="bottom-start" trigger="click" popper-class="popover-pop">
            <template v-slot:reference>
              <el-input v-model="currentChooseDepartment" placeholder="Department">
                <template v-slot:suffix>
                  <el-icon @click.stop="deptListTreeSetDefaultHandle()" class="el-input__icon"><circle-close /></el-icon>
                </template>
              </el-input>
            </template>
            <div class="popover-pop-body">
              <el-tree :data="deptList" :props="{ label: 'name', children: 'children' }" node-key="id" ref="deptListTree" :highlight-current="true" :expand-on-click-node="false" accordion @current-change="deptListTreeCurrentChangeHandle"></el-tree>
            </div>
          </el-popover>
        </el-form-item>
        <el-form-item>
          <el-button @click="state.getDataList()">Search</el-button>
        </el-form-item>
      </div>
      <el-form-item>
        <el-button v-if="state.hasPermission('space:space:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column label="Image" header-align="center" align="center">
        <template v-slot="scope">
          <el-image v-if="scope.row.imageUrls && scope.row.imageUrls[0]" style="width: 100px; height: 100px" :src="scope.row.imageUrls[0]"/>
          <div v-else>No Image Available</div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="Name" header-align="center" align="center"></el-table-column>
      <el-table-column prop="category" label="Category" header-align="center" align="center"></el-table-column>
      <el-table-column prop="deptName" label="Department" header-align="center" align="center"></el-table-column>
      <el-table-column prop="description" label="Description" header-align="center" align="center"></el-table-column>
      <el-table-column prop="facilities" label="Facilities" header-align="center" align="center"></el-table-column>
      <el-table-column label="Actions" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('space:space:update')" type="primary" link @click="addOrUpdateHandle(scope.row.id)">Update</el-button>
          <el-button v-if="state.hasPermission('space:space:delete')" type="primary" link @click="state.deleteHandle(scope.row.id)">Delete</el-button>
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
import {computed, reactive, ref, toRefs} from "vue";
import AddOrUpdate from "./space-add-or-update.vue";
import {useAppStore} from "@/store";
import {IObject} from "@/types/interface";
import baseService from "@/service/baseService";
import {ElMessage} from "element-plus";

const store = useAppStore();
const user = computed(() => store.state.user);
const deptList = ref([]);
const currentChooseDepartment = ref();

const view = reactive({
  deleteIsBatch: false,
  getDataListURL: "/space/space/page",
  getDataListIsPage: true,
  exportURL: "/space/space/export",
  deleteURL: "/space/space"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

const getDeptList = () => {
  return baseService.get("/sys/dept/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    deptList.value = res.data;
  });
};

const deptListTreeSetDefaultHandle = () => {
  state.dataForm.deptId = null;
  currentChooseDepartment.value = null;
};

const deptListTreeCurrentChangeHandle = (data: IObject) => {
  state.dataForm.deptId = data.id;
  currentChooseDepartment.value = data.name;
};

getDeptList();
</script>
<style>
.header-form {
  display:flex;
  justify-content: space-between;
}
.dept-list {
  .el-input__inner,
  .el-input__suffix {
    cursor: pointer;
  }
}
</style>
