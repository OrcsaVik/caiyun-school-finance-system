<template>
  <div class="auth-container">
    <div class="wrap">
      <div class="img-content">
        <img class="current" src="@/assets/dz_pic.png" alt="campus" />
      </div>
      <div class="main">
        <div class="tabHead" style="margin-bottom:12px;">
          <span :class="{selected_underline: tab==='password'}"><a @click.prevent="switchTab('password')">密码登录</a></span>
          <span :class="{selected_underline: tab==='dynamic'}" style="margin-left:16px;"><a @click.prevent="switchTab('dynamic')">验证码登录</a></span>
          <span :class="{selected_underline: tab==='qrcode'}" style="margin-left:16px;"><a @click.prevent="switchTab('qrcode')">扫码登录</a></span>
        </div>

        <div v-if="tab==='password'">
          <el-form :model="form" class="form m-account">
            <div class="item username">
              <span class="prex-icon"><img class="login-icon" src="@/assets/user1.png" alt="user" /></span>
              <el-input v-model="form.username" placeholder="请输入账号/学号/登录名" />
            </div>
            <div class="item password">
              <span class="prex-icon"><img class="login-icon" src="@/assets/pass1.png" alt="pass" /></span>
              <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
            </div>
            <div class="btn">
              <a class="login-btn" :class="{disabled: !canLogin}" @click.prevent="login">{{ loading ? '登录中...' : '登录' }}</a>
            </div>
          </el-form>
          <div class="new_ts">
            <p>账号：使用学号或工号登录。首次使用请先激活账号。</p>
          </div>
        </div>

        <div v-else-if="tab==='dynamic'">
          <el-form :model="form" class="form m-phone">
            <div class="item username">
              <span class="prex-icon"><img class="login-icon" src="@/assets/user1.png" alt="user" /></span>
              <el-input v-model="form.phone" placeholder="请输入手机号/学号" />
            </div>
            <div class="item code">
              <span class="prex-icon"><img class="login-icon" src="@/assets/captcha1.png" alt="captcha" /></span>
              <el-input v-model="form.sms" placeholder="请输入短信验证码" />
              <a class="get-code" :class="{disabled: countdown > 0}" href="javascript:void(0)" @click.prevent="sendCode"><em class="getCodeText">{{ codeText }}</em></a>
            </div>
            <div class="btn">
              <a class="login-btn" :class="{disabled: !canLogin}" @click.prevent="login">{{ loading ? '登录中...' : '登录' }}</a>
            </div>
            <div class="phoneCode-remark">海外手机请按“国家代码-手机号”的格式输入，如：1-888888</div>
          </el-form>
        </div>

        <div v-else>
          <div class="wechart-wrap">
            <div class="qr_scan_box">
              <div class="qr_scan_code">
                <img id="qr_img" :src="qrImg" alt="qr" />
              </div>
            </div>
            <p style="font-size:12px;color:#373737;text-align:center;margin-top:-6px;">使用微信扫一扫登录</p>
            <p style="font-size:12px;color:#373737;text-align:center;margin-bottom:10px;">“广东财经大学统一认证”</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed } from 'vue'
import { ElMessage, ElLoading } from 'element-plus'
import { useRouter } from 'vue-router'
import { authApi } from '@/api/auth'

const router = useRouter()
const tab = ref<'password'|'dynamic'|'qrcode'>('password')
const loading = ref(false)
const countdown = ref(0)
const qrImg = "data:image/png;base64,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";

// 1. 新增：用于防止重复点击“获取验证码”的loading状态
const sendCodeLoading = ref(false)
// 2. 新增：保存定时器实例，防止多定时器叠加
const countdownTimer = ref<NodeJS.Timeout | null>(null)

// 表单数据
const form = reactive({ 
  username: '', 
  password: '', 
  phone: '', // 手机号绑定字段
  sms: '',
  captcha: ''
})

// 表单验证规则（优化手机号验证：增加trim处理，去除空格）
const validateMobile = (value: string) => {
  const trimmedValue = value.trim() // 去除输入值的前后空格
  if (!trimmedValue) return '请输入手机号/学号'
  const phoneRegex = /^1[3-9]\d{9}$/
  const studentIdRegex = /^\d{8,12}$/
  if (!phoneRegex.test(trimmedValue) && !studentIdRegex.test(trimmedValue)) {
    return '请输入正确的手机号或学号'
  }
  return true
}

// 其他验证规则（保持不变）
const validateUsername = (value: string) => {
  if (!value) return '请输入账号/学号/登录名'
  if (value.length < 3) return '账号长度至少3位'
  return true
}
const validatePassword = (value: string) => {
  if (!value) return '请输入密码'
  if (value.length < 6) return '密码长度至少6位'
  return true
}
const validateSms = (value: string) => {
  if (!value) return '请输入短信验证码'
  if (!/^\d{6}$/.test(value)) return '验证码为6位数字'
  return true
}

