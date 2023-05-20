<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import Profile from "@/components/main/Profile.vue";
import MyComment from "@/components/main/MyComment.vue";

const nickname = ref('')
const currentPassword = ref('')
const newPassword = ref('')
const newPasswordCheck = ref('')
onMounted(() => {
  axios.get("/lan/member/me")
      .then(res => {
        nickname.value = res.data.result.data.nickname
      })
})
</script>

<template>
  <div id="pageBox">
    <h2 id="MemberInfo">회원 정보</h2>
    <div id="nickname">
      <el-text id="frontTap">닉네임</el-text>
      <el-input v-model="nickname"></el-input>
      <el-button>닉네임 변경</el-button>
    </div>
    <br/>
    <h2 id="MemberInfo">비밀번호 수정</h2>
    <div id="nickname">
      <el-text id="frontTap">기존 비밀번호</el-text>
      <el-input v-model="nickname"></el-input>
      <el-button>닉네임 변경</el-button>
    </div>
    <div id="nickname">
      <el-text id="frontTap">새 비밀번호</el-text>
      <el-input v-model="nickname"></el-input>
      <el-button>닉네임 변경</el-button>
    </div>
    <div id="nickname">
      <el-text id="frontTap">새 비밀번호 재입력</el-text>
      <el-input v-model="nickname"></el-input>
      <el-button>닉네임 변경</el-button>
    </div>
    <h2 id="MemberInfo">회원 삭제</h2>
    <div id="nickname">
      <el-button>회원 삭제</el-button>
    </div>
  </div>
  <Profile/>
  <MyPost/>
  <MyComment/>
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