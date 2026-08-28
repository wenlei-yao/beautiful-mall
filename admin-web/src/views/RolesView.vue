<script setup lang="ts">
import { h, reactive, ref } from 'vue';
import { Icon } from '@iconify/vue';
import {
  NAlert, NButton, NCard, NDataTable, NDrawer, NDrawerContent, NForm, NFormItem, NInput, NModal,
  NRadioButton, NRadioGroup, NSpace, NTag, NTree, useMessage, type DataTableColumns, type FormInst, type FormRules, type TreeOption
} from 'naive-ui';
import { mockPermissions, mockRoles } from '@/mock/data';
import type { Permission, Role } from '@/types/admin';

const message = useMessage();
const formRef = ref<FormInst | null>(null);
const editorVisible = ref(false);
const permissionVisible = ref(false);
const editingId = ref<number | null>(null);
const keyword = ref('');
const form = reactive({ name: '', code: '', description: '', status: 'enabled' });
const permissionForm = reactive({ roleName: '', checkedKeys: [] as string[] });
const rules: FormRules = {
  name: { required: true, message: '请输入角色名称', trigger: ['input', 'blur'] },
  code: { required: true, pattern: /^[a-z][a-z0-9_]*$/, message: '使用小写字母、数字和下划线', trigger: ['input', 'blur'] }
};

function toTreeOptions(items: Permission[]): TreeOption[] {
  return items.map(item => ({ key: item.code, label: `${item.name} · ${item.code}`, children: item.children ? toTreeOptions(item.children) : undefined }));
}
const permissionOptions = toTreeOptions(mockPermissions);

function openEditor(role?: Role) {
  editingId.value = role?.id ?? null;
  Object.assign(form, role ? { name: role.name, code: role.code, description: role.description, status: role.status } : { name: '', code: '', description: '', status: 'enabled' });
  editorVisible.value = true;
}

function openPermissions(role: Role) {
  permissionForm.roleName = role.name;
  permissionForm.checkedKeys = role.permissions.includes('*:*:*') ? permissionOptions.flatMap(item => [String(item.key), ...((item.children || []).flatMap(child => [String(child.key), ...((child.children || []).map(button => String(button.key)))]))]) : [...role.permissions];
  permissionVisible.value = true;
}

async function submitEditor() {
  try {
    await formRef.value?.validate();
    message.success(editingId.value ? '角色修改已完成（原型模式）' : '角色添加已完成（原型模式）');
    editorVisible.value = false;
  } catch { /* 字段错误已展示 */ }
}

function submitPermissions() {
  message.success(`已更新“${permissionForm.roleName}”的菜单与按钮权限（原型模式）`);
  permissionVisible.value = false;
}

