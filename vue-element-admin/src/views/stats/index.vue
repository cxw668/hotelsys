<template>
  <div class="app-container">
    <div class="filter-container">
      <el-date-picker v-model="listQuery.statDate" type="date" placeholder="选择日期" class="filter-item"
        value-format="yyyy-MM-dd" @change="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit"
        @click="handleCreate">
        添加
      </el-button>
    </div>

    <!-- Chart Section -->
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <div id="chart" style="height: 300px; width: 100%;"></div>
    </el-row>

    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%;">
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="日期" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.date }}</span>
        </template>
      </el-table-column>
      <el-table-column label="入住数" align="center">
        <template slot-scope="{row}">
          <span>{{ row.checkIns }}</span>
        </template>
      </el-table-column>
      <el-table-column label="营收" align="center">
        <template slot-scope="{row}">
          <span>{{ row.totalRevenue }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row, $index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="filterData" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="140px"
        style="width: 400px; margin-left:50px;">
        <el-form-item label="日期" prop="date">
          <el-date-picker v-model="temp.date" type="date" placeholder="请选择日期"
            value-format="yyyy-MM-dd" />
        </el-form-item>
        <el-form-item label="入住数" prop="checkIns">
          <el-input v-model.number="temp.checkIns" />
        </el-form-item>
        <el-form-item label="营收" prop="totalRevenue">
          <el-input v-model="temp.totalRevenue" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import Pagination from '@/components/Pagination'
import * as echarts from 'echarts'

export default {
  name: 'BusinessStats',
  components: { Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        statDate: undefined
      },
      temp: {
        id: undefined,
        date: '',
        checkIns: 0,
        totalRevenue: 0.00
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        date: [{ required: true, message: '请选择日期', trigger: 'change' }],
        checkIns: [{ required: true, message: '请输入入住数', trigger: 'blur' }],
        totalRevenue: [{ required: true, message: '请输入营收', trigger: 'blur' }]
      },
      chart: null
    }
  },
  computed: {
    ...mapState('hotel', ['stats'])
  },
  watch: {
    stats: {
      handler() {
        this.filterData()
        this.updateChart()
      },
      deep: true
    }
  },
  created() {
    this.getList()
  },
  mounted() {
    this.initChart()
    // Chart data will be updated when stats are loaded/changed via watcher
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    ...mapActions('hotel', ['getStats', 'addStat', 'updateStat', 'deleteStat']),
    initChart() {
      this.chart = echarts.init(document.getElementById('chart'))
    },
    updateChart() {
      if (!this.chart) return
      
      const items = this.stats || []
      // 取最近7天数据并按日期排序
      const recentItems = [...items]
        .sort((a, b) => new Date(a.date) - new Date(b.date))
        .slice(-7)
        
      const dates = recentItems.map(item => item.date)
      const revenues = recentItems.map(item => item.totalRevenue)

      this.chart.setOption({
        title: { text: '最近7天营收趋势' },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value' },
        series: [{ data: revenues, type: 'line', smooth: true }]
      })
    },
    async getList() {
      this.listLoading = true
      if (this.stats.length === 0) {
        await this.getStats()
      }
      this.filterData()
      this.listLoading = false
    },
    filterData() {
      let items = this.stats || []
      if (this.listQuery.statDate) {
        items = items.filter(item => item.date === this.listQuery.statDate)
      }
      this.total = items.length
      const start = (this.listQuery.page - 1) * this.listQuery.limit
      const end = start + this.listQuery.limit
      this.list = items.slice(start, end)
      
      // Also update chart when data changes/filters applied (or just keep chart on all data)
      // Here we keep chart based on all data (via watcher), but list is filtered.
    },
    handleFilter() {
      this.listQuery.page = 1
      this.filterData()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        date: '', 
        checkIns: 0,
        totalRevenue: 0.00
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.addStat(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          this.updateStat(tempData).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteStat(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
        })
      })
    }
  }
}
</script>