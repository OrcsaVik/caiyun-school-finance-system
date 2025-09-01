<template>
  <div class="profile-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>个人信息</h1>
      <p>管理您的个人资料和账户设置</p>
    </div>

    <div class="profile-content">
      <!-- 左侧导航 -->
      <div class="nav-section">
        <div class="user-card">
          <div class="avatar-wrapper">
            <el-avatar :size="80" :src="userInfo.avatar" @error="handleAvatarError">
              <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
            </el-avatar>
            <div class="avatar-upload" @click="triggerAvatarUpload">
              <el-icon><Camera /></el-icon>
              <input
                ref="avatarInput"
                type="file"
                accept="image/*"
                style="display: none"
                @change="handleAvatarChange"
              >
            </div>
          </div>
          <h3>{{ userInfo.name }}</h3>
          <p>{{ userInfo.studentId }}</p>
        </div>
        <el-menu
          :default-active="activeTab"
          class="profile-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="basic">
            <el-icon><User /></el-icon>
            <span>基本资料</span>
          </el-menu-item>
          <el-menu-item index="security">
            <el-icon><Lock /></el-icon>
            <span>安全设置</span>
          </el-menu-item>
          <el-menu-item index="notification">
            <el-icon><Bell /></el-icon>
            <span>通知设置</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 右侧内容 -->
      <div class="content-section">
        <!-- 基本资料 -->
        <div v-show="activeTab === 'basic'" class="tab-content">
          <h2>基本资料</h2>
          <el-form
            ref="basicFormRef"
            :model="basicForm"
            :rules="basicRules"
            label-width="100px"
          >
            <el-form-item label="姓名" prop="name">
              <el-input v-model="basicForm.name" />
            </el-form-item>
            <el-form-item label="学号" prop="studentId">
              <el-input v-model="basicForm.studentId" disabled />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="basicForm.gender">
                <el-radio label="male">男</el-radio>
                <el-radio label="female">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="basicForm.phone">
                <template #append>
                  <el-button @click="verifyPhone">验证</el-button>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="basicForm.email">
                <template #append>
                  <el-button @click="verifyEmail">验证</el-button>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="学院" prop="college">
              <el-select v-model="basicForm.college" placeholder="请选择学院">
                <el-option
                  v-for="item in colleges"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="专业" prop="major">
              <el-select v-model="basicForm.major" placeholder="请选择专业">
                <el-option
                  v-for="item in majors"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="年级" prop="grade">
              <el-select v-model="basicForm.grade" placeholder="请选择年级">
                <el-option
                  v-for="item in grades"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveBasicInfo">保存修改</el-button>
              <el-button @click="resetBasicForm">重置</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 安全设置 -->
        <div v-show="activeTab === 'security'" class="tab-content">
          <h2>安全设置</h2>
          <div class="security-items">
            <div class="security-item">
              <div class="item-info">
                <h3>登录密码</h3>
                <p>建议您定期更改密码，设置安全性高的密码可以使账号更安全</p>
              </div>
              <el-button @click="showChangePassword = true">修改</el-button>
            </div>
            <div class="security-item">
              <div class="item-info">
                <h3>手机验证</h3>
                <p>已绑定手机：{{ maskPhone(userInfo.phone) }}</p>
              </div>
              <el-button @click="showChangePhone = true">修改</el-button>
            </div>
            <div class="security-item">
              <div class="item-info">
                <h3>邮箱验证</h3>
                <p>已绑定邮箱：{{ maskEmail(userInfo.email) }}</p>
              </div>
              <el-button @click="showChangeEmail = true">修改</el-button>
            </div>
          </div>
        </div>

        <!-- 通知设置 -->
        <div v-show="activeTab === 'notification'" class="tab-content">
          <h2>通知设置</h2>
          <div class="notification-items">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>系统通知</span>
                  <el-switch v-model="notificationSettings.system.enabled" />
                </div>
              </template>
              <div class="notification-options">
                <el-checkbox v-model="notificationSettings.system.email">邮件通知</el-checkbox>
                <el-checkbox v-model="notificationSettings.system.sms">短信通知</el-checkbox>
                <el-checkbox v-model="notificationSettings.system.wechat">微信通知</el-checkbox>
              </div>
            </el-card>

            <el-card>
              <template #header>
                <div class="card-header">
                  <span>缴费提醒</span>
                  <el-switch v-model="notificationSettings.payment.enabled" />
                </div>
              </template>
              <div class="notification-options">
                <el-checkbox v-model="notificationSettings.payment.email">邮件通知</el-checkbox>
                <el-checkbox v-model="notificationSettings.payment.sms">短信通知</el-checkbox>
                <el-checkbox v-model="notificationSettings.payment.wechat">微信通知</el-checkbox>
              </div>
            </el-card>

            <el-card>
              <template #header>
                <div class="card-header">
                  <span>账户安全</span>
                  <el-switch v-model="notificationSettings.security.enabled" />
                </div>
              </template>
              <div class="notification-options">
                <el-checkbox v-model="notificationSettings.security.email">邮件通知</el-checkbox>
                <el-checkbox v-model="notificationSettings.security.sms">短信通知</el-checkbox>
                <el-checkbox v-model="notificationSettings.security.wechat">微信通知</el-checkbox>
              </div>
            </el-card>
          </div>
          <div class="notification-actions">
            <el-button type="primary" @click="saveNotificationSettings">保存设置</el-button>
            <el-button @click="resetNotificationSettings">重置</el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="showChangePassword"
      title="修改密码"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px"
      >
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input
            v-model="passwordForm.currentPassword"
            type="password"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showChangePassword = false">取消</el-button>
        <el-button type="primary" @click="changePassword">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Lock, Bell, Camera } from '@element-plus/icons-vue'

