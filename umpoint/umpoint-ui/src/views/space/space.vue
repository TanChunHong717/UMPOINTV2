<template>
  <div class="mod-space__space">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()" class="header-form">
      <div>
        <el-form-item>
          <el-input v-model="state.dataForm.name" placeholder="Name" clearable></el-input>
        </el-form-item>
        <el-form-item class="dept-list">
          <el-popover :width="218" ref="deptListPopover" placement="bottom-start" trigger="click" popper-class="popover-pop">
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
          <el-select
            v-model="state.dataForm.catId"
            placeholder="Category"
            filterable
          >
            <el-option
              v-for="category in categoryList"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="state.dataForm.tagId"
            placeholder="Tag"
            filterable
          >
            <el-option
              v-for="tag in tagList"
              :key="tag.id"
              :label="tag.tagName"
              :value="tag.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="state.getDataList()">Search</el-button>
        </el-form-item>
      </div>
      <el-form-item>
        <el-button v-if="state.hasPermission('space:space:save')" type="primary" @click="addHandle()">Add</el-button>
      </el-form-item>
    </el-form>
    <div v-for="space in state.dataList" :key="space.id" class="space-container">
      <el-row align="middle" justify="start" style="margin-bottom: 10px;" :gutter="10">
        <el-col :span="5">
          <div v-if="space.spcImageDTOList && space.spcImageDTOList.length > 0">
            <el-image class="space-image" :src="space.spcImageDTOList[0].imageUrl" fit="cover"/>
          </div>
          <el-empty v-else :image-size="100" description="No Image"></el-empty>
        </el-col>
        <el-col :span="16">
          <el-row class="in-col-row">
            <el-col :span="24" class="title">{{ space.name }}</el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-location"></use></svg>
              <span style="margin-left: 4px"> {{ space.address }}</span>
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-apartment"></use></svg>
              Department: {{ space.deptName }}
            </el-col>
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-appstore"></use></svg>
              Category: {{ space.category }}
            </el-col>
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-team"></use></svg>
              Capacity: {{ space.capacity }}
            </el-col>
          </el-row>
          <el-row v-if="space.facilities?.trim().length > 0">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-wrench"></use></svg>
              Facilities: {{ space.facilities }}
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-tag"></use></svg>
              Tag:
              <el-tag v-if="space.spcTagDTOList?.length > 0" v-for="tag in space.spcTagDTOList" type="primary">{{ tag.tagName }} </el-tag>
              <el-tag v-else type="info">No Tag</el-tag>
            </el-col>
          </el-row>
          <el-divider style="margin: 10px 0;"></el-divider>
          <el-row>
            <el-col :span="8">
              <span class="hour_price">RM{{ space.hour_price }}</span> / Hour
            </el-col>
            <el-col :span="8" v-if="space.four_hour_price">
              <span class="four_hour_price">RM{{ space.four_hour_price }}</span> / 4 Hours
            </el-col>
            <el-col :span="8" v-if="space.day_price">
              <span class="day_price">RM{{ space.day_price }}</span> / Day
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="3" class="button-column">
          <el-button @click="$router.push({name:`space-info`, params: {id:space.id}})" class="action-button" re>Details</el-button>
          <el-button class="action-button bottom-button">Availability</el-button>
        </el-col>
      </el-row>
    </div>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
  </div>
</template>
<script lang="ts" setup>
import useView from "@/hooks/useView";
import {onActivated, onMounted, reactive, ref, toRefs} from "vue";
import {IObject} from "@/types/interface";
import baseService from "@/service/baseService";
import {ElMessage} from "element-plus";
import router from "@/router";

const deptList = ref([]);
const categoryList = ref([]);
const tagList = ref([]);
const currentChooseDepartment = ref();

const view = reactive({
  deleteIsBatch: false,
  getDataListURL: "/space/space/page",
  getDataListIsPage: true,
  exportURL: "/space/space/export",
  deleteURL: "/space/space",
  dataForm: {
    name: "",
    deptId: null,
    catId: null,
    tagId: null
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addHandle = () => {
  router.push({name: "space-add"})
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

const getCategoryList = () => {
  return baseService.get("/space/category/list/filter").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    categoryList.value = res.data;
  });
};

const getTagList = () => {
  return baseService.get("/space/tag/list/filter").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    tagList.value = res.data;
  });
};

onMounted(() => {
  getDeptList();
  getCategoryList();
  getTagList();
})

onActivated(() => {
  state.getDataList();
})
</script>
<style>
.header-form {
  display:flex;
  justify-content: space-between;
}
.space-image {
  width: 100%;
  max-width: 250px;
  max-height: 150px;
  padding: 0 10px;
}
.title {
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 5px;
}
.in-col-row {
  margin-bottom: 3px;
}
.button-column {
  display: flex !important;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}
.action-button {
  width: 100px;
  margin-left: 0 !important;
}
.bottom-button {
  margin-top: 5px;
}
</style>
