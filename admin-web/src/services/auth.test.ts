import { describe, expect, it } from 'vitest';
import { getCurrentAdmin, login, logout } from './auth';

describe('mock authentication service', () => {
  it('accepts the configured prototype account', async () => {
    const session = await login({ username: 'admin', password: 'Admin@123456' });
    expect(session.accessToken).toContain('mock-admin-token');
    expect(session.tokenType).toBe('Bearer');
  });

  it('rejects invalid credentials without exposing details', async () => {
    await expect(login({ username: 'admin', password: 'wrong-password' })).rejects.toThrow('用户名或密码错误');
  });

  it('returns the current super administrator', async () => {
    const admin = await getCurrentAdmin();
    expect(admin.username).toBe('admin');
    expect(admin.permissions).toContain('*:*:*');
    await expect(logout()).resolves.toBeUndefined();
  });
});

