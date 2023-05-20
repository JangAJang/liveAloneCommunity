<script setup lang="ts">
import { onMounted } from 'vue'
import axios from 'axios'
import router from '@/router'
import VueCookies from "vue-cookies";

onMounted(() => {
  console.log("MOUNTED")
  const code = window.location.href.substring(44)
  console.log(code)
  axios.get("/lan/auth/kakao/callback", {params:
        { code: code}})
      .then(res=> {
        console.log(res)
        alert('방문을 환영합니다')
        const accessToken = res.headers.get('Authorization', String)
        VueCookies.set('Authorization', accessToken, 60 * 60 * 24)
        axios.defaults.headers.common['Authorization'] = VueCookies.get('Authorization')
        console.log(axios.defaults.headers.common['Authorization'].valueOf())
        router.push({ name: 'main' })
      }).catch(e => alert(e))
})
</script>

<template></template>

<style></style>
