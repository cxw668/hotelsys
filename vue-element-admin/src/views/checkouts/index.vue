<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.orderId" placeholder="订单ID" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column label="订单ID" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.orderId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="金额" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.totalRoomCharge }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.paymentMethod }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.checkoutTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
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

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="filterData" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item label="订单ID" prop="orderId">
          <el-input v-model="temp.orderId" />
        </el-form-item>
        <el-form-item label="金额" prop="totalRoomCharge">
          <el-input v-model="temp.totalRoomCharge" />
        </el-form-item>
        <el-form-item label="支付方式" prop="paymentMethod">
          <el-select v-model="temp.paymentMethod" class="filter-item" placeholder="请选择">
            <el-option v-for="item in paymentOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付时间" prop="checkoutTime">
          <el-date-picker v-model="temp.checkoutTime" type="datetime" placeholder="选择日期时间" />
        </el-form-item>
        <el-form-item label="状态">
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
import { parseTime } from '@/utils'

export default {
  name: 'CheckoutTable',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        Paid: 'success',
        Pending: 'info',
        Refunded: 'danger'
      }
      return statusMap[status]
    },
    parseTime
  },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        orderId: undefined,
        status: undefined
      },
      paymentOptions: ['现金', '信用卡', '支付宝', '微信支付'],
      // 现金、信用卡、支付宝、微信支付
      statusOptions: ['已支付', '待支付', '已退款'],
      temp: {
        id: undefined,
        orderId: undefined,
        totalRoomCharge: 0,
        paymentMethod: '',
        checkoutTime: undefined,
        status: '待支付'
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        orderId: [{ required: true, message: '请输入订单ID', trigger: 'blur' }],
        totalRoomCharge: [{ required: true, message: '请输入金额', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapState('hotel', ['checkouts'])
  },
  watch: {
    checkouts: {
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
    ...mapActions('hotel', ['getCheckouts', 'addCheckout', 'updateCheckout', 'deleteCheckout']),
    async getList() {
      this.listLoading = true
      if (this.checkouts.length === 0) {
        await this.getCheckouts()
      }
      this.filterData()
      this.listLoading = false
    },
    filterData() {
      let items = this.checkouts || []
      
      if (this.listQuery.orderId) {
        items = items.filter(item => item.orderId && item.orderId.indexOf(this.listQuery.orderId) > -1)
      }
      if (this.listQuery.status) {
        items = items.filter(item => item.status === this.listQuery.status)
      }
      
      this.total = items.length
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
        orderId: undefined,
        totalRoomCharge: 0,
        paymentMethod: '',
        checkoutTime: undefined,
        status: 'Pending'
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
          this.addCheckout(this.temp).then(() => {
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
          this.updateCheckout(tempData).then(() => {
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
      this.$confirm('确认要删除该记录吗？', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          this.deleteCheckout(row.id).then(() => {
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
