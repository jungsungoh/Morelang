<template>
  <div class="home">
    <!-- <img alt="Vue logo" src="../assets/logo.png" /> -->
    <div style="width: 100%">
      <Navbar />

      <!-- 메뉴바 -->
      <!-- <div style="width: 50px; ">
            <v-icon>hamburger-menu</v-icon>
          </div> -->
    </div>

    <!-- 소개 영상 -->
    <div style="margin-bottom: 14%; background-color: white; width: 100%; height: 34%; display: block; margin-top: 10px; text-align: center">
      <video
        style="width: 100%"
        muted
        autoplay
        loop
        playsinline
        src="../assets/video/몰랑_ucc.mp4"
      ></video>
        <!-- <img width="80%" src="../../../resource/img/화면캡쳐/9_영상.png" alt=""> -->
        <!-- <h2 style="color: black;" class="mt-3">설명dfdfd</h2> -->
    </div>

    <!-- 언어 필터링 -->
    <div class="mt-16">
      <div class="mt-5" style="width: 20%; position: absolute; right: 20px">
        <v-select
          :items="language"
          item-text="ko"
          item-value="lang"
          label="language"
          v-model="selectlang"
        ></v-select>
      </div>
    </div>
    <!-- 추천 채널 -->
    <div style="margin-top: 80px; padding: 0; display: block">
      <div
        style="margin: 0 100px"
        data-aos="slide-right"
        aos-offset="300"
        data-aos-duration="1500"
      >
        <h3>
          <span
            style="display: block; font-size: 30px; font-weight: 550; font-family: 'SilkSerif'; color: #EF5350;"
            >Recommend</span
          >
          <strong
            style="
              font-size: 40px;
              font-weight: 900;
              font-family: 'Calibre', sans-serif;
              line-height: 0.5;
            "
            >CHANNEL</strong
          >
        </h3>
      </div>

      <!-- 채널 목록 -->
      <div
        data-scroll
        data-scroll-offset="100"
        data-scroll-speed="2"
        data-scroll-direction="horizontal"
        style="position: relative; height: 240px"
      >
        <!-- 채널 좌우바 -->
        <!-- <div
            style="transform: translateY(250%); position: absolute; padding: 0 50px; z-index: 5; justify-content: space-between; align-items: center; display: flex; width: 100%;"
          > -->
        <!-- @click="prev()" -->
        <!-- <div
              style="margin: 100px; color: darkgray;"
              class="swiper-button-prev"
              slot="button-prev"
            ></div> -->
        <!-- @click="next()" -->
        <!-- <div
              style="margin: 100px; color: darkgray; "
              class="swiper-button-next"
              slot="button-next"
            ></div> -->
        <!-- </div> -->
        <!-- 채널 동그라미 -->
        <!-- style="transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 18.15, 0, 0, 1);" -->
        <!-- data-scroll
            data-scroll-offset="100"
            data-scroll-speed="2"
            data-scroll-direction="horizontal" -->
        <!-- <div class="swiper-container"> -->
        <!-- <div style="margin: 0 auto; transform: translate3d(-2160px, 0px, 0px); transition: all 0ms ease 0s;"> -->
        <!-- style="margin: 20px 150px;" -->
        <swiper
          style="width: 90%; margin-top: 40px"
          class="swiper"
          :options="swiperOption"
          data-aos="slide-left"
          aos-offset="500"
          data-aos-duration="2500"
          ref="mySwiperRef"
        >
          <!-- <a href="/" style="width: 240px;"> -->
          <!-- style="animation: float 2s ease-in-out infinite; " -->
          <swiper-slide v-for="(item, index) in hotChannels" :key="index">
            <!-- <img src="../assets/img/channel0.jpg" alt="" /> -->
            <v-avatar
              class="Shine"
              style="width: 150px; height: 150px; display: inline-block"
              @click="
                $router.push({
                  name: 'Channel',
                  params: { id: item.youtubeVideoid },
                })
              "
            >
              <img :src="item.thumbnail" alt="" />
            </v-avatar>
            <v-row
              style="
                font-family: 'Calibre', sans-serif;
                font-weight: 600;
                font-size: 18px;
              "
              @click="
                $router.push({
                  name: 'Channel',
                  params: { id: item.youtubeVideoid },
                })
              "
              align="center"
              justify="center"
              class="mt-5"
              >{{ item.title }}</v-row
            >
          </swiper-slide>
          <!-- <div class="swiper-button-prev" slot="button-prev"></div>
            <div class="swiper-button-next" slot="button-next"></div> -->

          <!-- </a> -->
          <div class="swiper-pagination" slot="pagination"></div>
        </swiper>
        <!-- </div> -->
      </div>
    </div>

    <!-- 인기 영상 -->
    <div style="padding: 50px 0; display: block; background: black">
      <div
        style="margin: 0 100px"
        data-aos="slide-right"
        aos-offset="300"
        data-aos-duration="1000"
      >
        <h3>
          <span
            style="display: block; font-size: 30px; font-weight: 300; font-family: 'SilkSerif'; color: red; font-weight: bold;"
            >Hot</span
          >
          <strong
            style="
              font-size: 40px;
              font-weight: 900;
              font-family: 'Calibre', sans-serif;
              line-height: 0.5;
            "
            >VIDEO</strong
          >
        </h3>
      </div>
      <div class="example-3d">
        <swiper
          style="width: 90%"
          class="swiper"
          :options="swiperOption"
          data-aos="slide-left"
          aos-offset="500"
          data-aos-duration="2500"
        >
          <swiper-slide
            class="swiper-slide mr-16"
            v-for="(item, index) in hotVideos"
            :key="index"
          >
            <!-- Slide 1 -->
            <v-img
              class="Shine"
              :src="item.thumbnail"
              alt=""
              width="100%"
              @click="selectVideo(item.youtubeVideoid)"
            />
            <v-row
              @click="selectVideo(item.youtubeVideoid)"
              class="mt-5"
              align="center"
              justify="center"
              style="
                margin: 0;
                font-family: 'Calibre', sans-serif;
                font-weight: 600;
                font-size: 18px;
              "
              >{{ item.title }}</v-row
            >
          </swiper-slide>

          <!-- <swiper-slide class="swiper-slide">Slide 1</swiper-slide>
          <swiper-slide class="swiper-slide">Slide 1</swiper-slide>
          <swiper-slide class="swiper-slide">Slide 1</swiper-slide>
          <swiper-slide class="swiper-slide">Slide 1</swiper-slide> -->
          <div class="swiper-pagination" slot="pagination"></div>
        </swiper>
      </div>
    </div>
  </div>
