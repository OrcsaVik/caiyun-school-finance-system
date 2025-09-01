<template>
  <div class="payment-layout">
    <!-- 顶部栏 -->
    <div class="payment-header">
      <div class="header-content">
        <div class="logo-section">
          <img src="@\assets\dz_logo.png" alt="Logo" class="logo" />
          <h1>收费系统</h1>
        </div>
        <div class="user-section">
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32" src="/user1.png" />
              <span class="username">张三</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <el-icon><User /></el-icon>个人信息
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-icon><SwitchButton /></el-icon>切换身份
                </el-dropdown-item>
                <el-dropdown-item divided>
                  <el-icon><SwitchFilled /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    
    <div class="payment-container">
      <!-- 左侧导航栏 -->
      <div class="payment-sidebar">
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          router>
          <el-menu-item index="/pay/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>缴费大厅</span>
          </el-menu-item>
          <el-menu-item index="/pay/unpaid">
            <el-icon><Wallet /></el-icon>
            <span>待缴费用</span>
          </el-menu-item>
          <el-menu-item index="/pay/history">
            <el-icon><Tickets /></el-icon>
            <span>缴费记录</span>
          </el-menu-item>
          <el-menu-item index="/pay/refund">
            <el-icon><Money /></el-icon>
            <span>退费情况</span>
          </el-menu-item>
          <el-menu-item index="/pay/profile">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
        </el-menu>
      </div>
      
      <!-- 内容区域 -->
      <div class="payment-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  HomeFilled,
  Wallet,
  Tickets,
  Money,
  User,
  ArrowDown,
  SwitchButton,
  SwitchFilled
} from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.payment-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
}

.payment-header {
  height: 60px;
  background-color: #1890ff;
  color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.logo-section {
  display: flex;
  align-items: center;
}

.logo {
  height: 36px;
  margin-right: 10px;
}

.user-section {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin: 0 8px;
  font-size: 14px;
}

.payment-container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.payment-sidebar {
  width: 220px;
  background-color: white(44, 35, 176);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  z-index: 5;
}

.sidebar-menu {
  border-right: none;
  height: 100%;
}

.payment-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .payment-container {
    flex-direction: column;
  }
  
  .payment-sidebar {
    width: 100%;
    height: auto;
  }
  
  .sidebar-menu {
    display: flex;
    justify-content: space-around;
  }
  
  .el-menu-item {
    flex: 1;
    display: flex;
    justify-content: center;
  }
  
  .el-menu-item span {
    display: none;
  }
}
</style>