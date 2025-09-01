import axios from 'axios'
import type { AxiosError, AxiosInstance, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

export interface ApiResponse<T = any> {
  code: number
  data: T
  msg: string
}

// 创建真实 API 客户端实例
export const apiClient: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})



// 移除mock数据客户端实例

// 请求拦截器
const setupRequestInterceptor = (client: AxiosInstance) => {
  client.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('userToken')
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`
      }
      return config
    },
    (error) => Promise.reject(error)
  )
}

// 响应拦截器
const setupResponseInterceptor = (client: AxiosInstance) => {
  client.interceptors.response.use(
    (response: AxiosResponse<ApiResponse>) => {
      const res = response.data

      // 非标准返回
      if (res.code === undefined) return response

      // 业务成功
      if (res.code === 200) {
        return res.data
      }

      // 业务失败
      ElMessage.error(res.msg || '操作失败')
      return Promise.reject(new Error(res.msg || '操作失败'))
    },
    (error: AxiosError<ApiResponse>) => {
      if (error.response) {
        const { status, data } = error.response

        switch (status) {
          case 401:
            localStorage.removeItem('userToken')
            localStorage.removeItem('userInfo')
            router.push('/auth/login')
            ElMessage.error('登录已过期，请重新登录')
            break
          case 403:
            ElMessage.error('没有权限进行此操作')
            break
          case 404:
            ElMessage.error('请求的资源不存在')
            break
          case 500:
            ElMessage.error('服务器错误，请稍后重试')
            break
          default:
            ElMessage.error(data?.msg || '操作失败，请重试')
        }
      } else if (error.request) {
        ElMessage.error('网络错误，请检查网络连接')
      } else {
        ElMessage.error('请求配置错误')
      }

      return Promise.reject(error)
    }
  )
}

// 配置拦截器
setupRequestInterceptor(apiClient)
setupResponseInterceptor(apiClient)

// 统一请求方法
export interface RequestConfig {
  url: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  data?: any
  params?: any
}

export async function request<T>(config: RequestConfig): Promise<T> {
  const { url, method = 'GET', data, params } = config

  try {
    const response = await apiClient.request({
      url,
      method,
      data,
      params
    })
    return response as T
  } catch (error) {
    throw error
  }
}

// 统一导出
export default {
  apiClient,
  request
}
