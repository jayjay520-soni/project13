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
      <el-button type="danger" @click="batchDelete" :disabled="!selectedIds.length">删除</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="commentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" width="100" />

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

      <el-table-column
          label="评论内容"
          min-width="250"
          prop="content"
          show-overflow-tooltip
      />

      <el-table-column prop="score" label="评分" width="80" />
      <el-table-column prop="createTime" label="创建时间" width="180" />

      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row }">
          <el-button type="text" size="small" @click="handleView(row)">
            <el-icon><View /></el-icon>
          </el-button>
          <el-button type="text" size="small" @click="handleDelete(row)">
            <el-icon><Delete /></el-icon>
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
      <el-descriptions :column="1" border>
        <el-descriptions-item label="ID">{{ viewForm?.id }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ viewForm?.userId }}</el-descriptions-item>
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
        <el-descriptions-item label="评论内容">{{ viewForm?.content || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="评分">{{ viewForm?.score }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewForm?.createTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import axios from 'axios'
import {ElMessage, ElMessageBox} from 'element-plus'
import {View, Delete} from '@element-plus/icons-vue'

const commentList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedIds = ref([])

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
  const params = {
    page: currentPage.value,
    size: pageSize.value,
    content: searchParams.value.content,
    type: searchParams.value.type
  }
  try {
    const res = await axios.get('/admin/comment/list', {params, headers: getHeaders()})
    const data = res.data.data || {}
    commentList.value = data.records || []
    total.value = data.total || 0
  } catch (e) {
    ElMessage.error('加载评论失败')
    console.error(e)
  }
}

const search = () => {
  currentPage.value = 1
  getCommentList()
}

const resetSearch = () => {
  searchParams.value = {content: '', type: ''}
  currentPage.value = 1
  getCommentList()
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleView = (row) => {
  viewForm.value = {...row}
  viewDialogVisible.value = true
}

// 单个删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该评论？')
    await axios.delete(`/admin/comment/delete/${row.id}`, {
      headers: getHeaders()
    })
    ElMessage.success('删除成功')
    getCommentList()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const batchDelete = async () => {
  try {
    await ElMessageBox.confirm('确定删除选中项？')
    for (const id of selectedIds.value) {
      await axios.delete(`/admin/comment/delete/${id}`, {
        headers: getHeaders()
      })
    }
    ElMessage.success('批量删除成功')
    selectedIds.value = []
    getCommentList()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
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
  padding: 20px;
  border-radius: 4px;
}

.toolbar {
  margin-bottom: 20px;
}

h2 {
  margin-bottom: 20px;
}
</style>