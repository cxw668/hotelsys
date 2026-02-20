<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.customerId" placeholder="客户ID" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column label="客户ID" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.customerId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理人" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.handler }}</span>
        </template>
      </el-table-column>
      <el-table-column label="内容" align="center">
        <template slot-scope="{row}">
          <span>{{ row.content }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="解决时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.reportTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
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
        <el-form-item label="客户ID" prop="customerId">
          <el-input v-model="temp.customerId" />
        </el-form-item>
        <el-form-item label="处理人" prop="handler">
          <el-input v-model="temp.handler" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="temp.content" type="textarea" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="解决时间" prop="reportTime">
          <el-date-picker v-model="temp.reportTime" type="datetime" placeholder="选择日期" />
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
  name: 'ComplaintTable',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        '已解决': 'success',
        '待处理': 'danger',
        '已关闭': 'info'
      }
      return statusMap[status]
    },
    parseTime
  },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        customerId: undefined,
        status: undefined
      },
      statusOptions: ['待处理', '已解决', '已关闭'],
      temp: {
        id: undefined,
        customerId: undefined,
        handler: undefined,
        content: '',
        status: '待处理',
        reportTime: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        customerId: [{ required: true, message: '请输入客户ID', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapState('hotel', ['complaints'])
  },
  watch: {
    complaints: {
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
    ...mapActions('hotel', ['getComplaints', 'addComplaint', 'updateComplaint', 'deleteComplaint']),
    async getList() {
      this.listLoading = true
      if (this.complaints.length === 0) {
        await this.getComplaints()
      }
      this.filterData()
      this.listLoading = false
    },
    filterData() {
      let items = this.complaints || []
      if (this.listQuery.customerId) {
        items = items.filter(item => item.customerId && item.customerId.indexOf(this.listQuery.customerId) > -1)
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
        customerId: undefined,
        handler: undefined,
        content: '',
        status: '待处理',
        reportTime: undefined
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
          this.addComplaint(this.temp).then(() => {
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
          this.updateComplaint(tempData).then(() => {
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
        this.deleteComplaint(row.id).then(() => {
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