<template>
  <div class="scenic-manage">
    <!-- 顶部操作栏 -->
    <div class="top-bar" style="margin-bottom: 16px; display: flex; gap: 10px;">
      <el-button type="primary" size="small" @click="showAddDialog">新增</el-button>
      <el-button type="danger" size="small" @click="batchDelete">删除</el-button>
    </div>

    <!-- 数据表格（完全对齐参考图） -->
    <el-table :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" />
      <el-table-column prop="name" label="景点名称" min-width="120" show-overflow-tooltip />
      <el-table-column prop="price" label="票价" width="80" />
      <el-table-column prop="address" label="所在地" min-width="180" show-overflow-tooltip />
      <el-table-column prop="region" label="地区" width="150" />
      <el-table-column prop="popularity" label="热度" width="80">
        <template #default="{ row }">热度 {{ row.popularity }}</template>
      </el-table-column>
      <el-table-column prop="level" label="等级" width="90" />
      <el-table-column prop="visitCount" label="游量" width="80" />
      <el-table-column label="图片" width="80">
        <template #default="{ row }">
          <el-image
              :src="row.imageUrl"
              style="width: 24px; height: 24px"
              fit="cover"
              :preview-src-list="[row.imageUrl]"
          />
        </template>
      </el-table-column>
      <el-table-column prop="history" label="文化历史" min-width="200" show-overflow-tooltip />

      <!-- 操作列：已修复图标 -->
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button type="text" @click="showDetail(row)">
            <el-icon><View /></el-icon>
          </el-button>
          <el-button type="text" @click="showEditDialog(row)">
            <el-icon><Edit /></el-icon>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页（和参考图一致） -->
    <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, prev, pager, next, jumper, ->, sizes"
        @size-change="getList"
        @current-change="getList"
        style="margin-top: 16px; text-align: right;"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '修改景点' : '新增景点'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="景点名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="票价">
          <el-input-number v-model="form.price" :min="0" />
        </el-form-item>
        <el-form-item label="所在地">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="地区">
          <el-input v-model="form.region" placeholder="如：江苏-南京-玄武区" />
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
        <el-form-item label="游量">
          <el-input-number v-model="form.visitCount" :min="0" />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.imageUrl" />
        </el-form-item>
        <el-form-item label="文化历史">
          <el-input type="textarea" v-model="form.history" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailVisible" title="景点详情" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="景点名称">{{ currentDetail.name }}</el-descriptions-item>
        <el-descriptions-item label="票价">{{ currentDetail.price }} 元</el-descriptions-item>
        <el-descriptions-item label="所在地">{{ currentDetail.address }}</el-descriptions-item>
        <el-descriptions-item label="地区">{{ currentDetail.region }}</el-descriptions-item>
        <el-descriptions-item label="热度">{{ currentDetail.popularity }}</el-descriptions-item>
        <el-descriptions-item label="等级">{{ currentDetail.level }}</el-descriptions-item>
        <el-descriptions-item label="游量">{{ currentDetail.visitCount }}</el-descriptions-item>
        <el-descriptions-item label="文化历史">{{ currentDetail.history }}</el-descriptions-item>
        <el-descriptions-item label="景点图片">
          <el-image
              v-if="currentDetail.imageUrl"
              :src="currentDetail.imageUrl"
              style="width: 120px; height: 120px"
              fit="cover"
              preview-src-list="[currentDetail.imageUrl]"
          />
          <span v-else>暂无图片</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { View, Edit } from '@element-plus/icons-vue'  // 这里加了图标

const page = ref(1)
const size = ref(10)
const total = ref(0)
const tableData = ref([])
const selectedRows = ref([])

// 新增/编辑弹窗
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({
  id: null,
  name: '',
  price: 0,
  address: '',
  region: '',
  popularity: 0,
  level: '',
  visitCount: 0,
  imageUrl: '',
  history: ''
})

// 详情弹窗
const detailVisible = ref(false)
const currentDetail = ref({})

// 获取列表
const getList = async () => {
  const res = await axios.get('http://localhost:8080/scenic/list', {
    params: { page: page.value, size: size.value }
  })
  if (res.data.code === 1) {
    tableData.value = res.data.data
    total.value = res.data.total
  }
}

// 表格选择变化
const handleSelectionChange = (val) => {
  selectedRows.value = val
}

// 批量删除
const batchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要删除的景点')
    return
  }
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个景点吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    for (const row of selectedRows.value) {
      await axios.delete(`http://localhost:8080/scenic/delete/${row.id}`)
    }
    ElMessage.success('批量删除成功')
    getList()
  } catch (error) {
    ElMessage.info('已取消删除')
  }
}

// 单个删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该景点吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await axios.delete(`http://localhost:8080/scenic/delete/${row.id}`)
    if (res.data.code === 1) {
      ElMessage.success(res.data.msg)
      getList()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (error) {
    ElMessage.info('已取消删除')
  }
}

// 新增弹窗
const showAddDialog = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    name: '',
    price: 0,
    address: '',
    region: '',
    popularity: 0,
    level: '',
    visitCount: 0,
    imageUrl: '',
    history: ''
  })
  dialogVisible.value = true
}

// 编辑弹窗
const showEditDialog = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

// 查看详情
const showDetail = (row) => {
  currentDetail.value = row
  detailVisible.value = true
}

// 提交表单（新增/编辑）
const submitForm = async () => {
  const url = isEdit.value ? '/scenic/update' : '/scenic/add'
  const res = await axios.post(`http://localhost:8080${url}`, form)
  if (res.data.code === 1) {
    ElMessage.success(res.data.msg)
    dialogVisible.value = false
    getList()
  } else {
    ElMessage.error(res.data.msg)
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.scenic-manage {
  background: #fff;
  padding: 16px;
  border-radius: 4px;
  max-width: 100%;
}
.el-table {
  font-size: 13px;
}
.el-table th, .el-table td {
  padding: 8px 0;
}
</style>
