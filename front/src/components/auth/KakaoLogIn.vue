<script setup lang="ts">

import {onMounted} from "vue";
import axios from "axios";
import router from "@/router";

onMounted(() => {
    const query = this.$router.currentRoute.value.query.code
    console.log(query)
    axios.get("http://localhost:8080/api/auth/kakao/callback", {
        params: {

        }
    })
        .then(res=> {
            console.log("결과 : " + res)
            router.push({name: 'main'})
        })
        .catch(e => alert(e))
})

</script>

<template>
</template>

<style>

</style>