<template>
    <BaseLayout>
        <template #title>Chat History</template>

        <ChatComponent
            v-if="isLoaded"
            :user-id="userId"
            :user-token="userToken"
        ></ChatComponent>
        <el-card v-else v-loading="true" style="height: 10em;">
        </el-card>
    </BaseLayout>
</template>

<script setup lang="ts">
import ChatComponent from "@/components/user/ChatComponent.vue";
import { computed } from "vue";
import { useStore } from "vuex";

let store = useStore();

// Using computed to ensure reactivity with Vuex store
const userId = computed(() => store.state.auth.userId);
const userToken = computed(() => store.state.auth.token);

const isLoaded = computed(() => !!userId.value && !!userToken.value);
</script>
