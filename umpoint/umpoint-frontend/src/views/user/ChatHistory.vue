<template>
    <BaseLayout>
        <template #title>Chat History</template>

        <ChatComponent
            :user-id="userId"
        ></ChatComponent>
    </BaseLayout>
</template>

<script setup lang="ts">
import ChatComponent from '@/components/user/ChatComponent.vue';
import { useStore } from 'vuex';

let store = useStore();

const userId = store.state.auth.userId;
</script>