// 用户信息
const userInfo = reactive({
  name: '张三',
  studentId: '2020123456',
  avatar: '',
  phone: '13800138000',
  email: 'zhangsan@example.com'
})

// 基本表单数据
const basicForm = reactive({
  name: userInfo.name,
  studentId: userInfo.studentId,
  gender: 'male',
  phone: userInfo.phone,
  email: userInfo.email,
  college: '',
  major: '',
  grade: ''
})

// 基本表单验证规则
const basicRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  college: [{ required: true, message: '请选择学院', trigger: 'change' }],
  major: [{ required: true, message: '请选择专业', trigger: 'change' }],
  grade: [{ required: true, message: '请选择年级', trigger: 'change' }]
}

// 密码表单数据
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 密码表单验证规则
const passwordRules = {
  currentPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, message: '密码长度不能小于8位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 通知设置
const notificationSettings = reactive({
  system: {
    enabled: true,
    email: true,
    sms: true,
    wechat: false
  },
  payment: {
    enabled: true,
    email: true,
    sms: true,
    wechat: true
  },
  security: {
    enabled: true,
    email: true,
    sms: true,
    wechat: false
  }
})

// 选项数据
const colleges = [
  { value: 'cs', label: '计算机学院' },
  { value: 'ee', label: '电子工程学院' },
  { value: 'me', label: '机械工程学院' }
]

const majors = [
  { value: 'se', label: '软件工程' },
  { value: 'cs', label: '计算机科学与技术' },
  { value: 'is', label: '信息安全' }
]

const grades = [
  { value: '2020', label: '2020级' },
  { value: '2021', label: '2021级' },
  { value: '2022', label: '2022级' },
  { value: '2023', label: '2023级' }
]

// 状态变量
const activeTab = ref('basic')
const showChangePassword = ref(false)
const showChangePhone = ref(false)
const showChangeEmail = ref(false)
const basicFormRef = ref()
const passwordFormRef = ref()
const avatarInput = ref()

// 方法
const handleMenuSelect = (index: string) => {
  activeTab.value = index
}

const handleAvatarError = () => {
  userInfo.avatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
}

const triggerAvatarUpload = () => {
  avatarInput.value.click()
}

const handleAvatarChange = async (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (!file) return

  // 验证文件类型和大小
  const isImage = /^image\//.test(file.type)
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB！')
    return
  }

  try {
    // 模拟上传
    await new Promise(resolve => setTimeout(resolve, 1000))
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => {
      userInfo.avatar = reader.result as string
      ElMessage.success('头像上传成功')
    }
  } catch (error) {
    ElMessage.error('头像上传失败，请重试')
  }
}

