<script setup lang="ts">
import axios from 'axios'
import { onMounted, ref } from 'vue'
import Profile from '@/components/main/Profile.vue'
import router from '@/router'
import CommentOfPost from '@/components/comment/CommentOfPost.vue'
import MyData from "@/components/main/MyData.vue";

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
    content.value = res.data.result.data.content.split('\n').join("<br/>");
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
  <MyData/>
  <div>
  </div>
  <div id="readPostBackground">
    <div id="postDataView">
      <div id="postGap">
        <div>
          <h3>글쓴이 : {{ writer }}</h3>
          <h3>{{ createdDate }}</h3>
        </div>
        <h2>{{ title }}</h2>
        <p v-html="content"></p>
      </div>
    </div>
    <CommentOfPost :post-id="props.postId"/>
  </div>
  <p @click="goToEdit" id="editPostLink">수정하기</p>
  <p @click="requestDelete" id="deletePostLink">삭제하기</p>
  <Profile />
</template>

<style>
#readPostBackground {
  box-sizing: border-box;
  position: absolute;
  width: 60%;
  margin-left: 20%;
  background: #FFFFFF;
  border: 3px solid #000000;
  border-radius: 15px;
}

#postDataView {
  box-sizing: border-box;
  width: 98%;
  margin-top: 3%;
  margin-left: 1%;
  background: #FFFFFF;
  border: 3px solid #000000;
}

#postGap {
  margin-top: 2%;
  margin-left: 2%;
}

#editPostLink {
  position: absolute;
  margin-left: 65%;
  margin-top: 5%;
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 700;
  letter-spacing: -0.015em;
  color: rgba(0, 0, 0, 0.42);
}

#deletePostLink {
  position: absolute;
  margin-left: 72%;
  margin-top: 5%;
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 700;
  letter-spacing: -0.015em;
  color: rgba(0, 0, 0, 0.42);
}
</style>
