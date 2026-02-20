<template>
  <el-dialog 
    title="待确认预订处理" 
    :visible.sync="visible" 
    width="600px"
    @close="resetForm"
    append-to-body
  >
    <el-descriptions :column="1" border v-if="reservation" size="medium">
      <el-descriptions-item label="预订人">{{ reservation.customerName }}</el-descriptions-item>
      <el-descriptions-item label="联系电话">{{ reservation.phone }}</el-descriptions-item>
      <el-descriptions-item label="预订房型">{{ reservation.roomType }}</el-descriptions-item>
      <el-descriptions-item label="入住日期">{{ reservation.checkInDate | formatDate }}</el-descriptions-item>
      <el-descriptions-item label="退房日期">{{ reservation.checkOutDate | formatDate }}</el-descriptions-item>
      <el-descriptions-item label="备注信息">{{ reservation.remarks || '无' }}</el-descriptions-item>
    </el-descriptions>

    <div style="margin-top: 20px;">
      <el-alert
        v-if="!reservation.roomNum"
        title="该预订尚未分配房间，请选择房间进行确认"
        type="warning"
        show-icon
        :closable="false"
        style="margin-bottom: 15px;"
      />
      <el-alert
        v-else
        :title="'当前已分配房间：' + reservation.roomNum"
        type="success"
        show-icon
        :closable="false"
        style="margin-bottom: 15px;"
      />

      <el-form :model="form" label-width="100px">
        <el-form-item label="分配房间">
          <el-select 
            v-model="form.roomNum" 
            placeholder="请选择房间" 
            style="width: 100%"
            filterable
          >
            <el-option
              v-for="room in availableRooms"
              :key="room.id"
              :label="`${room.roomNum} - ${room.roomType} (${room.price}/晚)`"
              :value="room.roomNum"
            >
              <span style="float: left">{{ room.roomNum }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ room.roomType }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input 
            type="textarea" 
            v-model="form.remarks" 
            placeholder="可选：添加确认备注"
            :rows="2"
          />
        </el-form-item>
      </el-form>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button type="danger" plain @click="handleReject" :loading="loading">拒绝预订</el-button>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">确认预订</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'ReservationConfirmDialog',
  data() {
    return {
      visible: false,
      reservation: {},
      form: {
        roomNum: '',
        remarks: ''
      },
      loading: false
    }
  },
  computed: {
    ...mapGetters('hotel', ['rooms']),
    // 筛选出空闲且匹配房型的房间
    availableRooms() {
      if (!this.reservation || !this.reservation.roomType) return []
      
      const targetType = this.reservation.roomType
      // 0: 空闲, 1: 已入住, 2: 维修/脏房
      return this.rooms.filter(r => 
        r.roomStatus === 0 && 
        r.roomType === targetType
      )
    }
  },
  filters: {
    formatDate(val) {
      if (!val) return ''
      return val.split('T')[0]
    }
  },
  methods: {
    open(reservationData) {
      this.reservation = { ...reservationData }
      this.form.roomNum = this.reservation.roomNum || ''
      this.form.remarks = ''
      this.visible = true
    },
    resetForm() {
      this.form = {
        roomNum: '',
        remarks: ''
      }
      this.loading = false
    },
    async handleConfirm() {
      if (!this.form.roomNum) {
        this.$message.warning('请先分配房间')
        return
      }

      this.loading = true
      try {
        // 1. 更新预订状态
        const updatedReservation = {
          ...this.reservation,
          status: 'confirmed',
          roomNum: this.form.roomNum,
          remarks: this.form.remarks ? `${this.reservation.remarks || ''} [确认备注: ${this.form.remarks}]` : this.reservation.remarks
        }
        
        await this.$store.dispatch('hotel/updateReservation', updatedReservation)

        // 2. (可选) 可以在这里调用 updateRoom 将房间状态改为"预留"(如果系统支持)
        // 目前系统只有 0(空闲) 1(入住)，暂时保持空闲，直到 Check-in

        this.$message.success('预订已确认')
        this.visible = false
        this.$emit('success')
        
        // 刷新数据
        await this.$store.dispatch('hotel/initAllData')
      } catch (error) {
        console.error(error)
        this.$message.error('操作失败')
      } finally {
        this.loading = false
      }
    },
    async handleReject() {
      try {
        await this.$confirm('确定要拒绝该预订吗？此操作不可撤销。', '警告', {
          confirmButtonText: '确定拒绝',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.loading = true
        const updatedReservation = {
          ...this.reservation,
          status: 'cancelled',
          remarks: this.form.remarks ? `${this.reservation.remarks || ''} [拒绝备注: ${this.form.remarks}]` : this.reservation.remarks
        }
        
        await this.$store.dispatch('hotel/updateReservation', updatedReservation)
        
        this.$message.success('预订已拒绝')
        this.visible = false
        this.$emit('success')
        
        // 刷新数据
        await this.$store.dispatch('hotel/initAllData')
      } catch (error) {
        if (error !== 'cancel') {
          console.error(error)
          this.$message.error('操作失败')
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.detail-container {
  border: 1px solid #EBEEF5;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}
.detail-item {
  margin-bottom: 10px;
  display: flex;
}
.detail-item:last-child {
  margin-bottom: 0;
}
.label {
  width: 80px;
  font-weight: bold;
  color: #606266;
}
.value {
  flex: 1;
  color: #303133;
}
</style>