<template>
  <div>
    <Navbar />
    <v-container style="margin-top: 8%;">
      <v-row no-gutters style="justify-content: center;">
        <!-- color="transparent" -->
        <v-col cols="12" md="6">
          <!-- <v-card tile :dark="isDark" flat class="mx-auto" max-width="50vw"> -->
          <v-card tile class="pa-9" height="600px" align="center">
          <v-card-title style="justify-content: center; margin-bottom: 2%;">
              <div class="logo ma-0" style="font-family: 'Do Hyeon', sans-serif;">
                <b><span style="font-size: 60px">SIGNUP</span></b>
              </div>
              <!-- <v-btn fab dark @click="$router.push({ name: 'Home' })">
                <v-icon>mdi-home-outline</v-icon>
              </v-btn> -->
            </v-card-title>
            <!-- <div v-if="step === 'main'">
              <v-card-text>
                <v-btn tile block color="info" @click="step = 'e'"
                  >이메일로 회원가입</v-btn
                >
              </v-card-text>
            </div> -->
            <ByEmail />
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import ByEmail from "@/components/SignUp/ByEmail.vue";
import { mapState } from "vuex";
import Navbar from "@/components/Navbar";

export default {
  name: "SignUp",
  components: {
    ByEmail,
    Navbar
  },
  data() {
    return {
      step: "main"
    };
  },
  computed: mapState(["isDark"]),
  methods: {
    goBack() {
      if (this.step === "main") {
        this.$router.go(-1);
      } else {
        this.step = "main";
      }
    }
  }
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Do+Hyeon&display=swap");
.logo {
  text-align: center; 
  user-select: none;
  margin-top: 10px;
}

.logo b{
  /* font: 300 4vh "Vibur"; */
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
