import request from '@/utils/request'

export interface Strategy {
  id?: number
  name: string
  type: 'SMOKE' | 'REGRESSION' | 'PERFORMANCE' | 'SECURITY' | 'INTEGRATION'
  description?: string
  priority: 'P0' | 'P1' | 'P2' | 'P3'
  status: 'ENABLED' | 'DISABLED'
  projectIds?: string[]
  caseScope?: string[]
  environments?: string[]
  scheduleEnabled?: boolean
  scheduleType?: 'DAILY' | 'WEEKLY' | 'MONTHLY'
  scheduleTime?: string
  creator?: string
  createTime?: string
  statusSwitch?: boolean
}

export interface StrategyQueryParams {
  name?: string
  type?: string
  status?: string
  page?: number
  size?: number
}

/**
 * 获取策略列表
 */
export const getStrategyList = (params?: StrategyQueryParams) => {
  return request({
    url: '/api/v1/strategy',
    method: 'get',
    params
  })
}

/**
 * 获取策略详情
 */
export const getStrategyDetail = (id: number) => {
  return request({
    url: `/api/v1/strategy/${id}`,
    method: 'get'
  })
}

/**
 * 创建策略
 */
export const createStrategy = (data: Strategy) => {
  return request({
    url: '/api/v1/strategy',
    method: 'post',
    data
  })
}

/**
 * 更新策略
 */
export const updateStrategy = (id: number, data: Partial<Strategy>) => {
  return request({
    url: `/api/v1/strategy/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除策略
 */
export const deleteStrategy = (id: number) => {
  return request({
    url: `/api/v1/strategy/${id}`,
    method: 'delete'
  })
}

/**
 * 切换策略状态
 */
export const toggleStrategyStatus = (id: number, status: string) => {
  return request({
    url: `/api/v1/strategy/${id}/status`,
    method: 'put',
    data: { status }
  })
}