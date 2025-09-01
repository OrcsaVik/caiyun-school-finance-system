<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold">服务大厅</h1>
    <div class="grid grid-cols-3 gap-4 mt-6">
      <el-card shadow="hover" @click="$router.push('/pay')">
        <div class="text-center">
          <el-icon><Money /></el-icon>
          <p class="mt-2">收费系统</p>
        </div>
      </el-card>

      <el-card shadow="hover">
        <div class="text-center opacity-50">
          <el-icon><Document /></el-icon>
          <p class="mt-2">其他应用（建设中）</p>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Document, Money } from '@element-plus/icons-vue'
</script>
