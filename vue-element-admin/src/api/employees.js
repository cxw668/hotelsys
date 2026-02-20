import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/employees.json',
    method: 'get',
    baseURL: '',
    params
  })
}

export function createEmployee(data) {
  return Promise.resolve({
    code: 0,
    message: '创建成功',
    data
  })
}

export function updateEmployee(data) {
  return Promise.resolve({
    code: 0,
    message: '更新成功',
    data
  })
}

export function deleteEmployee(id) {
  return Promise.resolve({
    code: 0,
    message: '删除成功'
  })
}
