<template>
  <div>
    <div id="map"></div>
    <div class="button-group">
      <button @click="saveLocation">
        위치 저장하기
      </button>
    </div>
  </div>
</template>

<script>

import {ref} from "vue";
import router from "@/router";
import axios from "axios";

const msg = ref('')
let latitude = 1.00
let longitude = 1.00
export default {
  name: "KakaoMap",
  data() {
    return {
      markers: [],
      infowindow: null,
    };
  },
  mounted() {
    axios.get("/lan/member/myLocation")
        .then(res => {
          latitude = res.data.result.data.latitude
          longitude = res.data.result.data.longitude
          this.createMap()
        })
  },
  methods: {
    createMap() {
      if (window.kakao && window.kakao.maps) {
        this.initMap();
      } else {
        const script = document.createElement("script");
        /* global kakao */
        script.onload = () => kakao.maps.load(this.initMap);
        script.src =
            "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=915cffed372954b7b44804ed422b9cf0";
        document.head.appendChild(script);
      }
    },
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(latitude, longitude),
        level: 5,
      };
      var map = new kakao.maps.Map(container, options);
      var marker = new kakao.maps.Marker({
        position: map.getCenter()
      });
      marker.setMap(map);
      kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
        const latlng = mouseEvent.latLng;

        // 마커 위치를 클릭한 위치로 옮깁니다
        marker.setPosition(latlng);

        let message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, ';
        message += '경도는 ' + latlng.getLng() + ' 입니다';
        latitude = latlng.getLat()
        longitude = latlng.getLng()
        msg.value = message;
      })
    },
    saveLocation() {
      console.log(latitude)
      console.log(longitude)
      axios.patch("/lan/member/edit/location", {
        latitude: latitude,
        longitude: longitude
      }).then(res => {
        alert("위치 수정에 성공했습니다.")
        router.push({name: 'myPage'})
      }).catch(e => {
        alert("다시 시도해주세요")
        router.push({name: 'editLocation'})
      })
    }

  },
};
</script>
<style scoped>
#map {
  width: 400px;
  height: 400px;
}

.button-group {
  margin: 10px 0px;
}

button {
  margin: 0 3px;
}
</style>
