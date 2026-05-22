import request from '@/utils/request'

export interface TestPlan {
  id?: number | null
  name: string
  description?: string
  project_id?: number
  projectId?: number
  status: string
  start_date?: string
  end_date?: string
  owner?: string
  createdAt?: string
  updatedAt?: string
  created_at?: string
  updated_at?: string
}

export interface TestPlanQueryParams {
  name?: string
  status?: string
  page?: number
  size?: number
}

/**
 * 获取测试计划列表
 */
export const getTestPlanList = (params?: TestPlanQueryParams) => {
  return request({
    url: '/api/v1/testplans',
    method: 'get',
    params
  })
}

/**
 * 获取测试计划详情
 */
export const getTestPlanDetail = (id: number) => {
  return request({
    url: `/api/v1/testplans/${id}`,
    method: 'get'
  })
}

/**
 * 创建测试计划
 */
export const createTestPlan = (data: TestPlan) => {
  return request({
    url: '/api/v1/testplans',
    method: 'post',
    data
  })
}

/**
 * 更新测试计划
 */
export const updateTestPlan = (id: number, data: Partial<TestPlan>) => {
  return request({
    url: `/api/v1/testplans/${id}`,
    method: 'post',
    data
  })
}

/**
 * 删除测试计划
 */
export const deleteTestPlan = (id: number) => {
  return request({
    url: `/api/v1/testplans/${id}`,
    method: 'delete'
  })
}
