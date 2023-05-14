<script setup lang="ts">
import axios from "axios";
import {onMounted, ref} from "vue";
import MyPost from "@/components/main/MyPost.vue";
import Profile from "@/components/main/Profile.vue";
import MyComment from "@/components/main/MyComment.vue";

const category = ref('HOBBY_SHARE')
const page = ref(0)
const size = ref(10)
let posts = ref([])

const getHobby = function () {
    category.value = 'HOBBY_SHARE'
    getPosts()
}

const getLost = function () {
    category.value = 'LOST'
    getPosts()
}

const getVillageInfo = function () {
    category.value = 'VILLAGE_INFO'
    getPosts()
}

const getSingleNews = function () {
    category.value = 'SINGLE_NEWS'
    getPosts()
}

const getRequests = function () {
    category.value = 'REQUESTS'
    getPosts()
}


const getPosts = function () {
    axios.get("/lan/post/category", {
        params: {
            category: category.value,
            page: page.value,
            size: size.value
        }
    }).then(res => {
        console.log(res.data.result.data.result)
        posts.value = (res.data.result.data.result)
    })
}
onMounted(() => getPosts())
</script>
<template>
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
                    <text>제목 : {{post.title}}</text>
                    <br/>
                    <text>작성자 : {{post.writer}}</text>
                    <br/>
                    <text>작성일 : {{post.createdDate}}</text>
                    <br/>
                </div>
            </li>
        </ul>
    </div>
</div>
    <Profile/>
    <MyPost/>
    <MyComment/>
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
</style>