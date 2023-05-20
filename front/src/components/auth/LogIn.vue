<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import router from '@/router'
import VueCookies from 'vue-cookies'

const username = ref('')
const password = ref('')
const signIn = function () {
  axios
    .post('/lan/auth/logIn', {
      username: username.value,
      password: password.value
    })
    .then((res) => {
      alert(username.value + '님의 방문을 환영합니다')
      const accessToken = res.headers.get('Authorization', String)
      VueCookies.set('Authorization', accessToken, 60 * 60 * 24)
      axios.defaults.headers.common['Authorization'] = VueCookies.get('Authorization')
      router.push({ name: 'main' })
    })
    .catch((reason) => alert(reason.response.data.result.failMessage))
}

const kakaoLogIn = function () {
  Kakao.init('229db5180010ffc0378044aa071a7a5c')
  Kakao.Auth.authorize({
    redirectUri: 'http://localhost:5173/auth/kakao-login',
    prompts: 'login',
  })
}
</script>
<template>
  <div id="logInPart">
    <el-input v-model="username" type="text" placeholder="아이디를 입력하세요" />
    <el-input v-model="password" type="password" placeholder="비밀번호를 입력하세요" />
    <el-button type="primary" @click="signIn()" id="logInButton">로그인</el-button>
  </div>
  <form>
    <a v-on:click="kakaoLogIn">
      <img src="@/assets/image/kakao_login_medium_narrow.png" />
    </a>
  </form>
</template>
<style>
#logInPart {
  width: 50%;
  height: 50%;
  margin-top: 5%;
  margin-left: 25%;
}
#logInButton {
  margin-left: 35%;
  width: 30%;
  height: 10%;
}
</style>
