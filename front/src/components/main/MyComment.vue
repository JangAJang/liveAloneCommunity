<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'

let result = []
onMounted(() => {
  axios
    .get('/lan/comment/member', {
      params: {
        page: 0,
        size: 5
      }
    })
    .then((res) => {
      result = res.data.result.data.readCommentResponseDto
    })
})
</script>
<template>
  <div id="commentInfo">
    <ul>
      <li v v-for="each in result">
        <el-text>게시글 : {{ each.title }}</el-text>
        <el-text>내 댓글 : {{ each.content }}</el-text>
      </li>
    </ul>
  </div>
</template>

<style>
#commentInfo {
  margin-left: 83%;
  margin-top: 5%;
  width: 15%;
  background-color: #ffe87c;
  text-align: center;
}
</style>
