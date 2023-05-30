<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import MyData from '@/components/main/MyData.vue'
import Profile from '@/components/main/Profile.vue'

let postResult = ref([])
const page = ref(1)
const size = ref(5)
const maxPage = ref(100)
onMounted(() => {
  getMyPost()
})

const getMyPost = function () {
  axios
    .get('/lan/post/of', {
      params: { page: page.value, size: size.value }
    })
    .then((res) => {
      postResult.value = res.data.result.data.result.content
      maxPage.value = res.data.result.data.result.totalPages
    })
}
const increasePage = function () {
  if (page.value == maxPage.value) {
    alert('마지막 페이지입니다.')
    return
  }
  page.value++
  getMyPost()
}

const decreasePage = function () {
  if (page.value == 1) {
    alert('1번 페이지입니다.')
    return
  }
  page.value--
  getMyPost()
}
</script>
<template>
  <MyData />
  <Profile />
  <div id="allPostCategory">
    <p id="allPostCategoryText">내 게시글</p>
  </div>
  <div id="postBox">
    <ul>
      <li v-for="post in postResult">
        <div>
          <RouterLink :to="{ name: 'readPost', params: { postId: post.id } }">
            제목 : {{ post.title }}
          </RouterLink>
          <br />
          <text>작성자 : {{ post.writer }}</text>
          <br />
          <text>작성일 : {{ post.createdDate }}</text>
          <br />
        </div>
        <br />
      </li>
    </ul>
  </div>
  <div id="myPostPageBox">
    <el-button id="pageButton" @click="decreasePage">이전 페이지</el-button>
    <el-text>{{ page }}</el-text>
    <el-button id="pageButton" @click="increasePage">다음 페이지</el-button>
  </div>
</template>

<style>
#allPostCategory {
  box-sizing: border-box;
  position: absolute;
  margin-left: 20%;
  width: 60%;
  margin-top: -10%;
  height: 10%;
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 15px;
}

#allPostCategoryText {
  position: absolute;
  margin-left: 37.5%;
  margin-top: -0.05%;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 40px;
  line-height: 48px;
  color: #000000;
}

#postBox {
  box-sizing: border-box;
  margin-top: -2%;
  position: absolute;
  width: 60%;
  margin-left: 20%;
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 15px;
}

#myPostPageBox {
  margin-left: 30%;
  width: 50%;
  margin-top: 20%;
}

#pageButton {
  margin-left: 5%;
  margin-right: 5%;
}
</style>
