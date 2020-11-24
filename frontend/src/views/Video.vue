<template>
  <v-container>
    <div class="video">
      <Navbar />
      <div style="width: 100%; height: 85vh; display: block; margin-top: 7%">
        <v-row no-gutters>
          <v-col cols="7" class="ml-5">
            <v-card>
              <youtube
                id="ytp"
                :video-id="videoId"
                ref="youtube"
                :nocookie="true"
                width="100%"
                :player-vars="playerVars"
                @ready="getCaptionsList"
                @paused="sayHi"
                @playing="playing"
              ></youtube>
            </v-card>
            <template v-if="videoInfo != null">
              <v-slider
                v-model="timer"
                color="error"
                :max="videoInfo.duration / 1000"
                @click="sliderClick"
                class="align-center"
              >
              </v-slider>
            </template>

            <v-card>
              <v-tabs color="red lighten-5">
                <!-- <v-tabs-slider color="yellow"></v-tabs-slider> -->
                <v-tab>
                  <v-icon left> mdi-comment-processing-outline </v-icon>
                  Text
                </v-tab>
                <v-tab>
                  <v-icon left> mdi-help </v-icon>
                  Quiz
                </v-tab>
                <v-tab>
                  <v-icon left> mdi-text-to-speech </v-icon>
                  Rec
                </v-tab>
                <v-tab>
                  <v-icon left> mdi-information-outline </v-icon>
                  Info
                </v-tab>

                <v-tab-item>
                  <v-card flat>
                    <v-card-text>
                      <v-row>
                        <v-col cols="12">
                          <h2 class="font" v-html="nowText">
                            <v-icon>mdi-comment-processing-outline</v-icon> :
                          </h2>
                        </v-col>
                      </v-row>
                    </v-card-text>
                    <v-card-actions style="padding: 0 16px">
                      <v-row>
                        <v-col cols="12">
                          <h3 v-html="translated">
                            <v-icon>mdi-google-translate</v-icon> :
                          </h3>
                        </v-col>
                      </v-row>
                    </v-card-actions>
                    <v-card-actions>
                      <v-row>
                        <v-spacer></v-spacer>
                        <v-col cols="2">
                          <v-btn
                            outlined
                            rounded
                            text
                            color="grey lighten-5"
                            @click="translate"
                          >
                            번역
                          </v-btn>
                        </v-col>
                        <v-col cols="2">
                          <v-dialog
                            v-model="dialog3"
                            persistent
                            max-width="600px"
                          >
                            <template v-slot:activator="{ on, attrs }">
                              <v-btn
                                outlined
                                rounded
                                text
                                color="grey lighten-5"
                                v-bind="attrs"
                                v-on="on"
                                @click="pauseVideo"
                              >
                                스크랩
                              </v-btn>
                            </template>
                            <v-card>
                              <v-card-title>
                                <span class="headline">Scrap</span>
                              </v-card-title>
                              <v-card-text>
                                <v-container>
                                  <v-row>
                                    <v-col cols="12">
                                      <p
                                        class="subtitle-2"
                                        v-html="nowText"
                                      ></p>
                                    </v-col>
                                    <v-col cols="12">
                                      <v-text-field
                                        v-model="memo"
                                        label="메모할 내용"
                                      ></v-text-field>
                                    </v-col>
                                  </v-row>
                                </v-container>
                              </v-card-text>
                              <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn
                                  color="blue darken-1"
                                  text
                                  @click="dialog3close"
                                >
                                  Close
                                </v-btn>
                                <v-btn
                                  color="blue darken-1"
                                  text
                                  @click="dialog3save"
                                >
                                  Save
                                </v-btn>
                              </v-card-actions>
                            </v-card>
                          </v-dialog>
                        </v-col>
                      </v-row>
                    </v-card-actions>
                  </v-card>
                </v-tab-item>
                <v-tab-item>
                  <v-card flat>
                    <v-card-text>
                      <!-- <Quiz /> -->
                      <v-btn class="mt-3" @click="onQuiz"
                        ><strong
                          ><span style="color: red; padding-right: 2px"
                            >Quiz
                          </span></strong
                        >생성</v-btn
                      >
                      <div class="code-box mx-auto font" @dragover="dragover">
                        <div v-if="loadingBar" class="text-center">
                          <v-progress-circular
                            class="mt-5"
                            :width="3"
                            color="red"
                            indeterminate
                          ></v-progress-circular>
                        </div>
                        <div class="play-box mx-auto mt-3">
                          <div
                            style="display: inline-block"
                            class="pr-1"
                            v-for="(item, i) in quizBox"
                            :key="i"
                          >
                            <div
                              v-if="item.quiz != 'blank'"
                              style="
                                margin-bottom: 10px;
                                color: white;
                                font-size: 20px;
                              "
                            >
                              {{ item.quiz }}
                            </div>
                            <div
                              @dragover="ondragover(`b${item.index}`)"
                              v-else
                              :id="`blank${item.index}`"
                              :class="`b${item.index}`"
                              class="blank droppable"
                              @drop="drop(item.index)"
                            ></div>
                          </div>
                        </div>
                        <div v-if="nowText" class="block-box">
                          <div class="block-list mt-2 droppable" @drop="drop">
                            <div class="droppable">
                              <div
                                v-for="(keyword, i) in keyword"
                                @drop="drop"
                                draggable="true"
                                @dragstart="dragstart(keyword.original, i)"
                                :key="i"
                                :id="`keyword${i}`"
                                :class="`k${i}`"
                                style="display: inline-block"
                              >
                                <div class="block">
                                  {{ keyword.key }}
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </v-card-text>
                  </v-card>
                </v-tab-item>
                <v-tab-item>
                  <v-card flat>
                    <v-card-text>
                      <div>
                        <h2 class="font" v-html="nowText">
                          <v-icon>mdi-comment-processing-outline</v-icon> :
                          {{ nowText }}
                        </h2>
                      </div>
                      <v-row class="mt-5" justify="center">
                        <vue-record-audio
                          mode="press"
                          @result="onResult"
                          class="red darken-1 mr-4 mb-2"
                        />
                        <audio
                          controls=""
                          :src="audioURL"
                          controlsList="nodownload"
                        ></audio>
                      </v-row>
                    </v-card-text>
                  </v-card>
                </v-tab-item>
                <v-tab-item>
                  <v-card flat>
                    <v-card-text>
                      <template v-if="videoInfo != null">
                        <v-card dark>
                          <v-card-title>
                            <v-icon large left> mdi-youtube </v-icon>
                            <span class="title font-weight-light">{{
                              videoInfo.title
                            }}</span>
                          </v-card-title>

                          <v-card-text class="font-weight-bold">
                            {{ videoInfo.description }}
                          </v-card-text>
                          <v-card-actions>
                            <v-list-item class="grow">
                              <v-flex>
                                <v-btn
                                  rounded
                                  color="error"
                                  @click="
                                    $router.push({
                                      name: 'Channel',
                                      params: { id: videoInfo.channelId },
                                    })
                                  "
                                  >{{ videoInfo.channelTitle }}</v-btn
                                >
                              </v-flex>
                              <v-flex class="text-xs-right">
                                <span class="subheading mr-2">{{
                                  videoInfo.publishedAt
                                }}</span>
                              </v-flex>
                            </v-list-item>
                          </v-card-actions>
                        </v-card>
                        <!-- <h3>{{videoInfo.title}}</h3>
                    <h4>{{videoInfo.description}}</h4>
                    <h4>업로드일 : {{videoInfo.publishedAt}}</h4>
                    <h4>채널 : {{videoInfo.channelTitle}}</h4> -->
                        <!-- <h4>채널ID : {{videoInfo.channelId}}</h4> -->
                        <!-- <h4>기본언어 : {{videoInfo.defaultLanguage}}</h4> -->
                        <!-- <h4>재생시간 : {{videoInfo.duration/1000}}</h4> -->
                      </template>
                    </v-card-text>
                  </v-card>
                </v-tab-item>
              </v-tabs>
            </v-card>
            <!-- <button @click="skipVideo">skip</button>   -->
            <!-- <div>    -->
            <!-- <button @click="seekVideo(parseFloat(timer)-parseFloat(unit))">back</button>   -->
            <!-- <input type="number" v-model="unit"> -->
            <!-- <button @click="seekVideo(parseFloat(timer)+parseFloat(unit))">forward</button>   -->
            <!-- </div> -->
            <!-- <div>timer: {{timer}}</div> -->
            <!-- <div>state: {{state}}</div> -->
            <!-- <div>mode: {{mode}}</div> -->
            <!-- <button @click="beforeCaption">이전문장</button>   -->
            <!-- <button @click="nextCaption">다음문장</button>   -->
            <!-- <select v-model="selectedLang" @change="onSelectClick($event)" >
                    <option disabled value="">Please select one</option>
                    <option  v-for="(item,index) in items"  v-bind:key="index" >{{item._attributes.lang_code}}</option>
                  </select> -->

            <!-- <span>선택단어: {{ word }}</span> -->
          </v-col>
          <v-spacer></v-spacer>
          <v-col cols="4" class="mr-3">
            <v-row>
              <!-- <v-col cols="5">
                         <v-btn text @click="dialog = !dialog" v-html="selectedLang.lang_translated">언어</v-btn>
                               </v-col> -->
              <v-col cols="8">
                <v-btn
                  rounded
                  dark
                  medium
                  color="red"
                  bottom
                  @click="dialog = !dialog"
                >
                  언어선택
                </v-btn>
              </v-col>
              <v-col cols="4">
                <v-switch
                  v-model="hide"
                  label="script"
                  color="red"
                  value
                  inset
                  hide-details
                ></v-switch>
              </v-col>
            </v-row>
            <v-card height="600px" class="scroll" elevation="4" v-if="hide">
              <ul id="example-2">
                <li
                  v-for="(item, index) in caption"
                  :data-start="parseFloat(item._attributes.start)"
                  :data-end="
                    (
                      parseFloat(item._attributes.start) +
                      parseFloat(item._attributes.dur)
                    ).toFixed(3)
                  "
                  class="script font"
                  @click="captionClick(index)"
                  v-bind:key="index"
                  v-html="item._text"
                >
                  <!-- {{item._text}} -->
                </li>
              </ul>
            </v-card>
          </v-col>
        </v-row>
      </div>
      <v-row justify="center">
        <v-dialog v-model="dialog" scrollable max-width="300px">
          <!-- <template v-slot:activator="{ on, attrs }" > -->
          <!-- <v-btn  dark large color="primary" fixed right top v-bind="attrs"  v-on="on"  style="top:100px"> -->
          <!-- <v-icon dark>{{ selectedLang }}</v-icon> -->
          <!-- 언어변경 -->
          <!-- </v-btn> -->
          <!-- </template> -->
          <v-card>
            <v-card-title>Select Language</v-card-title>
            <v-divider></v-divider>
            <v-card-text style="height: 300px">
              <v-radio-group v-model="selectedLang" column>
                <!-- <option  v-for="(item,index) in items"  v-bind:key="index" >{{item._attributes.lang_code}}</option> -->
                <template v-if="Array.isArray(this.items)">
                  <template v-for="(item, index) in items">
                    <v-radio
                      v-bind:label="item._attributes.lang_translated"
                      v-bind:value="item._attributes"
                      v-bind:key="index"
                    ></v-radio>
                  </template>
                </template>
                <template v-else-if="items != null">
                  <v-radio
                    v-bind:label="items._attributes.lang_translated"
                    v-bind:value="items._attributes.lang_code"
                  ></v-radio>
                </template>
              </v-radio-group>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
              <v-btn color="error" text @click="dialog = false"> close </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-row>

      <div id="controller" style="bottom: 270px">
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="ctrBtn"
              v-bind:class="[mode == 1 ? error : '']"
              fab
              dark
              small
              @click="changeMode(1)"
            >
              <v-icon dark>mdi-trending-neutral</v-icon>
            </v-btn>
          </template>
          <span>연속 재생</span>
        </v-tooltip>
      </div>
      <div id="controller" style="bottom: 220px">
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="ctrBtn"
              fab
              small
              v-bind:class="[mode == 3 ? error : '']"
              @click="changeMode(3)"
            >
              <v-icon dark> mdi-keyboard-tab</v-icon>
            </v-btn>
          </template>
          <span>한 문장 듣기</span>
        </v-tooltip>
      </div>
      <div id="controller" style="bottom: 170px">
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="ctrBtn"
              fab
              dark
              small
              v-bind:class="[mode == 2 ? error : '']"
              @click="changeMode(2)"
            >
              <v-icon dark>mdi-refresh</v-icon>
            </v-btn>
          </template>
          <span>문장 반복재생</span>
        </v-tooltip>
      </div>
      <div id="controller" style="bottom: 110px">
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="ctrBtn"
              fab
              dark
              small
              color="red"
              @click="playVideo"
            >
              <v-icon dark>mdi-play</v-icon>
            </v-btn>
          </template>
          <span>재생</span>
        </v-tooltip>
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="ctrBtn"
              fab
              dark
              small
              color="red"
              @click="pauseVideo"
            >
              <v-icon dark>mdi-pause</v-icon>
            </v-btn>
          </template>
          <span>일시정지</span>
        </v-tooltip>
      </div>
      <div id="controller" style="bottom: 60px">
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="ctrBtn"
              fab
              dark
              small
              color="red"
              @click="seekVideo(parseFloat(timer) - parseFloat(unit))"
            >
              <v-icon dark>mdi-chevron-left</v-icon>
            </v-btn>
          </template>
          <span>10초 뒤로</span>
        </v-tooltip>
        <!-- <vue-numeric-input size="60px" :step="10"  v-model="unit" autofocus controls-type="updown"></vue-numeric-input> -->
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="ctrBtn"
              fab
              dark
              small
              color="red"
              @click="seekVideo(parseFloat(timer) + parseFloat(unit))"
            >
              <v-icon dark> mdi-chevron-right</v-icon>
            </v-btn>
          </template>
          <span>10초 앞으로</span>
        </v-tooltip>
      </div>
      <div id="controller" style="bottom: 10px">
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="ctrBtn"
              fab
              dark
              small
              color="red"
              @click="beforeCaption"
            >
              <v-icon dark>mdi-chevron-double-left</v-icon>
            </v-btn>
          </template>
          <span>이전 문장</span>
        </v-tooltip>
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="ctrBtn"
              fab
              dark
              small
              color="red"
              @click="nextCaption"
            >
              <v-icon dark>mdi-chevron-double-right</v-icon>
            </v-btn>
          </template>
          <span>다음 문장</span>
        </v-tooltip>
      </div>
    </div>

    <span id="tool">
      <v-dialog
        v-model="dialog2"
        width="30%"
        hide-overlay
        transition="dialog-bottom-transition"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn color="red darken-1" rounded dark v-bind="attrs" v-on="on"
            >사전검색</v-btn
          >
        </template>
        <v-card>
          <iframe width="100%" height="500px" :src="dictUrl + word"></iframe>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error" text @click="dialog2 = false"> close </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-btn color="blue darken-1" rounded @click="addVoca(word)"
        >단어장추가</v-btn
      >
    </span>

    <v-snackbar
      v-model="snackbar"
      timeout="1500"
      class="align-center"
      color="error"
      absolute
      rounded="pill"
      style="justify-content: center"
    >
      <p class="text-center" style="margin: 0">{{ text }}</p>
    </v-snackbar>
    <!-- <v-row justify="center">
      <v-dialog v-model="dialog4" persistent max-width="290">
        <v-card color="white" class="black--text">
          <v-card-title>
            <v-row v-if="this.$store.state.nickname != null">
              결제가 필요합니다.😭
            </v-row>
            <v-row v-else>
              로그인이 필요합니다
            </v-row>
          </v-card-title>
          <v-card-text
            v-if="this.$store.state.nickname != null"
            class="black--text"
          >
            <v-row v-if="point >= 100">
              현재 포인트 : {{ point }}<br />
              차감 포인트 : - 100
            </v-row>
            <v-row v-else>
              현재 포인트 : {{ point }}<br />
              필요 포인트 : - 100<br />
              포인트가 부족합니다.
            </v-row>
          </v-card-text>
          <v-card-text v-if="this.$store.state.nickname == null">
            로그인 후 이용해 주세요
          </v-card-text>
          <v-card-actions v-if="this.$store.state.nickname != null">
            <v-spacer></v-spacer>
            <v-btn color="black" text outlined rounded @click="$router.go(-1)">
              뒤로가기
            </v-btn>
            <v-btn
              color="black"
              text
              outlined
              rounded
              @click="pay"
              v-if="point >= 100"
            >
              결제하기
            </v-btn>
            <v-btn color="black" text outlined rounded @click="charge()" v-else>
              충전하기
            </v-btn>
          </v-card-actions>
          <v-card-actions v-if="this.$store.state.nickname == null">
            <v-spacer></v-spacer>
            <v-btn color="black" text outlined rounded @click="$router.go(-1)">
              뒤로가기
            </v-btn>
            <v-btn
              color="black"
              text
              outlined
              rounded
              @click="changeRoute('Login')"
            >
              로그인
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row> -->

    <!-- <v-row justify="center">
      <v-dialog v-model="dialog5" persistent max-width="290">
        <v-card color="white" class="black--text">
          <v-card-title>
            <v-row>
              주의!
            </v-row>
          </v-card-title>
          <v-card-text>
            <v-row class="black--text">
              현재 영상은 지원되는 자막이 없습니다.
            </v-row>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="black" text outlined rounded @click="$router.go(-1)">
              뒤로가기
            </v-btn>
            <v-btn
              color="black"
              text
              outlined
              rounded
              @click="
                dialog5 = false;
                paid = true;
              "
            >
              영상보기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row> -->

    <v-dialog v-model="dialog6" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">단어장추가</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <p class="subtitle-2">
                  국가 : {{ selectedLang.lang_translated }}
                </p>
                <p class="subtitle-2">단어 : {{ addWord }}</p>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="mean"
                  label="의미를 적어주세요"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialog6close">
            Close
          </v-btn>
          <v-btn color="blue darken-1" text @click="dialog6save"> Save </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<script>
