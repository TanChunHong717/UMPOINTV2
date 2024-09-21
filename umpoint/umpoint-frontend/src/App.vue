<template>
    <Topbar></Topbar>
    <UmHeader></UmHeader>
    <div id="content-view" class="container-fluid">
        <RouterView ></RouterView>
    </div>
    <UmFooter></UmFooter>
</template>

<script>
import Topbar from './components/base/Topbar.vue'
import UmHeader from './components/base/Header.vue'
import UmFooter from './components/base/Footer.vue'

export default {
    components: {
        Topbar,
        UmHeader,
        UmFooter
    }
}
</script>

<style>
#content-view {
    display: block;
    flex-grow: 1; /* grow to fill page and force footer to bottom */
    padding: 0;
}
</style>