<template>
  <v-container style="margin-top: 15px;">      
    <h1 class="mb-2">포인트 충전</h1>
    <p class="ml-1" style="font-size: 15px;">10원당 100포인트로 전환됩니다.</p>
      <v-row no-gutters style="text-align:center; width:98%;">
        <v-col v-for="i in 4" :key="i">
          <v-card
            class="pa-2 selectLang"
            outlined
            tile
            style="position:relative; margin-right: 38px; margin-bottom: 20px;"
            @click="Charge(i)"
          >
            <img @click="Coin(i)" style="width: 80%" :src="require(`@/assets/img/${i}.png`)" alt="coin">
            <p class="my-auto">{{ 10000 * i }} 포인트</p>
            <p class="my-auto">{{ 1000 * i }} 원</p>
          </v-card>
        </v-col>
      </v-row>
    <h1 class="mt-5 mb-2">포인트 이용내역</h1>
      <v-data-table
        :headers="headers"
        :items="point"
        :items-per-page="5"
        class="elevation-1 mr-5"
        no-data-text="😢 이용내역이 없습니다 😢"
      >
      <template v-slot:[`item.date`]="{ item }">
        <span>{{ changeDate(item.chargeTime) }}</span>
      </template>
      <template v-slot:[`item.charge`]="{ item }">
        <span v-if = "item.charge===true">충전</span>
        <span v-else-if= "item.charge==false">사용</span>
      </template>
      <template v-slot:[`item.usedpoint`]="{ item }">
        <span class="ml-auto" v-if = "item.charge===true"> <v-icon size="17" style="color: #EF5350;">mdi-plus-circle-outline</v-icon> {{item.chargeAmount}} P</span>
        <span v-else-if= "item.charge==false"> <v-icon size="17" style="color: #64B5F6">mdi-minus-circle-outline</v-icon> {{item.chargeAmount}} P</span>
      </template>
      <template v-slot:[`item.restpoint`]="{ item }">
        <span>{{ item.remainPoint }} P</span>
      </template>
      </v-data-table>
      
  </v-container>
</template>

<script>
import axios from "@/plugins/axios";

export default {
  data() {
    return {
      degrees: 0,
      point: [],
      headers: [
        {
          text: '',
          align: 'center',
          sortable: false,
          value: 'name',
        },
        { text: '날짜', value: 'date' },
        { text: '구분', value: 'charge' },
        { text: '이용 포인트', value: 'usedpoint' },
        { text: '남은 포인트', value: 'restpoint' },
      ],
    };
  },
  methods: {
    Charge(point) {
        //  console.log(point);
         this.$router.push({ name: "Pay", params: { point: point * 10000} });
    },
    Coin(index) {
      var Coin = document.getElementById(index);
      // var degrees = 0;
      // Coin.onclick = function() {
      this.degrees += 1800;
      // console.log(this.degrees);
      Coin.style.webkitTransform = "rotateY(" + this.degrees + "deg)";
      Coin.style.MozTransform = "rotateY(" + this.degrees + "deg)";
      Coin.style.msTransform = "rotateY(" + this.degrees + "deg)";
      Coin.style.OTransform = "rotateY(" + this.degrees + "deg)";
      Coin.style.transform = "rotateY(" + this.degrees + "deg)";
         // };
         // Coin = null;
    },
    changeDate(strDate){
      var sDate = new Date(strDate);
      var result = "" + sDate.getFullYear() + "/"+(sDate.getMonth()+1) +"/"+ sDate.getDate() + " ";
      if(sDate.getHours() >= 13){
        result += ("PM 0" + (sDate.getHours()-12))
      }else{
        result += "AM " + sDate.getHours();
      }
      result += ":" + sDate.getMinutes();
      return result;
    }
  },
  mounted() {
    axios.get(
      `/user/pay/my-pointlogs?direction=DESC&page=0&size=300`
    )
  .then(res => {
    this.point = res.data.content;
    })
  }
};
</script>


<style>
/* body { */
  /* -webkit-transform: perspective(500px); */
  /* -webkit-transform-style: preserve-3d; */
/* } */

.coin {
  /* background-image: url("http://coins.thefuntimesguide.com/images/blogs/presidential-dollar-coin-reverse-statue-of-liberty-public-domain.png"); */
  background-size: 100% 100%;
  border-radius: 100%;
  height: 100px;
  margin: 25px auto;
  position: relative;
  width: 100px;
  /* -webkit-transition: 2s linear;
  -webkit-transform-style: preserve-3d; */
}
</style>