import Navbar from "@/components/Navbar";
// import Quiz from "@/components/Video/Quiz";
import Raxios from "axios";
import axios from "@/plugins/axios";
import Swal from "sweetalert2";
import $ from "jquery";

var convert = require("xml-js");

export default {
  name: "Video",
  components: {
    Navbar,
    // Quiz
  },
  data() {
    return {
      timeout: null,
      addWord: "",
      point: -1,
      paid: false,
      memo: "",
      mean: "",
      snackbar: false,
      text: "",
      hide: true,
      myTimer: null,
      setMode: null,
      primary: "primary",
      error: "error",
      context: null,
      audioURL: "",
      dictUrl: "https://m.dic.daum.net/search.do?q=",
      word: "",
      dialog: false,
      dialog2: false,
      dialog3: false,
      dialog4: false,
      dialog5: false,
      dialog6: false,
      videoInfo: null,
      isBlank: true,
      mode: 1,
      unit: 10,
      nowText: "",
      translated: "",
      nowIdx: -1,
      preIdx: -1,
      elements: null,
      videoId: "",
      selectedLang: {},
      state: 0,
      playerVars: {
        autoplay: 1,
        cc_load_policy: 0,
        // cc_lang_pref: 'en',
        controls: 0,
        modestbranding: 1,
        fs: 0,
        enablejsapi: 1,
        disablekb: 0,
      },
      items: null,
      caption: null,
      timer: 0,
      // quiz
      usedQuiz: false,
      nowText: "",
      quizBox: [],
      keyword: [],
      blankSize: "",
      answer: {},
      score: 0,
      userAns: 0,
      rightAns: [],
      keyIdx: "",
      keyIdxWidth: 0,
      keywordWidth: {},
      loadingBar: false,
      // drag
      blankIdx: "",
      idx: "",
      isMove: true,
      isObstacle: false,
      distX: "",
      distY: "",
      targetClass: "",
      targetClass2: "",
      targetNum: "",
      targetFlag: false,
      classId: "a",
    };
  },
  methods: {
    charge() {
      this.$router.push({
        name: "Pay",
        params: { point: 1000, vid: this.videoId },
      });
    },
    async pay() {
      console.log("결제진행");
      var temp = "https://i.ytimg.com/vi/" + this.videoId + "/mqdefault.jpg";
      var temp2 = this.videoInfo.defaultAudioLanguage + ".";
      const params = {
        title: this.videoInfo.title,
        defaultLanguage: temp2.substring(0, 2),
        youtubeVideoid: this.videoId,
        thumbnail: temp,
      };

      await axios
        .post("https://morelang.gq/api/user/watch-video", params, {
          headers: {
            "content-type": "application/json",
          },
        })
        .then((res) => {
          console.log(res);
        });
      this.dialog4 = false;
    },
    changeRoute(name) {
      // console.log(this.$route.name === name);
      if (this.$route.name === name) {
        this.$router.go({ name });
      } else {
        this.$router.push({ name });
      }
    },
    dialog6close() {
      this.dialog6 = false;
      this.mean = "";
    },
    dialog6save() {
      console.log(this.selectedLang.lang_translated);
      console.log(this.addWord);
      console.log(this.mean);
      const params = {
        country: this.selectedLang.lang_translated,
        eachVoca: this.addWord,
        learn: false,
        eachMean: this.mean,
      };

      axios
        .post("/user/regist-voca", params, {
          headers: {
            "content-type": "application/json",
          },
        })
        .then((res) => {
          console.log(res);
          this.text = "단어장 추가완료";
          this.snackbar = true;
        });

      this.mean = "";
      this.dialog6 = false;
    },
    dialog3close() {
      this.dialog3 = false;
      this.memo = "";
    },
    dialog3save() {
      const params = {
        country: this.selectedLang.lang_translated,
        memo: this.memo,
        sentence: this.nowText,
        videourl: this.videoId,
      };
      axios
        .post("/user/do-scrap", params, {
          headers: {
            "content-type": "application/json",
          },
        })
        .then((res) => {
          console.log(res);
          this.text = "스크랩 완료";
          this.snackbar = true;
        });
      this.dialog3 = false;
      this.memo = "";
    },
    addVoca(addWord) {
      if (this.$store.state.nickname != null) {
        this.pauseVideo();
        this.dialog6 = true;
        this.addWord = addWord;
      } else {
        this.text = "로그인이 필요한 기능입니다.";
        this.snackbar = true;
      }
    },
    sliderClick() {
      if (this.mode == 2) {
        this.mode = 1;
      }
      this.seekVideo(parseFloat(this.timer));
    },
    onResult(data) {
      // console.log('The blob data:', data);
      this.audioURL = window.URL.createObjectURL(data);
      // console.log('Downloadable audio', this.audioURL);
    },
    translate() {
      if (this.nowText != "") {
        // console.log(this.nowText);
        var temp2 = this.nowText.replace(/(\r\n|\n|\r)/gm, "");
        // console.log(temp2);
        var temp = this.selectedLang.lang_code.substr(0, 2);
        if (temp == "ko") {
          temp = "kr";
        } else if (temp == "ja") {
          temp = "jp";
        } else if (temp == "zh") {
          temp = "cn";
        }
        axios
          .get("/newuser/translate", {
            params: {
              query: temp2,
              src_lang: temp,
              target_lang: "kr",
            },
          })
          .then((res) => {
            if (res.data == "") {
              this.translated = "현재 언어는 번역이 지원되지 않습니다.";
            } else {
              // console.log("res = ",res);
              this.translated = res.data;
            }
          });
      }
    },
    removeAll() {
      Array.from(this.elements).some((el) => {
        if (el.classList.contains("current")) {
          el.classList.remove("current");
          return true;
        }
      });
    },
    captionClick(idx) {
      this.removeAll();
      this.preIdx = this.nowIdx;
      this.nowIdx = idx;
      this.nowText = this.elements[this.nowIdx].innerHTML;
      if (this.nowIdx != -1)
        this.elements[this.nowIdx].classList.add("current");
      this.seekVideo(this.caption[this.nowIdx]._attributes.start);
    },
    changeMode(num) {
      this.mode = num;
    },
    nextCaption() {
      if (this.nowIdx + 1 < this.caption.length) {
        this.removeAll();
        this.preIdx = this.nowIdx;
        this.nowIdx++;
        this.nowText = this.elements[this.nowIdx].innerHTML;
        this.seekVideo(this.caption[this.nowIdx]._attributes.start);
        this.elements[this.nowIdx].classList.add("current");
      }
    },
    beforeCaption() {
      if (this.nowIdx > 0) {
        this.removeAll();
        this.preIdx = this.nowIdx;
        this.nowIdx--;
        this.nowText = this.elements[this.nowIdx].innerHTML;
        this.seekVideo(this.caption[this.nowIdx]._attributes.start);
        this.elements[this.nowIdx].classList.add("current");
      }
    },
    async playMode() {
      if (this.state == 1) {
        var tempIdx = -1;
        if (this.elements != null) {
          await Array.from(this.elements).some((el, i) => {
            if (el.dataset.start < this.timer && this.timer < el.dataset.end) {
              tempIdx = i;
              // console.log("빠져나간다 tempIDX=",tempIdx);
              return true;
            }
            // console.log("아직 안빠져나갔다");
          });
          if (tempIdx != -1) {
            ///말하는중
            // console.log("말하는중");
            if (tempIdx != this.nowIdx && this.mode == 2) {
              //현재 대사 반복 재생  == 현재 인덱스 반복 재생   //새로운 대사 시작?  => 기존의 인덱스 시작으로 넘어가야함(이때 인덱스 -1 은 예외 처리)
              if (this.nowIdx != -1) {
                // console.log("돌아가자")
                this.seekVideo(this.caption[this.nowIdx]._attributes.start);
                this.nowText = this.elements[this.nowIdx].innerHTML;
              } else {
                // console.log("첫문장이구나");
                this.nowIdx = tempIdx;
                this.elements[this.nowIdx].classList.add("current");
                this.nowText = this.elements[this.nowIdx].innerHTML;
              }
            } else if (this.mode == 3) {
              //현재 문장 끝나면 일시 정지 시키기
              if (tempIdx != this.nowIdx) {
                if (this.nowIdx != -1 && this.isBlank == false) {
                  this.pauseVideo();
                  await this.sleep(100);
                  this.preIdx = this.nowIdx;
                  this.nowIdx = tempIdx;
                } else {
                  this.nowIdx = tempIdx;
                }
              } else {
                //새 대사?
                this.removeAll();
                if (this.nowIdx != -1)
                  this.elements[this.nowIdx].classList.add("current");
                this.nowText = this.elements[this.nowIdx].innerHTML;
              }
            } else if (this.mode == 1) {
              if (this.nowIdx != -1) {
                this.removeAll();
                this.preIdx = this.nowIdx;
              }
              this.nowIdx = tempIdx;
              this.elements[this.nowIdx].classList.add("current");
              this.nowText = this.elements[this.nowIdx].innerHTML;
            }

            this.isBlank = false;
          } else if (this.isBlank == false) {
            //정적이 흐르는중
            this.isBlank = true;
            if (this.mode == 2) {
              // console.log("정적이 흐르나")
              this.seekVideo(this.caption[this.nowIdx]._attributes.start);
            } else if (this.mode == 3) {
              this.pauseVideo();
            } else if (this.mode == 1) {
              this.nowText = "";
              this.preIdx = this.nowIdx;
              this.removeAll();
            }
          } else if (this.mode == 3) {
            this.nowText = "";
            this.removeAll();
            this.preIdx = this.nowIdx;
          }
        }
      }
    },

    async getCurrentTime() {
      if (this.state == 1) {
        await this.player.getCurrentTime().then((data) => (this.timer = data));
      }
    },

    async playVideo() {
      await this.player.playVideo();
    },
    async pauseVideo() {
      await this.player.pauseVideo();
    },
    async seekVideo(t) {
      // console.log("시크비디오");
      await this.player.seekTo(t, true);
      await this.playVideo();
    },

    async setCCLanguage() {
      await this.player.setOption("captions", "track", {
        languageCode: this.selectedLang.lang_code,
      });
    },
    async youtubeStateChange(event) {
      // var myTimer;
      // var setMode;
      // console.log('event:', event);
      // console.log('state data : ',event.data);
      this.state = event.data;

      if (event.data == 1) {
        // playing
        console.log("setIneteval");
        this.myTimer = setInterval(this.getCurrentTime, 100);
        this.setMode = setInterval(this.playMode, 1000);
      } else {
        // not playing
        console.log("clearInterval");

        clearInterval(this.myTimer);
        clearInterval(this.setMode);
      }
    },
    async getCaptionsList() {
      // console.log(await this.player.getOption( "captions" , 'track'));

      var temp = axios.defaults.headers.common;
      axios.defaults.headers.common = null;

      await Raxios.get("https://video.google.com/timedtext?type=list", {
        params: {
          v: this.videoId,
        },
        headers: {
          "Content-Type": null,
        },
      }).then((res) => {
        var xml = res.data;
        var json = convert.xml2json(xml, { compact: true });
        // console.log("json = ",json)
        this.items = JSON.parse(json).transcript_list.track;
        // console.log(this.items[0]._attributes.lang_code);
        // console.log("items = ",this.items.length)
        // console.log("type = ",typeof this.items)
        // console.log(this.items);
        if (this.items != undefined) {
          // console.log(this.items[0]);
          // console.log("isarray=",Array.isArray(this.items))
          if (Array.isArray(this.items)) {
            this.selectedLang = this.items[0]._attributes;
            console.log(this.selectedLang);
            this.getCaption();
          } else {
            this.selectedLang = this.items._attributes;
            console.log(this.selectedLang);
            this.getCaption();
          }
        } else {
          // 아무 자막이 없는거 처리 해줘야한다
          this.dialog5 = true;
          Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "현재 영상은 지원되는 자막이 없습니다.",
            allowOutsideClick: false,
            showCancelButton: true,
            confirmButtonText: "Close",
            cancelButtonText: "Continue",
            // footer: '<a href>Why do I have this issue?</a>'
          }).then((result) => {
            if (result.isConfirmed) {
              // this.$router.go(-1);
              window.close();
            } else {
              this.dialog5 = false;
              this.paid = true;
            }
          });
        }
      });
      axios.defaults.headers.common = temp;
    },
    async getCaption() {
      var temp = null;
      if (this.selectedLang.name != "") {
        temp = this.selectedLang.name;
      }
      await Raxios.get("https://video.google.com/timedtext", {
        params: {
          v: this.videoId,
          lang: this.selectedLang.lang_code,
          name: temp,
        },
      }).then((res) => {
        console.log(res);
        var xml = res.data;
        var json = convert.xml2json(xml, { compact: true });
        this.caption = JSON.parse(json).transcript.text;
        // console.log(this.caption);
      });

      this.elements = document.querySelectorAll(".script");
    },
    async youtubApiChange(youtubeState) {
      console.log("stateChange", youtubeState);
      await this.player.setOption("captions", "track", []);
    },
    // onSelectClick(event){
    //     this.selectedLang = event.target.value;
    //     this.getCaption();
    // },

    playing() {
      console.log(" start play");
    },
    async sayHi() {
      console.log("Hi");
    },
    sleep(t) {
      return new Promise((resolve) => setTimeout(resolve, t));
    },
    // async getOption() {                                     //현재 재생되고 있는 영상 자막이있는지 없는지 판별 가능
    //    var promise =this.player.getOptions();
    //    promise
    //           .then(function(data){
    //             console.dir(data[0]);
    //             // module = data[0];
    //             })
    //           .catch(err => console.log(err));
    // },

    // async getTracklist(){
    //     this.items = await this.player.getOption( "captions" , 'tracklist');
    //     console.log(this.items);
    // },

    // async getTrack(){

    // var temp = await this.player.getOption( "captions" , 'track');
    //  console.log(temp.languageCode);
    // },
    // 퀴즈
    onQuiz() {
      // this.$store.state.videoText = this.nowText;
      this.loadingBar = true;
      if (!this.usedQuiz) {
        this.usedQuiz = true;
      } else if (this.usedQuiz) {
        this.quizBox = [];
        this.keyword = [];
        this.userAns = 0;
        this.rightAns = [];
        this.score = 0;
      }
      this.quizMounted();
    },
    quizMounted() {
      this.onMove();
      if (this.nowText) {
        axios
          .post(
            `https://morelang.gq/api/newuser/puzzle?inputText=${this.nowText}`
          )
          .then((res) => {
            this.answer = res.data.answer;
            var quizInput = res.data.inputTextArray;
            this.keyword = res.data.keyword;
            var j = 1;
            for (var i = 0; i < quizInput.length; i++) {
              if (quizInput[i] === "______") {
                this.quizBox.push({ index: j++, quiz: "blank" });
              } else if (quizInput[i].startsWith("______")) {
                this.quizBox.push({ index: j++, quiz: "blank" });
                this.quizBox.push({ index: 0, quiz: quizInput[i].slice(6) });
              } else {
                this.quizBox.push({ index: 0, quiz: quizInput[i] });
              }
            }
            if (this.quizBox) {
              this.hide = false;
              this.changeMode(3);
              this.loadingBar = false;
            }
          })
          .catch((err) => {
            this.loadingBar = false;
            Swal.fire({
              icon: "warning",
              text: "해당 언어는 퀴즈 서비스 개발중입니다..",
              timer: 1550,
              background: "white",
              showConfirmButton: false,
            });
          });
      }
    },
    checkAnswer() {
      if (this.score !== 0  && this.score === Object.keys(this.keyword).length) {
        Swal.fire({
          width: 500,
          background: "#fff url(@/assets/img/answer.gif)",
          text: "정답입니다!",
          timer: 1700,
          icon: "success",
          iconColor: "red",
          showConfirmButton: false,
        });
        this.hide = true;
        setTimeout(() => {
          this.playVideo();
        }, 1750);
        this.keyword = [];
        this.quizBox = [];
        this.changeMode(1);
      }
    },
    onMove() {
      this.isMove = true;
    },
    dragstart(ans, i) {
      this.keyIdx = `k${i}`;
      if (!this.keywordWidth[[this.keyIdx]]) {
        this.keywordWidth[[this.keyIdx]] = $(`.${this.keyIdx}`).width();
      }
      let posX = event.pageX;
      let posY = event.pageY;
      this.distX = event.srcElement.offsetLeft - posX;
      this.distY = event.srcElement.offsetTop - posY;
      event.target.classList.add(`${this.classId}`);
      this.classId += "0";
      this.targetClass = event.target.classList[2];
      this.targetClass2 = event.target.classList[1];
      this.userAns = ans;
    },
    ondragover(idx) {
      var bIdx = idx.slice(1);
      this.blankIdx = $(`#blank${bIdx}`);
      if (!this.blankIdx.hasClass("checked")) {
        this.keyIdxWidth = $(`.${this.keyIdx}`).width() - 5;
        $(`.${idx}`).css("width", `${this.keyIdxWidth}`);
      } else if ($(`.${this.keyIdx}`).width() === 0) {
        this.keyIdxWidth = this.keywordWidth[[this.keyIdx]];
      }
    },
    dragover(event) {
      event.stopPropagation();
      event.preventDefault();
    },
    drop(idx) {
      event.stopPropagation();
      event.preventDefault();
      if (idx !== this.userAns) {
        $(`.b${idx}`).css("border", "red solid 2px");
        Swal.fire({
          width: 320,
          text: "😢 다시 생각해보세요! 😢",
          timer: 1550,
          background: "white",
          // icon: "success",
          showConfirmButton: false,
        });
      } else if (idx === this.userAns && !this.rightAns.includes(idx)) {
        $(`.b${idx}`).css("font-size", "18px");
        this.score += 1;
        // console.log(this.score);
        this.rightAns.push(idx);
        // 드롭
        // 클래스 추가
        var idIdx = "keyword" + this.keyIdx.slice(1);
        var keyId = document.getElementById(idIdx);
        // console.log(keyId);
        this.blankIdx = document.getElementById(`blank${idx}`);
        if (!$(`#blank${idx}`).hasClass("checked")) {
          this.blankIdx.appendChild(keyId);
        }
        this.blankIdx.classList.add("checked");
        if (
          event.target.classList &&
          event.target.classList.contains("droppable")
        ) {
          const CLONE = document.querySelectorAll(`.${this.targetClass2}`);
          for (let i = 0; i < CLONE.length; i++) {
            if (CLONE[i].classList.length == 2) {
              this.targetFlag = false;
            } else {
              this.targetFlag = true;
            }
          }
        }
      }
    },
  },

  watch: {
    selectedLang: {
      // console.log("바뀜!!")
      handler: function () {
        //  console.log('The list of colours has changed!');
        var temp = axios.defaults.headers.common;
        axios.defaults.headers.common = null;
        this.getCaption();
        this.nowText = "";
        axios.defaults.headers.common = temp;
      },
      deep: true,
    },
    nowText: function () {
      this.translated = "";
      this.audioURL = "";
    },
    // quiz
    score: function () {
      this.checkAnswer();
    },
  },
  computed: {
    player() {
      return this.$refs.youtube.player;
    },
  },
  async created() {
    console.log(this.videoId);
    this.videoId = this.$route.params.vid;
    await axios
      .get("/newuser/video", {
        params: {
          id: this.videoId,
        },
      })
      .then((res) => {
        this.videoInfo = res.data;
        console.log(this.videoInfo);
      });

    if (this.$store.state.nickname != null) {
      // console.log("오호라 유저구나");
      // console.log(this.videoInfo);
      await axios
        .get("https://morelang.gq/api/user/pay/my-point")
        .then((res) => {
          console.log(res);
          this.point = res.data;
        });
      var temp = "https://i.ytimg.com/vi/" + this.videoId + "/mqdefault.jpg";
      var temp2 = this.videoInfo.defaultAudioLanguage + ".";
      const params = {
        title: this.videoInfo.title,
        defaultLanguage: temp2.substring(0, 2),
        youtubeVideoid: this.videoId,
        thumbnail: temp,
      };

      await axios
        .post("https://morelang.gq/api/user/iswatched", params, {
          headers: {
            "content-type": "application/json",
          },
        })
        .then((res) => {
          console.log("봤니안봤니");
          console.log(res.data);
          if (res.data == true) {
            this.paid = true;
          }
        });
    }
  },
  mounted() {
    // if (this.dialog5) {
    //   console.log("dialog5" + this.dialog5)

    // }
    this.context = new AudioContext();
    // One-liner to resume playback when user interacted with the page.
    document.querySelector("button").addEventListener("click", function () {
      this.context.resume().then(() => {
        console.log("Playback resumed successfully");
      });
    });

    // console.log("mounted!!");
    this.player.addEventListener("onStateChange", this.youtubeStateChange);
    this.player.addEventListener("onApiChange", this.youtubApiChange);
    document.addEventListener("mouseup", (event) => {
      // console.log(event);
      this.word = window.getSelection().toString();
      var temp = document.getElementById("tool");
      // console.log(word != "");
      if (this.word != "" && temp.style.display == "none") {
        console.log(this.word);
        temp.style.display = "block ";
        temp.style.left = event.pageX + "px";
        temp.style.top = event.pageY + "px";
      } else if (this.word == "") {
        temp.style.display = "none";
      }
    });

    this.timeout = setTimeout(() => {
      if (this.items != undefined && this.paid == false) {
        this.dialog4 = true;
        this.pauseVideo();
        if (this.$store.state.nickname == null) {
          Swal.fire({
            title: "로그인 후 이용해 주세요",
            icon: "warning",
            confirmButtonText: "OK",
            allowOutsideClick: false,
            // closeOnClickOutside: false,
          }).then((result) => {
            if (result.isConfirmed) {
              this.changeRoute("Login");
            }
          });
        } else {
          Swal.fire({
            title: "영상을 결제하시겠습니까?",
            // "현재 포인트 : " + this.point + " 포인트<br>" +
            text: "차감 포인트 : 100 포인트",
            icon: "warning",
            // closeOnClickOutside: false,
            // allowOutsideClick: false,
            showCancelButton: true,
            cancelButtonText: "창닫기",
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "결제하기",
            allowOutsideClick: false,
          }).then((result) => {
            if (result.isConfirmed) {
              if (this.point >= 100) {
                this.pay();
                Swal.fire(
                  "결제성공!",
                  "해당 영상의 학습이 가능합니다.",
                  "success"
                );
              } else {
                Swal.fire({
                  icon: "error",
                  title: "Oops...",
                  text: "포인트 충전이 필요합니다.",
                  confirmButtonText: "충전하기",
                  // footer: "<a href>Why do I have this issue?</a>"
                }).then((result) => {
                  // console.log("충전이 필요할때 result=",result);
                  if (result.isConfirmed) {
                    this.charge();
                  }
                });
              }
            } else {
              window.close();
            }
          });
        }
      }
    }, 10000);
    //  document.addEventListener('mousedown', function() {
    // console.log(event);
    // window.getSelection().empty();
    // var word = window.getSelection().toString();
    // var temp =document.getElementById("tool")
    // console.log(word != "");
    // if(word != ""){
    // console.log(word);
    // temp.style.display = "block ";
    // temp.style.left=event.pageX+"px"
    // temp.style.top=event.pageY+"px"
    // }else{
    // temp.style.display = "none";
    // }
    // });
  },
  beforeDestroy() {
    clearTimeout(this.timeout);
  },
};

