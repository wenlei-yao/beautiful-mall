<script setup lang="ts">
import { computed, h, reactive, ref } from 'vue';
import { Icon } from '@iconify/vue';
import {
  NAlert,
  NButton,
  NCard,
  NDataTable,
  NForm,
  NFormItem,
  NFormItemGi,
  NGrid,
  NInput,
  NModal,
  NRadioButton,
  NRadioGroup,
  NSelect,
  NSpace,
  NTag,
  useDialog,
  useMessage,
  type DataTableColumns,
  type FormInst,
  type FormRules
} from 'naive-ui';
import { mockAdmins, mockRoles } from '@/mock/data';
import type { AdminUser } from '@/types/admin';

const message = useMessage();
const dialog = useDialog();
const formRef = ref<FormInst | null>(null);
const editorVisible = ref(false);
const roleVisible = ref(false);
const editingId = ref<number | null>(null);
const query = reactive({ keyword: '', status: null as string | null, role: null as string | null });
const form = reactive({ username: '', displayName: '', mobile: '', roleCodes: [] as string[], status: 'enabled' });
const roleAssignment = reactive({ adminName: '', roleCodes: [] as string[] });

const statusOptions = [
  { label: '正常', value: 'enabled' },
  { label: '禁用', value: 'disabled' }
];
const roleOptions = mockRoles.map(role => ({ label: role.name, value: role.code }));
const rules: FormRules = {
  username: [
    { required: true, message: '请输入登录用户名', trigger: ['input', 'blur'] },
    { min: 3, message: '用户名至少3个字符', trigger: 'blur' }
  ],
  displayName: { required: true, message: '请输入管理员姓名', trigger: ['input', 'blur'] },
  mobile: { required: true, pattern: /^1\d{10}$/, message: '请输入正确的11位手机号', trigger: ['input', 'blur'] },
  roleCodes: { type: 'array', required: true, min: 1, message: '请至少分配一个角色', trigger: 'change' }
};

const filteredAdmins = computed(() => mockAdmins.filter(admin => {
  const keyword = query.keyword.trim().toLowerCase();
  const matchesKeyword = !keyword || admin.username.toLowerCase().includes(keyword) || admin.displayName.toLowerCase().includes(keyword);
  const matchesStatus = !query.status || admin.status === query.status;
  const matchesRole = !query.role || admin.roleCodes.includes(query.role);
  return matchesKeyword && matchesStatus && matchesRole;
}));

function openEditor(admin?: AdminUser) {
  editingId.value = admin?.id ?? null;
  Object.assign(form, admin
    ? { username: admin.username, displayName: admin.displayName, mobile: admin.mobile.replace(/\*/g, '0'), roleCodes: [...admin.roleCodes], status: admin.status }
    : { username: '', displayName: '', mobile: '', roleCodes: [], status: 'enabled' });
  editorVisible.value = true;
}

function openRoleAssignment(admin: AdminUser) {
  roleAssignment.adminName = admin.displayName;
  roleAssignment.roleCodes = [...admin.roleCodes];
  roleVisible.value = true;
}

async function submitEditor() {
  try {
    await formRef.value?.validate();
    message.success(editingId.value ? '管理员修改已完成（原型模式）' : '管理员添加已完成（原型模式）');
    editorVisible.value = false;
  } catch {
    // Naive UI 已展示字段校验结果。
  }
}

function submitRoleAssignment() {
  if (!roleAssignment.roleCodes.length) {
    message.warning('请至少选择一个角色');
    return;
  }
  message.success(`已为${roleAssignment.adminName}更新角色（原型模式）`);
  roleVisible.value = false;
}

function confirmReset(admin: AdminUser) {
  dialog.warning({
    title: '确认重置密码',
    content: `将为“${admin.displayName}”生成临时密码，并要求下次登录时修改。`,
    positiveText: '确认重置',
    negativeText: '取消',
    onPositiveClick: () => message.success('临时密码已生成（原型模式）')
  });
}

function resetQuery() {
  Object.assign(query, { keyword: '', status: null, role: null });
}

