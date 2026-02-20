<template>
  <el-card class="box-card" shadow="hover">
    <div slot="header" class="clearfix">
      <span>待办事项</span>
      <el-tag type="danger" style="float: right">{{ list.length }}</el-tag>
    </div>
    <div v-if="list.length === 0" class="empty-text">
      暂无待办事项
    </div>
    <el-table
      v-else
      :data="list"
      style="width: 100%"
      :show-header="false"
    >
      <el-table-column width="40">
        <template slot-scope="scope">
          <svg-icon :icon-class="getIcon(scope.row.type)" :class-name="getColor(scope.row.type)" />
        </template>
      </el-table-column>
      <el-table-column min-width="180">
        <template slot-scope="scope">
          <div class="todo-title">{{ scope.row.title }}</div>
          <div class="todo-time">{{ scope.row.time | formatTime }}</div>
        </template>
      </el-table-column>
      <el-table-column width="80" align="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" plain @click="handleProcess(scope.row)">处理</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
export default {
  props: {
    list: {
      type: Array,
      default: () => []
    }
  },
  filters: {
    formatTime(time) {
      if (!time) return ''
      return time.replace('T', ' ').substring(0, 16)
    }
  },
  methods: {
    getIcon(type) {
      const map = {
        reservation: 'list',
        complaint: 'message',
        cleaning: 'skill'
      }
      return map[type] || 'list'
    },
    getColor(type) {
      const map = {
        reservation: 'icon-blue',
        complaint: 'icon-red',
        cleaning: 'icon-yellow'
      }
      return map[type] || ''
    },
    handleProcess(item) {
      this.$emit('process', item)
    }
  }
}
</script>

<style lang="scss" scoped>
.box-card {
  margin-bottom: 20px;
  min-height: 400px;
}
.todo-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}
.todo-time {
  font-size: 12px;
  color: #999;
}
.empty-text {
  text-align: center;
  color: #999;
  padding: 20px 0;
}
.icon-blue {
  color: #36a3f7;
}
.icon-red {
  color: #f4516c;
}
.icon-yellow {
  color: #f6c02d;
}
</style>