//  function selectText() {
//               var selectionText = "";
//               if (document.getSelection) {
//                   selectionText = document.getSelection();
//               } else if (document.selection) {
//                   selectionText = document.selection.createRange().text;
//               }
//               return selectionText;
//           }

//           document.onmouseup = function() {
//               document.getElementById("console").innerHTML = selectText();
//           }
</script>
<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Nanum+Pen+Script&display=swap");
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@100&display=swap");
@import url("https://fonts.googleapis.com/css2?family=Hi+Melody&display=swap");
@import url("https://fonts.googleapis.com/css2?family=Nanum+Myeongjo&display=swap");

.font {
  font-family: "Nanum Myeongjo", "Noto Sans SC", sans-serif;
}

.script:hover {
  background: #ff564a;
}
.script.current {
  background: gray;
}
.scroll {
  overflow-y: scroll;
}

.scroll::-webkit-scrollbar {
  width: 10px;
}

.scroll::-webkit-scrollbar-thumb {
  background: #ff3c33;
  border-radius: 40px;
}

.scroll::-webkit-scrollbar-track {
  /* background: #eee; */
  border-radius: 40px;
}

#controller {
  position: fixed;
  left: 10px;
}
.ctrBtn {
  margin: 10px;
}

#tool {
  /* background: orange; */
  display: none;
  position: absolute;
  /* top:  512px; left: 178px; */
}

