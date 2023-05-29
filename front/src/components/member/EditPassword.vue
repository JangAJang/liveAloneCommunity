<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import router from "@/router";

const currentPassword = ref('')
const newPassword = ref('')
const newPasswordCheck = ref('')
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
</script>
<template>
  <div id="editPasswordBackground">
    <div id="editPasswordPlace">
      <input v-model="currentPassword" type="text" id="editCurrentPasswordInput" placeholder="기존 비밀번호를 입력해주세요."/>
      <input v-model="newPassword" type="text" id="editNewPasswordInput" placeholder="새 비밀번호를 입력해주세요."/>
      <input v-model="newPasswordCheck" type="text" id="editNewPasswordCheckInput" placeholder="새 비밀번호를 다시 입력해주세요."/>
    </div>
    <button id="editPasswordButton" @click="changePassword">비밀번호 변경</button>
  </div>
</template>
<style>
#editPasswordBackground {
  margin-top: 20%;
  position: absolute;
  width: 60%;
  margin-left: 20%;
  height: 30%;
  background: #FEEFCA;
  border-radius: 15px;
}

#editPasswordPlace{
  position: absolute;
  width: 90%;
  margin-left: 5%;
  margin-top: 3%;
  height: 80%;
  background: #FFFFFF;
}
#editCurrentPasswordInput{
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
#editNewPasswordInput{
  position: absolute;
  margin-top: 7%;
  margin-left: 8%;
  font-family: 'Inter';
  width: 45%;
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}
#editNewPasswordCheckInput{
  position: absolute;
  margin-top: 13%;
  margin-left: 8%;
  font-family: 'Inter';
  width: 45%;
  font-style: normal;
  font-weight: 800;
  font-size: 20px;
  line-height: 24px;
  color: #000000;
}
#editPasswordButton {
  position: absolute;
  width: 20%;
  margin-top: 14.5%;
  height: 15%;
  margin-left: 55%;
  box-sizing: border-box;
  justify-content: center;
  align-items: center;
  background: #A4D9FF;
  border: 1px solid #000000;
  border-radius: 15px;
}
</style>