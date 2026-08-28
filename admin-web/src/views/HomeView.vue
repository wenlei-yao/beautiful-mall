<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { Icon } from '@iconify/vue';
import { NAvatar, NButton, NCard, NProgress, NSpace, NTag } from 'naive-ui';
import { mockAdmins, mockPermissions, mockRoles } from '@/mock/data';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();

function countPermissions(items = mockPermissions): number {
  return items.reduce((total, item) => total + 1 + (item.children ? countPermissions(item.children) : 0), 0);
}

const stats = computed(() => [
  { label: '管理员', value: mockAdmins.length, hint: `${mockAdmins.filter(item => item.status === 'enabled').length} 个账号正常`, icon: 'solar:users-group-rounded-outline', color: '#646cff', bg: 'rgba(100,108,255,.12)' },
  { label: '角色', value: mockRoles.length, hint: `${mockRoles.filter(item => item.status === 'enabled').length} 个角色启用`, icon: 'solar:shield-user-outline', color: '#19a974', bg: 'rgba(25,169,116,.12)' },
  { label: '权限节点', value: countPermissions(), hint: '菜单与按钮权限', icon: 'solar:key-minimalistic-square-outline', color: '#f59e42', bg: 'rgba(245,158,66,.13)' }
]);

const shortcuts = [
  { title: '管理员管理', description: '添加账号、调整状态与分配角色', path: '/system/admin', icon: 'solar:users-group-rounded-outline', tone: 'violet' },
  { title: '角色管理', description: '维护角色并配置菜单、按钮权限', path: '/system/role', icon: 'solar:shield-user-outline', tone: 'green' },
  { title: '权限管理', description: '查看系统权限树和权限编码', path: '/system/permission', icon: 'solar:key-minimalistic-square-outline', tone: 'orange' }
];

</script>

<template>
  <div class="page-shell home-page">
    <section class="welcome-card">
      <div class="welcome-copy">
        <NTag round :bordered="false" size="small">工作台</NTag>
        <h1>晚上好，{{ authStore.user?.displayName }} 👋</h1>
        <p>人员与权限配置清晰可控，系统当前运行正常。</p>
        <NSpace>
          <NButton type="primary" @click="router.push('/system/admin')">
            管理管理员
            <template #icon><Icon icon="solar:arrow-right-outline" /></template>
          </NButton>
          <NButton secondary @click="router.push('/system/role')">查看角色</NButton>
        </NSpace>
      </div>
      <div class="welcome-visual" aria-hidden="true">
        <div class="orbit orbit-a"></div><div class="orbit orbit-b"></div>
        <div class="visual-shield"><Icon icon="solar:shield-check-bold-duotone" /></div>
        <span class="visual-dot dot-a"></span><span class="visual-dot dot-b"></span>
      </div>
    </section>

    <section class="stats-grid">
      <NCard v-for="stat in stats" :key="stat.label" class="soft-card stat-card" :bordered="false">
        <div class="stat-icon" :style="{ color: stat.color, background: stat.bg }"><Icon :icon="stat.icon" /></div>
        <div><span>{{ stat.label }}</span><strong>{{ stat.value }}</strong><small>{{ stat.hint }}</small></div>
      </NCard>
    </section>

    <section class="home-grid">
      <NCard class="soft-card" :bordered="false" title="快捷入口">
        <template #header-extra><span class="section-extra">系统管理</span></template>
        <div class="shortcut-list">
          <button v-for="shortcut in shortcuts" :key="shortcut.path" class="shortcut-item" type="button" @click="router.push(shortcut.path)">
            <span class="shortcut-icon" :class="shortcut.tone"><Icon :icon="shortcut.icon" /></span>
            <span class="shortcut-copy"><strong>{{ shortcut.title }}</strong><small>{{ shortcut.description }}</small></span>
            <Icon class="shortcut-arrow" icon="solar:alt-arrow-right-linear" />
          </button>
        </div>
      </NCard>

      <NCard class="soft-card session-card" :bordered="false" title="当前会话">
        <template #header-extra><NTag type="success" size="small" round :bordered="false">安全</NTag></template>
        <div class="profile-row">
          <NAvatar round :size="48" color="#646cff">管</NAvatar>
          <div><strong>{{ authStore.user?.displayName }}</strong><span>@{{ authStore.user?.username }}</span></div>
        </div>
        <div class="session-list">
          <div><span>当前角色</span><strong>{{ authStore.user?.roleNames?.join('、') }}</strong></div>
          <div><span>最近登录</span><strong>{{ authStore.user?.lastLoginAt }}</strong></div>
          <div><span>登录 IP</span><strong>{{ authStore.user?.lastLoginIp }}</strong></div>
        </div>
        <div class="security-score">
          <div><span>账号安全度</span><strong>良好</strong></div>
          <NProgress type="line" :percentage="86" :show-indicator="false" color="#646cff" />
        </div>
      </NCard>
    </section>
  </div>
</template>

