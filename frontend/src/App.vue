<template>
  <v-app>
    <v-snackbar :color="alertColor" :value="showAlert" centered multi-line>
        {{ AlertMessage }}
      </v-snackbar>
    <router-view class="entire"/>
      
    <!-- <Home /> -->
  </v-app>
</template>

<script>
// import HelloWorld from "./components/HelloWorld";
// import Home from "@/views/Home"
import axios from "@/plugins/axios";
import { mapState } from "vuex";
export default {
  name: "App",

  components: {
    // HelloWorld
    // Home
  },
  computed: mapState(["showAlert","AlertMessage", "alertColor"]),
  data: () => ({
    //
  }),
  created() {
    if (this.$store.state.accessToken) {
      if (this.$store.state.accessToken) {
        // 갱신 후에도 토큰이 유효하다면
        axios.defaults.headers.common.Authorization = `Bearer ${this.$store.state.accessToken}`;
      }
    }
  },

};
</script>

<style>
  @font-face {
    font-family: 'paybooc-Medium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/paybooc-Medium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
  @font-face {
    font-family: 'KyoboHand';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-04@1.0/KyoboHand.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
/* @font-face {
    font-family: 'MapoPeacefull';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoPeacefullA.woff') format('woff');
    font-weight: normal;
    font-style: normal;
} */
.entire {
  font-family: 'paybooc-Medium', sans-serif;
}
</style>
