<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import router from '@/router'

const username = ref('')
const nickname = ref('')
const email = ref('')
const res = ref({})
const id = ref()

onMounted(() =>
  axios
    .get('/lan/member/me')
    .then((response) => {
      nickname.value = response.data.result.data.nickname
      id.value = response.data.result.data.id
    })
    .catch((e) => {
      alert(e)
      nickname.value = '로그인 후 이용 가능합니다.'
    })
)
</script>

<template>
  <div id="profile">
    <div id="profileImage"></div>
    <br />
    <el-text id="text">{{ nickname }}</el-text>
    <br />
    <div class="mt-3">
      <button @click="router.push({ name: 'myPage' })" id="myPageButton">
        <p id="buttonText">마이페이지</p>
      </button>
      <button id="myPageButton" @click="router.push({ name: 'logOut' })">
        <p id="buttonText">로그아웃</p>
      </button>
    </div>
  </div>
</template>

<style>
#profile {
  position: relative;
  margin-left: 83%;
  margin-top: 7%;
  width: 15%;
  background: #feefca;
  border-radius: 15px;
  height: 100px;
  text-align: center;
}

#profileImage {
  width: 80%;
  margin-top: 10%;
  margin-left: 10%;
}

#myPageButton {
  width: 40%;
  box-sizing: border-box;
  justify-content: center;
  align-items: center;
  background: #a4d9ff;
  border: 1px solid #000000;
  border-radius: 15px;
}

#text {
  align-content: center;
}

#buttonText {
  font-family: 'Montserrat';
  font-size: 5px;
  font-style: normal;
  font-weight: 500;
  display: flex;
  align-items: center;
  text-align: center;
  letter-spacing: -0.015em;
  color: #000000;
}
</style>
