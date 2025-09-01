import { request } from './client'
import type { RequestConfig } from './client'

export interface PasswordLoginRequest {
  account: string  // 修改为account以匹配后端接口
  password: string
}

export interface SmsLoginRequest {
  phone: string  // 修改为mobile以匹配后端接口
  code: string
}

export interface SendSmsCodeRequest {
  mobile: string;  // 修改为mobile以匹配后端接口
  usage: string;
}

export interface UserInfo {
  id: string
  username: string
  name: string
  role: string
  mobile?: string
  avatar?: string
}

export interface LoginResponse {
  token: string
  userInfo: UserInfo
}

export interface WechatQrCodeResponse {
  qrCode: string
  qrCodeId: string
}

export interface WechatLoginStatusResponse {
  status: 'WAITING' | 'SCANNED' | 'CONFIRMED' | 'CANCELED' | 'EXPIRED'  // 更新为后端定义的状态值
  token?: string
  userInfo?: UserInfo
}

export const authApi = {
  // 账号密码登录
  loginByPassword: async (params: PasswordLoginRequest) => {
    const config: RequestConfig = {
      url: '/auth/login/password',
      method: 'POST',
      data: params
    }
    return request<LoginResponse>(config)
  },

  // 短信验证码登录
  loginBySms: async (params: SmsLoginRequest) => {
    const config: RequestConfig = {
      url: '/auth/login/sms',
      method: 'POST',
      data: params
    }
    return request<LoginResponse>(config)
  },

  // 发送短信验证码
  sendSmsCode: async (params: SendSmsCodeRequest) => {
    const config: RequestConfig = {
      url: '/auth/sms/send',
      method: 'POST',
      data: params
    }
    return request<void>(config)
  },

  // 获取微信登录二维码
  getWechatQrCode: async () => {
    const config: RequestConfig = {
      url: '/auth/wechat/qrcode',
      method: 'GET'
    }
    return request<WechatQrCodeResponse>(config)
  },

  // 检查微信扫码状态
  checkWechatLogin: async (qrCodeId: string) => {
    const config: RequestConfig = {
      url: '/auth/wechat/check',
      method: 'GET',
      params: { qrCodeId }
    }
    return request<WechatLoginStatusResponse>(config)
  },

  // 获取当前用户信息
  getCurrentUser: async () => {
    const config: RequestConfig = {
      url: '/auth/user/info',
      method: 'GET'
    }
    return request<UserInfo>(config)
  },

  // 退出登录
  logout: async () => {
    const config: RequestConfig = {
      url: '/auth/logout',
      method: 'POST'
    }
    return request<void>(config)
  }
}