const columns: DataTableColumns<AdminUser> = [
  { title: '管理员', key: 'displayName', width: 190, render: row => h('div', { class: 'person-cell' }, [h('span', { class: 'person-avatar' }, row.displayName.slice(0, 1)), h('span', null, [h('strong', null, row.displayName), h('small', null, `@${row.username}`)])]) },
  { title: '手机号', key: 'mobile', width: 130 },
  { title: '角色', key: 'roleNames', minWidth: 160, render: row => h(NSpace, { size: 5 }, () => row.roleNames.map(role => h(NTag, { size: 'small', bordered: false, round: true }, { default: () => role }))) },
  { title: '状态', key: 'status', width: 90, render: row => h(NTag, { type: row.status === 'enabled' ? 'success' : 'default', size: 'small', round: true, bordered: false }, { default: () => row.status === 'enabled' ? '正常' : '禁用' }) },
  { title: '最近登录', key: 'lastLoginAt', width: 170, render: row => h('div', { class: 'login-cell' }, [h('span', null, row.lastLoginAt), h('small', null, row.lastLoginIp)]) },
  {
    title: '操作', key: 'actions', width: 255, fixed: 'right', render: row => h(NSpace, { size: 2 }, () => [
      h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => openEditor(row) }, { default: () => '编辑' }),
      h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => openRoleAssignment(row) }, { default: () => '分配角色' }),
      h(NButton, { text: true, size: 'small', onClick: () => confirmReset(row) }, { default: () => '重置密码' }),
      h(NButton, { text: true, type: row.status === 'enabled' ? 'error' : 'success', size: 'small', disabled: row.username === 'admin', onClick: () => message.success(`${row.status === 'enabled' ? '禁用' : '启用'}成功（原型模式）`) }, { default: () => row.status === 'enabled' ? '禁用' : '启用' })
    ])
  }
];
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><h1>管理员管理</h1><p>维护后台登录账号、账号状态和所属角色。</p></div>
      <NButton v-permission="'admin:create'" type="primary" @click="openEditor()">
        <template #icon><Icon icon="solar:user-plus-rounded-outline" /></template>新增管理员
      </NButton>
    </div>

    <NAlert type="info" :bordered="false" closable>
      当前为交互原型，提交操作会展示反馈，但不会修改演示数据。
    </NAlert>

    <NCard class="soft-card" :bordered="false">
      <div class="filter-row">
        <NInput v-model:value="query.keyword" clearable placeholder="搜索用户名或姓名" class="filter-keyword">
          <template #prefix><Icon icon="solar:magnifer-outline" /></template>
        </NInput>
        <NSelect v-model:value="query.role" clearable placeholder="全部角色" :options="roleOptions" />
        <NSelect v-model:value="query.status" clearable placeholder="全部状态" :options="statusOptions" />
        <NButton type="primary">查询</NButton>
        <NButton @click="resetQuery">重置</NButton>
      </div>
    </NCard>

    <NCard class="soft-card table-card" :bordered="false">
      <template #header><span class="table-title">管理员列表</span><span class="table-count">共 {{ filteredAdmins.length }} 人</span></template>
      <NDataTable :columns="columns" :data="filteredAdmins" :row-key="row => row.id" :scroll-x="1100" :pagination="{ pageSize: 8 }" />
    </NCard>

    <NModal v-model:show="editorVisible" preset="card" :title="editingId ? '编辑管理员' : '新增管理员'" class="editor-modal" :bordered="false">
      <NForm ref="formRef" :model="form" :rules="rules" label-placement="top">
        <NGrid :cols="2" :x-gap="16" responsive="screen" item-responsive>
          <NFormItemGi span="2 m:1" label="登录用户名" path="username"><NInput v-model:value="form.username" placeholder="如 mall_operator" /></NFormItemGi>
          <NFormItemGi span="2 m:1" label="管理员姓名" path="displayName"><NInput v-model:value="form.displayName" placeholder="请输入姓名" /></NFormItemGi>
          <NFormItemGi span="2 m:1" label="手机号" path="mobile"><NInput v-model:value="form.mobile" maxlength="11" placeholder="请输入手机号" /></NFormItemGi>
          <NFormItemGi span="2 m:1" label="账号状态" path="status"><NRadioGroup v-model:value="form.status"><NRadioButton value="enabled">正常</NRadioButton><NRadioButton value="disabled">禁用</NRadioButton></NRadioGroup></NFormItemGi>
          <NFormItemGi span="2" label="所属角色" path="roleCodes"><NSelect v-model:value="form.roleCodes" multiple placeholder="请选择角色" :options="roleOptions" /></NFormItemGi>
        </NGrid>
      </NForm>
      <template #footer><NSpace justify="end"><NButton @click="editorVisible = false">取消</NButton><NButton type="primary" @click="submitEditor">保存</NButton></NSpace></template>
    </NModal>

    <NModal v-model:show="roleVisible" preset="card" title="分配角色" class="role-modal" :bordered="false">
      <NForm label-placement="top">
        <NFormItem label="管理员"><NInput :value="roleAssignment.adminName" disabled /></NFormItem>
        <NFormItem label="所属角色" required><NSelect v-model:value="roleAssignment.roleCodes" multiple :options="roleOptions" /></NFormItem>
      </NForm>
      <template #footer><NSpace justify="end"><NButton @click="roleVisible = false">取消</NButton><NButton type="primary" @click="submitRoleAssignment">保存角色</NButton></NSpace></template>
    </NModal>
  </div>
</template>

<style scoped>
.filter-row { display:grid;grid-template-columns:minmax(220px,1.4fr) minmax(150px,.75fr) minmax(140px,.65fr) auto auto;gap:12px; }.table-card :deep(.n-card-header__main) { display:flex;align-items:center;gap:9px; }.table-title { font-size:15px;font-weight:650; }.table-count { color:var(--text-secondary);font-size:11px;font-weight:400; }.editor-modal { width:min(680px,calc(100vw - 28px)); }.role-modal { width:min(480px,calc(100vw - 28px)); }
:deep(.person-cell) { display:flex;align-items:center;gap:10px; }:deep(.person-avatar) { width:34px;height:34px;display:grid;place-items:center;border-radius:11px;color:#646cff;background:rgba(100,108,255,.11);font-size:13px;font-weight:700; }:deep(.person-cell strong),:deep(.person-cell small),:deep(.login-cell span),:deep(.login-cell small) { display:block; }:deep(.person-cell strong) { font-size:12px; }:deep(.person-cell small),:deep(.login-cell small) { margin-top:3px;color:var(--text-secondary);font-size:10px; }:deep(.login-cell span) { font-size:11px; }
@media (max-width:760px) { .filter-row { grid-template-columns:1fr 1fr; }.filter-keyword { grid-column:1/-1; } }
</style>

