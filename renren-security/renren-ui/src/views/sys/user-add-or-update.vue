<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? 'Add' : 'Update'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
      <el-form-item prop="username" label="Username">
        <el-input v-model="dataForm.username" placeholder="Username"></el-input>
      </el-form-item>
      <el-form-item prop="deptName" label="Department">
        <ren-dept-tree v-model="dataForm.deptId" placeholder="Department" v-model:deptName="dataForm.deptName"></ren-dept-tree>
      </el-form-item>
      <el-form-item prop="password" label="Password" :class="{ 'is-required': !dataForm.id }">
        <el-input v-model="dataForm.password" type="password" placeholder="Password"></el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword" label="Confirm password" :class="{ 'is-required': !dataForm.id }">
        <el-input v-model="dataForm.confirmPassword" type="password" placeholder="Confrim password"></el-input>
      </el-form-item>
      <el-form-item prop="realName" label="Name">
        <el-input v-model="dataForm.realName" placeholder="Name"></el-input>
      </el-form-item>
      <el-form-item prop="gender" label="Gender">
        <ren-radio-group v-model="dataForm.gender" dict-type="gender"></ren-radio-group>
      </el-form-item>
      <el-form-item prop="email" label="Email">
        <el-input v-model="dataForm.email" placeholder="Email"></el-input>
      </el-form-item>
      <el-form-item prop="mobile" label="Mobile">
        <el-input v-model="dataForm.mobile" placeholder="Mobile"></el-input>
      </el-form-item>
      <el-form-item prop="roleIdList" label="Role" class="role-list">
        <el-select v-model="dataForm.roleIdList" multiple placeholder="Role">
          <el-option v-for="role in roleList" :key="role.id" :label="role.name" :value="role.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="status" label="Status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">Suspend</el-radio>
          <el-radio :label="1">Normal</el-radio>
        </el-radio-group>
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
import { isEmail, isMobile } from "@/utils/utils";
import { IObject } from "@/types/interface";
import { ElMessage } from "element-plus";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const roleList = ref<any[]>([]);
const dataFormRef = ref();

const dataForm = reactive({
  id: "",
  username: "",
  deptId: "",
  deptName: "",
  password: "",
  confirmPassword: "",
  realName: "",
  gender: 0,
  email: "",
  mobile: "",
  roleIdList: [] as IObject[],
  highRoleIdList: [] as IObject[],
  status: 1
});

const validatePassword = (rule: any, value: string, callback: (e?: Error) => any): any => {
  if (!dataForm.id && !/\S/.test(value)) {
    return callback(new Error("Require field can not be empty"));
  }
  callback();
};
const validateConfirmPassword = (rule: any, value: string, callback: (e?: Error) => any): any => {
  if (!dataForm.id && !/\S/.test(value)) {
    return callback(new Error("Require field can not be empty"));
  }
  if (dataForm.password !== value) {
    return callback(new Error("Password and confirm password are not identical"));
  }
  callback();
};
const validateEmail = (rule: any, value: string, callback: (e?: Error) => any): any => {
  if (value && !isEmail(value)) {
    return callback(new Error("Email format wrong"));
  }
  callback();
};
// const validateMobile = (rule: any, value: string, callback: (e?: Error) => any): any => {
//   if (value && !isMobile(value)) {
//     return callback(new Error("Mobile format error"));
//   }
//   callback();
// };
const rules = ref({
  username: [{ required: true, message: "Require field can not be empty", trigger: "blur" }],
  deptName: [{ required: true, message: "Require field can not be empty", trigger: "change" }],
  password: [{ validator: validatePassword, trigger: "blur" }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: "blur" }],
  realName: [{ required: true, message: "Require field can not be empty", trigger: "blur" }],
  email: [{ validator: validateEmail, trigger: "blur" }],
  mobile: [{ validator: validateMobile, trigger: "blur" }]
});

const init = (id?: number) => {
  visible.value = true;
  dataForm.id = "";
  dataForm.deptId = "";

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  Promise.all([getRoleList()]).then(() => {
    if (id) {
      getInfo(id);
    }
  });
};

// 获取角色列表
const getRoleList = () => {
  return baseService.get("/sys/role/list").then((res) => {
    roleList.value = res.data;
  });
};

// 获取信息
const getInfo = (id: number) => {
  baseService.get(`/sys/user/${id}`).then((res) => {
    Object.assign(dataForm, res.data);
    dataForm.highRoleIdList = dataForm.roleIdList.filter(id => !roleList.value.some(role => role.id === id));
    dataForm.roleIdList = dataForm.roleIdList.filter(id => !dataForm.highRoleIdList.includes(id))
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.id ? baseService.post : baseService.put)("/sys/user", {
      ...dataForm,
      roleIdList: [...dataForm.roleIdList,...dataForm.highRoleIdList]
    }).then((res) => {
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

<style lang="less" scoped>
.mod-sys__user {
  .role-list {
    .el-select {
      width: 100%;
    }
  }
}
</style>
