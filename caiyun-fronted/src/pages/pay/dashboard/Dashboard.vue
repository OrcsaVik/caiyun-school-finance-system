<template>
  <div class="pay-dashboard">
    <div class="page-header">
      <h1>缴费大厅</h1>
      <p>查看并管理您的所有缴费项目</p>
    </div>
    
    <!-- 筛选和搜索区域 -->
    <div class="filter-card mb-6">
      <div class="flex flex-wrap gap-4 items-center">
        <div class="search-box flex-1 min-w-[200px]">
          <el-input
            v-model="keyword"
            placeholder="搜索缴费项目"
            prefix-icon="Search"
            clearable
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
          <el-select v-model="categoryFilter" placeholder="类别" clearable class="filter-select">
            <el-option label="学费" value="学费" />
            <el-option label="住宿费" value="住宿" />
            <el-option label="书本费" value="教材" />
            <el-option label="其他费用" value="其他" />
          </el-select>
          <el-select v-model="statusFilter" placeholder="状态" clearable class="filter-select">
            <el-option label="未缴费" value="未缴费" />
            <el-option label="已缴费" value="已缴费" />
            <el-option label="部分减免" value="部分减免" />
            <el-option label="已逾期" value="已逾期" />
          </el-select>
        </div>
      </div>
    </div>

    <!-- 统计信息卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
      <div class="stat-card">
        <div class="text-2xl font-bold text-blue-500 mb-2">{{ pendingCount }}</div>
        <div class="text-gray-600">待缴费项目</div>
      </div>
      <div class="stat-card">
        <div class="text-2xl font-bold text-green-500 mb-2">{{ completedCount }}</div>
        <div class="text-gray-600">已完成项目</div>
      </div>
      <div class="stat-card">
        <div class="text-2xl font-bold text-red-500 mb-2">¥{{ totalPendingAmount }}</div>
        <div class="text-gray-600">待缴总金额</div>
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
            <el-tag :type="tagType(item.status)" size="small">{{ item.status }}</el-tag>
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
              v-if="item.status === '未缴费'"
              type="primary"
              size="small"
              @click.stop="markAsPaid(item.id)"
            >
              立即缴费
            </el-button>
            <div v-else-if="item.status === '已缴费'" class="flex items-center text-xs text-green-500">
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
          <el-tag :type="tagType(currentItem.status)" effect="dark">{{ currentItem.status }}</el-tag>
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
            v-if="currentItem.status === '未缴费' || currentItem.status === '部分减免'"
            type="primary" 
            @click="payAndClose(currentItem.id)"
          >
            <el-icon class="mr-1"><CreditCard /></el-icon>
            立即缴费
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import { Search, Calendar, CreditCard, Check } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import mockPayData from '@/mock/pay'
import type { PaymentItem } from '@/types/pay'

// 支付项目列表
const payments = ref<PaymentItem[]>([])

// 详情对话框相关
const dialogVisible = ref(false)
const currentItem = ref<PaymentItem | null>(null)

// 加载支付项目列表（使用静态数据）
const loadPaymentItems = () => {
  try {
    // 使用模拟数据
    const mockResponse = mockPayData['/pay/items']
    
    // 处理模拟数据
    payments.value = mockResponse.list.map(item => ({
      ...item,
      // 确保所有必要字段都有值
      title: item.title || item.category || `缴费项目${item.id}`,
      description: item.description || `${item.title || item.category}的详细描述`,
      term: item.term || '当前学期',
      dueDate: item.dueDate ? new Date(item.dueDate).toLocaleDateString() : new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toLocaleDateString(),
      // 确保金额字段是数字
      amount: typeof item.amount === 'number' ? item.amount : parseFloat(item.amount || '0')
    }))
    
    // 添加更多模拟数据以便测试
    const additionalMockData = [
      {
        id: '3',
        title: '2024年春季教材费',
        description: '2024年春季学期教材费',
        amount: 300,
        category: '教材费',
        term: '2024春季',
        dueDate: '2024-03-15',
        status: '未缴费',
        createdAt: '2024-01-05',
        updatedAt: '2024-01-05'
      },
      {
        id: '4',
        title: '2024年春季实验室费',
        description: '2024年春季学期实验室使用费',
        amount: 500,
        category: '实验费',
        term: '2024春季',
        dueDate: '2024-03-10',
        status: '未缴费',
        createdAt: '2024-01-03',
        updatedAt: '2024-01-03'
      },
      {
        id: '5',
        title: '2024年春季校车费',
        description: '2024年春季学期校车服务费',
        amount: 800,
        category: '交通费',
        term: '2024春季',
        dueDate: '2024-02-28',
        status: '未缴费',
        createdAt: '2024-01-10',
        updatedAt: '2024-01-10'
      }
    ]
    
    // 将额外的模拟数据添加到支付列表中
    payments.value = [...payments.value, ...additionalMockData]
  } catch (error) {
    ElMessage.error('获取缴费项目失败')
    console.error('Failed to load payment items:', error)
  }
}

