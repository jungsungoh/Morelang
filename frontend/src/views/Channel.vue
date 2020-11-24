<template>
  <div class="channelDetail">
    <Navbar />
    <div>
      <!-- 'url(' + picture + ')' -->
      <!-- v-bind:style="{
          'background-image': 'url(channelInfo.imgUrl)'
        }" -->
      <div
        class="backImg"
        :style="{
          backgroundImage: `url(${channelInfo.imgUrl})`
        }"
        style="
        height: 380px;
        display: flex;
        justify-content: center;
        align-items: center;
      "
      >
        <!-- {{ channelInfo.imgUrl }} -->
        <!-- <h2 class="intro">{{ channelInfo.title }}</h2> -->
        <div class="img-cover">
          <div class="title" style="margin-top:130px;">
          <v-avatar
              style="margin-left: 260px;width: 150px; height: 150px; display: inline-block;"
            >
              <img :src="channelInfo.imgUrl" alt="" />
            </v-avatar>
            <div
              class="title mx-6"
              style="display: inline-block;"
            >
              <b><span style="font-size: 50px">{{ channelInfo.title }}</span></b>
            </div>
            </div>
          <div class="channelInfoBox" >
            <div class="channelInfo" style="background-color: black;">
          <div style="width: 50%">
            
          </div>
          <div
            style="width: 100%; padding: 20px 0; display: flex; flex-flow: row wrap; justify-content: space-between;"
          >
            <div v-for="(video, i) in videolst" :key="i">
              
              <div
                @click="selectVideo(video.id)"
                class="col video"
                style="width: clac(25% - 60px); margin: auto; display: inline-block; margin-bottom: 20px;"
              >
                <!-- @error="
                      $event.target.src =
                        video.imgUrl.substring(0, 35) + 'mqdefault.jpg'
                    " -->
                <div style="margin-bottom: 15px;">
                  <v-img class="Shine" :src="video.imgUrl" alt="" width="280" @error="aaa" />
                </div>
                <!-- id="video_img" -->
                <!-- <h3 v-if="video.title.length > 15" style="">
                  {{ video.title.substring(0, 15) }} ...
                </h3> -->
                <h4 class="videoTitle">
                  {{ video.title }}
                </h4>
                <div>
                  <v-btn
                    v-for="(lang, j) in video.captions.slice(0, 3)"
                    :key="j"
                    rounded
                    color="primary"
                    dark
                    x-small
                    style="margin: 0 1px;"
                    class="mr-1"
                  >
                    <div v-if="lang.lang_translated.indexOf('(') > -1">
                      {{
                        lang.lang_translated.substring(
                          0,
                          lang.lang_translated.indexOf("(")
                        )
                      }}
                    </div>
                    <div v-else-if="lang.lang_translated.length > 10">
                      {{ lang.lang_translated.substring(0, 10) }}
                    </div>
                    <div v-else>
                      {{ lang.lang_translated }}
                    </div>
                    <!-- <div v-if="j > 5 && j==video.captions.length - 1">
                + {{ video.captions.length - 5 }}
              </div> -->
                  </v-btn>
                  <div style="display: inline-block; float: right;"
                    v-if="video.captions.length > 3"
                    @mouseover="hover = true"
                    @mouseleave="hover = false"
                  >
                    + {{ video.captions.length - 3 }}
                  </div>
                  <!-- <span v-if="hover"> 
            <div v-for="(lang, j) in video.captions"
              :key="j">
              {{ lang.lang_translated }}

            </div> -->
                  <!-- </span> -->
                </div>
              </div>
            </div>
          </div>

          <!-- 무한스크롤 -->
          <infinite-loading @infinite="infiniteHandler" spinner="bubble">
            <div
              slot="no-more"
              style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px"
            >
              No More
            </div>
            <div
              slot="no-results"
              style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px"
            >
              No Results
            </div>
          </infinite-loading>
        </div>
            </div>
        </div>
      </div>
      
        
      </div>
  </div>
</template>

<script>
import axios from "@/plugins/axios";
import Navbar from "@/components/Navbar";
import InfiniteLoading from "vue-infinite-loading";
// import VueRouter from "vue-router"

// video.imgUrl

