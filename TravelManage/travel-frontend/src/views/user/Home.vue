<template>
  <div class="user-home">
    <header class="header">
      <div class="header-inner">
        <h1 class="logo">一体式旅游平台</h1>
        <nav class="nav-tabs">
          <div
            :class="['tab', { active: activeTab === 'scenic' }]" @click="switchTab('scenic')">
            <el-icon><Location /></el-icon>
            <span>景区</span>
          </div>
          <div
            :class="['tab', { active: activeTab === 'homestay' }]" @click="switchTab('homestay')">
            <el-icon><House /></el-icon>
            <span>民宿</span>
          </div>
          <div
            :class="['tab', { active: activeTab === 'restaurant' }]" @click="switchTab('restaurant')">
            <el-icon><Dish /></el-icon>
            <span>餐厅</span>
          </div>
        </nav>
        <div class="user-info">
          <el-avatar size="small">{{ username.charAt(0) }}</el-avatar>
          <span class="username">{{ username }}</span>
          <el-button type="danger" size="small" @click="handleLogout">退出</el-button>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div v-if="loading" class="loading-container">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>加载中...</span>
      </div>

      <div v-else-if="activeTab === 'scenic'" class="tab-content">
        <div class="section-header">
          <h2>热门景区</h2>
          <p>探索美丽风景，感受文化魅力</p>
        </div>
        <div class="card-grid">
          <div v-for="item in scenicList" :key="item.id" class="card" @click="showScenicDetail(item)">
            <div class="card-image">
              <el-image :src="item.imageUrl" fit="cover" />
              <div class="card-badge">{{ item.level }}</div>
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ item.name }}</h3>
              <div class="card-meta">
                <span class="price">¥{{ item.price }} 元</span>
                <span class="popularity">热度 {{ item.popularity }}</span>
              </div>
              <div class="card-address">
                <el-icon><Location /></el-icon>
                <span>{{ item.address }}</span>
              </div>
              <div class="card-footer">
                <span>游量 {{ item.visitCount }}</span>
                <el-button type="primary" size="small">查看详情</el-button>
              </div>
            </div>
          </div>
          <el-empty v-if="scenicList.length === 0" description="暂无景区数据" />
        </div>
      </div>

      <div v-else-if="activeTab === 'homestay'" class="tab-content">
        <div class="section-header">
          <h2>特色民宿</h2>
          <p>精选优质住宿，享受家的温馨</p>
        </div>
        <div class="card-grid">
          <div v-for="item in homestayList" :key="item.id" class="card" @click="showHomestayDetail(item)">
            <div class="card-image">
            <el-image :src="item.imageUrl" fit="cover" />
              <div class="card-badge badge-homestay">{{ item.type }}</div>
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ item.name }}</h3>
              <div class="card-meta">
                <span class="price">¥{{ item.price }}/晚</span>
                <span class="rating">
                  <el-rate :model-value="item.rating" disabled size="small" />
                </span>
              </div>
              <div class="card-address">
                <el-icon><Location /></el-icon>
                <span>{{ item.address }}</span>
              </div>
              <div class="card-footer">
                <span>房间 {{ item.roomCount }} 间</span>
                <el-button type="primary" size="small">查看详情</el-button>
              </div>
            </div>
          </div>
          <el-empty v-if="homestayList.length === 0" description="暂无民宿数据" />
        </div>
      </div>

      <div v-else-if="activeTab === 'restaurant'" class="tab-content">
        <div class="section-header">
          <h2>美食餐厅</h2>
          <p>品味地道美食，尽享舌尖美味</p>
        </div>
        <div class="card-grid">
          <div v-for="item in restaurantList" :key="item.id" class="card" @click="showRestaurantDetail(item)">
            <div class="card-image">
            <el-image :src="item.imageUrl" fit="cover" />
              <div class="card-badge badge-restaurant">{{ item.type }}</div>
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ item.name }}</h3>
              <div class="card-meta">
                <span class="price">人均 ¥{{ item.avgPrice }}</span>
                <span class="rating">
                  <el-rate :model-value="item.rating" disabled size="small" />
                </span>
              </div>
              <div class="card-address">
                <el-icon><Location /></el-icon>
                <span>{{ item.address }}</span>
              </div>
              <div class="card-footer">
                <span>访问量 {{ item.visitCount }}</span>
                <el-button type="primary" size="small">查看详情</el-button>
              </div>
            </div>
          </div>
          <el-empty v-if="restaurantList.length === 0" description="暂无餐厅数据" />
        </div>
      </div>
    </main>

    <el-dialog v-model="scenicDetailVisible" title="景区详情" width="700px">
      <div v-if="currentScenic" class="detail-content">
        <el-image v-if="currentScenic.imageUrl" :src="currentScenic.imageUrl" style="width: 100%; height: 300px" fit="cover" />
        <h2>{{ currentScenic.name }}</h2>
        <el-descriptions :column="2" border style="margin-top: 16px">
          <el-descriptions-item label="等级">{{ currentScenic.level }}</el-descriptions-item>
          <el-descriptions-item label="票价">¥{{ currentScenic.price }} 元</el-descriptions-item>
          <el-descriptions-item label="地区" :span="2">{{ currentScenic.region }}</el-descriptions-item>
          <el-descriptions-item label="地址" :span="2">{{ currentScenic.address }}</el-descriptions-item>
          <el-descriptions-item label="热度">{{ currentScenic.popularity }}</el-descriptions-item>
          <el-descriptions-item label="游量">{{ currentScenic.visitCount }}</el-descriptions-item>
          <el-descriptions-item label="文化历史" :span="2">{{ currentScenic.history }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <el-dialog v-model="homestayDetailVisible" title="民宿详情" width="700px">
      <div v-if="currentHomestay" class="detail-content">
        <el-image v-if="currentHomestay.imageUrl" :src="currentHomestay.imageUrl" style="width: 100%; height: 300px" fit="cover" />
        <h2>{{ currentHomestay.name }}</h2>
        <el-descriptions :column="2" border style="margin-top: 16px">
          <el-descriptions-item label="类型">{{ currentHomestay.type }}</el-descriptions-item>
          <el-descriptions-item label="价格">¥{{ currentHomestay.price }}/晚</el-descriptions-item>
          <el-descriptions-item label="地区" :span="2">{{ currentHomestay.region }}</el-descriptions-item>
          <el-descriptions-item label="地址" :span="2">{{ currentHomestay.address }}</el-descriptions-item>
          <el-descriptions-item label="评分">
            <el-rate :model-value="currentHomestay.rating" disabled />
          </el-descriptions-item>
          <el-descriptions-item label="房间数">{{ currentHomestay.roomCount }} 间</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ currentHomestay.description }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <el-dialog v-model="restaurantDetailVisible" title="餐厅详情" width="700px">
      <div v-if="currentRestaurant" class="detail-content">
        <el-image v-if="currentRestaurant.imageUrl" :src="currentRestaurant.imageUrl" style="width: 100%; height: 300px" fit="cover" />
        <h2>{{ currentRestaurant.name }}</h2>
        <el-descriptions :column="2" border style="margin-top: 16px">
          <el-descriptions-item label="菜系">{{ currentRestaurant.type }}</el-descriptions-item>
          <el-descriptions-item label="人均">¥{{ currentRestaurant.avgPrice }}</el-descriptions-item>
          <el-descriptions-item label="地区" :span="2">{{ currentRestaurant.region }}</el-descriptions-item>
          <el-descriptions-item label="地址" :span="2">{{ currentRestaurant.address }}</el-descriptions-item>
          <el-descriptions-item label="评分">
            <el-rate :model-value="currentRestaurant.rating" disabled />
          </el-descriptions-item>
          <el-descriptions-item label="访问量">{{ currentRestaurant.visitCount }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ currentRestaurant.description }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Location, House, Dish, Loading } from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const activeTab = ref('scenic')
