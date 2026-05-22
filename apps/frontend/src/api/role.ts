import request from '@/utils/request'

export interface Role {
  id: number
  code: string
  name: string
  description?: string
  status: number
  menuIds?: number[]
  createTime?: string
  updateTime?: string
}

export interface RoleQuery {
  code?: string
  name?: string
  status?: number
  pageNum?: number
  pageSize?: number
}

export interface RoleCreate {
  code: string
  name: string
  description?: string
  menuIds?: number[]
}

export interface RoleUpdate {
  name?: string
  description?: string
  menuIds?: number[]
}

export const roleApi = {
  list: (params?: RoleQuery) => request.get<any>('/api/roles', { params }),

  getAll: () => request.get<any>('/api/roles/all'),

  get: (id: number) => request.get<any>(`/api/roles/${id}`),

  create: (data: RoleCreate) => request.post<any>('/api/roles', data),

  update: (id: number, data: RoleUpdate) => request.put<any>(`/api/roles/${id}`, data),

  delete: (id: number) => request.delete<any>(`/api/roles/${id}`),

  assignToUser: (roleId: number, userId: number) =>
    request.post<any>(`/api/roles/${roleId}/assign/${userId}`),

  removeFromUser: (roleId: number, userId: number) =>
    request.delete<any>(`/api/roles/${roleId}/remove/${userId}`),

  batchAssign: (userId: number, roleIds: number[]) =>
    request.post<any>(`/api/roles/batch-assign/${userId}`, roleIds),
}
