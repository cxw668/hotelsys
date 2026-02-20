<template>
  <el-table :data="list" style="width: 100%;padding-top: 15px;">
    <el-table-column label="Order No" min-width="200">
      <template slot-scope="scope">
        {{ scope.row.id | orderNoFilter }}
      </template>
    </el-table-column>
    <el-table-column label="Amount" width="195" align="center">
      <template slot-scope="scope">
        ¥{{ scope.row.totalAmount | toThousandFilter }}
      </template>
    </el-table-column>
    <el-table-column label="Status" width="100" align="center">
      <template slot-scope="{row}">
        <el-tag :type="row.status | statusFilter">
          {{ row.status }}
        </el-tag>
      </template>
    </el-table-column>
  </el-table>
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
    statusFilter(status) {
      const statusMap = {
        '已支付': 'success',
        '待支付': 'danger',
        '已取消': 'info'
      }
      return statusMap[status] || 'info'
    },
    orderNoFilter(str) {
      return str ? str.substring(0, 30) : ''
    }
  }
}
</script>
