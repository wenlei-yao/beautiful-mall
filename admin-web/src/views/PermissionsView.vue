<script setup lang="ts">
import { h, reactive, ref } from 'vue';
import { Icon } from '@iconify/vue';
import {
  NAlert, NButton, NCard, NDataTable, NForm, NFormItem, NInput, NInputNumber, NModal,
  NRadioButton, NRadioGroup, NSelect, NSpace, NTag, useDialog, useMessage, type DataTableColumns, type FormInst, type FormRules
} from 'naive-ui';
import { mockPermissions } from '@/mock/data';
import type { Permission, PermissionType } from '@/types/admin';

const message = useMessage();
const dialog = useDialog();
const formRef = ref<FormInst | null>(null);
const modalVisible = ref(false);
const editingId = ref<number | null>(null);
const keyword = ref('');
const form = reactive({ parentId: null as number | null, name: '', code: '', type: 'MENU' as PermissionType, path: '', sort: 1, status: 'enabled' });
const typeOptions = [
  { label: '目录', value: 'DIRECTORY' },
  { label: '菜单', value: 'MENU' },
  { label: '按钮', value: 'BUTTON' }
];

function flattenOptions(items: Permission[], depth = 0): { label: string; value: number }[] {
  return items.flatMap(item => [{ label: `${'　'.repeat(depth)}${item.name}`, value: item.id }, ...(item.children ? flattenOptions(item.children, depth + 1) : [])]);
}
const parentOptions = [{ label: '顶级权限', value: 0 }, ...flattenOptions(mockPermissions)];
const rules: FormRules = {
  name: { required: true, message: '请输入权限名称', trigger: ['input', 'blur'] },
  code: { required: true, pattern: /^[a-z*][a-z0-9_*-]*(:[a-z0-9_*-]+){0,2}$/, message: '请输入规范的权限编码', trigger: ['input', 'blur'] },
  type: { required: true, message: '请选择权限类型', trigger: 'change' }
};

function openEditor(permission?: Permission, parent?: Permission) {
  editingId.value = permission?.id ?? null;
  Object.assign(form, permission
    ? { parentId: permission.parentId ?? 0, name: permission.name, code: permission.code, type: permission.type, path: permission.path || '', sort: permission.sort, status: permission.status }
    : { parentId: parent?.id ?? 0, name: '', code: '', type: parent?.type === 'MENU' ? 'BUTTON' : 'MENU', path: '', sort: 1, status: 'enabled' });
  modalVisible.value = true;
}

async function submit() {
  try {
    await formRef.value?.validate();
    message.success(editingId.value ? '权限修改已完成（原型模式）' : '权限添加已完成（原型模式）');
    modalVisible.value = false;
  } catch { /* 字段错误已展示 */ }
}

function confirmDelete(permission: Permission) {
  dialog.warning({
    title: '确认删除权限',
    content: permission.children?.length ? `“${permission.name}”包含子权限，原型中仅展示删除流程。` : `确认删除“${permission.name}”吗？`,
    positiveText: '确认删除',
    negativeText: '取消',
    onPositiveClick: () => message.success('权限已删除（原型模式）')
  });
}

const typeStyle: Record<PermissionType, { label: string; type: 'info' | 'success' | 'warning' }> = {
  DIRECTORY: { label: '目录', type: 'info' },
  MENU: { label: '菜单', type: 'success' },
  BUTTON: { label: '按钮', type: 'warning' }
};

function renderExpandIcon({ expanded }: { expanded: boolean }) {
  return h(Icon, {
    icon: expanded ? 'solar:alt-arrow-down-linear' : 'solar:alt-arrow-right-linear',
    class: 'permission-expand-icon'
  });
}

function rowClassName(row: Permission) {
  return `permission-row permission-row--${row.type.toLowerCase()}`;
}

