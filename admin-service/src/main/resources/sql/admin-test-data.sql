-- Beautiful Mall 后台管理测试数据
-- 仅限本地开发、联调和测试环境使用，禁止在生产环境执行。
-- 当前登录逻辑使用明文密码校验，因此本脚本中的密码为明文测试密码。
-- 执行前请先创建 admin_user、admin_role、admin_permission、
-- admin_user_role、admin_role_permission 五张表。

SET NAMES utf8mb4;

START TRANSACTION;

-- 1. 管理员
-- admin          / Admin@123456    超级管理员
-- operator       / Operator@123456 系统管理员
-- auditor        / Auditor@123456  审计员
-- disabled_admin / Disabled@123456 禁用账号，用于测试禁止登录
INSERT INTO `admin_user` (
    `username`,
    `password`,
    `display_name`,
    `mobile`,
    `enabled`,
    `must_change_password`,
    `password_updated_time`
) VALUES
    ('admin', 'Admin@123456', '超级管理员', '13800000001', 1, 0, CURRENT_TIMESTAMP),
    ('operator', 'Operator@123456', '系统管理员', '13800000002', 1, 0, CURRENT_TIMESTAMP),
    ('auditor', 'Auditor@123456', '审计员', '13800000003', 1, 0, CURRENT_TIMESTAMP),
    ('disabled_admin', 'Disabled@123456', '已禁用管理员', '13800000004', 0, 0, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
    `password` = VALUES(`password`),
    `display_name` = VALUES(`display_name`),
    `mobile` = VALUES(`mobile`),
    `enabled` = VALUES(`enabled`),
    `must_change_password` = VALUES(`must_change_password`),
    `password_updated_time` = VALUES(`password_updated_time`);

SET @admin_id = (
    SELECT `id` FROM `admin_user` WHERE `username` = 'admin' LIMIT 1
);

-- 2. 角色
INSERT INTO `admin_role` (
    `name`,
    `code`,
    `description`,
    `enabled`,
    `created_by`,
    `updated_by`
) VALUES
    ('超级管理员', 'super_admin', '拥有后台全部权限', 1, @admin_id, @admin_id),
    ('系统管理员', 'system_admin', '负责管理员和角色的日常维护', 1, @admin_id, @admin_id),
    ('审计员', 'auditor', '只拥有后台数据查看权限', 1, @admin_id, @admin_id)
ON DUPLICATE KEY UPDATE
    `name` = VALUES(`name`),
    `description` = VALUES(`description`),
    `enabled` = VALUES(`enabled`),
    `updated_by` = VALUES(`updated_by`);

-- 3. 一级权限：主页和系统管理目录
INSERT INTO `admin_permission` (
    `parent_id`,
    `name`,
    `code`,
    `type`,
    `sort`,
    `enabled`,
    `description`,
    `created_by`,
    `updated_by`
) VALUES
    (NULL, '首页', 'home:view', 'MENU', 1, 1, '后台首页访问权限', @admin_id, @admin_id),
    (NULL, '系统管理', 'system:manage', 'DIRECTORY', 2, 1, '系统管理目录', @admin_id, @admin_id)
ON DUPLICATE KEY UPDATE
    `parent_id` = VALUES(`parent_id`),
    `name` = VALUES(`name`),
    `type` = VALUES(`type`),
    `sort` = VALUES(`sort`),
    `enabled` = VALUES(`enabled`),
    `description` = VALUES(`description`),
    `updated_by` = VALUES(`updated_by`);

SET @system_manage_id = (
    SELECT `id` FROM `admin_permission` WHERE `code` = 'system:manage' LIMIT 1
);

-- 4. 二级权限：系统管理菜单
INSERT INTO `admin_permission` (
    `parent_id`,
    `name`,
    `code`,
    `type`,
    `sort`,
    `enabled`,
    `description`,
    `created_by`,
    `updated_by`
) VALUES
    (@system_manage_id, '管理员管理', 'system:admin', 'MENU', 1, 1, '管理员管理菜单', @admin_id, @admin_id),
    (@system_manage_id, '角色管理', 'system:role', 'MENU', 2, 1, '角色管理菜单', @admin_id, @admin_id),
    (@system_manage_id, '权限管理', 'system:permission', 'MENU', 3, 1, '权限管理菜单', @admin_id, @admin_id)
ON DUPLICATE KEY UPDATE
    `parent_id` = VALUES(`parent_id`),
    `name` = VALUES(`name`),
    `type` = VALUES(`type`),
    `sort` = VALUES(`sort`),
    `enabled` = VALUES(`enabled`),
    `description` = VALUES(`description`),
    `updated_by` = VALUES(`updated_by`);

SET @admin_menu_id = (
    SELECT `id` FROM `admin_permission` WHERE `code` = 'system:admin' LIMIT 1
);
SET @role_menu_id = (
    SELECT `id` FROM `admin_permission` WHERE `code` = 'system:role' LIMIT 1
);
SET @permission_menu_id = (
    SELECT `id` FROM `admin_permission` WHERE `code` = 'system:permission' LIMIT 1
);

-- 5. 三级权限：按钮权限
INSERT INTO `admin_permission` (
    `parent_id`,
    `name`,
    `code`,
    `type`,
    `sort`,
    `enabled`,
    `description`,
    `created_by`,
    `updated_by`
) VALUES
    (@admin_menu_id, '查询管理员', 'admin:view', 'BUTTON', 1, 1, '查询管理员列表和详情', @admin_id, @admin_id),
    (@admin_menu_id, '新增管理员', 'admin:create', 'BUTTON', 2, 1, '新增后台管理员', @admin_id, @admin_id),
    (@admin_menu_id, '编辑管理员', 'admin:update', 'BUTTON', 3, 1, '编辑和启停后台管理员', @admin_id, @admin_id),
    (@admin_menu_id, '分配角色', 'admin:assign-role', 'BUTTON', 4, 1, '为后台管理员分配角色', @admin_id, @admin_id),
    (@role_menu_id, '查询角色', 'role:view', 'BUTTON', 1, 1, '查询角色列表和详情', @admin_id, @admin_id),
    (@role_menu_id, '新增角色', 'role:create', 'BUTTON', 2, 1, '新增后台角色', @admin_id, @admin_id),
    (@role_menu_id, '编辑角色', 'role:update', 'BUTTON', 3, 1, '编辑和启停后台角色', @admin_id, @admin_id),
    (@role_menu_id, '分配权限', 'role:assign-permission', 'BUTTON', 4, 1, '为角色分配权限', @admin_id, @admin_id),
    (@permission_menu_id, '查询权限', 'permission:view', 'BUTTON', 1, 1, '查询权限树和详情', @admin_id, @admin_id),
    (@permission_menu_id, '新增权限', 'permission:create', 'BUTTON', 2, 1, '新增目录、菜单或按钮权限', @admin_id, @admin_id),
    (@permission_menu_id, '编辑权限', 'permission:update', 'BUTTON', 3, 1, '编辑权限信息', @admin_id, @admin_id)
ON DUPLICATE KEY UPDATE
    `parent_id` = VALUES(`parent_id`),
    `name` = VALUES(`name`),
    `type` = VALUES(`type`),
    `sort` = VALUES(`sort`),
    `enabled` = VALUES(`enabled`),
    `description` = VALUES(`description`),
    `updated_by` = VALUES(`updated_by`);

-- 6. 管理员与角色关联
INSERT IGNORE INTO `admin_user_role` (
    `admin_id`,
    `role_id`,
    `created_by`,
    `updated_by`
)
SELECT
    u.`id`,
    r.`id`,
    @admin_id,
    @admin_id
FROM `admin_user` u
CROSS JOIN `admin_role` r
WHERE
    (u.`username` = 'admin' AND r.`code` = 'super_admin')
    OR (u.`username` = 'operator' AND r.`code` = 'system_admin')
    OR (u.`username` = 'auditor' AND r.`code` = 'auditor')
    OR (u.`username` = 'disabled_admin' AND r.`code` = 'system_admin');

-- 7. 超级管理员角色拥有全部已启用权限
INSERT IGNORE INTO `admin_role_permission` (
    `role_id`,
    `permission_id`,
    `created_by`,
    `updated_by`
)
SELECT
    r.`id`,
    p.`id`,
    @admin_id,
    @admin_id
FROM `admin_role` r
CROSS JOIN `admin_permission` p
WHERE r.`code` = 'super_admin'
  AND p.`enabled` = 1;

-- 8. 系统管理员角色：管理员维护、角色查看
INSERT IGNORE INTO `admin_role_permission` (
    `role_id`,
    `permission_id`,
    `created_by`,
    `updated_by`
)
SELECT
    r.`id`,
    p.`id`,
    @admin_id,
    @admin_id
FROM `admin_role` r
CROSS JOIN `admin_permission` p
WHERE r.`code` = 'system_admin'
  AND p.`code` IN (
      'home:view',
      'system:manage',
      'system:admin',
      'admin:view',
      'admin:create',
      'admin:update',
      'admin:assign-role',
      'system:role',
      'role:view'
  );

-- 9. 审计员角色：三个管理模块只读
INSERT IGNORE INTO `admin_role_permission` (
    `role_id`,
    `permission_id`,
    `created_by`,
    `updated_by`
)
SELECT
    r.`id`,
    p.`id`,
    @admin_id,
    @admin_id
FROM `admin_role` r
CROSS JOIN `admin_permission` p
WHERE r.`code` = 'auditor'
  AND p.`code` IN (
      'home:view',
      'system:manage',
      'system:admin',
      'admin:view',
      'system:role',
      'role:view',
      'system:permission',
      'permission:view'
  );

COMMIT;

-- 验证管理员、角色和权限关系
SELECT
    u.`username`,
    u.`display_name`,
    u.`enabled`,
    GROUP_CONCAT(DISTINCT r.`code` ORDER BY r.`code`) AS `role_codes`,
    GROUP_CONCAT(DISTINCT p.`code` ORDER BY p.`code`) AS `permission_codes`
FROM `admin_user` u
LEFT JOIN `admin_user_role` ur ON ur.`admin_id` = u.`id`
LEFT JOIN `admin_role` r ON r.`id` = ur.`role_id` AND r.`enabled` = 1
LEFT JOIN `admin_role_permission` rp ON rp.`role_id` = r.`id`
LEFT JOIN `admin_permission` p ON p.`id` = rp.`permission_id` AND p.`enabled` = 1
WHERE u.`username` IN ('admin', 'operator', 'auditor', 'disabled_admin')
GROUP BY u.`id`, u.`username`, u.`display_name`, u.`enabled`
ORDER BY u.`id`;
