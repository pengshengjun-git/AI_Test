import request from '@/utils/request'

export interface Department {
  id: number
  name: string
  parentId: number
  description?: string
  sortOrder?: number
  status: number
  children?: Department[]
  createTime?: string
  updateTime?: string
}

export interface DepartmentCreate {
  name: string
  parentId?: number
  description?: string
  sortOrder?: number
}

export interface DepartmentUpdate {
  name?: string
  parentId?: number
  description?: string
  sortOrder?: number
}

export const departmentApi = {
  getTree: () => request.get<any>('/api/departments/tree'),

  list: () => request.get<any>('/api/departments'),

  get: (id: number) => request.get<any>(`/api/departments/${id}`),

  create: (data: DepartmentCreate) => request.post<any>('/api/departments', data),

  update: (id: number, data: DepartmentUpdate) => request.put<any>(`/api/departments/${id}`, data),

  delete: (id: number) => request.delete<any>(`/api/departments/${id}`),
}