// function altImg(imgerror) {
//   console.log("이미지에러다!")
// }
export default {
  data() {
    return {
      channelId: "",
      channelInfo: [],
      videolst: [],
      token: "",
      end: false,
      newimg: "",
      img: ""
    };
  },
  components: {
    Navbar,
    InfiniteLoading
  },
  mounted() {
    // this.channelId = this.$route.params.id;
    var index = window.location.href.indexOf("channel");
    var length = window.location.href.length;
    this.channelId = window.location.href.substring(index + 8, length);
    // if(this.channelId == null) {
    //   alert("채널 아이디를 입력해주세요.")
    //   // $router.go(-1)
    // }
    console.log("여기여기");
    console.log(this.channelId);

    axios.get(`/newuser/channel?id=${this.channelId}`).then(res => {
      console.log(res);
      this.channelInfo = res.data;
      axios
        .get(`/newuser/playlist?id=${this.channelInfo.uploads}`)
        .then(res => {
          console.log(res);
          this.videolst = res.data.items;
          this.token = res.data.token;
          // console.log("videolst" + this.videolst)
        });
    });
  },
  methods: {
    selectVideo(vid) {
      let routeData = this.$router.resolve({name: "Video", params: {vid: vid}});
      window.open(routeData.href, '_blank');

      // this.$router.push({ name: "Video", params: { vid: vid } });
    },
    aaa(e) {
      console.log(e);
      console.log("aldfjadl;kfja");
    },
    // imgUrlAlt(event){
    //   console.log("이미지에러")
    //   // console.log(img)
    //   var video_img = document.getElementById('video_img')
    //   event.target.src = video_img.replace("maxres", "mq")
    //   // this.newimg = img.replace("maxres", "mq")
    //   // event.target.src = "this.newimg"
    // },
    infiniteHandler($state) {
      setTimeout(() => {
        axios
          .get(
            `/newuser/playlist?id=${this.channelInfo.uploads}&token=${this.token}`
          )
          .then(res => {
            // console.log("infinitehandler");
            // console.log(res);
            this.videolst = this.videolst.concat(res.data.items);
            this.token = res.data.token;
            if (res.data.items.length != 50) {
              this.end = true;
            }
            // console.log("videolst" + this.videolst)
          });
        $state.loaded();
        if (this.end) {
          $state.complete();
        }
      }, 1000);
    }
  }
};
</script>

<style scoped>
@import url(//fonts.googleapis.com/css?family=Vibur);
.title {
  /* text-align: center;  */
  user-select: none;
  margin-top: 10px;
}

.title b{
  font: 300 4vh "KyoboHand", sans-serif;
  color: #fdd;
  text-shadow: 0 -40px 100px, 0 0 2px, 0 0 1em #ff0000, 0 0 0.5em #ff0000, 0 0 0.1em #ff0000, 0 10px 3px #000;
}
.title b span{
  animation: blink linear infinite 2s;
}
.title b span:nth-of-type(2){
  animation: blink linear infinite 3s;
}
.backImg {
  position: relative;
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
  height: 100vh;
  /* opacity: 0.3; */
}
.img-cover {
  position: absolute;
  height: 100%;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 1;
}

.intro {
  font-size: 50px;
  font-family: "Calibre", sans-serif;
  position: absolute;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  z-index: 2;
  text-align: center;
}

.channelInfoBox {
  /* margin: 0 150px; */
  z-index: 3;
  position: absolute;
  min-height: 500px;
  width: 100%;
  top: 325px;
  padding: 0 17%;
  border-radius: 4px;
}
.channelInfo {
  overflow: hidden;
  width: 100%;
  background: white;
  /* white; */
  padding: 2%;
}
.videoTitle {
  /* font-weight: 300; */
  display: inline-block;
  width: 290px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  white-space: normal;
  line-height: 1.2;
  height: 2.4em;
  text-align: left;
  word-wrap: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.video:hover {
  cursor: pointer;
}
/* .channelDetail {
  font-family: 'KyoboHand', sans-serif;
} */


/* Shine */
.Shine {
  position: relative;
}
.Shine::before {
  position: absolute;
  top: 0;
  left: -75%;
  z-index: 2;
  display: block;
  content: "";
  width: 50%;
  height: 100%;
  background: -webkit-linear-gradient(
    left,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.3) 100%
  );
  background: linear-gradient(
    to right,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.3) 100%
  );
  -webkit-transform: skewX(-25deg);
  transform: skewX(-25deg);
}
.Shine:hover::before {
  -webkit-animation: shine 0.75s;
  animation: shine 0.75s;
}
@-webkit-keyframes shine {
  100% {
    left: 125%;
  }
}
@keyframes shine {
  100% {
    left: 125%;
  }
}
</style>
