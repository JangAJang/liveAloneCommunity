<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import router from "@/router";
</script>

<template>
  <header>
    <h1 id="title">Live Alone Network</h1>
    <nav>
      <RouterLink to="/" id="board_link">게시판</RouterLink>
      <RouterLink to="/message" id="message_link">쪽지</RouterLink>
    </nav>
    <nav id="RectangleBase">
      <div id="Rectangle1">
        <button @click="router.push({name: 'logIn'})" >
          <p id="logInText">로그인</p>
        </button>
      </div>
      <div id="Rectangle2">
        <button @click="router.push({name: 'register'})">
          <p id="logInText">회원가입</p>
        </button>
      </div>
    </nav>
  </header>
  <RouterView />
</template>

<style>
header {
  position: absolute;
  height: 8%;
  left: 0%;
  right: 0%;
  top: 0%;
  background: #A4D9FF;
}

#RectangleBase{
  margin-left: 80%;
  width:20%;
  box-sizing: border-box;
  position: absolute;
  left: 0%;
  right: 0%;
  top: 0%;
  bottom: 0%;
}

#Rectangle1{
  width: 50%;
  box-sizing: border-box;
  position: absolute;
  left: 0%;
  right: 0%;
  top: 0%;
  bottom: 0%;
  background: #FFFFFF;
  border: 1px solid #18A0FB;
  border-radius: 6px;
}

#Rectangle2{
  margin-left: 50%;
  width: 50%;
  box-sizing: border-box;
  position: absolute;
  left: 0%;
  right: 0%;
  top: 0%;
  bottom: 0%;
  background: #FFFFFF;
  border: 1px solid #18A0FB;
  border-radius: 6px;
}

#logInText{
  width: 50%;
  margin-left: 25%;
  height: 100%;
  margin-top: -20%;
  position: absolute;
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 500;
  display: flex;
  align-items: center;
  text-align: center;
  color: #18A0FB;
}

#RegisterText{
  width: 50%;
  margin-left: 25%;
  height: 100%;
  margin-top: -20%;
  position: absolute;
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 500;
  display: flex;
  align-items: center;
  text-align: center;
  color: white;
}

#title {
  position: absolute;
  margin-left: 1%;
  margin-top: 1%;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 700;
  color: #000000;
}

#board_link {
  position: absolute;
  left: 60%;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 200%;
  color: #000000;
}

button {
  position: absolute;
  left: 0%;
  right: 0%;
  top: 0%;
  bottom: 0%;
}

#message_link {
  position: absolute;
  left: 71.5%;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 800;
  font-size: 200%;
  color: #000000;
}
</style>

