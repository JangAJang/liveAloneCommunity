<script setup lang="ts">
</script>

<template>
  <header>
    <h1>Live Alone Network</h1>
    <nav>
      <ul>
        <li><a href="#">게시판</a></li>
        <li><a href="#">위치</a></li>
        <li><a href="#">정보</a></li>
        <li><a href="#">친구</a></li>
      </ul>
    </nav>
      <nav>
          <ul>
              <li>
                  <button>회원가입</button>
                  <button>로그인</button>
              </li>
          </ul>
      </nav>
  </header>
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

ul {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
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
  height: 120px;
  border-bottom: 1px solid #35495e;
}
</style>