.i-am-active {
  color: orange;
  background: pink;
}

ul {
  list-style-type: "💬";
}

ul li:before {
  content: "  ";
  margin-left: 5px;
}

ul li {
  margin-left: 5px;
  margin-bottom: 10px;
}

.subtitle {
  border: none;
  /* background: #ff3c33; */
}

.white {
  /* background: #fff; */
}
/* quiz */
.code-block-container .unity-box {
  width: 80%;
  margin-right: 1%;
}
.code-block-container .code-box {
  position: relative;
}
.play-box {
  width: 100%;
  padding: 8px;
}
.code-box {
  width: 95%;
  height: 70%;
  border-radius: 7px;
}
.block-box .block-menu-bar {
  width: 30%;
}
.block-box {
  width: 100%;
  padding: 10px;
  margin: 0 2px;
  border-radius: 7px;
}
.block-box .block-menu-bar .on-menu-bar {
  background-color: black;
  font-weight: 700;
}
.block-box .block-menu-bar .menu {
  justify-content: center;
  align-items: center;
  padding-left: 5px;
  height: 50px;
  border-top-left-radius: 20px;
  border-bottom-left-radius: 10px;
  margin-bottom: 5px;
  cursor: pointer;
  transition: background-color 0.5s ease;
}
.block-list .block {
  padding: 2px 7px;
  margin-right: 8px;
  border-radius: 3px;
  background-color: #D32F2F;
  margin-bottom: 10px;
  cursor: pointer;
  color: white;
  font-size: 19px;
}
.play-box .play {
  position: absolute;
  top: 0;
  left: 0;
  width: 100px;
  height: 50px;
}
.code-block-container {
  width: 100%;
  height: 100%;
}
.blank {
  border-radius: 3px;
  background-color: lightgrey;
  width: 50px;
  height: 25px;
}
.checked {
  text-align: center;
  /* padding-left: 10px; */
  background-color: #D32F2F;
  color: white;
}
.v-progress-circular {
  margin: 1rem;
}
</style>