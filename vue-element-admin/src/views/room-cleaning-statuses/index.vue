<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.roomNum" placeholder="房间号" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column label="房间号" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roomNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="保洁员" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.cleaner }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center">
        <template slot-scope="{row}">
          <span>{{ row.remark }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.startTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.endTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.status !== 'cleaned' && row.status !== '已净' && row.status !== '已打扫'" type="success" size="mini" @click="handleFinishCleaning(row)">
            已净
          </el-button>
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
        <el-form-item label="房间号" prop="roomNum">
          <el-input v-model="temp.roomNum" />
        </el-form-item>
        <el-form-item label="保洁员" prop="cleaner">
          <el-input v-model="temp.cleaner" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="temp.remark" type="textarea" />
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
  name: 'RoomCleaningStatusTable',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        '已打扫': 'success',
        '未打扫': 'danger',
        '打扫中': 'info'
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
        roomId: undefined,
        status: undefined
      },
      statusOptions: ['已打扫', '未打扫', '打扫中'],
      temp: {
        id: undefined,
        roomId: undefined,
        status: '未打扫',
        remark: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        roomNum: [{ required: true, message: '请输入房间号', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  computed: {
    ...mapState('hotel', ['roomCleaningStatuses'])
  },
  watch: {
    roomCleaningStatuses: {
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
    ...mapActions('hotel', ['getRoomCleaningStatuses', 'addRoomCleaningStatus', 'updateRoomCleaningStatus', 'deleteRoomCleaningStatus', 'finishCleaning']),
    async handleFinishCleaning(row) {
      await this.finishCleaning({ roomNum: row.roomNum })
      this.$notify({
        title: '成功',
        message: '房间已标记为清洁完成',
        type: 'success',
        duration: 2000
      })
    },
    async getList() {
      this.listLoading = true
      if (this.roomCleaningStatuses.length === 0) {
        await this.getRoomCleaningStatuses()
      }
      this.filterData()
      this.listLoading = false
    },
    filterData() {
      let items = this.roomCleaningStatuses || []
      if (this.listQuery.roomId) {
        items = items.filter(item => item.roomId && item.roomId.indexOf(this.listQuery.roomId) > -1)
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
        roomNum: undefined,
        cleaner: '',
        status: '未打扫',
        remark: ''
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
          this.addRoomCleaningStatus(this.temp).then(() => {
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
          this.updateRoomCleaningStatus(tempData).then(() => {
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
        this.deleteRoomCleaningStatus(row.id).then(() => {
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