const columns: DataTableColumns<Role> = [
  { title: '角色名称', key: 'name', width: 150, render: row => h('div', { class: 'role-name' }, [h('span', null, row.name), row.code === 'super_admin' ? h(NTag, { size: 'tiny', type: 'warning', bordered: false }, { default: () => '内置' }) : null]) },
  { title: '角色编码', key: 'code', width: 150, render: row => h('code', { class: 'code-label' }, row.code) },
  { title: '说明', key: 'description', minWidth: 220 },
  { title: '成员数', key: 'memberCount', width: 90, render: row => `${row.memberCount} 人` },
  { title: '状态', key: 'status', width: 90, render: row => h(NTag, { type: row.status === 'enabled' ? 'success' : 'default', size: 'small', round: true, bordered: false }, { default: () => row.status === 'enabled' ? '启用' : '停用' }) },
  { title: '创建时间', key: 'createdAt', width: 170 },
  { title: '操作', key: 'actions', width: 205, fixed: 'right', render: row => h(NSpace, { size: 3 }, () => [
    h(NButton, { text: true, size: 'small', type: 'primary', onClick: () => openEditor(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, size: 'small', type: 'primary', onClick: () => openPermissions(row) }, { default: () => '分配权限' }),
    h(NButton, { text: true, size: 'small', type: row.status === 'enabled' ? 'error' : 'success', disabled: row.code === 'super_admin', onClick: () => message.success('角色状态已更新（原型模式）') }, { default: () => row.status === 'enabled' ? '停用' : '启用' })
  ]) }
];
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><h1>角色管理</h1><p>通过角色组合菜单和按钮权限，再分配给管理员。</p></div>
      <NButton v-permission="'role:create'" type="primary" @click="openEditor()"><template #icon><Icon icon="solar:shield-plus-outline" /></template>新增角色</NButton>
    </div>
    <NAlert type="info" :bordered="false" closable>权限勾选和角色编辑为交互原型，提交后不改变演示数据。</NAlert>
    <NCard class="soft-card" :bordered="false">
      <div class="role-filter"><NInput v-model:value="keyword" clearable placeholder="搜索角色名称或编码"><template #prefix><Icon icon="solar:magnifer-outline" /></template></NInput><NButton type="primary">查询</NButton><NButton @click="keyword = ''">重置</NButton></div>
    </NCard>
    <NCard class="soft-card" :bordered="false" title="角色列表">
      <NDataTable :columns="columns" :data="mockRoles.filter(role => !keyword || role.name.includes(keyword) || role.code.includes(keyword))" :row-key="row => row.id" :scroll-x="1050" />
    </NCard>

    <NModal v-model:show="editorVisible" preset="card" :title="editingId ? '编辑角色' : '新增角色'" class="role-editor" :bordered="false">
      <NForm ref="formRef" :model="form" :rules="rules" label-placement="top">
        <NFormItem label="角色名称" path="name"><NInput v-model:value="form.name" placeholder="如 商城运营" /></NFormItem>
        <NFormItem label="角色编码" path="code"><NInput v-model:value="form.code" :disabled="editingId === 1" placeholder="如 mall_operator" /></NFormItem>
        <NFormItem label="角色说明" path="description"><NInput v-model:value="form.description" type="textarea" :rows="3" placeholder="说明该角色的职责边界" /></NFormItem>
        <NFormItem label="角色状态"><NRadioGroup v-model:value="form.status"><NRadioButton value="enabled">启用</NRadioButton><NRadioButton value="disabled">停用</NRadioButton></NRadioGroup></NFormItem>
      </NForm>
      <template #footer><NSpace justify="end"><NButton @click="editorVisible = false">取消</NButton><NButton type="primary" @click="submitEditor">保存</NButton></NSpace></template>
    </NModal>

    <NDrawer v-model:show="permissionVisible" placement="right" :width="520">
      <NDrawerContent closable title="分配菜单与按钮权限">
        <div class="drawer-intro"><span>当前角色</span><strong>{{ permissionForm.roleName }}</strong><small>勾选目录、菜单及页面按钮，权限将在重新登录后生效。</small></div>
        <NTree v-model:checked-keys="permissionForm.checkedKeys" block-line cascade checkable default-expand-all :data="permissionOptions" />
        <template #footer><NSpace justify="end"><NButton @click="permissionVisible = false">取消</NButton><NButton type="primary" @click="submitPermissions">保存权限</NButton></NSpace></template>
      </NDrawerContent>
    </NDrawer>
  </div>
</template>

<style scoped>
.role-filter { display:grid;grid-template-columns:minmax(240px,420px) auto auto;gap:12px;justify-content:start; }.role-editor { width:min(520px,calc(100vw - 28px)); }.drawer-intro { display:flex;flex-direction:column;gap:6px;margin-bottom:18px;padding:15px;border-radius:13px;background:var(--app-bg); }.drawer-intro span,.drawer-intro small { color:var(--text-secondary);font-size:11px; }.drawer-intro strong { font-size:15px; }
:deep(.role-name) { display:flex;align-items:center;gap:7px;font-weight:600; }:deep(.code-label) { padding:3px 7px;border-radius:6px;color:#646cff;background:rgba(100,108,255,.09);font-family:inherit;font-size:11px; }
@media(max-width:600px){.role-filter{grid-template-columns:1fr 1fr}.role-filter .n-input{grid-column:1/-1}}
</style>

