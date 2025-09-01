import { apiClient } from './client'

export interface PaymentItemVO {
  id: string
  title: string
  description: string
  amount: number
  originalAmount: number
  discount: number
  category: string
  term: string
  dueDate: string
  status: string
  paidAt?: string
  documentUrl?: string
  notices1?: string
  processes2?: string
  faqs3?: string
  contacts?: string
}

export interface PaymentItemQueryVO {
  name?: string
  category?: string
  status?: string
  pageNum?: number
  pageSize?: number
}

export interface PaymentItemPageVO {
  list: PaymentItemVO[]
  total: number
}


export interface PaymentItemDetailVO extends PaymentItemVO {}

export interface CreateOrderRequestVO {
  itemId: string
  paymentMethod: string
  paymentScene: string
  paymentTerminal: string
  remark?: string
}

export interface CreateOrderResponseVO {
  orderId: string
  amount: number
  paymentMethod: string
  paymentScene: string
  payParams: string
  status: string
  payTime: string
  tradeNo: string
}

export interface OrderStatusVO {
  orderId: string
  itemId: string
  itemName: string
  amount: number
  paymentMethod: string
  status: string
  payTime: string
  tradeNo: string
  createTime: string
  expireTime: string
  refundStatus?: string
  refundAmount?: number
  refundTime?: string
  refundNo?: string
}

export interface CategoryStatisticsVO {
  category: string
  count: number
  amount: number
  userCount: number
}

export interface MonthlyTrendVO {
  month: string
  count: number
  amount: number
  userCount: number
}

export interface PaymentStatisticsVO {
  pendingItemCount: number
  pendingAmount: number
  paidItemCount: number
  paidAmount: number
  refundItemCount: number
  refundAmount: number
  categoryStatistics: CategoryStatisticsVO[]
  monthlyTrend: MonthlyTrendVO[]
}

export interface PaymentHistoryQueryVO {
  startDate?: string
  endDate?: string
  pageNum?: number
  pageSize?: number
}

export interface PaymentHistoryPageVO {
  list: Array<{
    id: string
    itemId: string
    amount: number
    paymentMethod: string
    status: string
    payTime: string
    tradeNo: string
  }>
  total: number
  pageNum: number
  pageSize: number
}

export interface RefundRequestVO {
  orderId: string
  amount: number
  reason: string
  description?: string
}

export interface RefundResponseVO {
  id: string
  orderId: string
  amount: number
  status: string
  reason: string
  description?: string
  refundTime: string
  refundNo: string
}

export interface RefundItemVO {
  refundId: string
  paymentTitle: string
  amount: number
  reason: string
  applyDate: string
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'COMPLETED'
  processDate: string | null
  feedback: string | null
  refundDate: string | null
  refundMethod: string | null
  paymentId: string
  phone: string
}

export interface RefundListQueryVO {
  keyword?: string
  status?: string
  startDate?: string
  endDate?: string
  pageNo?: number
  pageSize?: number
}

export interface RefundListPageVO {
  list: RefundItemVO[]
  total: number
}

export interface RefundStatusVO {
  refundId: string
  orderId: string
  itemId: string
  itemName: string
  amount: number
  status: string
  reason: string
  description?: string
  refundNo: string
  refundTime: string
  createTime: string
  failReason?: string
  expectedArrivalTime?: string
}

export const payApi = {
  // 获取缴费项目列表
  getPaymentItems: (query: PaymentItemQueryVO) => {
    return apiClient.get<PaymentItemPageVO>('/pay/items', { params: query })
  },

  // 获取缴费项目详情
  getPaymentItemDetail: (itemId: string) => {
    return apiClient.get<PaymentItemDetailVO>(`/pay/items/${itemId}`)
  },

  // 创建支付订单
  createOrder: (userId: string, request: CreateOrderRequestVO) => {
    return apiClient.post<CreateOrderResponseVO>('/pay/orders', request, { params: { userId } })
  },

  // 查询支付状态
  getOrderStatus: (orderId: string) => {
    return apiClient.get<OrderStatusVO>(`/pay/orders/${orderId}/status`)
  },

  // 获取支付统计信息
  getPaymentStatistics: (userId: string) => {
    return apiClient.get<PaymentStatisticsVO>('/pay/statistics', { params: { userId } })
  },

  // 获取支付历史记录
  getPaymentHistory: (userId: string, query: PaymentHistoryQueryVO) => {
    return apiClient.get<PaymentHistoryPageVO>('/pay/history', { params: { userId, ...query } })
  },


  // 获取退费申请列表
  getRefundList: (userId: string, query: RefundListQueryVO) => {
    // 这个接口在后端 PayController 中没有对应的实现，需要确认是否需要
    return apiClient.get<RefundListPageVO>('/pay/refunds', { params: { userId, ...query } })
  },

  // 撤销退费申请
  // 这个接口在后端 PayController 中没有对应的实现，需要确认是否需要
  cancelRefund: (refundId: string) => {
    return apiClient.delete(`/pay/refunds/${refundId}`)
  },

  // 申请退款
  applyRefund: (userId: string, request: RefundRequestVO) => {
    return apiClient.post<RefundResponseVO>('/pay/refunds', request, { params: { userId } })
  },

  // 查询退款状态
  getRefundStatus: (refundId: string) => {
    return apiClient.get<RefundStatusVO>(`/pay/refunds/${refundId}/status`)
  }
}
export const {
  getPaymentItems,
  getPaymentItemDetail,
  createOrder,
  getOrderStatus,
  getPaymentStatistics,
  getPaymentHistory,
  getRefundList,
  cancelRefund,
  applyRefund,
  getRefundStatus
} = payApi