// 计算属性（保持不变）
const canLogin = computed(() => {
  if (tab.value === 'password') {
    return form.username && form.password && !loading.value
  } else if (tab.value === 'dynamic') {
    return form.phone && form.sms && !loading.value
  }
  return false
})
const codeText = computed(() => {
  return countdown.value > 0 ? `${countdown.value}s后重新获取` : '获取验证码'
})

// 登录方法（保持不变）
const login = async () => {
  if (loading.value) return
  
  let validation = true
  let errorMsg = ''
  
  if (tab.value === 'password') {
    const usernameCheck = validateUsername(form.username)
    const passwordCheck = validatePassword(form.password)
    
    if (usernameCheck !== true) {
      errorMsg = usernameCheck
      validation = false
    } else if (passwordCheck !== true) {
      errorMsg = passwordCheck
      validation = false
    }
  } else if (tab.value === 'dynamic') {
    const mobileCheck = validateMobile(form.phone)
    const smsCheck = validateSms(form.sms)
    
    if (mobileCheck !== true) {
      errorMsg = mobileCheck
      validation = false
    } else if (smsCheck !== true) {
      errorMsg = smsCheck
      validation = false
    }
  }
  
  if (!validation) {
    ElMessage.error(errorMsg)
    return
  }
  
  loading.value = true
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '登录中...',
    background: 'rgba(0, 0, 0, 0.7)'
  })
  
  try {
    let response
    if (tab.value === 'password') {
      response = await authApi.loginByPassword({
        account: form.username, // 修改为account以匹配后端接口
        password: form.password
      })
    } else if (tab.value === 'dynamic') {
      response = await authApi.loginBySms({
        phone : form.phone.trim(), // 使用phone参数以匹配后端接口
        code: form.sms
      })
    }

    if (response) {
      const { token, userInfo } = response
      localStorage.setItem('userToken', token)
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
      ElMessage.success('登录成功！')
      router.push('/')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '登录失败，请重试')
  } finally {
    loading.value = false
    loadingInstance.close()
  }
}

// 发送验证码方法（关键修改）
// 发送验证码方法（关键修改：添加 usage 参数）
const sendCode = async () => {
  if (sendCodeLoading.value || countdown.value > 0) return
  
  const trimmedMobile = form.phone.trim()
  const mobileCheck = validateMobile(trimmedMobile)
  
  if (mobileCheck !== true) {
    ElMessage.error(mobileCheck)
    return
  }
  
  if (!trimmedMobile) {
    ElMessage.error('请输入手机号')
    return
  }
  
  try {
    sendCodeLoading.value = true
    await authApi.sendSmsCode({
      phone: trimmedMobile,  // 使用phone参数以匹配后端接口
      usage: 'login' // 传递验证码用途（与后端一致）
    })

    ElMessage.success('验证码已发送，请注意查收')
    
    if (countdownTimer.value) {
      clearInterval(countdownTimer.value)
    }
    countdown.value = 60
    countdownTimer.value = setInterval(() => {
      countdown.value--
      if (countdownTimer.value && countdown.value <= 0) {
        clearInterval(countdownTimer.value)
        countdownTimer.value = null
      }
    }, 1000)
  } catch (error: any) {
    ElMessage.error(error.message || '发送验证码失败，请重试')
  } finally {
    sendCodeLoading.value = false
  }
}

// 生成二维码方法（保持不变）
const generateQrCode = async () => {
  try {
    const response = await authApi.getWechatQrCode()


    const qrCodeId = response.qrCodeId
    const checkInterval = setInterval(async () => {
      try {
        const statusResponse = await authApi.checkWechatLogin(qrCodeId)
        if (statusResponse.status === 'CONFIRMED' && statusResponse.token && statusResponse.userInfo) {  // 修改为CONFIRMED以匹配后端状态
          clearInterval(checkInterval)
          localStorage.setItem('userToken', statusResponse.token)
          localStorage.setItem('userInfo', JSON.stringify(statusResponse.userInfo))
          ElMessage.success('登录成功！')
          router.push('/')
        } else if (statusResponse.status === 'EXPIRED') {
          clearInterval(checkInterval)
          ElMessage.warning('二维码已过期，请刷新重试')
          generateQrCode()
        }
      } catch (error: any) {
        clearInterval(checkInterval)
        ElMessage.error(error.message || '检查扫码状态失败')
      }
    }, 2000)
  } catch (error: any) {
    ElMessage.error(error.message || '获取二维码失败')
  }
}

// 切换标签页方法（优化：清除定时器，防止切换后倒计时继续）
const switchTab = (newTab: 'password'|'dynamic'|'qrcode') => {
  tab.value = newTab
  // 重置表单
  Object.assign(form, { username: '', password: '', mobile: '', sms: '', captcha: '' })
  
  // 6. 新增：切换tab时清除倒计时定时器
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value)
    countdownTimer.value = null
  }
  countdown.value = 0
  sendCodeLoading.value = false // 重置发送loading
  
  if (newTab === 'qrcode') {
    generateQrCode()
  }
}
</script>

