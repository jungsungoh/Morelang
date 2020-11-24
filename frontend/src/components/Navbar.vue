<template>
  <!-- 상단 -->
  <div
    class="Navbar"
    style="
      padding-top: 0px;
      top: 0;
      left: 0;
      z-index: 40;
      width: 100%;
      position: fixed;
    "
  >
    <div class="navigation" id="header">
      <div style="padding: 0">
        <v-card-title
          style="
            padding: 0px 30px;
            margin-top: 6px;
            margin-bottom: 5px;
            background-color: black;
          "
        >
          <router-link to="/">
            <div class="logo"><b>M<span>OR</span>EL<span>AN</span>G</b></div>
            <!-- <img style="height:63px;" src="@/assets/img/logo.png" alt=""> -->
            <!-- <h2 class="logo px-5 py-1" style="display:inline-block">
              Morelang
            </h2> -->
          </router-link>
          <div class="mx-auto mt-2">
            <v-text-field
              style="display: inline-block"
              autocomplete="off"
              v-model="keyword"
              @keyup.enter="onSearch(keyword)"
              height="37"
              hide-details=""
              placeholder="키워드 검색"
              outlined
              clearable
              append-icon="mdi-magnify"
              color="white"
              @click:append="onSearch(keyword)"
            />
            <div class="mx-1" style="display: inline-block">
              <v-btn
                @click="beforeTrans"
                class="py-4 mb-2"
                x-small
                color="white"
                dark
                outlined
                style="margin-bottom: 0 !important"
              >
                <v-icon>mdi-google-translate</v-icon>
              </v-btn>
            </div>
          </div>

          <v-col cols="8" sm="3" lg="2" class="text-center py-0">
            <v-row no-gutters v-if="!$store.state.nickname">
              <v-col>
                <v-btn
                  style="float: right"
                  text
                  @click="changeRoute('Login')"
                  class=""
                  >로그인</v-btn
                >
              </v-col>
            </v-row>
            <v-menu
              open-on-hover
              offset-y
              v-if="$store.state.nickname"
              no-gutters
            >
              <template v-slot:activator="{ on, attrs }">
                <v-card color="transparent" v-bind="attrs" v-on="on" flat>
                  <v-row no-gutters>
                    <v-col>
                      <v-icon style="float: right"> mdi-menu </v-icon>
                    </v-col>
                  </v-row>
                </v-card>
              </template>
              <v-list>
                <v-list-item-group color="primary">
                  <v-list-item
                    v-for="(item, i) in items"
                    :key="i"
                    @click="userMenu(i)"
                  >
                    <v-list-item-title v-text="item"></v-list-item-title>
                  </v-list-item>
                </v-list-item-group>
              </v-list>
            </v-menu>
          </v-col>
          <!-- <p class="navBtn mr-2 my-auto">로그아웃</p> -->
          <!-- <v-icon size="25" class="mr-3">mdi-logout-variant</v-icon> -->
        </v-card-title>
      </div>

      <!-- 검색어 없이 번역버튼 눌렀을 경우 스낵바 -->
      <v-container
        fluid
        class="text-center"
        style="height: 1px; padding-bottom: 0px"
      >
        <!-- <v-row
        class="flex"
        justify="space-between"
      > -->
        <v-col cols="12" class="mt-2" style="height: 0px !important">
          <v-tooltip v-model="errSnackbar" top>
            <template v-slot:activator="{ on, attrs }">
              <v-btn class="snackbar" icon v-bind="attrs" v-on="on"> </v-btn>
            </template>
            <v-icon color="white" class="mr-1">mdi-alert-circle-outline</v-icon>
            <span>검색어를 입력해주세요</span>
          </v-tooltip>
        </v-col>
        <!-- </v-row> -->
      </v-container>

      <!-- 번역 언어 모달 -->
      <v-dialog
        class="dialog"
        v-model="transDialog"
        scrollable
        max-width="300px"
      >
        <v-card>
          <v-toolbar color="black" dark>
            <v-toolbar-title class="toolbarTitle">
              <p
                class="my-auto title"
                style="margin-left: 83px; text-align: center !important"
              >
                <b
                  ><span style="font-size: 22px" class="trans"
                    >검색어 번역</span
                  ></b
                >
              </p>
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon> </v-btn>
          </v-toolbar>
          <v-card-text style="height: 300px">
            <v-card class="mx-auto mt-5" max-width="500" flat>
              <v-row justify="center">
                <v-container class="language" style="text-align: center">
                  <v-row no-gutters>
                    <!-- 언어 선택 -->
                    <v-col
                      v-for="(kr, en) in language"
                      :key="en"
                      cols="12"
                      sm="12"
                    >
                      <v-card
                        @click="onTranslate(en)"
                        class="pa-2 selectLang"
                        outlined
                        tile
                      >
                        <p class="my-auto">{{ kr }}</p>
                      </v-card>
                    </v-col>
                  </v-row>
                </v-container>
              </v-row>
            </v-card>
          </v-card-text>
          <v-divider class="mb-0"></v-divider>
          <v-card-actions class="mx-auto">
            <v-btn color="red" @click="onSearch(keyword)">검색</v-btn>
            <!-- <v-btn color="black" text @click="transDialog = false">닫기</v-btn> -->
            <!-- <v-btn
              color="black"
              text
              @click="transDialog = false"
              >닫기</v-btn
            > -->
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>

