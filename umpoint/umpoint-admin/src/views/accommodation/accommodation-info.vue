<template>
  <div v-if="!isLoading">
    <div>
      <el-row justify="space-between" align="middle">
        <el-col :span="16">
          <h1 class="h1-text">{{ accommodation.name }}</h1>
        </el-col>
        <el-col :span="8" class="end-justify">
          <el-button class="ml-5" type="danger" v-if="state.hasPermission('accommodation:accommodation:delete')" @click="deleteHandle()">Delete</el-button>
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
          <el-row class="content-row">
            <el-col :span="23">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-location"></use></svg>
              {{ accommodation.address }}
            </el-col>
            <el-col :span="1">
              <el-button v-if="state.hasPermission('accommodation:accommodation:update')" type="primary" @click="router.push({name: 'accommodation-update'})" size="small">Edit</el-button>
            </el-col>
          </el-row>
          <el-row class="content-row">
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
          <el-row class="content-row" v-if="accommodation.facilities?.trim().length > 0">
            <el-col :span="24">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-wrench"></use></svg>
              Facilities: {{ accommodation.facilities }}
            </el-col>
          </el-row>
          <el-row class="content-row">
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
        <el-tab-pane label="Booking Rule">
          <el-row class="content-row" >
            <el-col :span="23">
              <svg class="iconfont" aria-hidden="true"><use xlink:href="#icon-user"></use></svg>
              Contact:
              <el-tag type="success" v-if="accommodation.manager">{{ accommodation.managerName }}</el-tag>
              <el-tag type="warning" v-else>Manager is not config for this accommodation.</el-tag>
            </el-col>
            <el-col :span="1">
              <el-button v-if="state.hasPermission('accommodation:booking-rule:update')" type="primary" size="small" @click="bookingRuleUpdateHandle">Edit</el-button>
            </el-col>
          </el-row>
          <h1>Price</h1>
          <div v-if="accommodation.dayPrice">
            <el-row>
              <el-col :span="12"><span class="day_price">RM{{ accommodation.dayPrice }}</span> / Day</el-col>
              <el-col :span="12" v-if="accommodation.weekPrice"><span class="week_price">RM{{ accommodation.weekPrice }}</span> / Week</el-col>
            </el-row>
          </div>
          <div v-else>
            Price is not set for this accommodation.
          </div>
          <h1>Booking Rule</h1>
          <div v-if="accommodation.accBookingRuleDTO">
            <el-row>
              <el-col :span="24">
                Approval Required:
                <el-tag v-if="accommodation.accBookingRuleDTO.approvalRequired == 1" type="primary">Yes</el-tag>
                <el-tag v-else type="info">No</el-tag>
              </el-col>
            </el-row>
            <el-row class="content-row">
              <span class="radio-label">Open booking:</span>
              <el-checkbox v-model="accommodation.spcBookingRuleDTO.openForStaff" :true-value="Number(1)" disabled>Staff</el-checkbox>
              <el-checkbox v-model="accommodation.spcBookingRuleDTO.openForStudent" :true-value="Number(1)" disabled>Student</el-checkbox>
              <el-checkbox v-model="accommodation.spcBookingRuleDTO.openForPublic" :true-value="Number(1)" disabled>Public</el-checkbox>
            </el-row>
            <el-row style="margin-bottom: 10px">
              <el-col :span="24">
                Available in Public Holiday(Include weekend):
                <el-tag v-if="accommodation.accBookingRuleDTO.holidayAvailable == 1" type="primary">Yes</el-tag>
                <el-tag v-else type="info">No</el-tag>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: 14px">
              The accommodation will be open for booking {{ accommodation.accBookingRuleDTO.maxBookingAdvanceDay }} day(s) prior the event and will be closed {{  accommodation.accBookingRuleDTO.minBookingAdvanceDay }} day(s) before accommodation booking date.
            </el-row>
            <el-row style="margin-bottom: 14px">
              <el-col :span="12">Maximum reservation days: {{ accommodation.accBookingRuleDTO.maxReservationDays }}</el-col>
              <el-col :span="12">Minimum reservation days: {{ accommodation.accBookingRuleDTO.minBookingDays }}</el-col>
            </el-row>
          </div>
          <div v-else>
            Booking rule is not set for this accommodation.
          </div>
        </el-tab-pane>
        <el-tab-pane label="Availability">
          <vue-cal
            :disable-views="['years', 'year', 'day']"
            :special-hours="specialHours"
            :events="events"
            :on-event-click="onEventClick"
            :editable-events="{ title: false, drag: false, resize: false, delete: false, create: true }"
            :drag-to-create-event="true"
            :min-event-width="100"
            @event-drag-create="onClosureCreate"
            @view-change="onViewChange"
            style="height: 580px"
          ></vue-cal>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
  <!-- Popup, Add / Edit -->
  <update-booking-rule ref="bookingRuleUpdateRef" @refreshData="initialize">Confirm</update-booking-rule>
  <update-closure ref="closureUpdateRef" @refreshData="updateTimetable" @cancel="cancelClosureCreate">Confirm</update-closure>
</template>
<script lang="ts" setup>
import {onActivated, onMounted, reactive, ref, toRefs} from 'vue';
import baseService from "@/service/baseService";
import {useRoute} from "vue-router";
import useView from "@/hooks/useView";
import router from "@/router";
import {ElMessage} from "element-plus";
import 'vue-cal/dist/vuecal.css';
import VueCal from 'vue-cal';
import UpdateBookingRule from "@/views/accommodation/booking-rule-add-or-update.vue";
import UpdateClosure from "@/views/accommodation/closure-add-or-update.vue";
import {formatDescription, getMondayAndSunday} from "@/utils/custom-utils";
import {formatDateToDateTimeStr} from "@/utils/date";

