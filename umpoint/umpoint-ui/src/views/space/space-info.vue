<template>
  <div v-if="!isLoading">
    <div>
      <el-row justify="space-between" align="middle">
        <el-col :span="16">
          <h1 class="h1-text">{{ space.name }}</h1>
        </el-col>
        <el-col :span="8" class="end-justify">
          <el-button class="ml-5" type="danger" v-if="state.hasPermission('space:space:delete')" @click="deleteHandle()">Delete</el-button>
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
          <el-row class="content-row">
            <el-col :span="23">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-location"></use></svg>
              {{ space.address }}
            </el-col>
            <el-col :span="1">
              <el-button type="primary" @click="$router.push({name: 'space-update', params: {id: space.id}})" size="small">Edit</el-button>
            </el-col>
          </el-row>
          <el-row class="content-row">
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
          <el-row class="content-row" v-if="space.facilities?.trim().length > 0">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-wrench"></use></svg>
              Facilities: {{ space.facilities }}
            </el-col>
          </el-row>
          <el-row class="content-row">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-tag"></use></svg>
              Tag:
              <el-tag v-if="space.spcTagDTOList?.length > 0" v-for="tag in space.spcTagDTOList" type="primary">{{ tag.tagName }} </el-tag>
              <el-tag v-else type="info">No Tag</el-tag>
            </el-col>
          </el-row>
          <div v-if="space.description?.trim().length > 0">
            <h2>Description</h2>
            <div v-html="formatDescription(space.description)"></div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="Booking Rule">
          <el-row class="content-row">
            <el-col :span="23">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-user"></use></svg>
              Contact:
              <el-tag type="success" v-if="space.manager">{{ space.managerName }}</el-tag>
              <el-tag type="warning" v-else>Manager is not config for this space.</el-tag>
            </el-col>
            <el-col :span="1">
              <el-button v-if="state.hasPermission('space:booking-rule:update')" type="primary" size="small" @click="bookingRuleUpdateHandle">Edit</el-button>
            </el-col>
          </el-row>
          <h1>Price</h1>
          <div v-if="space.dayPrice">
            <el-row v-if="space.hourPrice">
              <el-col :span="8"><span class="hour_price">RM{{ space.hourPrice }}</span> / Hour</el-col>
              <el-col :span="8" v-if="space.fourHoursPrice"><span class="four_hour_price">RM{{ space.fourHoursPrice }}</span> / 4 Hours</el-col>
              <el-col :span="8" v-if="space.dayPrice"><span class="day_price">RM{{ space.dayPrice }}</span> / Day</el-col>
            </el-row>
          </div>
          <div v-else>
            Price is not set for this space.
          </div>
          <h1>Booking Rule</h1>
          <div v-if="space.spcBookingRuleDTO">
            <el-row class="content-row">
              <el-col :span="24">
                Approval Required:
                <el-tag v-if="space.spcBookingRuleDTO.approvalRequired == 1" type="primary">Yes</el-tag>
                <el-tag v-else type="info">No</el-tag>
              </el-col>
            </el-row>
            <el-row class="content-row">
              <span class="radio-label">Open booking:</span>
              <el-checkbox v-model="space.spcBookingRuleDTO.openForStaff" :true-value="Number(1)" disabled>Staff</el-checkbox>
              <el-checkbox v-model="space.spcBookingRuleDTO.openForStudent" :true-value="Number(1)" disabled>Student</el-checkbox>
              <el-checkbox v-model="space.spcBookingRuleDTO.openForPublic" :true-value="Number(1)" disabled>Public</el-checkbox>
            </el-row>
            <el-row style="margin-bottom: 10px">
              <el-col :span="24">
                Available in Public Holiday(Include weekend):
                <el-tag v-if="space.spcBookingRuleDTO.holidayAvailable == 1" type="primary">Yes</el-tag>
                <el-tag v-else type="info">No</el-tag>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: 14px">
              The accommodation will be open for booking {{ space.spcBookingRuleDTO.openDaysPriorBooking }} day(s) prior the event and will be closed {{  space.spcBookingRuleDTO.closeDaysAfterBooking }} day(s) before space booking date.
            </el-row>
            <el-row style="margin-bottom: 14px">
              <el-col :span="12">Start Time: {{ space.spcBookingRuleDTO.startTime }}</el-col>
              <el-col :span="12">End Time: {{ space.spcBookingRuleDTO.endTime }}</el-col>
            </el-row>
            <el-row style="margin-bottom: 14px">
              <el-col :span="12">Maximum reservation days: {{ space.spcBookingRuleDTO.maxReservationDays }}</el-col>
              <el-col :span="12">Minimum booking days: {{ space.spcBookingRuleDTO.minBookingDays }}</el-col>
            </el-row>
          </div>
          <div v-else>
            Booking rule is not set for this space.
          </div>
        </el-tab-pane>
        <el-tab-pane label="Availability">
          <vue-cal
            :disable-views="['years', 'year', 'day']"
            :disable-days="disabledDays"
            :time-from="startTime"
            :time-to="endTime"
            :time-step="30"
            @view-change="onViewChange"
            style="height: 400px"
          ></vue-cal>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
  <!-- Popup, Add / Edit -->
  <update-booking-rule ref="bookingRuleUpdateRef" @refreshData="initialize">Confirm</update-booking-rule>
