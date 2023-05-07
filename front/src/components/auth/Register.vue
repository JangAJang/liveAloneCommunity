<script setup lang="ts">
import { ref } from 'vue'
import axios from "axios";

const username = ref('')
const nickname = ref('')
const email = ref('')
const password = ref('')
const passwordCheck = ref('')
const authNum = ref('')
const emailVerified = ref(false);

const join = function () {
    if(!emailVerified){
        alert("이메일 인증을 먼저 진행해야 합니다.");
        return;
    }
    axios.post("/lan/api/auth/register", {
        username: username.value,
        nickname: nickname.value,
        email: email.value,
        password: password.value,
        passwordCheck: passwordCheck.value
    })
        .catch(reason => alert(reason));
}

const sendEmail = function () {
    axios.post("/lan/api/email/send", {
        email: email.value
    }).then(()=> alert('인증번호를 메일로 발송했습니다. 인증번호를 확인해주세요.'))
        .catch((e) => {
            alert("인증번호를 전송할 수 없습니다. 다른 이메일을 이용해주세요.");
            alert(e);
            return;
        })
}

const validateEmail = function () {
    axios.post("/lan/pai/email/verify", {
        email: email.value,
        authNum: authNum.value
    }).catch(reason=> {
        alert(reason);
        return;
    }).then(()=> alert("이메일을 성공적으로 인증했습니다."))
}
</script>

<template>
    <div class="align-content-center">
        <div class="mt-1">
            <el-input v-model="username" type="text" placeholder="아이디를 입력해주세요." />
        </div>
        <div class="mt-1">
            <el-input v-model="nickname" type="text" placeholder="닉네임을 입력해주세요." />
        </div>
        <div class="mt-1">
            <el-input v-model="email" type="email" placeholder="이메일을 입력해주세요." />
            <div>
                <el-button type="primary" @click="sendEmail()">인증번호 전송</el-button>
                <el-input v-model="authNum" type="text" placeholder="인증 번호를 입력해주세요." />
            </div>
            <el-button type="primary" @click="validateEmail()">인증번호 확인</el-button>
        </div>
        <div class="mt-1">
            <el-input v-model="password" type="password" placeholder="비밀번호를 입력해주세요." />
        </div>
        <div class="mt-1">
            <el-input
                    v-model="passwordCheck"
                    type="password"
                    placeholder="비밀번호를 다시 입력해주세요."
            />
        </div>
        <div class="mt-1">
            <el-button type="primary" @click="join()">회원 가입</el-button>
        </div>
    </div>
</template>

<style></style>
