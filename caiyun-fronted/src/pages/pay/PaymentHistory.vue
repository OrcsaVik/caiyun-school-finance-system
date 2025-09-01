<template>
  <div class="history-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>缴费记录</h1>
      <p>查看和管理您的所有缴费记录</p>
    </div>

    <!-- 筛选和导出区域 -->
    <div class="filter-section">
      <div class="filter-group">
        <el-input
          v-model="filters.keyword"
          placeholder="搜索项目名称"
          prefix-icon="Search"
          clearable
          @input="handleSearch"
        />
        <el-select v-model="filters.term" placeholder="选择学期" clearable @change="handleSearch">
          <el-option
            v-for="term in terms"
            :key="term.value"
            :label="term.label"
            :value="term.value"
          />
        </el-select>
        <el-select v-model="filters.category" placeholder="选择类别" clearable @change="handleSearch">
          <el-option
            v-for="category in categories"
            :key="category.value"
            :label="category.label"
            :value="category.value"
          />
        </el-select>
        <el-select v-model="filters.status" placeholder="支付状态" clearable @change="handleSearch">
          <el-option
            v-for="status in paymentStatuses"
            :key="status.value"
            :label="status.label"
            :value="status.value"
          />
        </el-select>
        <el-date-picker
          v-model="filters.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :shortcuts="dateShortcuts"
          @change="handleSearch"
        />
      </div>
      <div class="action-group">
        <el-button type="primary" @click="exportRecords">
          <el-icon><Download /></el-icon> 导出记录
        </el-button>
        <el-button @click="printRecords">
          <el-icon><Printer /></el-icon> 打印记录
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-section">
      <div class="stat-card">
        <el-icon><Money /></el-icon>
        <div class="stat-info">
          <span class="stat-label">总支付金额</span>
          <span class="stat-value">¥{{ totalAmount.toFixed(2) }}</span>
        </div>
      </div>
      <div class="stat-card">
        <el-icon><Document /></el-icon>
        <div class="stat-info">
          <span class="stat-label">总记录数</span>
          <span class="stat-value">{{ totalRecords }}</span>
        </div>
      </div>
      <div class="stat-card">
        <el-icon><CircleCheck /></el-icon>
        <div class="stat-info">
          <span class="stat-label">成功支付</span>
          <span class="stat-value">{{ successfulPayments }}</span>
        </div>
      </div>
    </div>

    <!-- 记录表格 -->
    <el-table
      v-loading="loading"
      :data="filteredRecords"
      style="width: 100%"
      :default-sort="{ prop: 'paymentDate', order: 'descending' }"
      @sort-change="handleSortChange"
    >
      <el-table-column prop="projectName" label="项目名称" min-width="200" sortable>
        <template #default="{ row }">
          <div class="project-cell">
            <span class="project-name">{{ row.projectName }}</span>
            <el-tag
              size="small"
              :type="row.status === 'success' ? 'success' : row.status === 'pending' ? 'warning' : 'danger'"
            >
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="类别" width="120">
        <template #default="{ row }">
          <el-tag size="small" :type="getCategoryType(row.category)">
            {{ row.category }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="term" label="学期" width="120" />
      <el-table-column prop="amount" label="金额" width="120" sortable>
        <template #default="{ row }">
          <span class="amount">¥{{ row.amount.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="paymentDate" label="支付时间" width="180" sortable>
        <template #default="{ row }">
          {{ formatDate(row.paymentDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="paymentMethod" label="支付方式" width="120">
        <template #default="{ row }">
          <el-tag size="small" effect="plain">
            {{ row.paymentMethod }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button
            link
            type="primary"
            size="small"
            @click="viewDetails(row)"
          >
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="totalRecords"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 详情抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      title="支付详情"
      size="30%"
      :destroy-on-close="true"
    >
      <template v-if="selectedRecord">
        <div class="payment-detail">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="项目名称">{{ selectedRecord.projectName }}</el-descriptions-item>
            <el-descriptions-item label="支付状态">
              <el-tag :type="selectedRecord.status === 'success' ? 'success' : 'danger'">
                {{ getStatusLabel(selectedRecord.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="支付金额">¥{{ selectedRecord.amount.toFixed(2) }}</el-descriptions-item>
            <el-descriptions-item label="支付时间">{{ formatDate(selectedRecord.paymentDate) }}</el-descriptions-item>
            <el-descriptions-item label="支付方式">{{ selectedRecord.paymentMethod }}</el-descriptions-item>
            <el-descriptions-item label="交易流水号">{{ selectedRecord.transactionId }}</el-descriptions-item>
            <el-descriptions-item label="备注">{{ selectedRecord.remarks || '无' }}</el-descriptions-item>
          </el-descriptions>

          <div class="detail-actions">
            <el-button type="primary" @click="downloadReceipt(selectedRecord)">
              <el-icon><Download /></el-icon> 下载收据
            </el-button>
            <el-button @click="printReceipt(selectedRecord)">
              <el-icon><Printer /></el-icon> 打印收据
            </el-button>
          </div>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Search, Money, Document, CircleCheck, Download, Printer } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

// 筛选选项
const terms = [
  { value: '2023-2024-1', label: '2023-2024学年第一学期' },
  { value: '2023-2024-2', label: '2023-2024学年第二学期' },
  { value: '2022-2023-2', label: '2022-2023学年第二学期' },
  { value: '2022-2023-1', label: '2022-2023学年第一学期' }
]

const categories = [
  { value: 'tuition', label: '学费' },
  { value: 'accommodation', label: '住宿费' },
  { value: 'insurance', label: '保险费' },
  { value: 'other', label: '其他费用' }
]

const paymentStatuses = [
  { value: 'success', label: '支付成功' },
  { value: 'pending', label: '处理中' },
  { value: 'failed', label: '支付失败' }
]

const dateShortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    }
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    }
  }
]

// 筛选条件
const filters = ref({
  keyword: '',
  term: '',
  category: '',
  status: '',
  dateRange: null
})

// 模拟支付记录数据
const paymentRecords = ref([
  {
    id: 1,
    projectName: '2023-2024学年第一学期学费',
    category: '学费',
    term: '2023-2024-1',
    amount: 5000.00,
    paymentDate: '2023-09-01 10:30:00',
    paymentMethod: '支付宝',
    status: 'success',
    transactionId: 'TX202309011030001',
    remarks: '按时缴费'
  },
  {
    id: 2,
    projectName: '2023-2024学年住宿费',
    category: '住宿费',
    term: '2023-2024-1',
    amount: 1200.00,
    paymentDate: '2023-09-01 10:35:00',
    paymentMethod: '微信支付',
    status: 'success',
    transactionId: 'TX202309011035001',
    remarks: ''
  },
  {
    id: 3,
    projectName: '学生保险费',
    category: '保险费',
    term: '2023-2024-1',
    amount: 100.00,
    paymentDate: '2023-09-02 14:20:00',
    paymentMethod: '银行卡',
    status: 'success',
    transactionId: 'TX202309021420001',
    remarks: '必缴项目'
  }
])

// 表格加载状态
const loading = ref(false)

// 分页
const currentPage = ref(1)
const pageSize = ref(10)

// 详情抽屉
const drawerVisible = ref(false)
const selectedRecord = ref(null)

// 计算属性
const filteredRecords = computed(() => {
  return paymentRecords.value.filter(record => {
    const matchesKeyword = record.projectName.toLowerCase().includes(filters.value.keyword.toLowerCase())
    const matchesTerm = !filters.value.term || record.term === filters.value.term
    const matchesCategory = !filters.value.category || record.category === filters.value.category
    const matchesStatus = !filters.value.status || record.status === filters.value.status
    const matchesDate = !filters.value.dateRange ||
      (dayjs(record.paymentDate).isAfter(filters.value.dateRange[0]) &&
       dayjs(record.paymentDate).isBefore(filters.value.dateRange[1]))
    
    return matchesKeyword && matchesTerm && matchesCategory && matchesStatus && matchesDate
  })
})

const totalAmount = computed(() => {
  return filteredRecords.value.reduce((sum, record) => sum + record.amount, 0)
})

const totalRecords = computed(() => filteredRecords.value.length)

const successfulPayments = computed(() => {
  return filteredRecords.value.filter(record => record.status === 'success').length
})

// 方法
const handleSearch = () => {
  currentPage.value = 1
}

const handleSortChange = ({ prop, order }) => {
  // 实现排序逻辑
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
}

const viewDetails = (record) => {
  selectedRecord.value = record
  drawerVisible.value = true
}

const exportRecords = async () => {
  loading.value = true
  try {
    // 模拟导出延迟
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('记录导出成功')
  } catch (error) {
    ElMessage.error('导出失败，请重试')
  } finally {
    loading.value = false
  }
}

const printRecords = () => {
  window.print()
}

const downloadReceipt = async (record) => {
  loading.value = true
  try {
    // 模拟下载延迟
    await new Promise(resolve => setTimeout(resolve, 800))
    ElMessage.success('收据下载成功')
  } catch (error) {
    ElMessage.error('下载失败，请重试')
  } finally {
    loading.value = false
  }
}

const printReceipt = (record) => {
  window.print()
}

const getStatusLabel = (status: string) => {
  const statusMap = {
    success: '支付成功',
    pending: '处理中',
    failed: '支付失败'
  }
  return statusMap[status] || status
}

const getCategoryType = (category: string) => {
  const typeMap = {
    '学费': '',
    '住宿费': 'success',
    '保险费': 'warning',
    '其他费用': 'info'
  }
  return typeMap[category] || ''
}

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}
</script>

<style scoped>
.history-container {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  animation: fadeInDown 0.6s ease-out;
  position: relative;
}

.page-header::after {
  content: '';
  position: absolute;
  bottom: -20px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 4px;
  background: linear-gradient(90deg, #2196F3, #4CAF50);
  border-radius: 2px;
}

.page-header h1 {
  font-size: 2.5rem;
  font-weight: 600;
  background: linear-gradient(120deg, #2196F3, #4CAF50);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 8px;
}

.page-header p {
  color: #666;
  font-size: 1.1rem;
}

.filter-section {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  backdrop-filter: blur(10px);
  animation: fadeIn 0.6s ease-out 0.2s both;
}

.filter-group {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.action-group {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.stats-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
  animation: fadeIn 0.6s ease-out 0.4s both;
}

.stat-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.12);
}

.stat-card .el-icon {
  font-size: 24px;
  color: var(--el-color-primary);
  background: rgba(var(--el-color-primary-rgb), 0.1);
  padding: 12px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-card:hover .el-icon {
  transform: scale(1.1);
}

.stat-info {
  flex: 1;
}

.stat-label {
  display: block;
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 4px;
}

.stat-value {
  display: block;
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
}

.el-table {
  border-radius: 16px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(10px);
  margin-bottom: 24px;
  animation: fadeIn 0.6s ease-out 0.6s both;
}

.project-cell {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.project-name {
  flex: 1;
}

.amount {
  font-family: monospace;
  font-weight: 600;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  animation: fadeIn 0.6s ease-out 0.8s both;
}

.payment-detail {
  padding: 24px;
}

.detail-actions {
  margin-top: 24px;
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media print {
  .filter-section,
  .action-group,
  .el-pagination,
  .el-button {
    display: none !important;
  }

  .history-container {
    padding: 0;
    background: none;
  }

  .el-table {
    box-shadow: none !important;
  }
}
</style>