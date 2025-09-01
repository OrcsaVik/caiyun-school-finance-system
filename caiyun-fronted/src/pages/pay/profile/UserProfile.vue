<template>
  <div class="user-profile">
    <div class="page-header">
      <h1>个人信息</h1>
      <p>查看和管理您的个人信息和缴费设置</p>
    </div>
    
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <!-- 左侧个人信息卡片 -->
      <div class="md:col-span-1">
        <el-card class="mb-6">
          <div class="flex flex-col items-center">
            <div class="avatar-container mb-4">
              <el-avatar :size="100" :src="userInfo.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
              <div class="avatar-upload">
                <el-upload
                  action="#"
                  :auto-upload="false"
                  :show-file-list="false"
                  :on-change="handleAvatarChange"
                >
                  <el-icon class="avatar-edit-icon"><Camera /></el-icon>
                </el-upload>
              </div>
            </div>
            <h2 class="text-xl font-bold mb-1">{{ userInfo.name }}</h2>
            <p class="text-gray-500 mb-4">{{ userInfo.studentId }}</p>
            <div class="w-full border-t pt-4">
              <div class="flex items-center mb-3">
                <el-icon class="mr-2 text-gray-400"><School /></el-icon>
                <span>{{ userInfo.college }}</span>
              </div>
              <div class="flex items-center mb-3">
                <el-icon class="mr-2 text-gray-400"><User /></el-icon>
                <span>{{ userInfo.major }}</span>
              </div>
              <div class="flex items-center mb-3">
                <el-icon class="mr-2 text-gray-400"><Calendar /></el-icon>
                <span>{{ userInfo.grade }}级</span>
              </div>
              <div class="flex items-center">
                <el-icon class="mr-2 text-gray-400"><Phone /></el-icon>
                <span>{{ userInfo.phone }}</span>
              </div>
            </div>
          </div>
        </el-card>
        
        <!-- 缴费统计卡片 -->
        <el-card>
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2"><DataAnalysis /></el-icon>
              <span>缴费统计</span>
            </div>
          </template>
          <div class="flex flex-col gap-4">
            <div class="flex justify-between items-center">
              <span class="text-gray-500">已缴费项目</span>
              <span class="text-lg font-bold">{{ completedCount }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-500">待缴费项目</span>
              <span class="text-lg font-bold text-warning">{{ pendingCount }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-500">总缴费金额</span>
              <span class="text-lg font-bold text-success">¥{{ totalPaidAmount }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-500">待缴费金额</span>
              <span class="text-lg font-bold text-danger">¥{{ totalPendingAmount }}</span>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 右侧信息和设置 -->
      <div class="md:col-span-2">
        <!-- 个人信息编辑 -->
        <el-card class="mb-6">
          <template #header>
            <div class="flex justify-between items-center">
              <div class="flex items-center">
                <el-icon class="mr-2"><Edit /></el-icon>
                <span>个人信息</span>
              </div>
              <el-button type="primary" @click="saveUserInfo" :disabled="!infoChanged">
                <el-icon class="mr-1"><Check /></el-icon>
                保存修改
              </el-button>
            </div>
          </template>
          
          <el-form :model="editUserInfo" label-width="100px">
            <el-form-item label="姓名">
              <el-input v-model="editUserInfo.name" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="学号">
              <el-input v-model="editUserInfo.studentId" placeholder="请输入学号" disabled />
            </el-form-item>
            <el-form-item label="手机号码">
              <el-input v-model="editUserInfo.phone" placeholder="请输入手机号码" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="editUserInfo.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="学院">
              <el-input v-model="editUserInfo.college" placeholder="请输入学院" disabled />
            </el-form-item>
            <el-form-item label="专业">
              <el-input v-model="editUserInfo.major" placeholder="请输入专业" disabled />
            </el-form-item>
            <el-form-item label="年级">
              <el-input v-model="editUserInfo.grade" placeholder="请输入年级" disabled />
            </el-form-item>
          </el-form>
        </el-card>
        
        <!-- 缴费设置 -->
        <el-card class="mb-6">
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2"><Setting /></el-icon>
              <span>缴费设置</span>
            </div>
          </template>
          
          <el-form :model="paymentSettings" label-width="120px">
            <el-form-item label="默认支付方式">
              <el-select v-model="paymentSettings.defaultPaymentMethod" class="w-full">
                <el-option label="支付宝" value="alipay" />
                <el-option label="微信支付" value="wechat" />
                <el-option label="银行卡" value="bank" />
              </el-select>
            </el-form-item>
            <el-form-item label="缴费提醒">
              <el-switch
                v-model="paymentSettings.enableReminder"
                active-text="开启"
                inactive-text="关闭"
              />
            </el-form-item>
            <el-form-item label="提醒提前天数" v-if="paymentSettings.enableReminder">
              <el-input-number v-model="paymentSettings.reminderDays" :min="1" :max="30" />
            </el-form-item>
            <el-form-item label="提醒方式" v-if="paymentSettings.enableReminder">
              <el-checkbox-group v-model="paymentSettings.reminderMethods">
                <el-checkbox label="email">邮件</el-checkbox>
                <el-checkbox label="sms">短信</el-checkbox>
                <el-checkbox label="app">App通知</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="自动缴费">
              <el-switch
                v-model="paymentSettings.enableAutoPay"
                active-text="开启"
                inactive-text="关闭"
              />
            </el-form-item>
            <el-form-item label="自动缴费限额" v-if="paymentSettings.enableAutoPay">
              <el-input-number
                v-model="paymentSettings.autoPayLimit"
                :min="0"
                :max="10000"
                :step="100"
                :precision="2"
              />
              <div class="text-gray-400 text-xs mt-1">超过此金额将不会自动缴费</div>
            </el-form-item>
          </el-form>
          
          <div class="flex justify-end mt-4">
            <el-button type="primary" @click="savePaymentSettings">
              <el-icon class="mr-1"><Check /></el-icon>
              保存设置
            </el-button>
          </div>
        </el-card>
        
        <!-- 账户安全 -->
        <el-card>
          <template #header>
            <div class="flex items-center">
              <el-icon class="mr-2"><Lock /></el-icon>
              <span>账户安全</span>
            </div>
          </template>
          
          <div class="flex flex-col gap-4">
            <div class="flex justify-between items-center p-3 border-b">
              <div>
                <div class="font-medium">登录密码</div>
                <div class="text-gray-400 text-sm">定期修改密码可以保护账户安全</div>
              </div>
              <el-button @click="showChangePasswordDialog">修改</el-button>
            </div>
            
            <div class="flex justify-between items-center p-3 border-b">
              <div>
                <div class="font-medium">手机绑定</div>
                <div class="text-gray-400 text-sm">已绑定：{{ userInfo.phone }}</div>
              </div>
              <el-button @click="showChangePhoneDialog">修改</el-button>
            </div>
            
            <div class="flex justify-between items-center p-3">
              <div>
                <div class="font-medium">邮箱绑定</div>
                <div class="text-gray-400 text-sm">{{ userInfo.email ? `已绑定：${userInfo.email}` : '未绑定' }}</div>
              </div>
              <el-button @click="showChangeEmailDialog">{{ userInfo.email ? '修改' : '绑定' }}</el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="changePasswordDialogVisible"
      title="修改密码"
      width="400px"
      destroy-on-close
    >
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input v-model="passwordForm.currentPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      
      <div class="flex justify-end mt-4">
        <el-button @click="changePasswordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="changePassword">
          <el-icon class="mr-1"><Check /></el-icon>
          确认修改
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { usePayStore } from '@/store/pay'
import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'
import { Camera, School, User, Calendar, Phone, DataAnalysis, Edit, Setting, Lock, Check } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'

const payStore = usePayStore()
const { payments } = storeToRefs(payStore)

// 用户信息
const userInfo = reactive({
  name: '张三',
  studentId: '2022010101',
  college: '计算机科学与技术学院',
  major: '软件工程',
  grade: '2022',
  phone: '13800138000',
  email: 'zhangsan@example.com',
  avatar: ''
})

// 编辑用户信息
const editUserInfo = reactive({ ...userInfo })

// 判断信息是否有修改
const infoChanged = computed(() => {
  return JSON.stringify(userInfo) !== JSON.stringify(editUserInfo)
})

// 保存用户信息
const saveUserInfo = () => {
  Object.assign(userInfo, editUserInfo)
  ElMessage.success('个人信息保存成功')
}

// 处理头像上传
const handleAvatarChange = (file) => {
  // 实际项目中这里会上传到服务器
  const reader = new FileReader()
  reader.onload = (e) => {
    userInfo.avatar = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

// 缴费设置
const paymentSettings = reactive({
  defaultPaymentMethod: 'alipay',
  enableReminder: true,
  reminderDays: 7,
  reminderMethods: ['email', 'app'],
  enableAutoPay: false,
  autoPayLimit: 500
})

// 保存缴费设置
const savePaymentSettings = () => {
  ElMessage.success('缴费设置保存成功')
}

// 修改密码相关
const changePasswordDialogVisible = ref(false)
const passwordFormRef = ref<FormInstance>()
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (passwordForm.confirmPassword !== '') {
      passwordFormRef.value.validateField('confirmPassword')
    }
    callback()
  }
}

const validateConfirmPass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = reactive<FormRules>({
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' },
    { validator: validatePass, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPass, trigger: 'blur' }
  ]
})

// 显示修改密码对话框
const showChangePasswordDialog = () => {
  changePasswordDialogVisible.value = true
}

// 修改密码
const changePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate((valid) => {
    if (valid) {
      // 实际项目中这里会调用API
      ElMessage.success('密码修改成功')
      changePasswordDialogVisible.value = false
    } else {
      return false
    }
  })
}

// 显示修改手机号对话框
const showChangePhoneDialog = () => {
  ElMessage.info('修改手机号功能开发中')
}

// 显示修改邮箱对话框
const showChangeEmailDialog = () => {
  ElMessage.info('修改邮箱功能开发中')
}

// 统计数据
const pendingCount = computed(() => {
  return payments.value.filter(item => item.status === '未缴费' || item.status === '已逾期').length
})

const completedCount = computed(() => {
  return payments.value.filter(item => item.status === '已缴费').length
})

const totalPaidAmount = computed(() => {
  return payments.value
    .filter(item => item.status === '已缴费')
    .reduce((sum, item) => sum + parseFloat(item.amount), 0)
    .toFixed(2)
})

const totalPendingAmount = computed(() => {
  return payments.value
    .filter(item => item.status === '未缴费' || item.status === '已逾期')
    .reduce((sum, item) => sum + parseFloat(item.amount), 0)
    .toFixed(2)
})
</script>

<style scoped>
.user-profile {
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

.avatar-container {
  position: relative;
  display: inline-block;
}

.avatar-upload {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 32px;
  height: 32px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.avatar-upload:hover {
  background-color: rgba(0, 0, 0, 0.7);
}

.avatar-edit-icon {
  color: #fff;
  font-size: 16px;
}

.text-warning {
  color: #e6a23c;
}

.text-success {
  color: #67c23a;
}

.text-danger {
  color: #f56c6c;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>