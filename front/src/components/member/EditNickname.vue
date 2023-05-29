<script setup lang="ts">
import axios from "axios";
import router from "@/router";
import {ref} from "vue";

const nickname = ref('')

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
</script>
<template>
  <div id="editNicknameBackground">
    <div id="editNicknamePlace">
      <input v-model="nickname" type="text" id="editNicknameInput" placeholder="변경할 닉네임을 입력해주세요."/>
    </div>
    <button id="editNicknameButton" @click="changeNickname">닉네임 변경</button>
  </div>
</template>
<style>
#editNicknameBackground {
  margin-top: 20%;
  position: absolute;
  width: 60%;
  margin-left: 20%;
  height: 15%;
  background: #FEEFCA;
  border-radius: 15px;
}

#editNicknamePlace{
  position: absolute;
  width: 90%;
  margin-left: 5%;
  margin-top: 3%;
  height: 40%;
  background: #FFFFFF;
}
#editNicknameInput{
  position: absolute;
  margin-top: 1%;
  margin-left: 8%;
  font-family: 'Inter';
  width: 45%;
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}
#editNicknameButton {
  position: absolute;
  width: 20%;
  margin-top: 4%;
  height: 30%;
  margin-left: 55%;
  box-sizing: border-box;
  justify-content: center;
  align-items: center;
  background: #A4D9FF;
  border: 1px solid #000000;
  border-radius: 15px;
}
</style>