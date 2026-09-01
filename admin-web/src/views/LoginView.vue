<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Icon } from '@iconify/vue';
import { NButton, NCheckbox, NForm, NFormItem, NInput, NTag, useMessage, type FormInst, type FormRules } from 'naive-ui';
import { useAuthStore } from '@/stores/auth';
import { useThemeStore } from '@/stores/theme';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const themeStore = useThemeStore();
const message = useMessage();
const formRef = ref<FormInst | null>(null);
const showPassword = ref(false);
const form = reactive({ username: 'admin', password: 'Admin@123456', remember: true });
const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: ['input', 'blur'] },
    { min: 3, message: '用户名至少3个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: ['input', 'blur'] },
    { min: 6, message: '密码至少6个字符', trigger: 'blur' }
  ]
};

async function submit() {
  try {
    await formRef.value?.validate();
    const admin = await authStore.signIn({ username: form.username.trim(), password: form.password }, form.remember);
    message.success(`欢迎回来，${admin.displayName}`);
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/home';
    await router.replace(redirect);
  } catch (error) {
    if (error instanceof Error) message.error(error.message);
  }
}
</script>

<template>
  <main class="login-page">
    <section class="brand-panel" aria-label="Beautiful Mall 品牌介绍">
      <div class="ambient ambient-one"></div>
      <div class="ambient ambient-two"></div>
      <div class="brand-top"><span class="login-logo">B</span><strong>Beautiful Mall</strong></div>
      <div class="brand-content">
        <NTag round :bordered="false" class="eyebrow">SMART COMMERCE OS</NTag>
        <h1>让每一次管理决策<br />清晰、可靠、高效。</h1>
        <p>商城运营提供安全而优雅的管理入口。</p>
        <div class="feature-grid">
          <div><Icon icon="solar:shield-check-outline" /><span><b>权限清晰</b><small>角色与操作边界一目了然</small></span></div>
          <div><Icon icon="solar:bolt-outline" /><span><b>高效协作</b><small>快捷进入常用管理模块</small></span></div>
        </div>
      </div>
      <p class="brand-footer">© 2026 Beautiful Mall · 商城管理平台</p>
    </section>

    <section class="login-panel">
      <button class="theme-toggle" type="button" aria-label="切换主题" @click="themeStore.toggleTheme">
        <Icon :icon="themeStore.isDark ? 'solar:sun-2-outline' : 'solar:moon-outline'" />
      </button>
      <div class="mobile-brand"><span class="login-logo">B</span><strong>Beautiful Mall</strong></div>
      <div class="login-card">
        <div class="login-heading">
          <span class="welcome-dot"></span>
          <h2>欢迎回来</h2>
          <p>请使用管理员账号登录管理后台</p>
        </div>

        <NForm ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="submit">
          <NFormItem label="用户名" path="username">
            <NInput v-model:value="form.username" placeholder="请输入管理员用户名" clearable>
              <template #prefix><Icon icon="solar:user-rounded-outline" /></template>
            </NInput>
          </NFormItem>
          <NFormItem label="密码" path="password">
            <NInput v-model:value="form.password" :type="showPassword ? 'text' : 'password'" placeholder="请输入登录密码">
              <template #prefix><Icon icon="solar:lock-password-outline" /></template>
              <template #suffix>
                <button class="password-toggle" type="button" aria-label="显示或隐藏密码" @click="showPassword = !showPassword">
                  <Icon :icon="showPassword ? 'solar:eye-closed-outline' : 'solar:eye-outline'" />
                </button>
              </template>
            </NInput>
          </NFormItem>
          <div class="form-options">
            <NCheckbox v-model:checked="form.remember">记住登录状态</NCheckbox>
            <span>忘记密码请联系超级管理员</span>
          </div>
          <NButton type="primary" block size="large" :loading="authStore.loading" @click="submit">
            登录管理后台
            <template #icon><Icon icon="solar:arrow-right-outline" /></template>
          </NButton>
        </NForm>

        <div class="demo-account">
          <Icon icon="solar:info-circle-outline" />
          <span>原型账号</span>
          <code>admin</code>
          <span class="divider">/</span>
          <code>Admin@123456</code>
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped>
.login-page { min-height: 100vh; display: grid; grid-template-columns: minmax(430px, 1.08fr) minmax(430px, .92fr); overflow: hidden; background: var(--panel-solid); }
.brand-panel { position: relative; min-height: 100vh; display: flex; flex-direction: column; padding: 42px 56px; overflow: hidden; color: #fff; background: radial-gradient(circle at 82% 16%, rgba(127,135,255,.28), transparent 32%), linear-gradient(145deg, #171b3b 0%, #292c67 58%, #4f57d7 120%); }
.brand-top { position: relative; z-index: 2; display: flex; align-items: center; gap: 12px; font-size: 17px; }
.login-logo { width: 40px; height: 40px; display: grid; place-items: center; border-radius: 13px; color: #fff; font-size: 21px; font-weight: 800; background: linear-gradient(145deg,#8b91ff,#626af4); box-shadow: 0 10px 24px rgba(0,0,0,.18); }
.brand-content { position: relative; z-index: 2; width: min(610px, 92%); margin: auto 0; }
.eyebrow { color: #dfe1ff !important; background: rgba(255,255,255,.1) !important; letter-spacing: .16em; }
.brand-content h1 { margin: 26px 0 22px; font-size: clamp(38px, 4.1vw, 62px); line-height: 1.16; letter-spacing: -.045em; }
.brand-content > p { max-width: 520px; margin: 0; color: rgba(255,255,255,.68); font-size: 16px; line-height: 1.9; }
.feature-grid { display: grid; grid-template-columns: repeat(2,minmax(0,1fr)); gap: 14px; margin-top: 46px; }
.feature-grid > div { display: flex; gap: 12px; padding: 16px; border: 1px solid rgba(255,255,255,.1); border-radius: 16px; background: rgba(255,255,255,.06); backdrop-filter: blur(10px); }
.feature-grid svg { flex: 0 0 auto; margin-top: 2px; color: #aeb3ff; font-size: 23px; }
.feature-grid b, .feature-grid small { display: block; }
.feature-grid b { font-size: 13px; }
.feature-grid small { margin-top: 5px; color: rgba(255,255,255,.56); font-size: 11px; }
.brand-footer { position: relative; z-index: 2; margin: 0; color: rgba(255,255,255,.38); font-size: 11px; }
.ambient { position: absolute; border-radius: 50%; filter: blur(1px); }
.ambient-one { right: -120px; bottom: -180px; width: 460px; height: 460px; border: 90px solid rgba(255,255,255,.035); }
.ambient-two { top: 20%; right: 10%; width: 180px; height: 180px; background: rgba(100,108,255,.15); filter: blur(50px); }
.login-panel { position: relative; min-height: 100vh; display: grid; place-items: center; padding: 48px; background: var(--panel-solid); }
.theme-toggle { position: absolute; top: 32px; right: 34px; width: 40px; height: 40px; display: grid; place-items: center; border: 1px solid var(--border-color); border-radius: 12px; color: var(--text-secondary); background: var(--panel-solid); cursor: pointer; }
.theme-toggle:hover { color: #646cff; border-color: rgba(100,108,255,.45); }
.mobile-brand { display: none; }
.login-card { width: min(410px, 100%); }
.login-heading { margin-bottom: 34px; }
.welcome-dot { display: block; width: 26px; height: 4px; margin-bottom: 18px; border-radius: 99px; background: #646cff; }
.login-heading h2 { margin: 0; font-size: 30px; letter-spacing: -.035em; }
.login-heading p { margin: 10px 0 0; color: var(--text-secondary); font-size: 14px; }
.form-options { display: flex; justify-content: space-between; align-items: center; margin: -2px 0 24px; color: var(--text-secondary); font-size: 11px; }
.password-toggle { display: grid; place-items: center; padding: 3px; border: 0; color: var(--text-secondary); background: transparent; cursor: pointer; }
.demo-account { display: flex; align-items: center; justify-content: center; gap: 7px; margin-top: 26px; padding: 11px; border-radius: 10px; color: var(--text-secondary); background: var(--app-bg); font-size: 11px; }
.demo-account svg { color: #646cff; font-size: 15px; }
.demo-account code { color: var(--text-primary); font-family: inherit; font-weight: 600; }
.demo-account .divider { opacity: .35; }
@media (max-width: 920px) {
  .login-page { display: block; background: var(--app-bg); }
  .brand-panel { display: none; }
  .login-panel { min-height: 100vh; padding: 92px 24px 40px; align-content: center; background: radial-gradient(circle at 80% 8%, rgba(100,108,255,.13), transparent 26%), var(--app-bg); }
  .mobile-brand { position: absolute; top: 28px; left: 26px; display: flex; align-items: center; gap: 10px; font-size: 15px; }
  .mobile-brand .login-logo { width: 34px; height: 34px; border-radius: 11px; font-size: 18px; }
  .login-card { padding: 28px; border: 1px solid var(--border-color); border-radius: 20px; background: var(--panel-solid); box-shadow: var(--shadow-soft); }
  .theme-toggle { top: 26px; right: 24px; }
}
@media (max-width: 480px) {
  .login-panel { padding: 84px 14px 20px; }
  .login-card { padding: 24px 18px; }
  .login-heading { margin-bottom: 26px; }
  .login-heading h2 { font-size: 26px; }
  .form-options { align-items: flex-start; flex-direction: column; gap: 8px; }
  .demo-account { flex-wrap: wrap; }
}
</style>
