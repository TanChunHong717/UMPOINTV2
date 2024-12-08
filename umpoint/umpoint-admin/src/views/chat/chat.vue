<template>
  <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()" class="header-form">
    <div>
      <el-form-item label="Room mode">
        <el-select v-model="state.dataForm.mode" clearable>
          <el-option key="assigned" label="Assigned" value="assigned" />
          <el-option key="department" label="All in department" value="department" />
        </el-select>
      </el-form-item>
    </div>
        <ChatComponent
            :user-id="userId"
            :user-token="userToken"
        ></ChatComponent>
  </el-form>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import { useAppStore } from "@/store";

import { getToken } from "@/utils/cache";

import ChatComponent from "./ChatComponent.vue";

const view = reactive({
  dataForm: {
    mode: "assigned"
  }
});
const state = reactive({ ...useView(view), ...toRefs(view) });

const store = useAppStore();
const userId = ref(store.state.user.id);
const userToken = ref(getToken());
</script>
