# 一体式旅游管理系统

## 一、项目简介

本项目是一个基于前后端分离架构的一体式旅游管理平台，面向普通用户和管理员两类角色。普通用户可以浏览热门景区、精品民宿和特色餐厅信息，查看详情；管理员可以对用户、景区、民宿、餐厅等核心数据进行增删改查管理。系统采用 JWT 进行身份认证，Spring Security 保障接口安全，整体功能完整，界面简洁友好。

### 主要功能模块

| 模块 | 说明 |
|------|------|
| 用户认证 | 登录 / 注册 / 角色区分（普通用户 / 管理员）/ JWT Token 鉴权 |
| 用户端首页 | 分类切换（餐厅 / 民宿 / 景区）、卡片列表、分页、详情弹窗 |
| 用户管理（管理员） | 用户列表查询、启用/禁用账号 |
| 景区管理（管理员） | 景区信息的增删改查、分页、条件筛选 |
| 民宿管理（管理员） | 民宿信息的增删改查、分页、条件筛选 |
| 餐厅管理（管理员） | 餐厅信息的增删改查、分页、条件筛选 |

---

## 二、技术栈

### 后端技术栈（Spring Boot）

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.2.0 | 核心 Web 框架 |
| MyBatis | - | ORM 持久层框架 |
| MyBatis-Plus | 3.5.3 | MyBatis 增强工具，简化 CRUD |
| Spring Security | - | 安全认证与权限控制 |
| JJWT | 0.11.5 | JWT Token 生成与解析 |
| MySQL Connector | 8.x | MySQL 数据库驱动 |
| Lombok | - | 简化 POJO 代码（@Data 等） |

### 前端技术栈（Vue 3）

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5.39 | 渐进式前端框架 |
| Vite | 8.1.1 | 前端构建工具 |
| Vue Router | 5.2.0 | 路由管理（含路由守卫鉴权） |
| Element Plus | 2.14.3 | UI 组件库 |
| @element-plus/icons-vue | 2.3.2 | Element Plus 图标库 |
| Pinia | 4.0.2 | 状态管理（已集成，可按需使用） |
| Axios | 1.18.1 | HTTP 请求库 |
| Sass | 1.102.0 | CSS 预处理器 |

---

## 三、项目结构

```
TravelManage/
├── db/
│   └── travel_manage_backup.sql      # 数据库备份脚本（含初始测试数据）
├── src/main/java/com/example/travelmanage/
│   ├── common/                       # 通用类：统一返回结果、全局异常处理
│   ├── config/                       # 配置类：CORS 跨域、Security 安全配置
│   ├── controller/                   # 控制层：User/Admin/Scenic/Homestay/Restaurant
│   ├── dto/                          # 数据传输对象
│   ├── entity/                       # 实体类：User/ScenicSpot/Homestay/Restaurant
│   ├── mapper/                       # MyBatis Mapper 接口
│   ├── service/                      # 业务层接口与实现
│   ├── utils/                        # 工具类：JWT 工具、日期工具
│   └── TravelManageApplication.java  # Spring Boot 启动类
├── src/main/resources/
│   ├── application.yml               # 主配置（数据库、端口）
│   └── application.properties        # 备用配置
├── travel-frontend/                  # Vue 3 前端项目
│   ├── src/
│   │   ├── views/
│   │   │   ├── Login.vue / Register.vue    # 登录/注册页
│   │   │   ├── user/Home.vue               # 用户端首页
│   │   │   └── admin/                      # 管理员后台（含侧边栏布局）
│   │   │       ├── Home.vue                # 管理员布局（菜单+路由出口）
│   │   │       ├── UserManage.vue          # 用户管理
│   │   │       ├── ScenicManage.vue        # 景区管理
│   │   │       ├── HomestayManage.vue      # 民宿管理
│   │   │       └── RestaurantManage.vue    # 餐厅管理
│   │   ├── router/index.js           # 路由配置（含鉴权守卫）
│   │   ├── App.vue
│   │   └── main.js
│   ├── vite.config.js                # Vite 配置（含后端接口代理）
│   └── package.json
└── pom.xml                           # Maven 依赖配置
```

---

## 四、运行步骤

### 环境要求

| 软件 | 推荐版本 |
|------|----------|
| JDK | 17+（Spring Boot 3.x 要求） |
| Maven | 3.6+ |
| Node.js | 16+ |
| MySQL | 8.0+ |

---

### 步骤 1：导入数据库

1. 在 MySQL 中创建数据库：

```sql
CREATE DATABASE travel_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 导入备份脚本（位于 `TravelManage/db/travel_manage_backup.sql`）：

```bash
mysql -uroot -p123456 travel_manage < TravelManage/db/travel_manage_backup.sql
```

> 备份脚本已包含 `user`、`scenic_spot`、`homestay`、`restaurant` 四张表及测试数据。

---

### 步骤 2：启动后端服务

1. 进入项目根目录：

```bash
cd TravelManage
```

2. 如有需要，修改数据库连接配置（`src/main/resources/application.yml`）：

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

3. 使用 Maven 启动：

```bash
./mvnw spring-boot:run
```

或者在 IDE 中直接运行 [TravelManageApplication.java](file:///workspace/TravelManage/src/main/java/com/example/travelmanage/TravelManageApplication.java)。

启动成功后，后端服务监听 `http://localhost:8080`。

---

### 步骤 3：启动前端服务

1. 进入前端目录：

```bash
cd TravelManage/travel-frontend
```

2. 安装依赖：

