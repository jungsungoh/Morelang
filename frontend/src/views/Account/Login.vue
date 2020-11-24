<template>
  <div>
    <Navbar />
    <v-container style="margin-top: 10%;">
      <v-row no-gutters style="justify-content: center;">
        <v-col cols="12" md="8">
          <v-card tile class="pa-9" height="650px" align="center">
            <v-card-title style="justify-content: center; margin-bottom: 2%;">
              <div class="logo ma-0" style="font-family: 'Do Hyeon', sans-serif;">
                <b><span style="font-size: 60px">LOGIN</span></b>
              </div>
              <!-- <v-btn fab dark @click="$router.push({ name: 'Home' })">
                <v-icon>mdi-home-outline</v-icon>
              </v-btn> -->
            </v-card-title>
            <div style="display: table; width: 100%; margin-bottom: 3.5%;">
              <v-form
                ref="form"
                style="display: table-cell;  width: 45%; padding: 0 30px; border-right: 1px solid rgba(0, 0, 0, 0.1);"
              >
                <v-text-field
                  class="my-3"
                  v-model="loginData.username"
                  placeholder="morelang@naver.com"
                  label="이메일"
                  required
                  color="red"
                  @keypress.enter="onSubmit()"
                ></v-text-field>
                <v-text-field
                  type="password"
                  v-model="loginData.password"
                  label="비밀번호"
                  required
                  color="red"
                  @keypress.enter="onSubmit()"
                ></v-text-field>
                <v-card-text style="padding: 0;">
                  <small class="red--text">{{ errorMsg }}</small>
                  <v-progress-circular
                    indeterminate
                    v-if="loading"
                  ></v-progress-circular>
                </v-card-text>
                <v-card-actions class="px-0 mt-2 mb-3">
                  <v-btn block :color="'red'" tile @click="onSubmit()"
                    >로그인</v-btn
                  >
                </v-card-actions>
                <small
                  style="cursor:pointer;"
                  @click="$router.push({ name: 'FindPass' })"
                  >비밀번호 찾기</small
                >
              </v-form>
              <v-form style="display: table-cell; width: 45%; padding: 0 30px;">
                <!-- <div>
                <hr
                  style="width:41%; display: inline-block;"
                  class="mb-1 mr-3"
                />
                <p style="display: inline-block;">OR</p>
                <hr
                  style="width:41%; display: inline-block;"
                  class="mb-1 ml-3"
                />
              </div> -->
                <!-- 소셜 로그인 -->
                <v-row justify="center">
                  <v-container style="text-align: center;">
                    <v-row no-gutters>
                      <v-col class="mb-5" cols="12" sm="12">
                        <v-card
                          class="pa-2"
                          outlined
                          tile
                          @click="GoogleLogin()"
                        >
                          <img
                            style="width:26px; float:left"
                            src="@/assets/img/google.png"
                            alt=""
                          />
                          <p class="my-auto" style="display:inline-block; ">
                            Sign up with Google
                          </p>
                        </v-card>
                      </v-col>
                      <v-col class="mb-5" cols="12" sm="12">
                        <a class="naverUrl" :href="naverUrl">
                          <v-card
                            class="pa-2"
                            outlined
                            tile
                            @click="NaverLogin()"
                          >
                            <img
                              style="width:26px; float:left"
                              src="@/assets/img/naver.png"
                              alt=""
                            />
                            <p class="my-auto">Sign up with naver</p>
                          </v-card>
                        </a>
                      </v-col>
                      <v-col class="mb-5" cols="12" sm="12">
                        <v-card
                          class="pa-2"
                          outlined
                          tile
                          @click="KakaoLogin()"
                        >
                          <img
                            style="width:26px; float:left"
                            src="@/assets/img/kakao.png"
                            alt=""
                          />
                          <p class="my-auto">Sign up with kakao</p>
                        </v-card>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-row>
              </v-form>
            </div>

            <div style="display: table; width: 95%; border: 1px solid #ccc; padding: 20px 47px">
              <div style="display: table-cell; width: 45%;position: relative;">
                <div style="font-size: 22px; color: #777; text-align: left; position: absolute; top: 35%;">
                  아직 회원이 아니신가요?
                </div>
              </div>
              <div style="display: table-cell; width: 45%;">
                <div style="text-align: center; font-size: 15px; color: #777; padding-bottom: 20px;">
                  지금 회원가입을 하시면 다양한 영상 및<br> 다국어 학습 참여가 가능하며
                  회원가입 <br>포인트를 받으실 수 있습니다.
                </div>
                <div style="padding-bottom: 20px; text-align: center; ">
                  <v-btn class="logo" @click="toRegister()" style="width: 50%; border: 1px solid #282d41; background: #333; font-size: 14px; font-weight: 500; padding: 20px 35px; border-radius: 25px;">
                    <b><span>
                    <v-icon style="margin-right: 5%;">
                      mdi-account-circle-outline
                    </v-icon>
                    회원가입
                    </span></b>
                  </v-btn>
                </div>
              </div>
              <!-- <v-card-actions class="px-0 my-3">
                <v-btn block :color="'primary'" tile @click="toRegister()"
                  >회원 가입</v-btn
                >
              </v-card-actions> -->
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script src="https://apis.google.com/js/platform.js"></script>
<script>
import Navbar from "@/components/Navbar";
import { mapState } from "vuex";
import axios from "@/plugins/axios";
export default {
  name: "Login",
  components: {
    Navbar
  },
  data() {
    return {
      loginData: {
        username: this.$route.query.username,
        password: null
      },
      errorMsg: null,
      showMenu: false,
      loading: false,
      gauth: {},
      naverUrl: ""
    };
  },
  methods: {
    async onSubmit() {
      console.log("click");
      if (!this.loginData.username || !this.loginData.password) {
        this.errorMsg = "이메일 또는 비밀번호를 입력해주세요";
      } else {
        this.errorMsg = null;
        this.loading = true;
        try {
          await this.$store.dispatch("LOGIN", this.loginData);
          if (this.$store.state.formerLink) {
            this.$router.push(this.$store.state.formerLink);
          } else {
            this.$router.push({ name: "Home" });
          }
        } catch (e) {
          this.errorMsg = "이메일 또는 비밀번호를 확인해주세요";
          console.error(e);
        }
        this.loading = false;
      }
    },
    toRegister() {
      this.$router.push({ name: "SignUp" });
    },
    toFindId() {
      this.$router.push({ name: "FindId" });
    },
    toFindPw() {
      this.$router.push({ name: "FindPass" });
    },
    async GoogleLogin() {
      await gapi.load("auth2", () => {
        this.gauth = gapi.auth2.init({
          client_id:
            "258439612277-a2k3f6ro1jvdkbois85pt4cngrs6hctk.apps.googleusercontent.com"
        });
        this.gauth.then(
          function() {
            console.log("init success");
          },
          function() {
            console.error("init fail");
          }
        );
        this.Glogin();
      });
    },
    async Glogin() {
      await this.gauth.grantOfflineAccess().then(data => {
        console.log(data.code);
        const fd = new FormData();
        axios.defaults.headers.common.Authorization = ``;
        fd.append("code", data.code);
        fd.append("redirect", window.location.href);
        axios
          .post("/newuser/googlelogin", fd)
          .then(response => {
            console.log("성공!");
            console.log(response);
            this.$store.dispatch("SocialLogin", response.data);
            if (this.$store.state.formerLink) {
              this.$router.push(this.$store.state.formerLink);
            } else {
              this.$router.push({ name: "Home" });
            }
          });
      });
    },
    KakaoLogin() {
      window.Kakao.Auth.loginForm({
        success: this.GetMe
      });
    },
    GetMe(authObj) {
      console.log(authObj);
      const fd = new FormData();
      fd.append("accessToken", authObj.access_token);
      axios
        .post(`/newuser/kakaologin`, fd)
        .then(response => {
          console.log("성공!");
          console.log(response);
          this.$store.dispatch("SocialLogin", response.data);
          if (this.$store.state.formerLink) {
            this.$router.push(this.$store.state.formerLink);
          } else {
            this.$router.push({ name: "Home" });
          }
        });
    },
    NaverLogin() {
      var clientId = "YOwQRknl_3ldIN3E5UQN"; //애플리케이션 클라이언트 아이디값";
      var redirectURI = "https://morelang.gq";
      var apiURL =
        "https://nid.naver.com/oauth2.0/authorize?response_type=code";
      apiURL += "&client_id=" + clientId;
      apiURL += "&redirect_uri=" + redirectURI + "/oauth2/redirect";
      apiURL += "&state=" + 234235;
      this.naverUrl = apiURL;
    }
  }
};
</script>
<style scoped>
@import url(//fonts.googleapis.com/css?family=Vibur);

@import url("https://fonts.googleapis.com/css2?family=Do+Hyeon&display=swap");
.p_page {
  margin-top: "200vh";
}
.loginPage {
  text-align: center !important;
}
.naverUrl {
  text-decoration: none;
}
.logo {
  text-align: center; 
  user-select: none;
  margin-top: 10px;
}

.logo b{
  /* font: 300 4vh "Vibur"; */
  color: #fdd;
  text-shadow: 0 -40px 100px, 0 0 2px, 0 0 1em #ee6f6f, 0 0 0.5em #f17474, 0 0 0.1em #ff0000, 0 10px 3px #000;
}
.logo b span{
  animation: blink linear infinite 2s;
}
.logo b span:nth-of-type(2){
  animation: blink linear infinite 3s;
}
</style>