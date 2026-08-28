import { createApp } from 'vue';
import { createPinia } from 'pinia';
import '@unocss/reset/tailwind-compat.css';
import 'virtual:uno.css';
import '@/styles/global.css';
import App from './App.vue';
import router from './router';
import { permissionDirective } from './directives/permission';

const app = createApp(App);
app.use(createPinia());
app.use(router);
app.directive('permission', permissionDirective);
app.mount('#app');
