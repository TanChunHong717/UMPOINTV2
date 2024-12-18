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
import { computed } from 'vue';
import { useStore } from 'vuex';

let store = useStore();

// Using computed to ensure reactivity with Vuex store
const userId = computed(() => store.state.auth.userId);
const userToken = computed(() => store.state.auth.token);
</script>