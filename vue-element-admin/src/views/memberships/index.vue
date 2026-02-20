<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.levelName" placeholder="等级名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column label="等级名称" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.levelName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="折扣率" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.discountRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="积分倍率" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.pointsMultiplier }}</span>
        </template>
      </el-table-column>
      <el-table-column label="升级门槛" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.upgradeThreshold }}</span>
        </template>
      </el-table-column>
      <el-table-column label="权益" align="center">
        <template slot-scope="{row}">
          <span>{{ row.benefits ? row.benefits.join(', ') : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="颜色" width="100px" align="center">
        <template slot-scope="{row}">
          <span :style="{color: row.color}">{{ row.color }}</span>
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
        <el-form-item label="等级名称" prop="levelName">
          <el-input v-model="temp.levelName" />
        </el-form-item>
        <el-form-item label="折扣率" prop="discountRate">
          <el-input v-model="temp.discountRate" />
        </el-form-item>
        <el-form-item label="积分倍率" prop="pointsMultiplier">
          <el-input v-model="temp.pointsMultiplier" />
        </el-form-item>
        <el-form-item label="升级门槛" prop="upgradeThreshold">
          <el-input v-model="temp.upgradeThreshold" />
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-input v-model="temp.color" />
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
  name: 'MembershipTable',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        '有效': 'success',
        '无效': 'danger'
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
        levelName: undefined
      },
      temp: {
        id: undefined,
        levelName: '',
        discountRate: 1.0,
        pointsMultiplier: 1.0,
        upgradeThreshold: 0,
        benefits: [],
        color: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        levelName: [{ required: true, message: '请输入等级名称', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapState('hotel', ['memberships'])
  },
  watch: {
    memberships: {
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
    ...mapActions('hotel', ['getMemberships', 'addMembership', 'updateMembership', 'deleteMembership']),
    async getList() {
      this.listLoading = true
      if (this.memberships.length === 0) {
        await this.getMemberships()
      }
      this.filterData()
      this.listLoading = false
    },
    filterData() {
      let items = this.memberships || []
      if (this.listQuery.levelName) {
        items = items.filter(item => item.levelName && item.levelName.indexOf(this.listQuery.levelName) > -1)
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
        levelName: '',
        discountRate: 1.0,
        pointsMultiplier: 1.0,
        upgradeThreshold: 0,
        benefits: [],
        color: ''
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
          this.addMembership(this.temp).then(() => {
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
          this.updateMembership(tempData).then(() => {
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
        this.deleteMembership(row.id).then(() => {
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