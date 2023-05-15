<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <h1>Live Alone Network</h1>
    <nav>
      <RouterLink to="/" style="margin-left: 2%">게시판</RouterLink>
      <RouterLink to="" style="margin-left: 2%">위치</RouterLink>
      <RouterLink to="" style="margin-left: 2%">정보</RouterLink>
      <RouterLink to="" style="margin-left: 2%">친구</RouterLink>
    </nav>
    <nav>
      <ul>
        <li>
          <RouterLink to="/auth/register">회원가입</RouterLink>
          <RouterLink to="/auth/logIn">로그인</RouterLink>
        </li>
      </ul>
    </nav>
  </header>
  <RouterView />
</template>

<style>
header {
  display: grid;
  grid-auto-flow: column;
  grid-template-columns: 1fr;
  align-items: center;
}
h1 {
  display: inline-block;
  vertical-align: middle;
  color: blue;
  margin: 0.5rem;
  padding: 0;
}

nav {
  display: inline-block;
  vertical-align: middle;
}

html {
  box-sizing: border-box;
}

header {
  width: 100%;
  height: 20%;
  border-bottom: 1px solid #35495e;
}
</style>
