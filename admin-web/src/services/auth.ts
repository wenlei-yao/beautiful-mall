import { http } from '@/services/http';
import { mockAdmins } from '@/mock/data';
import type { AdminUser, ApiResponse, AuthSession, CurrentAdminResponse, LoginPayload } from '@/types/admin';

const useMock = import.meta.env.VITE_USE_MOCK !== 'false';
const MOCK_USERNAME = 'admin';
const MOCK_PASSWORD = 'Admin@123456';

const wait = (milliseconds: number) => new Promise(resolve => window.setTimeout(resolve, milliseconds));

function mapCurrentAdmin(admin: CurrentAdminResponse): AdminUser {
  return {
    id: admin.id,
    username: admin.username,
    displayName: admin.displayName,
    mobile: admin.mobile || '',
    roleCodes: admin.roles.map(role => role.code),
    roleNames: admin.roles.map(role => role.name),
    permissions: admin.permissions,
    status: admin.enabled ? 'enabled' : 'disabled',
    lastLoginAt: admin.lastLoginTime || '-',
    lastLoginIp: admin.lastLoginIp || '-',
    createdAt: admin.createdTime
  };
}

export async function login(payload: LoginPayload): Promise<AuthSession> {
  if (!useMock) {
    const response = await http.post<ApiResponse<AuthSession>>('/login/account', payload);
    return response.data.data;
  }

  await wait(650);
  if (payload.username !== MOCK_USERNAME || payload.password !== MOCK_PASSWORD) {
    throw new Error('用户名或密码错误');
  }
  return {
    accessToken: `mock-admin-token-${Date.now()}`,
    tokenType: 'Bearer',
    expiresIn: 30 * 60
  };
}

export async function getCurrentAdmin(): Promise<AdminUser> {
  if (!useMock) {
    const response = await http.get<ApiResponse<CurrentAdminResponse>>('/user/me');
    return mapCurrentAdmin(response.data.data);
  }

  await wait(160);
  return { ...mockAdmins[0] };
}

export async function logout(): Promise<void> {
  if (!useMock) {
    return;
  }
  await wait(120);
}
