<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";

const messages = ref([])
const readMessageType = ref('ALL')
const page = ref(1)
const maxPage = ref(1)
const size = 10
const me = ref('')
const text = ref('')

onMounted(() => {
  page.value = 1
  readMessageType.value = 'ALL'
  readMessage()
  axios.get("/lan/member/me")
      .then(res=> {
        me.value = res.data.result.data.nickname
      })
})

const receivedMessage = function () {
  readMessageType.value = 'RECEIVER'
  page.value = 1
  readMessage()
}

const sentMessage = function () {
  readMessageType.value = 'SENDER'
  page.value = 1
  readMessage()
}

const allMessage = function () {
  readMessageType.value = 'ALL'
  page.value = 1
  readMessage()
}

const isSentMessage = function (message) {
  return message.sender === me.value
}

const readMessage = function () {
  axios.get("/lan/message/read", {params: {
      readMessageType: readMessageType.value,
      page: page.value,
      size: size
    }})
      .then(res => {
        messages.value = res.data.result.data.result.content
        maxPage.value = res.data.result.data.result.totalPages
      })
}

const limitContent = function (content) {
  return content.substring(0, 10);
}

</script>
<template>
  <div id="messageBlock">
    <div id="messageContent">
      <div>
        <el-button-group id="messageTypeButton">
          <div id="searchBox">
            <el-input placeholder="검색어를 입력해주세요." v-model="text"/>
            <el-button>검색</el-button>
          </div>
          <el-button @click="allMessage">전체 쪽지함</el-button>
          <el-button @click="receivedMessage">받은 쪽지함</el-button>
          <el-button @click="sentMessage">보낸 쪽지함</el-button>
        </el-button-group>
        <ul>
          <li v-for="message in messages">
            <div v-if="isSentMessage(message)">
              <el-text>받은 사람 : {{message.receiver}}</el-text>
              <br/>
            </div>
            <div v-if="!isSentMessage(message)">
              <el-text>보낸 사람 : {{message.sender}}</el-text>
              <br/>
            </div>
            <el-text>내용 : {{limitContent(message.content)}}</el-text>
            <br/>
            <el-text>전송일자 : {{message.createDate}}</el-text>
          </li>
        </ul>
      </div>
    </div>
  </div>


</template>
<style>

#searchBox {
  display: grid;
  grid-auto-flow: column;
}

#messageBlock {
  width: 85%;
  margin-top: 3%;
  margin-left: 12.5%;
  background-color: #ffe87c;
}

#messageContent {
  width: 95%;
  margin-top: 2.5%;
  margin-left: 2.5%;
}

#messageTypeButton {
  width: 30%;
  display: grid;
  grid-auto-flow: row;
}

</style>