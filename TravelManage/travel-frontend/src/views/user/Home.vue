<template>
  <div class="user-home">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="container header-content">
        <div class="header-right">
          <span class="user-name">欢迎，{{ username }}</span>
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </div>
    </header>

    <!-- 顶部横幅 -->
    <div class="hero-banner">
      <div class="hero-content">
        <h1>发现美好旅程</h1>
        <p>精选餐厅 · 舒适民宿 · 热门景区</p>
      </div>
    </div>

    <!-- 分类导航 -->
    <div class="category-nav">
      <div class="container">
        <div
            class="nav-item"
            :class="{ active: currentCategory === 'restaurant' }"
            @click="switchCategory('restaurant')"
        >
          <el-icon><KnifeFork /></el-icon>
          <span>餐厅</span>
          <span class="nav-badge">{{ restaurantList.length }}</span>
        </div>
        <div
            class="nav-item"
            :class="{ active: currentCategory === 'homestay' }"
            @click="switchCategory('homestay')"
        >
          <el-icon><House /></el-icon>
          <span>民宿</span>
          <span class="nav-badge">{{ homestayList.length }}</span>
        </div>
        <div
            class="nav-item"
            :class="{ active: currentCategory === 'scenic' }"
            @click="switchCategory('scenic')"
        >
          <el-icon><Location /></el-icon>
          <span>景区</span>
          <span class="nav-badge">{{ scenicList.length }}</span>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area container">
      <!-- 数据列表 -->
      <div class="data-list" v-loading="loading">
        <!-- 餐厅列表 -->
        <div v-if="currentCategory === 'restaurant'">
          <div v-if="restaurantList.length > 0" class="card-grid">
            <div v-for="item in restaurantList" :key="item.id" class="card">
              <div class="card-image">
                <el-image
                    :src="item.image_url || ''"
                    fit="cover"
                    @error="handleImageError"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                      <span>暂无图片</span>
                    </div>
                  </template>
                </el-image>
              </div>
              <div class="card-body">
                <h3 class="card-title">{{ item.name }}</h3>
                <div class="card-info">
                  <p><el-icon><Location /></el-icon> {{ item.address }}</p>
                  <p><el-icon><Menu /></el-icon> 菜系：{{ item.type }}</p>
                  <p><el-icon><Money /></el-icon> 人均 ¥{{ item.price }}</p>
                  <p><el-icon><TrendCharts /></el-icon> 销量：{{ item.sales }}</p>
                  <p><el-icon><Star /></el-icon> 评分：{{ item.score }}</p>
                </div>
                <div class="card-footer">
                  <el-button size="small" type="primary" @click="viewDetail('餐厅', item)">查看详情</el-button>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="empty-state">
            <el-empty description="暂无餐厅信息" />
          </div>
        </div>

        <!-- 民宿列表 -->
        <div v-if="currentCategory === 'homestay'">
          <div v-if="homestayList.length > 0" class="card-grid">
            <div v-for="item in homestayList" :key="item.id" class="card">
              <div class="card-image">
                <el-image
                    :src="item.image_url || ''"
                    fit="cover"
                    @error="handleImageError"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                      <span>暂无图片</span>
                    </div>
                  </template>
                </el-image>
              </div>
              <div class="card-body">
                <h3 class="card-title">{{ item.name }}</h3>
                <div class="card-info">
                  <p><el-icon><Location /></el-icon> {{ item.address }}</p>
                  <p><el-icon><House /></el-icon> 类型：{{ item.type }}</p>
                  <p><el-icon><Money /></el-icon> ¥{{ item.price }}/晚</p>
                  <p><el-icon><User /></el-icon> 可住：{{ item.capacity }}人</p>
                  <p><el-icon><Star /></el-icon> 评分：{{ item.score }}</p>
                </div>
                <div class="card-footer">
                  <el-button size="small" type="primary" @click="viewDetail('民宿', item)">查看详情</el-button>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="empty-state">
            <el-empty description="暂无民宿信息" />
          </div>
        </div>

        <!-- 景区列表 -->
        <div v-if="currentCategory === 'scenic'">
          <div v-if="scenicList.length > 0" class="card-grid">
            <div v-for="item in scenicList" :key="item.id" class="card">
              <div class="card-image">
                <el-image
                    :src="item.image_url || ''"
                    fit="cover"
                    @error="handleImageError"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                      <span>暂无图片</span>
                    </div>
                  </template>
                </el-image>
              </div>
              <div class="card-body">
                <h3 class="card-title">{{ item.name }}</h3>
                <div class="card-info">
                  <p><el-icon><Location /></el-icon> {{ item.address }}</p>
                  <p><el-icon><Money /></el-icon> 门票 ¥{{ item.price }}</p>
                  <p><el-icon><TrendCharts /></el-icon> 热度：{{ item.popularity }}</p>
                  <p><el-icon><Medal /></el-icon> 等级：{{ item.level }}</p>
                  <p><el-icon><User /></el-icon> 游览量：{{ item.visit_count }}</p>
                </div>
                <div class="card-footer">
                  <el-button size="small" type="primary" @click="viewDetail('景区', item)">查看详情</el-button>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="empty-state">
            <el-empty description="暂无景区信息" />
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-wrapper" v-if="total > 0">
          <el-pagination
              v-model:current-page="pageNum"
              v-model:page-size="pageSize"
              :total="total"
              layout="total, sizes, prev, pager, next, jumper"
              @change="loadData"
          />
        </div>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" :title="detailTitle" width="600px">
      <div class="detail-content" v-if="detailData">
        <div class="detail-image">
          <el-image
              :src="detailData.image_url || ''"
              style="width:100%;height:280px;object-fit:cover;border-radius:8px"
              fit="cover"
          >
            <template #error>
              <div class="detail-image-placeholder">
                <el-icon><Picture /></el-icon>
                <span>暂无图片</span>
              </div>
            </template>
          </el-image>
        </div>
        <div class="detail-info">
          <h2>{{ detailData.name }}</h2>
          <p><el-icon><Location /></el-icon> 地址：{{ detailData.address }}</p>

          <!-- 餐厅独有字段 -->
          <p v-if="currentCategory === 'restaurant'">
            <el-icon><Menu /></el-icon> 菜系：{{ detailData.type }} |
            <el-icon><TrendCharts /></el-icon> 销量：{{ detailData.sales }} |
            <el-icon><Star /></el-icon> 评分：{{ detailData.score }}
          </p>
          <!-- 民宿独有字段 -->
          <p v-if="currentCategory === 'homestay'">
            <el-icon><House /></el-icon> 类型：{{ detailData.type }} |
            <el-icon><User /></el-icon> 可住：{{ detailData.capacity }}人 |
            <el-icon><Star /></el-icon> 评分：{{ detailData.score }}
          </p>
          <!-- 景区独有字段 -->
          <p v-if="currentCategory === 'scenic'">
            <el-icon><Medal /></el-icon> 等级：{{ detailData.level }} |
            <el-icon><TrendCharts /></el-icon> 热度：{{ detailData.popularity }} |
            <el-icon><User /></el-icon> 游览量：{{ detailData.visit_count }}
          </p>

          <p><el-icon><Money /></el-icon> 价格：¥{{ detailData.price }}</p>
          <p class="intro">
            <el-icon><Document /></el-icon> 简介：{{ detailData.description || detailData.history || '暂无' }}
          </p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import {
  KnifeFork,
  House,
  Location,
  Picture,
  Money,
  Star,
  User,
  Menu,
  TrendCharts,
  Medal,
  Document
} from '@element-plus/icons-vue'

