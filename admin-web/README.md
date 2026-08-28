# admin-web

Beautiful Mall 商城管理后台前端原型，使用 Vue 3、TypeScript、Vite、Naive UI、Pinia、Vue Router 与 UnoCSS 构建。

## 本地运行

```bash
npm install
npm run dev
```

原型账号：

```text
用户名：admin
密码：Admin@123456
```

## 可用命令

```bash
npm run typecheck
npm run lint
npm run test
npm run build
```

默认使用 Mock 认证。后端接口准备完成后，将 `VITE_USE_MOCK` 设置为 `false`，并通过 `VITE_API_BASE_URL` 配置网关地址。

