<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
      <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
        <el-form-item label="Username" prop="username">
          <el-input v-model="dataForm.username" placeholder="Username" disabled></el-input>
        </el-form-item>
        <el-form-item label="Mobile" prop="mobile">
          <el-input v-model="dataForm.mobile" placeholder="Mobile" disabled></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="dataForm.email" placeholder="Email" disabled></el-input>
        </el-form-item>
        <el-form-item label="Type" prop="type">
          <el-select
            v-model="dataForm.type"
            placeholder="Select"
            @change="changePermission"
          >
            <el-option :key="'Staff'" :label="'Staff'" :value="'Staff'"/>
            <el-option :key="'Student'" :label="'Student'" :value="'Student'"/>
            <el-option :key="'Other'" :label="'Other'" :value="'Other'"/>
          </el-select>
        </el-form-item>
        <el-form-item label="Permission">
          <el-checkbox v-model="dataForm.spacePermission" label="Space" :true-value="1" :false-value="0"/>
          <el-checkbox v-model="dataForm.servicePermission" label="Service" :true-value="1" :false-value="0"/>
          <el-checkbox v-model="dataForm.accommodationPermission" label="Accommodation" :true-value="1" :false-value="0"/>
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
  id: '',
  username: '',
  mobile: '',
  password: '',
  email: '',
  type: '',
  spacePermission: 0,
  servicePermission: 0,
  accommodationPermission: 0,
  createDate: ''
});

const rules = ref({
  type: [
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
  baseService.get("/client/user/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

const changePermission = (value: any) => {
  dataForm.spacePermission = 0;
  dataForm.servicePermission = 0;
  dataForm.accommodationPermission = 0;

  if (value == "Staff") {
    dataForm.spacePermission = 1;
    dataForm.servicePermission = 1;
    dataForm.accommodationPermission = 1;
  } else if (value == "Student") {
    dataForm.spacePermission = 1;
    dataForm.accommodationPermission = 1;
  } else {
    dataForm.spacePermission = 1;
    dataForm.servicePermission = 1;
  }
}

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/client/user", dataForm).then((res) => {
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
