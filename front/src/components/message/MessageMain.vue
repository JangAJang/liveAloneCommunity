<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import router from '@/router'

const messages = ref([])
const message = ref({})
const readMessageType = ref('ALL')
const searchMessageType = ref(null)
const page = ref(1)
const maxPage = ref(1)
const size = 5
const me = ref('')
const text = ref('')
const messageId = ref(1)
const options = [
  {
    value: 'NAME',
    label: '작성자명'
  },
  {
    value: 'CONTENT',
    label: '내용'
  }
]

onMounted(() => {
  page.value = 1
  readMessageType.value = 'ALL'
  readMessage()
  axios.get('/lan/member/me').then((res) => {
    me.value = res.data.result.data.nickname
  })
  getSingleMessage()
})

const moveToWrite = function () {
  router.push({ name: 'writeMessage' })
}

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
  searchMessageType.value = null
  axios
    .get('/lan/message/read', {
      params: {
        readMessageType: readMessageType.value,
        page: page.value,
        size: size
      }
    })
    .then((res) => {
      messages.value = res.data.result.data.result.content
      messageId.value = res.data.result.data.result.content[0].id
      maxPage.value = res.data.result.data.result.totalPages
      console.log(messageId.value)
    })
}

const startSearch = function () {
  page.value = 1
  searchMessage()
}

const getSingleMessage = function () {
  axios
      .get('/lan/message', {
        params: {
          id: messageId.value
        }
      })
      .then((res) => {
        message.value = res.data.result.data
        message.value.content = message.value.content.split('\n').join("<br/>");
      })
      .catch(() => {
        message.value = { nickname: null }
      })
}

const searchMessage = function () {
  axios
    .get('/lan/message/search', {
      params: {
        text: text.value,
        searchMessageType: searchMessageType.value,
        readMessageType: readMessageType.value,
        page: page.value,
        size: 10
      }
    })
    .then((res) => {
      messages.value = res.data.result.data.result.content
      messageId.value = res.data.result.data.result.content[0].id
      maxPage.value = res.data.result.data.result.totalPages
      console.log(messageId.value)
    })
    .catch((e) => {
      console.log(e)
    })
}

const limitContent = function (content) {
  if ( content.length > 10) return content.substring(0, 10) + '...'
  return content
}

const increasePage = function () {
  if (page.value == maxPage.value) {
    alert('마지막 페이지입니다.')
    return
  }
  page.value++
  if (isReadMessage()) {
    readMessage()
    return
  }
  searchMessage()
}

const decreasePage = function () {
  if (page.value == 1) {
    alert('1번 페이지입니다.')
    return
  }
  page.value--
  if (isReadMessage()) {
    readMessage()
    return
  }
  searchMessage()
}

const deleteMessage = function () {
  if (confirm('쪽지를 삭제하시겠습니까?')) {
    axios
        .delete('/lan/message?id='+messageId.value)
        .then(() => {
          alert('쪽지를 삭제했습니다..')
          router.replace({ name: 'messageMain' })
        })
        .catch((reason) => alert(reason.response.data.result.failMessage))
  }
}

const chooseMessage = function (message) {
  messageId.value = message.id
  console.log(messageId.value)
  getSingleMessage()
}

const isReadMessage = function () {
  return searchMessageType.value === null
}
</script>
<template>
  <div id="typeBox">
    <button id="buttonBox" @click="allMessage">전체</button>
    <button id="buttonBox" @click="receivedMessage">받은 쪽지</button>
    <button id="buttonBox" @click="sentMessage">보낸 쪽지</button>
  </div>
  <div id="messageList">
    <ul>
      <li v-for="message in messages">
        <div @click="chooseMessage(message)">
          <h3 v-if="!isSentMessage(message)">보낸사람 : {{message.sender}}</h3>
          <h3 v-if="isSentMessage(message)">받은 사람 : {{message.receiver}}</h3>
          <p>전송일자 : {{message.createDate}}</p>
          <p>{{limitContent(message.content)}}</p>
        </div>
      </li>
    </ul>
  </div>
  <div id="messageView">
    <div id="messageContent">
      <p>보낸 사람 : {{message.sender}}</p>
      <p>받는 사람 : {{message.receiver}}</p>
      <p>전송일자 : {{message.createDate}}</p>
      <p v-html="message.content"></p>
    </div>
  </div>
  <div id="messagePageBox">
    <el-button id="pageButton" @click="decreasePage">이전 페이지</el-button>
    <el-text>{{ page }}</el-text>
    <el-button id="pageButton" @click="increasePage">다음 페이지</el-button>
  </div>
  <div id="messageSearchBox">
    <select v-model="searchMessageType" id="messageTypeSelector">
      <option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"/>
    </select>
    <input v-model="text" type="text" placeholder="검색어를 입력해주세요." id="messageText"/>
    <button @click="startSearch" id="searchButton">검색</button>
  </div>
  <div id="messageButtons">
    <button id="writeMessage" @click="moveToWrite">쪽지 작성</button>
    <button id="deleteMessage" @click="deleteMessage">쪽지 삭제</button>
  </div>
</template>
<style>

#typeBox {
  box-sizing: border-box;
  position: absolute;
  margin-left: 2%;
  margin-top: 5%;
  width: 25%;
  height: 7%;
}

#buttonBox {
  box-sizing: border-box;
  width: 33.3%;
  height: 100%;
  background: #FFFFFF;
  border: 3px solid #000000;
  border-radius: 15px;
}

#messageList {
  box-sizing: border-box;
  position: absolute;
  margin-left: 2%;
  margin-top: 10%;
  width: 25%;
  height: 70%;
  background: #FFFFFF;
  border: 3px solid #000000;
  border-radius: 15px;
}

#messageView {
  box-sizing: border-box;
  position: absolute;
  margin-left: 29%;
  margin-top: 10%;
  width: 69%;
  height: 70%;
  background: #FFFFFF;
  border: 3px solid #000000;
  border-radius: 15px;
}

#messagePageBox {
  box-sizing: border-box;
  position: absolute;
  margin-left: 2%;
  margin-top: 52%;
  width: 25%;
  height: 8%;
}

#messageSearchBox {
  box-sizing: border-box;
  position: absolute;
  margin-left: 29%;
  margin-top: 5%;
  width: 69%;
  height: 7%;
}

#pageButton {
  height: 100%;
  background: #FFFFFF;
  border: 3px solid #000000;
  border-radius: 15px;
}

#messageTypeSelector {
  height: 100%;
  background: #FFFFFF;
  border: 3px solid #000000;
  border-radius: 15px;
  width: 30%;
}
#messageText {
  width: 50%;
  height:80%;
  background: #FFFFFF;
  border: 3px solid #000000;
  border-radius: 15px;
}

#searchButton {
  background: #FFFFFF;
  position: absolute;
  border: 3px solid #000000;
  border-radius: 15px;
  height: 100%;
  width: 20%;
}

#messageButtons {
  position: absolute;
  margin-left: 80%;
  width: 18%;
  height: 7%;
  margin-top: 52%;
}

#writeMessage {
  background: #FFFFFF;
  position: absolute;
  border: 3px solid #000000;
  border-radius: 15px;
  width: 50%;
  height: 100%;
}

#deleteMessage {
  background: #FFFFFF;
  position: absolute;
  border: 3px solid #000000;
  border-radius: 15px;
  width: 50%;
  margin-left: 50%;
  height: 100%;
}

#messageContent {
  margin-left: 2%;
  margin-top: 2%;
}
</style>
