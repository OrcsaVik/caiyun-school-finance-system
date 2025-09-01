<template>
  <div class="app-root">
    <router-view v-slot="{ Component, route }">
      <component :is="route.meta?.layout === 'auth' ? AuthLayout : 'div'">
        <div v-if="!route.meta?.layout && !route.path.startsWith('/pay')" class="topbar">
          <div class="inner">
            <div class="brand">学校财务管理系统</div>
            <div class="nav">
              <a @click.prevent="$router.push('/')">服务大厅</a>
              <a @click.prevent="$router.push('/pay/dashboard')">缴费大厅</a>
              <a @click.prevent="$router.push('/apps')">应用中心</a>
              <a @click.prevent="$router.push('/login')">统一认证</a>
            </div>
          </div>
        </div>
        <div :class="{ 'page-container': !route.meta?.layout && !route.path.startsWith('/pay') }">
          <component :is="Component" />
        </div>
      </component>
    </router-view>
  </div>
  
</template>

<script setup lang="ts">
import AuthLayout from '@/layouts/AuthLayout.vue'
</script>

<style scoped>
.app-root { min-height: 100vh; background: #f9fafb; }
.topbar { background:#fff; box-shadow: 0 1px 2px rgba(0,0,0,.06); }
.topbar .inner { max-width: 1120px; margin: 0 auto; padding: 12px 16px; display:flex; align-items:center; justify-content:space-between; }
.brand { font-size: 16px; font-weight: 600; }
.nav a { margin-left: 12px; color:#1f2937; cursor:pointer; text-decoration:none; }
.nav a:hover { color:#1a73e8; }
.page-container { max-width: 1120px; margin: 0 auto; padding: 24px 16px; }
</style>
