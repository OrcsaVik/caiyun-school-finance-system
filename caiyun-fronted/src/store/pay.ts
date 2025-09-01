import { defineStore } from 'pinia'
import { payApi } from '@/api/pay'
import type { PaymentItemVO, CreateOrderRequestVO, OrderStatusVO, PaymentStatisticsVO } from '@/api/pay'

// 用户信息，实际项目中应该从用户状态获取
const getUserId = () => {
  // 这里应该从用户状态或本地存储中获取用户ID
  return localStorage.getItem('userId') || '1' // 默认用户ID为1
}

export const usePayStore = defineStore('pay', {
  state: () => ({
    payments: [] as PaymentItemVO[],
    statistics: null as PaymentStatisticsVO | null,
    loading: false,
    error: null as string | null,
    currentPage: 1,
    pageSize: 10,
    total: 0,
    filters: {
      name: '',
      category: '',
      status: ''
    }
  }),
  actions: {
    // 获取缴费项目列表
    async fetchPaymentItems() {
      try {
        this.loading = true
        this.error = null
        
        const response = await payApi.getPaymentItems({
          name: this.filters.name || undefined,
          category: this.filters.category || undefined,
          status: this.filters.status || undefined,
          pageNum: this.currentPage,
          pageSize: this.pageSize
        })
        
        this.payments = response.data.list
        this.total = response.data.total
        return response
      } catch (error) {
        console.error('获取缴费项目失败:', error)
        this.error = '获取缴费项目失败，请稍后重试'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    // 获取缴费项目详情
    async fetchPaymentItemDetail(itemId: string) {
      try {
        this.loading = true
        this.error = null
        
        const response = await payApi.getPaymentItemDetail(itemId)
        return response
      } catch (error) {
        console.error('获取缴费项目详情失败:', error)
        this.error = '获取缴费项目详情失败，请稍后重试'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    // 创建支付订单
    async createOrder(itemId: string, paymentMethod: string = 'WECHAT', paymentScene: string = 'APP', paymentTerminal: string = 'MOBILE', remark?: string) {
      try {
        this.loading = true
        this.error = null
        
        const userId = getUserId()
        const request: CreateOrderRequestVO = {
          itemId,
          paymentMethod,
          paymentScene,
          paymentTerminal,
          remark
        }
        
        const response = await payApi.createOrder(userId, request)
        return response
      } catch (error) {
        console.error('创建支付订单失败:', error)
        this.error = '创建支付订单失败，请稍后重试'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    // 查询订单状态
    async getOrderStatus(orderId: string) {
      try {
        this.loading = true
        this.error = null
        
        const response = await payApi.getOrderStatus(orderId)
        return response
      } catch (error) {
        console.error('查询订单状态失败:', error)
        this.error = '查询订单状态失败，请稍后重试'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    // 获取支付统计信息
    async fetchPaymentStatistics() {
      try {
        this.loading = true
        this.error = null
        
        const userId = getUserId()
        const response = await payApi.getPaymentStatistics(userId)
        this.statistics = response.data
        return response
      } catch (error) {
        console.error('获取支付统计信息失败:', error)
        this.error = '获取支付统计信息失败，请稍后重试'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    // 申请退款
    async applyRefund(orderId: string, amount: number, reason: string, description?: string) {
      try {
        this.loading = true
        this.error = null
        
        const userId = getUserId()
        const response = await payApi.applyRefund(userId, { orderId, amount, reason, description })
        return response
      } catch (error) {
        console.error('申请退款失败:', error)
        this.error = '申请退款失败，请稍后重试'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    // 查询退款状态
    async getRefundStatus(refundId: string) {
      try {
        this.loading = true
        this.error = null
        
        const response = await payApi.getRefundStatus(refundId)
        return response
      } catch (error) {
        console.error('查询退款状态失败:', error)
        this.error = '查询退款状态失败，请稍后重试'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    // 设置筛选条件
    setFilters(filters: { name?: string; category?: string; status?: string }) {
      this.filters = { ...this.filters, ...filters }
      this.currentPage = 1 // 重置页码
      return this.fetchPaymentItems()
    },
    
    // 设置分页
    setPage(page: number) {
      this.currentPage = page
      return this.fetchPaymentItems()
    },
    
    // 设置每页数量
    setPageSize(size: number) {
      this.pageSize = size
      this.currentPage = 1 // 重置页码
      return this.fetchPaymentItems()
    },
    
    // 刷新数据
    refresh() {
      return this.fetchPaymentItems()
    },
    
    // 支付项目（实际应该调用创建订单接口）
    async markPaid(itemId: string) {
      try {
        // 创建订单
        const order = await this.createOrder(itemId)
        
        // 这里应该跳转到支付页面或调用支付SDK
        // 为了演示，我们假设支付成功，然后刷新列表
        await this.fetchPaymentItems()
        
        return order
      } catch (error) {
        console.error('支付失败:', error)
        this.error = '支付失败，请稍后重试'
        throw error
      }
    }
  }
})
