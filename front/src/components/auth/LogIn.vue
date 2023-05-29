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
    prompts: 'login'
  })
}
</script>
<template>
  <div id="logInBox">
    <div>
      <div>
        <p id="username_text">아이디</p>
        <input v-model="username" type="text" id="username_input"/>
      </div>
      <div>
        <p id="password_text">비밀번호</p>
        <input v-model="password" type="password" id="password_input"/>
      </div>
      <button id="logIn_button" @click="signIn">
        <p id="logIn_text">로그인</p>
      </button>

    </div>
    <p id="kakao_text">카카오톡으로 로그인</p>
    <form id="kakao_img">
      <a v-on:click="kakaoLogIn">
        <img src="@/assets/image/kakao_login_medium_narrow.png" />
      </a>
    </form>
  </div>
</template>
<style>

#logInBox {
  box-sizing: border-box;
  position: absolute;
  width: 80%;
  height: 60%;
  left: 10%;
  top: 20%;
  background: #FEEFCA;
  border: 1px solid #000000;
  border-radius: 15px;
}

#username_text {
  position: absolute;
  margin-top: 10%;
  margin-left: 8%;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}

#username_input {
  position: absolute;
  margin-top: 14%;
  margin-left: 8%;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}

#password_text{
  position: absolute;
  margin-top: 20%;
  margin-left: 8%;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}

#password_input{
  position: absolute;
  margin-top: 24%;
  margin-left: 8%;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}

#logIn_button {
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  padding: 8px;
  gap: 8px;
  position: absolute;
  width: 12%;
  height: 14%;
  left: 40%;
  top: 70%;
  background: #FFEB35;
  border: 3px solid #000000;
  border-radius: 15px;
}

#logIn_text{
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 30px;
  color: #000000;
  flex: none;
  order: 0;
  flex-grow: 0;
  margin-top: -5%;
}

#kakao_text {
  position: absolute;
  margin-top: 10%;
  margin-left: 58%;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}

#kakao_img {
  margin-top: 20%;
  margin-left: 58%;
}
</style>