const route = useRoute()
const accommodation = ref();
const isLoading = ref(true);

const holidays = ref([]);
const specialHours = ref<any>({});
let events = ref<any>([]);
const weekendDays = [6, 7];

const view = reactive({});
const state = reactive({ ...useView(view), ...toRefs(view) });

const initialize = () => {
  const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
  getInfo(BigInt(id));
}

const getInfo = (id: bigint) => {
  baseService.get("/accommodation/accommodation/" + id).then((res) => {
    accommodation.value = res.data;
    isLoading.value = false;
    //getHoliday(new Date().getFullYear());
    initializeTimeTable();
  });
};

const getHoliday = (year: number) => {
  baseService.get("https://calendarific.com/api/v2/holidays?&api_key=bjgsLdWRYGYUsxteQAwtxx7uxf9AqKOz&country=MY&year=" + year).then((res: any) => {
    holidays.value = res.response.holidays;
    initializeTimeTable();
  });
}

const initializeTimeTable = () => {
  onViewChange(getMondayAndSunday(new Date()));
}

const onViewChange = (object: any) => {
  if (object.view == 'month')
    return;

  specialHours.value = generateWeekendAndHoliday(object.startDate, object.endDate);
  getEvent(object.startDate, object.endDate);
};

const generateWeekendAndHoliday = (startDate: Date, endDate: Date) => {
  const holidayObject: Record<number, any> = {};
  const holidayClass = (
    accommodation.value.accBookingRuleDTO &&
    accommodation.value.accBookingRuleDTO.holidayAvailable == 1)? 'close': 'info';

  holidays.value.forEach((holiday: any) => {
    const holidayDate = new Date(holiday.date.iso);

    if (holidayDate >= startDate && holidayDate <= endDate) {
      const dayOfWeek = (holidayDate.getDay() + 6) % 7 + 1;
      holidayObject[dayOfWeek] = { from:0, to:24*60, label: holiday.name, class: holidayClass };
    }
  });

  if (accommodation.value.accBookingRuleDTO && accommodation.value.accBookingRuleDTO.holidayAvailable == 1) {
    weekendDays.forEach((day) => {
      if (!holidayObject[day]) {
        holidayObject[day] = { from:0, to:24*60, label: "Weekend", class: 'close' };
      }
    });
  }

  return holidayObject;
}

const getEvent = async (startDate: Date, endDate: Date) => {
  baseService.get("/accommodation/event",
    {
      accommodationId: accommodation.value.id,
      startTime: formatDateToDateTimeStr(startDate),
      endTime: formatDateToDateTimeStr(endDate)
    }
  ).then((res) => {
    events.value = [];

    res.data.forEach((eventDTO: any) => {
      const event = {
        start: new Date(eventDTO.startTime),
        end: new Date(eventDTO.endTime),
        title: (eventDTO.type == '0')? "Booking:": "Closure",
        class: (eventDTO.type == '0')? "booking": "closure",
        type: eventDTO.type,
        bookingId: eventDTO.bookingId,
        closureId: eventDTO.closureId,
      }
      event.start.setHours(0,0,0);
      event.end.setHours(23, 59, 59);

      events.value.push(event);
    })
  });
}

const onEventClick = (event: any) => {
  if (event.type == '0' && state.hasPermission("accommodation:booking:info"))
    router.push({path: '/booking/acc-booking', query: {id: event.bookingId}});
  else if (event.type == '1' && state.hasPermission("accommodation:closure:info")) {
    onClosureUpdate(event);
  }
}

const bookingRuleUpdateRef = ref();
const bookingRuleUpdateHandle = () => {
  bookingRuleUpdateRef.value.init(accommodation.value);
};

const closureUpdateRef = ref();
const onClosureCreate = (event: any) => {
  if (state.hasPermission("accommodation:closure:create"))
    closureUpdateRef.value.init(null, event, accommodation.value.id);
}
const onClosureUpdate = (event: any) => {
  closureUpdateRef.value.init(event.closureId, event, accommodation.value.id);
}
const cancelClosureCreate = (e: any) => {
  if (!e.id)
    events.value = events.value.filter((event: any) => event._eid != e._eid);
}
const updateTimetable = (event: any) => {
  onViewChange(getMondayAndSunday(new Date(event.start)));
}

const deleteHandle = () => {
  baseService.delete("/accommodation/accommodation", [accommodation.value.id]).then((res) => {
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
.radio-label {
  padding-top: 7px;
  padding-right: 3px;
}
.close {
  background:
    #fff7f0
    repeating-linear-gradient(
      -45deg,
      rgba(255, 162, 87, 0.25),
      rgba(255, 162, 87, 0.25) 5px,
      rgba(255, 255, 255, 0) 5px,
      rgba(255, 255, 255, 0) 15px
    );
  color: #f6984c;
  .special-hours-label {
    padding-top: 10px;
  }
}
.vuecal__event-title {
  font-size: 1.2em;
  font-weight: bold;
  margin: 4px 0 8px;
}
.vuecal__event.booking {
  background-color: rgba(0, 123, 255, 0.9);  /* Blue */
  border: 1px solid rgb(0, 104, 217);
  color: #fff;
}
.vuecal__event.close {
  background-color: rgba(108, 117, 125, 0.9);  /* Gray */
  border: 1px solid rgb(88, 97, 104);
  color: #fff;
}
.vuecal__event.closure {
  background-color: rgba(255, 102, 102, 0.9);
  border: 1px solid rgb(235, 82, 82);
  color: #fff;
}
</style>
