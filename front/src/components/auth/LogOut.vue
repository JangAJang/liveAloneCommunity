<script setup lang="ts">
import { onMounted } from 'vue'
import VueCookies from 'vue-cookies'
import router from '@/router'

onMounted(() => {
  VueCookies.set('Authorization', null, 60 * 60 * 24)
  alert('로그아웃 되었습니다.')
  router.push({ name: 'logIn' })
})
</script>
<template></template>
<style></style>
