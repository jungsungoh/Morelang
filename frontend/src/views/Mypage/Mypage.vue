<template>
  <div>
    <Navbar />
    <v-container>
    <div class="mypage" style="padding: 0% 5%; margin-top: 7%;">
      <!-- 유저 정보 -->
      <div style="margin-right: 25px;">
        <v-tabs vertical style="margin: 3% 0%">
          <div style="color:black; margin: 10px 0px; width: 220px;">
           <div class="logo" style="margin-bottom:20px;"><b><span style="font-size:45px;">MyPage</span></b></div>
            <!-- <h1 style="font-size:38px; text-align: center; color: white; margin-bottom:40px;">My page</h1> -->
            <p style="font-size:22px;margin-top:10px; margin-bottom:10px; color:white; text-align:center;">
              {{ this.name }} 님
            </p>
            <p class="points" style="font-size: 15px;color:white; text-align:center;">
              <v-icon size="18px">
              mdi-alpha-p-circle-outline
            </v-icon>
            {{this.point}} 포인트
            </p>
          </div>
          <!-- 탭 -->
          <v-tab class="tabs">
            <v-icon left size="25">
              mdi-youtube
            </v-icon>
            학습영상
          </v-tab>
          <v-tab class="tabs">
            <v-icon left size="25">
              mdi-alphabetical-variant
            </v-icon>
            단어모음
          </v-tab>
          <v-tab class="tabs">
            <v-icon left size="25">
              mdi-file-document-edit-outline
            </v-icon>
            단어테스트
          </v-tab>
          <v-tab class="tabs">
            <i class="fas fa-chart-line fa-1x mr-2"></i>
            <!-- <v-icon left size="25">
              mdi-book-open
            </v-icon> -->
            학습 그래프
          </v-tab>
          <v-tab class="tabs">
            <v-icon left size="25">
              mdi-book-open
            </v-icon>
            문장모음
          </v-tab>
          <v-tab class="tabs">
            <v-icon left size="25">
              mdi-alpha-p-circle-outline
            </v-icon>
            포인트
          </v-tab>

          <v-tab-item>
            <v-card flat>
              <v-card-text style="margin-left:17px">
                <VideoLearning />
              </v-card-text>
            </v-card>
          </v-tab-item>
          <v-tab-item>
            <v-card flat>
              <v-card-text style="margin-left:27px">
                <MyWords />
              </v-card-text>
            </v-card>
          </v-tab-item>
          <v-tab-item>
            <v-card flat>
              <v-card-text style="margin-left:27px">
                <MyTest />
              </v-card-text>
            </v-card>
          </v-tab-item>
           <v-tab-item>
            <v-card flat>
              <v-card-text style="margin-left:27px">
                <line-chart
                  style ="background-color: black; width: 97%; "
                  v-if="loaded"
                  :chartdata="chartdata"
                  :options="options"/>
              </v-card-text>
            </v-card>
          </v-tab-item>
          <v-tab-item>
            <v-card flat>
              <v-card-text style="margin-left:27px">
                <MySentences />
              </v-card-text>
            </v-card>
          </v-tab-item>
          <v-tab-item>
            <v-card flat>
              <v-card-text style="margin-left:27px">
                <MyPoint />
              </v-card-text>
            </v-card>
          </v-tab-item>
        </v-tabs>
      </div>
    </div>
    </v-container>
  </div>
</template>

<script>
import axios from "@/plugins/axios";
import VideoLearning from "@/views/Mypage/VideoLearning";
import MyWords from "@/views/Mypage/MyWords";
import MySentences from "@/views/Mypage/MySentences";
import MyPoint from "@/views/Mypage/MyPoint";
import MyTest from "@/views/Mypage/MyTest";
import Navbar from "@/components/Navbar";
import "@/../public/css/Mypage.scss";
import LineChart from '@/components/Chart.vue'

