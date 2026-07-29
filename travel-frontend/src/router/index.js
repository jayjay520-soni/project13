// router/index.js ✅ 正确配置
import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import UserHome from '../views/user/Home.vue'
import AdminHome from '../views/admin/Home.vue'
import UserManage from '../views/admin/UserManage.vue'
import ScenicManage from '@/views/admin/ScenicManage.vue'

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    {
        path: '/user',
        name: 'UserHome',
        component: UserHome,
        meta: { requiresAuth: true, role: 'user' }
    },
    // ✅ 只定义一次 /admin，所有子页面都放在 children 里
    {
        path: '/admin',
        name: 'AdminHome',
        component: AdminHome,
        meta: { requiresAuth: true, role: 'admin' },
        children: [
            { path: 'user', component: UserManage },   // 用户管理（子路由）
            { path: 'scenic', component: ScenicManage }, // 景区管理（子路由）
            // 后续可添加民宿/餐厅/评论管理等
            { path: 'restaurant', component: () => import('../views/admin/RestaurantManage.vue') },
            { path: 'homestay', component: () => import('../views/admin/HomestayManage.vue') }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫（不变）
router.beforeEach((to, from, next) => {
    const userRole = localStorage.getItem('userRole')
    if (to.meta.requiresAuth && !userRole) {
        next('/login')
    } else if (to.meta.role && userRole !== to.meta.role) {
        next('/403')
    } else {
        next()
    }
})

export default router
