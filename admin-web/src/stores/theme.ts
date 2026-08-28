import { computed, ref } from 'vue';
import { darkTheme } from 'naive-ui';
import { defineStore } from 'pinia';

export type ThemeMode = 'light' | 'dark';

const THEME_KEY = 'admin-web-theme';

function initialTheme(): ThemeMode {
  const saved = localStorage.getItem(THEME_KEY);
  if (saved === 'light' || saved === 'dark') return saved;
  return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
}

export const useThemeStore = defineStore('theme', () => {
  const mode = ref<ThemeMode>(initialTheme());
  const isDark = computed(() => mode.value === 'dark');
  const naiveTheme = computed(() => (isDark.value ? darkTheme : null));

  function applyTheme() {
    document.documentElement.classList.toggle('dark', isDark.value);
    document.documentElement.dataset.theme = mode.value;
    localStorage.setItem(THEME_KEY, mode.value);
  }

  function toggleTheme() {
    mode.value = isDark.value ? 'light' : 'dark';
    applyTheme();
  }

  applyTheme();
  return { mode, isDark, naiveTheme, toggleTheme, applyTheme };
});

