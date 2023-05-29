<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'

let postResult = ref([])
let commentResult = ref([])
const page = ref(0)
const size = ref(5)

onMounted(() => {
  axios
      .get('/lan/post/of', {
        params: { page: page.value, size: size.value }
      })
      .then((res) => {
        postResult.value = res.data.result.data.result.content
      })
  axios
      .get('/lan/comment/member', {
        params: {
          page: 0,
          size: 5
        }
      })
      .then((res) => {
        console.log(res)
        commentResult.value = res.data.result.data.readCommentResponseDto.content
      })
})
</script>

<template>
  <div id="postInfo">
    <div id="myPostInfo">
      <h3 id="myPostTitle">나의 게시글</h3>
      <RouterLink to="/post/read/mine" id="myPostMore">더보기</RouterLink>
    </div>
    <div id="postBackground">
      <ul>
        <li v-for="post in postResult" class="mt-1">
          <RouterLink :to="{ name: 'readPost', params: { postId: post.id } }">
            {{post.title }}
          </RouterLink>
          <br />
          <el-text>{{ post.createdDate }}</el-text>
          <br />
          <el-text>{{ post.categoryName }}</el-text>
        </li>
      </ul>
    </div>
    <div id="myCommentInfo">
      <h3 id="myCommentTitle">나의 댓글</h3>
      <RouterLink to="" id="myCommentMore">더보기</RouterLink>
    </div>
    <div id="commentBackground">
      <ul>
        <li v v-for="each in commentResult">
          <el-text>내 댓글 : {{ each.content }}</el-text>
        </li>
      </ul>
    </div>
  </div>
</template>

<style>
#postInfo {
  position: absolute;
  margin-left: 2%;
  width: 15%;
  background: #FEEFCA;
  border-radius: 15px;
}

#postBackground{
  width: 90%;
  margin-left: 5%;
  background: #FFFFFF;
  border-radius: 15px;
}

#myPostTitle {
  margin-left: 7%;
}

#myPostMore {
  margin-top: 33%;
  position: relative;
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 700;
  letter-spacing: -0.015em;
  color: rgba(0, 0, 0, 0.42);
}

#myPostInfo {
  display: grid;
  grid-auto-flow: column;
}

#commentInfo {
  margin-top: 20%;
  position: absolute;
  margin-left: 2%;
  width: 15%;
  background: #FEEFCA;
  border-radius: 15px;
}

#commentBackground{
  width: 90%;
  margin-left: 5%;
  background: #FFFFFF;
  border-radius: 15px;
}

#myCommentTitle {
  margin-left: 7%;
}

#myCommentMore {
  margin-top: 33%;
  position: relative;
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 700;
  letter-spacing: -0.015em;
  color: rgba(0, 0, 0, 0.42);
}

#myCommentInfo {
  display: grid;
  grid-auto-flow: column;
}
</style>
