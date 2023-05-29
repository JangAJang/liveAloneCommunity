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
const categoryName = ref('취미 공유')
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
  categoryName.value = '취미 공유'
  page.value = 1
  category.value = 'HOBBY_SHARE'
  getPosts()
}

const getLost = function () {
  categoryName.value = '분실물'
  page.value = 1
  category.value = 'LOST'
  getPosts()
}

const getVillageInfo = function () {
  categoryName.value = '동네 정보'
  page.value = 1
  category.value = 'VILLAGE_INFO'
  getPosts()
}

const getSingleNews = function () {
  categoryName.value = '1인 가구 소식'
  page.value = 1
  category.value = 'SINGLE_NEWS'
  getPosts()
}

const getRequests = function () {
  categoryName.value = '건의 사항'
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
  <div id="allPostCategory">
    <p id="allPostCategoryText">{{categoryName}}</p>
  </div>
  <div id="postBox">
    <div id="categoryBox">
      <button id="categoryButton" @click="getHobby">
        <p id="categoryText">취미 공유</p>
      </button>
      <button id="categoryButton" @click="getVillageInfo">
        <p id="categoryText">동네 정보</p>
      </button>
      <button id="categoryButton" @click="getSingleNews">
        <p id="categoryText">1인 가구 소식</p>
      </button>
      <button id="categoryButton" @click="getLost">
        <p id="categoryText">분실물</p>
      </button>
      <button id="categoryButton" @click="getRequests">
        <p id="categoryText">건의사항</p>
      </button>
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
      <el-select v-model="searchPostType" placeholder="Select" id="postSearchSelect">
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
#allPostCategory{
  box-sizing: border-box;
  position: absolute;
  margin-left: 20%;
  width: 60%;
  margin-top: -10%;
  height: 10%;
  background: #FFFFFF;
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
  background: #FFFFFF;
  border: 3px solid #000000;
  border-radius: 15px;
}

#pageBox {
  margin-left: 25%;
  width: 50%;
  margin-top: 10%;
  position: absolute;
}

#pageButton {
  margin-left: 5%;
  margin-right: 5%;
}

#categoryBox{
  box-sizing: border-box;
  width: 101%;
  margin-left: -0.5%;
  margin-top: -0.5%;
  height: 65px;
  background: #A4D9FF;
  border: 3px solid #000000;
  border-radius: 15px 15px 0px 0px;
  display: flex;
  flex-direction: row;
}

#categoryButton {
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  padding: 8px;
  gap: 8px;
  height: 80%;
  width: 14%;
  margin-top: 1%;
  margin-left: 5%;
  background: #FFFFFF;
  border: 1px solid #000000;
  border-radius: 15px;
  align-items: center;
}

#categoryText{
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 14px;
  line-height: 36px;
  color: #000000;
}
#searchBox {
  position: absolute;
  display: grid;
  grid-auto-flow: column;
  margin-top: 3%;
  width: 70%;
  margin-left: 15%;
}
</style>
