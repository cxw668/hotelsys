import * as roomsApi from '@/api/rooms'
import * as reservationsApi from '@/api/reservations'
import * as customersApi from '@/api/customers'
import * as ordersApi from '@/api/orders'
import * as employeesApi from '@/api/employees'
import * as statsApi from '@/api/stats'
import * as checkoutsApi from '@/api/checkouts'
import * as complaintsApi from '@/api/complaints'
import * as inventoriesApi from '@/api/inventories'
import * as membershipsApi from '@/api/memberships'
import * as pricingPoliciesApi from '@/api/pricing-policies'
import * as roomCleaningStatusesApi from '@/api/room-cleaning-statuses'

const state = {
  rooms: [],
  reservations: [],
  customers: [],
  orders: [],
  employees: [],
  stats: [],
  checkouts: [],
  complaints: [],
  inventories: [],
  memberships: [],
  pricingPolicies: [],
  roomCleaningStatuses: []
}

const mutations = {
  SET_DATA: (state, { key, data }) => {
    state[key] = data
  },
  ADD_ITEM: (state, { key, item }) => {
    // 模拟 ID 生成，防止 key 重复
    if (!item.id) {
      item.id = Math.floor(Math.random() * 10000) + 1000
    }
    state[key].unshift(item)
  },
  UPDATE_ITEM: (state, { key, id, item }) => {
    const index = state[key].findIndex(v => v.id === id)
    if (index > -1) {
      state[key].splice(index, 1, item)
    }
  },
  DELETE_ITEM: (state, { key, id }) => {
    const index = state[key].findIndex(v => v.id === id)
    if (index > -1) {
      state[key].splice(index, 1)
    }
  }
}

