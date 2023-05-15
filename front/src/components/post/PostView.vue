<script setup lang="ts">
import axios from 'axios'
import { onMounted, ref } from 'vue'
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
const writer = ref('')
const createdDate = ref('')
const category = ref('')

onMounted(() => {
  axios.get(`/lan/post?id=${props.postId}`).then((res) => {
    console.log(res.data.result.data)
    title.value = res.data.result.data.title
    content.value = res.data.result.data.content
    writer.value = res.data.result.data.writer
    createdDate.value = res.data.result.data.createdDate
    category.value = res.data.result.data.categoryName
  })
})

const goToEdit = function () {
  axios
    .get(`/lan/post/mine?id=${props.postId}`)
    .then(() => {
      router.push({ name: 'editPost', params: { postId: props.postId } })
    })
    .catch((reason) => alert(reason.response.data.result.failMessage))
}

const requestDelete = function () {
  axios
    .get(`/lan/post/mine?id=${props.postId}`)
    .then(() => {
      deletePost()
    })
    .catch((reason) => alert(reason.response.data.result.failMessage))
}

const deletePost = function () {
  axios.delete(`/lan/post/delete?id=${props.postId}`).then(() => {
    alert('성공적으로 게시글을 삭제했습니다.')
    router.replace({ name: 'main' })
  })
}
</script>

<template>
  <div id="background">
    <div id="post">
      <h3>제목 : {{ title }}</h3>
      <h3>{{ category }}</h3>
      <h3>내용 : {{ content }}</h3>
      <h3>글쓴이 : {{ writer }}</h3>
      <h3>작성일자 : {{ createdDate }}</h3>
      <el-button-group id="buttons">
        <el-button @click="goToEdit">글 수정</el-button>
        <el-button @click="requestDelete">글 삭제</el-button>
      </el-button-group>
    </div>
  </div>

  <Profile />
  <MyPost />
  <MyComment />
</template>

<style>
#background {
  margin-top: 2%;
  width: 60%;
  margin-left: 20%;
  background-color: #ffe87c;
  position: absolute;
}

#buttons {
  position: absolute;
  margin-left: 85%;
  display: grid;
  grid-auto-flow: column;
}

#post {
  margin-left: 2%;
  margin-top: 2%;
}
</style>
