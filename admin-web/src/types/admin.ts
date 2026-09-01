export type Status = 'enabled' | 'disabled';
export type PermissionType = 'DIRECTORY' | 'MENU' | 'BUTTON';

export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

export interface AuthSession {
  accessToken: string;
  tokenType: 'Bearer';
  expiresIn: number;
}

export interface LoginPayload {
  username: string;
  password: string;
}

export interface CurrentAdminRole {
  id: number;
  code: string;
  name: string;
}

export interface CurrentAdminResponse {
  id: number;
  username: string;
  displayName: string;
  mobile: string | null;
  enabled: boolean;
  roles: CurrentAdminRole[];
  permissions: string[];
  lastLoginTime: string | null;
  lastLoginIp: string | null;
  createdTime: string;
}

export interface AdminUser {
  id: number;
  username: string;
  displayName: string;
  mobile: string;
  roleCodes: string[];
  roleNames: string[];
  permissions: string[];
  status: Status;
  lastLoginAt: string;
  lastLoginIp: string;
  createdAt: string;
}

export interface Role {
  id: number;
  name: string;
  code: string;
  description: string;
  memberCount: number;
  permissions: string[];
  status: Status;
  createdAt: string;
}

export interface Permission {
  id: number;
  parentId: number | null;
  name: string;
  code: string;
  type: PermissionType;
  path?: string;
  icon?: string;
  sort: number;
  status: Status;
  children?: Permission[];
}
