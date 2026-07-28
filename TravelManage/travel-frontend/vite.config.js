import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5173,
    proxy: {
      // 只代理 /api/... 接口（不匹配 /api，只匹配 /api/xxx）
      '^/api/': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      // 只代理 /admin/... 接口（不匹配 /admin，只匹配 /admin/xxx）
      '^/admin/[a-z]+/': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path // 路径不变
      }
    }
  }
})