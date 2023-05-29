<script setup lang="ts">
import {onMounted} from "vue";
import axios from "axios";
import router from "@/router";

onMounted(()=> {
  if (confirm('회원을 삭제하시겠습니까?')) {
    axios
        .delete('/lan/member/delete')
        .then(() => {
          alert('회원을 탈퇴했습니다. 그동한 L.A.N과 함꼐 해주셔서 감사합니다.')
          router.push({ name: 'logIn' })
        })
        .catch((reason) => alert(reason.response.data.result.failMessage))

  }
  router.push({name: 'myPage'})
})
</script>
<template>

</template>
<style>

</style>