<template>
  <div class="pay-container">
    <div class="page-header">
      <h1>缴费大厅</h1>
      <p>查看并管理您的所有缴费项目</p>
    </div>
    
    <!-- 详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="缴费项目详情"
      width="500px"
      destroy-on-close
    >
      <div v-if="currentItem">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-xl font-bold">{{ currentItem.title }}</h3>
          <el-tag :type="tagType(currentItem.status)" effect="dark">{{ statusText(currentItem.status) }}</el-tag>
        </div>
        
        <el-descriptions :column="1" border>
          <el-descriptions-item label="学期">{{ currentItem.term }}</el-descriptions-item>
          <el-descriptions-item label="类别">{{ currentItem.category }}</el-descriptions-item>
          <el-descriptions-item label="截止日期">{{ currentItem.dueDate }}</el-descriptions-item>
          <el-descriptions-item label="描述">{{ currentItem.description || '无' }}</el-descriptions-item>
          <el-descriptions-item label="金额">
            <div v-if="currentItem.originalAmount" class="flex items-center">
              <span class="line-through text-gray-400 mr-2">￥{{ currentItem.originalAmount }}</span>
              <el-tag size="small" type="danger">已减免 ￥{{ currentItem.discount }}</el-tag>
            </div>
            <div class="text-xl font-bold text-primary">￥{{ currentItem.amount }}</div>
          </el-descriptions-item>
          <el-descriptions-item v-if="currentItem.paidAt" label="缴费时间">
            {{ currentItem.paidAt }}
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="flex justify-end mt-4">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button 
            v-if="currentItem.status === 'UNPAID' || currentItem.status === '未缴费' || currentItem.status === 'PARTIAL_EXEMPTION' || currentItem.status === '部分减免'"
            type="primary" 
            @click="payAndClose(currentItem.id)"
          >
            <el-icon class="mr-1"><CreditCard /></el-icon>
            立即缴费
          </el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 筛选和搜索区域 -->
    <div class="filter-card mb-6">
      <div class="flex flex-wrap gap-4 items-center">
        <div class="search-box flex-1 min-w-[200px]">
          <el-input
            v-model="keyword"
            placeholder="搜索缴费项目"
            prefix-icon="Search"
            clearable
            @input="updateFilters"
          />
        </div>
        <div class="filter-options flex gap-3">
          <el-select v-model="termFilter" placeholder="学期" clearable class="filter-select">
            <el-option
              v-for="term in availableTerms"
              :key="term"
              :label="term"
              :value="term"
            />
          </el-select>
          <el-select v-model="categoryFilter" placeholder="类别" clearable class="filter-select" @change="updateFilters">
            <el-option label="学费" value="学费" />
            <el-option label="住宿费" value="住宿费" />
            <el-option label="书本费" value="书本费" />
            <el-option label="其他费用" value="其他费用" />
          </el-select>
          <el-select v-model="statusFilter" placeholder="状态" clearable class="filter-select" @change="updateFilters">
            <el-option label="未缴费" value="UNPAID" />
            <el-option label="已缴费" value="PAID" />
            <el-option label="已逾期" value="OVERDUE" />
            <el-option label="部分减免" value="PARTIAL_EXEMPTION" />
          </el-select>
          <el-button type="primary" @click="refresh" :loading="loading.value">
            <el-icon class="mr-1"><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计信息卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
      <div class="stat-card" @mouseover="showStatDetails = 'pending'" @mouseleave="showStatDetails = ''">
        <el-icon class="text-blue-500 text-xl mb-2"><Wallet /></el-icon>
        <div class="text-2xl font-bold text-blue-500 mb-2">
          <span class="number-animation">{{ pendingCount }}</span>
        </div>
        <div class="text-gray-600">待缴费项目</div>
        <div class="stat-details" v-show="showStatDetails === 'pending'">
          <div class="text-sm text-gray-500">近期截止: {{ upcomingDeadlines }} 项</div>
          <div class="text-sm text-gray-500">已逾期: {{ overdueCount }} 项</div>
        </div>
      </div>
      <div class="stat-card" @mouseover="showStatDetails = 'completed'" @mouseleave="showStatDetails = ''">
        <el-icon class="text-green-500 text-xl mb-2"><CircleCheck /></el-icon>
        <div class="text-2xl font-bold text-green-500 mb-2">
          <span class="number-animation">{{ completedCount }}</span>
        </div>
        <div class="text-gray-600">已完成项目</div>
        <div class="stat-details" v-show="showStatDetails === 'completed'">
          <div class="text-sm text-gray-500">本月完成: {{ monthlyCompleted }} 项</div>
          <div class="text-sm text-gray-500">总金额: ¥{{ totalCompletedAmount }}</div>
        </div>
      </div>
      <div class="stat-card" @mouseover="showStatDetails = 'amount'" @mouseleave="showStatDetails = ''">
        <el-icon class="text-red-500 text-xl mb-2"><Money /></el-icon>
        <div class="text-2xl font-bold text-red-500 mb-2">
          <span class="number-animation">¥{{ totalPendingAmount }}</span>
        </div>
        <div class="text-gray-600">待缴总金额</div>
        <div class="stat-details" v-show="showStatDetails === 'amount'">
          <div class="text-sm text-gray-500">最高单项: ¥{{ highestPendingAmount }}</div>
          <div class="text-sm text-gray-500">平均金额: ¥{{ averagePendingAmount }}</div>
        </div>
      </div>
    </div>

    <!-- 缴费项目列表 -->
    <div class="mb-6">
      <div v-if="filtered.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 payment-items">
        <div
          v-for="item in filtered"
          :key="item.id"
          class="payment-card cursor-pointer"
          @click="showDetails(item)"
        >
          <div class="p-4 border-b flex justify-between items-center">
            <h3 class="text-base font-medium truncate mr-2">{{ item.title }}</h3>
            <el-tag :type="tagType(item.status)" size="small">{{ statusText(item.status) }}</el-tag>
          </div>
          <div class="p-4">
            <p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ item.description }}</p>
            <div class="flex justify-between text-xs text-gray-500 mb-2">
              <span>类别: {{ item.category }}</span>
              <span>学期: {{ item.term }}</span>
            </div>
            <div class="flex items-center text-xs text-red-500">
              <el-icon class="mr-1"><Calendar /></el-icon>
              <span>截止日期: {{ item.dueDate }}</span>
            </div>
          </div>
          <div class="p-3 bg-gray-50 flex justify-between items-center">
            <div class="text-lg font-bold text-red-500">¥{{ item.amount }}</div>
            <el-button
              v-if="item.status === 'UNPAID' || item.status === '未缴费'"
              type="primary"
              size="small"
              @click.stop="markAsPaid(item.id)"
              :loading="loading.value"
            >
              立即缴费
            </el-button>
            <div v-else-if="item.status === 'PAID' || item.status === '已缴费'" class="flex items-center text-xs text-green-500">
              <el-icon class="mr-1"><Check /></el-icon>
              <span>{{ item.paidAt }}</span>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="flex justify-center items-center py-12">
        <el-empty description="暂无符合条件的缴费项目" />
      </div>
    </div>

    <!-- 无数据提示 -->
    <el-empty v-if="filtered.length === 0" description="没有找到符合条件的缴费项目" />
    
    <!-- 分页控件 -->
    <div class="flex justify-center mt-4" v-if="filtered.length > 0">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <!-- 加载状态 -->
    <el-overlay v-if="loading.value" :z-index="1000">
      <div class="flex justify-center items-center h-full">
        <el-spin size="large" />
      </div>
    </el-overlay>
    
    <!-- 错误提示 -->
    <el-alert
      v-if="error.value"
      :title="error.value"
      type="error"
      show-icon
      closable
      @close="payStore.clearError()"
    />
  </div>