const router = useRouter()

// 当前分类
const currentCategory = ref('restaurant')

// 用户名
const username = ref('用户')

// 分页
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

// 数据列表（字段与数据库完全对齐）
const restaurantList = ref([])
const homestayList = ref([])
const scenicList = ref([])

// 详情弹窗
const detailVisible = ref(false)
const detailData = ref(null)
const detailTitle = ref('')

// 获取用户名
const getUsername = () => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    try {
      const user = JSON.parse(userStr)
      username.value = user.username || user.name || '用户'
    } catch {
      username.value = '用户'
    }
  }
}

// 退出登录
const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

// 切换分类
const switchCategory = (category) => {
  currentCategory.value = category
  pageNum.value = 1
  loadData()
}

// 加载数据（接口路径与后台管理端保持一致）
// 加载数据（接口路径与后台管理端保持一致）
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: pageNum.value,
      size: pageSize.value
    }

    let url = ''
    let listRef = null

    switch (currentCategory.value) {
      case 'restaurant':
        url = '/admin/restaurant/list'
        listRef = restaurantList
        break
      case 'homestay':
        url = '/admin/homestay/list'
        listRef = homestayList
        break
      case 'scenic':
        url = '/admin/scenic/list'
        listRef = scenicList
        break
      default:
        return
    }

    const res = await axios.get(url, { params })
    console.log(`${currentCategory.value} 完整数据：`, res.data)

    if (res.data.code === 1) {
      // ✅ 核心修复：兼容多种分页返回格式
      const data = res.data.data || {}
      listRef.value = data.records || data.list || data || []
      total.value = data.total || listRef.value.length
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (err) {
    console.error('加载失败：', err)
    ElMessage.error('加载数据失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 查看详情
const viewDetail = (type, item) => {
  detailData.value = item
  detailTitle.value = `${type}详情`
  detailVisible.value = true
}

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.src = ''
}

// 初始化加载
onMounted(() => {
  getUsername()
  loadData()
})
</script>

<style scoped>
.user-home {
  min-height: 100vh;
  background: #f0f2f5;
}

/* 顶部导航 */
.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 12px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-name {
  font-size: 14px;
  color: #666;
}

/* 顶部横幅 */
.hero-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 50px 20px;
  text-align: center;
}

.hero-content h1 {
  font-size: 32px;
  margin-bottom: 8px;
}

.hero-content p {
  font-size: 16px;
  opacity: 0.9;
}

/* 分类导航 */
.category-nav {
  background: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  position: sticky;
  top: 64px;
  z-index: 50;
}

.category-nav .container {
  display: flex;
  justify-content: center;
  gap: 20px;
  padding: 12px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 28px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  color: #666;
  transition: all 0.3s ease;
  position: relative;
}

.nav-item:hover {
  background: #f0f2f5;
  color: #667eea;
}

.nav-item.active {
  background: #667eea;
  color: white;
}

.nav-item .el-icon {
  font-size: 20px;
}

.nav-badge {
  background: #f56c6c;
  color: white;
  font-size: 12px;
  padding: 1px 8px;
  border-radius: 12px;
  min-width: 20px;
  text-align: center;
}

.nav-item.active .nav-badge {
  background: rgba(255,255,255,0.3);
}

/* 内容区域 */
.content-area {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 20px;
}

/* 卡片网格 */
.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}