<style scoped>
.auth-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, #bde4ea 0%, #e6f6f9 100%);
  animation: gradientBG 10s ease infinite;
}
.wrap {
  width: 900px;
  height: 600px;
  display: flex;
  align-items: center;
  background: #ffffff;
  box-shadow: 0px 4px 12px 1px rgba(0, 104, 113, 0.31);
  border-radius: 10px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.wrap:hover {
  transform: translateY(-5px);
  box-shadow: 0px 8px 24px 2px rgba(0, 104, 113, 0.4);
}
.img-content {
  width: 445px;
  height: 100%;
  overflow: hidden;
  position: relative;
}
.img-content img.current {
  width: calc(100% + 2px);
  height: calc(100% + 2px);
  display: block;
  margin: -1px 0 0 -1px;
  transition: transform 0.5s ease;
}
.img-content:hover img.current {
  transform: scale(1.05);
}
.main {
  width: calc(100% - 445px);
  height: 100%;
  background: #fff;
  padding: 45px 28px;
  position: relative;
  animation: fadeIn 0.6s ease;
}
.tabHead a {
  font-size: 18px;
  color: #999;
  line-height: 30px;
  text-decoration: none;
  transition: all 0.3s ease;
  position: relative;
}
.tabHead a:hover {
  color: #006871;
}
.selected_underline a{
  font-weight: bold;
  color: #006871;
  letter-spacing: 1.2px;
  border-bottom: 2px solid #006871;
  animation: borderSlide 0.3s ease;
}
.form .item{
  border: 1px solid #ccc;
  margin-bottom: 30px;
  position: relative;
  transition: all 0.3s ease;
  border-radius: 4px;
}
.form .item:focus-within {
  border-color: #006871;
  box-shadow: 0 0 0 2px rgba(0, 104, 113, 0.1);
  transform: translateY(-2px);
}
.prex-icon{
  position: absolute;
  left: 0;
  top: 0;
  transition: transform 0.3s ease;
}
.form .item:focus-within .prex-icon {
  transform: scale(1.1);
}
.login-icon{ width: 40px; height: 40px; }
.form .item :deep(.el-input__wrapper){
  box-shadow: none;
  border-radius: 0;
  padding-left: 40px;
  height: 40px;
  transition: background 0.3s ease;
}
.btn{ margin-bottom: 30px; }
.login-btn{
  display: block;
  width: 100%;
  height: 40px;
  line-height: 40px;
  text-align: center;
  color: #fff;
  text-decoration: none;
  background: linear-gradient(90deg, #02757F, #006871);
  border-radius: 4px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}
.login-btn:hover:not(.disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 104, 113, 0.3);
  background: linear-gradient(90deg, #038791, #007983);
}
.login-btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 5px;
  height: 5px;
  background: rgba(255, 255, 255, 0.5);
  opacity: 0;
  border-radius: 100%;
  transform: scale(1, 1) translate(-50%);
  transform-origin: 50% 50%;
}
.login-btn:active:not(.disabled)::after {
  animation: ripple 1s ease-out;
}
.form .item.code {
  display: flex;
  align-items: center;
  padding-right: 120px;
}

.form .item.code :deep(.el-input__wrapper) {
  flex: 1;
}

.get-code {
  position: absolute;
  right: 0;
  top: 0;
  width: 120px;
  height: 100%;
  line-height: 40px;
  text-align: center;
  color: #006871;
  text-decoration: none;
  border-left: 1px solid #ccc;
  transition: all 0.3s ease;
}

.get-code:hover:not(.disabled) {
  background: rgba(0, 104, 113, 0.05);
}

.get-code.disabled {
  color: #999;
  cursor: not-allowed;
}

.getCodeText {
  font-style: normal;
}
.btn{ margin-bottom: 14px; }
.login-btn{
  display: block;
  width: 100%;
  height: 36px;
  line-height: 40px;
  text-align: center;
  color: #fff;
  text-decoration: none;
  background: linear-gradient(90deg, #02757F, #006871);
  border-radius: 4px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}
.login-btn:hover:not(.disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 104, 113, 0.3);
  background: linear-gradient(90deg, #038791, #007983);
}
.login-btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 5px;
  height: 5px;
  background: rgba(255, 255, 255, 0.5);
  opacity: 0;
  border-radius: 100%;
  transform: scale(1, 1) translate(-50%);
  transform-origin: 50% 50%;
}
.login-btn:active:not(.disabled)::after {
  animation: ripple 1s ease-out;
}
@keyframes gradientBG {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
@keyframes borderSlide {
  from { width: 0; }
  to { width: 100%; }
}
@keyframes ripple {
  0% { transform: scale(0, 0); opacity: 0.5; }
  20% { transform: scale(25, 25); opacity: 0.3; }
  100% { opacity: 0; transform: scale(40, 40); }
}
@media screen and (max-width: 1366px) {
  .wrap{ width: 700px; height: 500px; }
  .img-content{ width: 330px; }
  .main{ width: calc(100% - 330px); padding: 28px; }
  .form .item{ margin-bottom: 14px; }
  .form .item :deep(.el-input__wrapper){ height: 36px; }
  .btn{ margin: 14px 0; }
}
</style>