</template>

<script setup>
import { computed, ref, onMounted, watch } from 'vue'
import { usePayStore } from '@/store/pay'
import { storeToRefs } from 'pinia'
import { Search, Refresh, Money, Calendar, Timer, CreditCard, CircleCheck, Wallet, Check } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const payStore = usePayStore()
const { payments, loading, error, statistics, currentPage, pageSize, total } = storeToRefs(payStore)
const markAsPaid = payStore.markPaid
const refresh = payStore.fetchPaymentItems

// 分页处理方法
const handleSizeChange = (size) => {
  payStore.setPageSize(size)
  refresh()
}

const handleCurrentChange = (page) => {
  payStore.setCurrentPage(page)
  refresh()
}

// 详情对话框相关
const dialogVisible = ref(false)
const currentItem = ref(null)

// 显示详情对话框
const showDetails = (item) => {
  currentItem.value = item
  dialogVisible.value = true
}

// 缴费并关闭对话框
const payAndClose = async (id) => {
  try {
    await markAsPaid(id)
    ElMessage.success('支付订单创建成功')
    dialogVisible.value = false
    // 刷新缴费项目列表
    refresh()
  } catch (err) {
    ElMessage.error('支付失败：' + (err.message || '未知错误'))
  }
}

const keyword = ref('')
const categoryFilter = ref('')
const statusFilter = ref('')
const termFilter = ref('')
const showStatDetails = ref('')

