基于该项目的代码分析，我为您生成一个全面的 README.md 文档，展示财云学校财务管理系统的技术栈和架构。<cite/>

# 财云学校财务管理系统 (CaiYun School Finance System)

> 仿照广财的财务管理系统，提供学生缴费、财务管理等功能的现代化Web应用

###### !!!注意事项

但前端中的登陆界面 已接入后端的接口

**前端项目现阶段其他页面使用静态数据进行展示 并未接入后端接口模式**



## 🏗️ 项目架构

该项目采用前后端分离架构，包含以下主要模块：

```
caiyun-school-finance-system/
├── caiyun-fronted/          # Vue.js 前端应用
├── caiyun-web/              # Spring Boot 后端服务
│   ├── caiyun-framework/    # 框架层
│   └── caiyun-module/       # 业务模块
```

## 🎯 核心功能

- **服务大厅**: 主要服务入口和导航
- **缴费大厅**: 学费、住宿费等各类费用缴纳
- **应用中心**: 各类应用服务集成
- **统一认证**: 用户登录和权限管理

## 🛠️ 技术栈

### 前端技术栈

| 技术组件 | 版本 | 用途 |
|---------|------|------|
| ![Vue.js](https://img.shields.io/badge/Vue.js-3.5.18-4FC08D?style=flat&logo=vue.js) | 3.5.18 | 核心前端框架 |
| ![TypeScript](https://img.shields.io/badge/TypeScript-5.8.3-3178C6?style=flat&logo=typescript) | 5.8.3 | 类型安全开发 |
| ![Vite](https://img.shields.io/badge/Vite-7.1.2-646CFF?style=flat&logo=vite) | 7.1.2 | 构建工具和开发服务器 |
| ![Vue Router](https://img.shields.io/badge/Vue_Router-4.5.1-4FC08D?style=flat) | 4.5.1 | 客户端路由 |
| ![Pinia](https://img.shields.io/badge/Pinia-3.0.3-FFD859?style=flat) | 3.0.3 | 状态管理 |
| ![Element Plus](https://img.shields.io/badge/Element_Plus-2.11.1-409EFF?style=flat) | 2.11.1 | UI组件库 |
| ![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-4.1.12-06B6D4?style=flat&logo=tailwindcss) | 4.1.12 | 原子化CSS框架 | [1](#0-0) 

### 后端技术栈

| 技术组件 | 版本 | 用途 |
|---------|------|------|
| ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0.2-6DB33F?style=flat&logo=springboot) | 3.0.2 | 核心后端框架 |
| ![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=openjdk) | 17 | 开发语言 |
| ![MyBatis Plus](https://img.shields.io/badge/MyBatis_Plus-3.5.5-DC382D?style=flat) | 3.5.5 | ORM框架 |
| ![MySQL](https://img.shields.io/badge/MySQL-8.0.29-4479A1?style=flat&logo=mysql) | 8.0.29 | 关系型数据库 |
| ![Redis](https://img.shields.io/badge/Redis-Latest-DC382D?style=flat&logo=redis) | Latest | 缓存数据库 |
| ![Sa-Token](https://img.shields.io/badge/Sa--Token-1.38.0-FF6B6B?style=flat) | 1.38.0 | 权限认证框架 |
| ![Druid](https://img.shields.io/badge/Druid-1.2.23-40E0D0?style=flat) | 1.2.23 | 数据库连接池 | [2](#0-1) 

### 开发工具

| 工具 | 版本 | 用途 |
|------|------|------|
| ![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=flat&logo=apachemaven) | 3.x | 项目构建管理 |
| ![Lombok](https://img.shields.io/badge/Lombok-1.18.30-BC4521?style=flat) | 1.18.30 | 代码生成工具 |
| ![Hutool](https://img.shields.io/badge/Hutool-5.8.26-4285F4?style=flat) | 5.8.26 | Java工具类库 | [3](#0-2) 

## 🚀 快速开始

### 前端启动

```bash
cd caiyun-fronted
npm install
npm run dev
```

前端应用将在 `http://localhost:5173` 启动，

### 后端启动

```bash
cd caiyun-web
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

## 📁 项目结构

### 前端结构
```
caiyun-fronted/
├── src/
│   ├── main.ts              # 应用入口点
│   ├── App.vue              # 根组件
│   ├── pages/               # 页面组件
│   │   └── pay/             # 缴费相关页面
│   ├── store/               # Pinia状态管理
│   └── styles/              # 样式文件
├── index.html               # HTML模板
├── vite.config.ts           # Vite配置
└── tailwind.config.js       # Tailwind配置
``` [6](#0-5) 

### 后端结构
```
caiyun-web/
├── caiyun-framework/        # 框架层
│   ├── caiyun-common/       # 通用模块
│   └── caiyun-spring-boot-starter-biz-operationlog/  # 操作日志
└── caiyun-module/           # 业务模块
    └── caiyun-module-system/  # 系统模块
``` [7](#0-6) 

## 🎨 UI设计

项目采用现代化的设计语言，结合Element Plus组件库和Tailwind CSS：

- **主色调**: `#1a73e8` (蓝色)
- **辅助色**: `#fbbc04` (黄色)
- **成功色**: `#34a853` (绿色)  
- **危险色**: `#ea4335` (红色) [8](#0-7) 

## 🔧 开发配置

### 数据库配置
- **数据库**: MySQL 8.0+
- **连接池**: Druid
- **ORM**: MyBatis Plus [9](#0-8) 

### 缓存配置
- **Redis**: 用于会话管理和缓存
- **Sa-Token**: 集成Redis进行权限认证 [10](#0-9) 

## 📝 Notes

该项目是一个完整的学校财务管理系统，采用现代化的技术栈构建。前端使用Vue 3 + TypeScript + Vite的组合提供良好的开发体验，后端基于Spring Boot 3构建微服务架构。项目特别注重用户体验和系统安全性，集成了完整的权限认证体系和操作日志记录功能。

