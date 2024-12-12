# 订单管理系统

一个基于Spring Boot的微服务订单管理系统，提供订单处理、库存管理、支付集成和物流跟踪等功能。

## 功能特性

### 订单管理
- 订单创建和状态管理
- 订单支付集成(支付宝、微信)
- 订单超时自动取消
- 订单历史查询

### 库存管理
- 实时库存监控
- 库存锁定机制
- 库存预警
- 自动补货提醒

### 物流跟踪
- 物流状态实时更新
- 物流轨迹查询
- 多物流商支持
- 自动物流信息同步

### 支付处理
- 支付宝支付集成
- 微信支付集成
- 支付状态实时通知
- 退款处理

## 技术栈

### 后端
- Spring Boot 2.7.x
- Spring Cloud
- MySQL 5.7
- Redis
- RabbitMQ
- Maven

### 前端
- Vue.js 3
- Element Plus
- Axios
- Vite

### 运维
- Docker
- Jenkins
- ELK Stack
- Prometheus + Grafana

## 快速开始

### 环境要求
- JDK 11+
- Maven 3.6+
- MySQL 5.7+
- Redis 6.x
- Docker & Docker Compose

### 本地开发

1. 克隆项目