<template>
  <div class="admin-layout">
    <!-- 左侧菜单栏 -->
    <aside class="sidebar">
      <h2 class="logo">一体式旅游管理平台</h2>
      <el-menu
          :default-active="activeMenu"
      class="menu"
      router
      >
        <el-menu-item index="/admin/user">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/scenic">
          <el-icon><Location /></el-icon>
          <span>景区管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/homestay">
          <el-icon><House /></el-icon>
          <span>民宿管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/restaurant">
          <el-icon><Dish /></el-icon>
          <span>餐厅管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/comment">
          <el-icon><ChatRound /></el-icon>
          <span>评论管理</span>
        </el-menu-item>
      </el-menu>
    </aside>

    <!-- 右侧内容区 -->
    <main class="content">
      <header class="header">
        <span class="title">管理员后台</span>
        <div class="user-info">
          <el-avatar size="small" :src="''" />
          <span>{{ username }}</span>
          <!-- 新增：退出登录按钮 -->
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </header>
      <div class="page-content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Location, House, Dish, ChatRound } from '@element-plus/icons-vue'

const router = useRouter()

// 安全获取用户名
const username = computed(() => {
  return localStorage?.getItem('username') || 'admin'
})

// 退出登录逻辑
const handleLogout = () => {
  ElMessage.success('退出登录成功')
  // 清空本地存储的登录信息
  localStorage.clear()
  // 跳转到登录页
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
}
.sidebar {
  width: 220px;
  background: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}
.logo {
  padding: 20px;
  font-size: 16px;
  color: #409eff;
  text-align: center;
}
.menu {
  border: none;
}
.content {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.header {
  height: 60px;
  padding: 0 20px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 20px;
  font-weight: bold;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
.page-content {
  flex: 1;
  padding: 20px;
  background: #f0f2f5;
  overflow-y: auto;
}
</style>
