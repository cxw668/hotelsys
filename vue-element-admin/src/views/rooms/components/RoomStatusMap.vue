<template>
  <div class="room-status-map">
    <div class="legend">
      <span class="legend-item"><span class="color-box status-0"></span> 空闲</span>
      <span class="legend-item"><span class="color-box status-1"></span> 已入住</span>
      <span class="legend-item"><span class="color-box status-2"></span> 维修/待清洁</span>
    </div>
    
    <div class="room-grid">
      <div 
        v-for="room in rooms" 
        :key="room.id" 
        class="room-card"
        :class="'status-' + room.roomStatus"
        @click="handleRoomClick(room)"
      >
        <div class="room-num">{{ room.roomNum }}</div>
        <div class="room-type">{{ room.roomType }}</div>
        <div class="room-guest" v-if="room.roomStatus === 1">
          <i class="el-icon-user"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    rooms: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    handleRoomClick(room) {
      this.$emit('room-click', room)
    }
  }
}
</script>

<style lang="scss" scoped>
.room-status-map {
  padding: 20px;
}

.legend {
  margin-bottom: 20px;
  display: flex;
  gap: 20px;
  
  .legend-item {
    display: flex;
    align-items: center;
    font-size: 14px;
    
    .color-box {
      width: 20px;
      height: 20px;
      margin-right: 8px;
      border-radius: 4px;
    }
  }
}

.room-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 15px;
}

.room-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  }
  
  .room-num {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .room-type {
    font-size: 12px;
    color: #666;
  }

  .room-guest {
    position: absolute;
    top: 5px;
    right: 5px;
    color: #fff;
  }
}

/* 状态颜色 */
.status-0 { // 空闲 - 绿色
  background-color: #f0f9eb;
  border-color: #c2e7b0;
  .room-num { color: #67c23a; }
  .color-box { background-color: #67c23a; }
}
.legend .status-0 { background-color: #67c23a; }

.status-1 { // 已入住 - 红色
  background-color: #fef0f0;
  border-color: #fbc4c4;
  .room-num { color: #f56c6c; }
  .color-box { background-color: #f56c6c; }
  .room-guest { color: #f56c6c; }
}
.legend .status-1 { background-color: #f56c6c; }

.status-2 { // 维修/待清洁 - 黄色
  background-color: #fdf6ec;
  border-color: #f5dab1;
  .room-num { color: #e6a23c; }
  .color-box { background-color: #e6a23c; }
}
.legend .status-2 { background-color: #e6a23c; }

</style>
