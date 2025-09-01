<template>
  <div class="error-page">
    <div class="error-content">
      <!-- 404 插图或图标 -->
      <div class="error-image">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" stroke-linecap="round" stroke-linejoin="round" class="icon">
          <circle cx="12" cy="12" r="10"/>
          <line x1="12" y1="8" x2="12" y2="12"/>
          <line x1="12" y1="16" x2="12.01" y2="16"/>
        </svg>
      </div>

      <h1 class="error-title">404</h1>
      <p class="error-message">抱歉，您访问的页面不存在</p>

      <div class="error-actions">
        <router-link to="/" class="back-home">返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 可在此添加埋点或日志上报逻辑（可选）
</script>

<style scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.error-content {
  text-align: center;
  padding: 2rem;
}

.error-image {
  width: 80px;
  height: 80px;
  margin: 0 auto 1rem;
  border-radius: 50%;
  background-color: #e0e0e0;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #999;
}

.icon {
  width: 48px;
  height: 48px;
}

.error-title {
  font-size: 3.5rem;
  color: #333;
  margin: 0 0 0.5rem;
  font-weight: 700;
}

.error-message {
  color: #666;
  font-size: 1.1rem;
  margin: 0 0 1.5rem;
}

.error-actions {
  display: flex;
  justify-content: center;
}

.back-home {
  display: inline-block;
  padding: 0.6rem 1.5rem;
  font-size: 1rem;
  color: #fff;
  background-color: #1890ff;
  border-radius: 4px;
  text-decoration: none;
  transition: background-color 0.3s;
}

.back-home:hover {
  background-color: #40a9ff;
}
</style>