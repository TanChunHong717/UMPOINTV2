<template>
  <el-row justify="space-between">
    <el-col :span="8">
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        unlink-panels
        range-separator="To"
        start-placeholder="Start date"
        end-placeholder="End date"
        :shortcuts="shortcuts"
        size="small"
        @change="getData"
      />
    </el-col>
    <el-col :span="2">
      <el-button size="small" type="primary" @click="downloadFile">Download File</el-button>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="14">
      <el-row style="margin-top: 20px">
        <el-col :span="8">
          <el-statistic title="Number of booking" :value="data.bookingCount"/>
        </el-col>
        <el-col :span="8">
          <el-statistic title="Total booking amount" :value="data.bookingAmount">
            <template #prefix>RM</template>
          </el-statistic>
        </el-col>
      </el-row>
      <el-row style="margin-top: 20px">
        <el-col :span="8">
          <el-statistic title="Number of space" :value="data.spaceNumber"/>
        </el-col>
        <el-col :span="8">
          <el-statistic title="Number of service" :value="data.serviceNumber"/>
        </el-col>
        <el-col :span="8">
          <el-statistic title="Number of accommodation" :value="data.accommodationNumber"/>
        </el-col>
      </el-row>
    </el-col>
    <el-col :span="10">
      <v-chart :option="donutChartOptions" style="height: 200px"></v-chart>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="16">
      <v-chart :option="barChartOptions" style="height: 500px"></v-chart>
    </el-col>
    <el-col :span="8" style="padding-left: 10px">
      <el-radio-group v-model="tableType" size="small" @change="radioChange" style="padding-bottom: 5px">
        <el-radio-button label="Space" :value="0"/>
        <el-radio-button label="Service" :value="1"/>
        <el-radio-button label="Accommodation" :value="2"/>
      </el-radio-group>
      <el-table v-loading="dataListLoading" :data="dataList" border style="width: 100%">
        <el-table-column type="index"/>
        <el-table-column prop="name" label="Name" header-align="center" align="center"></el-table-column>
        <el-table-column prop="amount" label="Amount(RM)" header-align="center" align="center" sortable></el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import VChart from 'vue-echarts';
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import {BarChart, LineChart, PieChart} from 'echarts/charts';
import {
  GridComponent,
  LegendComponent,
  TimelineComponent,
  TitleComponent,
  ToolboxComponent,
  TooltipComponent
} from 'echarts/components';
import baseService from "@/service/baseService";

use([CanvasRenderer, PieChart, BarChart, LineChart, GridComponent, TitleComponent, TimelineComponent, TooltipComponent, ToolboxComponent, LegendComponent]);

const dateRange = ref<Date[]>([new Date(), new Date()]);
const tableType = ref(0);
const dataListLoading = ref(true);
const dataList = ref([]);

//Sample data
const data = ref<any>({
  bookingCount: 100,
  bookingAmount: 4240,
  spaceNumber: 157,
  serviceNumber: 24,
  accommodationNumber: 38,
  spaceBookingAmount: 2300,
  serviceBookingAmount: 1180,
  accommodationBookingAmount: 4240 - 2300 - 1180,
  xAxisData: [
    new Date('2024-10-01').getTime(), new Date('2024-10-02').getTime(), new Date('2024-10-03').getTime(),
    new Date('2024-10-04').getTime(), new Date('2024-10-05').getTime(), new Date('2024-10-06').getTime(),
    new Date('2024-10-07').getTime(), new Date('2024-10-08').getTime(), new Date('2024-10-09').getTime(),
    new Date('2024-10-10').getTime(), new Date('2024-10-11').getTime(), new Date('2024-10-12').getTime(),
    new Date('2024-10-13').getTime(), new Date('2024-10-14').getTime()
  ],
  spaceData: [120, 200, 150, 80, 70, 110, 130, 160, 190, 140, 170, 180, 220, 210],
  serviceData: [90, 130, 140, 100, 120, 80, 110, 150, 130, 180, 160, 170, 200, 190],
  accommodationData: [60, 100, 110, 90, 150, 130, 140, 170, 160, 120, 150, 140, 180, 160],
  spaceList: [
    { name: 'Conference Room A', amount: 500 },
    { name: 'Conference Room B', amount: 300 },
    { name: 'Meeting Room 1', amount: 200 },
    { name: 'Meeting Room 2', amount: 150 },
    { name: 'Co-working Space', amount: 700 },
    { name: 'Private Office 1', amount: 450 },
    { name: 'Private Office 2', amount: 600 },
    { name: 'Event Hall', amount: 1200 },
    { name: 'Outdoor Space', amount: 800 },
    { name: 'Studio Room', amount: 350 }
  ],
  serviceList: [
    { name: 'Catering Service', amount: 1000 },
    { name: 'AV Equipment Rental', amount: 600 },
    { name: 'Event Planning', amount: 1200 },
    { name: 'Cleaning Service', amount: 300 },
    { name: 'Security Service', amount: 400 },
    { name: 'Transportation Service', amount: 500 },
    { name: 'Decoration Service', amount: 800 },
    { name: 'Photography Service', amount: 700 },
    { name: 'Valet Parking', amount: 450 },
    { name: 'Technical Support', amount: 550 }
  ],
  accommodationList: [
    { name: 'Deluxe Suite', amount: 2500 },
    { name: 'Standard Room', amount: 1500 },
    { name: 'Family Room', amount: 1800 },
    { name: 'Executive Room', amount: 2000 },
    { name: 'Budget Room', amount: 800 },
    { name: 'Penthouse Suite', amount: 3000 },
    { name: 'Studio Apartment', amount: 2200 },
    { name: 'Business Suite', amount: 2600 },
    { name: 'Luxury Villa', amount: 4000 },
    { name: 'Hostel Room', amount: 400 }
  ]
});

