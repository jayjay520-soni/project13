<template>
  <div class="comment-manage">
    <h2>评论管理</h2>

    <!-- 搜索筛选栏 -->
    <div class="search-bar" style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center">
      <el-input v-model="searchParams.content" placeholder="搜索评论内容" clearable style="width: 240px" />

      <el-select v-model="searchParams.type" placeholder="筛选类型" clearable style="width: 160px">
        <el-option label="景区评论" value="scenic" />
        <el-option label="民宿评论" value="homestay" />
        <el-option label="餐厅评论" value="restaurant" />
      </el-select>

      <el-button type="primary" @click="search">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <div class="toolbar">
      <el-button type="danger" @click="batchDelete" :disabled="!selectedIds.length">批量删除</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="commentList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" width="100" />
      <el-table-column prop="username" label="用户名" width="120" />

      <!-- 评论类型 -->
      <el-table-column label="评论类型" width="120">
        <template #default="{ row }">
          <el-tag
              :type="
              row.type === 'scenic' ? 'primary' :
              row.type === 'homestay' ? 'success' : 'warning'
            "
          >
            {{
              row.type === 'scenic' ? '景区' :
                  row.type === 'homestay' ? '民宿' :
                      row.type === 'restaurant' ? '餐厅' : '未知'
            }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="targetId" label="对象ID" width="100" />

      <el-table-column
          label="评论内容"
          min-width="250"
          prop="content"
          show-overflow-tooltip
      />

      <el-table-column label="评分" width="100">
        <template #default="{ row }">
          <el-rate v-model="row.score" disabled />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />

      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleView(row)">
            查看
          </el-button>
          <el-button type="danger" link size="small" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        @size-change="getCommentList"
        @current-change="getCommentList"
        layout="total, prev, pager, next, jumper, ->, sizes"
        style="margin-top:20px;text-align:right"
    />

    <!-- 详情弹窗 -->
    <el-dialog v-model="viewDialogVisible" title="评论详情" width="50%">
      <el-descriptions :column="1" border v-if="viewForm">
        <el-descriptions-item label="ID">{{ viewForm.id }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ viewForm.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ viewForm.username || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="评论类型">
          <el-tag
              :type="
              viewForm.type === 'scenic' ? 'primary' :
              viewForm.type === 'homestay' ? 'success' : 'warning'
            "
          >
            {{
              viewForm.type === 'scenic' ? '景区' :
                  viewForm.type === 'homestay' ? '民宿' :
                      viewForm.type === 'restaurant' ? '餐厅' : '未知'
            }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="评论对象ID">{{ viewForm.targetId }}</el-descriptions-item>
        <el-descriptions-item label="评分">
          <el-rate v-model="viewForm.score" disabled />
        </el-descriptions-item>
        <el-descriptions-item label="评论内容">
          <div style="white-space: pre-wrap; line-height: 1.6">{{ viewForm.content || '暂无' }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewForm.createTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const commentList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedIds = ref([])
const loading = ref(false)

const viewDialogVisible = ref(false)
const viewForm = ref({})

const searchParams = ref({
  content: '',
  type: ''
})

// 获取请求头（统一带上 user）
const getHeaders = () => {
  const userStr = localStorage.getItem('user')
  return {
    user: userStr
  }
}

// 获取评论列表
const getCommentList = async () => {
  loading.value = true
  const params = {
    page: currentPage.value,
    size: pageSize.value,
    content: searchParams.value.content || undefined,
    type: searchParams.value.type || undefined
  }
  try {
    const res = await axios.get('/admin/comment/list', { params, headers: getHeaders() })
    if (res.data.code === 1) {
      const data = res.data.data || {}
      commentList.value = data.records || []
      total.value = data.total || 0
    } else {
      ElMessage.error(res.data.message || '加载评论失败')
    }
  } catch (e) {
    ElMessage.error('加载评论失败')
    console.error(e)
  } finally {
    loading.value = false
  }
}

const search = () => {
  currentPage.value = 1
  getCommentList()
}

const resetSearch = () => {
  searchParams.value = { content: '', type: '' }
  currentPage.value = 1
  getCommentList()
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleView = (row) => {
  viewForm.value = { ...row }
  viewDialogVisible.value = true
}

// 单个删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该评论？删除后不可恢复。', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await axios.delete(`/admin/comment/delete/${row.id}`, {
      headers: getHeaders()
    })
    if (res.data.code === 1) {
      ElMessage.success('删除成功')
      getCommentList()
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const batchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条评论？`, '批量删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    let successCount = 0
    for (const id of selectedIds.value) {
      try {
        await axios.delete(`/admin/comment/delete/${id}`, {
          headers: getHeaders()
        })
        successCount++
      } catch (e) {
        console.error(`删除评论${id}失败`, e)
      }
    }
    ElMessage.success(`成功删除 ${successCount} 条评论`)
    selectedIds.value = []
    getCommentList()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

onMounted(() => {
  getCommentList()
})
</script>

<style scoped>
.comment-manage {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.toolbar {
  margin-bottom: 16px;
}

h2 {
  margin: 0 0 20px 0;
  font-size: 20px;
  color: #303133;
}

.search-bar {
  flex-wrap: wrap;
}
</style>
