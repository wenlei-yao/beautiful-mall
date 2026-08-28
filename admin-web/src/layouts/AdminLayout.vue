<script setup lang="ts">
import { computed, h, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useWindowSize } from '@vueuse/core';
import { Icon } from '@iconify/vue';
import {
  NAvatar,
  NBreadcrumb,
  NBreadcrumbItem,
  NButton,
  NDropdown,
  NDrawer,
  NDrawerContent,
  NIcon,
  NLayout,
  NLayoutContent,
  NLayoutHeader,
  NLayoutSider,
  NMenu,
  NScrollbar,
  NTab,
  NTabs,
  NTag,
  NTooltip,
  useMessage,
  type MenuOption
} from 'naive-ui';
import { useAuthStore } from '@/stores/auth';
import { useTabsStore } from '@/stores/tabs';
import { useThemeStore } from '@/stores/theme';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const tabsStore = useTabsStore();
const themeStore = useThemeStore();
const message = useMessage();
const collapsed = ref(false);
const mobileMenuVisible = ref(false);
const { width } = useWindowSize();
const isMobile = computed(() => width.value < 900);

const renderIcon = (name: string) => () => h(NIcon, null, { default: () => h(Icon, { icon: name }) });

const menuOptions: MenuOption[] = [
  {
    label: '首页',
    key: '/home',
    icon: renderIcon('solar:home-smile-angle-outline')
  },
  {
    label: '系统管理',
    key: 'system',
    icon: renderIcon('solar:settings-outline'),
    children: [
      { label: '管理员管理', key: '/system/admin', icon: renderIcon('solar:users-group-rounded-outline') },
      { label: '角色管理', key: '/system/role', icon: renderIcon('solar:shield-user-outline') },
      { label: '权限管理', key: '/system/permission', icon: renderIcon('solar:key-minimalistic-square-outline') }
    ]
  }
];

const userOptions = [
  { label: '账号设置（待开放）', key: 'profile', disabled: true, icon: renderIcon('solar:user-circle-outline') },
  { type: 'divider', key: 'divider' },
  { label: '退出登录', key: 'logout', icon: renderIcon('solar:logout-2-outline') }
];

watch(
  () => route.fullPath,
  () => {
    if (route.meta.title && route.name !== 'login') {
      tabsStore.addTab({ path: route.path, title: route.meta.title, closable: route.path !== '/home' });
    }
    if (isMobile.value) mobileMenuVisible.value = false;
  },
  { immediate: true }
);

function navigate(path: string) {
  router.push(path);
}

function closeTab(path: string) {
  const currentIndex = tabsStore.tabs.findIndex(tab => tab.path === path);
  tabsStore.removeTab(path);
  if (route.path === path) {
    const nextTab = tabsStore.tabs[Math.max(0, currentIndex - 1)] || tabsStore.tabs[0];
    router.push(nextTab.path);
  }
}

async function handleUserAction(key: string) {
  if (key !== 'logout') return;
  await authStore.signOut();
  message.success('已安全退出');
  await router.replace('/login');
}
</script>

<template>
  <NLayout class="admin-layout" :has-sider="!isMobile">
    <NLayoutSider
      v-if="!isMobile"
      class="sidebar"
      bordered
      collapse-mode="width"
      :collapsed-width="76"
      :width="232"
      :collapsed="collapsed"
      show-trigger="bar"
      @collapse="collapsed = true"
      @expand="collapsed = false"
    >
      <div class="brand" :class="{ compact: collapsed }">
        <div class="brand-mark">B</div>
        <div v-if="!collapsed" class="brand-copy">
          <strong>Beautiful Mall</strong>
          <span>管理后台</span>
        </div>
      </div>
      <NMenu
        :collapsed="collapsed"
        :collapsed-width="76"
        :collapsed-icon-size="22"
        :options="menuOptions"
        :value="route.path"
        :indent="20"
        @update:value="navigate"
      />
      <div v-if="!collapsed" class="sidebar-footer">
        <div class="sidebar-footer-icon"><Icon icon="solar:shield-check-outline" /></div>
        <div>
          <strong>安全会话</strong>
          <span>Mock 原型环境</span>
        </div>
      </div>
    </NLayoutSider>

    <NLayout class="main-layout">
      <NLayoutHeader class="top-header" bordered>
        <div class="header-left">
          <NButton quaternary circle aria-label="打开导航" @click="isMobile ? (mobileMenuVisible = true) : (collapsed = !collapsed)">
            <template #icon><Icon :icon="isMobile ? 'solar:hamburger-menu-outline' : 'solar:sidebar-minimalistic-outline'" /></template>
          </NButton>
          <NBreadcrumb class="desktop-breadcrumb">
            <NBreadcrumbItem>Beautiful Mall</NBreadcrumbItem>
            <NBreadcrumbItem v-if="route.meta.title">{{ route.meta.title }}</NBreadcrumbItem>
          </NBreadcrumb>
        </div>
        <div class="header-actions">
          <NTag v-if="!isMobile" size="small" round :bordered="false" type="warning">原型环境</NTag>
          <NTooltip trigger="hover">
            <template #trigger>
              <NButton quaternary circle aria-label="切换主题" @click="themeStore.toggleTheme">
                <template #icon><Icon :icon="themeStore.isDark ? 'solar:sun-2-outline' : 'solar:moon-outline'" /></template>
              </NButton>
            </template>
            {{ themeStore.isDark ? '切换浅色模式' : '切换暗色模式' }}
          </NTooltip>
          <NDropdown :options="userOptions" trigger="click" @select="handleUserAction">
            <button class="user-entry" type="button">
              <NAvatar round size="small" color="#646cff">管</NAvatar>
              <span v-if="!isMobile" class="user-copy">
                <strong>{{ authStore.user?.displayName || '管理员' }}</strong>
                <small>{{ authStore.user?.roleNames?.[0] || '后台账号' }}</small>
              </span>
              <Icon v-if="!isMobile" icon="solar:alt-arrow-down-linear" />
            </button>
          </NDropdown>
        </div>
      </NLayoutHeader>

      <div class="tab-strip">
        <NScrollbar x-scrollable>
          <NTabs type="card" size="small" :value="route.path" @update:value="navigate" @close="closeTab">
            <NTab v-for="tab in tabsStore.tabs" :key="tab.path" :name="tab.path" :closable="tab.closable">
              {{ tab.title }}
            </NTab>
          </NTabs>
        </NScrollbar>
      </div>

      <NLayoutContent class="content-area" content-style="min-height: 100%; padding: 20px;">
        <RouterView v-slot="{ Component }">
          <Transition name="page" mode="out-in">
            <component :is="Component" />
          </Transition>
        </RouterView>
      </NLayoutContent>
    </NLayout>

    <NDrawer v-model:show="mobileMenuVisible" placement="left" :width="280">
      <NDrawerContent :native-scrollbar="false" closable>
        <template #header>
          <div class="mobile-brand"><span class="brand-mark">B</span> Beautiful Mall</div>
        </template>
        <NMenu :options="menuOptions" :value="route.path" @update:value="navigate" />
      </NDrawerContent>
    </NDrawer>
  </NLayout>
