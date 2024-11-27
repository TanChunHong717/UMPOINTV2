<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="User ID that is reported" prop="reportedUserId">
        <el-input v-model="dataForm.reportedUserId" placeholder="User ID that is reported"></el-input>
      </el-form-item>
          <el-form-item label="User Type for ID that is reported" prop="reportedUserType">
        <el-input v-model="dataForm.reportedUserType" placeholder="User Type for ID that is reported"></el-input>
      </el-form-item>
          <el-form-item label="Reported chat room ID" prop="chatRoomId">
        <el-input v-model="dataForm.chatRoomId" placeholder="Reported chat room ID"></el-input>
      </el-form-item>
          <el-form-item label="Reported message ID" prop="messageId">
        <el-input v-model="dataForm.messageId" placeholder="Reported message ID"></el-input>
      </el-form-item>
          <el-form-item label="Reason" prop="reason">
        <el-input v-model="dataForm.reason" placeholder="Reason"></el-input>
      </el-form-item>
          <el-form-item label="User ID that send this report" prop="reportedBy">
        <el-input v-model="dataForm.reportedBy" placeholder="User ID that send this report"></el-input>
      </el-form-item>
          <el-form-item label="User Type for Id that send this report" prop="reportedByType">
        <el-input v-model="dataForm.reportedByType" placeholder="User Type for Id that send this report"></el-input>
      </el-form-item>
          <el-form-item label="Creation Date" prop="createdAt">
        <el-input v-model="dataForm.createdAt" placeholder="Creation Date"></el-input>
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
  id: '',  reportedUserId: '',  reportedUserType: '',  chatRoomId: '',  messageId: '',  reason: '',  reportedBy: '',  reportedByType: '',  createdAt: ''});

const rules = ref({
          reportedUserId: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          reportedUserType: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          chatRoomId: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          messageId: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          reason: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          reportedBy: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          reportedByType: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ],
          createdAt: [
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
  baseService.get("/chat/chatuserreport/" + id).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/chat/chatuserreport", dataForm).then((res) => {
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
