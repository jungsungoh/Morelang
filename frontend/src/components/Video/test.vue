<template>
  <!-- <h3 class="title ml-3 px-5">스크랩한 단어</h3> -->
  <!-- 언어 필터링 -->
  <div>
  <v-container>
    <v-container style="text-align:center;">
      <v-row no-gutters>
        <!-- 정렬 맞추기 위해 왼쪽 빈칸 사용 -->
        <!-- <v-col cols="12" sm="1"></v-col> -->
        <!-- @click="onLanguage(category.name, category.key)" -->
        <!-- <v-col v-for="(lang, i) in langLst" :key="i" cols="12" sm="1">
            <v-card
              class="pa-2 categoryCard"
              tile
            >
              <p class="mb-0 categoryTag">{{lang}}</p>
            </v-card>
          </v-col> -->
        <v-col v-for="(lang, i) in langLst" :key="i" sm="1">
          <v-chip color="green" text-color="white" @click="wordList(lang)">
            {{ language[lang] }}
          </v-chip>
        </v-col>
      </v-row>
    </v-container>

    <v-row v-if="wordlang" class="px-5 mt-5 mx-3" justify="center">
      <v-container>
        <!-- 언어별 단어장 -->
        <!-- expander 사용예정 / expand => 뜻/예문 보이기 -->
        <!-- <h2 class="mb-3">영어</h2>
        <v-row no-gutters style="text-align:center; width:70%;">
          <v-col v-for="i in 6" :key="i" cols="12" sm="6">
            <v-card class="pa-2 selectLang" outlined tile>
              <p class="my-auto">{{ i }}</p>
            </v-card>
          </v-col>
        </v-row> -->
        <h2 class="mt-5 mb-3">{{ language[selectlang] }}</h2>
        <div style="display: table; width: 100%;">
          <div style="display: table-cell; width: 40%">
            <h4>학습중</h4>
            <!-- <v-row no-gutters style="text-align:center; width:70%;">
              <v-col v-for="(word, i) in wlist" :key="i" cols="12" md="10"> -->
            <!-- <div v-if="!word.isLearn"> -->
            <v-expansion-panels accordion  style=" width:70%;">
              <v-expansion-panel
                v-for="(word, i) in wlist"
                :key="i"
                cols="12"
                md="10"
              >
                <draggable class="list-group" :list="wlist" group="people" @change="logtoCompleted(word)">
                  <v-expansion-panel-header>
                    {{ word.eachVoca }}
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    {{ word.eachMean }}
                  </v-expansion-panel-content>
                </draggable>
              </v-expansion-panel>
            </v-expansion-panels>

            <!-- <transition name="mode-fade" mode="out-in"> -->
            <!-- key="show" -->
            <!-- <v-card
                    v-if="show"
                    @click="show = false"
                    class="pa-2 selectLang"
                    outlined
                    tile
                    style="position:relative"
                  > -->
            <!-- append-icon="mdi-lead-pencil" label="복습" -->
            <!-- <v-checkbox
                      style="width: 27%;
                        position: absolute;
                        bottom: 7px;
                left: 5px;"
                      color="success"
                      value="success"
                      hide-details
                    ></v-checkbox> -->

            <!-- <v-icon small left>mdi-lead-pencil</v-icon>
              복습 -->

            <!-- <p class="my-auto">{{ word.eachVoca }}</p>
                  </v-card> -->
            <!-- </transition> -->
            <!-- <transition name="fade" mode="out-in"> -->
            <!-- key="show" -->
            <!-- <v-card
                    v-else
                    @click="show = true"
                    class="pa-2 selectLang"
                    outlined
                    tile
                    style="position:relative"
                  >
                    append-icon="mdi-lead-pencil" label="복습" -->
            <!-- <v-checkbox
                      style="width: 27%;
                        position: absolute;
                        bottom: 7px;
                left: 5px;"
                      color="success"
                      value="success"
                      hide-details
                    ></v-checkbox> -->

            <!-- <v-icon small left>mdi-lead-pencil</v-icon>
              복습 -->

            <!-- <p class="my-auto">{{ word.eachMean }}</p>
                  </v-card>
                </transition> -->
            <!-- </div> -->
            <!-- </v-col>
            </v-row> -->
          </div>

          <div style="display: table-cell; width: 40%;">
            <h4>학습완료</h4>
            <v-expansion-panels accordion style=" width:70%;">
                <v-expansion-panel
                  v-for="(word, i) in wlist2"
                  :key="i"
                  cols="12"
                  md="10"
                >
              <draggable class="list-group" :list="wlist2" group="people">
                  <v-expansion-panel-header>
                    {{ word.eachVoca }}
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    {{ word.eachMean }}
                  </v-expansion-panel-content>
              </draggable>
                </v-expansion-panel>
            </v-expansion-panels>

            <!-- <v-row no-gutters style="text-align:center; width:70%;"> -->
              <!-- <p>word</p> -->
              <!-- <v-col v-for="(word, i) in wlist2" :key="i" cols="12" md="10"> -->
                <!-- v-if="word.isLearn" -->
                <!-- <v-card
                  class="pa-2 selectLang"
                  outlined
                  tile
                  style="position:relative"
                > -->
                  <!-- append-icon="mdi-lead-pencil" label="복습" -->
                  <!-- <v-checkbox
                    style="width: 27%;
                position: absolute;
                bottom: 7px;
                left: 5px;"
                    color="success"
                    value="success"
                    hide-details
                  ></v-checkbox> -->

                  <!-- <v-icon small left>mdi-lead-pencil</v-icon>
              복습 -->

                  <!-- <p class="my-auto">{{ word.eachVoca }}</p>
                </v-card>
              </v-col> -->
            <!-- </v-row> -->
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
import draggable from "vuedraggable";