</template>

<style scoped>
.admin-layout { height: 100vh; height: 100dvh; min-height: 0; overflow: hidden; background: var(--app-bg); }
.sidebar { height: 100%; }
.main-layout { height: 100%; min-width: 0; overflow: hidden; }
.brand { height: 64px; display: flex; align-items: center; gap: 11px; padding: 0 20px; overflow: hidden; }
.brand.compact { justify-content: center; padding: 0; }
.brand-mark { flex: 0 0 auto; width: 36px; height: 36px; display: grid; place-items: center; border-radius: 12px; color: #fff; font-size: 20px; font-weight: 800; background: linear-gradient(145deg, #7b82ff, #5159e8); box-shadow: 0 8px 18px rgba(100, 108, 255, 0.28); }
.brand-copy { min-width: 0; display: flex; flex-direction: column; line-height: 1.15; white-space: nowrap; }
.brand-copy strong { font-size: 15px; letter-spacing: -.01em; }
.brand-copy span { margin-top: 4px; color: var(--text-secondary); font-size: 10px; letter-spacing: .16em; }
.sidebar-footer { position: absolute; right: 16px; bottom: 18px; left: 16px; display: flex; align-items: center; gap: 10px; padding: 12px; border: 1px solid var(--border-color); border-radius: 12px; background: var(--app-bg); }
.sidebar-footer-icon { width: 30px; height: 30px; display: grid; place-items: center; border-radius: 9px; color: #646cff; background: rgba(100,108,255,.12); font-size: 18px; }
.sidebar-footer strong, .sidebar-footer span { display: block; }
.sidebar-footer strong { font-size: 12px; }
.sidebar-footer span { margin-top: 2px; color: var(--text-secondary); font-size: 10px; }
.top-header { height: 64px; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; background: var(--panel-bg); backdrop-filter: blur(16px); }
.header-left, .header-actions { display: flex; align-items: center; gap: 10px; }
.user-entry { display: flex; align-items: center; gap: 9px; margin: 0; padding: 5px 7px; border: 0; border-radius: 10px; color: inherit; background: transparent; cursor: pointer; }
.user-entry:hover { background: rgba(100,108,255,.08); }
.user-copy { display: flex; flex-direction: column; text-align: left; line-height: 1.2; }
.user-copy strong { font-size: 12px; }
.user-copy small { margin-top: 3px; color: var(--text-secondary); font-size: 10px; }
.tab-strip { height: 48px; display: flex; align-items: end; padding: 0 20px; border-bottom: 1px solid var(--border-color); background: var(--panel-bg); }
.tab-strip :deep(.n-scrollbar) { width: 100%; }
.tab-strip :deep(.n-tabs-nav) { border-bottom: 0 !important; }
.tab-strip :deep(.n-tabs-tab) { background: transparent; }
.content-area { height: calc(100vh - 112px); height: calc(100dvh - 112px); min-height: 0; background: var(--app-bg); }
.mobile-brand { display: flex; align-items: center; gap: 10px; font-weight: 700; }
.page-enter-active, .page-leave-active { transition: opacity .18s ease, transform .18s ease; }
.page-enter-from { opacity: 0; transform: translateY(5px); }
.page-leave-to { opacity: 0; transform: translateY(-3px); }
@media (max-width: 899px) {
  .top-header { padding: 0 12px; }
  .desktop-breadcrumb { display: none; }
  .tab-strip { padding: 0 12px; }
  .content-area :deep(.n-layout-scroll-container) { padding: 14px !important; }
}
</style>
