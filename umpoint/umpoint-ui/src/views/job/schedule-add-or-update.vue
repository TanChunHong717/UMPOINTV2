<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="beanName" label="bean name">
        <el-input v-model="dataForm.beanName" placeholder="spring bean name, like: testTask"></el-input>
      </el-form-item>
      <el-form-item prop="params" label="Params">
        <el-input v-model="dataForm.params" placeholder="Params"></el-input>
      </el-form-item>
      <el-form-item prop="cronExpression" label="cron expression">
        <el-input v-model="dataForm.cronExpression" placeholder="Example: 0 0 12 * * ?"></el-input>
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
  beanName: "",
  params: "",
  cronExpression: "",
  remark: "",
  status: 0
});

const rules = ref({
  beanName: [{ required: true, message: "Required field can not be empty", trigger: "blur" }],
  cronExpression: [{ required: true, message: "Required field can not be empty", trigger: "blur" }]
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
  baseService.get(`/sys/schedule/${id}`).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    const fn = !dataForm.id ? baseService.post("/sys/schedule", dataForm) : baseService.put("/sys/schedule", dataForm);
    fn.then((res) => {
      ElMessage.success({
        message: "Success",
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

<style lang="less">
.schedule-cron {
  &-popover {
    width: auto !important;
    min-width: 550px !important;
  }
}
</style>
