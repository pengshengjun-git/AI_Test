import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import './style.css'
import App from './App.vue'
import router from './router'

/**
 * 应用入口
 */
const app = createApp(App)
const pinia = createPinia()

// 注册所有Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 全局错误处理 - 捕获ResizeObserver警告
const originalErrorHandler = app.config.errorHandler
app.config.errorHandler = (err, instance, info) => {
  if (err instanceof Error && err.message.includes('ResizeObserver')) {
    return
  }
  if (originalErrorHandler) {
    originalErrorHandler(err, instance, info)
  }
}

// 捕获控制台ResizeObserver警告
if (typeof ResizeObserver !== 'undefined') {
  const ResizeObserverPolyfill = ResizeObserver
  ResizeObserver = class extends ResizeObserverPolyfill {
    constructor(callback: ResizeObserverCallback) {
      super((entries, observer) => {
        try {
          callback(entries, observer)
        } catch (e) {
          if (!(e instanceof Error && e.message.includes('ResizeObserver'))) {
            throw e
          }
        }
      })
    }
  }
}

app.use(pinia)
app.use(router)
app.use(ElementPlus, {
  locale: zhCn
})

app.mount('#app')
