import type { PaymentItemVO, PaymentStatisticsVO, RefundItemVO, RefundListPageVO } from '@/api/pay';
const mockPayData = {
  '/pay/items': {
    list: [
      {
        id: '1',
        title: '2024年春季学费',
        description: '2024年春季学期学费缴纳',
        amount: 5000,
        category: '学费',
        term: '2024春季',
        dueDate: '2024-03-01',
        status: '未缴费' as const,
        createdAt: '2024-01-01',
        updatedAt: '2024-01-01'
      },
      {
        id: '2',
        title: '2024年春季住宿费',
        description: '2024年春季学期住宿费缴纳',
        amount: 1200,
        category: '住宿费',
        term: '2024春季',
        dueDate: '2024-03-01',
        status: '已缴费' as const,
        paidAt: '2024-01-15',
        createdAt: '2024-01-01',
        updatedAt: '2024-01-15'
      }
    ],
    total: 2
  },

  '/pay/items/1': {
    id: '1',
    title: '2024年春季学费',
    description: '2024年春季学期学费缴纳',
    amount: 5000,
    category: '学费',
    term: '2024春季',
    dueDate: '2024-03-01',
    status: '未缴费' as const,
    createdAt: '2024-01-01',
    updatedAt: '2024-01-01'
  },

  '/pay/orders': {
    orderId: 'mock-order-id',
    payUrl: 'https://example.com/pay/mock-order-id'
  },

  '/pay/orders/mock-order-id/status': {
    status: 'PENDING' as const
  },

  '/pay/statistics': {
    pendingCount: 5,
    completedCount: 10,
    totalPendingAmount: 8000,
    upcomingDeadlines: 2,
    overdueCount: 1,
    monthlyCompleted: 3,
    totalCompletedAmount: 15000,
    highestPendingAmount: 5000,
    averagePendingAmount: 1600
  },

  '/pay/history': {
    list: [
      {
        id: '2',
        title: '2024年春季住宿费',
        description: '2024年春季学期住宿费缴纳',
        amount: 1200,
        category: '住宿费',
        term: '2024春季',
        dueDate: '2024-03-01',
        status: '已缴费' as const,
        paidAt: '2024-01-15',
        createdAt: '2024-01-01',
        updatedAt: '2024-01-15'
      }
    ],
    total: 1
  },

  '/pay/refunds': {
    list: [
      {
        refundId: 'refund-001',
        paymentTitle: '2024年春季住宿费',
        amount: 1200,
        reason: '休学一学期',
        applyDate: '2024-02-15',
        status: 'PENDING',
        processDate: null,
        feedback: null,
        refundDate: null,
        refundMethod: null,
        paymentId: '2',
        phone: '13800138000'
      },
      {
        refundId: 'refund-002',
        paymentTitle: '2024年春季教材费',
        amount: 300,
        reason: '教材已有，不需要重复购买',
        applyDate: '2024-02-10',
        status: 'APPROVED',
        processDate: '2024-02-12',
        feedback: '申请已批准，将在3个工作日内退款',
        refundDate: null,
        refundMethod: '原路返回',
        paymentId: '3',
        phone: '13800138000'
      },
      {
        refundId: 'refund-003',
        paymentTitle: '2024年春季实验室费',
        amount: 500,
        reason: '已转专业，不再需要此实验室',
        applyDate: '2024-01-20',
        status: 'REJECTED',
        processDate: '2024-01-25',
        feedback: '根据学校规定，开学后两周内不可退实验室费用',
        refundDate: null,
        refundMethod: null,
        paymentId: '4',
        phone: '13800138000'
      },
      {
        refundId: 'refund-004',
        paymentTitle: '2024年春季校车费',
        amount: 800,
        reason: '已更换住宿地点，不需要校车',
        applyDate: '2024-01-15',
        status: 'COMPLETED',
        processDate: '2024-01-18',
        feedback: '申请已批准',
        refundDate: '2024-01-20',
        refundMethod: '支付宝',
        paymentId: '5',
        phone: '13800138000'
      }
    ],
    total: 4
  },

  '/pay/refunds/mock-refund-id/status': {
    status: 'PENDING' as const
  },
  
  '/pay/refunds/refund-001/status': {
    refundId: 'refund-001',
    orderId: 'order-001',
    itemId: '2',
    itemName: '2024年春季住宿费',
    amount: 1200,
    status: 'PENDING',
    reason: '休学一学期',
    refundNo: '',
    refundTime: '',
    createTime: '2024-02-15',
    expectedArrivalTime: '2024-02-25'
  }
}

export default mockPayData