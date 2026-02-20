<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.roomType" placeholder="类型" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in typeOptions" :key="item" :label="item" :value="item" />
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
      <el-table-column label="名称" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roomType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="值" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.basePrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.startDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.endDate }}</span>
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="类型" prop="roomType">
          <el-select v-model="temp.roomType" class="filter-item" placeholder="请选择">
            <el-option v-for="item in typeOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="值" prop="basePrice">
          <el-input v-model="temp.basePrice" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="temp.startDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="temp.endDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" />
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

export default {
  name: 'PricingPolicyTable',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        '生效': 'success',
        '失效': 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined,
        roomType: undefined
      },
      typeOptions: ['折扣', '附加费', '固定价'],
      statusOptions: ['生效', '失效'],
      temp: {
        id: undefined,
        name: '',
        roomType: '折扣',
        basePrice: 0,
        startDate: '',
        endDate: '',
        status: '生效'
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        roomType: [{ required: true, message: '请选择类型', trigger: 'change' }]
      }
    }
  },
  computed: {
    ...mapState('hotel', ['pricingPolicies'])
  },
  watch: {
    pricingPolicies: {
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
    ...mapActions('hotel', ['getPricingPolicies', 'addPricingPolicy', 'updatePricingPolicy', 'deletePricingPolicy']),
    async getList() {
      this.listLoading = true
      if (this.pricingPolicies.length === 0) {
        await this.getPricingPolicies()
      }
      this.filterData()
      this.listLoading = false
    },
    filterData() {
      let items = this.pricingPolicies || []
      if (this.listQuery.name) {
        items = items.filter(item => item.name && item.name.indexOf(this.listQuery.name) > -1)
      }
      if (this.listQuery.roomType) {
        items = items.filter(item => item.roomType === this.listQuery.roomType)
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
        name: '',
        roomType: '折扣',
        basePrice: 0,
        startDate: '',
        endDate: '',
        status: '生效'
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
          this.addPricingPolicy(this.temp).then(() => {
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
          this.updatePricingPolicy(tempData).then(() => {
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
        this.deletePricingPolicy(row.id).then(() => {
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