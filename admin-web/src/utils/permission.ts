import type { AdminUser } from '@/types/admin';

export function hasPermission(user: AdminUser | null, required: string | string[]): boolean {
  if (!user) return false;
  if (user.permissions.includes('*:*:*')) return true;
  const permissions = Array.isArray(required) ? required : [required];
  return permissions.some(permission => user.permissions.includes(permission));
}

export function hasRole(user: AdminUser | null, required: string | string[]): boolean {
  if (!user) return false;
  if (user.roleCodes.includes('super_admin')) return true;
  const roles = Array.isArray(required) ? required : [required];
  return roles.some(role => user.roleCodes.includes(role));
}

