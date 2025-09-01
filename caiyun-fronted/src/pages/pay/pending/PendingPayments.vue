<template>
  <div class="pending-payments">
    <div class="page-header">
      <h1>待缴费用</h1>
      <p>查看并处理您的待缴费项目</p>
    </div>
    
    <!-- 统计信息卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
      <div class="stat-card">
        <div class="text-2xl font-bold text-blue-500 mb-2">{{ pendingCount }}</div>
        <div class="text-gray-600">待缴费项目</div>
      </div>
      <div class="stat-card">
        <div class="text-2xl font-bold text-red-500 mb-2">{{ overdueCount }}</div>
        <div class="text-gray-600">已逾期项目</div>
      </div>
      <div class="stat-card">
        <div class="text-2xl font-bold text-red-500 mb-2">¥{{ totalPendingAmount }}</div>
        <div class="text-gray-600">待缴总金额</div>
      </div>
    </div>
    
    <!-- 筛选和搜索区域 -->
    <div class="filter-card mb-6">
      <div class="flex flex-wrap gap-4 items-center">
        <div class="search-box flex-1 min-w-[200px]">
          <el-input
            v-model="keyword"
            placeholder="搜索待缴费项目"
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
          <el-select v-model="urgencyFilter" placeholder="紧急程度" clearable class="filter-select">
            <el-option label="全部" value="" />
            <el-option label="已逾期" value="overdue" />
            <el-option label="即将到期" value="soon" />
          </el-select>
        </div>
      </div>
    </div>

    <!-- 待缴费项目表格 -->
    <el-card class="mb-6">
      <template #header>
        <div class="flex justify-between items-center">
          <span class="font-bold">待缴费项目列表</span>
          <el-button type="primary" @click="batchPay" :disabled="selectedItems.length === 0">
            <el-icon class="mr-1"><CreditCard /></el-icon>
            批量缴费
          </el-button>
        </div>
      </template>
      
      <el-table
        :data="paginatedItems"
        style="width: 100%"
        :empty-text="'暂无待缴费项目'"
        stripe
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="title" label="缴费项目" min-width="180">
          <template #default="{ row }">
            <div class="flex items-center">
              <el-icon class="mr-2"><Document /></el-icon>
              <span class="cursor-pointer text-blue-500 hover:underline" @click="showDetails(row)">
                {{ row.title }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="类别" width="120" />
        <el-table-column prop="term" label="学期" width="120" />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            <span class="font-bold">¥{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="截止日期" width="120">
          <template #default="{ row }">
            <div :class="{'text-red-500': isOverdue(row), 'text-orange-500': isSoonDue(row)}">
              {{ row.dueDate }}
              <el-tag v-if="isOverdue(row)" type="danger" size="small" effect="dark">已逾期</el-tag>
              <el-tag v-else-if="isSoonDue(row)" type="warning" size="small" effect="dark">即将到期</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="tagType(row.status)" effect="plain">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <div class="flex gap-2">
              <el-button type="primary" size="small" @click="payItem(row.id)">
                <el-icon><CreditCard /></el-icon> 缴费
              </el-button>
              <el-button type="info" size="small" @click="showDetails(row)">
                <el-icon><View /></el-icon> 详情
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="flex justify-end mt-4">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="filteredItems.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="缴费项目详情"
      width="500px"
      destroy-on-close
    >
      <div v-if="currentItem">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-xl font-bold">{{ currentItem.title || currentItem.category || `缴费项目${currentItem.id}` }}</h3>
          <el-tag :type="tagType(currentItem.status)" effect="dark">{{ currentItem.status }}</el-tag>
        </div>
        
        <el-descriptions :column="1" border>
          <el-descriptions-item label="学期">{{ currentItem.term || '当前学期' }}</el-descriptions-item>
          <el-descriptions-item label="类别">{{ currentItem.category || '其他' }}</el-descriptions-item>
          <el-descriptions-item label="截止日期">
            <span :class="{'text-red-500': isOverdue(currentItem), 'text-orange-500': isSoonDue(currentItem)}">
              {{ currentItem.dueDate || '无截止日期' }}
              <el-tag v-if="isOverdue(currentItem)" type="danger" size="small" effect="dark">已逾期</el-tag>
              <el-tag v-else-if="isSoonDue(currentItem)" type="warning" size="small" effect="dark">即将到期</el-tag>
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="描述">{{ currentItem.description || '无' }}</el-descriptions-item>
          <el-descriptions-item label="金额">
            <div v-if="currentItem?.originalAmount && currentItem?.originalAmount !== currentItem?.amount" class="flex items-center">
              <span class="line-through text-gray-400 mr-2">￥{{ currentItem.originalAmount }}</span>
              <el-tag size="small" type="danger">已减免 ￥{{ currentItem.discount || 0 }}</el-tag>
            </div>
            <div class="text-xl font-bold text-primary">￥{{ typeof currentItem.amount === 'number' ? currentItem.amount.toFixed(2) : parseFloat(currentItem.amount || 0).toFixed(2) }}</div>
          </el-descriptions-item>
          <el-descriptions-item v-if="currentItem.contacts" label="联系人">
            {{ currentItem.contacts }}
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="flex justify-end mt-4">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="payAndClose(currentItem.id)">
            <el-icon class="mr-1"><CreditCard /></el-icon>
            立即缴费
          </el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 批量缴费对话框 -->
    <el-dialog
      v-model="batchPayDialogVisible"
      title="批量缴费确认"
      width="500px"
      destroy-on-close
    >
      <div>
        <p class="mb-4">您选择了以下 {{ selectedItems.length }} 个项目进行缴费：</p>
        
        <el-table :data="selectedItems" style="width: 100%" border>
          <el-table-column prop="title" label="缴费项目" min-width="180" />
          <el-table-column prop="amount" label="金额" width="120">
            <template #default="{ row }">
              <span class="font-bold">¥{{ row.amount }}</span>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="flex justify-between items-center mt-4 p-3 bg-gray-50 rounded">
          <span class="font-bold">总计金额：</span>
          <span class="text-xl font-bold text-red-500">¥{{ selectedTotalAmount }}</span>
        </div>
        
        <div class="mt-4">
          <p class="font-bold mb-2">选择支付方式：</p>
          <el-radio-group v-model="paymentMethod">
            <el-radio label="alipay">支付宝</el-radio>
            <el-radio label="wechat">微信支付</el-radio>
            <el-radio label="bank">银行卡</el-radio>
          </el-radio-group>
        </div>
        
        <div class="flex justify-end mt-6">
          <el-button @click="batchPayDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmBatchPay">
            <el-icon class="mr-1"><CreditCard /></el-icon>
            确认支付
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import { Search, Document, View, CreditCard } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import mockPayData from '@/mock/pay'
import type { PaymentItemVO } from '@/api/pay'

// 支付数据
const payments = ref<PaymentItemVO[]>([])
const loading = ref(false)

// 加载支付项目列表（使用静态数据）
const loadPaymentItems = () => {
  loading.value = true
  try {
    // 使用模拟数据
    const mockResponse = mockPayData['/pay/items']
    
    // 处理模拟数据，确保所有必要字段都有值
    payments.value = mockResponse.list.map(item => ({
      ...item,
      // 为null的字段提供默认值
      title: item.title || item.category || `缴费项目${item.id}`,
      term: item.term || '当前学期',
      dueDate: item.dueDate ? new Date(item.dueDate).toLocaleDateString() : new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toLocaleDateString(),
      status: item.status === 'IN_PROGRESS' ? '未缴费' : item.status,
      // 确保金额字段是数字
      amount: typeof item.amount === 'number' ? item.amount : parseFloat(item.amount || '0'),
      originalAmount: item.originalAmount || item.amount,
      discount: item.discount || 0
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
    console.error('加载缴费项目失败:', error)
    ElMessage.error('加载缴费项目失败')
  } finally {
    loading.value = false
  }
}

// 初始化加载
onMounted(() => {
  loadPaymentItems()
})

// 详情对话框相关
const dialogVisible = ref(false)
const currentItem = ref(null)

// 批量缴费相关
const batchPayDialogVisible = ref(false)
const selectedItems = ref([])
const paymentMethod = ref('alipay')

// 显示详情对话框
const showDetails = (item) => {
  currentItem.value = item
  dialogVisible.value = true
}

// 缴费并关闭对话框（使用静态数据）
const payAndClose = (id: string) => {
  try {
    // 模拟创建订单响应
    const mockResponse = mockPayData['/pay/orders']
    
    // 模拟轮询订单状态
    setTimeout(() => {
      dialogVisible.value = false
      ElMessage.success('缴费成功')
      
      // 更新本地数据状态
      const index = payments.value.findIndex(item => item.id === id)
      if (index !== -1) {
        payments.value[index].status = '已缴费'
        payments.value[index].paidAt = new Date().toISOString()
        payments.value[index].updatedAt = new Date().toISOString()
      }
    }, 1000) // 模拟1秒后支付成功
  } catch (error) {
    console.error('缴费失败:', error)
    ElMessage.error('缴费失败，请重试')
  }
}

// 模拟轮询订单状态（使用静态数据）
const pollOrderStatus = (orderId: string) => {
  // 在使用静态数据的情况下，我们不需要实际轮询
  // 这个函数保留是为了保持代码结构一致性
  console.log('模拟轮询订单状态:', orderId)
  return Promise.resolve(true)
}

// 单个项目缴费
const payItem = (id: string) => {
  ElMessageBox.confirm('确认为该项目缴费吗？', '缴费确认', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'info'
  }).then(() => {
    payAndClose(id)
  }).catch(() => {})
}

// 处理表格选择变化
const handleSelectionChange = (selection) => {
  selectedItems.value = selection
}

// 批量缴费
const batchPay = () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请先选择需要缴费的项目')
    return
  }
  batchPayDialogVisible.value = true
}

// 确认批量缴费（使用静态数据）
const confirmBatchPay = () => {
  try {
    const itemIds = selectedItems.value.map(item => item.id)
    
    // 模拟创建批量订单
    setTimeout(() => {
      batchPayDialogVisible.value = false
      ElMessage.success('批量缴费成功')
      
      // 更新本地数据状态
      itemIds.forEach(id => {
        const index = payments.value.findIndex(item => item.id === id)
        if (index !== -1) {
          payments.value[index].status = '已缴费'
          payments.value[index].paidAt = new Date().toISOString()
          payments.value[index].updatedAt = new Date().toISOString()
        }
      })
    }, 1500) // 模拟1.5秒后支付成功
  } catch (error) {
    console.error('批量缴费失败:', error)
    ElMessage.error('批量缴费失败，请重试')
  }
}

// 筛选相关
const keyword = ref('')
const categoryFilter = ref('')
const termFilter = ref('')
const urgencyFilter = ref('')

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

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

// 判断是否逾期
const isOverdue = (item) => {
  if (!item || !item.dueDate) return false
  const today = new Date()
  const dueDate = new Date(item.dueDate)
  return dueDate < today
}

// 判断是否即将到期（7天内）
const isSoonDue = (item) => {
  if (!item || !item.dueDate) return false
  const today = new Date()
  const dueDate = new Date(item.dueDate)
  const diffTime = dueDate.getTime() - today.getTime()
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  return diffDays >= 0 && diffDays <= 7
}

// 筛选待缴费项目
const pendingItems = computed(() => payments.value)

// 应用筛选条件
const filteredItems = computed(() => {
  let result = pendingItems.value.filter(item => {
    try {
      // 关键词筛选
      if (keyword.value) {
        const title = (item.title || '').toLowerCase()
        const description = (item.description || '').toLowerCase()
        const searchTerm = keyword.value.toLowerCase()
        if (!title.includes(searchTerm) && !description.includes(searchTerm)) {
          return false
        }
      }
      
      // 类别筛选
      if (categoryFilter.value && item.category !== categoryFilter.value) {
        return false
      }
      
      // 学期筛选
      if (termFilter.value && item.term !== termFilter.value) {
        return false
      }
      
      // 紧急程度筛选
      if (urgencyFilter.value === 'overdue' && !isOverdue(item)) {
        return false
      }
      if (urgencyFilter.value === 'soon' && !isSoonDue(item)) {
        return false
      }
      
      return true
    } catch (error) {
      console.error('筛选项目时出错:', error, item)
      return false
    }
  })
  
  // 按截止日期升序排序
  result.sort((a, b) => {
    try {
      const dateA = a.dueDate ? new Date(a.dueDate) : new Date()
      const dateB = b.dueDate ? new Date(b.dueDate) : new Date()
      return dateA - dateB
    } catch (error) {
      console.error('排序时出错:', error)
      return 0
    }
  })
  
  return result
})

// 分页后的数据
const paginatedItems = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredItems.value.slice(start, end)
})

// 统计数据
const pendingCount = computed(() => pendingItems.value.length)

const overdueCount = computed(() => {
  return pendingItems.value.filter(item => isOverdue(item)).length
})

const totalPendingAmount = computed(() => {
  try {
    return pendingItems.value
      .reduce((sum, item) => {
        const amount = item && item.amount ? parseFloat(item.amount) : 0
        return isNaN(amount) ? sum : sum + amount
      }, 0)
      .toFixed(2)
  } catch (error) {
    console.error('计算总金额时出错:', error)
    return '0.00'
  }
})

const selectedTotalAmount = computed(() => {
  try {
    return selectedItems.value
      .reduce((sum, item) => {
        const amount = item && item.amount ? parseFloat(item.amount) : 0
        return isNaN(amount) ? sum : sum + amount
      }, 0)
      .toFixed(2)
  } catch (error) {
    console.error('计算选中项目总金额时出错:', error)
    return '0.00'
  }
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
.pending-payments {
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

/* 响应式调整 */
@media (max-width: 768px) {
  .filter-options {
    width: 100%;
    flex-direction: column;
    gap: 8px;
  }
  
  .filter-select {
    width: 100%;
  }
}
</style>