const saveBasicInfo = async () => {
  if (!basicFormRef.value) return
  
  await basicFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        // 模拟保存
        await new Promise(resolve => setTimeout(resolve, 800))
        Object.assign(userInfo, {
          name: basicForm.name,
          phone: basicForm.phone,
          email: basicForm.email
        })
        ElMessage.success('保存成功')
      } catch (error) {
        ElMessage.error('保存失败，请重试')
      }
    }
  })
}

const resetBasicForm = () => {
  if (basicFormRef.value) {
    basicFormRef.value.resetFields()
  }
}

const changePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        // 模拟修改密码
        await new Promise(resolve => setTimeout(resolve, 800))
        showChangePassword.value = false
        passwordForm.currentPassword = ''
        passwordForm.newPassword = ''
        passwordForm.confirmPassword = ''
        ElMessage.success('密码修改成功')
      } catch (error) {
        ElMessage.error('密码修改失败，请重试')
      }
    }
  })
}

const verifyPhone = async () => {
  try {
    // 模拟发送验证码
    await new Promise(resolve => setTimeout(resolve, 800))
    ElMessage.success('验证码已发送到您的手机')
  } catch (error) {
    ElMessage.error('验证码发送失败，请重试')
  }
}

const verifyEmail = async () => {
  try {
    // 模拟发送验证邮件
    await new Promise(resolve => setTimeout(resolve, 800))
    ElMessage.success('验证邮件已发送到您的邮箱')
  } catch (error) {
    ElMessage.error('验证邮件发送失败，请重试')
  }
}

const saveNotificationSettings = async () => {
  try {
    // 模拟保存设置
    await new Promise(resolve => setTimeout(resolve, 800))
    ElMessage.success('设置保存成功')
  } catch (error) {
    ElMessage.error('设置保存失败，请重试')
  }
}

const resetNotificationSettings = () => {
  Object.assign(notificationSettings, {
    system: { enabled: true, email: true, sms: true, wechat: false },
    payment: { enabled: true, email: true, sms: true, wechat: true },
    security: { enabled: true, email: true, sms: true, wechat: false }
  })
}

const maskPhone = (phone: string) => {
  return phone.replace(/^(\d{3})\d{4}(\d{4})$/, '$1****$2')
}

const maskEmail = (email: string) => {
  const [username, domain] = email.split('@')
  const maskedUsername = username.charAt(0) + '*'.repeat(username.length - 2) + username.charAt(username.length - 1)
  return `${maskedUsername}@${domain}`
}
</script>

<style scoped>
.profile-container {
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

.profile-content {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 24px;
  animation: fadeIn 0.6s ease-out 0.2s both;
}

.nav-section {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  backdrop-filter: blur(10px);
}

.user-card {
  padding: 24px;
  text-align: center;
  border-bottom: 1px solid #eee;
}

.avatar-wrapper {
  position: relative;
  width: 80px;
  height: 80px;
  margin: 0 auto 16px;
}

.avatar-upload {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 24px;
  height: 24px;
  background: var(--el-color-primary);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.avatar-upload:hover {
  transform: scale(1.1);
}

.user-card h3 {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}

.user-card p {
  color: #666;
  font-size: 0.9rem;
}

.profile-menu {
  border: none;
  background: transparent;
}

.content-section {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  backdrop-filter: blur(10px);
}

.tab-content h2 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 24px;
  color: #333;
}

.security-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.security-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.security-item:hover {
  transform: translateX(5px);
  background: #f0f2f5;
}

.item-info h3 {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}

.item-info p {
  color: #666;
  font-size: 0.9rem;
}

.notification-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.notification-options {
  display: flex;
  gap: 24px;
  margin-top: 16px;
}

.notification-actions {
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
</style>