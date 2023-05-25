<script setup lang="ts">
import axios from "axios";
import {onMounted} from "vue";

const props = defineProps({
  messageId: {
    type: [Number, String],
    required: true
  }
})

const getPostData = function () {
  axios.get("/lan/message", {params: {
    id: props.messageId
    }})
      .then(res=> console.log(res))
}

onMounted(() => getPostData())
</script>
<template>

</template>
<style>

</style>