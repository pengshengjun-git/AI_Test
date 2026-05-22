import request from '@/utils/request'

export interface Requirement {
  id?: number
  title?: string
  name?: string
  description?: string
  type?: string
  priority?: string
  source?: string
  status?: string
  projectId?: number
  project_id?: number
  proposer?: string
  owner?: string
  reviewer?: string
  effective_version?: string
  acceptance_criteria?: string
  review_result?: string
  review_comments?: string
  online_time?: string
  close_reason?: string
  createdAt?: string
  updatedAt?: string
}

export interface RequirementQueryParams {
  name?: string
  title?: string
  status?: string
  priority?: string
  page?: number
  size?: number
}

/**
 * 获取需求列表
 */
export const getRequirementList = (params?: RequirementQueryParams) => {
  return request({
    url: '/api/v1/requirements',
    method: 'get',
    params
  })
}

/**
 * 获取需求详情
 */
export const getRequirementDetail = (id: number) => {
  return request({
    url: `/api/v1/requirements/${id}`,
    method: 'get'
  })
}

/**
 * 创建需求
 */
export const createRequirement = (data: Requirement) => {
  return request({
    url: '/api/v1/requirements',
    method: 'post',
    data
  })
}

/**
 * 更新需求
 */
export const updateRequirement = (id: number, data: Partial<Requirement>) => {
  return request({
    url: `/api/v1/requirements/${id}`,
    method: 'post',
    data
  })
}

/**
 * 删除需求
 */
export const deleteRequirement = (id: number) => {
  return request({
    url: `/api/v1/requirements/${id}`,
    method: 'delete'
  })
}

/**
 * 获取需求下拉选项列表（id + name 映射）
 */
export const getRequirementOptions = async () => {
  const res = await getRequirementList({ page: 1, size: 100 })
  const list = res.data?.list ?? []
  return list.map((item: any) => ({
    id: item.id,
    name: item.name || item.title || `需求 ${item.id}`
  }))
}
