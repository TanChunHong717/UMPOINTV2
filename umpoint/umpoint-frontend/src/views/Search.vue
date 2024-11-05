<template>
    <BaseLayout>
        <template #title>Facility Search</template>

        <el-row :gutter="12" style="margin-block-start: 1em">
            <el-col :sm="24" :md="6">
                <el-affix>
                    <el-form label-position="top">
                        <el-form-item label="Description">
                            <el-input v-model="searchForm.desc" />
                        </el-form-item>
                        <el-form-item label="Event Date">
                            <el-date-picker
                                v-model="searchForm.date"
                                style="width: 100%"
                            />
                        </el-form-item>
                        <el-form-item label="Category">
                            <el-select v-model="searchForm.category">
                                <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>
                        </el-form-item>
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
                <el-card>
                    <el-row :gutter="12">
                        <el-col :span="6">
                            <el-image
                                src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAeAXCYHdm1-SXe-evCVF1VlhelqfXEG8TGw&s"
                                fit="cover"
                            ></el-image>
                        </el-col>
                        <el-col :span="15">
                            <h4>Tennis Court</h4>
                            <li class="info-item">
                                <svg-icon
                                    class="info-icon"
                                    type="mdi"
                                    :path="mdiMapMarkerOutline"
                                ></svg-icon>
                                Tennis Court, Sports Centre
                            </li>
                            <li class="info-item">
                                <svg-icon
                                    class="info-icon"
                                    type="mdi"
                                    :path="mdiOfficeBuildingOutline"
                                ></svg-icon>
                                Tennis Court
                                <div class="tags">
                                    <el-tag
                                        type="info"
                                        v-for="tag in facilityInfo.spcTagDTOList"
                                    >
                                        {{ tag }}
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
                            <li class="info-item">
                                <svg-icon
                                    class="info-icon"
                                    type="mdi"
                                    :path="mdiClockOutline"
                                ></svg-icon>
                                Open from 08:00 to 22:00
                            </li>
                            <br />

                            <el-descriptions
                                border
                                size="small"
                                direction="vertical"
                                :column="3"
                            >
                                <el-descriptions-item label="Per hour">
                                    <el-text size="large" tag="b">RM10</el-text>
                                </el-descriptions-item>
                                <el-descriptions-item
                                    label="Per 4 hour"
                                    v-if="facilityInfo.fourHoursPrice"
                                >
                                    <el-text size="large" tag="b">RM20</el-text>
                                </el-descriptions-item>
                                <el-descriptions-item label="Per day">
                                    <el-text size="large" tag="b"
                                        >RM200</el-text
                                    >
                                </el-descriptions-item>
                            </el-descriptions>
                        </el-col>
                        <el-col :span="3">
                            <RouterLink :to="`/facility/2`">
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
import { reactive, ref, watch } from "vue";
import { useRoute } from "vue-router";

const facilityInfo = reactive({
    spcTagDTOList: ["35m^2", "ISO Spec'ed", "Air Conditioned"],
    capacity: 4,
    facilities: "Air cond, ball refiller",
});
const searchForm = reactive({
    desc: "",
    date: "",
    category: "",
});
const options = [
    { value: "space", label: "Space" },
    { value: "equipment", label: "Equipment" },
    { value: "service", label: "Service" },
];

const route = useRoute();
const initSearchTerm = (keyword) => {
    searchForm.desc = keyword;
};
watch(
    () => route.query,
    (query) => {
        console.log(query);
        initSearchTerm(query.q);
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