const columns: DataTableColumns<Permission> = [
  {
    title: '权限名称',
    key: 'name',
    minWidth: 270,
    render: row => h('div', { class: ['permission-name', `permission-name--${row.type.toLowerCase()}`] }, [
      h('span', { class: 'permission-node-icon' }, [
        row.icon
          ? h(Icon, { icon: row.icon })
          : h(Icon, { icon: row.type === 'BUTTON' ? 'solar:cursor-square-outline' : 'solar:menu-dots-square-outline' })
      ]),
      h('span', { class: 'permission-node-label' }, row.name)
    ])
  },
  { title: '类型', key: 'type', width: 90, render: row => h(NTag, { type: typeStyle[row.type].type, bordered: false, round: true, size: 'small' }, { default: () => typeStyle[row.type].label }) },
  { title: '权限编码', key: 'code', minWidth: 180, render: row => h('code', { class: 'code-label' }, row.code) },
  { title: '路由地址', key: 'path', minWidth: 170, render: row => row.path || '—' },
  { title: '排序', key: 'sort', width: 70 },
  { title: '状态', key: 'status', width: 85, render: row => h(NTag, { type: row.status === 'enabled' ? 'success' : 'default', bordered: false, size: 'small' }, { default: () => row.status === 'enabled' ? '正常' : '停用' }) },
  { title: '操作', key: 'actions', width: 175, fixed: 'right', render: row => h(NSpace, { size: 3 }, () => [
    row.type !== 'BUTTON' ? h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => openEditor(undefined, row) }, { default: () => '新增下级' }) : null,
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => openEditor(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => confirmDelete(row) }, { default: () => '删除' })
  ]) }
];

function filterTree(items: Permission[]): Permission[] {
  const value = keyword.value.trim().toLowerCase();
  if (!value) return items;
  return items.reduce<Permission[]>((result, item) => {
    const children = item.children ? filterTree(item.children) : [];
    if (item.name.toLowerCase().includes(value) || item.code.toLowerCase().includes(value) || children.length) result.push({ ...item, children });
    return result;
  }, []);
}
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><h1>权限管理</h1><p>维护目录、菜单与按钮权限，权限编码将作为前后端鉴权标识。</p></div>
      <NButton v-permission="'permission:create'" type="primary" @click="openEditor()"><template #icon><Icon icon="solar:add-square-outline" /></template>新增权限</NButton>
    </div>
    <NAlert type="info" :bordered="false" closable>权限树为静态演示数据，编辑、添加和删除暂不持久化。</NAlert>
    <NCard class="soft-card" :bordered="false">
      <div class="permission-filter"><NInput v-model:value="keyword" clearable placeholder="搜索权限名称或编码"><template #prefix><Icon icon="solar:magnifer-outline" /></template></NInput><NButton type="primary">查询</NButton><NButton @click="keyword = ''">重置</NButton></div>
    </NCard>
    <NCard class="soft-card tree-card" :bordered="false" title="权限树">
      <template #header-extra>
        <div class="tree-legend" aria-label="权限层级说明">
          <span class="legend-directory">目录</span><Icon icon="solar:alt-arrow-right-linear" />
          <span class="legend-menu">菜单</span><Icon icon="solar:alt-arrow-right-linear" />
          <span class="legend-button">按钮</span>
        </div>
      </template>
      <NDataTable
        class="permission-tree-table"
        default-expand-all
        :indent="28"
        :columns="columns"
        :data="filterTree(mockPermissions)"
        :row-key="row => row.id"
        :row-class-name="rowClassName"
        :render-expand-icon="renderExpandIcon"
        :scroll-x="1120"
      />
    </NCard>

    <NModal v-model:show="modalVisible" preset="card" :title="editingId ? '编辑权限' : '新增权限'" class="permission-modal" :bordered="false">
      <NForm ref="formRef" :model="form" :rules="rules" label-placement="top">
        <NFormItem label="上级权限"><NSelect v-model:value="form.parentId" :options="parentOptions" /></NFormItem>
        <NFormItem label="权限类型" path="type"><NRadioGroup v-model:value="form.type"><NRadioButton v-for="option in typeOptions" :key="option.value" :value="option.value">{{ option.label }}</NRadioButton></NRadioGroup></NFormItem>
        <NFormItem label="权限名称" path="name"><NInput v-model:value="form.name" placeholder="请输入权限名称" /></NFormItem>
        <NFormItem label="权限编码" path="code"><NInput v-model:value="form.code" placeholder="如 admin:create" /></NFormItem>
        <NFormItem v-if="form.type !== 'BUTTON'" label="路由地址"><NInput v-model:value="form.path" placeholder="如 /system/admin" /></NFormItem>
        <div class="form-inline"><NFormItem label="显示排序"><NInputNumber v-model:value="form.sort" :min="1" :max="999" /></NFormItem><NFormItem label="状态"><NRadioGroup v-model:value="form.status"><NRadioButton value="enabled">正常</NRadioButton><NRadioButton value="disabled">停用</NRadioButton></NRadioGroup></NFormItem></div>
      </NForm>
      <template #footer><NSpace justify="end"><NButton @click="modalVisible = false">取消</NButton><NButton type="primary" @click="submit">保存</NButton></NSpace></template>
    </NModal>
  </div>
