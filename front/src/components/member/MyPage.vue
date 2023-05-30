<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import Profile from '@/components/main/Profile.vue'
import router from '@/router'
import MyData from '@/components/main/MyData.vue'

const nickname = ref('')
const username = ref('')
const email = ref('')

onMounted(() => {
  axios.get('/lan/member/me').then((res) => {
    nickname.value = res.data.result.data.nickname
    email.value = res.data.result.data.email
    username.value = res.data.result.data.username
  })
})
</script>

<template>
  <MyData />
  <div id="myPageBackground">
    <div id="tag1">
      <p id="myPageTagTitle">내 정보</p>
      <div id="myPageMe">
        <p>아이디 : {{ username }}</p>
        <p>이메일 : {{ email }}</p>
        <p>닉네임 : {{ nickname }}</p>
      </div>
      <button @click="router.push({ name: 'logOut' })" id="logOutButton">
        <p id="logOutText">로그아웃</p>
      </button>
    </div>
    <div id="tag2">
      <p id="myPageTagTitle">계정</p>
      <div id="tag2List">
        <RouterLink to="/member/edit/nickname" id="changeNickname"
          ><p id="tag2Component">닉네임 변경</p></RouterLink
        >
        <RouterLink to="/member/edit/password" id="changePassword"
          ><p id="tag2Component">비밀번호 변경</p></RouterLink
        >
        <RouterLink to="/member/delete" id="deleteMember"
          ><p id="tag2Component">회원탈퇴</p></RouterLink
        >
      </div>
    </div>
  </div>
  <Profile />
</template>

<style>
#myPageBackground {
  position: absolute;
  width: 60%;
  margin-left: 20%;
  height: 70%;
  background: #feefca;
  border-radius: 15px;
}

#tag1 {
  position: absolute;
  width: 90%;
  margin-left: 5%;
  margin-top: 3%;
  height: 40%;
  background: #ffffff;
}

#tag2 {
  position: absolute;
  width: 90%;
  margin-left: 5%;
  margin-top: 33%;
  height: 40%;
  background: #ffffff;
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
  margin-left: 3%;
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

#tag2Component {
  margin-top: 2%;
  margin-left: 1%;
}

#logOutText {
  color: #ffffff;
}

#tag2List {
  margin-top: 7%;
  margin-left: 2%;
}
</style>
