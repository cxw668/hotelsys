<template>
  <el-dialog 
    title="投诉处理" 
    :visible.sync="visible" 
    width="500px"
    @close="resetForm"
    append-to-body
  >
    <div v-if="complaint">
      <el-descriptions :column="1" border size="medium">
        <el-descriptions-item label="房间号">{{ complaint.roomNum }}</el-descriptions-item>
        <el-descriptions-item label="投诉类型">{{ complaint.type }}</el-descriptions-item>
        <el-descriptions-item label="投诉内容">{{ complaint.content }}</el-descriptions-item>
        <el-descriptions-item label="上报时间">{{ complaint.reportTime | formatTime }}</el-descriptions-item>
      </el-descriptions>

      <el-form :model="form" label-width="80px" style="margin-top: 20px;">
        <el-form-item label="处理结果">
          <el-input 
            type="textarea" 
            v-model="form.result" 
            placeholder="请输入处理结果"
            :rows="3"
          />
        </el-form-item>
      </el-form>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleResolve" :loading="loading">标记已处理</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'ComplaintProcessDialog',
  data() {
    return {
      visible: false,
      complaint: null,
      form: {
        result: ''
      },
      loading: false
    }
  },
  filters: {
    formatTime(time) {
      if (!time) return ''
      return time.replace('T', ' ').substring(0, 16)
    }
  },
  methods: {
    open(complaintData) {
      this.complaint = { ...complaintData }
      this.form.result = ''
      this.visible = true
    },
    resetForm() {
      this.form = { result: '' }
      this.loading = false
    },
    async handleResolve() {
      if (!this.form.result) {
        this.$message.warning('请输入处理结果')
        return
      }

      this.loading = true
      try {
        const updatedComplaint = {
          ...this.complaint,
          status: 'resolved',
          result: this.form.result,
          resolveTime: new Date().toISOString()
        }
        
        await this.$store.dispatch('hotel/updateComplaint', updatedComplaint)
        
        this.$message.success('投诉已处理')
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

<style scoped>
.el-descriptions {
  margin-bottom: 15px;
}
</style>