import { LoginResponse, UserInfo, WechatQrCodeResponse, WechatLoginStatusResponse } from '@/api/auth'

// 模拟用户数据
const mockAdminUser: UserInfo = {
  id: '1',
  username: 'admin',
  name: '管理员',
  role: 'admin',
  mobile: '13800138000',
  avatar: 'https://example.com/avatar.jpg'
}

const mockStudentUser: UserInfo = {
  id: '2',
  username: 'student',
  name: '张三',
  role: 'student',
  mobile: '13900139000'
}

// 模拟登录响应
const mockPasswordLoginResponse: LoginResponse = {
  token: 'mock-token-password',
  userInfo: mockAdminUser
}

const mockSmsLoginResponse: LoginResponse = {
  token: 'mock-token-sms',
  userInfo: mockStudentUser
}

// 模拟微信登录相关响应
const mockWechatQrCode: WechatQrCodeResponse = {
  qrCode: 'data:image/png;base64,mock-qr-code',
  qrCodeId: 'mock-qr-code-id'
}

const mockWechatLoginStatus: WechatLoginStatusResponse = {
  status: 'PENDING'
}

const mockAuthData = {
  // 账号密码登录
  '/auth/login/password': () => mockPasswordLoginResponse,

  // 短信验证码登录
  '/auth/login/sms': () => mockSmsLoginResponse,

  // 发送短信验证码
  '/auth/sms/send': () => ({}),

  // 获取微信登录二维码
  '/auth/wechat/qrcode': () => mockWechatQrCode,

  // 检查微信扫码状态
  '/auth/wechat/check': () => mockWechatLoginStatus,

  // 获取当前用户信息
  '/auth/user/info': () => mockAdminUser,

  // 退出登录
  '/auth/logout': () => ({})
}

export default mockAuthData