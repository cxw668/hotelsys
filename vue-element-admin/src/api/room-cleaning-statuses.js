import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/room-cleaning-statuses.json',
    method: 'get',
    baseURL: '',
    params
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

export function deleteRoomCleaningStatus(id) {
  return Promise.resolve({
    code: 0,
    message: '删除成功'
  })
}
