<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import router from '@/router'

const receiver = ref('')
const message = ref('')

const sendMessage = function () {
  axios
    .post('/lan/message', {
      content: message.value,
      receiver: receiver.value
    })
    .then((res) => {
      console.log(res)
      alert('쪽지를 성공적으로 발송했습니다.')
      router.push({ name: 'messageMain' })
    })
    .catch((reason) => alert(reason.response.data.result.failMessage))
}
</script>
<template>
  <div id="writeMessageForm">
    <h2 id="writeMessageTitle">쪽지 보내기</h2>
    <div id="receiverData">
      <p>받는 사람 <input v-model="receiver" /></p>
    </div>
    <div id="writeMessagePlate">
      <input id="writeMessageInput" v-model="message" />
    </div>
  </div>
  <button @click="sendMessage" id="sendMessageButton">쪽지 보내기</button>
</template>
<style>
#writeMessageForm {
  box-sizing: border-box;
  position: absolute;
  width: 70%;
  margin-left: 15%;
  margin-top: 7%;
  height: 70%;
  background: #feefca;
  border: 1px solid #000000;
  border-radius: 15px;
}

#writeMessageTitle {
  margin-left: 2%;
  position: absolute;
}

#writeMessagePlate {
  position: absolute;
  margin-left: 10%;
  width: 80%;
  margin-top: 13%;
  height: 65%;
  background: #ffffff;
}

#receiverData {
  position: absolute;
  margin-left: 5%;
  margin-top: 5%;
}

#writeMessageInput {
  margin-left: 2%;
  margin-top: 2%;
  width: 94%;
  height: 90%;
}

#sendMessageButton {
  position: absolute;
  margin-left: 70%;
  margin-top: 43%;
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 15px;
}
</style>
