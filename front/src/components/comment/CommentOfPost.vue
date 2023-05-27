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
      router.replace({ name: 'readPost', params: { postId: props.postId } })
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
      router.replace({ name: 'readPost', params: { postId: props.postId } })
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
        <div id="commentEach">
          <el-text>작성자 : {{ comment.nickname }}</el-text>
          <br />
          <el-text>작성일자 : {{ comment.createdDate }}</el-text>
          <br />
          <el-text v-if="!isEditingComment(index)">내용 : {{ comment.content }}</el-text>
          <br />
          <form v-if="isEditingComment(index)" id="writeCommentBox">
            <textarea v-if="isMyComment(comment)" v-model="editContent"></textarea>
            <el-button v-if="isMyComment(comment)" @click="saveText(comment)">저장</el-button>
          </form>
          <el-button-group>
            <el-button v-if="isMyComment(comment)" @click="placeText(index)">수정</el-button>
            <el-button v-if="isMyComment(comment)" @click="deleteComment(comment)">삭제</el-button>
          </el-button-group>
        </div>
      </li>
    </ul>
  </div>
  <div id="pageBox">
    <el-button id="pageButton" @click="decreasePage">이전 페이지</el-button>
    <el-text>{{ page }}</el-text>
    <el-button id="pageButton" @click="increasePage">다음 페이지</el-button>
  </div>
</template>

<style>
#writeCommentBox {
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

#commentEach {
  margin-top: 2%;
  width: 80%;
  margin-left: 10%;
  background-color: #ffc520;
  align-content: center;
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
</style>
