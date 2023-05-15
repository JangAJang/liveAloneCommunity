<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'

let result = ref([])
onMounted(() => {
  axios
    .get('/lan/comment/member', {
      params: {
        page: 0,
        size: 5
      }
    })
    .then((res) => {
      result.value = res.data.result.data.readCommentResponseDto
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
  margin-top: 2%;
  width: 15%;
  height: 100%;
  background-color: #ffe87c;
  text-align: center;
}
</style>
