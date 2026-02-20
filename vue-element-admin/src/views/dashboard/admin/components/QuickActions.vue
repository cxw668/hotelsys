<template>
  <el-card class="box-card" shadow="hover">
    <div slot="header" class="clearfix">
      <span>快捷操作</span>
    </div>
    <div class="action-buttons">
      <el-button type="primary" icon="el-icon-edit" @click="handleAction('checkin')">快速入住</el-button>
      <el-button type="success" icon="el-icon-finished" @click="handleAction('checkout')">快速退房</el-button>
      <el-button type="warning" icon="el-icon-date" @click="handleAction('reservation')">新增预订</el-button>
      <el-button type="info" icon="el-icon-refresh" @click="handleAction('roomstatus')">房态更新</el-button>
    </div>
  </el-card>
</template>

<script>
export default {
  methods: {
    handleAction(action) {
      this.$emit('handleAction', action)
    }
  }
}
</script>

<style lang="scss" scoped>
.box-card {
  margin-bottom: 20px;
}
.action-buttons {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  
  .el-button {
    margin-bottom: 10px;
    width: 45%;
    margin-left: 0;
    margin-right: 10px;
  }
}
</style>
