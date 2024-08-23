<template>
  <div v-if="!isLoading">
    <div>
      <el-row justify="space-between" align="middle">
        <el-col :span="16">
          <h1 class="h1-text">{{ space.name }}</h1>
        </el-col>
        <el-col :span="8" class="end-justify">
          <el-button class="ml-5" type="danger" v-if="state.hasPermission('space:space:delete')">Delete</el-button>
        </el-col>
      </el-row>
      <el-carousel height="300px" class="carousel-container">
        <template v-if="space && space.spcImageDTOList && space.spcImageDTOList.length > 0">
          <el-carousel-item class="center-justify image-bg" v-for="(spcImageDTO, index) in space.spcImageDTOList" :key="index">
            <img :src="spcImageDTO.imageUrl" style="max-height: 300px; object-fit: contain" />
          </el-carousel-item>
        </template>
        <template v-else>
          <div class="center-justify no-image">
            <el-empty description="No Image" />
          </div>
        </template>
      </el-carousel>
      <el-tabs>
        <el-tab-pane label="Details">
          <el-row v-if="state.hasPermission('space:space:update')" class="button-row" justify="end">
            <el-col :span="24">
              <el-button type="primary" @click="router.push({name: 'space-update'})" size="small">Edit</el-button>
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-location"></use></svg>
              {{ space.address }}
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-apartment"></use></svg>
              Department: {{ space.deptName }}
            </el-col>
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-appstore"></use></svg>
              Category: {{ space.category }}
            </el-col>
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-team"></use></svg>
              Capacity: {{ space.capacity }}
            </el-col>
          </el-row>
          <el-row v-if="space.facilities?.trim().length > 0">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-wrench"></use></svg>
              Facilities: {{ space.facilities }}
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-tag"></use></svg>
              Tag:
              <el-tag v-if="space.spcTagDTOList?.length > 0" v-for="tag in space.spcTagDTOList" type="primary">{{ tag.tagName }} </el-tag>
              <el-tag v-else type="info">No Tag</el-tag>
            </el-col>
          </el-row>
          <div v-if="space.description?.trim().length > 0">
            <h2>Description</h2>
            <div v-html="space.description"></div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="Availability">
          <el-row v-if="state.hasPermission('space:booking-rule:update')" class="button-row" justify="end">
            <el-col :span="24">
              <el-button type="primary" size="small">Edit</el-button>
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-user"></use></svg>
              Contact: {{ space.managerName }}
            </el-col>
          </el-row>
          <div v-if="space.spcBookingRuleDTO">
            <el-row class="in-col-row">
              <el-col :span="12">Days open for booking before event: {{ space.spcBookingRuleDTO.openDaysBeforeEvent }}</el-col>
              <el-col :span="12">Maximum reservation days: {{ space.spcBookingRuleDTO.maxReservationDays }}</el-col>
            </el-row>
            <el-row class="in-col-row">
              <el-col :span="12">Days close for booking before event: {{ space.spcBookingRuleDTO.closeDaysBeforeEvent }}</el-col>
              <el-col :span="12">Minimum booking hours: {{ space.spcBookingRuleDTO.minBookingHours }}</el-col>
            </el-row>
            <el-row class="in-col-row">
              <el-col :span="24">
                Approval Required:
                <el-tag v-if="space.spcBookingRuleDTO.approvalRequired" type="primary">Yes</el-tag>
                <el-tag v-else type="info">No</el-tag>
              </el-col>
            </el-row>
          </div>
          <h2>Price</h2>
          <el-row>
            <el-col :span="8"><span class="hour_price">RM{{ space.hour_price }}</span> / Hour</el-col>
            <el-col :span="8" v-if="space.four_hour_price"><span class="four_hour_price">RM{{ space.four_hour_price }}</span> / 4 Hours</el-col>
            <el-col :span="8" v-if="space.day_price"><span class="day_price">RM{{ space.day_price }}</span> / Day</el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script lang="ts" setup>
import {onMounted, ref, reactive, toRefs} from 'vue';
import baseService from "@/service/baseService";
import {useRoute} from "vue-router";
import useView from "@/hooks/useView";
import router from "@/router";

const route = useRoute()
const space = ref();
const isLoading = ref(true);
const view = reactive({});
const state = reactive({ ...useView(view), ...toRefs(view) });

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
.button-row {
  padding-bottom: 10px;
}
</style>
