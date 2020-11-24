<template>
  <div>
  <v-container style="margin-top: 15px;">      
    <v-select
      v-if="`quizVoca` && !testStart"
      style="float:right; max-width:220px; margin-right: 50px"
      :items="language"
      label="Language"
      v-model="selectlang"
      dense
      outlined
    ></v-select>
    <h1 class="mb-3">단어 테스트</h1>
    <span>단어모음에 저장된 단어들로 자가 테스트를 합니다.</span>

    
    <div v-if="testStart && !quizVoca && !testCompleted" style="margin-top: 100px; text-align:center;">
      <v-icon size="70">mdi-file-document-edit-outline</v-icon>
      <p style="font-size: 20px; margin-top: 7px"> 단어장 추가를 하면 테스트를 이용할 수 있습니다. </p>
    </div>
    <div v-else-if="testStart && !nextVoca && testCompleted" style="text-align:center;">
      <p style="font-size: 20px; margin-top: 110px; text-align:center;"> 단어장의 모든 단어 테스트를 완료하였습니다. </p>
      <v-btn style="text-align:center;" color="blue" @click="End()">다시 보기</v-btn>
    </div>
    <v-card 
      v-if="quizVoca && !testCompleted"
      class="mt-5 px-5"
      color="white"
      max-width="700px"
      min-height="250px"
      style= "margin-left: 10px"
    >
    <div v-if="!testStart" style="text-align:center; padding-top: 100px;">
      <p style="color:black; margin-bottom: 8px; font-size: 18px">버튼을 누르면 테스트가 시작됩니다.</p>
      <v-btn color="blue" @click="Start()">테스트 시작</v-btn>
    </div>
    <v-card-text v-if="testStart" style="color:black" >
      <div v-if="selectlang != 'All'">{{this.selectlang}}</div>
      <span style="float:right">{{nextIdx}}/{{maxIdx+1}}</span>
      
      <h2 class="display-1 my-3 mx-auto" style="text-align:center; font-weight: bold;">{{quizVoca}}</h2>
      <div style=" margin-top: 30px !important;" >
        <div v-for="(ans, i) in answerLst" :key="i" class="pretty p-icon p-round p-jelly my-5 mx-5" style="display:block;">
            <input type="radio" v-model="checkedAns" :value="i" />
            <div :id="i" style="font-size: 22px;" class="state p-primary ">
              <i class="icon mdi mdi-check" ></i>
              <label :class="`${ans}`">{{ans}}</label>
            </div>
        </div>
      </div>
    </v-card-text>
    <v-card-actions v-if="testStart">
      <v-dialog
        v-model="dialog2"
        width="30%"
        hide-overlay
        transition="dialog-bottom-transition"
      >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          v-bind="attrs" 
          v-on="on"
          text
          color="red accent-4"
          style="font-size: 18px;"
        >
          Learn More
        </v-btn>
      </template>
      <v-card>
      <iframe width="100%" height="500px" :src="dictUrl + quizVoca"></iframe>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="dialog2 = false"> close </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
      <v-btn
        v-if="!ifChecked"
        class="ml-auto"
        style="font-size: 18px;"
        text
        color="blue accent-4"
        @click="checkAnswer"
      >
        채점하기
      </v-btn>
      <v-btn
        v-if="ifChecked && nextVoca"
        class="ml-auto"
        style="font-size: 18px;"
        text
        color="blue accent-4"
        @click="nextQuiz"
      >
        다음 문제
      </v-btn>
      <v-btn
        v-if="ifChecked && !nextVoca"
        class="ml-auto"
        style="font-size: 18px;"
        text
        color="blue accent-4"
        @click="nextQuiz"
      >
        퀴즈 종료
      </v-btn>
    </v-card-actions>
  </v-card>
  </v-container>
  <p style="text-align:center; margin-top: 15px; margin-bottom: 3px; font-size: 16px;">맞은 개수 {{answerCnt}}/ 총 푼 개수 {{allCnt}}</p>
  </div>
