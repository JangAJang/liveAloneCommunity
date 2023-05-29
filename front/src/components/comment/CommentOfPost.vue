<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import router from '@/router'

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true
  }
})
const content = ref('')
const editContent = ref('')
const comments = ref([])
const editContentIndex = ref(-1)
const page = ref(1)
const maxPage = ref(1)
const nickname = ref('')

const writeComment = function () {
  axios
    .post('/lan/comment', {
      postId: props.postId,
      content: content.value
    })
    .then((res) => {
      console.log(res)
      alert('댓글이 저장되었습니다.')
      router.push({ name: 'readPost', params: { postId: props.postId } })
    })
}

const increasePage = function () {
  if (page.value == maxPage.value) {
    alert('마지막 페이지입니다.')
    return
  }
  page.value++
  getComment()
}

const decreasePage = function () {
  if (page.value == 1) {
    alert('1번 페이지입니다.')
    return
  }
  page.value--
  getComment()
}

const getComment = function () {
  axios
    .get('/lan/comment/post', {
      params: {
        postId: props.postId,
        page: page.value,
        size: 10
      }
    })
    .then((res) => {
      comments.value = res.data.result.data.readCommentResponseDto.content
      maxPage.value = res.data.result.data.readCommentResponseDto.totalPages
    })
}

onMounted(() => {
  getComment()
  axios.get('/lan/member/me').then((response) => {
    nickname.value = response.data.result.data.nickname
  })
})

const isMyComment = function (comment) {
  return comment.nickname == nickname.value
}

const placeText = function (index) {
  console.log(index)
  if (index >= 0 && index < comments.value.length) {
    editContent.value = comments.value[index].content
    editContentIndex.value = index
    return
  }
  console.log('없는 댓글')
}

const isEditingComment = function (index) {
  return editContentIndex.value === index
}

const saveText = function (comment) {
  console.log(comment)
  axios
    .patch('/lan/comment/edit', {
      commentId: comment.id,
      modifyContent: editContent.value
    })
    .then((res) => {
      alert('댓글 수정이 완료되었습니다.')
      router.push({ name: 'readPost', params: { postId: props.postId } })
    })
    .catch((reason) => alert(reason.response.data.result.failMessage))
}

const deleteComment = function (comment) {
  axios
    .delete('/lan/comment', {
      params: {
        id: comment.id
      }
    })
    .then(() => {
      alert('댓글이 삭제되었습니다.')
      router.push({ name: 'readPost', params: { postId: props.postId } })
    })
    .catch((reason) => alert(reason.response.data.result.failMessage))
}
</script>

<template>
  <div id="writeCommentBox">
    <textarea placeholder="내용 작성" id="commentTextArea" v-model="content"></textarea>
    <button id="commentWriteButton" @click="writeComment">댓글 저장</button>
  </div>
  <div>
    <ul class="mt-5">
      <li v-for="(comment, index) in comments" :key="index">
        <div>
          <p>{{ comment.nickname }}</p>
          <p>작성일자 : {{ comment.createdDate }}</p>
          <p v-if="!isEditingComment(index)">내용 : {{ comment.content }}</p>
          <form v-if="isEditingComment(index)" id="writeCommentBox">
            <textarea v-if="isMyComment(comment)" v-model="editContent"></textarea>
            <el-button v-if="isMyComment(comment)" @click="saveText(comment)">저장</el-button>
          </form>
          <div id="customComment">
            <p v-if="isMyComment(comment)" @click="placeText(index)" id="customCommentText">수정</p>
            <p v-if="isMyComment(comment)" @click="deleteComment(comment)" id="customCommentText">삭제</p>
          </div>
        </div>
      </li>
    </ul>
    <div id="pageBox">
      <el-button id="pageButton" @click="decreasePage">이전 페이지</el-button>
      <el-text>{{ page }}</el-text>
      <el-button id="pageButton" @click="increasePage">다음 페이지</el-button>
    </div>
  </div>
</template>

<style>
#writeCommentBox {
  margin-top: 2%;
  display: grid;
  grid-auto-flow: column;
  margin-left: 20%;
  width: 60%;
}

#commentTextArea {
  height: 120%;
}
#commentWriteButton {
  height: 120%;
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

#customComment {
  display: grid;
  grid-auto-flow: column;
  width: 30%;
  margin-left: 70%;
  position: absolute;
  margin-top: -15%;
}

#customCommentText {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 700;
  letter-spacing: -0.015em;
  color: rgba(0, 0, 0, 0.42);
}
</style>