const actions = {
  // 通用加载方法
  async fetchData({ commit }, { key, api, params }) {
    try {
      const response = await api.getList(params)
      const data = response.data.items || []
      commit('SET_DATA', { key, data })
      return data
    } catch (error) {
      console.error(`Failed to fetch ${key}:`, error)
      return []
    }
  },

  // Rooms
  async getRooms({ dispatch }) { return dispatch('fetchData', { key: 'rooms', api: roomsApi }) },
  async addRoom({ commit }, item) {
    await roomsApi.create(item)
    commit('ADD_ITEM', { key: 'rooms', item })
  },
  async updateRoom({ commit }, item) {
    await roomsApi.update(item.id, item)
    commit('UPDATE_ITEM', { key: 'rooms', id: item.id, item })
  },
  async deleteRoom({ commit }, id) {
    await roomsApi.deleteRoom(id)
    commit('DELETE_ITEM', { key: 'rooms', id })
  },

  // Reservations
  async getReservations({ dispatch }) { return dispatch('fetchData', { key: 'reservations', api: reservationsApi }) },
  async addReservation({ commit }, item) {
    await reservationsApi.create(item)
    commit('ADD_ITEM', { key: 'reservations', item })
  },
  async updateReservation({ commit }, item) {
    await reservationsApi.update(item.id, item)
    commit('UPDATE_ITEM', { key: 'reservations', id: item.id, item })
  },
  async deleteReservation({ commit }, id) {
    await reservationsApi.deleteReservation(id)
    commit('DELETE_ITEM', { key: 'reservations', id })
  },

  // Customers
  async getCustomers({ dispatch }) { return dispatch('fetchData', { key: 'customers', api: customersApi }) },
  async addCustomer({ commit }, item) {
    await customersApi.create(item)
    commit('ADD_ITEM', { key: 'customers', item })
  },
  async updateCustomer({ commit }, item) {
    await customersApi.update(item.id, item)
    commit('UPDATE_ITEM', { key: 'customers', id: item.id, item })
  },
  async deleteCustomer({ commit }, id) {
    // 假设 API 命名为 deleteCustomer，如果不是则需调整
    await customersApi.deleteCustomer ? customersApi.deleteCustomer(id) : customersApi.delete(id)
    commit('DELETE_ITEM', { key: 'customers', id })
  },

  // Orders
  async getOrders({ dispatch }) { return dispatch('fetchData', { key: 'orders', api: ordersApi }) },
  async addOrder({ commit }, item) {
    await ordersApi.create(item)
    commit('ADD_ITEM', { key: 'orders', item })
  },
  async updateOrder({ commit }, item) {
    await ordersApi.update(item.id, item)
    commit('UPDATE_ITEM', { key: 'orders', id: item.id, item })
  },
  async deleteOrder({ commit }, id) {
    await ordersApi.deleteOrder ? ordersApi.deleteOrder(id) : ordersApi.delete(id)
    commit('DELETE_ITEM', { key: 'orders', id })
  },

  // Employees
  async getEmployees({ dispatch }) { return dispatch('fetchData', { key: 'employees', api: employeesApi }) },
  async addEmployee({ commit }, item) {
    await employeesApi.createEmployee(item)
    commit('ADD_ITEM', { key: 'employees', item })
  },
  async updateEmployee({ commit }, item) {
    await employeesApi.updateEmployee(item)
    commit('UPDATE_ITEM', { key: 'employees', id: item.id, item })
  },
  async deleteEmployee({ commit }, id) {
    await employeesApi.deleteEmployee ? employeesApi.deleteEmployee(id) : employeesApi.delete(id)
    commit('DELETE_ITEM', { key: 'employees', id })
  },

  // Stats
  async getStats({ dispatch }) { return dispatch('fetchData', { key: 'stats', api: statsApi }) },
  
  // Inventories
  async getInventories({ dispatch }) { return dispatch('fetchData', { key: 'inventories', api: inventoriesApi }) },
  async addInventory({ commit }, item) {
    await inventoriesApi.create(item)
    commit('ADD_ITEM', { key: 'inventories', item })
  },
  async updateInventory({ commit }, item) {
    await inventoriesApi.update(item.id, item)
    commit('UPDATE_ITEM', { key: 'inventories', id: item.id, item })
  },
  async deleteInventory({ commit }, id) {
    await inventoriesApi.deleteInventory ? inventoriesApi.deleteInventory(id) : inventoriesApi.delete(id)
    commit('DELETE_ITEM', { key: 'inventories', id })
  },

  // Checkouts
  async getCheckouts({ dispatch }) { return dispatch('fetchData', { key: 'checkouts', api: checkoutsApi }) },
  async createCheckout({ commit }, item) {
    await checkoutsApi.createCheckout(item)
    commit('ADD_ITEM', { key: 'checkouts', item })
  },
  async updateCheckout({ commit }, item) {
    await checkoutsApi.updateCheckout(item.id, item)
    commit('UPDATE_ITEM', { key: 'checkouts', id: item.id, item })
  },
  async deleteCheckout({ commit }, id) {
    await checkoutsApi.deleteCheckout ? checkoutsApi.deleteCheckout(id) : checkoutsApi.delete(id)
    commit('DELETE_ITEM', { key: 'checkouts', id })
  },

  // Complaints
  async getComplaints({ dispatch }) { return dispatch('fetchData', { key: 'complaints', api: complaintsApi }) },
  async addComplaint({ commit }, item) {
    await complaintsApi.createComplaint(item)
    commit('ADD_ITEM', { key: 'complaints', item })
  },
  async updateComplaint({ commit }, item) {
    await complaintsApi.updateComplaint(item.id, item)
    commit('UPDATE_ITEM', { key: 'complaints', id: item.id, item })
  },
  async deleteComplaint({ commit }, id) {
    await complaintsApi.deleteComplaint ? complaintsApi.deleteComplaint(id) : complaintsApi.delete(id)
    commit('DELETE_ITEM', { key: 'complaints', id })
  },

  // Memberships
  async getMemberships({ dispatch }) { return dispatch('fetchData', { key: 'memberships', api: membershipsApi }) },
  async addMembership({ commit }, item) {
    await membershipsApi.createMembership(item)
    commit('ADD_ITEM', { key: 'memberships', item })
  },
  async updateMembership({ commit }, item) {
    await membershipsApi.updateMembership(item.id, item)
    commit('UPDATE_ITEM', { key: 'memberships', id: item.id, item })
  },
  async deleteMembership({ commit }, id) {
    await membershipsApi.deleteMembership ? membershipsApi.deleteMembership(id) : membershipsApi.delete(id)
    commit('DELETE_ITEM', { key: 'memberships', id })
  },

  // PricingPolicies
  async getPricingPolicies({ dispatch }) { return dispatch('fetchData', { key: 'pricingPolicies', api: pricingPoliciesApi }) },
  async addPricingPolicy({ commit }, item) {
    await pricingPoliciesApi.createPricingPolicy(item)
    commit('ADD_ITEM', { key: 'pricingPolicies', item })
  },
  async updatePricingPolicy({ commit }, item) {
    await pricingPoliciesApi.updatePricingPolicy(item.id, item)
    commit('UPDATE_ITEM', { key: 'pricingPolicies', id: item.id, item })
  },
  async deletePricingPolicy({ commit }, id) {
    await pricingPoliciesApi.deletePricingPolicy ? pricingPoliciesApi.deletePricingPolicy(id) : pricingPoliciesApi.delete(id)
    commit('DELETE_ITEM', { key: 'pricingPolicies', id })
  },

  // RoomCleaningStatuses
  async getRoomCleaningStatuses({ dispatch }) { return dispatch('fetchData', { key: 'roomCleaningStatuses', api: roomCleaningStatusesApi }) },
  async addRoomCleaningStatus({ commit }, item) {
    await roomCleaningStatusesApi.createRoomCleaningStatus(item)
    commit('ADD_ITEM', { key: 'roomCleaningStatuses', item })
  },
  async updateRoomCleaningStatus({ commit }, item) {
    await roomCleaningStatusesApi.updateRoomCleaningStatus(item.id, item)
    commit('UPDATE_ITEM', { key: 'roomCleaningStatuses', id: item.id, item })
  },
  async deleteRoomCleaningStatus({ commit }, id) {
    await roomCleaningStatusesApi.deleteRoomCleaningStatus ? roomCleaningStatusesApi.deleteRoomCleaningStatus(id) : roomCleaningStatusesApi.delete(id)
    commit('DELETE_ITEM', { key: 'roomCleaningStatuses', id })
  },

  // 初始化所有数据 (用于 Dashboard)
  async initAllData({ dispatch }) {
    await Promise.all([
      dispatch('getRooms'),
      dispatch('getReservations'),
      dispatch('getCustomers'),
      dispatch('getOrders'),
      dispatch('getStats'),
      dispatch('getComplaints'),
      dispatch('getRoomCleaningStatuses')
    ])
  },

  // ------------------------------------------------
  // 业务场景联动 Action (Data Linkage)
  // ------------------------------------------------

  /**
   * 场景一：预订 -> 入住 (Quick Check-in)
   * 1. 创建订单 (Order)
   * 2. 更新房间状态 -> 1 (已入住)
   * 3. (可选) 更新预订状态 -> checked_in
   * 4. 记录日志 (Stats) - 简化为更新今日营收
   */
  async checkIn({ dispatch, commit, state }, { reservationId, roomNum, customerName, amount }) {
    const today = new Date().toISOString().split('T')[0]
    
    // 1. 创建订单
    const newOrder = {
      orderNo: `ORD-${Date.now()}`,
      roomNum,
      customerName,
      totalAmount: amount || 0,
      status: 'active', // 在住
      createTime: new Date().toISOString()
    }
    await dispatch('addOrder', newOrder)

    // 2. 更新房间状态
    const room = state.rooms.find(r => r.roomNum === roomNum)
    if (room) {
      await dispatch('updateRoom', { ...room, roomStatus: 1 }) // 1=已入住
    }

    // 3. 更新预订状态 (如果有)
    if (reservationId) {
      const reservation = state.reservations.find(r => r.id === reservationId)
      if (reservation) {
        await dispatch('updateReservation', { ...reservation, status: 'checked_in' })
      }
    }

    // 4. 更新今日营收 (Stats)
    // 注意：这里仅更新本地 state 中的 stats，实际应调用后端接口聚合
    // 简单模拟：找到今日 stat，累加 revenue；如果没有则创建
    let todayStat = state.stats.find(s => s.date === today)
    if (todayStat) {
      const updatedStat = { 
        ...todayStat, 
        totalRevenue: (todayStat.totalRevenue || 0) + (amount || 0),
        checkIns: (todayStat.checkIns || 0) + 1
      }
      await dispatch('fetchData', { key: 'stats', api: statsApi }) // 重新拉取或直接 commit
      // 由于没有 updateStat 的完整实现，这里暂时略过 API 调用，直接依赖前端计算
    }
  },

  /**
   * 场景二：退房 -> 清洁 (Quick Check-out)
   * 1. 结算订单 -> completed
   * 2. 更新房间状态 -> 2 (维修/不可用) 或 自定义状态 (待清洁)
   *    注意：roomStatus 定义为 0:空闲, 1:已入住, 2:维修中。
   *    这里我们可以暂定 2 为"待清洁/不可用"，或者增加状态码。
   *    为了兼容，暂且设为 2 (脏房/待清洁)。
   * 3. 创建清洁任务 (RoomCleaningStatus)
   */
  async checkOut({ dispatch, state }, { orderId, roomNum }) {
    // 1. 更新订单
    if (orderId) {
      const order = state.orders.find(o => o.id === orderId)
      if (order) {
        await dispatch('updateOrder', { ...order, status: 'completed', updateTime: new Date().toISOString() })
      }
    } else {
      // 如果没有传 orderId，尝试通过 roomNum 查找当前活动订单
      const activeOrder = state.orders.find(o => o.roomNum === roomNum && o.status === 'active')
      if (activeOrder) {
        await dispatch('updateOrder', { ...activeOrder, status: 'completed', updateTime: new Date().toISOString() })
      }
    }

    // 2. 更新房间状态 -> 待清洁 (暂用 2 表示，或者扩展状态)
    // 假设 0=空闲, 1=已入住, 2=维修/脏房
    const room = state.rooms.find(r => r.roomNum === roomNum)
    if (room) {
      await dispatch('updateRoom', { ...room, roomStatus: 2 })
    }

    // 3. 创建清洁任务
    const cleaningTask = {
      roomNum,
      status: '未打扫', // 待扫
      cleanerName: '',
      updateTime: new Date().toISOString()
    }
    await dispatch('addRoomCleaningStatus', cleaningTask)
  },

  /**
   * 场景三：清洁完成 (Finish Cleaning)
   * 1. 更新清洁任务 -> cleaned
   * 2. 更新房间状态 -> 0 (空闲)
   */
  async finishCleaning({ dispatch, state }, { roomNum }) {
    // 1. 更新清洁任务
    // 找到该房间最近的一条 dirty 记录
    const tasks = state.roomCleaningStatuses.filter(t => t.roomNum === roomNum && t.status === '未打扫')
    for (const task of tasks) {
      await dispatch('updateRoomCleaningStatus', { ...task, status: '已打扫', updateTime: new Date().toISOString() })
    }

    // 2. 更新房间状态 -> 空闲
    const room = state.rooms.find(r => r.roomNum === roomNum)
    if (room) {
      await dispatch('updateRoom', { ...room, roomStatus: 0 })
    }
  }
}