</template>
<script>
// @ is 0an alias to /src

// import { Swiper, Navigation, Pagination, Scrollbar } from "swiper";

// import 'swiper/swiper-bundle.css'

// Swiper.use([Navigation, Pagination, Scrollbar]);

import Navbar from "@/components/Navbar";
import axios from "axios";
import { Swiper, SwiperSlide, directive } from "vue-awesome-swiper";
// import "swiper/css/swiper.css";
import "swiper/swiper-bundle.css";
// import "@/../public/css/home.scss";

// import { directive } from 'vue/types/umd';

// 상단 네브바 고정
// var nav = document.getElementsByClassName("navigation");
// window.onscroll = function sticky() {
//   if (window.pageYOffset > nav[0].offsetTop) {
//     nav[0].classList.add("nav");
//   } else {
//     nav[0].classList.remove("nav");
//   }
// };

export default {
  name: "Home",
  components: {
    Swiper,
    SwiperSlide,
    Navbar,
  },
  directives: {
    mySwiperRef: directive,
  },
  data() {
    return {
      hotChannels: null,
      hotVideos: null,
      userid: "",
      selectlang: { ko: "전체", lang: null },
      search_word: "",
      // logout: false,
      search: false,
      swiperVideo: {
        effect: "coverflow",
        grabCursor: true,
        centeredSlides: true,
        slidesPerView: "auto",
        coverflowEffect: {
          rotate: 50,
          stretch: 0,
          depth: 100,
          modifier: 1,
          slideShadows: true,
        },
        pagination: {
          el: ".swiper-pagination-video",
        },
      },
      swiperOption: {
        // navigation: {
        //   nextEl: ".swiper-button-next",
        //   prevEl: ".swiper-button-prev"
        // }
        slidesPerView: 4,
        spaceBetween: 10,
        // centeredSlides: true,
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
        navigation: {
          nextEl: ".swiper-button-next",
          prevEl: ".swiper-button-prev",
        },
      },
      language: [
        { ko: "전체", lang: null },
        { ko: "영어", lang: "en" },
        { ko: "일본어", lang: "jp" },
        { ko: "중국어", lang: "cn" },
        { ko: "프랑스어", lang: "fr" },
        { ko: "스페인어", lang: "es" },
        { ko: "독일어", lang: "de" },
        { ko: "이탈리아어", lang: "it" },
        { ko: "포르투갈어", lang: "pt" },
        { ko: "러시아어", lang: "ru" }
      ]
    };
  },
  mounted() {
    this.getHotChannels();
    this.getHotVideos();
  },
  watch: {
    selectlang: function () {
      // console.log(this.selectlang);
      this.getHotChannels(this.selectlang);
      this.getHotVideos(this.selectlang);
    },
  },
  methods: {
    selectVideo(vid) {
      let routeData = this.$router.resolve({
        name: "Video",
        params: { vid: vid },
      });
      window.open(routeData.href, "_blank");
    },

    prev() {
      this.$refs.mySwiperRef.$swiper.slidePrev();
    },
    next() {
      this.$refs.mySwiperRef.$swiper.slideNext();
    },

    async getHotChannels(lang) {
      await axios
        .get("https://morelang.gq/api/newuser/recommend-list", {
          params: {
            country: lang,
          },
        })
        .then((res) => {
          console.log("핫채널들정보");
          this.hotChannels = res.data;
          console.log(this.hotChannels);
        });
    },
    async getHotVideos(lang) {
      await axios
        .get("https://morelang.gq/api/newuser/highcount", {
          params: {
            direction: "DESC",
            page: 1,
            size: 10,
            defaultlang: lang,
          },
        })
        .then((res) => {
          console.log("핫비디오들정보");
          console.log(res.data.content);
          this.hotVideos = res.data.content;
          console.log(this.hotVideos);
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.swiper-container {
  width: 100%;
  height: 100%;
}
.swiper-slide {
  width: 80%;
  text-align: center;
  align-items: center;
  justify-content: left;
  /* display: flex; */
}
/* .swiper-slide {
  width: 60%;
}
.swiper-slide:nth-child(2n) {
  width: 40%;
}
.swiper-slide:nth-child(3n) {
  width: 20%;
} */

.example-3d {
  width: 100%;
  height: 400px;
  padding-top: 20px;
  padding-bottom: 50px;
}

.swiper-video {
  height: 100%;
  width: 100%;
}

.swiper-slide-video {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 300px;
  height: 300px;
  text-align: center;
  font-weight: bold;
}

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

// .videoTitle {
//   /* font-weight: 300; */
//   // display: inline-block;
//   margin: 0;
//   width: 300px;
//   white-space: nowrap;
//   overflow: hidden;
//   text-overflow: ellipsis;

//   white-space: normal;
//   line-height: 1.2;
//   height: 2.4em;
//   text-align: center;
//   word-wrap: break-word;
//   // display: -webkit-box;
//   -webkit-line-clamp: 2;
//   -webkit-box-orient: vertical;
// }

// .nav {
//   position: fixed;
//   background: beige;
//   padding: 30px 0;
// }

// .navigation {
//   width: 100%;
//   top: 0;
// }

</style>