<style scoped>
.home-page { max-width: 1500px; margin: 0 auto; }
.welcome-card { position: relative; min-height: 250px; display: flex; align-items: center; overflow: hidden; padding: 38px 44px; border-radius: 22px; color: #fff; background: radial-gradient(circle at 78% 24%, rgba(255,255,255,.18), transparent 24%), linear-gradient(120deg,#545ce6,#757cff 55%,#8b91ff); box-shadow: 0 16px 40px rgba(82,90,222,.23); }
.welcome-copy { position: relative; z-index: 2; }
.welcome-copy :deep(.n-tag) { color: #fff; background: rgba(255,255,255,.15); }
.welcome-copy h1 { margin: 18px 0 9px; font-size: clamp(26px,3vw,38px); letter-spacing: -.035em; }
.welcome-copy p { margin: 0 0 28px; color: rgba(255,255,255,.72); }
.welcome-copy :deep(.n-button--primary-type) { color: #5159e8; background: #fff; }
.welcome-copy :deep(.n-button--secondary-type) { color: #fff; --n-color: rgba(255,255,255,.1) !important; --n-border: 1px solid rgba(255,255,255,.25) !important; }
.welcome-visual { position: absolute; top: 0; right: 0; width: 43%; height: 100%; }
.visual-shield { position: absolute; top: 50%; left: 52%; width: 112px; height: 112px; display: grid; place-items: center; transform: translate(-50%,-50%) rotate(-5deg); border: 1px solid rgba(255,255,255,.25); border-radius: 31px; background: rgba(255,255,255,.14); backdrop-filter: blur(12px); box-shadow: 0 22px 50px rgba(28,32,115,.25); }
.visual-shield svg { font-size: 65px; }
.orbit { position: absolute; top: 50%; left: 52%; border: 1px solid rgba(255,255,255,.16); border-radius: 50%; transform: translate(-50%,-50%); }
.orbit-a { width: 220px; height: 220px; }.orbit-b { width: 315px; height: 315px; }
.visual-dot { position: absolute; width: 11px; height: 11px; border-radius: 50%; background: #fff; box-shadow: 0 0 0 8px rgba(255,255,255,.1); }.dot-a { top: 22%; left: 19%; }.dot-b { right: 17%; bottom: 20%; }
.stats-grid { display: grid; grid-template-columns: repeat(3,minmax(0,1fr)); gap: 16px; }
.stat-card :deep(.n-card__content) { display: flex; align-items: center; gap: 15px; }
.stat-icon { flex: 0 0 auto; width: 52px; height: 52px; display: grid; place-items: center; border-radius: 15px; font-size: 26px; }
.stat-card span,.stat-card small,.stat-card strong { display: block; }.stat-card span { color: var(--text-secondary); font-size: 12px; }.stat-card strong { margin: 2px 0; font-size: 27px; }.stat-card small { color: var(--text-secondary); font-size: 10px; }
.home-grid { display: grid; grid-template-columns: minmax(0,1.35fr) minmax(320px,.65fr); gap: 16px; }
.section-extra { color: var(--text-secondary); font-size: 11px; }
.shortcut-list { display: flex; flex-direction: column; gap: 10px; }
.shortcut-item { width: 100%; display: flex; align-items: center; gap: 13px; padding: 14px; border: 1px solid transparent; border-radius: 14px; color: inherit; text-align: left; background: var(--app-bg); cursor: pointer; transition: transform .2s,border-color .2s; }
.shortcut-item:hover { transform: translateX(3px); border-color: rgba(100,108,255,.25); }
.shortcut-icon { width: 42px; height: 42px; display: grid; place-items: center; border-radius: 13px; font-size: 21px; }.shortcut-icon.violet { color:#646cff;background:rgba(100,108,255,.12); }.shortcut-icon.green { color:#19a974;background:rgba(25,169,116,.12); }.shortcut-icon.orange { color:#f59e42;background:rgba(245,158,66,.13); }
.shortcut-copy { flex: 1; min-width: 0; }.shortcut-copy strong,.shortcut-copy small { display:block; }.shortcut-copy strong { font-size:13px; }.shortcut-copy small { margin-top:5px;color:var(--text-secondary);font-size:11px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap; }.shortcut-arrow { color:var(--text-secondary); }
.profile-row { display:flex;align-items:center;gap:12px;padding-bottom:18px;border-bottom:1px solid var(--border-color); }.profile-row strong,.profile-row span { display:block; }.profile-row strong { font-size:14px; }.profile-row span { margin-top:4px;color:var(--text-secondary);font-size:11px; }
.session-list { padding:14px 0; }.session-list div { display:flex;justify-content:space-between;gap:18px;padding:7px 0;font-size:11px; }.session-list span { color:var(--text-secondary); }.session-list strong { font-weight:500;text-align:right; }
.security-score { padding-top:13px;border-top:1px solid var(--border-color); }.security-score div { display:flex;justify-content:space-between;margin-bottom:8px;font-size:11px; }.security-score span { color:var(--text-secondary); }.security-score strong { color:#19a974; }
@media (max-width: 1000px) { .home-grid { grid-template-columns: 1fr; }.welcome-visual { opacity:.65;width:50%; } }
@media (max-width: 700px) { .welcome-card { min-height:230px;padding:30px 24px; }.welcome-copy { max-width:100%; }.welcome-visual { opacity:.22;width:70%; }.stats-grid { grid-template-columns:1fr; }.welcome-copy p { max-width:72%;line-height:1.6; } }
</style>
