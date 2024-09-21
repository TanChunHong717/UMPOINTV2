<template>
    <BaseLayout :loading="loading">
        <template #title>Facility Information</template>

        <h2>Dewan Tunku Chancellor</h2>
        <el-carousel arrow="always" trigger="click" :autoplay="false">
            <el-carousel-item v-for="item in 5" :key="item">
                <img :src="getImgUrl(item - 1)" />
            </el-carousel-item>
        </el-carousel>

        <br>

        <el-row :gutter="8">
            <el-col :xs="24" :sm="16">
                <el-card class="info" shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span class="el-descriptions__title">Facilities</span>
                        </div>
                    </template>
                    <el-text>
                        <el-icon aria-label="Location">
                            <svg-icon type="mdi" :path="mdiMapMarkerOutline"></svg-icon>
                        </el-icon>
                        Faculty of Law
                    </el-text>
                    <el-text>
                        <el-icon aria-label="Category">
                            <svg-icon type="mdi" :path="mdiOfficeBuildingOutline"></svg-icon>
                        </el-icon>
                        Auditorium
                    </el-text>
                    <el-text>
                        <el-icon aria-label="Maximum capacity">
                            <svg-icon type="mdi" :path="mdiAccountGroup"></svg-icon>
                        </el-icon>
                        150 persons
                    </el-text>
                    <el-text>
                        <el-icon aria-label="Facilities">
                            <svg-icon type="mdi" :path="mdiToolboxOutline"></svg-icon>
                        </el-icon>
                        Kerusi, Meja, Lcd Projector & Skrin, Sound System, Micro Wave Oven, Peti Sejuk, Penapis Air
                        Coway,
                        Penapis Udara Coway, Wifi Tidak Disedikan (perlu Buat Permohonan)
                    </el-text>
                </el-card>
            </el-col>
            <el-col :xs="24" :sm="8">
                <el-card class="info" shadow="hover">
                    <template #header>
                        <div class="card-header card-header-action-call">
                            <span class="el-descriptions__title">Pricing</span>
                            <RouterLink :to="bookingUrl">
                                <el-button type="primary">Book now</el-button>
                            </RouterLink>
                        </div>
                    </template>
                    <el-descriptions border size="small" :column="1">
                        <el-descriptions-item label="Per hour">
                            <el-text size="large" tag="b">RM 200</el-text>
                        </el-descriptions-item>
                        <el-descriptions-item label="Per 4 hour">
                            <el-text size="large" tag="b">RM 700</el-text>
                        </el-descriptions-item>
                        <el-descriptions-item label="Per day">
                            <el-text size="large" tag="b">RM 1000</el-text>
                        </el-descriptions-item>
                    </el-descriptions>
                </el-card>
            </el-col>
        </el-row>
        <el-row>
            <el-card class="info" shadow="hover">
                <template #header>
                    <div class="card-header card-header-action-call">
                        <span>Person In Charge @ <strong>Faculty of Law</strong></span>
                        <el-button type="primary" plain>
                            <template #icon>
                                <svg-icon type="mdi" :path="mdiMessageTextOutline"></svg-icon>
                            </template>
                            Chat
                        </el-button>
                    </div>
                </template>
                <el-text type="danger">
                    <el-icon aria-label="Warning">
                        <svg-icon type="mdi" :path="mdiExclamationThick" color="red"></svg-icon>
                    </el-icon>
                    This facility requires prior contact before booking
                </el-text>
                <el-text>
                    <el-icon aria-label="Contact person">
                        <svg-icon type="mdi" :path="mdiCardAccountDetailsOutline"></svg-icon>
                    </el-icon>
                    Pn. Noraisa bin Mohammad
                </el-text>
                <el-text>
                    <el-icon aria-label="Phone number">
                        <svg-icon type="mdi" :path="mdiPhone"></svg-icon>
                    </el-icon>
                    +6012-3456789
                </el-text>
                <el-text>
                    <el-icon aria-label="Email">
                        <svg-icon type="mdi" :path="mdiEmailArrowRightOutline"></svg-icon>
                    </el-icon>
                    sample@test.um.com.my
                </el-text>
            </el-card>
        </el-row>
        <el-row>
            <el-card shadow="never">
                <template #header>
                    <div class="card-header">
                        <span class="el-descriptions__title">Details</span>
                    </div>
                </template>
                <el-text style="white-space: pre-wrap;">Before completing your booking on UMPoint, please contact the
                    staff in
                    charge regarding your event:
                    WAN NUR ANI BINTI WAN HAR (Assistant Registrar)
                    Email : wannurani_@um.edu.my
                    Tel: +603-79673056
                    or
                    Mr. Zaid bin Idris (Assistant Engineer)
                    Email: yed@um.edu.my
                    Tel: +603-79673056

                    Staf teknikal membantu dalam urusan berikut:
                    Membuka/menutup pintu ruang/suis elektrik (lampu/aircond)
                    Peralatan ICT/elektrik/mekanikal/perabot tersedia untuk digunakan
                    Menyelesaikan aduan berkaitan kerja teknikal dalam tempoh ruang digunakan

                    Technical staff assist in the following:
                    Opening / closing the door / electric switch door (lamp / aircond)
                    ICT / electrical / mechanical / furniture equipment is available for use
                    Solve technical work complaints within the space used
                </el-text>
            </el-card>
        </el-row>
        <el-row>
            <el-card>
                <template #header>
                    <div class="card-header">
                        <span class="el-descriptions__title">Map</span>
                    </div>
                </template>
                Map here
            </el-card>
        </el-row>
    </BaseLayout>
</template>

<script setup>
import {
    mdiMapMarkerOutline,
    mdiOfficeBuildingOutline,
    mdiAccountGroup,
    mdiToolboxOutline,
    mdiExclamationThick,
    mdiCardAccountDetailsOutline,
    mdiEmailArrowRightOutline,
    mdiPhone,
    mdiMessageTextOutline,
} from '@mdi/js';
import { reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getFacilityInformation } from '@/helpers/api.js'

const route = useRoute()

const loading = ref(false)
const post = reactive({})
const error = ref(null)

// watch the params of the route to fetch the data on change
watch(() => route.params.id, fetchData, { immediate: false })

async function fetchData(id) {
    error.value = post.value = null
    loading.value = true
    
    try {
        post.value = await getFacilityInformation(id)
    } catch (err) {
        error.value = err.toString()
    } finally {
        loading.value = false
    }
}

// build booking form url
const bookingUrl = `/facility/${route.params.id}/reserve`

/** temp fetch image url */
const baseUrl =
    "https://raw.githubusercontent.com/vueComponent/ant-design-vue/main/components/carousel/demo/";
const getImgUrl = (i) => {
    return `${baseUrl}abstract0${i + 1}.jpg`;
};

</script>

<style scoped>
.el-card {
    width: 100%;
    height: calc(100% - 12px);
    margin-block-end: 8px;
}

.el-card .el-text {
    display: block;
    margin-block-start: 0;
    margin-block-end: 0.75em;
    line-height: 1.4;

    >.el-text {
        display: inline;
    }

    &:last-child {
        margin-block-end: 0;
    }
}

.card-header-action-call {
    display: flex;
    align-items: center;

    >span {
        flex: 1;
    }

    >button {
        text-align: right;
    }
}

.el-icon {
    display: inline-block;
    font-size: 1.2em;
    margin-inline-end: 0.75em;
}
</style>
