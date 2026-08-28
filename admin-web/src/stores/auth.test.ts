import { beforeEach, describe, expect, it } from 'vitest';
import { createPinia, setActivePinia } from 'pinia';
import { useAuthStore } from './auth';

describe('authentication store', () => {
  beforeEach(() => {
    localStorage.clear();
    sessionStorage.clear();
    setActivePinia(createPinia());
  });

  it('persists remembered sessions in localStorage', async () => {
    const store = useAuthStore();
    await store.signIn({ username: 'admin', password: 'Admin@123456' }, true);
    expect(store.isLoggedIn).toBe(true);
    expect(store.user?.displayName).toBe('超级管理员');
    expect(localStorage.getItem('admin-web-token')).toBe(store.token);
    expect(sessionStorage.getItem('admin-web-token')).toBeNull();
  });

  it('uses sessionStorage when remember login is disabled', async () => {
    const store = useAuthStore();
    await store.signIn({ username: 'admin', password: 'Admin@123456' }, false);
    expect(sessionStorage.getItem('admin-web-token')).toBe(store.token);
    expect(localStorage.getItem('admin-web-token')).toBeNull();
  });

  it('clears all authentication state on logout', async () => {
    const store = useAuthStore();
    await store.signIn({ username: 'admin', password: 'Admin@123456' }, true);
    await store.signOut();
    expect(store.isLoggedIn).toBe(false);
    expect(store.user).toBeNull();
    expect(localStorage.getItem('admin-web-token')).toBeNull();
  });
});

