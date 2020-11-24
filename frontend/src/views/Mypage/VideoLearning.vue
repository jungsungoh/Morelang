<template>
  <v-container style="margin-top: 15px;">
  <h1 >학습한 영상</h1>
  <br>
  <v-row >
    <v-col cols="4" v-for="(video, i) in videoLst" :key="i" @click="selectVideo(video.youtubeVideoid)">
        <v-hover v-slot="{ hover }">
     <v-card
    max-width="220"
    elevation="10"
  >
    <v-img
      class="white--text align-end"
      height="150px"
      :src="video.thumbnail"
    >
      <v-expand-transition>
          <div
            v-if="hover"
            class="d-flex transition-fast-in-fast-out red v-card--reveal  white--text"
            style="text-align: center; height: 50%;"
          >
           {{video.title}}
          </div>
      </v-expand-transition>
    </v-img>
  </v-card>
</v-hover>

    </v-col>
  </v-row>
      <v-pagination
      color="red"
       v-if="total" 
      v-model="page"
      :length="Number(total/9+1)"
      :total-visible="7"
    ></v-pagination>
  </v-container>
</template>

<script>
import axios from "@/plugins/axios";

export default {
  data() {
    return {
      page:1,
      total:1,
      videoLst: []
    }
  },
  methods:{
    selectVideo(vid) {
      let routeData = this.$router.resolve({name: "Video", params: {vid: vid}});
      window.open(routeData.href, '_blank');
    },
    getTotal(){
       axios
      .get("user/myvideosize")
      .then((res) => {
        console.log(res)
        if(res.data != null){
          var temp = res.data;
        if(temp>9){
          this.total=temp;
        }
        }
      })
    },
    getVideo(pnum){
        axios
      .get("user/myvideo",{params: {
          direction: "DESC",
          page: pnum,
          size :9
        }})
      .then((res) => {
        console.log(res)
        this.videoLst = res.data;
      })
    }
  },
  watch :{
    page : function(){
      this.getVideo(this.page);
    }
  },
  mounted() {
    this.getVideo(1);
    this.getTotal();
  },
}
</script>

<style scoped>
  .title {
    color: black;
  }
  .transparent {
   background-color: white!important;
   opacity: 0.65;
   border-color: transparent!important;
 }
 .v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: .9;
  position: absolute;
  width: 100%;
}
</style>