const donutChartOptions = reactive({
  tooltip: {
    trigger: 'item'
  },
  legend: {
    top: '5%',
    left: 'center'
  },
  series: [
    {
      name: 'Total Booking Amount(RM)',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: true,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },

      labelLine: {
        show: false
      },
      data: [
        { value: data.value.spaceBookingAmount, name: 'Space' },
        { value: data.value.serviceBookingAmount, name: 'Service' },
        { value: data.value.accommodationBookingAmount, name: 'Accommodation' }
      ]
    }
  ]
});

const barChartOptions = reactive({
  title: {
    text: 'Booking Amount(RM)'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['Space', 'Service', 'Accommodation']
  },
  toolbox: {
    show: true,
    feature: {
      magicType: { show: true, type: ['line', 'bar'] },
      saveAsImage: { show: true }
    }
  },
  calculable: true,
  xAxis: {
    type: 'time',
    boundaryGap: false
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: 'RM{value}'
    }
  },
  series: [
    {
      name: 'Space',
      type: 'bar',
      data: data.value.xAxisData.map((date:any, i:any) => [new Date(date), data.value.spaceData[i]]),
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' }
        ]
      },
      markLine: {
        data: [{ type: 'average', name: 'Avg' }]
      }
    },
    {
      name: 'Service',
      type: 'bar',
      data: data.value.xAxisData.map((date:any, i:any) => [new Date(date), data.value.serviceData[i]]),
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' }
        ]
      },
      markLine: {
        data: [{ type: 'average', name: 'Avg' }]
      }
    },
    {
      name: 'Accommodation',
      type: 'bar',
      data: data.value.xAxisData.map((date:any, i:any) => [new Date(date), data.value.accommodationData[i]]),
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' }
        ]
      },
      markLine: {
        data: [{ type: 'average', name: 'Avg' }]
      }
    }
  ]
});

const lastMonth = () => {
  const end = new Date()
  const start = new Date()
  start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
  return [start, end]
};

const shortcuts = [
  {
    text: 'Last week',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: 'Last month',
    value: lastMonth,
  },
  {
    text: 'Last 3 months',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
];

const radioChange = (val: number) => {
  dataListLoading.value = true;
  if (val == 0)
    dataList.value = data.value.spaceList;
  else if (val == 1)
    dataList.value = data.value.serviceList;
  else
    dataList.value = data.value.accommodationList;
  dataListLoading.value = false;
}

const generateDateTimestamps = (start: Date, end: Date): number[] => {
  const timestamps: number[] = [];
  let currentDate = new Date(start);

  while (currentDate <= end) {
    timestamps.push(currentDate.getTime());
    currentDate.setDate(currentDate.getDate() + 1);
  }

  return timestamps;
}

const getData = () => {
  // baseService.get("/dashboard/data", {startTime:dateRange.value[0], endTime:dateRange.value[1]}).then((res) => {
  //   data.value = res.data;
  //   data.value.xAxisData = generateDateTimestamps(dateRange.value[0], dateRange.value[1]);
  //
  //   donutChartOptions.series[0].data = [
  //     { value: data.value.spaceBookingAmount, name: 'Space' },
  //     { value: data.value.serviceBookingAmount, name: 'Service' },
  //     { value: data.value.accommodationBookingAmount, name: 'Accommodation' }
  //   ];
  //
  //   barChartOptions.series= [
  //     {
  //       name: 'Space',
  //       type: 'bar',
  //       data: data.value.xAxisData.map((date:any, i:any) => [new Date(date), data.value.spaceData[i]]),
  //       markPoint: {
  //         data: [
  //           { type: 'max', name: 'Max' },
  //           { type: 'min', name: 'Min' }
  //         ]
  //       },
  //       markLine: {
  //         data: [{ type: 'average', name: 'Avg' }]
  //       }
  //     },
  //     {
  //       name: 'Service',
  //       type: 'bar',
  //       data: data.value.xAxisData.map((date:any, i:any) => [new Date(date), data.value.serviceData[i]]),
  //       markPoint: {
  //         data: [
  //           { type: 'max', name: 'Max' },
  //           { type: 'min', name: 'Min' }
  //         ]
  //       },
  //       markLine: {
  //         data: [{ type: 'average', name: 'Avg' }]
  //       }
  //     },
  //     {
  //       name: 'Accommodation',
  //       type: 'bar',
  //       data: data.value.xAxisData.map((date:any, i:any) => [new Date(date), data.value.accommodationData[i]]),
  //       markPoint: {
  //         data: [
  //           { type: 'max', name: 'Max' },
  //           { type: 'min', name: 'Min' }
  //         ]
  //       },
  //       markLine: {
  //         data: [{ type: 'average', name: 'Avg' }]
  //       }
  //     }
  //   ];
  //
  //   dataList.value = data.value.spaceList;
  //   dataListLoading.value = false;
  // }).catch((error) => {
  //   console.error('Error fetching dashboard data');
  // });

  // Remove code below in production
  dataList.value = data.value.spaceList;
  dataListLoading.value = false;
}

const downloadFile = () => {
  baseService.get("/dashboard/report", {startTime:dateRange.value[0], endTime:dateRange.value[1]}).then(res => {
    const fileUrl = res.data;
    const link = document.createElement('a');
    link.href = fileUrl;
    link.download = 'Data Analytics Report.pdf';
    link.click();
  })
}

onMounted(() => {
  dateRange.value = lastMonth();
  getData();
})
</script>
<style>
.el-statistic__head {
  font-size: 16px;
}
</style>
