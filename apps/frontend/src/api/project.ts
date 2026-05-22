import request from '@/utils/request'

export interface Project {
  id?: number
  name: string
  code?: string
  description?: string
  status: 'PLANNING' | 'IN_PROGRESS' | 'COMPLETED' | 'ARCHIVED'
  priority?: string
  testcaseCount?: number
  requirementCount?: number
  defectCount?: number
  createdAt?: string
  updatedAt?: string
  createTime?: string
  updateTime?: string
  ownerName?: string
  createdBy?: number
}

export interface ProjectQueryParams {
  name?: string
  status?: string
  page?: number
  size?: number
}

/**
 * 获取项目列表
 */
export const getProjectList = (params?: ProjectQueryParams) => {
  return request({
    url: '/api/v1/projects',
    method: 'get',
    params
  })
}

/**
 * 获取项目详情
 */
export const getProjectDetail = (id: number) => {
  return request({
    url: `/api/v1/projects/${id}`,
    method: 'get'
  })
}

/**
 * 创建项目
 */
export const createProject = (data: Project) => {
  return request({
    url: '/api/v1/projects',
    method: 'post',
    data
  })
}

/**
 * 更新项目
 */
export const updateProject = (id: number, data: Partial<Project>) => {
  return request({
    url: `/api/v1/projects/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除项目
 */
export const deleteProject = (id: number) => {
  return request({
    url: `/api/v1/projects/${id}`,
    method: 'delete'
  })
}

/**
 * 获取项目下拉选项列表（id + name 映射）
 * 用于表单下拉框等场景，复用 getProjectList 接口
 */
export const getProjectOptions = async () => {
  const res = await getProjectList({ page: 1, size: 100 })
  const list = res.data?.list ?? []
  return list.map((item: any) => ({
    id: item.id,
    name: item.name
  }))
}
