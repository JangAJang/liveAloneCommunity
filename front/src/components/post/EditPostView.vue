<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import MyPost from '@/components/main/MyPost.vue'
import Profile from '@/components/main/Profile.vue'
import MyComment from '@/components/main/MyComment.vue'
import router from '@/router'

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true
  }
})

const title = ref('')
const content = ref('')

onMounted(() => {
  axios.get(`/lan/post?id=${props.postId}`).then((res) => {
    console.log(res.data.result.data)
    title.value = res.data.result.data.title
    content.value = res.data.result.data.content
  })
})

const savePost = function () {
  axios
    .patch('/lan/post/edit', {
      id: props.postId,
      title: title.value,
      content: content.value
    })
    .then((res) => {
      alert('글 수정 성공했습니다.')
      console.log(res)
      router.push({ name: 'readPost', params: { postId: res.data.result.data.id } })
    })
}
</script>

<template>
  <div id="postBox">
    <div id="upper">
      <div id="title">
        <el-input v-model="title" placeholder="제목" />
      </div>
    </div>
    <div>
      <textarea v-model="content" placeholder="내용" id="content" />
    </div>
    <el-button id="button" @click="savePost">저장</el-button>
  </div>

  <Profile />
  <MyPost />
  <MyComment />
</template>

<style>
#button {
  position: absolute;
}

#upper {
  margin-top: 1%;
  display: grid;
  grid-auto-flow: column;
}

#postBox {
  margin-top: 2%;
  width: 60%;
  margin-left: 20%;
  background-color: #ffe87c;
  position: absolute;
}
#title {
  width: 40%;
  margin-left: 2%;
}

#content {
  margin-left: 2%;
  margin-top: 1%;
  height: 400px;
  width: 80%;
}
</style>
