<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { RouterView } from 'vue-router'
import Profile from '@/components/main/Profile.vue'
import router from '@/router'
import MyData from '@/components/main/MyData.vue'

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
  <MyData />
  <div id="writePostBox">
    <div id="writePostArea">
      <div id="upper">
        <div id="writeTitle">
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
  background: #ffeb35;
  border: 3px solid #000000;
  border-radius: 15px;
  align-items: center;
}

#savePostText {
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 15px;
  align-content: center;
  color: #000000;
  margin-left: 29%;
}

#upper {
  margin-top: 1%;
  display: grid;
  grid-auto-flow: column;
}

#writePostBox {
  box-sizing: border-box;
  position: absolute;
  width: 60%;
  margin-left: 20%;
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 15px;
  height: 75%;
}

#category {
  margin-left: 2%;
}

#writeTitle {
  width: 58%;
  margin-left: 32%;
  position: absolute;
}

#content {
  position: absolute;
  margin-left: 2%;
  margin-top: 1%;
  height: 73%;
  width: 87%;
}

#writePostArea {
  margin-top: 5%;
  margin-left: 5%;
  width: 90%;
}
</style>
