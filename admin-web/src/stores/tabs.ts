import { ref } from 'vue';
import { defineStore } from 'pinia';

export interface PageTab {
  path: string;
  title: string;
  closable: boolean;
}

export const useTabsStore = defineStore('tabs', () => {
  const tabs = ref<PageTab[]>([{ path: '/home', title: '首页', closable: false }]);

  function addTab(tab: PageTab) {
    if (!tabs.value.some(item => item.path === tab.path)) tabs.value.push(tab);
  }

  function removeTab(path: string) {
    const index = tabs.value.findIndex(item => item.path === path);
    if (index >= 0 && tabs.value[index].closable) tabs.value.splice(index, 1);
  }

  return { tabs, addTab, removeTab };
});

