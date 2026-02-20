import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/stats.json',
    method: 'get',
    baseURL: '',
    params
  })
}

export function getDetail(id) {
  return Promise.resolve({
    code: 0,
    message: '查询成功',
    data: {}
  })
}

export function create(data) {
  return Promise.resolve({
    code: 0,
    message: '创建成功',
    data
  })
}

export function update(id, data) {
  return Promise.resolve({
    code: 0,
    message: '更新成功',
    data
  })
}

export function deleteStat(id) {
  return Promise.resolve({
    code: 0,
    message: '删除成功'
  })
}

export function getRecent7Days() {
  return request({
    url: '/stats.json', // 暂时复用全量数据，前端再处理
    method: 'get',
    baseURL: ''
  })
}
