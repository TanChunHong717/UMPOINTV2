<template>
    <section v-if="$slots.title" id="page-title" class="page-title-pattern">
        <div class="container clearfix">
            <h1 class="section-title">
                <slot name="title"></slot>
            </h1>
            <span class="section-subtitle">
                <slot name="subtitle"></slot>
            </span>
            <el-breadcrumb class="breadcrumb">
                <el-breadcrumb-item
                    v-for="(item, index) in breadList"
                    :key="item.name"
                >
                    <router-link
                        v-if="item.name != name && index != 1"
                        :to="{ path: item.path === '' ? '/' : item.path }"
                        >{{ item.meta.title }}
                    </router-link>
                    <span v-else>{{ item.meta.title }}</span>
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
    </section>
    <el-main id="content" class="container" v-loading="loading" v-bind="$attrs">
        <slot></slot>
    </el-main>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRoute } from "vue-router";

// background image for the header
const props = defineProps({
    headerBackgroundImage: {
        type: String,
        default: 'url("/images/facility.png")',
    },
    loading: Boolean,
});

// slots
defineSlots<{
    default(props: any): void;
    title(props: any): void;
    subtitle(props: any): void;
}>();

// breadcrumbs
const route = useRoute();
const name = ref(route.name);
const breadList = ref(route.matched);
</script>

<style scoped>
#page-title {
    position: relative;
    padding: 3.5rem 0;
    border-bottom: 1px solid #eee;
}

.page-title-pattern {
    background-image: v-bind(headerBackgroundImage);
    background-size: 100vw;
}

#page-title .container {
    position: relative;
}

#page-title h1 {
    padding: 0;
    margin: 0;
    line-height: 1;
    /* color: #fff; */
    font-size: 2rem;
    font-weight: 280;
    max-inline-size: 75%;
    text-transform: uppercase;
}

#page-title span.section-subtitle {
    display: block;
    margin-top: 10px;
    font-weight: 600;
    /* color: #fff; */
    font-size: 1.125rem;
    max-inline-size: 75%;
}

#page-title span.section-subtitle:empty {
    display: none;
}

#page-title .breadcrumb {
    position: absolute !important;
    top: 50% !important;
    right: 15px !important;
    background-color: transparent !important;
    padding: 0 !important;
    font-size: 90%;
    transform: translateY(-50%);
}

#content {
    padding-block: 1rem;
    overflow-x: hidden;
}
</style>
