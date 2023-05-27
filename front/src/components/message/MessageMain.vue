<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import router from '@/router'
import MessageView from '@/components/message/MessageView.vue'

const messages = ref([])
const readMessageType = ref('ALL')
const searchMessageType = ref(null)
const page = ref(1)
const maxPage = ref(1)
const size = 10
const me = ref('')
const text = ref('')
const messageId = ref()
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
    })
}

const startSearch = function () {
  page.value = 1;
  searchMessage();
}

const searchMessage = function () {
  axios.get("/lan/message/search", {
    params: {
      text: text.value,
      searchMessageType: searchMessageType.value,
      readMessageType: readMessageType.value,
      page: page.value,
      size: 10
    }
  })
      .then(res => {
        messages.value = res.data.result.data.result.content
        messageId.value = res.data.result.data.result.content[0].id
        maxPage.value = res.data.result.data.result.totalPages
      })
      .catch(e => {
        console.log(e)
      })
}

const limitContent = function (content) {
  return content.substring(0, 10)
}

const increasePage = function () {
  if (page.value == maxPage.value) {
    alert('마지막 페이지입니다.')
    return
  }
  page.value++
  if(isReadMessage()){
    readMessage()
    return;
  }
  searchMessage();
}

const decreasePage = function () {
  if (page.value == 1) {
    alert('1번 페이지입니다.')
    return
  }
  page.value--
  if(isReadMessage()){
    readMessage()
    return;
  }
  searchMessage();
}

const isReadMessage = function () {
  return searchMessageType.value === null
}
</script>
<template>
  <div id="messageBlock">
    <div id="messageContent">
      <div id="messageTypeButton">
        <el-button-group>
          <div id="searchBox">
            <el-select v-model="searchMessageType" placeholder="검색방식">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
            <el-input placeholder="검색어를 입력해주세요." v-model="text" />
            <el-button @click="startSearch">검색</el-button>
          </div>
          <el-button @click="allMessage">전체 쪽지함</el-button>
          <el-button @click="receivedMessage">받은 쪽지함</el-button>
          <el-button @click="sentMessage">보낸 쪽지함</el-button>
        </el-button-group>
        <ul>
          <li v-for="message in messages">
            <div id="messageEach">
              <div v-if="isSentMessage(message)">
                <el-text>받은 사람 : {{ message.receiver }}</el-text>
                <br />
              </div>
              <div v-if="!isSentMessage(message)">
                <el-text>보낸 사람 : {{ message.sender }}</el-text>
                <br />
              </div>
              <el-text>내용 : {{ limitContent(message.content) }}</el-text>
            </div>
          </li>
        </ul>
      </div>
      <div id>
        <el-button id="pageButton" @click="decreasePage">이전 페이지</el-button>
        <el-text>{{ page }}</el-text>
        <el-button id="pageButton" @click="increasePage">다음 페이지</el-button>
      </div>
    </div>
    <el-button @click="moveToWrite">쪽지 작성</el-button>
  </div>
  <MessageView :messageId="messageId" />
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

#messageEach {
  margin-top: 2%;
  background-color: #ffc520;
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
