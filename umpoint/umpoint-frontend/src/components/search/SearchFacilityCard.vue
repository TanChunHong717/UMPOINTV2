<template>
    <el-card
        @click="handleCardClick"
        shadow="hover"
        :body-class="isSelected ? 'selected' : ''"
    >
        <el-row :gutter="12">
            <el-col :span="8">
                <el-image
                    :src="
                        facilityInfo?.gallery.length > 0
                            ? facilityInfo.gallery[0].imageUrl
                            : 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/240px-No_image_available.svg.png'
                    "
                    fit="cover"
                ></el-image>
            </el-col>
            <el-col :span="16">
                <div class="action-button">
                    <el-checkbox
                        v-if="selectionMode == 'select'"
                        v-model="isSelected"
                        size="large"
                    />
                    <RouterLink v-else :to="facilityDetailUrl">
                        <el-button>Details </el-button>
                    </RouterLink>
                </div>

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
                <li class="info-item" v-if="!!facilityInfo.capacity">
                    <svg-icon
                        class="info-icon"
                        type="mdi"
                        :path="mdiAccountGroup"
                    ></svg-icon>
                    {{ facilityInfo.capacity }} person(s)
                </li>
                <li class="info-item" v-if="!!facilityInfo.facilities">
                    <svg-icon
                        class="info-icon"
                        type="mdi"
                        :path="mdiToolboxOutline"
                    ></svg-icon>
                    {{ facilityInfo.facilities }}
                </li>
                <li class="info-item" v-if="facilityInfo.spcBookingRuleDTO">
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
                            facilityInfo.spcBookingRuleDTO.endTime.split(":")[0]
                        }:${
                            facilityInfo.spcBookingRuleDTO.endTime.split(":")[1]
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
        </el-row>
    </el-card>
</template>

<script setup>
import {
    mdiMapMarkerOutline,
    mdiOfficeBuildingOutline,
    mdiAccountGroup,
    mdiToolboxOutline,
    mdiClockOutline,
} from "@mdi/js";
import { transformGallery } from "@/helpers/api-facility";
import { ref, computed } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

// facility information
const props = defineProps({
    facilityInfo: Object,
    facilityType: String,
});
const facilityInfo = ref(
    transformGallery(props.facilityType, props.facilityInfo)
);
const facilityDetailUrl = computed(
    () => `/${props.facilityType}/${facilityInfo.value.id}`
);

const emit = defineEmits(["change"]);

// selection mode
const selectionMode = defineModel("selectionMode", {
    event: "change",
    default: "redirect",
});
const isSelected = defineModel("isSelected", {
    event: "change",
    default: false,
});

function handleCardClick() {
    if (selectionMode.value === "select") {
        isSelected.value = !isSelected.value;
        emit("change", {
            isSelected: isSelected.value,
            facilityId: facilityInfo.value.id,
        });
    } else {
        router.push(facilityDetailUrl.value);
    }
}
</script>

<style scoped>
.action-button {
    float: inline-end;
    height: auto;
}
</style>

<style>
.selected {
    border-color: var(--el-color-primary-light-6);
    background-color: var(--el-color-primary-light-9);
}
</style>
