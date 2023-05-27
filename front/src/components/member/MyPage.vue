<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import Profile from '@/components/main/Profile.vue'
import MyComment from '@/components/main/MyComment.vue'
import router from '@/router'

const nickname = ref('')
const currentPassword = ref('')
const newPassword = ref('')
const newPasswordCheck = ref('')
onMounted(() => {
  axios.get('/lan/member/me').then((res) => {
    nickname.value = res.data.result.data.nickname
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
  <div id="pageBox">
    <h2 id="MemberInfo">회원 정보</h2>
    <div id="nickname">
      <el-text id="frontTap">닉네임</el-text>
      <el-input v-model="nickname" type="text"></el-input>
      <el-button @click="changeNickname">닉네임 변경</el-button>
    </div>
    <br />
    <h2 id="MemberInfo">비밀번호 수정</h2>
    <div id="nickname">
      <el-text id="frontTap">기존 비밀번호</el-text>
      <el-input v-model="currentPassword" type="password"></el-input>
    </div>
    <div id="nickname">
      <el-text id="frontTap">새 비밀번호</el-text>
      <el-input v-model="newPassword" type="password"></el-input>
    </div>
    <div id="nickname">
      <el-text id="frontTap">새 비밀번호 재입력</el-text>
      <el-input v-model="newPasswordCheck" type="password"></el-input>
    </div>
    <div id="nickname">
      <el-button @click="changePassword">비밀번호 변경</el-button>
    </div>
    <h2 id="MemberInfo">회원 삭제</h2>
    <div id="nickname">
      <el-button @click="deleteMember">회원 삭제</el-button>
    </div>
  </div>
  <Profile />
  <MyPost />
  <MyComment />
</template>

<style>
#MemberInfo {
  text-align: center;
}

#pageBox {
  margin-top: 2%;
  width: 60%;
  margin-left: 20%;
  background-color: #ffe87c;
  position: absolute;
  height: 50%;
}

#frontTap {
  margin-left: 40%;
}

#nickname {
  width: 70%;
  display: grid;
  grid-auto-flow: column;
}
</style>