const loading = ref(false)
const scenicList = ref([])
const homestayList = ref([])
const restaurantList = ref([])

const scenicDetailVisible = ref(false)
const homestayDetailVisible = ref(false)
const restaurantDetailVisible = ref(false)
const currentScenic = ref(null)
const currentHomestay = ref(null)
const currentRestaurant = ref(null)

const username = computed(() => {
  return localStorage?.getItem('username') || '用户'
})

const switchTab = (tab) => {
  activeTab.value = tab
  if (tab === 'scenic' && scenicList.value.length === 0) {
    loadScenic()
  } else if (tab === 'homestay' && homestayList.value.length === 0) {
    loadHomestay()
  } else if (tab === 'restaurant' && restaurantList.value.length === 0) {
    loadRestaurant()
  }
}

const loadScenic = async () => {
  loading.value = true
  try {
    const res = await axios.get('http://localhost:8080/scenic/all')
    if (res.data.code === 1) {
      scenicList.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('加载景区数据失败')
  } finally {
    loading.value = false
  }
}

const loadHomestay = async () => {
  loading.value = true
  try {
    const res = await axios.get('http://localhost:8080/homestay/all')
    if (res.data.code === 1) {
      homestayList.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('加载民宿数据失败')
  } finally {
    loading.value = false
  }
}

const loadRestaurant = async () => {
  loading.value = true
  try {
    const res = await axios.get('http://localhost:8080/restaurant/all')
    if (res.data.code === 1) {
      restaurantList.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('加载餐厅数据失败')
  } finally {
    loading.value = false
  }
}

const showScenicDetail = (item) => {
  currentScenic.value = item
  scenicDetailVisible.value = true
}

const showHomestayDetail = (item) => {
  currentHomestay.value = item
  homestayDetailVisible.value = true
}

const showRestaurantDetail = (item) => {
  currentRestaurant.value = item
  restaurantDetailVisible.value = true
}

const handleLogout = () => {
  ElMessage.success('退出登录成功')
  localStorage.clear()
  router.push('/login')
}

onMounted(() => {
  loadScenic()
})
</script>

<style scoped>
.user-home {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-inner {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  font-size: 22px;
  color: #409eff;
  margin: 0;
  font-weight: 700;
}

.nav-tabs {
  display: flex;
  gap: 8px;
}

.tab {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  color: #606266;
  transition: all 0.2s;
  font-size: 15px;
}

.tab:hover {
  background: #ecf5ff;
  color: #409eff;
}

.tab.active {
  background: #409eff;
  color: #fff;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  font-size: 14px;
  color: #303133;
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 32px 24px;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  gap: 16px;
  color: #909399;
}

.section-header {
  margin-bottom: 24px;
}

.section-header h2 {
  margin: 0 0 8px 0;
  font-size: 26px;
  color: #303133;
}

.section-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.card-image {
  position: relative;
  width: 100%;
  height: 200px;
  background: #f0f0f0;
  overflow: hidden;
}

.card-image :deep(.el-image) {
  width: 100%;
  height: 100%;
}

.card-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.badge-homestay {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.badge-restaurant {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-body {
  padding: 16px;
}

.card-title {
  margin: 0 0 10px 0;
  font-size: 17px;
  color: #303133;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.price {
  color: #f56c6c;
  font-weight: 600;
  font-size: 16px;
}

.popularity {
  color: #e6a23c;
  font-size: 13px;
}

.card-address {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  font-size: 13px;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  font-size: 13px;
  color: #909399;
}

.detail-content h2 {
  margin: 16px 0;
  color: #303133;
}
</style>