<script scoped>
import "@/../public/css/Navbar.scss";
import axios from "@/plugins/axios";
import { mapState } from "vuex";
// import store from "@/../src/store/index.js";

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
  updated() {
    if (this.errSnackbar && this.keyword) {
      this.errSnackbar = false;
    }
  },
  data() {
    return {
      userid: null,
      keyword: "",
      transDialog: false,
      errSnackbar: false,
      timeout: 1500,
      errtext: "검색어를 입력해주세요",
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
        tr: "터키어",
      },
      items: ["Mypage", "Logout"],
    };
  },
  computed: mapState(["userThumbnail", "nickname", "accessToken"]),
  methods: {
    onSearch(word) {
      if (word.indexOf("v=") > -1) {
        console.log("야아아아");
        var tmp = word.indexOf("v=");
        var id = word.slice(tmp + 2, word.length);
        console.log(tmp);
        console.log(id);
        this.$router.push({ name: "Video", params: { vid: id } });
      } else {
        this.$router.push({ name: "Search", params: { target: word } });
        this.keyword = word;
      }
    },
    beforeTrans() {
      if (this.keyword) {
        this.transDialog = true;
      } else {
        this.errSnackbar = true;
      }
    },
    onTranslate(lang) {
      axios
        .get(
          `/newuser/translate?query=${this.keyword}&src_lang=kr&target_lang=${lang}`
        )
        .then((res) => {
          this.keyword = res.data;
        });
    },
    // gotoMypage() {
    //   // path: 'mypage/${this.$store.state.member.userid}`'
    //   console.log("userid")
    //   console.log(this.userid)
    //   this.$router.push({ name: 'Mypage', params: { userid: this.userid}})
    // },
    userMenu(idx) {
      if (idx === 0) {
        // console.log("userid = " + this.$store.state.userid)
        this.$router.push({
          name: "Mypage",
          params: { userid: this.$store.state.userid },
        });
        // } else if (idx === 1) {
        //   this.$router.push({name: "Home"});
        // }
      } else {
        this.$store.dispatch("LOGOUT");
      }
    },
    changeRoute(name) {
      // console.log(this.$route.name === name);
      if (this.$route.name === name) {
        this.$router.go({ name });
      } else {
        this.$router.push({ name });
      }
    },
  },
};
</script>

<style scoped>
@import url(//fonts.googleapis.com/css?family=Vibur);

.Navbar {
  position: fixed;
  top: 0;
  background: black;
}

.logo {
  font-family: "Kaushan Script", cursive;
}
a {
  color: black !important;
  text-decoration: none !important;
}
.navBtn {
  font-family: "Nanum Gothic", sans-serif;
  /* font-family: 'Do Hyeon', sans-serif; */
  font-size: 17.5px;
  color: #616161;
}
.selectLang:hover {
  background-color: #443535;
}
.language {
  font-family: "paybooc-Medium", sans-serif;
}
.v-snack__wrapper {
  min-width: none;
  min-height: none;
}
.container text-center container--fluid {
  min-height: none;
}
/* .nav {
  position: fixed;
  background: beige;
  padding: 0;
} */
.navigation {
  width: 100%;
  top: 0;
}

.logo,
.title {
  user-select: none;
  margin-top: 10px;
}

.logo b,
.title b {
  font: 300 4vh "Vibur";
  color: #fdd;
  text-shadow: 0 -40px 100px, 0 0 2px, 0 0 1em #ff0000, 0 0 0.5em #ff0000,
    0 0 0.1em #ff0000, 0 10px 3px #000;
}
.logo b span {
  animation: blink linear infinite 2s;
}
.logo b span:nth-of-type(2) {
  animation: blink linear infinite 3s;
}
@keyframes blink {
  78% {
    color: inherit;
    text-shadow: inherit;
  }
  79% {
    color: #333;
  }
  80% {
    text-shadow: none;
  }
  81% {
    color: inherit;
    text-shadow: inherit;
  }
  82% {
    color: #333;
    text-shadow: none;
  }
  83% {
    color: inherit;
    text-shadow: inherit;
  }
  92% {
    color: #333;
    text-shadow: none;
  }
  92.5% {
    color: inherit;
    text-shadow: inherit;
  }
}

/* follow me @nodws */
#btn-twtr {
  clear: both;
  color: #fff;
  border: 2px solid;
  border-radius: 3px;
  text-align: center;
  text-decoration: none;
  display: block;
  font-family: sans-serif;
  font-size: 14px;
  width: 13em;
  padding: 5px 10px;
  font-weight: 600;
  position: absolute;
  bottom: 20px;
  left: 0;
  right: 0;
  margin: 0 auto;
  background: rgba(0, 0, 0, 0.2);
  box-shadow: 0 0 0px 3px rgba(0, 0, 0, 0.2);
  opacity: 0.4;
}
#btn-twtr:hover {
  color: #fff;
  opacity: 1;
}

.trans {
  font-family: "paybooc-Medium", sans-serif;
}
</style>
