<template>
  <div class="user-manage">
    <h2>用户管理</h2>

    <!-- 搜索 + 状态筛选栏 -->
    <div class="search-bar" style="margin-bottom: 20px; display: flex; gap: 10px;">
      <el-input
          v-model="searchUsername"
          placeholder="搜索用户名"
          style="width: 250px"
          @keyup.enter="getUserList"
      />

      <el-select v-model="searchStatus" placeholder="状态筛选" style="width: 150px">
        <el-option label="全部" value="" />
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>

      <el-button type="primary" @click="getUserList">搜索</el-button>
    </div>

    <!-- 用户表格 -->
    <el-table :data="userList" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="role" label="角色" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.createTime) }}
        </template>
      </el-table-column>
      <!-- 新增：最近登录时间列 -->
      <el-table-column prop="lastLoginTime" label="最近登录时间" width="180">
        <template #default="{ row }">
          {{ row.lastLoginTime ? formatTime(row.lastLoginTime) : '未登录' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              active-text="启用"
              inactive-text="禁用"
              @change="changeStatus(row)"
          />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const userList = ref([])
const searchUsername = ref('')
const searchStatus = ref('')

// 格式化时间为 YYYY-MM-DD HH:mm:ss
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  const second = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

// 获取用户列表
const getUserList = async () => {
  const res = await axios.get('http://localhost:8080/admin/users', {
    params: {
      username: searchUsername.value,
      status: searchStatus.value
    }
  })
  if (res.data.code === 1) {
    userList.value = res.data.data
  }
}

// 切换用户状态
const changeStatus = async (row) => {
  const res = await axios.post('http://localhost:8080/admin/user/status', {
    id: row.id,
    status: row.status
  })
  if (res.data.code === 1) {
    ElMessage.success(res.data.msg)
    getUserList()
  } else {
    ElMessage.error(res.data.msg)
    row.status = row.status === 1 ? 0 : 1
  }
}

onMounted(() => {
  getUserList()
})
</script>

<style scoped>
.user-manage {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}
h2 {
  margin-bottom: 20px;
}
</style>
