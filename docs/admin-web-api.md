# Beautiful Mall 后台管理接口文档

## 1. 文档说明

本文档定义 `admin-web` 与后台服务之间的接口契约，覆盖认证、首页统计、管理员、角色和权限管理。

接口状态说明：

- **前端已预留**：前端已经存在对应的请求封装，但后端接口尚未实现或响应结构仍需调整。
- **待后端实现**：根据当前交互原型定义的接口，前端尚未接入真实请求。

> 当前前端的登录响应只接收令牌，管理员、角色和权限仍使用字符串字段 `status`。本接口契约统一改为布尔字段 `enabled`，后续联调时需要同步调整前端类型和 Mock 数据。

## 2. 公共约定

### 2.1 基础路径

管理端接口统一使用以下前缀：

```text
/api/v1/admin
```

前端通过 `VITE_API_BASE_URL` 配置基础地址。当前默认值为 `/api`，请求封装中使用的相对地址以 `/v1/admin` 开头，组合后的完整路径即 `/api/v1/admin/...`。

### 2.2 鉴权方式

除登录接口外，请求头统一携带访问令牌：

```http
Authorization: Bearer <accessToken>
```

“记住登录”只决定前端将令牌保存到 `localStorage` 还是 `sessionStorage`，不会作为登录参数发送给后端。

### 2.3 统一响应

所有接口使用统一响应结构：

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `code` | `number` | 业务状态码，`0` 表示成功 |
| `message` | `string` | 结果说明或错误信息 |
| `data` | `object/array/null` | 业务数据 |

### 2.4 通用字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `enabled` | `boolean` | `true` 表示启用，`false` 表示停用或禁用 |
| `createdAt` | `string` | ISO 8601 格式的创建时间 |
| `updatedAt` | `string` | ISO 8601 格式的更新时间 |

权限类型：

| 值 | 中文名称 | 说明 |
| --- | --- | --- |
| `DIRECTORY` | 目录 | 菜单分组，一般不对应具体页面 |
| `MENU` | 菜单 | 可访问的后台页面，一般包含路由地址 |
| `BUTTON` | 按钮 | 页面操作权限，不包含路由地址 |

### 2.5 分页结构

分页查询统一使用：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `page` | `number` | 否 | 页码，从 `1` 开始，默认 `1` |
| `pageSize` | `number` | 否 | 每页条数，默认 `10` |

分页响应的 `data` 结构：

```json
{
  "list": [],
  "total": 0,
  "page": 1,
  "pageSize": 10
}
```

## 3. 公共数据模型

### 3.1 AdminUser

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `number` | 管理员ID |
| `username` | `string` | 登录用户名 |
| `displayName` | `string` | 管理员姓名 |
| `mobile` | `string` | 手机号 |
| `roleCodes` | `string[]` | 角色编码集合 |
| `roleNames` | `string[]` | 角色名称集合 |
| `permissions` | `string[]` | 当前有效权限编码集合 |
| `enabled` | `boolean` | 管理员是否启用 |
| `lastLoginAt` | `string/null` | 最近登录时间 |
| `lastLoginIp` | `string/null` | 最近登录IP |
| `createdAt` | `string` | 创建时间 |

### 3.2 Role

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `number` | 角色ID |
| `name` | `string` | 角色名称 |
| `code` | `string` | 唯一角色编码 |
| `description` | `string` | 角色说明 |
| `memberCount` | `number` | 关联管理员数量 |
| `permissionCodes` | `string[]` | 已分配权限编码集合 |
| `enabled` | `boolean` | 角色是否启用 |
| `createdAt` | `string` | 创建时间 |

### 3.3 Permission

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `number` | 权限ID |
| `parentId` | `number/null` | 上级权限ID，顶级节点为 `null` |
| `name` | `string` | 权限名称 |
| `code` | `string` | 唯一权限编码，例如 `admin:create` |
| `type` | `DIRECTORY/MENU/BUTTON` | 权限类型 |
| `path` | `string/null` | 路由地址，按钮权限为 `null` |
| `icon` | `string/null` | 菜单图标标识 |
| `sort` | `number` | 同级显示顺序 |
| `enabled` | `boolean` | 权限是否启用 |
| `children` | `Permission[]` | 下级权限集合，仅树接口返回 |

## 4. 认证接口

### 4.1 管理员登录

- **状态**：前端已预留，响应契约待调整
- **方法**：`POST`
- **路径**：`/api/v1/admin/auth/login`
- **鉴权**：不需要