.card-image {
  position: relative;
  height: 180px;
  overflow: hidden;
  background: #f5f7fa;
}

.card-image :deep(.el-image) {
  width: 100%;
  height: 100%;
}

.card-image :deep(.el-image__inner) {
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #ccc;
}

.image-placeholder .el-icon {
  font-size: 40px;
}

.image-placeholder span {
  font-size: 12px;
  margin-top: 4px;
}

.card-body {
  padding: 14px 18px 18px;
}

.card-title {
  font-size: 17px;
  margin: 0 0 10px 0;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-info {
  margin-bottom: 10px;
}

.card-info p {
  display: flex;
  align-items: center;
  gap: 6px;
  margin: 5px 0;
  font-size: 13px;
  color: #666;
}

.card-info .el-icon {
  font-size: 14px;
  color: #999;
  flex-shrink: 0;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
}

/* 空状态 */
.empty-state {
  padding: 40px 0;
}

/* 分页 */
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  padding: 16px 0;
  background: white;
  border-radius: 8px;
}

/* 详情弹窗 */
.detail-content {
  padding: 4px 0;
}

.detail-image {
  margin-bottom: 16px;
}

.detail-image-placeholder {
  width: 100%;
  height: 280px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #ccc;
  border-radius: 8px;
}

.detail-image-placeholder .el-icon {
  font-size: 48px;
}

.detail-info h2 {
  margin: 0 0 14px 0;
  font-size: 22px;
  color: #333;
}

.detail-info p {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin: 6px 0;
  font-size: 14px;
  color: #555;
}

.detail-info .el-icon {
  font-size: 16px;
  color: #667eea;
  flex-shrink: 0;
  margin-top: 2px;
}

.detail-info .intro {
  line-height: 1.6;
  color: #666;
}

/* 响应式 */
@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 24px;
  }

  .hero-content p {
    font-size: 14px;
  }

  .category-nav .container {
    gap: 8px;
    padding: 10px 12px;
  }

  .nav-item {
    padding: 8px 14px;
    font-size: 13px;
  }

  .nav-item .el-icon {
    font-size: 16px;
  }

  .card-grid {
    grid-template-columns: 1fr;
  }

  .header-content {
    justify-content: center;
  }
}
</style>