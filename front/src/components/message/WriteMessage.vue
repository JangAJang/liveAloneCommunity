<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import router from "@/router";

const receiver = ref('')
const message = ref('')

const sendMessage = function () {
  axios.post("/lan/message", {
    content: message.value,
    receiver: receiver.value
  }).then(res => {
    console.log(res);
    alert("쪽지를 성공적으로 발송했습니다.")
    router.push({name: 'main'})
  }).catch(e => console.log(e))
}


</script>
<template>
  <div>
    <input v-model="receiver" placeholder="수신자의 이름을 입력해주세요"/>
    <textarea v-model="message" placeholder="내용을 입력해주세요"/>
    <el-button @click="sendMessage">보내기</el-button>
  </div>
</template>
<style>

</style>