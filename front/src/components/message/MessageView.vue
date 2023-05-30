<script setup lang="ts">
import axios from 'axios'
import { onMounted, ref } from 'vue'
import router from '@/router'

const message = ref({})

const props = defineProps({
  messageId: {
    type: [Number, String],
    required: true
  }
})

const containsMessage = function () {
  return message.value.nickname !== null
}

const getPostData = function () {
  axios
    .get('/lan/message', {
      params: {
        id: props.messageId
      }
    })
    .then((res) => {
      message.value = res.data.result.data
    })
    .catch(() => {
      message.value = { nickname: null }
    })
}

const deleteMessage = function () {
  axios
    .delete('/lan/message', {
      params: {
        id: message.value.id
      }
    })
    .then(() => {
      alert('쪽지를 삭제했습니다.')
      router.push({ name: 'messageMain' })
    })
}

onMounted(() => getPostData())
</script>
<template>
  <div>
    <p>{{ message.sender }}</p>
    <p>{{ message.receiver }}</p>
    <p>{{ message.content }}</p>
    <p>{{ message.createDate }}</p>
  </div>
  <el-button-group>
    <el-button v-if="containsMessage" @click="deleteMessage">삭제하기</el-button>
  </el-button-group>
</template>
<style></style>
