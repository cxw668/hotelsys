<template>
  <div class="dashboard-editor-container">
    <github-corner class="github-corner" />

    <panel-group 
      :occupancy-rate="occupancyRate"
      :today-revenue="todayRevenue"
      :current-guests="occupiedRooms"
      :pending-tasks="pendingTotal"
    />

    <el-row :gutter="32">
      <!-- 可视化图表区 (中部) -->
      <el-col :xs="24" :sm="24" :lg="16">
        <div class="chart-wrapper">
          <p class="chart-title">近7天营收趋势</p>
          <line-chart :chart-data="lineChartData" />
        </div>
        
        <el-row :gutter="32">
          <el-col :xs="24" :sm="12" :lg="12">
            <div class="chart-wrapper">
              <p class="chart-title">房型出租占比</p>
              <pie-chart :chart-data="roomTypeOccupancy" />
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12">
            <div class="chart-wrapper">
              <p class="chart-title">房态分布</p>
              <bar-chart :chart-data="roomStatusDistribution" />
            </div>
          </el-col>
        </el-row>
      </el-col>

      <!-- 右侧区域 -->
      <el-col :xs="24" :sm="24" :lg="8">
        <!-- 待办任务列表 -->
        <business-todo-list :list="businessTodos" @process="handleTodoProcess" />
        
        <!-- 快捷操作入口 -->
        <quick-actions @handleAction="handleQuickAction" />
      </el-col>
    </el-row>

    <!-- 快捷操作对话框 -->
    <quick-action-dialogs ref="quickActionDialogs" />
    
    <!-- 待确认预订弹窗 -->
    <reservation-confirm-dialog ref="reservationConfirmDialog" />

    <!-- 投诉处理弹窗 -->
    <complaint-process-dialog ref="complaintProcessDialog" />

    <!-- 客房清洁处理弹窗 -->
    <cleaning-process-dialog ref="cleaningProcessDialog" />
  </div>
</template>

<script>
import GithubCorner from '@/components/GithubCorner'
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import PieChart from './components/PieChart'
import BarChart from './components/BarChart'
import BusinessTodoList from './components/BusinessTodoList'
import QuickActions from './components/QuickActions'
import QuickActionDialogs from './components/QuickActionDialogs'
import ReservationConfirmDialog from './components/ReservationConfirmDialog'
import ComplaintProcessDialog from './components/ComplaintProcessDialog'
import CleaningProcessDialog from './components/CleaningProcessDialog'
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'DashboardAdmin',
  components: {
    GithubCorner,
    PanelGroup,
    LineChart,
    PieChart,
    BarChart,
    BusinessTodoList,
    QuickActions,
    QuickActionDialogs,
    ReservationConfirmDialog,
    ComplaintProcessDialog,
    CleaningProcessDialog
  },
  computed: {
    ...mapGetters('hotel', [
      'occupancyRate',
      'todayRevenue',
      'occupiedRooms',
      'pendingReservations',
      'pendingComplaints',
      'cleaningRooms',
      'revenueChartData',
      'roomTypeOccupancy',
      'roomStatusDistribution',
      'businessTodos'
    ]),
    pendingTotal() {
      return this.pendingReservations + this.pendingComplaints + this.cleaningRooms
    },
    lineChartData() {
      console.log('lineChartData computed:', this.revenueChartData)
      return {
        xAxisData: this.revenueChartData.dates || [],
        seriesData: this.revenueChartData.values || []
      }
    }
  },
  created() {
    this.initAllData()
  },
  methods: {
    ...mapActions('hotel', ['initAllData']),
    handleQuickAction(action) {
      if (['checkin', 'checkout', 'roomstatus'].includes(action)) {
        this.$refs.quickActionDialogs.open(action)
      } else if (action === 'reservation') {
        this.$router.push('/reservations/list')
      } else {
        this.$message.info('功能开发中...')
      }
    },
    handleTodoProcess(item) {
      if (item.type === 'reservation' && item.data) {
        this.$refs.reservationConfirmDialog.open(item.data)
      } else if (item.type === 'complaint' && item.data) {
        this.$refs.complaintProcessDialog.open(item.data)
      } else if (item.type === 'cleaning' && item.data) {
        this.$refs.cleaningProcessDialog.open(item.data)
      } else {
        this.$router.push(item.path)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
    
    .chart-title {
      font-size: 16px;
      font-weight: bold;
      color: #333;
      margin-bottom: 15px;
      padding-left: 10px;
      border-left: 4px solid #409EFF;
    }
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
