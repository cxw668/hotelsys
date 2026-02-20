<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="500px"
    @close="resetForm"
  >
    <!-- 快速入住表单 -->
    <el-form v-if="type === 'checkin'" ref="checkinForm" :model="checkinForm" label-width="100px">
      <el-form-item label="选择房间" prop="roomNum" :rules="[{ required: true, message: '请选择房间', trigger: 'change' }]">
        <el-select v-model="checkinForm.roomNum" placeholder="请选择空闲房间">
          <el-option
            v-for="room in availableRooms"
            :key="room.id"
            :label="room.roomNum + ' (' + room.roomType + ')'"
            :value="room.roomNum"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="客户姓名" prop="customerName" :rules="[{ required: true, message: '请输入客户姓名', trigger: 'blur' }]">
        <el-input v-model="checkinForm.customerName" />
      </el-form-item>
      <el-form-item label="预收金额" prop="amount">
        <el-input-number v-model="checkinForm.amount" :min="0" />
      </el-form-item>
    </el-form>

    <!-- 快速退房表单 -->
    <el-form v-else-if="type === 'checkout'" ref="checkoutForm" :model="checkoutForm" label-width="100px">
      <el-form-item label="选择房间" prop="roomNum" :rules="[{ required: true, message: '请选择房间', trigger: 'change' }]">
        <el-select v-model="checkoutForm.roomNum" placeholder="请选择已入住房间">
          <el-option
            v-for="room in occupiedRooms"
            :key="room.id"
            :label="room.roomNum + ' (' + room.roomType + ')'"
            :value="room.roomNum"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <!-- 房态更新表单 -->
    <el-form v-else-if="type === 'roomstatus'" ref="roomStatusForm" :model="roomStatusForm" label-width="100px">
      <el-form-item label="选择房间" prop="roomNum" :rules="[{ required: true, message: '请选择房间', trigger: 'change' }]">
        <el-select v-model="roomStatusForm.roomNum" placeholder="请选择房间">
          <el-option
            v-for="room in allRooms"
            :key="room.id"
            :label="room.roomNum"
            :value="room.roomNum"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设置状态" prop="status" :rules="[{ required: true, message: '请选择状态', trigger: 'change' }]">
        <el-select v-model="roomStatusForm.status" placeholder="请选择状态">
          <el-option label="空闲" :value="0" />
          <el-option label="已入住" :value="1" />
          <el-option label="维修/脏房" :value="2" />
        </el-select>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取 消</el-button>
      <el-button type="primary" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  data() {
    return {
      visible: false,
      type: '', // checkin, checkout, roomstatus
      checkinForm: {
        roomNum: '',
        customerName: '',
        amount: 0
      },
      checkoutForm: {
        roomNum: ''
      },
      roomStatusForm: {
        roomNum: '',
        status: 0
      }
    }
  },
  computed: {
    ...mapState('hotel', ['rooms']),
    dialogTitle() {
      const titles = {
        checkin: '快速入住',
        checkout: '快速退房',
        roomstatus: '更新房态'
      }
      return titles[this.type] || '操作'
    },
    allRooms() {
      return this.rooms
    },
    availableRooms() {
      return this.rooms.filter(r => r.roomStatus === 0)
    },
    occupiedRooms() {
      return this.rooms.filter(r => r.roomStatus === 1)
    }
  },
  methods: {
    ...mapActions('hotel', ['checkIn', 'checkOut', 'updateRoom']),
    
    open(type, initialData = {}) {
      this.type = type
      this.visible = true
      this.$nextTick(() => {
        if (type === 'checkin' && initialData.roomNum) {
          this.checkinForm.roomNum = initialData.roomNum
        } else if (type === 'checkout' && initialData.roomNum) {
          this.checkoutForm.roomNum = initialData.roomNum
        } else if (type === 'roomstatus') {
          if (initialData.roomNum) this.roomStatusForm.roomNum = initialData.roomNum
          if (initialData.status !== undefined) this.roomStatusForm.status = initialData.status
        }
      })
    },
    
    resetForm() {
      this.checkinForm = { roomNum: '', customerName: '', amount: 0 }
      this.checkoutForm = { roomNum: '' }
      this.roomStatusForm = { roomNum: '', status: 0 }
      this.$nextTick(() => {
        if (this.$refs.checkinForm) this.$refs.checkinForm.clearValidate()
        if (this.$refs.checkoutForm) this.$refs.checkoutForm.clearValidate()
        if (this.$refs.roomStatusForm) this.$refs.roomStatusForm.clearValidate()
      })
    },

    async handleSubmit() {
      try {
        if (this.type === 'checkin') {
          this.$refs.checkinForm.validate(async valid => {
            if (valid) {
              await this.checkIn(this.checkinForm)
              this.$message.success('入住办理成功')
              this.visible = false
            }
          })
        } else if (this.type === 'checkout') {
          this.$refs.checkoutForm.validate(async valid => {
            if (valid) {
              await this.checkOut({ roomNum: this.checkoutForm.roomNum })
              this.$message.success('退房办理成功，房间已置为待清洁')
              this.visible = false
            }
          })
        } else if (this.type === 'roomstatus') {
          this.$refs.roomStatusForm.validate(async valid => {
            if (valid) {
              const room = this.rooms.find(r => r.roomNum === this.roomStatusForm.roomNum)
              if (room) {
                await this.updateRoom({ ...room, roomStatus: this.roomStatusForm.status })
                this.$message.success('房态更新成功')
                this.visible = false
              }
            }
          })
        }
      } catch (error) {
        console.error(error)
        this.$message.error('操作失败')
      }
    }
  }
}
</script>