// 统计卡片的详细信息计算属性
const upcomingDeadlines = computed(() => {
  const now = new Date()
  const thirtyDaysFromNow = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000)
  return payments.value.filter(item => {
    const dueDate = new Date(item.dueDate)
    return item.status === '未缴费' && dueDate > now && dueDate <= thirtyDaysFromNow
  }).length
})

const overdueCount = computed(() => {
  const now = new Date()
  return payments.value.filter(item => {
    const dueDate = new Date(item.dueDate)
    return item.status === '未缴费' && dueDate < now
  }).length
})

const monthlyCompleted = computed(() => {
  const now = new Date()
  const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1)
  return payments.value.filter(item => {
    if (!item.paidAt) return false
    const paidDate = new Date(item.paidAt)
    return item.status === '已缴费' && paidDate >= startOfMonth
  }).length
})

const totalCompletedAmount = computed(() => {
  return payments.value
    .filter(item => item.status === '已缴费')
    .reduce((sum, item) => sum + parseFloat(item.amount), 0)
    .toFixed(2)
})

const highestPendingAmount = computed(() => {
  const pendingPayments = payments.value.filter(item => item.status === '未缴费')
  if (pendingPayments.length === 0) return '0.00'
  return Math.max(...pendingPayments.map(item => parseFloat(item.amount))).toFixed(2)
})

const averagePendingAmount = computed(() => {
  const pendingPayments = payments.value.filter(item => item.status === '未缴费')
  if (pendingPayments.length === 0) return '0.00'
  const total = pendingPayments.reduce((sum, item) => sum + parseFloat(item.amount), 0)
  return (total / pendingPayments.length).toFixed(2)
})

// 获取所有可用的学期选项
const availableTerms = computed(() => {
  const terms = new Set()
  payments.value.forEach(payment => {
    if (payment.term) {
      terms.add(payment.term)
    }
  })
  return Array.from(terms).sort()
})

// 加载数据
onMounted(async () => {
  try {
    // 加载缴费项目列表
    await refresh()
    
    // 加载统计信息
    await payStore.fetchPaymentStatistics()
  } catch (err) {
    ElMessage.error('加载数据失败：' + (err.message || '未知错误'))
  }
})

// 监听筛选条件变化，调用API获取数据
const updateFilters = () => {
  payStore.setFilters({
    name: keyword.value,
    category: categoryFilter.value,
    status: statusFilter.value
  })
  refresh()
}

// 使用计算属性获取筛选后的数据
const filtered = computed(() => {
  // 如果有学期筛选，需要在前端进行过滤，因为后端API可能不支持按学期筛选
  if (termFilter.value) {
    return payments.value.filter(item => item.term === termFilter.value)
  }
  return payments.value
})

// 统计数据计算属性
const pendingCount = computed(() => statistics.value?.pendingItemCount || 0)
const completedCount = computed(() => statistics.value?.paidItemCount || 0)
const totalPendingAmount = computed(() => statistics.value?.pendingAmount?.toFixed(2) || '0.00')

const tagType = (status) => {
  // 根据后端返回的状态值映射标签类型
  switch (status) {
    case 'PAID': return 'success'
    case 'UNPAID': return 'warning'
    case 'OVERDUE': return 'danger'
    case 'PARTIAL_EXEMPTION': return 'info'
    // 兼容中文状态（如果后端返回中文）
    case '已缴费': return 'success'
    case '未缴费': return 'warning'
    case '已逾期': return 'danger'
    case '部分减免': return 'info'
    default: return 'info'
  }
}

