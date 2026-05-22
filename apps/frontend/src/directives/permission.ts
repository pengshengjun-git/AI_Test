import type { Directive, DirectiveBinding } from 'vue'
import { checkPermission } from '@/utils/permission'
import { useUserStore } from '@/stores/user'

interface PermissionDirectiveValue {
  permission: string
  disabled?: boolean
}

export const permissionDirective: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding<PermissionDirectiveValue>) {
    const { value } = binding
    
    if (!value) {
      console.warn('v-permission 指令缺少参数')
      return
    }

    const { permission, disabled = false } = value
    
    if (!checkPermission(permission)) {
      if (disabled) {
        el.setAttribute('disabled', 'true')
        el.classList.add('is-disabled')
      } else {
        el.parentNode?.removeChild(el)
      }
    }
  },
  updated(el: HTMLElement, binding: DirectiveBinding<PermissionDirectiveValue>) {
    const { value, oldValue } = binding
    
    if (!value) {
      console.warn('v-permission 指令缺少参数')
      return
    }

    if (JSON.stringify(value) !== JSON.stringify(oldValue)) {
      const { permission, disabled = false } = value
      
      if (!checkPermission(permission)) {
        if (disabled) {
          el.setAttribute('disabled', 'true')
          el.classList.add('is-disabled')
        } else {
          el.parentNode?.removeChild(el)
        }
      } else {
        el.removeAttribute('disabled')
        el.classList.remove('is-disabled')
      }
    }
  }
}

export const roleDirective: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding<string | string[]>) {
    const { value } = binding
    
    if (!value) {
      console.warn('v-role 指令缺少参数')
      return
    }

    const roles = Array.isArray(value) ? value : [value]
    const userStore = useUserStore()
    const userInfo = userStore.getUserInfo()
    
    if (!userInfo || !userInfo.roles?.some(r => roles.includes(r))) {
      el.parentNode?.removeChild(el)
    }
  }
}

export default {
  install(app: any) {
    app.directive('permission', permissionDirective)
    app.directive('role', roleDirective)
  }
}
