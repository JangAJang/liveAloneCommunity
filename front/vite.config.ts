import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      "/lan": {
        target: "http://34.219.46.112:80/api",
        ws: true,
        changeOrigin: true,
        rewrite: (path)=> path.replace(/^\/lan/, ""),
      }
    },
  }
})