</template>
<script lang="ts" setup>
import {onMounted, ref, reactive, toRefs, onActivated} from 'vue';
import baseService from "@/service/baseService";
import {useRoute} from "vue-router";
import useView from "@/hooks/useView";
import {ElMessage} from "element-plus";
import 'vue-cal/dist/vuecal.css';
import VueCal from 'vue-cal';
import UpdateBookingRule from "@/views/space/booking-rule-add-or-update.vue";
import {formatDescription, generateDisabledWeekends} from "@/utils/custom-utils";

const route = useRoute()
const space = ref();
const isLoading = ref(true);
const disabledDays = ref<any[]>([]);
const startTime = ref();
const endTime = ref();
const view = reactive({});
const state = reactive({ ...useView(view), ...toRefs(view) });

const getInfo = (id: bigint) => {
  baseService.get("/space/space/" + id).then((res) => {
    space.value = res.data;
    isLoading.value = false;
    initializeTimeTable();
  });
};

const initialize = () => {
  const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
  getInfo(BigInt(id));
}

const onViewChange = (object: any) => {
  if (space.value.spcBookingRuleDTO.holidayAvailable != '1')
    disabledDays.value = generateDisabledWeekends(object.startDate, object.endDate, true);
};

const initializeTimeTable = () => {
  if (space.value.spcBookingRuleDTO.holidayAvailable != '1') {
    const startDate = new Date();
    startDate.setDate(1)
    const endDate = new Date();
    endDate.setDate(1);
    endDate.setMonth(endDate.getMonth() + 1);
    disabledDays.value = generateDisabledWeekends(startDate, endDate, false);
  }

  const startTimeArray = space.value.spcBookingRuleDTO.startTime.split(':')
  endTime.value = Number(startTimeArray[0]) * 60 + Number(startTimeArray[1])
  const endTimeArray = space.value.spcBookingRuleDTO.endTime.split(':')
  endTime.value = Number(endTimeArray[0]) * 60 + Number(endTimeArray[1])
}

const bookingRuleUpdateRef = ref();
const bookingRuleUpdateHandle = () => {
  bookingRuleUpdateRef.value.init(space.value);
};

const deleteHandle = () => {
  baseService.delete("/space/space", [space.value.id]).then((res) => {
    ElMessage.success({
      message: "Success",
      duration: 500,
      onClose: () => {
        state.closeCurrentTab();
      }
    });
  })
}

onMounted(() => {
  initialize();
});

onActivated(() => {
  initialize();
})
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
h1 {
  margin-bottom: 10px;
}
.radio-label {
  padding-top: 7px;
  padding-right: 3px;
}
</style>
