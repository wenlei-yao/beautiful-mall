<script setup lang="ts">
import { computed } from 'vue';
import { NConfigProvider, NDialogProvider, NGlobalStyle, NMessageProvider, zhCN, dateZhCN } from 'naive-ui';
import { useThemeStore } from '@/stores/theme';

const themeStore = useThemeStore();
const themeOverrides = computed(() => ({
  common: {
    primaryColor: '#646CFF',
    primaryColorHover: '#7B82FF',
    primaryColorPressed: '#5159E8',
    primaryColorSuppl: '#646CFF',
    borderRadius: '10px',
    borderRadiusSmall: '8px'
  }
}));
</script>

<template>
  <NConfigProvider :locale="zhCN" :date-locale="dateZhCN" :theme="themeStore.naiveTheme" :theme-overrides="themeOverrides">
    <NGlobalStyle />
    <NDialogProvider>
      <NMessageProvider>
        <RouterView />
      </NMessageProvider>
    </NDialogProvider>
  </NConfigProvider>
</template>