// 状态显示文本
const statusText = (status) => {
  // 根据后端返回的状态值映射显示文本
  switch (status) {
    case 'PAID': return '已缴费'
    case 'UNPAID': return '未缴费'
    case 'OVERDUE': return '已逾期'
    case 'PARTIAL_EXEMPTION': return '部分减免'
    // 如果已经是中文，则直接返回
    default: return status
  }
}
</script>

<style scoped>
.pay-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  animation: fadeIn 0.6s ease-out;
}

.page-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #ebeef5;
  position: relative;
  animation: slideDown 0.5s ease-out;
}

.page-header::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 100px;
  height: 2px;
  background: linear-gradient(90deg, #1890ff, #69c0ff);
  animation: expandWidth 0.8s ease-out forwards;
}

.page-header h1 {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
  background: linear-gradient(120deg, #1890ff, #69c0ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: fadeInLeft 0.5s ease-out;
}

.page-header p {
  color: #606266;
  font-size: 16px;
  animation: fadeInLeft 0.5s ease-out 0.2s both;
}

.filter-card {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(245, 247, 250, 0.8);
  backdrop-filter: blur(10px);
  padding: 20px;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.filter-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.filter-select {
  width: 140px;
  transition: all 0.3s ease;
}

.filter-select:hover {
  transform: translateY(-2px);
}

.stat-card {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.1);
  position: relative;
  overflow: hidden;
  min-height: 160px;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transform: translateX(-100%);
  transition: 0.5s;
}

.stat-card:hover::before {
  transform: translateX(100%);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.12);
}

.stat-card .el-icon {
  transition: transform 0.3s ease;
}

.stat-card:hover .el-icon {
  transform: scale(1.2);
}

.number-animation {
  display: inline-block;
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.stat-card:hover .number-animation {
  transform: scale(1.1);
  color: var(--el-color-primary);
}

.stat-details {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px dashed rgba(0, 0, 0, 0.1);
  opacity: 0;
  transform: translateY(10px);
  transition: all 0.3s ease;
}

.stat-details[v-show="true"] {
  opacity: 1;
  transform: translateY(0);
}

.stat-details > div {
  margin: 4px 0;
  transition: color 0.3s ease;
}

.stat-details > div:hover {
  color: var(--el-color-primary) !important;
}

.payment-card {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  border: 1px solid rgba(235, 238, 245, 0.8);
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.9);
  position: relative;
}

.payment-card::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(120deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transform: translateX(-100%);
  transition: 0.5s;
}

.payment-card:hover::after {
  transform: translateX(100%);
}

.payment-card:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-8px);
}

.status-tag {
  font-weight: 600;
  transition: all 0.3s ease;
}

.pay-button {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.pay-button::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 5px;
  height: 5px;
  background: rgba(255, 255, 255, 0.5);
  opacity: 0;
  border-radius: 100%;
  transform: scale(1, 1) translate(-50%);
  transform-origin: 50% 50%;
}

.pay-button:active::after {
  animation: ripple 1s ease-out;
}

.pay-button:hover {
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2);
}

.completed-button {
  opacity: 0.7;
  transition: opacity 0.3s ease;
}

/* 详情对话框样式 */
.payment-detail h2 {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 24px;
  background: linear-gradient(120deg, #1890ff, #69c0ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* 动画关键帧 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideDown {
  from { transform: translateY(-20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

@keyframes expandWidth {
  from { width: 0; }
  to { width: 100px; }
}

@keyframes fadeInLeft {
  from { transform: translateX(-20px); opacity: 0; }
  to { transform: translateX(0); opacity: 1; }
}

@keyframes ripple {
  0% { transform: scale(0, 0); opacity: 0.5; }
  20% { transform: scale(25, 25); opacity: 0.3; }
  100% { opacity: 0; transform: scale(40, 40); }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .pay-container {
    padding: 16px;
    border-radius: 12px;
  }

  .payment-items {
    grid-template-columns: 1fr;
  }
  
  .filter-options {
    width: 100%;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 8px;
  }

  .filter-select {
    width: calc(50% - 4px);
  }

  .page-header h1 {
    font-size: 24px;
  }

  .page-header p {
    font-size: 14px;
  }
}
</style>