</template>

<style scoped>
.permission-filter { display:grid;grid-template-columns:minmax(250px,440px) auto auto;gap:12px;justify-content:start; }.permission-modal { width:min(540px,calc(100vw - 28px)); }.form-inline { display:grid;grid-template-columns:1fr 1fr;gap:16px; }
.tree-legend { display:flex;align-items:center;gap:6px;color:var(--text-secondary);font-size:10px; }.tree-legend > span { padding:3px 8px;border-radius:999px;font-weight:600; }.tree-legend > svg { font-size:12px; }.legend-directory { color:#4f6fdc;background:rgba(79,111,220,.11); }.legend-menu { color:#15936b;background:rgba(21,147,107,.11); }.legend-button { color:#d88925;background:rgba(216,137,37,.12); }
:deep(.permission-name) { display:inline-flex;align-items:center;gap:9px;height:28px;vertical-align:middle;font-weight:600; }:deep(.permission-node-icon) { width:27px;height:27px;display:inline-grid;place-items:center;border-radius:8px;color:#646cff;background:rgba(100,108,255,.1);font-size:16px; }:deep(.permission-name--directory .permission-node-icon) { color:#4f6fdc;background:rgba(79,111,220,.12); }:deep(.permission-name--menu .permission-node-icon) { color:#15936b;background:rgba(21,147,107,.11); }:deep(.permission-name--button .permission-node-icon) { width:23px;height:23px;border-radius:7px;color:#d88925;background:rgba(216,137,37,.11);font-size:14px; }:deep(.permission-name--button .permission-node-label) { color:var(--text-secondary);font-weight:500; }
:deep(.permission-tree-table .n-data-table-expand-trigger), :deep(.permission-tree-table .n-data-table-expand-placeholder) { width:22px;height:28px;margin-right:5px;align-items:center;justify-content:center;vertical-align:middle; }:deep(.permission-tree-table .n-data-table-expand-trigger) { border-radius:7px;color:#646cff;background:rgba(100,108,255,.08); }:deep(.permission-tree-table .n-data-table-expand-trigger:hover) { background:rgba(100,108,255,.16); }:deep(.permission-tree-table .permission-expand-icon) { width:15px;height:15px; }
:deep(.permission-tree-table .n-data-table-indent) { position:relative;height:28px;vertical-align:middle; }:deep(.permission-tree-table .n-data-table-indent::after) { content:"";position:absolute;top:-17px;bottom:-17px;left:13px;border-left:1px dashed rgba(100,108,255,.24); }:deep(.permission-tree-table td:has(.n-data-table-indent) .n-data-table-expand-trigger::before), :deep(.permission-tree-table td:has(.n-data-table-indent) .n-data-table-expand-placeholder::before) { content:"";position:absolute;top:13px;left:-14px;width:14px;border-top:1px dashed rgba(100,108,255,.24); }
:deep(.permission-tree-table .permission-row--directory > td) { background:rgba(100,108,255,.025); }:deep(.permission-tree-table .permission-row--directory .permission-node-label) { font-weight:700; }:deep(.permission-tree-table .permission-row--button > td:first-child) { color:var(--text-secondary); }
:deep(.code-label) { padding:3px 7px;border-radius:6px;color:#646cff;background:rgba(100,108,255,.09);font-family:inherit;font-size:11px; }
@media(max-width:600px){.permission-filter{grid-template-columns:1fr 1fr}.permission-filter .n-input{grid-column:1/-1}.form-inline{grid-template-columns:1fr}}
</style>
