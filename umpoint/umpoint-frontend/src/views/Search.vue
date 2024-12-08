<template>
    <BaseLayout>
        <template #title>Facility Search</template>

        <!-- fake element to highlight anchor -->
        <template :id="facilityType"></template>

        <el-row :gutter="12" style="margin-block-start: 1em">
            <el-col :sm="24" :md="6">
                <el-anchor>
                    <el-anchor-link href="#space"> Space </el-anchor-link>
                    <el-anchor-link href="#service"> Service </el-anchor-link>
                    <el-anchor-link href="#accommodation">
                        Accommodation
                    </el-anchor-link>
                </el-anchor>
                <el-affix>
                    <el-form label-position="top">
                        <el-form-item label="Name">
                            <el-input v-model="searchForm.name" />
                        </el-form-item>
                        <el-form-item label="Department">
                            <el-tree-select
                                v-model="searchForm.deptId"
                                :data="departmentsOptions"
                                check-strictly
                                :render-after-expand="false"
                            />
                        </el-form-item>
                        <el-form-item label="Category">
                            <el-select v-model="searchForm.catId">
                                <el-option
                                    v-for="item in categoriesOptions"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
                        <el-button>Search</el-button>
                    </el-form>
                </el-affix>
            </el-col>
            <el-col :sm="24" :md="18">
                <h2>Showing 1 facility in Universiti Malaya, Kuala Lumpur</h2>
                <el-text>
                    Price shown does not include technician fee and might differ
                    after login.
                </el-text>
                <br />
                <br />
                <el-card v-for="facilityInfo of facilitiesRes">
                    <el-row :gutter="12">
                        <el-col :span="6">
                            <el-image
                                :src="
                                    transformGallery(facilityType, facilityInfo)
                                        .gallery[0].imageUrl
                                "
                                fit="cover"
                            ></el-image>
                        </el-col>
                        <el-col :span="15">
                            <h4>{{ facilityInfo.name }}</h4>
                            <li class="info-item">
                                <svg-icon
                                    class="info-icon"
                                    type="mdi"
                                    :path="mdiMapMarkerOutline"
                                ></svg-icon>
                                {{ facilityInfo.deptName }}
                            </li>
                            <li class="info-item">
                                <svg-icon
                                    class="info-icon"
                                    type="mdi"
                                    :path="mdiOfficeBuildingOutline"
                                ></svg-icon>
                                {{ facilityInfo.category }}
                                <div class="tags">
                                    <el-tag
                                        type="info"
                                        v-for="tag in facilityInfo.spcTagDTOList"
                                    >
                                        {{ tag.tagName }}
                                    </el-tag>
                                </div>
                            </li>
                            <li class="info-item">
                                <svg-icon
                                    class="info-icon"
                                    type="mdi"
                                    :path="mdiAccountGroup"
                                ></svg-icon>
                                {{ facilityInfo.capacity }} person(s)
                            </li>
                            <li class="info-item">
                                <svg-icon
                                    class="info-icon"
                                    type="mdi"
                                    :path="mdiToolboxOutline"
                                ></svg-icon>
                                {{ facilityInfo.facilities }}
                            </li>
                            <li
                                class="info-item"
                                v-if="facilityInfo.spcBookingRuleDTO"
                            >
                                <svg-icon
                                    class="info-icon"
                                    type="mdi"
                                    :path="mdiClockOutline"
                                ></svg-icon>
                                Open from
                                {{
                                    `${
                                        facilityInfo.spcBookingRuleDTO.startTime.split(
                                            ":"
                                        )[0]
                                    }:${
                                        facilityInfo.spcBookingRuleDTO.startTime.split(
                                            ":"
                                        )[1]
                                    }`
                                }}
                                to
                                {{
                                    `${
                                        facilityInfo.spcBookingRuleDTO.endTime.split(
                                            ":"
                                        )[0]
                                    }:${
                                        facilityInfo.spcBookingRuleDTO.endTime.split(
                                            ":"
                                        )[1]
                                    }`
                                }}
                            </li>
                            <br />

                            <el-descriptions
                                border
                                size="small"
                                direction="vertical"
                                :column="3"
                            >
                                <el-descriptions-item
                                    label="Per hour"
                                    v-if="facilityInfo.hourPrice"
                                >
                                    <el-text size="large" tag="b">
                                        RM
                                        {{ facilityInfo.hourPrice }}
                                    </el-text>
                                </el-descriptions-item>
                                <el-descriptions-item
                                    label="Per 4 hour"
                                    v-if="facilityInfo.fourHoursPrice"
                                >
                                    <el-text size="large" tag="b">
                                        RM
                                        {{ facilityInfo.fourHoursPrice }}
                                    </el-text>
                                </el-descriptions-item>
                                <el-descriptions-item
                                    label="Per day"
                                    v-if="facilityInfo.dayPrice"
                                >
                                    <el-text size="large" tag="b">
                                        RM
                                        {{ facilityInfo.dayPrice }}
                                    </el-text>
                                </el-descriptions-item>
                            </el-descriptions>
                        </el-col>
                        <el-col :span="3">
                            <RouterLink :to="`/${facilityType}/${facilityInfo.id}`">
                                <el-button>Details</el-button>
                            </RouterLink>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>
    </BaseLayout>
