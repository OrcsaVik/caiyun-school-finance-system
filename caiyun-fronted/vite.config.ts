import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
// 导入 Node.js 的 url 模块中的 fileURLToPath
import { fileURLToPath } from 'url'

// 正确定义 __filename 和 __dirname（ES 模块环境中）
const __filename = fileURLToPath(import.meta.url) // 移除多余的 a，取消注释
const __dirname = path.dirname(__filename)

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'), // 此时 __dirname 已正确定义
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 已修复 localhost 拼写
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      '/mock': {
        target: 'http://localhost:5173',
        rewrite: (path) => path.replace(/^\/mock/, '/mock-data'),
      },
    },
  },
})