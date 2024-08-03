<template>
  <el-dialog v-model="visible" :title="!dataForm.spaceId ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="Tag ID" prop="tagId">
        <el-input v-model="dataForm.tagId" placeholder="Tag ID"></el-input>
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
  spaceId: '',  tagId: ''});

const rules = ref({
          tagId: [
      { required: true, message: 'Required fields cannot be empty', trigger: 'blur' }
    ]
  });

const init = (spaceId?: number) => {
  visible.value = true;
  dataForm.spaceId = "";

  // Reset form data
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (spaceId) {
    getInfo(spaceId);
  }
};

// Retrieve information
const getInfo = (spaceId: number) => {
  baseService.get("/space/spacetag/" + spaceId).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// Form submission
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.spaceId ? baseService.post : baseService.put)("/space/spacetag", dataForm).then((res) => {
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
