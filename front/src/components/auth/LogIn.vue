<script setup lang="ts">
import { ref } from 'vue'
import axios from "axios";
import router from "@/router";

const username = ref('');
const password = ref('');

const signIn = function () {
    axios.post("/lan/auth/logIn", {
        username: username.value,
        password: password.value
    }).then(()=> {
        alert(username.value+"님의 방문을 환영합니다")
        router.replace({name: 'main'})
    }).catch(reason => alert(reason.response.data.result.failMessage));

}

</script>
<template>
    <div id="logInPart">
        <el-input v-model="username" type="text" placeholder="아이디를 입력하세요"/>
        <el-input v-model="password" type="password" placeholder="비밀번호를 입력하세요"/>
        <el-button type="primary" @click="signIn()" id="logInButton">로그인</el-button>
    </div>
</template>
<style>
#logInPart {
    width:50%;
    height:50%;
    margin-top: 5%;
    margin-left: 25%;
}
#logInButton{
    margin-left: 35%;
    width:30%;
    height: 10%
}
</style>
