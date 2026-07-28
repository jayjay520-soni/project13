<template>
  <div class="restaurant-manage">
    <h2>餐厅管理</h2>

    <!-- 搜索筛选栏 -->
    <div class="search-bar" style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center">
      <el-input v-model="searchParams.name" placeholder="搜索餐厅名称" clearable style="width: 240px" />

      <el-select v-model="searchParams.region" placeholder="按地区筛选" clearable style="width: 160px">
        <el-option label="北京" value="北京" />
        <el-option label="上海" value="上海" />
        <el-option label="成都" value="成都" />
        <el-option label="广州" value="广州" />
        <el-option label="长沙" value="长沙" />
        <el-option label="泉州" value="泉州" />
      </el-select>

      <el-select v-model="searchParams.type" placeholder="按菜系筛选" clearable style="width: 160px">
        <el-option label="川菜" value="川菜" />
        <el-option label="粤菜" value="粤菜" />
        <el-option label="湘菜" value="湘菜" />
        <el-option label="西餐" value="西餐" />
      </el-select>

      <el-button type="primary" @click="search">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">新增</el-button>
      <el-button type="danger" @click="batchDelete" :disabled="!selectedIds.length">删除</el-button>
    </div>

    <el-table :data="restaurantList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="name" label="餐厅名称" />
      <el-table-column prop="price" label="人均消费" width="100" />

      <!-- 地址：和景区管理完全一致：超长自动省略 + hover显示完整 -->
      <el-table-column
          label="地址"
          min-width="180"
          prop="address"
          show-overflow-tooltip
      />

      <el-table-column prop="region" label="地区" />
      <el-table-column prop="score" label="评分" width="80" />
      <el-table-column prop="type" label="菜系类型" width="100" />
      <el-table-column prop="sales" label="销量" width="80" />
      <el-table-column label="餐厅介绍" min-width="200">
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
        @size-change="getRestaurantList"
        @current-change="getRestaurantList"
        layout="total, prev, pager, next, jumper, ->, sizes"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑餐厅' : '新增餐厅'">
      <el-form :model="form" label-width="100px">
        <el-form-item label="餐厅名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="人均消费">
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
        <el-form-item label="菜系类型">
          <el-select v-model="form.type">
            <el-option label="川菜" value="川菜" />
            <el-option label="粤菜" value="粤菜" />
            <el-option label="湘菜" value="湘菜" />
            <el-option label="西餐" value="西餐" />
          </el-select>
        </el-form-item>
        <el-form-item label="图片链接">
          <el-input v-model="form.imageUrl" placeholder="输入图片URL" />
        </el-form-item>
        <el-form-item label="餐厅介绍">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入餐厅介绍" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="viewDialogVisible" title="餐厅详情" width="50%">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="餐厅名称">{{ viewForm?.name }}</el-descriptions-item>
        <el-descriptions-item label="人均消费">{{ viewForm?.price }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ viewForm?.address || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="地区">{{ viewForm?.region }}</el-descriptions-item>
        <el-descriptions-item label="评分">{{ viewForm?.score }}</el-descriptions-item>
        <el-descriptions-item label="菜系类型">{{ viewForm?.type }}</el-descriptions-item>
        <el-descriptions-item label="销量">{{ viewForm?.sales }}</el-descriptions-item>
        <el-descriptions-item label="餐厅介绍">{{ viewForm?.description || '暂无' }}</el-descriptions-item>
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

const restaurantList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedIds = ref([])

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  name: '', price: 0, address: '', region: '', score: 3.0, type: '', sales: 0, imageUrl: '', description: ''
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
const getRestaurantList = async () => {
  const params = {
    page: currentPage.value,
    size: pageSize.value,
    ...searchParams.value
  }
  try {
    const res = await axios.get('/admin/restaurant/list', { params })
    if (res.data.code === 1) {
      restaurantList.value = res.data.data.records || []
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
  getRestaurantList()
}

// 重置
const resetSearch = () => {
  searchParams.value = { name: '', region: '', type: '' }
  currentPage.value = 1
  getRestaurantList()
}

// 多选
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 新增
const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    name: '',
    price: 0,
    address: '',
    region: '',
    score: 3.0,
    type: '',
    sales: 0,
    imageUrl: '',
    description: ''
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
  const url = isEdit.value ? '/admin/restaurant/update' : '/admin/restaurant/add'
  const res = await axios.post(url, form.value)
  if (res.data.code === 1) {
    ElMessage.success(res.data.msg)
    dialogVisible.value = false
    getRestaurantList()
  } else {
    ElMessage.error(res.data.msg)
  }
}

// 批量删除
const batchDelete = async () => {
  for (const id of selectedIds.value) {
    await axios.delete(`/admin/restaurant/delete/${id}`)
  }
  ElMessage.success('批量删除成功')
  getRestaurantList()
}

onMounted(() => {
  getRestaurantList()
})
</script>

<style scoped>
.restaurant-manage {
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