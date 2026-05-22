import request from '@/utils/request'

export interface User {
  id: number
  username: string
  email: string
  phone?: string
  realName?: string
  departmentId?: number
  status: number
  roles?: string[]
  permissions?: string[]
  createTime?: string
  updateTime?: string
}

export interface UserQuery {
  username?: string
  email?: string
  phone?: string
  status?: number
  departmentId?: number
  realName?: string
  pageNum?: number
  pageSize?: number
}

export interface UserCreate {
  username: string
  password: string
  email?: string
  phone?: string
  realName?: string
  departmentId?: number
  roleIds?: number[]
}

export interface UserUpdate {
  email?: string
  phone?: string
  realName?: string
  departmentId?: number
  roleIds?: number[]
}

export const userApi = {
  list: (params: UserQuery) => request.get<any>('/api/users', { params }),

  get: (id: number) => request.get<any>(`/api/users/${id}`),

  create: (data: UserCreate) => request.post<any>('/api/users', data),

  update: (id: number, data: UserUpdate) => request.put<any>(`/api/users/${id}`, data),

  delete: (id: number) => request.delete<any>(`/api/users/${id}`),

  disable: (id: number) => request.put<any>(`/api/users/${id}/disable`),

  enable: (id: number) => request.put<any>(`/api/users/${id}/enable`),

  resetPassword: (id: number, newPassword: string) =>
    request.put<any>(`/api/users/${id}/reset-password`, null, { params: { newPassword } }),

  batchDisable: (userIds: number[]) => request.put<any>('/api/users/batch-disable', userIds),

  batchEnable: (userIds: number[]) => request.put<any>('/api/users/batch-enable', userIds),
}
