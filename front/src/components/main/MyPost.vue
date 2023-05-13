<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'

let result = []
const page = ref(0)
const size = ref(5)
const id = ref(1)

onMounted(() => {
  axios
    .get('/lan/post/of', {
      params: {page: page.value, size: size.value }
    })
    .then((res) => {
      result = res.data.result.data.result
    })
})
</script>

<template>
  <div id="postInfo">
    <ul>
      <li v-for="post in result" class="mt-1">
        <el-text>{{ post.title }}</el-text>
        <br />
        <el-text>{{ post.createdDate }}</el-text>
        <br />
        <el-text>{{ post.categoryName }}</el-text>
      </li>
    </ul>
  </div>
</template>

<style>
#postInfo {
  margin-left: 83%;
  margin-top: 2%;
  width: 15%;
  height: 100%;
  background-color: #ffe87c;
  text-align: center;
}

#text {
  align-content: center;
}
</style>
