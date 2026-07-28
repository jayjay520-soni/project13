import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import UserHome from '../views/user/Home.vue'
import AdminHome from '../views/admin/Home.vue'
import UserManage from '../views/admin/UserManage.vue'
import ScenicManage from '@/views/admin/ScenicManage.vue'
import HomestayManage from '@/views/admin/HomestayManage.vue'
import RestaurantManage from '@/views/admin/RestaurantManage.vue'

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
    {
        path: '/admin',
        name: 'AdminHome',
        component: AdminHome,
        meta: { requiresAuth: true, role: 'admin' },
        children: [
            { path: 'user', component: UserManage },
            { path: 'scenic', component: ScenicManage },
            { path: 'homestay', component: HomestayManage },
            { path: 'restaurant', component: RestaurantManage }
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
