<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

const username = ref('')
const nickname = ref('')
const email = ref('')
const res = ref({})

axios
  .get('/lan/member?id=1')
  .then((response) => {
    username.value = "아이디 : " + response.data.result.data.username
    nickname.value = "닉네임 : " + response.data.result.data.nickname
    email.value = "이메일 : " + response.data.result.data.email
  })
  .catch(() => {
    username.value = ''
    nickname.value = '로그인 후 이용 가능합니다.'
    email.value = ''
  })
</script>

<template>
  <div id="profileInfo">
      <br/>
    <el-text id="text">{{ username }}</el-text>
      <br/>
    <el-text id="text">{{ nickname }}</el-text>
      <br/>
    <el-text id="text">{{ email }}</el-text>
  </div>
</template>

<style>
#profileInfo{
    margin-left: 83%;
    margin-top: 5%;
    width: 15%;
    background-color: #ffe87c;
    height: 100px;
    text-align: center;
}

#text{
    align-content: center;
}
</style>
