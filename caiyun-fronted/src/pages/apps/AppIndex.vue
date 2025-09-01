<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>应用中心</h1>
      <p>探索和管理您的所有应用</p>
    </div>

    <!-- 搜索和筛选 -->
    <div class="filter-section">
      <div class="search-box">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索应用"
          prefix-icon="Search"
          clearable
        />
      </div>
      <div class="filter-tags">
        <el-radio-group v-model="currentCategory" size="large">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button
            v-for="category in categories"
            :key="category.value"
            :label="category.value"
          >
            {{ category.label }}
          </el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <!-- 应用网格 -->
    <div class="apps-grid">
      <div
        v-for="(app, index) in filteredApps"
        :key="app.id"
        class="app-card"
        :style="{ animationDelay: getAnimationDelay(index) }"
        @click="launchApp(app)"
        v-loading="isLoading && selectedApp?.id === app.id"
      >
        <div class="app-icon" :style="{ backgroundColor: app.iconBg }">
          <el-icon :size="24">
            <component :is="app.icon" />
          </el-icon>
        </div>
        <div class="app-info">
          <h3>{{ app.name }}</h3>
          <p>{{ app.description }}</p>
        </div>
        <div class="app-meta">
          <el-tag size="small" :type="app.status === 'active' ? 'success' : 'info'">
            {{ app.status === 'active' ? '已启用' : '未启用' }}
          </el-tag>
          <span class="update-time">{{ app.lastUpdated }}</span>
        </div>
      </div>
    </div>

    <!-- 无数据提示 -->
    <el-empty v-if="filteredApps.length === 0" description="没有找到符合条件的应用" />

    <!-- 应用详情抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      title="应用详情"
      size="30%"
      :destroy-on-close="true"
    >
      <template v-if="selectedApp">
        <div class="app-detail">
          <div class="detail-header">
            <div class="detail-icon" :style="{ backgroundColor: selectedApp.iconBg }">
              <el-icon :size="32">
                <component :is="selectedApp.icon" />
              </el-icon>
            </div>
            <div class="detail-title">
              <h2>{{ selectedApp.name }}</h2>
              <p>{{ selectedApp.description }}</p>
            </div>
          </div>

          <el-divider />

          <div class="detail-info">
            <h3>基本信息</h3>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="状态">
                <el-tag :type="selectedApp.status === 'active' ? 'success' : 'info'">
                  {{ selectedApp.status === 'active' ? '已启用' : '未启用' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="最后更新">
                {{ selectedApp.lastUpdated }}
              </el-descriptions-item>
              <el-descriptions-item label="版本">
                {{ selectedApp.version }}
              </el-descriptions-item>
              <el-descriptions-item label="开发者">
                {{ selectedApp.developer }}
              </el-descriptions-item>
            </el-descriptions>

            <h3 class="mt-6">功能特性</h3>
            <div class="features-list">
              <div
                v-for="feature in selectedApp.features"
                :key="feature"
                class="feature-item"
              >
                <el-icon><Check /></el-icon>
                <span>{{ feature }}</span>
              </div>
            </div>
          </div>

          <div class="detail-actions">
            <el-button type="primary" @click="launchApp(selectedApp)">
              <el-icon><Position /></el-icon> 打开应用
            </el-button>
            <el-button
              v-if="selectedApp.status === 'active'"
              @click="toggleAppStatus(selectedApp)"
            >
              <el-icon><TurnOff /></el-icon> 禁用应用
            </el-button>
            <el-button
              v-else
              type="success"
              @click="toggleAppStatus(selectedApp)"
            >
              <el-icon><Open /></el-icon> 启用应用
            </el-button>
          </div>
        </div>
      </template>
    </el-drawer>
  </div>
</template> <!-- ✅ 修复1: 补全 </template> -->

<script setup lang="ts">
import { ref, computed } from 'vue'
import {
  Search,
  Check,
  Position,
  TurnOff,
  Open,
  Money,
  Calendar,
  Tools,
  Reading,
  CreditCard,
  Document,
  Setting,
  Refresh
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// 应用分类
const categories = [
  { value: 'finance', label: '财务应用' },
  { value: 'academic', label: '教务应用' },
  { value: 'life', label: '生活服务' },
  { value: 'tools', label: '实用工具' }
]

// 模拟应用数据
const apps = ref([
  {
    id: 1,
    name: '缴费中心',
    description: '在线缴纳学费、住宿费等各项费用',
    icon: 'Money',
    iconBg: '#4CAF50',
    category: 'finance',
    status: 'active',
    lastUpdated: '2024-03-15',
    version: '2.1.0',
    developer: '校园信息化中心',
    features: ['在线支付', '费用查询', '账单管理', '支付记录', '自动提醒']
  },
  {
    id: 2,
    name: '选课系统',
    description: '课程查询、选课、退课一站式服务',
    icon: 'Calendar',
    iconBg: '#2196F3',
    category: 'academic',
    status: 'active',
    lastUpdated: '2024-03-10',
    version: '3.0.1',
    developer: '教务处',
    features: ['课程搜索', '快速选课', '课表查看', '成绩查询']
  },
  {
    id: 3,
    name: '宿舍报修',
    description: '在线提交宿舍维修申请',
    icon: 'Tools',
    iconBg: '#FF9800',
    category: 'life',
    status: 'active',
    lastUpdated: '2024-03-12',
    version: '1.5.0',
    developer: '后勤保障处',
    features: ['故障报修', '进度跟踪', '满意度评价', '维修记录']
  },
  {
    id: 4,
    name: '图书馆',
    description: '图书借阅、续借、预约服务',
    icon: 'Reading',
    iconBg: '#9C27B0',
    category: 'academic',
    status: 'active',
    lastUpdated: '2024-03-08',
    version: '2.3.2',
    developer: '图书馆',
    features: ['馆藏查询', '在线续借', '预约座位', '借阅历史']
  },
  {
    id: 5,
    name: '校园卡',
    description: '校园卡充值、挂失、账单查询',
    icon: 'CreditCard',
    iconBg: '#E91E63',
    category: 'life',
    status: 'active',
    lastUpdated: '2024-03-14',
    version: '2.0.1',
    developer: '校园卡中心',
    features: ['余额查询', '在线充值', '消费记录', '卡片挂失']
  },
  {
    id: 6,
    name: '成绩查询',
    description: '查询各学期成绩和学分统计',
    icon: 'Document',
    iconBg: '#3F51B5',
    category: 'academic',
    status: 'active',
    lastUpdated: '2024-03-11',
    version: '1.8.0',
    developer: '教务处',
    features: ['成绩查询', '学分统计', '成绩分析', 'GPA计算']
  }
])

// 搜索和筛选
const searchKeyword = ref('')
const currentCategory = ref('all')

// 筛选后的应用列表
const filteredApps = computed(() => {
  return apps.value.filter(app => {
    const matchesSearch =
      app.name.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
      app.description.toLowerCase().includes(searchKeyword.value.toLowerCase())
    const matchesCategory = currentCategory.value === 'all' || app.category === currentCategory.value
    return matchesSearch && matchesCategory
  })
})

// 应用详情抽屉
const drawerVisible = ref(false)
const selectedApp = ref(null)
const isLoading = ref(false)

// 动画相关
const getAnimationDelay = (index: number) => `${0.1 * (index % 6)}s`

// 打开应用
const launchApp = async (app) => {
  if (app.status !== 'active') {
    ElMessage.warning('请先启用该应用')
    return
  }
  isLoading.value = true
  selectedApp.value = app
  drawerVisible.value = true

  try {
    // 模拟应用加载
    await new Promise(resolve => setTimeout(resolve, 800))
    ElMessage.success('应用加载成功')
  } catch (error) {
    ElMessage.error('应用加载失败，请重试')
  } finally {
    isLoading.value = false
  }
}

// 切换应用状态
const toggleAppStatus = (app) => {
  app.status = app.status === 'active' ? 'inactive' : 'active'
  const message = app.status === 'active' ? '应用已启用' : '应用已禁用'
  ElMessage.success(message)
}
</script>

<style scoped>
.app-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  color: #1f2937;
}

.page-header p {
  margin: 8px 0 0;
  color: #6b7280;
}

.filter-section {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.filter-section .search-box {
  flex: 1;
  min-width: 240px;
}

.filter-section .filter-tags {
  flex-shrink: 0;
}

.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.app-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
  opacity: 0;
  animation: fadeInUp 0.5s ease forwards;
}

.app-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.app-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-bottom: 12px;
}

.app-info h3 {
  margin: 0 0 8px;
  font-size: 16px;
  color: #1f2937;
}

.app-info p {
  margin: 0;
  font-size: 14px;
  color: #6b7280;
  line-height: 1.4;
}

.app-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  font-size: 12px;
  color: #9ca3af;
}

.update-time {
  margin-left: 8px;
}

/* 抽屉样式 */
.app-detail {
  padding: 0 20px;
}

.detail-header {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.detail-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.detail-title h2 {
  margin: 0 0 8px;
  font-size: 20px;
  color: #1f2937;
}

.detail-title p {
  margin: 0;
  color: #6b7280;
}

.detail-info h3 {
  margin: 24px 0 16px;
  color: #1f2937;
}

.mt-6 {
  margin-top: 24px;
}

.features-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 12px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #1f2937;
  font-size: 14px;
}

.detail-actions {
  display: flex;
  gap: 12px;
  margin-top: 40px;
  flex-wrap: wrap;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style> <!-- ✅ 修复2: 补全 </style> -->