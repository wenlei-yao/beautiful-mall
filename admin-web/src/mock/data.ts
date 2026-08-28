import type { AdminUser, Permission, Role } from '@/types/admin';

export const mockAdmins: AdminUser[] = [
  {
    id: 10001,
    username: 'admin',
    displayName: '超级管理员',
    mobile: '138****8000',
    roleCodes: ['super_admin'],
    roleNames: ['超级管理员'],
    permissions: ['*:*:*'],
    status: 'enabled',
    lastLoginAt: '2026-08-24 20:36:18',
    lastLoginIp: '192.168.1.18',
    createdAt: '2026-01-06 09:20:00'
  },
  {
    id: 10002,
    username: 'mall_operator',
    displayName: '商城运营',
    mobile: '136****2208',
    roleCodes: ['operator'],
    roleNames: ['商城运营'],
    permissions: ['admin:view', 'role:view'],
    status: 'enabled',
    lastLoginAt: '2026-08-24 16:12:09',
    lastLoginIp: '10.10.8.21',
    createdAt: '2026-02-18 11:30:00'
  },
  {
    id: 10003,
    username: 'customer_service',
    displayName: '客服主管',
    mobile: '159****7316',
    roleCodes: ['customer_service'],
    roleNames: ['客服主管'],
    permissions: ['admin:view'],
    status: 'enabled',
    lastLoginAt: '2026-08-23 19:42:31',
    lastLoginIp: '10.10.8.16',
    createdAt: '2026-03-08 14:05:00'
  },
  {
    id: 10004,
    username: 'finance_audit',
    displayName: '财务审核员',
    mobile: '186****1092',
    roleCodes: ['auditor'],
    roleNames: ['审计员'],
    permissions: ['role:view'],
    status: 'disabled',
    lastLoginAt: '2026-08-12 10:18:42',
    lastLoginIp: '10.10.9.11',
    createdAt: '2026-04-21 09:45:00'
  },
  {
    id: 10005,
    username: 'content_editor',
    displayName: '内容编辑',
    mobile: '133****5780',
    roleCodes: ['operator'],
    roleNames: ['商城运营'],
    permissions: ['admin:view'],
    status: 'enabled',
    lastLoginAt: '2026-08-22 14:06:27',
    lastLoginIp: '10.10.8.32',
    createdAt: '2026-05-19 10:20:00'
  }
];

export const mockRoles: Role[] = [
  {
    id: 1,
    name: '超级管理员',
    code: 'super_admin',
    description: '拥有后台全部菜单和操作权限',
    memberCount: 1,
    permissions: ['*:*:*'],
    status: 'enabled',
    createdAt: '2026-01-06 09:00:00'
  },
  {
    id: 2,
    name: '商城运营',
    code: 'operator',
    description: '负责商城日常运营和内容维护',
    memberCount: 2,
    permissions: ['system:admin', 'admin:view', 'system:role', 'role:view'],
    status: 'enabled',
    createdAt: '2026-02-18 10:00:00'
  },
  {
    id: 3,
    name: '客服主管',
    code: 'customer_service',
    description: '负责客服团队与后台人员查询',
    memberCount: 1,
    permissions: ['system:admin', 'admin:view'],
    status: 'enabled',
    createdAt: '2026-03-08 13:40:00'
  },
  {
    id: 4,
    name: '审计员',
    code: 'auditor',
    description: '只读查看后台配置与操作记录',
    memberCount: 1,
    permissions: ['system:role', 'role:view', 'system:permission'],
    status: 'disabled',
    createdAt: '2026-04-21 09:30:00'
  }
];

export const mockPermissions: Permission[] = [
  {
    id: 1,
    parentId: null,
    name: '首页',
    code: 'home:view',
    type: 'MENU',
    path: '/home',
    icon: 'solar:home-smile-angle-outline',
    sort: 1,
    status: 'enabled'
  },
  {
    id: 10,
    parentId: null,
    name: '系统管理',
    code: 'system:manage',
    type: 'DIRECTORY',
    path: '/system',
    icon: 'solar:settings-outline',
    sort: 2,
    status: 'enabled',
    children: [
      {
        id: 11,
        parentId: 10,
        name: '管理员管理',
        code: 'system:admin',
        type: 'MENU',
        path: '/system/admin',
        sort: 1,
        status: 'enabled',
        children: [
          { id: 111, parentId: 11, name: '查询管理员', code: 'admin:view', type: 'BUTTON', sort: 1, status: 'enabled' },
          { id: 112, parentId: 11, name: '新增管理员', code: 'admin:create', type: 'BUTTON', sort: 2, status: 'enabled' },
          { id: 113, parentId: 11, name: '编辑管理员', code: 'admin:update', type: 'BUTTON', sort: 3, status: 'enabled' },
          { id: 114, parentId: 11, name: '分配角色', code: 'admin:assign-role', type: 'BUTTON', sort: 4, status: 'enabled' }
        ]
      },
      {
        id: 12,
        parentId: 10,
        name: '角色管理',
        code: 'system:role',
        type: 'MENU',
        path: '/system/role',
        sort: 2,
        status: 'enabled',
        children: [
          { id: 121, parentId: 12, name: '查询角色', code: 'role:view', type: 'BUTTON', sort: 1, status: 'enabled' },
          { id: 122, parentId: 12, name: '新增角色', code: 'role:create', type: 'BUTTON', sort: 2, status: 'enabled' },
          { id: 123, parentId: 12, name: '编辑角色', code: 'role:update', type: 'BUTTON', sort: 3, status: 'enabled' },
          { id: 124, parentId: 12, name: '分配权限', code: 'role:assign-permission', type: 'BUTTON', sort: 4, status: 'enabled' }
        ]
      },
      {
        id: 13,
        parentId: 10,
        name: '权限管理',
        code: 'system:permission',
        type: 'MENU',
        path: '/system/permission',
        sort: 3,
        status: 'enabled',
        children: [
          { id: 131, parentId: 13, name: '查询权限', code: 'permission:view', type: 'BUTTON', sort: 1, status: 'enabled' },
          { id: 132, parentId: 13, name: '新增权限', code: 'permission:create', type: 'BUTTON', sort: 2, status: 'enabled' },
          { id: 133, parentId: 13, name: '编辑权限', code: 'permission:update', type: 'BUTTON', sort: 3, status: 'enabled' }
        ]
      }
    ]
  }
];

