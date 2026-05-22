import request from '@/utils/request'

export interface Menu {
  id: number
  name: string
  parentId: number
  path?: string
  component?: string
  icon?: string
  sortOrder?: number
  type: number
  permissionCode?: string
  status: number
  children?: Menu[]
  createTime?: string
  updateTime?: string
}

export interface MenuCreate {
  name: string
  parentId?: number
  path?: string
  component?: string
  icon?: string
  sortOrder?: number
  type?: number
  permissionCode?: string
}

export interface MenuUpdate {
  name?: string
  parentId?: number
  path?: string
  component?: string
  icon?: string
  sortOrder?: number
  type?: number
  permissionCode?: string
  status?: number
}

export const menuApi = {
  getTree: () => request.get<any>('/api/menus/tree'),

  list: () => request.get<any>('/api/menus'),

  get: (id: number) => request.get<any>(`/api/menus/${id}`),

  create: (data: MenuCreate) => request.post<any>('/api/menus', data),

  update: (id: number, data: MenuUpdate) => request.put<any>(`/api/menus/${id}`, data),

  delete: (id: number) => request.delete<any>(`/api/menus/${id}`),

  getPermissions: () => request.get<any>('/api/menus/permissions'),
}
