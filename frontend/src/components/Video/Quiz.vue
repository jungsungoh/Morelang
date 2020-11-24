<template>
  <div class="code-box mx-auto" @dragover="dragover">
    <!-- 퀴즈 Content -->
    <div class="play-box mx-auto mt-5">
      <div style="display:inline-block" class="pr-1" v-for="(item, i) in quizBox" :key=i>
        <div v-if="item.quiz!='blank'" style="margin-bottom: 10px; color: white; font-size:16px;">
          {{item.quiz}}
        </div>
        <div 
          @dragover="ondragover(`b${item.index}`)" v-else :id="`blank${item.index}`" :class="`b${item.index}`" class="blank droppable" @drop="drop(item.index)">
        </div>
      </div>
    </div>
    <!-- 퀴즈 키워드 -->
    <div v-if="nowText" class="block-box">
      <div class="block-list mt-2 droppable" @drop="drop">
        <div class="droppable">
          <div v-for="(keyword, i) in keyword"  @drop="drop" draggable="true" @dragstart="dragstart(keyword.original, i)" :key=i :id="`keyword${i}`" :class="`k${i}`" style="display: inline-block;">
            <div class="block">
              {{keyword.key}}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import axios from "axios";
import axios from "@/plugins/axios";
import $ from 'jquery';
import Swal from "sweetalert2";

export default {
  name: 'Quiz',
  data() {
    return {
      // quiz
      nowText: '',
      quizBox: [],
      keyword: [],
      blankSize: '',
      answer: {},
      score: 0,
      userAns: 0,
      rightAns: [],
      keyIdx: '',
      keyIdxWidth: 0,
      keywordWidth: {},
      // drag
      blankIdx: '',
      idx: '',
      isMove: true,
      isObstacle: false,
      distX: '',
      distY: '',
      targetClass: '',
      targetClass2: '',
      targetNum: '',
      targetFlag: false,
      items: {
        block0: 1, block1: 1, block2: 1, block3: 1, block4: 1, block5: 1, block6: 1
      },
      onQuiz: [],
      classId: 'a',
    }
  },
  created() {
    // window.addEventListener('scroll', this.handleScroll)
  },
  mounted() {
    this.nowText = this.$store.state.videoText;
    this.whenMounted();
    console.log('이거 mounted');
  },
  watch: {
    nowText: function() {
      console.log('이건 안되냐고');
      console.log(this.nowText);
      // this.whenMounted();
    },
    score: function () {
      this.checkAnswer();
      // console.log(this.score);
    },
  },
  methods: {
    whenMounted() {
      this.onMove();
      console.log('여기는 quiz'+this.nowText);
      this.nowText = 'Именительный падеж, или первый падеж этих прилагательных. Если вы еще не умеете';
      if (this.nowText) {
        axios.post(
          `https://morelang.gq/api/newuser/puzzletest?inputText=${this.nowText}`  
          ).then(res => { 
            this.answer = res.data.answer;
            console.log('이게 res.data')
            console.log(res.data);
            // this.quizBox = res.data.quizeText;
            var quizInput = res.data.inputTextArray;
            this.keyword = res.data.keyword;
            var j = 1;
            for (var i=0; i<quizInput.length; i++) {
              if (quizInput[i] === '______') { 
                  this.quizBox.push({index: j++, quiz: 'blank'});
              } else if (quizInput[i].startsWith('______')) {
                  this.quizBox.push({index: j++, quiz: 'blank'})
                  this.quizBox.push({index: 0, quiz: quizInput[i].slice(6)})
              } else {
                this.quizBox.push({index: 0, quiz: quizInput[i]})    
              }
            }
        })
      }
    },
    checkAnswer() {
      if (this.score === Object.keys(this.keyword).length) {
        Swal.fire(
          {
            // title: "정답!",
            width: 500,
            background: '#fff url(@/assets/img/answer.gif)',
            text: "정답입니다!",
            timer: 1700,
            icon: "success",
            iconColor: 'red',
            showConfirmButton: false,
          })
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
      // 두번째 드래그할 때, class 제거
      // if (!$(`#blank${idx}`).hasClass("checked")) {
      //   this.blankIdx.classList.remove("checked");
      // };
      // console.log('드래그 시작, keyIdx 생김');
      // console.log(this.keywordWidth);
      // event.target.style.position = 'absolute';
      let posX = event.pageX;
      let posY = event.pageY;
      this.distX = event.srcElement.offsetLeft - posX;
      this.distY = event.srcElement.offsetTop - posY;
      event.target.classList.add(`${this.classId}`)
      this.classId += '0'
      this.targetClass = event.target.classList[2]
      this.targetClass2 = event.target.classList[1]
      // console.log(event.target)
      // 키워드 
      this.userAns = ans;
    },
    ondragover(idx) {
      // 사이즈 변경
      // idx width를 this.keyIdx로 바꾸기
      // console.log('키워드 클래스'+ this.keyIdx);
      
      var bIdx = idx.slice(1);
      this.blankIdx = $(`#blank${bIdx}`)
      
      // 너비 유지
      // document.getElementById(`blank${bIdx}`);
      // console.log('blanKinDX')
      // console.log(this.blankIdx);
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
      // console.log(this.keywordWidth[[this.keyIdx]])
      // 정답이 아니면 틀린 표시
      if (idx !== this.userAns) {
        $(`.b${idx}`).css("border", "red solid 2px");
        Swal.fire(
          {
            width: 320,
            text: "😢 다시 생각해보세요! 😢",
            timer: 1550,
            background: 'white',
            // icon: "success",
            showConfirmButton: false,
          })
    }
      // 정답처리
      // idx: 빈칸이 몇번째 칸인지
      if (idx === this.userAns && !this.rightAns.includes(idx)) {
        this.score += 1;
        // console.log(this.score);
        this.rightAns.push(idx);
        // 드롭
        // 클래스 추가
        var idIdx = 'keyword' + this.keyIdx.slice(1);
        var keyId = document.getElementById(idIdx);
        // console.log(keyId);
        this.blankIdx = document.getElementById(`blank${idx}`);
        if (!$(`#blank${idx}`).hasClass("checked")) {
          this.blankIdx.appendChild(keyId);
        };
        this.blankIdx.classList.add('checked');
        // if (posX >= 300 && posX <= 1450) {
          // if (posY >= 113 && posY <= 520) {

      if(event.target.classList && event.target.classList.contains("droppable")){
          const CLONE = document.querySelectorAll(`.${this.targetClass2}`)
          for (let i=0; i<CLONE.length; i++) {
            if (CLONE[i].classList.length == 2) {
              this.targetFlag = false;
            } else {
              this.targetFlag = true;
            }
          }
        };
      }
    }
  },
}
</script>

<style scoped>

/* .code-block-container {
  display: flex;
  width: 100%;
  text-align: center;
} */
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
  transition: background-color .5s ease;
}
 .block-list .block {
  padding: 2px 7px;
  margin-right: 8px;
  border-radius: 3px;
  background-color: #D32F2F;
  margin-bottom: 10px;
  cursor: pointer;
  color: white;
  font-size: 14px;
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
</style>
