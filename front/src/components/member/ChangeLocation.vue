<script>
import {onMounted} from "vue";

const latitude = ref()
const longitude = ref()
const map = null;

onMounted( () => {
  axios.get('/lan/member/getLocation')
      .then(res => {
        latitude.value = res.data.result.data.latitude
        longitude.value = res.data.result.data.longitude
      })
  loadMap()
  loadMarker()
})

const loadMap = function () {
  const container = document.getElementById("map")
  const option = {
    center: new window.kakao.maps.LatLng(37.23369467112643, 127.18849753254402),
    level: 3,
  };
  map.value = new window.kakao.maps.Map(container, option);
}

const loadMarker = function () {
  const markerPosition = new window.kakao.maps.LatLng(37.23369467112643, 127.18849753254402);
  const marker = new window.kakao.maps.Marker({
    position: markerPosition
  });
  marker.setMap(this.map)
}

</script>
<template>
  <div>
    <h2>카카오 맵 보기</h2>
    <div id="map"></div>
  </div>
</template>
<style scoped>
#map {
  width: 80%;
  height: 400px;
}
</style>