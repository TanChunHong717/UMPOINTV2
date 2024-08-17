<template>
  <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
    <el-form-item label="Name" prop="name">
      <el-input v-model="dataForm.name" placeholder="Name"></el-input>
    </el-form-item>
    <el-form-item label="Category" prop="category">
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
    <el-form-item label="Department" prop="department">
      <el-popover :width="530" ref="deptListPopover" placement="bottom-start" trigger="click" popper-class="popover-pop">
        <template v-slot:reference>
          <el-input v-model="dataForm.deptName" :readonly="true" placeholder="Department"></el-input>
        </template>
        <div class="popover-pop-body"><el-tree :data="deptList" :props="{ label: 'name', children: 'children' }" node-key="id" ref="deptListTree" :highlight-current="true" :expand-on-click-node="false" accordion @current-change="deptListTreeCurrentChangeHandle"> </el-tree></div>
      </el-popover>
    </el-form-item>
    <el-form-item label="Address" prop="address">
      <el-input v-model="dataForm.address" placeholder="Address" :rows="2" type="textarea"></el-input>
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
    <el-form-item label="Facilities" prop="facilities">
      <el-input v-model="dataForm.facilities" placeholder="Facilities"></el-input>
    </el-form-item>
    <el-form-item label="Capacity" prop="capacity">
      <el-input-number v-model="dataForm.capacity" controls-position="right" :min="1" aria-label="Capacity"></el-input-number>
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
      <el-button @click="emits.emit(EMitt.OnCloseCurrTab)" size="large">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()" size="large">Confirm</el-button>
    </el-row>
  </el-form>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
import {IObject} from "@/types/interface";
import MultiUpload from "@/components/upload/multi-upload.vue";
import emits from "@/utils/emits";
import {EMitt} from "@/constants/enum";
import {useRoute} from "vue-router";
import Editor from "@tinymce/tinymce-vue";

const route = useRoute();
const categoryList = ref([]);

const deptList = ref([]);
const deptListPopover = ref();
const deptListTree = ref();

const tagList = ref([]);
const selectTagList = ref([]);

const fileList = ref([]);

const dataFormRef = ref();
const dataForm = reactive({
  id: '',
  name: '',
  catId: '',
  deptId: '',
  address: '',
  description: '',
  facilities: '',
  capacity: 30,
  deptName: '',
  imageDTOList: [],
  tagDTOList: []
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
  ],
  address: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'change' }
  ],
  capacity: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'change' }
  ]
});

const getCategoryList = () => {
  return baseService.get("/space/category/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    categoryList.value = res.data;
  });
};

const deptListTreeCurrentChangeHandle = (data: IObject) => {
  dataForm.deptId = data.id;
  dataForm.department = data.name;
  deptListPopover.value.hide();
};

const getDeptList = () => {
  return baseService.get("/sys/dept/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    deptList.value = res.data;
  });
};

const getTagList = () => {
  return baseService.get("/space/tag/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    tagList.value = res.data;
  });
};

const getInfo = (id: number) => {
  baseService.get("/space/space/" + id).then((res) => {
    Object.assign(dataForm, res.data);
    for (let i = 0; i < res.data.imageDTOList.length; i++) {
      let imageDTO = res.data.imageDTOList[i];
      fileList.value.push({id: imageDTO.id, url: imageDTO.imageUrl})
    }
    for (let i = 0; i < res.data.tagDTOList.length; i++) {
      let tagDTO = res.data.tagDTOList[i];
      selectTagList.value.push(tagDTO.id);
    }
  });
};

const init = (id?: number) => {
  dataForm.id = "";

  getCategoryList();
  getDeptList();
  getTagList();

  // Reset form data
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (id) {
    getInfo(id);
  }
};

const imageUploadHandle = (imageDTOList) => {
  dataForm.imageDTOList = imageDTOList
}

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    dataForm.imageDTOList.forEach((imageDTO: any) => {
      imageDTO.spaceId = dataForm.id;
    })
    selectTagList.value.forEach((tagId) => {
      dataForm.tagDTOList.push({id: tagId, spaceId: dataForm.id});
    });
    (!dataForm.id ? baseService.post : baseService.put)("/space/space", dataForm).then((res) => {
      emits.emit(EMitt.OnCloseCurrTab);
      ElMessage.success({
        message: 'Success',
        duration: 500,
      });
    });
  });
};

onMounted(() => {
  init(route.params.id);
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
