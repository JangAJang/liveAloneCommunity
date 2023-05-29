<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import Profile from '@/components/main/Profile.vue'
import router from '@/router'
import MyData from "@/components/main/MyData.vue";

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
  <MyData/>
  <div id="writePostBox">
    <div id="writePostArea">
      <input v-model="title" placeholder="제목" id="editTitle"/>
      <div>
        <textarea v-model="content" placeholder="내용" id="editContent" />
      </div>
    </div>
  </div>
  <button id="savePostButton" @click="savePost">
    <p id="savePostText">저장</p>
  </button>
  <Profile />
</template>

<style>
#savePostButton {
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  position: absolute;
  left: 46%;
  width: 8%;
  margin-top: 45%;
  background: #FFEB35;
  border: 3px solid #000000;
  border-radius: 15px;
  align-items: center;
}

#savePostText{
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 15px;
  align-content: center;
  color: #000000;
  margin-left: 29%;
}

#writePostBox {
  box-sizing: border-box;
  position: absolute;
  width: 60%;
  margin-left: 20%;
  background: #FFFFFF;
  border: 3px solid #000000;
  border-radius: 15px;
  height: 75%
}

#editTitle {
  width: 58%;
  position: absolute;
}

#editContent {
  position: absolute;
  margin-top: 6%;
  height: 73%;
  width: 87%;
}

#writePostArea{
  margin-top: 5%;
  margin-left: 5%;
  width: 90%;
}
</style>
