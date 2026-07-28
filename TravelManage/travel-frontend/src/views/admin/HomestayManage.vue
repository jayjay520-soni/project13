<template>
  <div class="homestay-manage">
    <div class="top-bar" style="margin-bottom: 16px; display: flex; gap: 10px;">
      <el-button type="primary" size="small" @click="showAddDialog">新增</el-button>
      <el-button type="danger" size="small" @click="batchDelete">删除</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" />
      <el-table-column prop="name" label="民宿名称" min-width="120" show-overflow-tooltip />
      <el-table-column prop="type" label="民宿类型" width="100" />
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">¥{{ row.price }}/晚</template>
      </el-table-column>
      <el-table-column prop="address" label="地址" min-width="180" show-overflow-tooltip />
      <el-table-column prop="region" label="地区" width="150" />
      <el-table-column prop="rating" label="评分" width="80" />
      <el-table-column prop="roomCount" label="房间数" width="80" />
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
      <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '修改民宿' : '新增民宿'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="民宿名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="民宿类型">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option label="整套房源" value="整套房源" />
            <el-option label="独立房间" value="独立房间" />
            <el-option label="特色民宿" value="特色民宿" />
            <el-option label="度假别墅" value="度假别墅" />
            <el-option label="农家院" value="农家院" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格/晚">
          <el-input-number v-model="form.price" :min="0" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="地区">
          <el-input v-model="form.region" placeholder="如：江苏-南京-玄武区" />
        </el-form-item>
        <el-form-item label="评分">
          <el-rate v-model="form.rating" allow-half />
        </el-form-item>
        <el-form-item label="房间数">
          <el-input-number v-model="form.roomCount" :min="0" />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.imageUrl" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="民宿详情" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="民宿名称">{{ currentDetail.name }}</el-descriptions-item>
        <el-descriptions-item label="民宿类型">{{ currentDetail.type }}</el-descriptions-item>
        <el-descriptions-item label="价格">¥{{ currentDetail.price }}/晚</el-descriptions-item>
        <el-descriptions-item label="地址">{{ currentDetail.address }}</el-descriptions-item>
        <el-descriptions-item label="地区">{{ currentDetail.region }}</el-descriptions-item>
        <el-descriptions-item label="评分">{{ currentDetail.rating }}</el-descriptions-item>
        <el-descriptions-item label="房间数">{{ currentDetail.roomCount }}</el-descriptions-item>
        <el-descriptions-item label="描述">{{ currentDetail.description }}</el-descriptions-item>
        <el-descriptions-item label="图片">
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
import { View, Edit } from '@element-plus/icons-vue'

const page = ref(1)
const size = ref(10)
const total = ref(0)
const tableData = ref([])
const selectedRows = ref([])

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({
  id: null,
  name: '',
  type: '',
  price: 0,
  address: '',
  region: '',
  rating: 0,
  roomCount: 0,
  imageUrl: '',
  description: ''
})

const detailVisible = ref(false)
const currentDetail = ref({})

const getList = async () => {
  const res = await axios.get('http://localhost:8080/homestay/list', {
    params: { page: page.value, size: size.value }
  })
  if (res.data.code === 1) {
    tableData.value = res.data.data
    total.value = res.data.total
  }
}

const handleSelectionChange = (val) => {
  selectedRows.value = val
}

const batchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要删除的民宿')
    return
  }
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个民宿吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    for (const row of selectedRows.value) {
      await axios.delete(`http://localhost:8080/homestay/delete/${row.id}`)
    }
    ElMessage.success('批量删除成功')
    getList()
  } catch (error) {
    ElMessage.info('已取消删除')
  }
}

const showAddDialog = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    name: '',
    type: '',
    price: 0,
    address: '',
    region: '',
    rating: 0,
    roomCount: 0,
    imageUrl: '',
    description: ''
  })
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const showDetail = (row) => {
  currentDetail.value = row
  detailVisible.value = true
}

const submitForm = async () => {
  const url = isEdit.value ? '/homestay/update' : '/homestay/add'
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
.homestay-manage {
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
