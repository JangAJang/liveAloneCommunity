<script>
import {ref} from "vue";
import router from "@/router";
import axios from "axios";
import Profile from "@/components/main/Profile.vue";
import MyData from "@/components/main/MyData.vue";

const msg = ref('')
let latitude = 1.00
let longitude = 1.00
export default {
  name: "KakaoMap",
  components: {MyData, Profile},
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

<template>
  <MyData />
  <div id="changeLocationBackground">
    <div id="map"></div>
    <button @click="saveLocation" id="saveLocationButton">
      위치 저장하기
    </button>
  </div>
  <Profile />
</template>

<style scoped>

#changeLocationBackground {
  position: absolute;
  width: 60%;
  margin-left: 20%;
  height: 70%;
  background: #feefca;
  border-radius: 15px;
}

#map {
  width: 90%;
  margin-left: 5%;
  height: 90%;
  margin-top: 5%;
  border-radius: 15px;
}

#saveLocationButton {
  background: #ffffff;
  position: absolute;
  border: 3px solid #000000;
  border-radius: 15px;
  width: 20%;
  margin-top: 5%;
  margin-left: 40%;
}
</style>