export default {
  components: {
    Navbar,
    VideoLearning,
    MyWords,
    MySentences,
    MyPoint,
    MyTest,
    LineChart
  },
  data() {
    return {
      name: null,
      tab: null,
      items: {
        영상학습: "mdi-youtube",
        스크랩: "mdi-alphabetical-variant",
        좋아요: "mdi-book-play-outline"
      },
      icons: [
        "mdi-youtube",
        "mdi-alphabetical-variant",
        "mdi-book-play-outline"
      ],
      point: null,
      // graph
        loaded: false,
    chartdata:  {
        datasets: []
    },
    options: {
        responsive: true,
        title: {
            display: true,
            text: '퀴즈 성장률',
            fontColor:"white"
        },
        tooltips: {
            mode: 'index',
            intersect: false,
        },
        hover: {
            mode: 'nearest',
            intersect: true
        },
        scales: {
            xAxes: [{
                display: true,
                ticks: {
                  stepSize : 1,
                  fontColor : "white",
                     },
                scaleLabel: {
                    display: true,
                    labelString: '회차'
                }
            }],
            yAxes: [{
                display: true,
                ticks: {
                        min: 0,
                  max: 100,
                  stepSize : 10,
                  fontColor : "white",
                     },
                scaleLabel: {
                    display: true,
                    labelString: '점수(%)',
                    
                }
            }]
        }
      }
    };
  },
  mounted() {
    this.name = this.$store.state.nickname;
    axios.get(
         "/user/pay/my-point"
      )
      .then(res => {
        this.point = res.data;
      });
    // graph
    this.loaded = false
    var myColor = ['rgba(255, 99, 132, 0.8)',
                'rgba(54, 162, 235, 0.8)',
                'rgba(255, 206, 86, 0.8)',
                'rgba(75, 192, 192, 0.8)',
                'rgba(153, 102, 255, 0.8)',
                'rgba(255, 159, 64,0.8)']
    try {
      var t = 1;
      axios
        .get("/user/quiz-log").
        then((res)=> {
          console.log(res);
          for (const [key,value] of Object.entries(res.data)) {
            var temp = {}
            var sdata=[];
            var k = 1;
            
            for(const [,value2] of Object.entries(value)){
              sdata.push({x: k++,y:value2.accRate})
            }
            temp["label"] = key;
            temp["data"] = sdata;
            temp["fill"] = false;
            temp["lineTension"] = 0;
            temp["borderColor"] = myColor[(t++)%6];
            temp["showLine"] = true;
            this.chartdata.datasets.push(temp);
          }
        })
      console.log(this.chartdata.datasets);
      this.loaded = true
    } catch (e) {
      console.error(e)
    }
  },
  methods: {
    onCharge() {
      alert("충전하시겠습니까?");
      this.$router.push({ name: "Pay" });
    },
  }
};
</script>

<style scoped>
@import url(//fonts.googleapis.com/css?family=Vibur);

@font-face {
    font-family: 'KyoboHand';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-04@1.0/KyoboHand.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
.v-toolbar__content {
  height: 0px !important;
}
.v-tab--active {
  text-decoration: none !important;
  color: black !important;
  background-color: white;
}
.mypageTabs {
  border-top-right-radius: 5px;
  border-top-left-radius: 5px;
}
.v-tabs-slider-wrapper {
  display: none !important;
}

.v-item-group {
  color: black;
}
.v-tabs v-tabs--vertical theme--dark {
  height: 700px;
}
.mypage {
  font-family: 'KyoboHand', sans-serif;
}
.tabs {
  font-size: 15px;
  font-family: 'paybooc-Medium', sans-serif;
}
.points {
  font-family: "Nanum Gothic", sans-serif;
}
.logo {
  text-align: center; 
  user-select: none;
  margin-top: 10px;
}

.logo b{
  font: 300 4vh "Vibur";
  color: #fdd;
  text-shadow: 0 -40px 100px, 0 0 2px, 0 0 1em #ff0000, 0 0 0.5em #ff0000, 0 0 0.1em #ff0000, 0 10px 3px #000;
}
.logo b span{
  animation: blink linear infinite 2s;
}
.logo b span:nth-of-type(2){
  animation: blink linear infinite 3s;
}
</style>