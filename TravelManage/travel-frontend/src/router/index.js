import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import UserHome from '../views/user/Home.vue'
import AdminHome from '../views/admin/Home.vue'
import UserManage from '../views/admin/UserManage.vue'
import ScenicManage from '@/views/admin/ScenicManage.vue'

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', component: Login },
    { path: '/register', component: Register },

    // 用户端只保留主页，不再跳转子页面
    {
        path: '/user',
        component: UserHome,
        meta: { requiresAuth: true, role: 'user' }
    },

    // 管理员端
    {
        path: '/admin',
        component: AdminHome,
        meta: { requiresAuth: true, role: 'admin' },
        children: [
            { path: '', redirect: 'user' },
            { path: 'user', component: UserManage },
            { path: 'scenic', component: ScenicManage },
            { path: 'restaurant', component: () => import('@/views/admin/RestaurantManage.vue') },
            { path: 'homestay', component: () => import('@/views/admin/HomestayManage.vue') }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const userRole = localStorage.getItem('userRole')
    if (to.meta.requiresAuth && !userRole) {
        next('/login')
    } else if (to.meta.role && userRole !== to.meta.role) {
        next('/login')
    } else {
        next()
    }
})

export default router