</template>

<script>
import axios from "@/plugins/axios";
import "@/../public/css/MyTest.scss";
import Swal from "sweetalert2";
import $ from 'jquery';

export default {
  data() {
    return {
      language: ["All"],
      selectlang: 'All',
      dictUrl: "https://m.dic.daum.net/search.do?q=",
      quizVoca: '',
      answer: '',
      answerLst: [],
      maxIdx: 0,
      nextIdx: 0,
      checkedAns: '',
      ifChecked: false,
      dialog2: false,
      nextVoca: false,
      testCompleted: false,
      answerCnt:0,
      allCnt:0,
      testStart:false,
    };
  },
  watch: {
    selectlang: function () {
      this.selectlanguage();
    },
    // nextIdx: function() {
    //   if (this.nextIdx > this.maxIdx + 1) {
    //     this.nextVoca = false;
    //     this.testCompleted = true;
    //     this.quizVoca = false;
    //   }
    // }
  },
  methods: {
    Start(){
      this.testStart = true;
    },
    End(){
      this.testStart=false;
      this.answerCnt = 0;
      this.allCnt = 0;
      this.nextIdx=1;
      this.nextVoca = true;
      this.testCompleted = false;
      this.quizVoca = true;
      this.selectlanguage();
    },
    async init(){
      await axios.get(`user/init-quiz`);
    },
    async selectlanguage() {
      await this.init();
      console.log("lang? = = " + this.selectlang)
      // if (this.nextIdx <= this.maxIdx) {
      //   this.nextVoca = false;
      // } else if (this.nextIdx > this.maxIdx) {
      //   this.nextVoca = true;
      // }
      if (this.selectlang == 'All') {
        await axios.get(
        `user/myvoca-quize?index=0`
      ).then(res => {
        if (res.data.result == 'not exist') {
            console.log('data == not exist??')
            this.testCompleted = true;
        } else {
        console.log('여기 퀴즈 전체로 mounted됐을때');
        console.log(res.data.result);
        var quiz = res.data.result;
        this.quizVoca = quiz.problem;
        this.answerLst = quiz.answer_list;
        this.answer = quiz.answer;
        this.maxIdx = quiz.maxIdx;
        this.allCnt = 0;
        this.nextIdx = 1;
          }
        })
      } else {
        await axios.get(
          `user/myvoca-quize?country=${this.selectlang}&index=0`
        ).then(res => {
          if (res.data.result == 'not exist') {
              this.testCompleted = true;
          } else {
          console.log('여기 퀴즈 특정언어로 mounted됐을때');
          console.log(res.data.result);
          var quiz = res.data.result;
          this.quizVoca = quiz.problem;
          this.answerLst = quiz.answer_list;
          this.answer = quiz.answer;
          this.maxIdx = quiz.maxIdx;
          this.allCnt = 0;
          this.nextIdx = 1;
            }
          })
        }
      },
    checkAnswer() {
      this.ifChecked = true;
      if (this.nextIdx > this.maxIdx) {
        this.nextVoca = false;
      } else if (this.nextIdx <= this.maxIdx) {
        this.nextVoca = true;
      };
      if (this.checkedAns == this.answer) {
        $(`#${this.answer}`).addClass("p-success");
        this.answerCnt++;
        this.allCnt++;
        Swal.fire({
            width: 430,
            text: "정답입니다!",
            timer: 1675,
            icon: "success",
            iconColor: "red",
            showConfirmButton: false,
          });
      } else {
        this.checkedAns = this.answer;
        $(`#${this.checkedAns}`).addClass("p-danger");
        this.allCnt++;
        Swal.fire({
          width: 430,
          text: "😢 틀렸습니다.",
          timer: 1675,
          icon: "error",
          iconColor: "red",
          showConfirmButton: false,
        });
      }
    },
    nextQuiz() {
      $(`#${this.checkedAns}`).removeClass("p-success");
      $(`#${this.checkedAns}`).removeClass("p-danger");
      this.checkedAns = '';
      this.ifChecked = false;

      console.log("nextIdx " + this.nextIdx);
      console.log("maxIdx " +this.maxIdx);
    
      if (this.nextIdx <= this.maxIdx) {
        if (this.selectlang == 'All') {
          console.log('다음퀴즈// nextIdx ' + this.nextIdx);
          axios.get(`user/myvoca-quize?index=${this.nextIdx}`)
          .then(res => {
            console.log(res.data.result);
            if (res.data.result == 'not exist') {
              console.log(res.data.result);
              this.testCompleted = true;
            } else {
            var quiz = res.data.result;
            this.quizVoca = quiz.problem;
            this.answerLst = quiz.answer_list;
            this.answer = quiz.answer;
            this.maxIdx = quiz.maxIdx;
            this.nextIdx += 1;
            }
          })
        } else {
          axios.get(`user/myvoca-quize?country=${this.selectlang}&index=${this.nextIdx}`
          ).then(res => {
            if (res.data.result == 'not exist') {
              console.log(res.data.result);
              this.testCompleted = true;
            } else {
            var quiz = res.data.result;
            this.quizVoca = quiz.problem;
            this.answerLst = quiz.answer_list;
            this.answer = quiz.answer;
            this.maxIdx = quiz.maxIdx;
            this.nextIdx += 1;
            }
          })
        }
      } else if (this.nextIdx > this.maxIdx) {
        this.nextVoca = false;
        this.testCompleted = true;
        const fd = new FormData();
        fd.append("all_cnt", this.allCnt);
        fd.append("answer_cnt", this.answerCnt);
        fd.append("country", this.selectlang);
        axios.post("/user/end-quiz", fd)
        .then((res)=>{
          console.log(res);

        });
       }
    },
  },
  mounted() {
    axios.get(
        `user/myvoca-country`
      ).then(res => {
        var lang = res.data;
        console.log(res.data);
        for (var i=0; i < lang.length; i++) {
          this.language.push(lang[i]);
        }
        console.log(this.language);
      if (lang.length != 0) {
         this.selectlanguage();
      }
      })
    // axios.get(
    //     `user/myvoca-quize?index=0`
    //   ).then(res => {
    //     console.log('여기 퀴즈 mounted됐을때')
    //     console.log(res.data.result);
    //     var quiz = res.data.result;
    //     this.quizVoca = quiz.problem;
    //     this.answerLst = quiz.answer_list;
    //     this.answer = quiz.answer;
    //     this.maxIdx = quiz.maxIdx;
    //     this.nextIdx = 1;
    //     });

    },
  updated() {
    console.log('여기는 updated// nextIdx ' + this.nextIdx);
  },
};
</script>


<style>
*{margin:0;padding:0;box-sizing:border-box;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;}
#container{
  height:230px;
}

.button-4{
  width:250px;
  height:50px;
  border:2px solid black;
  float:left;
  text-align:center;
  cursor:pointer;
  position:relative;
  box-sizing:border-box;
  overflow:hidden;
  /* margin:0 0 40px 50px; */
}
.button-4 a{
  font-family:arial;
  font-size:22px;
  color: #FFCDD2;
  text-decoration:none;
  line-height:50px;
  transition:all .5s ease;
  z-index:2;
  position:relative;
}
.eff-4{
  width:140px;
  height:50px;
  left:-140px;
  background: #FFCDD2;
  position:absolute;
  transition:all .5s ease;
  z-index:1;
}
.button-4:hover .eff-4{
  left:0;
}
.button-4:hover a{
  color:#fff;
}
footer{
  position:absolute;
  width:100%;
  height:30px;
  border-top:1px solid #34495e;
  bottom:0;
  display:none;
}
</style>