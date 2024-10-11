<template>
  <el-dialog v-model="visible" title="Approve Space Booking" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item label="Technician" prop="technician">
        <el-select
          v-model="dataForm.technician"
          :placeholder="'Select ' + maxTechnicianNumber + ' technician' + ((maxTechnicianNumber > 1)?'s':'')"
          multiple
          :multiple-limit="maxTechnicianNumber"
        >
          <el-option
            v-for="user in userList"
            :key="user.id"
            :label="user.username"
            :value="user.id"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">Confirm</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();
const dataForm = ref({
  technician: []
});

const approveId = ref();
const maxTechnicianNumber = ref(0);
const userList = ref<{id: number; username: string}[]>([]);

const rules = ref({
  technician: [
    { required: true, message: 'Required fields cannot be empty', trigger: 'blur'},
    {
      validator: (rule: any, value: any, callback: any) => {
        console.log(value);
        if (!value || value.length != maxTechnicianNumber.value) {
          callback(new Error(`Please select ${maxTechnicianNumber.value} technician` + ((maxTechnicianNumber.value > 0)?'s':'')));
        } else {
          callback();
        }
      },
      trigger: 'change'
    }
  ]
});

const init = (id: number, maxTechnician: number) => {
  visible.value = true;

  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  getUserList();
  approveId.value = id;
  maxTechnicianNumber.value = maxTechnician;
};

const getUserList = async () => {
  return baseService.get("/sys/user/list").then((res) => {
    if (res.code !== 0) {
      return ElMessage.error(res.msg);
    }
    userList.value = res.data;
  });
};

const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    baseService.put("/space/booking/approve/" + approveId.value, dataForm.value.technician).then((res) => {
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
