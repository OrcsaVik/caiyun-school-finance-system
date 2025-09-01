<template>
  <div class="refund-status">
    <div class="page-header">
      <h1>退费情况</h1>
      <p>查看您的退费申请和处理状态</p>
    </div>
    
    <!-- 统计信息卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
      <div class="stat-card">
        <div class="text-2xl font-bold text-blue-500 mb-2">{{ pendingCount }}</div>
        <div class="text-gray-600">待处理申请</div>
      </div>
      <div class="stat-card">
        <div class="text-2xl font-bold text-green-500 mb-2">{{ approvedCount }}</div>
        <div class="text-gray-600">已批准申请</div>
      </div>
      <div class="stat-card">
        <div class="text-2xl font-bold text-red-500 mb-2">¥{{ totalRefundAmount }}</div>
        <div class="text-gray-600">退费总金额</div>
      </div>
    </div>
    
    <!-- 筛选和搜索区域 -->
    <div class="filter-card mb-6">
      <div class="flex flex-wrap gap-4 items-center">
        <div class="search-box flex-1 min-w-[200px]">
          <el-input
            v-model="keyword"
            placeholder="搜索退费申请"
            prefix-icon="Search"
            clearable
          />
        </div>
        <div class="filter-options flex gap-3">
          <el-select v-model="statusFilter" placeholder="状态" clearable class="filter-select">
            <el-option label="全部" value="" />
            <el-option label="待审核" value="pending" />
            <el-option label="已批准" value="approved" />
            <el-option label="已拒绝" value="rejected" />
            <el-option label="已退款" value="refunded" />
          </el-select>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            class="date-range"
          />
        </div>
      </div>
    </div>

    <!-- 退费申请表格 -->
    <el-card class="mb-6">
      <template #header>
        <div class="flex justify-between items-center">
          <span class="font-bold">退费申请列表</span>
          <el-button type="primary" @click="showNewRefundDialog">
            <el-icon class="mr-1"><Plus /></el-icon>
            新建退费申请
          </el-button>
        </div>
      </template>
      
      <el-table
        :data="paginatedRefunds"
        style="width: 100%"
        :empty-text="'暂无退费申请'"
        stripe
        border
      >
        <el-table-column prop="title" label="退费项目" min-width="180">
          <template #default="{ row }">
            <div class="flex items-center">
              <el-icon class="mr-2"><Document /></el-icon>
              <span class="cursor-pointer text-blue-500 hover:underline" @click="showDetails(row)">
                {{ row.title }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="退费金额" width="120">
          <template #default="{ row }">
            <span class="font-bold">¥{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="applyDate" label="申请日期" width="120" />
        <el-table-column prop="reason" label="退费原因" min-width="150">
          <template #default="{ row }">
            <el-tooltip
              :content="row.reason"
              placement="top"
              :show-after="500"
              :hide-after="0"
            >
              <div class="truncate max-w-[200px]">{{ row.reason }}</div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="refundStatusType(row.status)" effect="plain">{{ row.statusText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="processDate" label="处理日期" width="120">
          <template #default="{ row }">
            {{ row.processDate || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="showDetails(row)">
              <el-icon><View /></el-icon> 详情
            </el-button>
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
          :total="filteredRefunds.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="退费申请详情"
      width="500px"
      destroy-on-close
    >
      <div v-if="currentRefund">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-xl font-bold">{{ currentRefund.title }}</h3>
          <el-tag :type="refundStatusType(currentRefund.status)" effect="dark">{{ currentRefund.statusText }}</el-tag>
        </div>
        
        <el-descriptions :column="1" border>
          <el-descriptions-item label="申请日期">{{ currentRefund.applyDate }}</el-descriptions-item>
          <el-descriptions-item label="退费金额">
            <span class="text-xl font-bold text-red-500">¥{{ currentRefund.amount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="退费原因">{{ currentRefund.reason }}</el-descriptions-item>
          <el-descriptions-item label="申请状态">{{ currentRefund.statusText }}</el-descriptions-item>
          <el-descriptions-item v-if="currentRefund.processDate" label="处理日期">
            {{ currentRefund.processDate }}
          </el-descriptions-item>
          <el-descriptions-item v-if="currentRefund.feedback" label="审核反馈">
            {{ currentRefund.feedback }}
          </el-descriptions-item>
          <el-descriptions-item v-if="currentRefund.refundDate" label="退款日期">
            {{ currentRefund.refundDate }}
          </el-descriptions-item>
          <el-descriptions-item v-if="currentRefund.refundMethod" label="退款方式">
            {{ currentRefund.refundMethod }}
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="flex justify-end mt-4">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button 
            v-if="currentRefund.status === 'pending'"
            type="danger" 
            @click="cancelRefund(currentRefund.id)"
          >
            <el-icon class="mr-1"><Close /></el-icon>
            撤销申请
          </el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 新建退费申请对话框 -->
    <el-dialog
      v-model="newRefundDialogVisible"
      title="新建退费申请"
      width="500px"
      destroy-on-close
    >
      <el-form :model="refundForm" label-width="100px" :rules="refundRules" ref="refundFormRef">
        <el-form-item label="缴费项目" prop="paymentId">
          <el-select v-model="refundForm.paymentId" placeholder="选择已缴费项目" class="w-full">
            <el-option 
              v-for="item in paidPayments" 
              :key="item.id" 
              :label="item.title" 
              :value="item.id"
            >
              <div class="flex justify-between items-center">
                <span>{{ item.title }}</span>
                <span class="text-red-500">¥{{ item.amount }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="退费金额" prop="amount">
          <el-input-number 
            v-model="refundForm.amount" 
            :min="0" 
            :max="selectedPaymentAmount" 
            :precision="2" 
            :step="10"
            class="w-full"
          />
        </el-form-item>
        <el-form-item label="退费原因" prop="reason">
          <el-input 
            v-model="refundForm.reason" 
            type="textarea" 
            :rows="3" 
            placeholder="请详细说明退费原因"
          />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="refundForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
      </el-form>
      
      <div class="flex justify-end mt-4">
        <el-button @click="newRefundDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRefundApplication">
          <el-icon class="mr-1"><Check /></el-icon>
          提交申请
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, reactive, onMounted, watch } from 'vue'
import { Search, Document, View, Plus, Close, Check } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
// 移除实际API导入，仅保留类型导入
import type { PaymentItemVO, RefundRequestVO, RefundResponseVO, RefundStatusVO, RefundItemVO, RefundListQueryVO } from '@/api/pay'
import mockPayData from '@/mock/pay'
// 引入用于精确数值计算的工具函数
const preciseAdd = (num1: number, num2: number): number => {
  const precision = Math.pow(10, 10)
  return Math.round((num1 + num2) * precision) / precision
}

const preciseMultiply = (num1: number, num2: number): number => {
  const precision = Math.pow(10, 10)
  return Math.round((num1 * num2) * precision) / precision
}

// 筛选相关
const keyword = ref('')
const statusFilter = ref('')
const dateRange = ref([])
interface Refund {
  id: string
  title: string
  amount: number
  reason: string
  applyDate: string
  status: string
  statusText: string
  processDate: string | null
  feedback: string | null
  refundDate: string | null
  refundMethod: string | null
  paymentId: string | number
  phone: string
}

const refunds = ref<Refund[]>([])
const paidPayments = ref<PaymentItemVO[]>([])

// 加载已缴费项目
const loadPaidPayments = async () => {
  try {
    // 验证模拟数据是否存在且格式正确
    if (!mockPayData || !mockPayData['/pay/items'] || !Array.isArray(mockPayData['/pay/items'].list)) {
      throw new Error('模拟数据格式不正确')
    }
    
    // 使用模拟数据替代API调用
    const mockResponse = {
      list: mockPayData['/pay/items'].list.filter(item => item.status === '已缴费')
    }
    
    // 转换并验证每个缴费项目
    paidPayments.value = mockResponse.list.map(item => {
      // 验证必要字段
      if (!item.id || !item.title) {
        console.warn('缴费项目数据不完整:', item)
      }
      
      // 确保金额是数字类型
      const amount = typeof item.amount === 'number' ? item.amount : Number(item.amount || 0)
      
      return {
        ...item,
        id: item.id || `unknown-${Date.now()}`,
        title: item.title || '未知项目',
        amount: amount,
        originalAmount: amount,
        discount: 0
      }
    })
    
    console.log('已加载模拟缴费项目数据:', paidPayments.value)
  } catch (error) {
    console.error('加载已缴费项目失败:', error)
    ElMessage.error(`加载已缴费项目失败: ${error instanceof Error ? error.message : '未知错误'}`)
    // 确保即使出错也有一个空数组
    paidPayments.value = []
  }
}

// 加载退费申请列表
const loadRefunds = async () => {
  try {
    // 构建查询参数，但实际上只用于日志记录，因为我们使用的是模拟数据
    const query: RefundListQueryVO = {
      keyword: keyword.value,
      status: statusFilter.value,
      startDate: dateRange.value?.[0],
      endDate: dateRange.value?.[1],
      pageNo: currentPage.value,
      pageSize: pageSize.value
    }
    
    // 使用模拟数据替代API调用
    const mockResponse = mockPayData['/pay/refunds']
    
    // 验证模拟数据是否存在且格式正确
    if (!mockResponse || !Array.isArray(mockResponse.list)) {
      throw new Error('模拟数据格式不正确')
    }
    
    // 转换并验证每个退费项目
    refunds.value = mockResponse.list.map(item => {
      // 验证必要字段
      if (!item.refundId || !item.paymentTitle) {
        console.warn('退费项目数据不完整:', item)
      }
      
      // 确保金额是数字类型
      const amount = typeof item.amount === 'number' ? item.amount : Number(item.amount || 0)
      
      return {
        id: item.refundId || `unknown-${Date.now()}`,
        title: item.paymentTitle || '未知项目',
        amount: amount,
        reason: item.reason || '',
        applyDate: item.applyDate || new Date().toISOString().split('T')[0],
        status: (item.status || 'PENDING').toLowerCase(),
        statusText: getStatusText(item.status || 'PENDING'),
        processDate: item.processDate || null,
        feedback: item.feedback || null,
        refundDate: item.refundDate || null,
        refundMethod: item.refundMethod || null,
        paymentId: item.paymentId || '',
        phone: item.phone || ''
      }
    })
    
    console.log('已加载模拟退费申请数据:', refunds.value)
  } catch (error) {
    console.error('加载退费申请列表失败:', error)
    ElMessage.error(`加载退费申请列表失败: ${error instanceof Error ? error.message : '未知错误'}`)
    // 确保即使出错也有一个空数组
    refunds.value = []
  }
}

onMounted(() => {
  loadPaidPayments()
  loadRefunds()
})



// 监听筛选条件变化
watch([keyword, statusFilter, dateRange], () => {
  currentPage.value = 1
})

// 详情对话框相关
const dialogVisible = ref(false)
const currentRefund = ref<Refund | null>(null)

// 新建退费申请相关
const newRefundDialogVisible = ref(false)
const refundFormRef = ref<FormInstance>()
const refundForm = reactive({
  paymentId: '',
  amount: 0,
  reason: '',
  phone: ''
})

const refundRules = reactive<FormRules>({
  paymentId: [
    { required: true, message: '请选择缴费项目', trigger: 'change' }
  ],
  amount: [
    { required: true, message: '请输入退费金额', trigger: 'blur' },
    { type: 'number', min: 1, message: '金额必须大于0', trigger: 'blur' }
  ],
  reason: [
    { required: true, message: '请输入退费原因', trigger: 'blur' },
    { min: 5, max: 200, message: '退费原因长度应在5到200个字符之间', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
})

// 显示详情对话框
const viewDetail = (row: Refund) => {
  try {
    // 验证行数据是否完整
    if (!row || !row.id) {
      ElMessage.error('退费申请数据不完整')
      return
    }
    
    // 深拷贝数据，避免直接修改原始数据
    currentRefund.value = JSON.parse(JSON.stringify(row))
    dialogVisible.value = true
  } catch (error) {
    console.error('查看详情失败:', error)
    ElMessage.error('查看详情失败，请稍后重试')
  }
}

// 显示新建退费申请对话框
const showNewRefundDialog = () => {
  if (paidPayments.value.length === 0) {
    ElMessage.warning('暂无可退费的缴费项目')
    return
  }
  // 重置表单
  refundForm.paymentId = ''
  refundForm.amount = 0
  refundForm.reason = ''
  refundForm.phone = ''
  
  // 延迟打开对话框，确保表单重置完成
  setTimeout(() => {
    if (refundFormRef.value) {
      refundFormRef.value.resetFields()
    }
    newRefundDialogVisible.value = true
  }, 0)
}

// 撤销退费申请
const cancelRefund = async (id: string) => {
  // 验证ID是否存在
  if (!id) {
    ElMessage.error('退费申请ID不能为空')
    return
  }
  
  // 验证该申请是否存在
  const refundToCancel = refunds.value.find(item => item.id === id)
  if (!refundToCancel) {
    ElMessage.error('未找到对应的退费申请')
    return
  }
  
  // 验证申请状态是否为待审核
  if (refundToCancel.status !== 'pending') {
    ElMessage.error('只能撤销待审核状态的申请')
    return
  }
  
  try {
    // 确认对话框
    await ElMessageBox.confirm('确认撤销此退费申请吗？', '撤销确认', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 使用模拟数据替代API调用
    console.log('模拟撤销退费申请:', id)
    
    // 从本地数据中移除该申请
    refunds.value = refunds.value.filter(item => item.id !== id)
    
    ElMessage.success('退费申请已撤销')
    dialogVisible.value = false
  } catch (error) {
    // 用户取消操作不显示错误
    if (error !== 'cancel') {
      console.error('撤销退费申请失败:', error)
      ElMessage.error(`撤销退费申请失败: ${error instanceof Error ? error.message : '未知错误'}`)
    }
  }
}

// 提交退费申请
const submitRefundApplication = async () => {
  if (!refundFormRef.value) return
  
  try {
    // 表单验证
    const valid = await refundFormRef.value.validate()
    if (!valid) return false

    // 验证选中的缴费项目
    const selectedPayment = paidPayments.value.find(item => item.id === refundForm.paymentId)
    if (!selectedPayment) {
      ElMessage.error('所选缴费项目不存在')
      return
    }
    
    // 验证退费金额
    if (refundForm.amount <= 0) {
      ElMessage.error('退费金额必须大于0')
      return
    }
    
    // 验证退费金额不能超过原始金额
    const paymentAmount = typeof selectedPayment.amount === 'number' ? 
      selectedPayment.amount : Number(selectedPayment.amount || 0)
    if (refundForm.amount > paymentAmount) {
      ElMessage.error(`退费金额不能超过原始金额 ${paymentAmount}`)
      return
    }

    // 构建退费请求对象
    const request: RefundRequestVO = {
      orderId: selectedPayment.id,
      reason: refundForm.reason.trim(), // 去除首尾空格
      amount: refundForm.amount
    }

    // 使用模拟数据替代API调用
    console.log('模拟提交退费申请:', request)
    
    // 创建新的退费申请并添加到本地数据中
    const newRefund: Refund = {
      id: `refund-${Date.now()}`,
      title: selectedPayment.title,
      amount: refundForm.amount,
      reason: refundForm.reason.trim(),
      applyDate: new Date().toISOString().split('T')[0],
      status: 'pending',
      statusText: '待审核',
      processDate: null,
      feedback: null,
      refundDate: null,
      refundMethod: null,
      paymentId: selectedPayment.id,
      phone: refundForm.phone.trim()
    }
    
    // 添加到本地数据
    refunds.value.unshift(newRefund)
    
    ElMessage.success('退费申请已提交，请等待审核')
    newRefundDialogVisible.value = false
    
    // 重置表单
    resetRefundForm()
  } catch (error) {
    console.error('提交退费申请失败:', error)
    ElMessage.error(`提交失败: ${error instanceof Error ? error.message : '请稍后重试'}`)
  }
}

// 重置退费申请表单
const resetRefundForm = () => {
  refundForm.paymentId = ''
  refundForm.amount = 0
  refundForm.reason = ''
  refundForm.phone = ''
  if (refundFormRef.value) {
    refundFormRef.value.resetFields()
  }
}



// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)

const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
}

// 获取状态文本
const getStatusText = (status: string) => {
  switch (status) {
    case 'PENDING': return '待审核'
    case 'APPROVED': return '已批准'
    case 'REJECTED': return '已拒绝'
    case 'COMPLETED': return '已退款'
    default: return '未知状态'
  }
}

// 选中的缴费项目金额
const selectedPaymentAmount = computed(() => {
  const selectedPayment = paidPayments.value.find(item => item.id === refundForm.paymentId)
  // 确保返回数值类型，并处理可能的类型转换
  return selectedPayment ? (typeof selectedPayment.amount === 'number' ? selectedPayment.amount : Number(selectedPayment.amount)) : 0
})

// 应用筛选条件
const filteredRefunds = computed(() => {
  let result = refunds.value.filter(item => {
    try {
      // 关键词筛选
      if (keyword.value) {
        const itemTitle = (item.title || '').toLowerCase()
        const itemReason = (item.reason || '').toLowerCase()
        const searchKeyword = keyword.value.toLowerCase()
        
        if (!itemTitle.includes(searchKeyword) && !itemReason.includes(searchKeyword)) {
          return false
        }
      }
      
      // 状态筛选
      if (statusFilter.value && item.status !== statusFilter.value) {
        return false
      }
      
      // 日期范围筛选
      if (dateRange.value && dateRange.value.length === 2) {
        const applyDate = new Date(item.applyDate)
        const startDate = new Date(dateRange.value[0])
        const endDate = new Date(dateRange.value[1])
        endDate.setHours(23, 59, 59, 999) // 设置为当天结束时间
        
        // 使用时间戳比较日期，避免直接相减可能导致的问题
        if (applyDate.getTime() < startDate.getTime() || applyDate.getTime() > endDate.getTime()) {
          return false
        }
      }
      
      return true
    } catch (error) {
      console.error('筛选数据时出错:', error, item)
      return false
    }
  })
  
  // 按申请日期降序排序，使用精确计算进行比较
  result.sort((a, b) => {
    try {
      return new Date(b.applyDate).getTime() - new Date(a.applyDate).getTime()
    } catch (error) {
      console.error('排序数据时出错:', error)
      return 0
    }
  })
  
  return result
})

// 分页后的数据
const paginatedRefunds = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredRefunds.value.slice(start, end)
})

// 待审核数量
const pendingCount = computed(() => {
  try {
    return filteredRefunds.value.filter(item => item && item.status === 'pending').length
  } catch (error) {
    console.error('计算待审核数量时出错:', error)
    return 0
  }
})

// 已批准数量
const approvedCount = computed(() => {
  try {
    return filteredRefunds.value.filter(item => {
      return item && (item.status === 'approved' || item.status === 'refunded')
    }).length
  } catch (error) {
    console.error('计算已批准数量时出错:', error)
    return 0
  }
})

const totalRefundAmount = computed(() => {
  try {
    // 使用精确计算函数处理浮点数加法
    const total = refunds.value
      .filter(item => item.status === 'approved' || item.status === 'refunded')
      .reduce((sum, item) => {
        try {
          // 确保金额是数字类型
          const amount = typeof item.amount === 'number' ? item.amount : Number(item.amount || 0)
          if (isNaN(amount)) {
            console.warn('退款金额无效:', item)
            return sum
          }
          return preciseAdd(sum, amount)
        } catch (error) {
          console.error('计算退款金额时出错:', error, item)
          return sum
        }
      }, 0)
    
    // 格式化为两位小数的字符串
    return total.toFixed(2)
  } catch (error) {
    console.error('计算总退款金额时出错:', error)
    return '0.00'
  }
})

const refundStatusType = (status: string) => {
  if (status === 'approved') return 'success'
  if (status === 'pending') return 'warning'
  if (status === 'rejected') return 'danger'
  if (status === 'refunded') return 'info'
  return 'info'
}
</script>

<style scoped>
.refund-status {
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

.date-range {
  width: 260px;
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
  
  .filter-select,
  .date-range {
    width: 100%;
  }
}
</style>