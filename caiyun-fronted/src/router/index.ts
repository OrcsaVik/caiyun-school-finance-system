import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/pages/dashboard/Dashboard.vue'
import Login from '@/pages/auth/Login.vue'
import PaymentLayout from '@/layouts/PaymentLayout.vue'

// 支付系统子组件
import PayDashboard from '@/pages/pay/dashboard/Dashboard.vue'
import PendingPayments from '@/pages/pay/pending/PendingPayments.vue'
import PaymentRecords from '@/pages/pay/records/PaymentRecords.vue'
import RefundStatus from '@/pages/pay/refund/RefundStatus.vue'
import UserProfile from '@/pages/pay/profile/UserProfile.vue'

const AppIndex = () => import('@/pages/apps/AppIndex.vue')

const routes = [
  { path: '/', name: 'Dashboard', component: Dashboard },
  {
    path: '/pay',
    component: PaymentLayout,
    children: [
      { path: '', redirect: '/pay/dashboard' },
      { path: 'dashboard', name: 'PayDashboard', component: PayDashboard },
      { path: 'unpaid', name: 'PendingPayments', component: PendingPayments },
      { path: 'history', name: 'PaymentRecords', component: PaymentRecords },
      { path: 'refund', name: 'RefundStatus', component: RefundStatus },
      { path: 'profile', name: 'UserProfile', component: UserProfile },
    ]
  },
  { path: '/apps', name: 'Apps', component: AppIndex },
  {
    path: '/login',
    name: 'Login',
    components: { default: Login },
    meta: { layout: 'auth' }
  }
]

export default createRouter({
  history: createWebHistory(),
  routes,
})
