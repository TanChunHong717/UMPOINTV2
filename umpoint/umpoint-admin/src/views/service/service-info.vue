<template>
  <div v-if="!isLoading">
    <div>
      <el-row justify="space-between" align="middle">
        <el-col :span="16">
          <h1 class="h1-text">{{ service.name }}</h1>
        </el-col>
        <el-col :span="8" class="end-justify">
          <el-button class="ml-5" type="danger" v-if="state.hasPermission('service:service:delete')" @click="deleteHandle()">Delete</el-button>
        </el-col>
      </el-row>
      <el-carousel height="300px" class="carousel-container">
        <template v-if="service && service.svcImageDTOList && service.svcImageDTOList.length > 0">
          <el-carousel-item class="center-justify image-bg" v-for="(svcImageDTO, index) in service.svcImageDTOList" :key="index">
            <img :src="svcImageDTO.imageUrl" style="max-height: 300px; object-fit: contain" />
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
          <el-row class="content-row">
            <el-col :span="23">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-location"></use></svg>
              {{ service.address }}
            </el-col>
            <el-col :span="1">
              <el-button v-if="state.hasPermission('service:service:update')" type="primary" @click="router.push({name: 'service-update'})" size="small">Edit</el-button>
            </el-col>
          </el-row>
          <el-row class="content-row">
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-apartment"></use></svg>
              Department: {{ service.deptName }}
            </el-col>
            <el-col :span="8">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-appstore"></use></svg>
              Category: {{ service.category }}
            </el-col>
          </el-row>
          <el-row class="content-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-tag"></use></svg>
              Tag:
              <el-tag v-if="service.svcTagDTOList?.length > 0" v-for="tag in service.svcTagDTOList" type="primary">{{ tag.tagName }} </el-tag>
              <el-tag v-else type="info">No Tag</el-tag>
            </el-col>
          </el-row>
          <div v-if="service.description?.trim().length > 0" style="padding-bottom: 20px">
            <h2>Description</h2>
            <div v-html="formatDescription(service.description)"></div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="Booking Rule">
          <el-row class="content-row">
            <el-col :span="23">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-user"></use></svg>
              Contact:
              <el-tag type="success" v-if="service.manager">{{ service.managerName }}</el-tag>
              <el-tag type="warning" v-else>Manager is not config for this service.</el-tag>
            </el-col>
            <el-col :span="1">
              <el-button v-if="state.hasPermission('service:booking-rule:update')" type="primary" size="small" @click="bookingRuleUpdateHandle">Edit</el-button>
            </el-col>
          </el-row>
          <el-row class="content-row">
            <el-col v-if="service.price" :span="24">Price: <span class="price">RM{{ service.price }}</span></el-col>
            <el-col v-else :span="24">Price is not set for this service.</el-col>
          </el-row>
          <h1>Booking Rule</h1>
          <div v-if="service.svcBookingRuleDTO">
            <el-row class="content-row">
              <el-col :span="24">
                Approval Required:
                <el-tag v-if="service.approvalRequired" type="primary">Yes</el-tag>
                <el-tag v-else type="info">No</el-tag>
              </el-col>
            </el-row>
            <el-row class="content-row">
              <span class="radio-label">Open booking:</span>
              <el-checkbox v-model="service.svcBookingRuleDTO.openForStaff" :true-value="Number(1)" disabled>Staff</el-checkbox>
              <el-checkbox v-model="service.svcBookingRuleDTO.openForStudent" :true-value="Number(1)" disabled>Student</el-checkbox>
              <el-checkbox v-model="service.svcBookingRuleDTO.openForPublic" :true-value="Number(1)" disabled>Public</el-checkbox>
            </el-row>
          </div>
          <div v-else>
            Booking rule is not set for this service.
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
  <!-- Popup, Add / Edit -->
  <update-booking-rule ref="bookingRuleUpdateRef" @refreshData="initialize">Confirm</update-booking-rule>
</template>
<script lang="ts" setup>
import {onMounted, ref, reactive, toRefs, onUpdated, onActivated} from 'vue';
import baseService from "@/service/baseService";
import {useRoute} from "vue-router";
import useView from "@/hooks/useView";
import router from "@/router";
import {ElMessage, ElMessageBox} from "element-plus";
import UpdateBookingRule from "@/views/service/booking-rule-add-or-update.vue";
import {formatDescription} from "@/utils/custom-utils";

const route = useRoute()
const service = ref();
const isLoading = ref(true);
const view = reactive({
  deleteIsBatch: true,
  deleteURL: "/service/service",
});
const state = reactive({ ...useView(view), ...toRefs(view) });

const getInfo = (id: bigint) => {
  baseService.get("/service/service/" + id).then((res) => {
    service.value = res.data;
    isLoading.value = false;
  });
};

const initialize = () => {
  const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
  getInfo(BigInt(id));
}

const bookingRuleUpdateRef = ref();
const bookingRuleUpdateHandle = () => {
  bookingRuleUpdateRef.value.init(service.value);
};

const deleteHandle = () => {
  ElMessageBox.confirm(state.deleteMessage, "Hint", {
    confirmButtonText: "Confirm",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      baseService.delete("/service/service", [service.value.id]).then((res) => {
        ElMessage.success({
          message: "Success",
          duration: 500,
          onClose: () => {
            state.closeCurrentTab();
          }
        });
      })
    })
    .catch(() => {
      //
    });
}

onMounted(() => {
  initialize();
});

onActivated(() => {
  initialize();
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
.content-row {
  margin-bottom: 5px;
}
.radio-label {
  padding-top: 7px;
  padding-right: 3px;
}
</style>
