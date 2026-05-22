import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import tailwindcss from '@tailwindcss/vite'
import { fileURLToPath, URL } from 'node:url'

/**
 * Vite配置文件
 */
export default defineConfig({
  plugins: [vue(), tailwindcss()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 80,
    host: '0.0.0.0',
    proxy: {
      '/api/v1/auth': {
        target: 'http://localhost:9001',
        changeOrigin: true
      },
      '/api/v1/users': {
        target: 'http://localhost:9002',
        changeOrigin: true
      },
      '/api/v1/projects': {
        target: 'http://localhost:9003',
        changeOrigin: true
      },
      '/api/v1/testcases': {
        target: 'http://localhost:9004',
        changeOrigin: true
      },
      '/api/v1/defects': {
        target: 'http://localhost:9005',
        changeOrigin: true
      },
      '/api/v1/requirements': {
        target: 'http://localhost:9006',
        changeOrigin: true
      },
      '/api/v1/dashboard': {
        target: 'http://localhost:9007',
        changeOrigin: true
      },
      '/api/v1/strategy': {
        target: 'http://localhost:9008',
        changeOrigin: true
      },
      '/api/v1/ai': {
        target: 'http://localhost:9010',
        changeOrigin: true
      }
    }
  }
})