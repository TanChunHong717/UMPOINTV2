<template>
  <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
    <el-form-item label="Username">
      <span>{{ user.username }}</span>
    </el-form-item>
    <el-form-item prop="password" label="Old password">
      <el-input v-model="dataForm.password" type="password" placeholder="Old password"></el-input>
    </el-form-item>
    <el-form-item prop="newPassword" label="New password">
      <el-input v-model="dataForm.newPassword" type="password" placeholder="New password"></el-input>
    </el-form-item>
    <el-form-item prop="confirmPassword" label="Confirm password">
      <el-input v-model="dataForm.confirmPassword" type="password" placeholder="Confrim password"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="dataFormSubmitHandle">Confirm</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts" setup>
import { computed, reactive, ref } from "vue";
import { IObject } from "@/types/interface";
import baseService from "@/service/baseService";
import { useAppStore } from "@/store";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
const router = useRouter();

const dataFormRef = ref();
const dataForm = reactive({
  password: "",
  newPassword: "",
  confirmPassword: ""
});

const store = useAppStore();
const user = computed(() => store.state.user);

const validateConfirmPassword = (rule: IObject, value: string, callback: (e?: Error) => any) => {
  if (dataForm.newPassword !== value) {
    return callback(new Error("Password and confirm password are not identical"));
  }
  callback();
};

const rules = ref({
  password: [{ required: true, message: "Require field can not be empty", trigger: "blur" }],
  newPassword: [{ required: true, message: "Require field can not be empty", trigger: "blur" }],
  confirmPassword: [
    { required: true, message: "Require field can not be empty", trigger: "blur" },
    { validator: validateConfirmPassword, trigger: "blur" }
  ]
});

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    baseService.put("/sys/user/password", dataForm).then((res) => {
      ElMessage.success({
        message: "Success",
        duration: 500,
        onClose: () => {
          router.replace("/login");
        }
      });
    });
  });
};
</script>