</template>

<script setup>
import {
    mdiMapMarkerOutline,
    mdiOfficeBuildingOutline,
    mdiAccountGroup,
    mdiToolboxOutline,
    mdiClockOutline,
} from "@mdi/js";
import { onMounted, reactive, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
    getFacilityCategories,
    getDepartments,
    getFacilities,
    transformGallery,
} from "@/helpers/api-facility";

const router = useRouter();
const route = useRoute();

// get facility type
const facilityType = ref("space");
watch(
    () => route.hash,
    (hash, oldhash) => {
        if (oldhash && oldhash !== hash) {
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
        updateCategories(facilityType.value);
    },
    { immediate: true }
);

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

        if (facilityType.value) getSearchFacilities();
    },
    { immediate: true }
);
function clearSearchFormCatId() {
    searchForm.catId = "";
}

// search result
const facilitiesRes = ref();
function getSearchFacilities() {
    if (!facilityType || !facilityType.value) return;

    let params = {};
    if (searchForm.name) params.name = searchForm.name;
    if (searchForm.deptId) params.deptId = searchForm.deptId;
    if (searchForm.catId) params.catId = searchForm.catId;

    getFacilities(facilityType.value, params).then((res) => {
        console.log(res.data.data);
        facilitiesRes.value = res.data.data.list;
    });
}

// facility dropdown
const departmentsOptions = ref([]);
function updateDepartments() {
    getDepartments().then((res) => {
        departmentsOptions.value = transformToTreeStructure(
            res.data.data[0]
        ).children;
    });
}
function transformToTreeStructure(obj) {
    // Create a new object with the transformed keys
    const transformedObj = { ...obj };

    // Recursively traverse and update each node
    if (transformedObj.children && Array.isArray(transformedObj.children)) {
        transformedObj.children = transformedObj.children.map((child) =>
            transformToTreeStructure(child)
        );
    }

    // Replace 'name' with 'label' and 'id' with 'value'
    if (transformedObj.name) {
        transformedObj.label = transformedObj.name;
    }
    if (transformedObj.id) {
        transformedObj.value = transformedObj.id;
    }

    return transformedObj;
}
onMounted(() => {
    updateDepartments();
});

// category dropdown
const categoriesOptions = ref([]);
function updateCategories(facilityType) {
    getFacilityCategories(facilityType).then((res) => {
        console.log(res.data.data);
        categoriesOptions.value = res.data.data.map((item) => ({
            label: item.name,
            value: item.id,
        }));
    });
}
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
