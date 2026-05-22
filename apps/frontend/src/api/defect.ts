import request from '@/utils/request'

export interface Defect {
  id?: number
  title: string
  description?: string
  priority: 'P0' | 'P1' | 'P2' | 'P3'
  severity: 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW' | 'FATAL' | 'SERIOUS' | 'NORMAL' | 'SLIGHT'
  status: 'NEW' | 'CONFIRMING' | 'FIXING' | 'FIXED' | 'CLOSED' | 'CONFIRMED' | 'ASSIGNED'
  project_id?: number
  module?: string
  steps?: string
  expectedResult?: string
  actualResult?: string
  assignee_id?: number
  assignee?: string
  requirement_id?: number
  creator?: string
  created_at?: string
  updatedAt?: string
}

export interface DefectQueryParams {
  title?: string
  priority?: string
  severity?: string
  status?: string
  project_id?: number
  page?: number
  size?: number
}

/**
 * 获取缺陷列表
 */
export const getDefectList = (params?: DefectQueryParams) => {
  return request({
    url: '/api/v1/defects',
    method: 'get',
    params
  })
}

/**
 * 获取缺陷详情
 */
export const getDefectDetail = (id: number) => {
  return request({
    url: `/api/v1/defects/${id}`,
    method: 'get'
  })
}

/**
 * 创建缺陷
 */
export const createDefect = (data: Defect) => {
  return request({
    url: '/api/v1/defects',
    method: 'post',
    data
  })
}

/**
 * 更新缺陷
 */
export const updateDefect = (id: number, data: Partial<Defect>) => {
  return request({
    url: `/api/v1/defects/${id}`,
    method: 'post',
    data
  })
}

/**
 * 删除缺陷
 */
export const deleteDefect = (id: number) => {
  return request({
    url: `/api/v1/defects/${id}`,
    method: 'delete'
  })
}

/**
 * 分配缺陷
 */
export const assignDefect = (id: number, handler: string) => {
  return request({
    url: `/api/v1/defects/${id}/assign`,
    method: 'post',
    data: { handler }
  })
}