请求体：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `username` | `string` | 是 | 管理员用户名 |
| `password` | `string` | 是 | 登录密码 |

响应数据：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `accessToken` | `string` | 访问令牌 |
| `tokenType` | `Bearer` | 令牌类型 |
| `expiresIn` | `number` | 有效期，单位为秒 |
| `user` | `AdminUser` | 当前管理员资料、角色和有效权限 |

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "accessToken": "eyJhbGciOi...",
    "tokenType": "Bearer",
    "expiresIn": 1800,
    "user": {
      "id": 1,
      "username": "admin",
      "displayName": "超级管理员",
      "mobile": "13800000000",
      "roleCodes": ["super_admin"],
      "roleNames": ["超级管理员"],
      "permissions": ["*:*:*"],
      "enabled": true,
      "lastLoginAt": "2026-08-25T09:30:00+08:00",
      "lastLoginIp": "127.0.0.1",
      "createdAt": "2026-08-01T10:00:00+08:00"
    }
  }
}
```

禁用管理员登录时返回业务失败响应，不签发令牌。

### 4.2 获取当前管理员

- **状态**：前端已预留
- **方法**：`GET`
- **路径**：`/api/v1/admin/auth/me`

响应 `data` 为最新的 `AdminUser`。该接口用于刷新页面后恢复会话，并同步管理员状态、角色和权限。

### 4.3 退出登录

- **状态**：前端已预留
- **方法**：`POST`
- **路径**：`/api/v1/admin/auth/logout`

无需请求体，成功响应的 `data` 为 `null`。后端应使当前令牌对应的会话失效。

## 5. 首页接口

### 5.1 获取首页统计

- **状态**：待后端实现
- **方法**：`GET`
- **路径**：`/api/v1/admin/dashboard/summary`

响应数据：

```json
{
  "admins": { "total": 12, "enabled": 10 },
  "roles": { "total": 5, "enabled": 4 },
  "permissions": { "total": 36, "enabled": 34 }
}
```

该接口只返回全局统计；当前管理员资料和最近登录信息由 `/auth/me` 或登录响应中的 `user` 提供。

## 6. 管理员接口

### 6.1 分页查询管理员

- **状态**：待后端实现
- **方法**：`GET`
- **路径**：`/api/v1/admin/admins`

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `keyword` | `string` | 否 | 模糊匹配用户名或管理员姓名 |
| `roleCode` | `string` | 否 | 按角色编码筛选 |
| `enabled` | `boolean` | 否 | 按启停状态筛选 |
| `page` | `number` | 否 | 页码 |
| `pageSize` | `number` | 否 | 每页条数 |

响应 `data` 为分页结构，`list` 元素为 `AdminUser`。

### 6.2 查询管理员详情

- **状态**：待后端实现
- **方法**：`GET`
- **路径**：`/api/v1/admin/admins/{id}`

响应 `data` 为 `AdminUser`。

### 6.3 新增管理员

- **状态**：待后端实现
- **方法**：`POST`
- **路径**：`/api/v1/admin/admins`

请求体：

```json
{
  "username": "mall_operator",
  "displayName": "商城运营",
  "mobile": "13800000001",
  "roleCodes": ["operator"],
  "enabled": true
}
```

响应 `data` 为创建后的 `AdminUser`。

### 6.4 编辑管理员

- **状态**：待后端实现
- **方法**：`PUT`
- **路径**：`/api/v1/admin/admins/{id}`

请求体包含 `username`、`displayName`、`mobile`、`roleCodes` 和 `enabled`，响应 `data` 为更新后的 `AdminUser`。

### 6.5 修改管理员状态

- **状态**：待后端实现
- **方法**：`PATCH`
- **路径**：`/api/v1/admin/admins/{id}/status`

```json
{
  "enabled": false
}
```

响应 `data` 为更新后的 `AdminUser`。管理员被禁用后不能再次登录，已有会话应在后续请求时失效。

### 6.6 分配管理员角色

- **状态**：待后端实现
- **方法**：`PUT`
- **路径**：`/api/v1/admin/admins/{id}/roles`

```json
{
  "roleCodes": ["operator", "auditor"]
}
```

该操作覆盖管理员原有角色，响应 `data` 为更新后的 `AdminUser`。

### 6.7 重置管理员密码

- **状态**：待后端实现
- **方法**：`POST`
- **路径**：`/api/v1/admin/admins/{id}/password/reset`

无需请求体。响应数据：

```json
{
  "temporaryPassword": "随机临时密码",
  "mustChangePassword": true
}
```

临时密码只在本次响应中返回。

## 7. 角色接口

### 7.1 分页查询角色

- **状态**：待后端实现
- **方法**：`GET`
- **路径**：`/api/v1/admin/roles`

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `keyword` | `string` | 否 | 模糊匹配角色名称或编码 |
| `enabled` | `boolean` | 否 | 按启停状态筛选 |
| `page` | `number` | 否 | 页码 |
| `pageSize` | `number` | 否 | 每页条数 |

响应 `data` 为分页结构，`list` 元素为 `Role`。

### 7.2 获取角色选项

- **状态**：待后端实现
- **方法**：`GET`
- **路径**：`/api/v1/admin/roles/options`

用于管理员编辑和角色分配下拉框，默认只返回已启用角色：

```json
[
  { "id": 2, "name": "商城运营", "code": "operator", "enabled": true }
]
```

### 7.3 查询角色详情

- **状态**：待后端实现
- **方法**：`GET`
- **路径**：`/api/v1/admin/roles/{id}`

响应 `data` 为 `Role`，其中 `permissionCodes` 为该角色已经分配的权限编码。

### 7.4 新增角色

- **状态**：待后端实现
- **方法**：`POST`
- **路径**：`/api/v1/admin/roles`

```json
{
  "name": "商城运营",
  "code": "operator",
  "description": "负责商城日常运营",
  "enabled": true
}
```

响应 `data` 为创建后的 `Role`。

### 7.5 编辑角色

- **状态**：待后端实现
- **方法**：`PUT`
- **路径**：`/api/v1/admin/roles/{id}`

请求体包含 `name`、`code`、`description` 和 `enabled`，响应 `data` 为更新后的 `Role`。

### 7.6 修改角色状态

- **状态**：待后端实现
- **方法**：`PATCH`
- **路径**：`/api/v1/admin/roles/{id}/status`

```json
{
  "enabled": false
}
```

响应 `data` 为更新后的 `Role`。停用角色后，该角色不再向管理员提供有效权限。

### 7.7 分配角色权限

- **状态**：待后端实现
- **方法**：`PUT`
- **路径**：`/api/v1/admin/roles/{id}/permissions`

```json
{
  "permissionCodes": ["system:admin", "admin:view", "admin:create"]
}
```

该操作覆盖角色原有权限，响应 `data` 为更新后的 `Role`。

## 8. 权限接口

### 8.1 查询权限树

- **状态**：待后端实现
- **方法**：`GET`
- **路径**：`/api/v1/admin/permissions/tree`

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `keyword` | `string` | 否 | 模糊匹配权限名称或编码；匹配子节点时保留其父级链路 |

响应 `data` 为 `Permission[]`，通过 `children` 形成目录、菜单、按钮三级树。

### 8.2 新增权限

- **状态**：待后端实现
- **方法**：`POST`
- **路径**：`/api/v1/admin/permissions`

```json
{
  "parentId": 11,
  "name": "新增管理员",
  "code": "admin:create",
  "type": "BUTTON",
  "path": null,
  "icon": null,
  "sort": 2,
  "enabled": true
}
```

响应 `data` 为创建后的 `Permission`。

### 8.3 编辑权限

- **状态**：待后端实现
- **方法**：`PUT`
- **路径**：`/api/v1/admin/permissions/{id}`

请求体包含 `parentId`、`name`、`code`、`type`、`path`、`icon`、`sort` 和 `enabled`，响应 `data` 为更新后的 `Permission`。

停用权限后，该权限不再出现在管理员的有效权限集合中，也不参与授权判断。

### 8.4 删除权限

- **状态**：待后端实现
- **方法**：`DELETE`
- **路径**：`/api/v1/admin/permissions/{id}`

成功响应的 `data` 为 `null`。存在子权限或仍被角色引用时，后端应拒绝删除并返回业务失败响应。

## 9. 联调注意事项

- 当前 `admin-web` 已预留 `/auth/login`、`/auth/me`、`/auth/logout` 三个请求；管理员、角色、权限和首页统计仍使用 Mock 数据。
- 登录响应需要在现有令牌信息之外增加 `user`，登录成功后可直接初始化管理员与权限状态。
- `/auth/me` 仍需保留，页面刷新或恢复本地令牌时通过它校验会话并获取最新权限。
- 当前前端模型中的 `status: "enabled" | "disabled"` 应在后续联调改为 `enabled: boolean`。
- 超级管理员等内置账号、角色的禁用限制应由后端校验，不能只依赖前端按钮禁用。
- 权限编码是前后端共同使用的鉴权标识，应保持全局唯一。
