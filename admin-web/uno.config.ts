import { defineConfig, presetAttributify, presetUno } from 'unocss';

export default defineConfig({
  presets: [presetUno(), presetAttributify()],
  theme: {
    colors: {
      primary: '#646cff'
    }
  }
});

