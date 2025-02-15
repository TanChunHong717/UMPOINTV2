<template>
  <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
    <el-form-item label="Name" prop="name">
      <el-input v-model="dataForm.name" placeholder="Name"></el-input>
    </el-form-item>
    <el-form-item label="Category" prop="catId">
      <el-select
        v-model="dataForm.catId"
        placeholder="Select category"
      >
        <el-option
          v-for="category in categoryList"
          :key="category.id"
          :label="category.name"
          :value="category.id"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="Department" prop="deptName">
      <el-popover :width="530" ref="deptListPopover" placement="bottom-start" trigger="click" popper-class="popover-pop">
        <template v-slot:reference>
          <el-input v-model="dataForm.deptName" :readonly="true" placeholder="Department"></el-input>
        </template>
        <div class="popover-pop-body"><el-tree :data="deptList" :props="{ label: 'name', children: 'children' }" node-key="id" ref="deptListTree" :highlight-current="true" :expand-on-click-node="false" accordion @current-change="deptListTreeCurrentChangeHandle"> </el-tree></div>
      </el-popover>
    </el-form-item>
    <el-form-item label="Description" prop="description">
      <Editor
        api-key="q32ldzad7pdjt17n0ej2icck28i6pzk3954qkxfeo6n99d1k"
        v-model="dataForm.description"
        :init="{
          plugins: 'lists link image table code help wordcount'
        }"
      />
    </el-form-item>
    <el-form-item label="Tag" prop="tag">
      <el-select
        v-model="selectTagList"
        placeholder="Select tags"
        multiple
      >
        <el-option
          v-for="tag in tagList"
          :key="tag.id"
          :label="tag.tagName"
          :value="tag.id"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="Image" prop="image">
      <multi-upload :url="fileList" @upload="imageUploadHandle"></multi-upload>
    </el-form-item>
    <el-row class="bottom-button" justify="end">
      <el-button @click="cancelHandle" size="large">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()" size="large">Confirm</el-button>
    </el-row>
  </el-form>
</template>
<script lang="ts" setup>
import {onMounted, onUpdated, reactive, ref} from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
import {IObject} from "@/types/interface";
import MultiUpload from "@/components/upload/multi-upload.vue";
import emits from "@/utils/emits";
import {EMitt} from "@/constants/enum";
import {useRoute} from "vue-router";
import Editor from "@tinymce/tinymce-vue";
import {formatDescription} from "@/utils/custom-utils";

const route = useRoute();
const categoryList = ref<{id: number; name: string}[]>([]);

const deptList = ref([]);
const deptListPopover = ref();
const deptListTree = ref();

const tagList = ref<{id: number; tagName: string}[]>([]);
const selectTagList = ref<number[]>([]);

const fileList = ref<any[]>([]);

const dataFormRef = ref();
const dataForm = reactive<any>({
  id: '',
  name: '',
  catId: '',
  deptId: '',
  description: '',
  deptName: '',
  svcImageDTOList: [],
  svcTagDTOList: [],
  status: 1
});

const rules = ref({
  name: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'change' }
  ],
  catId: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'change' }
  ],
  deptName: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'change' }
  ]
});

const getCategoryList = async () => {
  return baseService.get("/service/category/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    categoryList.value = res.data;
  });
};

const deptListTreeCurrentChangeHandle = (data: IObject) => {
  dataForm.deptId = data.id;
  dataForm.deptName = data.name;
  deptListPopover.value.hide();
};

const getDeptList = async () => {
  return baseService.get("/sys/dept/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    deptList.value = res.data;
  });
};

const getTagList = async () => {
  return baseService.get("/service/tag/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    tagList.value = res.data;
  });
};

const getInfo = (id: bigint) => {
  baseService.get("/service/service/" + id).then((res) => {
    Object.assign(dataForm, res.data);
    for (let i = 0; res.data.svcImageDTOList && i < res.data.svcImageDTOList.length; i++) {
      let svcImageDTO = res.data.svcImageDTOList[i];
      fileList.value.push({id: svcImageDTO.id, url: svcImageDTO.imageUrl})
    }
    for (let i = 0; res.data.svcTagDTOList && i < res.data.svcTagDTOList.length; i++) {
      let svcTagDTO = res.data.svcTagDTOList[i];
      selectTagList.value.push(svcTagDTO.id);
    }
    dataForm.description = formatDescription(dataForm.description);
  });
};

const init = (id?: bigint) => {
  dataForm.id = "";

  getCategoryList();
  getDeptList();
  getTagList();

  selectTagList.value = [];
  fileList.value = [];

  if (id && !isNaN(Number(id))) {
    getInfo(id);
  }
};

const initialize = () => {
  const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
  init(id ? BigInt(id): undefined)
}

const imageUploadHandle = (svcImageDTOList: any) => {
  dataForm.svcImageDTOList = svcImageDTOList
}

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    dataForm.svcImageDTOList.forEach((svcImageDTO: any) => {
      svcImageDTO.serviceId = dataForm.id;
    });
    dataForm.svcTagDTOList = [];
    selectTagList.value.forEach((tagId) => {
      dataForm.svcTagDTOList.push({id: tagId, serviceId: dataForm.id});
    });
    (!dataForm.id ? baseService.post : baseService.put)("/service/service", dataForm, {"Contain-HTML": true}).then((res) => {
      emits.emit(EMitt.OnCloseCurrTab);
      ElMessage.success({
        message: 'Success',
        duration: 500,
      });
    });
  });
};

const cancelHandle = () => {
  dataFormRef.value.resetFields();
  emits.emit(EMitt.OnCloseCurrTab);
}

onMounted(() => {
  initialize();
})

onUpdated(() => {
  initialize();
})
</script>
<style>
div.el-form-item__content > div {
  width: 100% !important;
}
.bottom-button {
  padding-top: 10px;
}
</style>
