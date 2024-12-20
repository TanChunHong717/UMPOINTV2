<template>
    <BaseLayout>
        <template #title>Facility Search</template>

        <!-- fake element to highlight anchor -->
        <template :id="facilityType"></template>

        <el-row :gutter="12">
            <el-col :sm="24" :md="6" style="margin-block-end: 1em">
                <el-anchor>
                    <el-anchor-link href="search#space"> Space </el-anchor-link>
                    <el-anchor-link href="search#service">
                        Service
                    </el-anchor-link>
                    <el-anchor-link href="search#accommodation">
                        Accommodation
                    </el-anchor-link>
                </el-anchor>
                <el-affix>
                    <el-form label-position="top">
                        <el-form-item label="Name">
                            <el-input v-model="searchForm.name" />
                        </el-form-item>
                        <el-form-item label="Department">
                            <DepartmentDropdown v-model="searchForm.deptId" />
                        </el-form-item>
                        <el-form-item label="Category">
                            <CategoriesDropdown
                                v-model="searchForm.catId"
                                :facilityType="facilityType"
                            />
                        </el-form-item>
                        <el-button @click.prevent="getSearchFacilities"
                            >Search</el-button
                        >
                    </el-form>
                </el-affix>
            </el-col>
            <el-col :sm="24" :md="18">
                <h2>
                    Showing {{ totalFacilitiesRes }} facility in Universiti
                    Malaya, Kuala Lumpur
                </h2>
                <el-text>
                    Price shown does not include technician fee and might differ
                    after login.
                </el-text>
                <br />
                <br />
                <SearchFacilityCard
                    v-for="facilityInfo in facilitiesRes"
                    :key="facilityInfo.id"
                    :facilityInfo="facilityInfo"
                    :facilityType="facilityType"
                    style="margin-bottom: 8px"
                />
                <el-pagination
                    v-model:current-page="currentPage"
                    @current-change="getSearchFacilities"
                    :hide-on-single-page="false"
                    :total="totalFacilitiesRes"
                    layout="prev, pager, next, jumper,->,slot"
                    :page-size="pageSize"
                >
                    <el-button>Compare timetable mode</el-button>
                </el-pagination>
            </el-col>
        </el-row>
    </BaseLayout>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getFacilities as getFacilitiesAPI } from "@/helpers/api-facility";
import DepartmentDropdown from "@/components/search/DepartmentDropdown.vue";
import SearchFacilityCard from "@/components/search/SearchFacilityCard.vue";
import CategoriesDropdown from "@/components/search/CategoriesDropdown.vue";

const router = useRouter();
const route = useRoute();

// get facility type
const facilityType = ref("space");
watch(
    () => route.hash,
    (hash, oldhash) => {
        if (oldhash && oldhash !== hash) {
            function clearSearchFormCatId() {
                searchForm.catId = "";
            }
            // change facility type, clear search term
            clearSearchFormCatId();
        }

        if (hash === "#space") {
            facilityType.value = "space";
        } else if (hash === "#service") {
            facilityType.value = "service";
        } else if (hash === "#accommodation") {
            facilityType.value = "accommodation";
        } else {
            router.replace({ query: route.query, hash: "#space" });
        }
    },
    { immediate: true }
);

// search result
const facilitiesRes = ref();
const totalFacilitiesRes = ref();
const pageSize = 10;
const currentPage = ref(1);
async function getSearchFacilities() {
    if (!facilityType || !facilityType.value) return;

    let params = {};
    if (searchForm.name && searchForm.name.trim() != "")
        params.name = searchForm.name;
    if (searchForm.deptId && searchForm.deptId.trim() != "")
        params.deptId = searchForm.deptId;
    if (searchForm.catId && searchForm.catId.trim() != "")
        params.catId = searchForm.catId;
    if (currentPage.value) params.page = currentPage.value;

    // update url
    router.push({ query: params, hash: "#" + facilityType.value });

    // update items
    let {
        data: {
            data: { list, total },
        },
    } = await getFacilitiesAPI(facilityType.value, {
        ...params,
        limit: pageSize,
    });

    facilitiesRes.value = list;
    totalFacilitiesRes.value = total;
}

// search form
const searchForm = reactive({
    name: "",
    deptId: "",
    catId: "",
});
watch(
    () => route.query,
    (query) => {
        if (query.q) searchForm.name = query.q;
        if (query.dept) searchForm.deptId = query.dept;
        if (query.cat) searchForm.catId = query.cat;
        if (query.page) currentPage.value = Number(query.page);

        if (facilityType.value) getSearchFacilities();
    },
    { immediate: true }
);
</script>

<style>
h2 {
    margin-block-start: 0;
    margin-block-end: 0.5em;
}
h4 {
    margin-block-start: 0;
    margin-block-end: 6px;
}
li {
    font-size: var(--el-font-size-base);
}
</style>
