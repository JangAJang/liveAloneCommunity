<script setup lang="ts">
import axios from "axios";
import {onMounted, ref} from "vue";
import MyPost from "@/components/main/MyPost.vue";
import Profile from "@/components/main/Profile.vue";
import MyComment from "@/components/main/MyComment.vue";

const category = ref('HOBBY_SHARE')
const page = ref(1)
const size = ref(5)
let posts = ref([])
const text = ref('')
const searchPostType = ref('')
const options = [
    {
        value: 'TITLE',
        label: '제목',
    },
    {
        value: 'TITLE_AND_CONTENT',
        label: '제목 & 내용',
    },
    {
        value: 'WRITER_AND_TITLE',
        label: '작성자 & 제목',
    },
    {
        value: 'WRITER',
        label: '작성자',
    },
    {
        value: 'CONTENT',
        label: '내용',
    },
]

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
    page.value = 1
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

const searchPost = function () {
    page.value = 1
    category.value = 'Search'
    axios.get("/lan/post/search", {
        params: {
            page: page.value,
            size: size.value,
            text: text.value,
            searchPostType: searchPostType.value
        }
    })
        .then(res => {
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
                <br/>
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
            <el-input v-model="text"/>
        </div>
        <el-button @click="searchPost">검색</el-button>
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

#searchBox {
    position: absolute;
    display: grid;
    grid-auto-flow: column;
    width: 70%;
    margin-left: 15%;
}

#searchText {
    align-content: center;
}
</style>