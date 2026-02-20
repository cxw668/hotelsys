<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.status" placeholder="状态" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="客户ID" align="center">
        <template slot-scope="{row}">
          <span>{{ row.customerId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="房间号" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roomNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="入住日期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.checkInDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退房日期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.checkOutDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item label="客户ID" prop="customerId">
          <el-input v-model.number="temp.customerId" />
        </el-form-item>
        <el-form-item label="房间号" prop="roomNum">
          <el-input v-model.number="temp.roomNum" />
        </el-form-item>
        <el-form-item label="入住日期" prop="checkInDate">
           <el-date-picker v-model="temp.checkInDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="退房日期" prop="checkOutDate">
           <el-date-picker v-model="temp.checkOutDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import Pagination from '@/components/Pagination'

export default {
  name: 'Reservations',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        '已确认': 'success',
        '已入住': 'success',
        '已取消': 'danger',
        '待处理': 'info',
        '已完成': 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        status: undefined
      },
      statusOptions: ['已确认', '已入住', '已取消', '待处理', '已完成'],
      temp: {
        id: undefined,
        customerId: undefined,
        roomNum: undefined,
        checkInDate: undefined,
        checkOutDate: undefined,
        status: '待处理'
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        customerId: [{ required: true, message: '请输入客户ID', trigger: 'blur' }],
        roomNum: [{ required: true, message: '请输入房间号', trigger: 'blur' }],
        checkInDate: [{ required: true, message: '请选择入住日期', trigger: 'change' }],
        checkOutDate: [{ required: true, message: '请选择退房日期', trigger: 'change' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  computed: {
    ...mapState('hotel', ['reservations'])
  },
  watch: {
    reservations: {
      handler() {
        this.filterData()
      },
      deep: true
    }
  },
  created() {
    this.getList()
  },
  methods: {
    ...mapActions('hotel', ['getReservations', 'addReservation', 'updateReservation', 'deleteReservation']),
    async getList() {
      this.listLoading = true
      if (this.reservations.length === 0) {
        await this.getReservations()
      }
      this.filterData()
      this.listLoading = false
    },
    filterData() {
      let items = this.reservations || []
      // 前端过滤状态
      if (this.listQuery.status) {
        items = items.filter(item => item.status === this.listQuery.status)
      }
      this.total = items.length
      // 分页
      const start = (this.listQuery.page - 1) * this.listQuery.limit
      const end = start + this.listQuery.limit
      this.list = items.slice(start, end)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.filterData()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        customerId: undefined,
        roomNum: undefined,
        checkInDate: undefined,
        checkOutDate: undefined,
        status: '待处理'
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
          this.addReservation(this.temp).then(() => {
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
          this.updateReservation(tempData).then(() => {
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
      this.$confirm('确定要删除这条预订记录吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          this.deleteReservation(row.id).then(() => {
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
