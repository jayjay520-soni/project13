<template>
  <div class="login-container">
    <el-card class="login-box" shadow="hover">
      <h2 class="login-title">旅游管理系统登录</h2>
      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password clearable />
        </el-form-item>
        <el-form-item label="身份" prop="role">
          <el-radio-group v-model="loginForm.role">
            <el-radio label="user">普通用户</el-radio>
            <el-radio label="admin">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item class="btn-group">
          <el-button type="primary" @click="handleLogin" :loading="loading" class="login-btn">登录</el-button>
          <el-button @click="$router.push('/register')" class="reg-btn">去注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const loading = ref(false)
const loginFormRef = ref()

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  role: 'user'
})

// 表单校验规则
const loginRules = {
  username: [
    { required: true, message: '请填写信息', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请填写信息', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择身份', trigger: 'change' }
  ]
}

// 请求拦截器（自动带 Token）
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

// 登录请求处理
const handleLogin = async () => {
  try {
    await loginFormRef.value.validate()
    loading.value = true

    const res = await axios.post('http://localhost:8080/api/auth/login', loginForm)

    if (res.data.code === 1) {
      ElMessage.success('登录成功')
      localStorage.clear()

      // 存储 Token、角色、用户名
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userRole', res.data.role)
      localStorage.setItem('username', loginForm.username)

      // ✅ 关键：组装完整 user 对象并存储
      const userInfo = {
        id: res.data.userId, // 后端必须返回 userId
        username: loginForm.username,
        role: res.data.role
      }
      localStorage.setItem('user', JSON.stringify(userInfo))

      // 根据角色跳转
      if (res.data.role === 'admin') {
        router.push('/admin')
      } else {
        router.push('/user')
      }
    } else {
      ElMessage.error('用户名或密码错误')
    }
  } catch (err) {
    console.error(err)
    if (err.message.includes('Network Error')) {
      ElMessage.error('网络异常，请稍后再试')
    } else {
      ElMessage.error('登录失败，请检查账号密码')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}
.login-box {
  width: 420px;
  padding: 30px;
}
.login-title {
  text-align: center;
  margin-bottom: 25px;
  color: #409eff;
}
.btn-group {
  display: flex;
  gap: 10px;
}
.login-btn {
  flex: 1;
}
.reg-btn {
  flex: 1;
}
</style>