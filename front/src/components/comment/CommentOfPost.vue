<script setup lang="ts">
import {onMounted} from "vue";
import axios from "axios";

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true
  }
})

onMounted(() => {
  axios.get("/lan/comment/post", {
    params: {
      postId: props.postId,
      page: 1,
      size: 10
    }
  }).then(res => {
    console.log("댓글 조회 완료")
    console.log(res)
  })
})
</script>

<template>

</template>

<style>

</style>