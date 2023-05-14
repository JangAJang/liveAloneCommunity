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
      .then(() => router.push({ name: 'logIn' }))
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
    <div id="RegisterInput">
      <div class="mt-1">
        <el-input v-model="username" type="text" placeholder="아이디를 입력해주세요." />
      </div>
      <div class="mt-1">
        <el-input v-model="nickname" type="text" placeholder="닉네임을 입력해주세요." />
      </div>
      <div class="mt-1">
        <el-input v-model="email" type="email" placeholder="이메일을 입력해주세요." />
        <div>
          <el-button id="buttonState" type="primary" @click="sendEmail()">인증번호 전송</el-button>
          <el-input v-model="authNum" type="text" placeholder="인증 번호를 입력해주세요." />
        </div>
        <el-button id="buttonState" type="primary" @click="verifyEmail()">인증번호 확인</el-button>
      </div>
      <div class="mt-1">
        <el-input v-model="password" type="password" placeholder="비밀번호를 입력해주세요." />
      </div>
      <div class="mt-1">
        <el-input
          v-model="passwordCheck"
          type="password"
          placeholder="비밀번호를 다시 입력해주세요."
        />
      </div>
      <div class="mt-1">
        <el-button id="buttonState" type="primary" @click="join()">회원 가입</el-button>
      </div>
    </div>
  </div>
</template>

<style>
#RegisterModel {
  width: 60%;
  margin-left: 20%;
  margin-top: 5%;
  background-color: #ffe87c;
}

#RegisterInput {
  width: 50%;
  margin-left: 25%;
  margin-top: 20%;
}

#buttonState {
  width: 50%;
  margin-left: 25%;
}
</style>
