<script setup lang="ts">
import axios from 'axios'
import { onMounted, ref } from 'vue'
import Profile from '@/components/main/Profile.vue'
import router from '@/router'
import { RouterView } from 'vue-router'
import MyData from "@/components/main/MyData.vue";

const category = ref('HOBBY_SHARE')
const page = ref(1)
const size = ref(5)
const maxPage = ref(1)
let posts = ref([])
const text = ref('')
const searchPostType = ref('')
const options = [
  {
    value: 'TITLE',
    label: '제목'
  },
  {
    value: 'TITLE_AND_CONTENT',
    label: '제목 & 내용'
  },
  {
    value: 'WRITER_AND_TITLE',
    label: '작성자 & 제목'
  },
  {
    value: 'WRITER',
    label: '작성자'
  },
  {
    value: 'CONTENT',
    label: '내용'
  }
]

const getHobby = function () {
  page.value = 1
  category.value = 'HOBBY_SHARE'
  getPosts()
}

const getLost = function () {
  page.value = 1
  category.value = 'LOST'
  getPosts()
}

const getVillageInfo = function () {
  page.value = 1
  category.value = 'VILLAGE_INFO'
  getPosts()
}

const getSingleNews = function () {
  page.value = 1
  category.value = 'SINGLE_NEWS'
  getPosts()
}

const getRequests = function () {
  page.value = 1
  category.value = 'REQUESTS'
  getPosts()
}

const getPosts = function () {
  posts.value = []
  axios
    .get('/lan/post/category', {
      params: {
        category: category.value,
        page: page.value,
        size: size.value
      }
    })
    .then((res) => {
      maxPage.value = res.data.result.data.result.totalPages
      posts.value = res.data.result.data.result.content
      console.log(res.data.result.data.result.content)
      console.log(posts.value)
    })
}

const searchPost = function () {
  posts.value = []
  category.value = 'Search'
  axios
    .get('/lan/post/search', {
      params: {
        page: page.value,
        size: size.value,
        text: text.value,
        searchPostType: searchPostType.value
      }
    })
    .then((res) => {
      console.log(res)
      maxPage.value = res.data.result.data.result.totalPages
      posts.value = res.data.result.data.result.content
      console.log(res.data.result.data.result.content)
      console.log(posts.value)
    })
}

const increasePage = function () {
  if (page.value == maxPage.value) {
    alert('마지막 페이지입니다.')
    return
  }
  if (category.value == 'Search') {
    page.value++
    searchPost()
    return
  }
  page.value++
  getPosts()
}

const decreasePage = function () {
  if (page.value == 1) {
    alert('1번 페이지입니다.')
    return
  }
  if (category.value == 'Search') {
    page.value--
    searchPost()
    return
  }
  page.value--
  getPosts()
}

const goToWritePost = function () {
  router.push({ name: 'writePost' })
}

onMounted(() => getPosts())
</script>
<template>
  <MyData/>
  <Profile />
  <div id="postBox">
    <div>
      <el-button-group>
        <el-button id="botton" @click="getHobby">취미 공유</el-button>
        <el-button id="botton" @click="getVillageInfo">동네 정보</el-button>
        <el-button id="botton" @click="getLost">분실물</el-button>
        <el-button id="botton" @click="getSingleNews">1인 가구 소식</el-button>
        <el-button id="botton" @click="getRequests">건의 사항</el-button>
      </el-button-group>
    </div>
    <div>
      <ul>
        <li v-for="post in posts">
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
    <div id="searchBox">
      <el-select v-model="searchPostType" placeholder="Select">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <div id="searchText">
        <el-input v-model="text" />
      </div>
      <el-button @click="searchPost">검색</el-button>
      <el-button @click="goToWritePost">글 작성</el-button>
    </div>
    <div id="pageBox">
      <el-button id="pageButton" @click="decreasePage">이전 페이지</el-button>
      <el-text>{{ page }}</el-text>
      <el-button id="pageButton" @click="increasePage">다음 페이지</el-button>
    </div>
  </div>
</template>

<style>
#postBox {
  margin-top: 2%;
  width: 60%;
  margin-left: 20%;
  background-color: #ffe87c;
  position: absolute;
}

#botton {
  background-color: #ffc520;
  width: 20%;
}

#searchBox {
  position: absolute;
  display: grid;
  grid-auto-flow: column;
  width: 70%;
  margin-left: 15%;
}

#pageBox {
  margin-left: 40%;
  width: 50%;
  margin-top: 5%;
  position: absolute;
}

#pageButton {
  margin-left: 5%;
  margin-right: 5%;
}

#searchText {
  align-content: center;
}
</style>
