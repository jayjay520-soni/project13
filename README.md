# 一体式旅游管理系统

## 一、项目简介
本项目是一个基于前后端分离架构的一体式旅游管理平台，面向普通用户和管理员两类角色。 普通用户可以浏览热门景区、精品民宿和特色餐厅信息，查看详情；管理员可以对用户、景区、民宿、餐厅等核心数据进行增删改查管理。系统采用 JWT 进行身份认证，Spring Security 保障接口安全，整体功能完整，界面简洁友好。
### 主要功能模块

| 模块             | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| 用户认证         | 登录 / 注册 / 角色区分（普通用户 / 管理员）/ JWT Token 鉴权   |
| 用户端首页       | 分类切换（餐厅 / 民宿 / 景区）、卡片列表、分页、详情弹窗      |
| 用户管理（管理员）| 用户列表查询、启用/禁用账号                                  |
| 景区管理（管理员）| 景区信息的增删改查、分页、条件筛选                           |
| 民宿管理（管理员）| 民宿信息的增删改查、分页、条件筛选                           |
| 餐厅管理（管理员）| 餐厅信息的增删改查、分页、条件筛选                           |


## 二、技术栈

### 后端技术栈（Spring Boot）

| 技术              | 版本    | 用途                                       |
| ----------------- | ------- | ------------------------------------------ |
| Spring Boot       | 3.2.0   | 核心 Web 框架                              |
| MyBatis           | -       | ORM 持久层框架                             |
| MyBatis-Plus      | 3.5.3   | MyBatis 增强工具，简化 CRUD                |
| Spring Security   | -       | 安全认证与权限控制                         |
| JJWT              | 0.11.5  | JWT Token 生成与解析                       |
| MySQL Connector   | 8.x     | MySQL 数据库驱动                           |
| Lombok            | -       | 简化 POJO 代码（@Data 等）|

### 前端技术栈（Vue 3）
| 技术                      | 版本     | 用途                                   |
| ------------------------- | -------- | -------------------------------------- |
| Vue                       | 3.5.39   | 渐进式前端框架                         |
| Vite                      | 8.1.1    | 前端构建工具                           |
| Vue Router                | 5.2.0    | 路由管理（含路由守卫鉴权）|
| Element Plus              | 2.14.3   | UI 组件库                              |
| @element-plus/icons-vue   | 2.3.2    | Element Plus 图标库                    |
| Pinia                     | 4.0.2    | 状态管理（已集成，可按需使用）|
| Axios                     | 1.18.1   | HTTP 请求库                            |
| Sass                      | 1.102.0  | CSS 预处理器                           |

## 三、运行步骤
### 环境要求
| 软件     | 推荐版本                          |
| -------- | --------------------------------- |
| JDK      | 17+（Spring Boot 3.x 要求）|
| Maven    | 3.6+                              |
| Node.js  | 16+                               |
| MySQL    | 8.0+                              |

### 步骤 1：导入数据库
在 MySQL 中创建数据库：

```sql
CREATE DATABASE travel_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```
导入备份脚本（位于 TravelManage/db/travel_manage_backup.sql）：

```bash
mysql -uroot -p123456 travel_manage < TravelManage/db/travel_manage_backup.sql
```
备份脚本已包含 user、scenic_spot、homestay、restaurant 四张表及测试数据。

### 步骤 2：启动后端服务

进入项目根目录：
```bash
cd TravelManage
```
如有需要，修改数据库连接配置（src/main/resources/application.yml）：
```yaml
spring:
datasource:
url: jdbc:mysql://localhost:3306/travel_manage?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
username: root
password: 123456   # 改成你的 MySQL 密码
driver-class-name: com.mysql.cj.jdbc.Driver

server:
port: 8080
```

使用 Maven 启动：
```bash
./mvnw spring-boot:run
```
或者在 IDE 中直接运行 TravelManageApplication.java。
启动成功后，后端服务监听 http://localhost:8080 。

### 步骤 3：启动前端服务
进入前端目录：
```bash
cd TravelManage/travel-frontend
```

安装依赖：
```bash
npm install
```

启动开发服务器：
```bash
npm run dev
```
Vite 启动后默认地址为 http://localhost:5173 。
前端已在 vite.config.js 中配置了接口代理，/admin/xxx 和 /api/xxx 的请求会自动转发到 http://localhost:8080 ，无需额外处理跨域。

### 步骤 4：访问系统
打开浏览器访问 http://localhost:5173 ，使用下面的测试账号登录即可。

## 四、测试账号
数据库中已预置以下账号（密码均为 BCrypt 加密存储）：
### 管理员账号
| 用户名 | 密码   | 角色  | 说明                                   |
| ------ | ------ | ----- | -------------------------------------- |
| admin  | 123456 | admin | 超级管理员，可登录后台执行全部数据管理 |
### 普通用户账号
| 用户名     | 密码 | 角色 |
| ---------- |----| ---- |
| 111        | 111111 | user |
| 222        | 222222 | user |
| 333        | 333333 | user |
| 444        | 444444 | user |

## 五、分工

| 组员   | 负责内容                                                                                                                                       |
|------|--------------------------------------------------------------------------------------------------------------------------------------------|
| 陈奕涵  | 用户认证、JWT、权限控制；用户管理、景区/民宿/餐厅 CRUD 接口开发；统一返回结构、异常处理；CORS 与 Security 配置； 登录/注册页；管理员后台布局（侧边栏菜单）；用户管理、景区管理、餐厅管理、民宿管理页面；Axios 封装与路由守卫 ；数据库表结构设计； | 
| 欧阳艳  | 用户端首页（分类切换、卡片列表、分页、详情弹窗）；各阶段文档整理输出；前后端联调、接口测试；README文档撰写；项目打包部署验证。                                                                         | 

