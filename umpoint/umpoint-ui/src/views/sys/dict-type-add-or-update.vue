<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="dictName" label="Dictionary name">
        <el-input v-model="dataForm.dictName" placeholder="Dictionary name"></el-input>
      </el-form-item>
      <el-form-item prop="dictType" label="Dictionary type">
        <el-input v-model="dataForm.dictType" placeholder="Dictionary type"></el-input>
      </el-form-item>
      <el-form-item prop="sort" label="Sort">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" label="Sort"></el-input-number>
      </el-form-item>
      <el-form-item prop="remark" label="Remark">
        <el-input v-model="dataForm.remark" placeholder="Remark"></el-input>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
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
  id: "",
  dictName: "",
  dictType: "",
  sort: 0,
  remark: ""
});

const init = (id?: number) => {
  visible.value = true;
  dataForm.id = "";

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (id) {
    getInfo(id);
  }
};

// 获取信息
const getInfo = (id: number) => {
  baseService.get(`/sys/dict/type/${id}`).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

const rules = ref({
  dictName: [{ required: true, message: "Require field can not be empty", trigger: "blur" }],
  dictType: [{ required: true, message: "Require field can not be empty", trigger: "blur" }],
  sort: [{ required: true, message: "Require field can not be empty", trigger: "blur" }]
});

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/sys/dict/type", dataForm).then((res) => {
      ElMessage.success({
        message: "成功",
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
