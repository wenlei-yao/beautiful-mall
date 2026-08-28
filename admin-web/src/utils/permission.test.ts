import { describe, expect, it } from 'vitest';
import { mockAdmins } from '@/mock/data';
import { hasPermission, hasRole } from './permission';

describe('permission helpers', () => {
  it('allows every permission for the super administrator', () => {
    expect(hasPermission(mockAdmins[0], 'permission:create')).toBe(true);
    expect(hasRole(mockAdmins[0], 'unknown-role')).toBe(true);
  });

  it('checks normal users against explicit permissions and roles', () => {
    const operator = mockAdmins[1];
    expect(hasPermission(operator, 'admin:view')).toBe(true);
    expect(hasPermission(operator, 'admin:create')).toBe(false);
    expect(hasRole(operator, 'operator')).toBe(true);
    expect(hasRole(operator, 'auditor')).toBe(false);
  });

  it('denies anonymous users', () => {
    expect(hasPermission(null, 'admin:view')).toBe(false);
    expect(hasRole(null, 'operator')).toBe(false);
  });
});

