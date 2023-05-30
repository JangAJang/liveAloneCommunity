<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import router from '@/router'

const username = ref('')
const nickname = ref('')
const email = ref('')
const password = ref('')
const passwordCheck = ref('')
const authNum = ref('')
const emailSent = ref(false)
const emailVerified = ref(false)

const join = function () {
  if (emailVerified.value) {
    axios
      .post('/lan/auth/register', {
        username: username.value,
        nickname: nickname.value,
        email: email.value,
        password: password.value,
        passwordCheck: passwordCheck.value
      })
      .then(() => {
        alert('회원가입을 성공했습니다.')
        router.push({ name: 'logIn' })
      })
      .catch((reason) => alert(reason.response.data.result.failMessage))
  } else alert('이메일 인증을 먼저 진행해주세요.')
}

const sendEmail = function () {
  axios
    .post('/lan/email/send', {
      email: email.value
    })
    .then(() => {
      alert('인증번호를 메일로 발송했습니다. 인증번호를 확인해주세요.')
      emailSent.value = true
    })
    .catch((reason) => alert(reason.response.data.result.failMessage))
}

const verifyEmail = function () {
  if (emailSent.value) {
    axios
      .post('/lan/email/verify', {
        email: email.value,
        authNum: authNum.value
      })
      .then(() => {
        alert('이메일을 성공적으로 인증했습니다.')
        emailVerified.value = true
      })
      .catch((reason) => alert(reason.response.data.result.failMessage))
  } else alert('인증번호를 먼저 요청해주세요.')
}
</script>

<template>
  <div id="RegisterModel">
    <h2 id="registerTitle">회원가입</h2>
    <div id="RegisterInput">
      <div>
        <p id="usernameInfo">아이디</p>
        <input id="usernameInput" v-model="username" type="text" />
      </div>

      <p id="passwordInfo">비밀번호</p>
      <input id="passwordInput" v-model="password" type="password" />
      <p id="passwordCheckInfo">비밀번호 확인</p>
      <input id="passwordCheckInput" v-model="passwordCheck" type="password" />
      <div>
        <p id="nicknameInfo">닉네임</p>
        <input id="nicknameInput" v-model="nickname" type="text" />
      </div>
      <p id="emailInfo">이메일</p>
      <input id="emailInput" v-model="email" type="text" />
      <p id="authNumInfo">인증번호</p>
      <input id="authNumInput" v-model="authNum" type="text" />
      <button id="sendEmail" @click="sendEmail">
        <p id="sendEmailText">인증번호</p>
      </button>
      <button id="checkAuth" @click="verifyEmail">
        <p id="checkAuthText">확인</p>
      </button>
      <button id="tryRegister" @click="join">
        <p id="tryRegisterText">회원가입</p>
      </button>
    </div>
  </div>
</template>

<style>
#RegisterModel {
  box-sizing: border-box;
  position: absolute;
  width: 70%;
  height: 85%;
  left: 15%;
  top: 15%;
  background: #feefca;
  border: 1px solid #000000;
  border-radius: 15px;
}

#registerTitle {
  margin-left: 5%;
  margin-top: 5%;
}

#RegisterInput {
  width: 50%;
  margin-left: 25%;
}

#usernameInfo {
  position: absolute;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  color: #000000;
  margin-top: 1%;
}

#usernameInput {
  position: absolute;
  background: #ffffff;
  margin-top: 4%;
}

#passwordInfo {
  position: absolute;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  color: #000000;
  margin-top: 7%;
}

#passwordInput {
  position: absolute;
  background: #ffffff;
  margin-top: 10%;
}

#passwordCheckInfo {
  position: absolute;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  color: #000000;
  margin-top: 13%;
}

#passwordCheckInput {
  position: absolute;
  background: #ffffff;
  margin-top: 16%;
}

#nicknameInfo {
  position: absolute;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  color: #000000;
  margin-top: 19%;
}

#nicknameInput {
  position: absolute;
  background: #ffffff;
  margin-top: 22%;
}

#emailInfo {
  position: absolute;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  color: #000000;
  margin-top: 25%;
}

#emailInput {
  position: absolute;
  background: #ffffff;
  margin-top: 28%;
}

#authNumInfo {
  position: absolute;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  color: #000000;
  margin-top: 31%;
}

#authNumInput {
  position: absolute;
  background: #ffffff;
  margin-top: 34%;
}

#sendEmail {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 8px;
  gap: 8px;
  position: absolute;
  margin-left: 23%;
  margin-top: 28%;
  width: 15%;
  background: #a4d9ff;
  border-radius: 15px;
}
#sendEmailText {
  position: absolute;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}

#checkAuth {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 8px;
  gap: 8px;
  position: absolute;
  margin-left: 23%;
  margin-top: 34%;
  width: 15%;
  background: #a4d9ff;
  border-radius: 15px;
}
#checkAuthText {
  position: absolute;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}

#tryRegister {
  align-self: center;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 8px;
  gap: 8px;
  position: absolute;
  margin-left: 10%;
  margin-top: 40%;
  width: 30%;
  height: 7.5%;
  background: #ffeb35;
  border: 3px solid #000000;
  border-radius: 15px;
}
#tryRegisterText {
  position: absolute;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}
</style>
