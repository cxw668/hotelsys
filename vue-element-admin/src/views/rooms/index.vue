<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.roomNum" placeholder="房间号" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.roomType" placeholder="房型" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in roomTypeOptions" :key="item" :label="item" :value="item" />
      </el-select>
      <el-select v-model="listQuery.roomStatus" placeholder="状态" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in statusOptions" :key="item.key" :label="item.label" :value="item.key" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit"
        @click="handleCreate">
        添加
      </el-button>
      <el-radio-group v-model="viewMode" style="margin-left: 20px;">
        <el-radio-button label="list">列表视图</el-radio-button>
        <el-radio-button label="map">房态图</el-radio-button>
      </el-radio-group>
    </div>

    <div v-if="viewMode === 'list'">
      <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%;">
        <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="房间号" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.roomNum }}</span>
          </template>
        </el-table-column>
        <el-table-column label="房型" min-width="150px">
          <template slot-scope="{row}">
            <span>{{ row.roomType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="row.roomStatus | statusFilter">
              {{ row.roomStatus | statusLabel }}
            </el-tag>
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
        @pagination="getList" />
    </div>

    <room-status-map 
      v-else 
      :rooms="list" 
      @room-click="handleMapRoomClick" 
    />

    <!-- 快捷操作对话框 (复用 Dashboard 的组件) -->
    <quick-action-dialogs ref="quickActionDialogs" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px"
        style="width: 400px; margin-left:50px;">
        <el-form-item label="房间号" prop="roomNum">
          <el-input v-model="temp.roomNum" />
        </el-form-item>
        <el-form-item label="房型" prop="roomType">
          <el-select v-model="temp.roomType" class="filter-item" placeholder="请选择房型">
            <el-option v-for="item in roomTypeOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="roomStatus">
          <el-select v-model="temp.roomStatus" class="filter-item" placeholder="请选择状态">
            <el-option v-for="item in statusOptions" :key="item.key" :label="item.label" :value="item.key" />
          </el-select>
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
import RoomStatusMap from './components/RoomStatusMap'
import QuickActionDialogs from '@/views/dashboard/admin/components/QuickActionDialogs'

export default {
  name: 'Rooms',
  components: { Pagination, RoomStatusMap, QuickActionDialogs },
  data() {
        return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        roomNum: undefined,
        roomType: undefined,
        roomStatus: undefined
      },
      roomTypeOptions: ['单人间', '双人间', '套房', '豪华房'],
      statusOptions: [
        { key: 0, label: '空闲' },
        { key: 1, label: '已入住' },
        { key: 2, label: '维护中' }
      ],
      temp: {
        id: undefined,
        roomNum: '',
        roomType: '',
        roomStatus: 0
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        roomNum: [{ required: true, message: '请输入房间号', trigger: 'blur' }],
        roomType: [{ required: true, message: '请选择房型', trigger: 'change' }],
        roomStatus: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
    // return {
    //   viewMode: 'list', // list or map
    //   tableKey: 0,
    // statusFilter(status) {
    //   const statusMap = {
    //     0: 'success',
    //     1: 'danger',
    //     2: 'info'
    //   }
    //   return statusMap[status]
    // },
    // statusLabel(status) {
    //   const statusMap = {
    //     0: '空闲',
    //     1: '已入住',
    //     2: '维护中'
    //   }
    //   return statusMap[status]
    // }
  },
  computed: {
    ...mapState('hotel', ['rooms'])
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        0: 'success',
        1: 'danger',
        2: 'info'
      }
      return statusMap[status]
    },
    statusLabel(status) {
      const statusMap = {
        0: '空闲',
        1: '已入住',
        2: '维护中'
      }
      return statusMap[status] || '未知'
    }
  },
  watch: {
    rooms: {
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
    ...mapActions('hotel', ['getRooms', 'addRoom', 'updateRoom', 'deleteRoom']),
    async getList() {
      this.listLoading = true
      if (this.rooms.length === 0) {
        await this.getRooms()
      }
      this.filterData()
      this.listLoading = false
    },
    filterData() {
      let items = this.rooms || []
      
      // 前端模拟过滤
      if (this.listQuery.roomNum) {
        items = items.filter(item => item.roomNum.indexOf(this.listQuery.roomNum) > -1)
      }
      if (this.listQuery.roomType) {
        items = items.filter(item => item.roomType === this.listQuery.roomType)
      }
      if (this.listQuery.roomStatus !== undefined && this.listQuery.roomStatus !== '') {
        items = items.filter(item => item.roomStatus === this.listQuery.roomStatus)
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
        roomNum: '',
        roomType: '',
        roomStatus: 0,
        floor: 1,
        direction: 'South',
        hasWindow: true,
        area: 20,
        bedType: 'Single',
        price: 200,
        features: []
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
    handleMapRoomClick(room) {
      // 根据房间状态打开不同的快捷操作对话框
      if (room.roomStatus === 0) {
        // 空闲 -> 快速入住
        this.$refs.quickActionDialogs.open('checkin', { roomNum: room.roomNum })
      } else if (room.roomStatus === 1) {
        // 已入住 -> 快速退房
        this.$refs.quickActionDialogs.open('checkout', { roomNum: room.roomNum })
      } else {
        // 维修/脏房 -> 更新房态
        this.$refs.quickActionDialogs.open('roomstatus', { roomNum: room.roomNum, status: room.roomStatus })
      }
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.addRoom(this.temp).then(() => {
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
      this.temp = Object.assign({}, row) // copy obj
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
          this.updateRoom(tempData).then(() => {
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
      this.$confirm('确认要删除该房间吗？', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRoom(row.id).then(() => {
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