```bash
npm install
```

3. 启动开发服务器：

```bash
npm run dev
```

Vite 启动后默认地址为 `http://localhost:5173`。

> 前端已在 [vite.config.js](file:///workspace/TravelManage/travel-frontend/vite.config.js) 中配置了接口代理，`/admin/xxx` 和 `/api/xxx` 的请求会自动转发到 `http://localhost:8080`，无需额外处理跨域。

---

### 步骤 4：访问系统

打开浏览器访问 `http://localhost:5173`，使用下面的测试账号登录即可。

---

## 五、测试账号

数据库中已预置以下账号（密码均为 BCrypt 加密存储）：

### 管理员账号

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| `admin` | `123456` | admin | 超级管理员，可进入后台管理 |

### 普通用户账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| `111` | `123456` | user |
| `222` | `123456` | user |
| `333` | `123456` | user |
| `444` | `123456` | user |
| `testuser01` | `123456` | user |

> 若上述密码验证不通过，可通过 **注册页面** 自行创建新账号进行测试（注册时身份选择「普通用户」或「管理员」即可）。

---

## 六、接口一览

### 认证接口（前缀 `/api/auth`）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 登录（返回 JWT Token） |
| POST | `/api/auth/register` | 注册新用户 |

### 管理员接口（前缀 `/admin`，需 admin 角色）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET    | `/admin/users` | 查询用户列表（支持 username/status 筛选） |
| POST   | `/admin/user/status` | 更新用户状态（启用/禁用） |
| GET    | `/admin/scenic/list` | 景区分页列表（支持 name/region/level 筛选） |
| POST   | `/admin/scenic/add` | 新增景区 |
| POST   | `/admin/scenic/update` | 修改景区 |
| DELETE | `/admin/scenic/delete/{id}` | 删除景区 |
| GET    | `/admin/homestay/list` | 民宿分页列表（支持 name/region/type 筛选） |
| POST   | `/admin/homestay/add` | 新增民宿 |
| POST   | `/admin/homestay/update` | 修改民宿 |
| DELETE | `/admin/homestay/delete/{id}` | 删除民宿 |
| GET    | `/admin/restaurant/list` | 餐厅分页列表（支持 name/region/type 筛选） |
| POST   | `/admin/restaurant/add` | 新增餐厅 |
| POST   | `/admin/restaurant/update` | 修改餐厅 |
| DELETE | `/admin/restaurant/delete/{id}` | 删除餐厅 |

---

## 七、分工建议

项目为典型的小组实训项目，可按 **后端 / 前端 / 数据库** 三个维度进行分工，参考如下：

| 角色 | 负责内容 | 涉及文件/模块 |
|------|----------|---------------|
| **组员 A - 后端开发** | 用户认证、JWT、权限控制；用户管理、景区/民宿/餐厅 CRUD 接口开发；统一返回结构、异常处理；CORS 与 Security 配置 | [controller/](file:///workspace/TravelManage/src/main/java/com/example/travelmanage/controller)、[service/](file:///workspace/TravelManage/src/main/java/com/example/travelmanage/service)、[mapper/](file:///workspace/TravelManage/src/main/java/com/example/travelmanage/mapper)、[utils/JwtUtil.java](file:///workspace/TravelManage/src/main/java/com/example/travelmanage/utils/JwtUtil.java)、[config/SecurityConfig.java](file:///workspace/TravelManage/src/main/java/com/example/travelmanage/config/SecurityConfig.java) |
| **组员 B - 前端开发** | 登录/注册页；用户端首页（分类切换、卡片列表、分页、详情弹窗）；管理员后台布局（侧边栏菜单）；用户/景区/民宿/餐厅 4 个管理页（表格、搜索、新增编辑对话框、删除确认）；Axios 封装与路由守卫 | [Login.vue](file:///workspace/TravelManage/travel-frontend/src/views/Login.vue)、[Register.vue](file:///workspace/TravelManage/travel-frontend/src/views/Register.vue)、[views/user/Home.vue](file:///workspace/TravelManage/travel-frontend/src/views/user/Home.vue)、[views/admin/](file:///workspace/TravelManage/travel-frontend/src/views/admin)、[router/index.js](file:///workspace/TravelManage/travel-frontend/src/router/index.js) |
| **组员 C - 数据库 & 集成测试** | 数据库表结构设计；编写并导出初始化 SQL（含测试数据）；前后端联调与接口测试；README 文档编写；项目打包部署验证 | [travel_manage_backup.sql](file:///workspace/TravelManage/db/travel_manage_backup.sql)、[application.yml](file:///workspace/TravelManage/src/main/resources/application.yml)、本 README |

> 小组成员可根据实际情况灵活调整；若人数较多，可将景区/民宿/餐厅的前后端进一步细分给不同组员独立完成。

---

## 八、常见问题

1. **登录报 403 或 身份选择错误？**
   请确认登录时选择的身份（普通用户/管理员）与账号实际 `role` 字段一致。`admin` 账号必须选择「管理员」登录。

2. **前端启动后接口 404？**
   确认后端已启动（`http://localhost:8080`），Vite 代理配置只匹配 `/admin/xxx/` 格式，路径需规范。

3. **数据库连接失败？**
   检查 MySQL 服务是否启动，`application.yml` 中的 `username/password` 是否与本地环境一致。

4. **密码不正确？**
   可使用注册功能新建账号；或直接修改数据库中 `user` 表的 `password` 字段，通过后端 `PasswordEncoder` 生成 BCrypt 密文后替换。
