import type { Directive } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { hasPermission } from '@/utils/permission';

export const permissionDirective: Directive<HTMLElement, string | string[]> = {
  mounted(element, binding) {
    const auth = useAuthStore();
    if (!hasPermission(auth.user, binding.value)) element.remove();
  }
};

