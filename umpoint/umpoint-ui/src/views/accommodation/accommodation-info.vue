<template>
  <div v-if="!isLoading">
    <div>
      <el-row justify="space-between" align="middle">
        <el-col :span="16">
          <h1 class="h1-text">{{ accommodation.name }}</h1>
        </el-col>
        <el-col :span="8" class="end-justify">
          <el-button class="ml-5" type="danger" v-if="state.hasPermission('accommodation:accommodation:delete')">Delete</el-button>
        </el-col>
      </el-row>
      <el-carousel height="300px" class="carousel-container">
        <template v-if="accommodation && accommodation.accImageDTOList && accommodation.accImageDTOList.length > 0">
          <el-carousel-item class="center-justify image-bg" v-for="(accImageDTO, index) in accommodation.accImageDTOList" :key="index">
            <img :src="accImageDTO.imageUrl" style="max-height: 300px; object-fit: contain" />
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
          <el-row v-if="state.hasPermission('accommodation:accommodation:update')" class="button-row" justify="end">
            <el-col :span="24">
              <el-button type="primary" @click="router.push({name: 'accommodation-update'})" size="small">Edit</el-button>
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-location"></use></svg>
              {{ accommodation.address }}
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-apartment"></use></svg>
              Department: {{ accommodation.deptName }}
            </el-col>
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-appstore"></use></svg>
              Category: {{ accommodation.category }}
            </el-col>
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-team"></use></svg>
              Capacity: {{ accommodation.capacity }}
            </el-col>
          </el-row>
          <el-row v-if="accommodation.facilities?.trim().length > 0">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-wrench"></use></svg>
              Facilities: {{ accommodation.facilities }}
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-tag"></use></svg>
              Tag:
              <el-tag v-if="accommodation.accTagDTOList?.length > 0" v-for="tag in accommodation.accTagDTOList" type="primary">{{ tag.tagName }} </el-tag>
              <el-tag v-else type="info">No Tag</el-tag>
            </el-col>
          </el-row>
          <div v-if="accommodation.description?.trim().length > 0">
            <h2>Description</h2>
            <div v-html="formatDescription(accommodation.description)"></div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="Availability" v-if="accommodation.accBookingRuleDTO">
          <el-row v-if="state.hasPermission('accommodation:booking-rule:update')" class="button-row" justify="end">
            <el-col :span="24">
              <el-button type="primary" size="small">Edit</el-button>
            </el-col>
          </el-row>
          <el-row class="in-col-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-user"></use></svg>
              Contact: {{ accommodation.managerName }}
            </el-col>
          </el-row>
          <div v-if="accommodation.accBookingRuleDTO">
            <el-row class="in-col-row">
              <el-col :span="12">Days open for booking before event: {{ accommodation.accBookingRuleDTO.openDaysBeforeEvent }}</el-col>
              <el-col :span="12">Maximum reservation days: {{ accommodation.accBookingRuleDTO.maxReservationDays }}</el-col>
            </el-row>
            <el-row class="in-col-row">
              <el-col :span="12">Days close for booking before event: {{ accommodation.accBookingRuleDTO.closeDaysBeforeEvent }}</el-col>
            </el-row>
            <el-row class="in-col-row">
              <el-col :span="24">
                Approval Required:
                <el-tag v-if="accommodation.accBookingRuleDTO.approvalRequired" type="primary">Yes</el-tag>
                <el-tag v-else type="info">No</el-tag>
              </el-col>
            </el-row>
          </div>
          <h2>Price</h2>
          <el-row>
            <el-col :span="8"><span class="hour_price">RM{{ accommodation.day_price }}</span> / Day</el-col>
            <el-col :span="8" v-if="accommodation.four_hour_price"><span class="four_hour_price">RM{{ accommodation.week_price }}</span> / Week</el-col>
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
const accommodation = ref();
const isLoading = ref(true);
const view = reactive({});
const state = reactive({ ...useView(view), ...toRefs(view) });

const getInfo = (id: bigint) => {
  baseService.get("/accommodation/accommodation/" + id).then((res) => {
    accommodation.value = res.data;
    isLoading.value = false;
  });
};

const formatDescription = (description: string) => {
  if (description.startsWith('"'))
    description = description.substring(1);
  if (description.endsWith('"'))
    description = description.substring(0, description.length-1);
  description = description.replace("\\n", "");
  return description;
}

onMounted(() => {
  const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
  getInfo(BigInt(id))
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