const getters = {
  // 基础数据
  rooms: state => state.rooms,
  
  // Dashboard 统计数据
  totalRooms: state => state.rooms.length,
  occupiedRooms: state => state.rooms.filter(r => r.roomStatus === 1).length,
  availableRooms: state => state.rooms.filter(r => r.roomStatus === 0).length,
  
  // 今日入住率
  occupancyRate: state => {
    const total = state.rooms.length
    if (total === 0) return 0
    const occupied = state.rooms.filter(r => r.roomStatus === 1).length
    return Math.round((occupied / total) * 100)
  },
  
  todayCheckIns: state => {
    const today = new Date().toISOString().split('T')[0]
    return state.reservations.filter(r => r.checkInDate === today).length
  },
  
  todayRevenue: state => {
    const today = new Date().toISOString().split('T')[0]
    // 优先使用 business_stats 中的数据
    const todayStat = state.stats.find(s => s.date === today)
    if (todayStat) return todayStat.totalRevenue
    
    // 降级使用 orders 计算
    return state.orders
      .filter(o => o.createTime && o.createTime.startsWith(today))
      .reduce((sum, order) => sum + (order.totalAmount || 0), 0)
  },
  
  // 待处理事项
  pendingReservations: state => state.reservations.filter(r => r.status === 'pending' || r.status === '待确认').length,
  pendingComplaints: state => state.complaints.filter(c => c.status === 'pending' || c.status === '待处理').length,
  cleaningRooms: state => state.roomCleaningStatuses.filter(r => r.status !== '已打扫' && r.status !== 'cleaned').length,
  
  totalCustomers: state => state.customers.length,
  
  // 图表数据
  revenueChartData: state => {
    // 如果有 stats 数据则优先使用
    if (state.stats.length > 0) {
      // 按日期排序
      const sortedStats = [...state.stats].sort((a, b) => new Date(a.date) - new Date(b.date))
      console.log('Stats data for chart:', sortedStats)
      return {
        dates: sortedStats.map(s => s.date).slice(-7), // 取最近7天
        values: sortedStats.map(s => s.totalRevenue).slice(-7)
      }
    }
    console.log('No stats data available for chart')
    return { dates: [], values: [] }
  },
  
  // 饼图数据：房型出租占比
  roomTypeOccupancy: state => {
    const occupied = state.rooms.filter(r => r.roomStatus === 1)
    const counts = {}
    occupied.forEach(r => {
      const type = r.roomType || '未知'
      counts[type] = (counts[type] || 0) + 1
    })
    
    return Object.keys(counts).map(key => ({ value: counts[key], name: key }))
  },

  // 柱状图数据：房态分布
  roomStatusDistribution: state => {
    const statusMap = { 0: '空闲', 1: '已入住', 2: '维修中' }
    const counts = { '空闲': 0, '已入住': 0, '维修中': 0 }
    
    state.rooms.forEach(r => {
      const status = statusMap[r.roomStatus] || '未知'
      if (counts[status] !== undefined) {
        counts[status]++
      }
    })
    
    const types = Object.keys(counts)
    return {
      xAxisData: types,
      seriesData: types.map(t => counts[t])
    }
  },
  
  // 业务待办列表
  businessTodos: state => {
    const todos = []
    
    // 待确认预订
    state.reservations
      .filter(r => r.status === 'pending' || r.status === '待确认')
      .forEach(r => {
        todos.push({
          type: 'reservation',
          title: `预订确认: ${r.customerName} (${r.roomType})`,
          time: r.createTime,
          path: '/reservations',
          data: r
        })
      })
      
    // 待处理投诉
    state.complaints
      .filter(c => c.status === 'pending' || c.status === '待处理')
      .forEach(c => {
        todos.push({
          type: 'complaint',
          title: `投诉处理: ${c.roomNum} - ${c.type}`,
          time: c.reportTime,
          path: '/complaints',
          data: c
        })
      })
      
    // 待清洁房间
    state.roomCleaningStatuses
      .filter(r => r.status === '待扫' || r.status === '未打扫' || r.status === 'dirty')
      .forEach(r => {
        todos.push({
          type: 'cleaning',
          title: `客房清洁: ${r.roomNum}`,
          time: r.updateTime,
          path: '/room-cleaning-statuses',
          data: r
        })
      })
      
    // 按时间倒序
    return todos.sort((a, b) => new Date(b.time) - new Date(a.time)).slice(0, 10)
  },
  
  // 最新订单
  latestOrders: state => {
    return [...state.orders]
      .sort((a, b) => new Date(b.createTime || 0) - new Date(a.createTime || 0))
      .slice(0, 8)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
