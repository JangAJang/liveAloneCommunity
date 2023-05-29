<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { RouterView } from 'vue-router'
import Profile from '@/components/main/Profile.vue'
import router from '@/router'
import MyPage from "@/components/member/MyPage.vue";

const title = ref('')
const content = ref('')
const category = ref('')
const options = [
  {
    value: 'HOBBY_SHARE',
    label: '취미 공유'
  },
  {
    value: 'LOST',
    label: '분실'
  },
  {
    value: 'VILLAGE_INFO',
    label: '동네 정보공유'
  },
  {
    value: 'SINGLE_NEWS',
    label: '1인 가구 소식'
  },
  {
    value: 'REQUESTS',
    label: '건의사항'
  }
]

const savePost = function () {
  axios
    .post('/lan/post/write', {
      category: category.value,
      title: title.value,
      content: content.value
    })
    .then((res) => {
      alert('글 작성에 성공했습니다.')
      console.log(res)
      router.push({ name: 'readPost', params: { postId: res.data.result.data.id } })
    })
}
</script>

<template>
  <MyPage/>
  <div id="postBox">
    <div id="upper">
      <div id="title">
        <el-input v-model="title" placeholder="제목" />
      </div>
      <div id="category">
        <el-select v-model="category" placeholder="카테고리">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
    </div>
    <div>
      <textarea v-model="content" placeholder="내용" id="content" />
    </div>
    <el-button id="button" @click="savePost">저장</el-button>
  </div>

  <Profile />
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

#category {
  margin-left: 2%;
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
