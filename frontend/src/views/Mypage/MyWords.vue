<template>
  <!-- <h3 class="title ml-3 px-5">스크랩한 단어</h3> -->
  <!-- 언어 필터링 -->
  <div>
    <v-container>
      <v-container style="text-align: center">
        <v-row no-gutters>
          <swiper style="width: 95%" :options="swiperOption">
            <swiper-slide v-for="(lang, i) in langLst" :key="i" cols="2">
              <v-btn
                miduam
                width="120"
                class="truncate"
                outlined
                text-color="white"
                @click="wordList(lang, i)"
                id="btnlang"
                :style="{ backgroundColor: index[i] ? 'white' : 'black' , color: index[i] ? 'black' : 'white'}"
              >
                {{ lang.substring(0, 8) }}
              </v-btn>
            </swiper-slide>
          </swiper>
        </v-row>
        <hr style="width: 100%" class="mt-4" />
      </v-container>

      <v-row v-if="wordlang" class="mx-3" justify="center">
        <v-container style="width: 50%; display: inline-block">
          <h2 class="mb-3">학습중</h2>
          <div style="display: table; width: 100%">
            <div style="display: table-cell; width: 40%">
              <v-expansion-panels
                :accordion="panel"
                style="width: 100%"
                v-model="panel"
              >
                <v-expansion-panel
                  v-for="(word, i) in wlist"
                  :key="i"
                  cols="12"
                  md="10"
                >
                  <v-expansion-panel-header>
                    {{ word.eachVoca }}
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    {{ word.eachMean }}
                    <v-icon
                      medium
                      style="float: right; color: red"
                      @click="delWord(word.vocaId, word.country)"
                      >mdi-close</v-icon
                    >
                    <v-icon
                      medium
                      style="float: right; color: green"
                      @click="Check(word.vocaId, word.country)"
                      >mdi-check</v-icon
                    >
                  </v-expansion-panel-content>
                </v-expansion-panel>
              </v-expansion-panels>
            </div>
          </div>
        </v-container>
        <v-container style="width: 50%; display: inline-block">
          <h2 class="mb-3">학습완료</h2>
          <div style="display: table; width: 100%">
            <div style="display: table-cell; width: 40%">
              <v-expansion-panels :accordion="panel" style="width: 100%">
                <v-expansion-panel
                  v-for="(word, i) in wlist2"
                  :key="i"
                  cols="12"
                  md="10"
                >
                  <v-expansion-panel-header>
                    {{ word.eachVoca }}
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    {{ word.eachMean }}
                    <v-icon
                      medium
                      style="float: right; color: red"
                      @click="delWord(word.vocaId, word.country)"
                      >mdi-close</v-icon
                    >
                    <v-icon
                      medium
                      style="float: right; color: gray"
                      @click="Check(word.vocaId, word.country)"
                    >
                      mdi-replay
                    </v-icon>
                  </v-expansion-panel-content>
                </v-expansion-panel>
              </v-expansion-panels>
            </div>
          </div>
        </v-container>
      </v-row>
    </v-container>
    <v-row v-if="wordlang" class="px-5 mt-5 mx-3" justify="center">
      <!-- 
    <rawDisplayer class="col-3" :value="list1" title="List 1" />

    <rawDisplayer class="col-3" :value="list2" title="List 2" /> -->
    </v-row>
  </div>
</template>
<script>
import axios from "@/plugins/axios";
import "swiper/swiper-bundle.css";
import { Swiper, SwiperSlide } from "vue-awesome-swiper";
// import draggable from "vuedraggable";

export default {
  name: "two-lists",
  display: "Two Lists",
  order: 1,
  components: {
    // draggable
    Swiper,
    SwiperSlide,
  },
  data() {
    return {
      panel: false,
      swiperOption: {
        slidesPerView: 4,
        spaceBetween: 0,
      },
      index: [],
      show: true,
      wordlist: [],
      selectlang: "",
      wordlang: false,
      langLst: [],
      wlist: [],
      wlist2: [],
    };
  },
  computed: {
    // wlist: function () {
    //   return this.wordlist.filter(function (w) {
    //     return !w.isLearn;
    //   });
    // },
    // wlist2: function () {
    //   return this.wordlist.filter(function (w) {
    //     return w.isLearn;
    //   });
    // },
  },
  methods: {
    async Check(id, country) {
      await axios.put(`/user/islearn?vocaId=${id}`);
      this.Next(country);
    },
    async Next(country) {
      this.panel = false;
      await axios
        .get(`/user/myvoca?country=${country}&direction=ASC&page=0&size=1000`)
        .then((res) => {
          console.log(res);
          this.wordlist = res.data.content;
          this.wlist = [];
          this.wlist2 = [];
          for (var i = 0; i < this.wordlist.length; i++) {
            if (this.wordlist[i].isLearn) {
              this.wlist2.push(this.wordlist[i]);
              console.log("돌아라");
              console.log(i);
            } else {
              this.wlist.push(this.wordlist[i]);
            }
          }
          // console.log("언어별 단어장");
        });
    },
    async delWord(wid, country) {
      // var flag = 0;
      await axios.delete(`/user/delete-voca?vocaId=${wid}`);
      this.Next(country);
    },
    wordList(lang, idx) {
      this.wlist = [];
      this.wlist2 = [];
      // var btn = document.getElementById("btnlang")
      for (var i = 0; i < this.index.length; i++) {
        if (i == idx) {
          this.index[i] = !this.index[i];
        } else {
          this.index[i] = false;
        }
      }
      if (lang == this.selectlang) {
        console.log("여기");
        this.wordlang = !this.wordlang;
      } else {
        this.wordlang = true;
      }
      this.selectlang = lang;
      axios
        .get(`/user/myvoca?country=${lang}&direction=ASC&page=0&size=10`)
        .then((res) => {
          console.log(res);
          this.wordlist = res.data.content;
          console.log("언어별 단어장");
          for (var i = 0; i < this.wordlist.length; i++) {
            if (this.wordlist[i].isLearn) {
              this.wlist2.push(this.wordlist[i]);
            } else {
              this.wlist.push(this.wordlist[i]);
            }
          }
        });
      this.panel = false;
    },
  },
  mounted() {
    axios.get("/user/myvoca-country").then((res) => {
      console.log("단어장에 저장된 국가들");
      console.log(res);
      this.langLst = res.data;
      console.log("길이");
      console.log(res.data.length);
      for (var i = 0; i < res.data.length; i++) {
        this.index.push(false);
      }
      console.log(this.index);
    });
  },
};
</script>

<style scoped>
.title {
  color: black;
}

.mode-fade-enter-active,
.mode-fade-leave-active {
  transition: opacity 0.8s ease;
}

.mode-fade-enter-from,
.mode-fade-leave-to {
  opacity: 0;
}

/* .fade-enter-active {
  transition: all 0.3s ease;
}
.fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}
.fade-enter, .fade-leave-to
.slide-fade-leave-active below version 2.1.8 */
/* transform: translateX(10px);
  opacity: 0;
} */

.truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>