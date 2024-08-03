<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="Name" prop="name">
        <el-input v-model="dataForm.name" placeholder="Name"></el-input>
      </el-form-item>
          <el-form-item label="Category ID" prop="catId">
        <el-input v-model="dataForm.catId" placeholder="Category ID"></el-input>
      </el-form-item>
          <el-form-item label="Department ID" prop="deptId">
        <el-input v-model="dataForm.deptId" placeholder="Department ID"></el-input>
      </el-form-item>
          <el-form-item label="Description" prop="description">
        <el-input v-model="dataForm.description" placeholder="Description"></el-input>
      </el-form-item>
          <el-form-item label="Facilities" prop="facilities">
        <el-input v-model="dataForm.facilities" placeholder="Facilities"></el-input>
      </el-form-item>
      </el-form>
    <template #footer>
      <el-button @click="visible = false">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">Confirm</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  id: '',  name: '',  catId: '',  deptId: '',  description: '',  facilities: ''});

const rules = ref({
          name: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          catId: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          deptId: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          description: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          facilities: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ]
  });

const init = (id?: number) => {
  visible.value = true;
  dataForm.id = "";

  // Reset form data
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (id) {
    getInfo(id);
  }
};

// Retrieve information
const getInfo = (id: number) => {
  baseService.get("/space/space/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/space/space", dataForm).then((res) => {
      ElMessage.success({
        message: 'Success',
        duration: 500,
        onClose: () => {
          visible.value = false;
          emit("refreshDataList");
        }
      });
    });
  });
};

defineExpose({
  init
});
</script>
