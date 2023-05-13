<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

let result = []
const page = ref(0);
const size = ref(5);
const id = ref(1);

axios.get("/lan/post/of", {
    params: {id: id.value, page: page.value, size: size.value}
})
.then(res => {
    console.log(res)
    result = res.data.result.data.result
    console.log(result)
})
</script>

<template>
    <div id="profileInfo">
        <li v-for="post in result" class="mt-1">
            <div>{{post.title}}</div>
            <div>{{post.createdDate}}</div>
            <div>{{post.categoryName}}</div>
        </li>
    </div>
</template>

<style>
#profileInfo{
    margin-left: 83%;
    margin-top: 2%;
    width: 15%;
    background-color: #ffe87c;
    height: 100px;
    text-align: center;
}

#text{
    align-content: center;
}
</style>