// 创建订单并轮询状态（使用静态数据）
const createOrderAndPoll = (paymentId: string) => {
  try {
    // 模拟创建订单响应
    const mockResponse = mockPayData['/pay/orders']
    const orderId = mockResponse.orderId
    
    // 模拟支付成功
    setTimeout(() => {
      ElMessage.success('支付成功')
      
      // 更新本地数据状态
      const index = payments.value.findIndex(item => item.id === paymentId)
      if (index !== -1) {
        payments.value[index].status = '已缴费'
        payments.value[index].paidAt = new Date().toLocaleDateString()
        payments.value[index].updatedAt = new Date().toISOString()
      }
    }, 1500) // 模拟1.5秒后支付成功
  } catch (error) {
    ElMessage.error('创建订单失败')
    console.error('Failed to create order:', error)
  }
}

// 立即缴费（使用静态数据）
const markAsPaid = (id: string) => {
  createOrderAndPoll(id)
}

// 初始化加载
onMounted(() => {
  loadPaymentItems()
})

// 显示详情对话框
const showDetails = (item: PaymentItem) => {
  currentItem.value = item
  dialogVisible.value = true
}

// 缴费并关闭对话框（使用静态数据）
const payAndClose = (id: string) => {
  createOrderAndPoll(id)
  setTimeout(() => {
    dialogVisible.value = false
  }, 1500) // 模拟1.5秒后关闭对话框
}

const keyword = ref('')
const categoryFilter = ref('')
const statusFilter = ref('')
const termFilter = ref('')

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

const filtered = computed(() => {
  return payments.value.filter(item => {
    // 关键词筛选
    if (keyword.value && !item.title.toLowerCase().includes(keyword.value.toLowerCase()) && 
        !item.description.toLowerCase().includes(keyword.value.toLowerCase())) {
      return false
    }
    
    // 类别筛选
    if (categoryFilter.value && item.category !== categoryFilter.value) {
      return false
    }
    
    // 状态筛选
    if (statusFilter.value && item.status !== statusFilter.value) {
      return false
    }
    
    // 学期筛选
    if (termFilter.value && item.term !== termFilter.value) {
      return false
    }
    
    return true
  })
})

// 统计数据计算
const pendingCount = computed(() => {
  return payments.value.filter(item => item.status === '未缴费').length
})

const completedCount = computed(() => {
  return payments.value.filter(item => item.status === '已缴费').length
})

const totalPendingAmount = computed(() => {
  return payments.value
    .filter(item => item.status === '未缴费')
    .reduce((sum, item) => sum + parseFloat(item.amount), 0)
    .toFixed(2)
})

const tagType = (status) => {
  if (status === '已缴费') return 'success'
  if (status === '未缴费') return 'warning'
  if (status === '部分减免') return 'info'
  if (status === '已逾期') return 'danger'
  return 'info'
}
</script>

<style scoped>
.pay-dashboard {
  width: 100%;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #1890ff;
}

.page-header p {
  color: #606266;
}

.filter-card {
  transition: all 0.3s ease;
  background-color: #f5f7fa;
  padding: 16px;
  border-radius: 8px;
}

.filter-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.filter-select {
  width: 120px;
}

.stat-card {
  transition: all 0.3s ease;
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px 0 rgba(0, 0, 0, 0.1);
}

.payment-card {
  transition: all 0.3s ease;
  overflow: hidden;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background-color: #fff;
}

.payment-card:hover {
  box-shadow: 0 5px 15px 0 rgba(0, 0, 0, 0.1);
  transform: translateY(-5px);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .payment-items {
    grid-template-columns: 1fr;
  }
  
  .filter-options {
    width: 100%;
    justify-content: space-between;
  }
}
</style>