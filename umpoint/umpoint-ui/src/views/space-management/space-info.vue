<template>
  <div v-loading>
    <div v-if="!isLoading">
      <el-row justify="space-between" align="middle">
        <el-col :span="16">
          <h1 class="h1-text">{{ space.name }}</h1>
        </el-col>
        <el-col :span="8" class="end-justify">
          <el-button type="primary">Edit</el-button>
          <el-button class="ml-5" type="danger">Delete</el-button>
        </el-col>
      </el-row>
      <el-carousel height="300px" class="carousel-container">
        <template v-if="space && space.imageDTOList && space.imageDTOList.length > 0">
          <el-carousel-item class="center-justify image-bg" v-for="(imageDTO, index) in space.imageDTOList" :key="index">
            <img :src="imageDTO.imageUrl" style="max-height: 300px; object-fit: contain" />
          </el-carousel-item>
        </template>
        <template v-else>
          <div class="center-justify no-image">
            <el-empty description="No Image" />
          </div>
        </template>
      </el-carousel>
      <el-row class="in-col-row">
        <el-col :span="12">
          <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-apartment"></use></svg>
          Department: {{ space.deptName }}
        </el-col>
        <el-col :span="12">
          <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-appstore"></use></svg>
          Category: {{ space.category }}
        </el-col>
      </el-row>
      <el-row class="in-col-row">
        <el-col :span="12">
          <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-team"></use></svg>
          Capacity: {{ space.capacity }}
        </el-col>
        <el-col :span="12">
          <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-user"></use></svg>
          Manager: {{ space.manageru }}
        </el-col>
      </el-row>
      <el-row class="in-col-row">
        <el-col :span="24">
          <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-tag"></use></svg>
          Tag: <el-tag v-for="tag in space.tagDTOList" type="primary">{{ tag.tagName }} </el-tag>
        </el-col>
      </el-row>
      <el-row class="in-col-row">
        <el-col :span="24">
          <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-location"></use></svg>
          Address: {{ space.address }}
        </el-col>
      </el-row>
      <el-row v-if="space.facilities?.trim().length > 0">
        <el-col :span="24">
          <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-wrench"></use></svg>
          Facilities: {{ space.facilities }}
        </el-col>
      </el-row>
      <h2>Price</h2>
      <el-row>
        <el-col :span="8"><span class="hour_price">RM{{ space.hour_price }}</span> / Hour</el-col>
        <el-col :span="8" v-if="space.four_hour_price"><span class="four_hour_price">RM{{ space.four_hour_price }}</span> / 4 Hours</el-col>
        <el-col :span="8" v-if="space.day_price"><span class="day_price">RM{{ space.day_price }}</span> / Day</el-col>
      </el-row>
      <h2>Description</h2>
      <div v-html="space.description"></div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import {onMounted, defineProps, ref} from 'vue';
import baseService from "@/service/baseService";
import {useRoute} from "vue-router";

const route = useRoute()
const space = ref();
const isLoading = ref(true);

const getInfo = (id: number) => {
  baseService.get("/space/space/" + id).then((res) => {
    space.value = res.data;
    isLoading.value = false;
  });
};

onMounted(() => {
  getInfo(Number(route.params.id))
});
</script>
<style>
.h1-text {
  font-size: 1.75rem !important;
  margin: 0 0 10px 0;
}
.end-justify {
  display: flex;
  justify-content: end !important;
}
.carousel-container {
  min-height: 300px;
  margin-bottom: 10px;
}
.center-justify {
  display: flex;
  justify-content: center !important;
}
.image-bg {
  background-color: #393939;
}
.no-image {
  width: 100%;
  margin-top: 80px;
}
.ml-5 {
  margin-left: 5px;
}
.in-col-row {
  margin-bottom: 5px;
}
</style>
