import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src') // 配置 @ 指向 src 目录，方便导入
    }
  },
  server: {
    port: 5173, // 可自定义端口
    proxy: {
      // 配置后端接口代理（开发环境跨域用）
      '/api': {
        target: 'http://localhost:8080', // 你的后端服务地址
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
