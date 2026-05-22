import { ElMessage } from 'element-plus'

let isShowingMessage = false

export const showMessage = (message: string, type: 'success' | 'warning' | 'info' | 'error' = 'error') => {
  if (!isShowingMessage) {
    isShowingMessage = true
    ElMessage({
      message,
      type,
      duration: 2000
    })
    setTimeout(() => {
      isShowingMessage = false
    }, 2000)
  }
}

export const showSuccess = (message: string) => showMessage(message, 'success')
export const showWarning = (message: string) => showMessage(message, 'warning')
export const showInfo = (message: string) => showMessage(message, 'info')
export const showError = (message: string) => showMessage(message, 'error')