export default {
  name: "two-lists",
  display: "Two Lists",
  order: 1,
  components: {
    draggable
  },
  data() {
    return {
      show: true,
      wordlist: [],
      selectlang: "",
      wordlang: false,
      langLst: [],
      language: {
        en: "영어",
        jp: "일본어",
        cn: "중국어",
        vi: "베트남어",
        id: "인도네시아어",
        ar: "아랍어",
        bn: "뱅갈어",
        de: "독일어",
        es: "스페인어",
        fr: "프랑스어",
        hi: "힌디어",
        it: "이탈리아어",
        ms: "말레이시아어",
        nl: "네덜란드어",
        pt: "포르투갈어",
        ru: "러시아",
        th: "태국어",
        tr: "터키어"
      }
    };
  },
  computed: {
    wlist: function() {
      return this.wordlist.filter(function(w) {
        return !w.isLearn;
      });
    },
    wlist2: function() {
      return this.wordlist.filter(function(w) {
        return w.isLearn;
      });
    }
  },
  methods: {
    clone: function(el) {
      console.log('clone함수');
      return {
        name: el.name + " cloned"
      };
    },
    logtoProgress: function(word) {
      console.log('logto Progess함수');
      word.isLearn = false;
      console.log(word);
    },
    logtoCompleted: function(word) {
      console.log('logtoCompleted함수');
      word.isLearn = true;
      console.log(word);
    },
    wordList(lang) {
      if (lang == this.selectlang) {
        console.log("여기");
        this.wordlang = !this.wordlang;
      } else {
        this.wordlang = true;
      }
      this.selectlang = lang;
      axios
        .get(`/user/myvoca?country=${lang}&direction=ASC&page=0&size=10`)
        .then(res => {
          console.log(res);
          this.wordlist = res.data.content;
          console.log("언어별 단어장");
        });
    }
  },
  mounted() {
      axios.get("/user/myvoca-country").then(res => {
        console.log("단어장에 저장된 국가들");
        console.log(res);
        this.langLst = res.data;
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
</style>