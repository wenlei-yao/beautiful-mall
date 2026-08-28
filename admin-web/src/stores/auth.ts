import { computed, ref } from 'vue';
import { defineStore } from 'pinia';
import * as authService from '@/services/auth';
import type { AdminUser, LoginPayload } from '@/types/admin';

const TOKEN_KEY = 'admin-web-token';

function readToken() {
  return localStorage.getItem(TOKEN_KEY) || sessionStorage.getItem(TOKEN_KEY) || '';
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(readToken());
  const user = ref<AdminUser | null>(null);
  const loading = ref(false);
  const isLoggedIn = computed(() => Boolean(token.value));

  function persistToken(value: string, remember: boolean) {
    localStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(TOKEN_KEY);
    const storage = remember ? localStorage : sessionStorage;
    storage.setItem(TOKEN_KEY, value);
    token.value = value;
  }

  async function signIn(payload: LoginPayload, remember: boolean) {
    loading.value = true;
    try {
      const session = await authService.login(payload);
      persistToken(session.accessToken, remember);
      user.value = await authService.getCurrentAdmin();
      return user.value;
    } catch (error) {
      clearSession();
      throw error;
    } finally {
      loading.value = false;
    }
  }

  async function restoreSession() {
    if (!token.value) return false;
    try {
      user.value = await authService.getCurrentAdmin();
      return true;
    } catch {
      clearSession();
      return false;
    }
  }

  async function signOut() {
    try {
      if (token.value) await authService.logout();
    } finally {
      clearSession();
    }
  }

  function clearSession() {
    localStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(TOKEN_KEY);
    token.value = '';
    user.value = null;
  }

  return { token, user, loading, isLoggedIn, signIn, signOut, restoreSession, clearSession };
});

