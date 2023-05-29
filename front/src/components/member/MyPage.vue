<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import Profile from '@/components/main/Profile.vue'
import router from '@/router'
import MyData from "@/components/main/MyData.vue";

const nickname = ref('')
const username = ref('')
const email = ref('')
const currentPassword = ref('')
const newPassword = ref('')
const newPasswordCheck = ref('')
onMounted(() => {
  axios.get('/lan/member/me').then((res) => {
    nickname.value = res.data.result.data.nickname
    email.value = res.data.result.data.email
    username.value = res.data.result.data.username
  })
})

const changeNickname = function () {
  axios
    .patch('/lan/member/edit', {
      nickname: nickname.value
    })
    .then(() => {
      alert('닉네임 변경에 성공했습니다.')
      router.push({ name: 'myPage' })
    })
    .catch((reason) => alert(reason.response.data.result.failMessage))
}

const changePassword = function () {
  axios
    .patch('/lan/member/changePassword', {
      currentPassword: currentPassword.value,
      newPassword: newPassword.value,
      newPasswordCheck: newPasswordCheck.value
    })
    .then(() => {
      alert('비밀번호 변경에 성공했습니다.')
      router.push({ name: 'myPage' })
    })
    .catch((reason) => alert(reason.response.data.result.failMessage))
}

const deleteMember = function () {
  if (confirm('회원을 삭제하시겠습니까?')) {
    axios
      .delete('/lan/member/delete')
      .then(() => {
        alert('회원을 탈퇴했습니다. 그동한 L.A.N과 함꼐 해주셔서 감사합니다.')
        router.push({ name: 'logIn' })
      })
      .catch((reason) => alert(reason.response.data.result.failMessage))
  }
}
</script>

<template>
  <MyData/>
  <div id="myPageBackground">
    <div id="tag1">
      <p id="myPageTagTitle">내 정보</p>
      <div id="myPageMe">
        <p>아이디 : {{username}}</p>
        <p>이메일 : {{email}}</p>
        <p>닉네임 : {{nickname}}</p>
      </div>
      <button @click="" id="logOutButton">
        <p id="logOutText">로그아웃</p>
      </button>
    </div>
    <div id="tag2">

    </div>
    <div id="tag3">

    </div>
  </div>
  <Profile />
</template>

<style>
#myPageBackground {
  position: absolute;
  width: 60%;
  margin-left: 20%;
  height: 100%;
  background: #FEEFCA;
  border-radius: 15px;
}

#tag1{
  position: absolute;
  width: 90%;
  margin-left: 5%;
  margin-top: 3%;
  height: 30%;
  background: #FFFFFF;
}

#tag2{
  position: absolute;
  width: 90%;
  margin-left: 5%;
  margin-top: 34%;
  height: 30%;
  background: #FFFFFF;
}

#tag3{
  position: absolute;
  width: 90%;
  margin-left: 5%;
  margin-top: 65%;
  height: 30%;
  background: #FFFFFF;
}

#myPageTagTitle {
  position: absolute;
  margin-left: 1%;
  margin-top: 1%;
  font-family: 'Inter';
  font-size: 20px;
  font-style: normal;
  font-weight: 700;
  color: #000000;
}

#myPageMe {
  position: absolute;
  margin-left: 1%;
  margin-top: 5%;
}

#logOutButton {
  position: absolute;
  background-color: black;
  margin-left: 83%;
  margin-top: 1%;
  width: 15%;
  height: 17%;
  border-radius: 15px;
}

#logOutText {
  color: #FFFFFF;
}
</style>