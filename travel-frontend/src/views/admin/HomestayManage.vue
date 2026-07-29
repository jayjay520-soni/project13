<template>
  <div class="homestay-manage">
    <h2>民宿管理</h2>

    <!-- 搜索筛选栏 -->
    <div class="search-bar" style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center">
      <el-input v-model="searchParams.name" placeholder="搜索民宿名称" clearable style="width: 240px" />

      <el-select v-model="searchParams.region" placeholder="按地区筛选" clearable style="width: 160px">
        <el-option label="北京" value="北京" />
        <el-option label="上海" value="上海" />
        <el-option label="成都" value="成都" />
        <el-option label="广州" value="广州" />
        <el-option label="长沙" value="长沙" />
        <el-option label="泉州" value="泉州" />
      </el-select>

      <el-select v-model="searchParams.type" placeholder="按房型筛选" clearable style="width: 160px">
        <el-option label="大床房" value="大床房" />
        <el-option label="双床房" value="双床房" />
        <el-option label="亲子房" value="亲子房" />
        <el-option label="套房" value="套房" />
      </el-select>

      <el-button type="primary" @click="search">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">新增</el-button>
      <el-button type="danger" @click="batchDelete" :disabled="!selectedIds.length">删除</el-button>
    </div>

    <el-table :data="homestayList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="name" label="民宿名称" />
      <el-table-column prop="price" label="每晚价格" width="100" />

      <!-- 地址：统一风格：超长自动省略 + hover显示完整 -->
      <el-table-column
          label="地址"
          min-width="180"
          prop="address"
          show-overflow-tooltip
      />

      <el-table-column prop="region" label="地区" />
      <el-table-column prop="score" label="评分" width="80" />
      <el-table-column prop="type" label="房型类型" width="100" />
      <el-table-column prop="capacity" label="可住人数" width="80" />
      <el-table-column label="民宿介绍" min-width="200">
        <template #default="{ row }">
          <span>{{ row?.description ? (row.description.length > 20 ? row.description.slice(0,20)+'...' : row.description) : '暂无' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="图片" width="100">
        <template #default="{ row }">
          <el-image :src="row?.imageUrl" style="width: 50px; height: 50px" fit="cover" />
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
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        @size-change="getHomestayList"
        @current-change="getHomestayList"
        layout="total, prev, pager, next, jumper, ->, sizes"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑民宿' : '新增民宿'">
      <el-form :model="form" label-width="100px">
        <el-form-item label="民宿名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="每晚价格">
          <el-input-number v-model="form.price" :min="0" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="地区">
          <el-input v-model="form.region" />
        </el-form-item>
        <el-form-item label="评分">
          <el-slider v-model="form.score" :min="0" :max="5" :step="0.1" />
        </el-form-item>
        <el-form-item label="房型类型">
          <el-select v-model="form.type">
            <el-option label="大床房" value="大床房" />
            <el-option label="双床房" value="双床房" />
            <el-option label="亲子房" value="亲子房" />
            <el-option label="套房" value="套房" />
          </el-select>
        </el-form-item>
        <el-form-item label="可住人数">
          <el-input-number v-model="form.capacity" :min="1" />
        </el-form-item>
        <el-form-item label="图片链接">
          <el-input v-model="form.imageUrl" placeholder="输入图片URL" />
        </el-form-item>
        <el-form-item label="民宿介绍">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入民宿介绍" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="viewDialogVisible" title="民宿详情" width="50%">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="民宿名称">{{ viewForm?.name }}</el-descriptions-item>
        <el-descriptions-item label="每晚价格">{{ viewForm?.price }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ viewForm?.address || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="地区">{{ viewForm?.region }}</el-descriptions-item>
        <el-descriptions-item label="评分">{{ viewForm?.score }}</el-descriptions-item>
        <el-descriptions-item label="房型类型">{{ viewForm?.type }}</el-descriptions-item>
        <el-descriptions-item label="可住人数">{{ viewForm?.capacity }}</el-descriptions-item>
        <el-descriptions-item label="民宿介绍">{{ viewForm?.description || '暂无' }}</el-descriptions-item>
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
import { ElMessage } from 'element-plus'
import { View, Setting } from '@element-plus/icons-vue'

const homestayList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedIds = ref([])

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  name: '', price: 0, address: '', region: '', score: 3.0, type: '', capacity: 2, imageUrl: '', description: ''
})

const viewDialogVisible = ref(false)
const viewForm = ref({})

// 搜索条件
const searchParams = ref({
  name: '',
  region: '',
  type: ''
})

// 获取列表（带搜索）
const getHomestayList = async () => {
  const params = {
    page: currentPage.value,
    size: pageSize.value,
    ...searchParams.value
  }
  try {
    const res = await axios.get('/admin/homestay/list', { params })
    if (res.data.code === 1) {
      homestayList.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    } else {
      ElMessage.error(res.data.msg || '查询失败')
    }
  } catch (e) {
    ElMessage.error('请求异常')
    console.error(e)
  }
}

// 搜索
const search = () => {
  currentPage.value = 1
  getHomestayList()
}

// 重置
const resetSearch = () => {
  searchParams.value = { name: '', region: '', type: '' }
  currentPage.value = 1
  getHomestayList()
}

// 多选
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 新增
const showAddDialog = () => {
  isEdit.value = false
  form.value = { name: '', price: 0, address: '', region: '', score: 3.0, type: '', capacity: 2, imageUrl: '', description: '' }
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
  const url = isEdit.value ? '/admin/homestay/update' : '/admin/homestay/add'
  const res = await axios.post(url, form.value)
  if (res.data.code === 1) {
    ElMessage.success(res.data.msg)
    dialogVisible.value = false
    getHomestayList()
  } else {
    ElMessage.error(res.data.msg)
  }
}

// 批量删除
const batchDelete = async () => {
  for (const id of selectedIds.value) {
    await axios.delete(`/admin/homestay/delete/${id}`)
  }
  ElMessage.success('批量删除成功')
  getHomestayList()
}

onMounted(() => {
  getHomestayList()
})
</script>

<style scoped>
.homestay-manage {
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