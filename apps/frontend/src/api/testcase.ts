import request from '@/utils/request'

export interface Testcase {
  id?: number
  title: string
  description?: string
  priority: 'P0' | 'P1' | 'P2' | 'P3'
  status: 'PENDING_REVIEW' | 'APPROVED' | 'EXECUTING' | 'COMPLETED'
  type: 'FUNCTIONAL' | 'API' | 'PERFORMANCE' | 'SECURITY' | 'UI'
  project_id?: number
  projectId?: number
  test_module?: string
  testModule?: string
  test_status?: string
  testStatus?: string
  requirement_id?: number
  requirementId?: number
  precondition?: string
  preconditions?: string
  steps?: string
  expectedResult?: string
  createdAt?: string
  updatedAt?: string
}

export interface TestcaseQueryParams {
  name?: string
  title?: string
  priority?: string
  status?: string
  projectId?: number
  page?: number
  size?: number
}

/**
 * 获取测试用例列表
 */
export const getTestcaseList = (params?: TestcaseQueryParams) => {
  return request({
    url: '/api/v1/testcases',
    method: 'get',
    params
  })
}

/**
 * 获取测试用例详情
 */
export const getTestcaseDetail = (id: number) => {
  return request({
    url: `/api/v1/testcases/${id}`,
    method: 'get'
  })
}

/**
 * 创建测试用例
 */
export const createTestcase = (data: Testcase) => {
  return request({
    url: '/api/v1/testcases',
    method: 'post',
    data
  })
}

/**
 * 更新测试用例
 */
export const updateTestcase = (id: number, data: Partial<Testcase>) => {
  return request({
    url: `/api/v1/testcases/${id}`,
    method: 'post',
    data
  })
}

/**
 * 删除测试用例
 */
export const deleteTestcase = (id: number) => {
  return request({
    url: `/api/v1/testcases/${id}`,
    method: 'delete'
  })
}

/**
 * 执行测试用例
 */
export const executeTestcase = (id: number) => {
  return request({
    url: `/api/v1/testcases/${id}/execute`,
    method: 'post'
  })
}

/**
 * AI生成测试用例
 */
export const generateTestcaseByAI = (data: { projectId: number; requirement?: string }) => {
  return request({
    url: '/api/v1/testcases/generate',
    method: 'post',
    data
  })
}