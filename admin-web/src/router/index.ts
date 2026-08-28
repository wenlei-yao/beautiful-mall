import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import AdminLayout from '@/layouts/AdminLayout.vue';
import { useAuthStore } from '@/stores/auth';
import { hasPermission, hasRole } from '@/utils/permission';

declare module 'vue-router' {
  interface RouteMeta {
    title?: string;
    requiresAuth?: boolean;
    roles?: string[];
    permissions?: string[];
  }
}

export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/LoginView.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: AdminLayout,
    redirect: '/home',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'home',
        name: 'home',
        component: () => import('@/views/HomeView.vue'),
        meta: { title: '首页', requiresAuth: true }
      },
      {
        path: 'system/admin',
        name: 'admin-management',
        component: () => import('@/views/AdminUsersView.vue'),
        meta: { title: '管理员管理', requiresAuth: true, permissions: ['system:admin'] }
      },
      {
        path: 'system/role',
        name: 'role-management',
        component: () => import('@/views/RolesView.vue'),
        meta: { title: '角色管理', requiresAuth: true, permissions: ['system:role'] }
      },
      {
        path: 'system/permission',
        name: 'permission-management',
        component: () => import('@/views/PermissionsView.vue'),
        meta: { title: '权限管理', requiresAuth: true, permissions: ['system:permission'] }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/home'
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior: () => ({ top: 0 })
});

router.beforeEach(async to => {
  const auth = useAuthStore();
  if (to.name === 'login' && auth.isLoggedIn) return '/home';
  if (!to.meta.requiresAuth) return true;
  if (!auth.isLoggedIn) return { name: 'login', query: { redirect: to.fullPath } };
  if (!auth.user && !(await auth.restoreSession())) {
    return { name: 'login', query: { redirect: to.fullPath } };
  }
  if (to.meta.roles && !hasRole(auth.user, to.meta.roles)) return '/home';
  if (to.meta.permissions && !hasPermission(auth.user, to.meta.permissions)) return '/home';
  return true;
});

router.afterEach(to => {
  document.title = to.meta.title ? `${to.meta.title} - Beautiful Mall 管理后台` : 'Beautiful Mall 管理后台';
});

export default router;

