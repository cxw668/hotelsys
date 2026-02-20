import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/rooms.json',
    method: 'get',
    baseURL: '', // 覆盖全局 baseURL，直接请求 public 下的静态资源
    params
  })
}

export function getDetail(id) {
  return request({
    url: `/rooms/${id}`,
    method: 'get'
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

export function deleteRoom(id) {
  return Promise.resolve({
    code: 0,
    message: '删除成功'
  })
}
