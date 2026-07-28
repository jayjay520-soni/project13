<template>
  <div class="scenic-manage">
    <h2>景区管理</h2>

    <!-- 🔍 搜索 + 筛选栏 -->
    <div class="search-bar" style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center">
      <el-input v-model="searchParams.name" placeholder="搜索景点名称" clearable style="width: 240px" />

      <el-select v-model="searchParams.region" placeholder="按地区筛选" clearable style="width: 160px">
        <el-option label="北京" value="北京" />
        <el-option label="上海" value="上海" />
        <el-option label="成都" value="成都" />
        <el-option label="广州" value="广州" />
        <el-option label="长沙" value="长沙" />
        <el-option label="泉州" value="泉州" />
      </el-select>

      <el-select v-model="searchParams.level" placeholder="按景区等级筛选" clearable style="width: 160px">
        <el-option label="5A景区" value="5A景区" />
        <el-option label="4A景区" value="4A景区" />
        <el-option label="3A景区" value="3A景区" />
      </el-select>

      <el-button type="primary" @click="getScenicList">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">新增</el-button>
      <el-button type="danger" @click="batchDelete" :disabled="!selectedIds.length">删除</el-button>
    </div>

    <el-table :data="scenicList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="name" label="景点名称" />
      <el-table-column prop="price" label="票价" width="80" />

      <el-table-column label="所在地" min-width="180">
        <template #default="{ row }">
          <span>
            {{ row.address
              ? (row.address.length > 15 ? row.address.slice(0,15)+'...' : row.address)
              : '暂无'
            }}
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="region" label="地区" />
      <el-table-column prop="popularity" label="热度" width="80" />
      <el-table-column prop="level" label="等级" width="100" />
      <el-table-column prop="visitCount" label="游量" width="80" />

      <el-table-column label="文化历史" min-width="200">
        <template #default="{ row }">
          <span>
            {{ row.history
              ? (row.history.length > 20 ? row.history.slice(0,20)+'...' : row.history)
              : '暂无'
            }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="图片" width="100">
        <template #default="{ row }">
          <el-image :src="row.imageUrl" style="width: 50px; height: 50px" fit="cover" />
        </template>
      </el-table-column>

      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row }">
          <el-button type="text" size="small" @click="handleView(row)">
            <el-icon><View /></el-icon>
          </el-button>
          <el-button type="text" size="small" @click="showEditDialog(row)">
            <el-icon><Setting /></el-icon>
          </el-button>
          <el-button type="text" size="small" @click="handleDelete(row.id)" danger>
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        @size-change="getScenicList"
        @current-change="getScenicList"
        layout="total, prev, pager, next, jumper, ->, sizes"
        style="margin-top:20px;text-align:right"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑景区' : '新增景区'">
      <el-form :model="form" label-width="100px">
        <el-form-item label="景点名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="票价">
          <el-input-number v-model="form.price" :min="0" />
        </el-form-item>
        <el-form-item label="所在地">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="地区">
          <el-input v-model="form.region" />
        </el-form-item>
        <el-form-item label="热度">
          <el-slider v-model="form.popularity" :min="0" :max="1" :step="0.01" />
        </el-form-item>
        <el-form-item label="等级">
          <el-select v-model="form.level">
            <el-option label="5A景区" value="5A景区" />
            <el-option label="4A景区" value="4A景区" />
            <el-option label="3A景区" value="3A景区" />
          </el-select>
        </el-form-item>
        <el-form-item label="图片链接">
          <el-input v-model="form.imageUrl" placeholder="输入图片URL" />
        </el-form-item>
        <el-form-item label="文化历史">
          <el-input v-model="form.history" type="textarea" :rows="3" placeholder="请输入景区文化历史介绍" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="viewDialogVisible" title="景区详情" width="50%">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="景点名称">{{ viewForm.name }}</el-descriptions-item>
        <el-descriptions-item label="票价">{{ viewForm.price }}</el-descriptions-item>
        <el-descriptions-item label="所在地">{{ viewForm.address || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="地区">{{ viewForm.region }}</el-descriptions-item>
        <el-descriptions-item label="热度">{{ viewForm.popularity }}</el-descriptions-item>
        <el-descriptions-item label="等级">{{ viewForm.level }}</el-descriptions-item>
        <el-descriptions-item label="游量">{{ viewForm.visitCount }}</el-descriptions-item>
        <el-descriptions-item label="文化历史">{{ viewForm.history || '暂无' }}</el-descriptions-item>
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
import { View, Setting } from '@element-plus/icons-vue'

const scenicList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedIds = ref([])

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  name: '',
  price: 0,
  address: '',
  region: '',
  popularity: 0.5,
  level: '',
  visitCount: 0,
  imageUrl: '',
  history: ''
})

const viewDialogVisible = ref(false)
const viewForm = ref({})

// 🔍 搜索参数
const searchParams = ref({
  name: '',
  region: '',
  level: ''
})

// 获取列表（带搜索）
const getScenicList = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      ...searchParams.value
    }
    const res = await axios.get('/admin/scenic/list', { params })

    if (res.data.code === 1) {
      // ✅ 修复这里！！！
      scenicList.value = res.data.data.records
      total.value = res.data.data.total
    }
  } catch (err) {
    ElMessage.error('加载失败：' + err.message)
  }
}

// 重置搜索
const resetSearch = () => {
  searchParams.value = { name: '', region: '', level: '' }
  currentPage.value = 1
  getScenicList()
}

// 多选
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 新增
const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    name: '', price: 0, address: '', region: '',
    popularity: 0.5, level: '', visitCount: 0, imageUrl: '', history: ''
  }
  dialogVisible.value = true
}

// 编辑
const showEditDialog = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

// 查看详情
const handleView = (row) => {
  viewForm.value = { ...row }
  viewDialogVisible.value = true
}

// 提交
const submitForm = async () => {
  try {
    const url = isEdit.value ? '/admin/scenic/update' : '/admin/scenic/add'
    const res = await axios.post(url, form.value)
    if (res.data.code === 1) {
      ElMessage.success(res.data.msg)
      dialogVisible.value = false
      getScenicList()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (err) {
    ElMessage.error('提交失败')
  }
}

// 删除单个
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
    const res = await axios.delete(`/admin/scenic/delete/${id}`)
    if (res.data.code === 1) {
      ElMessage.success('删除成功')
      getScenicList()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch {
    ElMessage.info('已取消')
  }
}

// 批量删除
const batchDelete = async () => {
  try {
    await ElMessageBox.confirm('确定删除选中项？')
    for (const id of selectedIds.value) {
      await axios.delete(`/admin/scenic/delete/${id}`)
    }
    ElMessage.success('批量删除成功')
    getScenicList()
  } catch {
    ElMessage.info('已取消')
  }
}

onMounted(() => {
  getScenicList()
})
</script>

<style scoped>
.scenic-manage {
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