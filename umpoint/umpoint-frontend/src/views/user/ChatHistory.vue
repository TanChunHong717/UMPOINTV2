<template>
    <BaseLayout>
        <template #title>Chat History</template>

        <ChatComponent
            v-if="userId && userToken"
            :user-id="userId"
            :user-token="userToken"
        ></ChatComponent>
    </BaseLayout>
</template>

<script setup lang="ts">
import ChatComponent from '@/components/user/ChatComponent.vue';
import { useStore } from 'vuex';

let store = useStore();

const userId = store.state.auth.userId;
const userToken = store.state.auth.token;
</script>