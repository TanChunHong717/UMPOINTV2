<template>
    <section id="hero-title">
        <div id="hero-darken">
            <h1>UMPoint</h1>
            <div>
                One Stop Center for Facility Booking<br />
                @ Universiti Malaya
            </div>
        </div>
    </section>

    <el-input
        v-model="searchInput"
        @keyup.enter="searchFacility"
        id="search-input"
        class="search-input"
        placeholder="SEARCH NOW"
    >
        <template #prefix>
            <SvgIcon
                style="cursor: pointer"
                type="mdi"
                :path="mdiMagnify"
                @click.prevent="searchFacility"
            />
        </template>
    </el-input>

    <BaseLayout>
        <el-divider class="hero-divider" content-position="left"
            >DISCOVER OUR FACILITIES</el-divider
        >

        <div class="card-grid">
            <!-- <el-card>
                <el-image
                    src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAeAXCYHdm1-SXe-evCVF1VlhelqfXEG8TGw&s"
                    fit="cover"
                    style="width: 100%"
                ></el-image>
                <div slot="header" class="clearfix">
                    <h4>Test facility</h4>
                </div>
                <div>
                    <p>Auditorium - 2</p>
                </div>
                <RouterLink :to="`/facility/2`">Visit test location</RouterLink>
            </el-card> -->
            <el-card v-for="facility in facilities" :key="facility.id">
                <el-image
                    :src="
                        facility.gallery.length > 0
                            ? facility.gallery[0].imageUrl
                            : 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/240px-No_image_available.svg.png'
                    "
                    fit="cover"
                    style="width: 100%; aspect-ratio: 16/9"
                ></el-image>
                <div slot="header" class="clearfix">
                    <h4>{{ facility.name }}</h4>
                </div>
                <div>
                    <p>{{ facility.category }} - {{ facility.deptName }}</p>
                </div>
                <RouterLink :to="`/space/${facility.id}`">
                    <el-button>Visit location</el-button></RouterLink
                >
            </el-card>
        </div>
    </BaseLayout>
</template>

<script setup>
import { mdiMagnify } from "@mdi/js";
import { getFacilities, transformGallery } from "@/helpers/api-facility";
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const facilities = ref([]);
getFacilities("space").then((response) => {
    if (response.status !== 200 || response.data.code !== 0) {
        console.error("Failed to get facilities");
        return;
    }
    console.log(response.data);
    facilities.value = response.data.data.list.map((facility) =>
        transformGallery("space", facility)
    );
});

// search feature
const searchInput = ref("");
const searchFacility = () => {
    console.log("Searching for: ", searchInput.value);
    router.push({ path: "/search", query: { q: searchInput.value } });
};
</script>

<style scoped>
#hero-title {
    position: relative;
    width: 100%;
    height: max-content;

    background: url(/images/hero-cover.jpg) no-repeat center;
    background-size: 100vw;

    #hero-darken {
        padding-inline: 0;
        padding-block-start: 3em;
        padding-block-end: 4em;
        height: max-content;
        text-align: center;
        background-color: rgba(0, 0, 0, 0.25);

        h1 {
            font-size: 3rem;
            font-weight: 250;
            margin: 0;
            color: #fff;
        }
        div {
            font-size: 1.5rem;
            font-weight: 650;
            margin-block-start: 1em;
            color: #fff;
        }
    }
}

.search-input {
    display: flex;
    height: 3em;
    margin-block-start: -1.5em;
    margin-block-end: 0.5em;
    max-width: 65ch;
    width: 90%;
    margin-inline: auto;
    font-size: 1.05rem;
}

.card-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 1em;
    margin-block-start: 1em;

    .el-card h4 {
        margin-block: 0.5em;
    }
}
</style>

<style>
.search-input .el-input__wrapper {
    border-radius: 5em;
    padding-inline: 1.5em;
    box-shadow: var(--el-box-shadow);
    &:hover {
        box-shadow: var(--el-box-shadow-dark);
    }
}
.el-divider.hero-divider .el-divider__text {
    letter-spacing: 2px;
    font-size: 1.05rem;
    font-weight: 650;
}
</style>
