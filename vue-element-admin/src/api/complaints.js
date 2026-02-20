import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/complaints.json',
    method: 'get',
    baseURL: '',
    params
  })
}

export function createComplaint(data) {
  return Promise.resolve({
    code: 0,
    message: '创建成功',
    data
  })
}

export function updateComplaint(id, data) {
  return Promise.resolve({
    code: 0,
    message: '更新成功',
    data
  })
}

export function deleteComplaint(id) {
  return Promise.resolve({
    code: 0,
    message: '删除成功'
  })
}
