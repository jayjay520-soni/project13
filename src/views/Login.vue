<template>
  <div class="login-page">
    <div class="login-card">
      <h2>旅游管理系统</h2>
      <el-form :model="loginForm" class="login-form">
        <el-form-item>
          <el-input v-model="loginForm.username" placeholder="请输入账号" clearable />
        </el-form-item>
        <el-form-item>
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
// 如果你用了路由，导入 router 并在登录成功后跳转
// import { useRouter } from 'vue-router'
// const router = useRouter()

const loginForm = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  try {
    const res = await axios.post('http://localhost:8080/user/login', loginForm.value)
    if (res.data.code === 200) {
      ElMessage.success('登录成功')
      // 登录成功后跳转到首页
      // router.push('/home')
    } else {
      ElMessage.error(res.data.message || '登录失败')
    }
  } catch (err) {
    ElMessage.error('网络或服务器异常')
  }
}
</script>

<style scoped>
.login-page {
  width: 100vw;
  height: 100vh;
  background: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-card {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
.login-card h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.login-btn {
  width: 100%;
}
</style>