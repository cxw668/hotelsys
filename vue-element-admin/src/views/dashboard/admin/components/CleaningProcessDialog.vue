<template>
  <el-dialog 
    title="客房清洁处理" 
    :visible.sync="visible" 
    width="400px"
    @close="resetForm"
    append-to-body
  >
    <div v-if="task" style="text-align: center;">
      <i class="el-icon-cleaning-square" style="font-size: 48px; color: #E6A23C; margin-bottom: 20px;"></i>
      <div style="font-size: 18px; font-weight: bold; margin-bottom: 10px;">
        房间号: {{ task.roomNum }}
      </div>
      <div style="color: #666; margin-bottom: 20px;">
        当前状态: {{ task.status }}
      </div>
      
      <el-alert
        title="确认清洁完成后，房间状态将自动变更为「空闲」"
        type="info"
        :closable="false"
        show-icon
        style="text-align: left;"
      />
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="success" @click="handleFinish" :loading="loading">确认已清洁</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'CleaningProcessDialog',
  data() {
    return {
      visible: false,
      task: null,
      loading: false
    }
  },
  methods: {
    open(taskData) {
      this.task = { ...taskData }
      this.visible = true
    },
    resetForm() {
      this.loading = false
    },
    async handleFinish() {
      this.loading = true
      try {
        // 调用 finishCleaning action，它会更新 roomStatus 和 cleaningStatus
        await this.$store.dispatch('hotel/finishCleaning', { roomNum: this.task.roomNum })
        
        this.$message.success('清洁任务已完成，房间已恢复空闲')
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
    }
  }
}
</script>
