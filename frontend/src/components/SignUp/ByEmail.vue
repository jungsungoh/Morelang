<template>
  <div>
    <v-card-title class="pt-2">
      <h5>고객님의 소중한 정보를 입력해주세요 :)</h5>
    </v-card-title>
    <v-card-text>
      <v-form ref="form" lazy-validation>
        <v-card-actions class="px-0">
          <v-text-field
            @keyup="checkEmail"
            type="email"
            :rules="emailRules"
            v-model="userData.username"
            label="이메일"
            required
          ></v-text-field>
          <v-btn style="margin-left: 1% !important" class="ml-auto" rounded outlined @click="getCode()">{{
            codeCheck.button
          }}</v-btn>
        </v-card-actions>
        <v-card-actions class="px-0" v-if="codeCheck.show">
          <v-text-field
            type="text"
            v-model="codeCheck.code"
            label="인증코드"
            required
          ></v-text-field>
          <v-btn class="ml-auto" rounded outlined @click="checkCode()"
            >확인</v-btn
          >
        </v-card-actions>
        <v-card-text class="text-right pa-0">
          <small class="deep-orange--text">{{ codeCheck.error }}</small>
          <small class="info--text" v-if="checkedFlags.code">인증완료</small>
        </v-card-text>
        <v-text-field
          v-model="userData.password"
          label="비밀번호"
          :rules="textRules"
          type="password"
          required
        ></v-text-field>
        <v-text-field
          v-model="passwdcheck"
          :rules="pwRules"
          label="비밀번호(확인)"
          type="password"
          required
        ></v-text-field>
        <v-text-field
          type="text"
          @keyup="checkNickname"
          :rules="nicknameRules"
          v-model="userData.nickname"
          label="닉네임"
          required
        ></v-text-field>
      </v-form>
    </v-card-text>
    <v-card-text class="py-0">
      <small class="deep-orange--text" v-show="errorMsg">{{ errorMsg }}</small>
    </v-card-text>
    <v-card-text>
      <v-btn class="px-0" tile block color="info" @click="signUp()"
        >회원가입</v-btn
      >
    </v-card-text>
  </div>
</template>

<script>
import { mapMutations } from "vuex";
import axios from "@/plugins/axios";
export default {
  name: "ByEmail",
  data() {
    return {
      step: "main",
      userData: {
        password: null,
        nickname: null,
        username: null
      },
      passwdcheck: null,
      // 검사하고있는지를 판단
      chekcingFlags: {
        email: false,
        nickname: false
      },
      // 검사완료가 되었는지를 판단
      checkedFlags: {
        email: false,
        nickname: false,
        code: false
      },
      pwRules: [
        v => !!v || "입력이 필요합니다.",
        v =>
          this.userData.password === v || "비밀번호 확인이 일치하지 않습니다."
      ],
      textRules: [v => !!v || "입력이 필요합니다."],
      errorMsg: null,
      codeCheck: {
        show: false,
        code: null,
        originCode: null,
        error: null,
        button: "인증번호 발송"
      },
    };
  },
  methods: {
    ...mapMutations(["ShowAlert"]),
    checkCode() {
      const { code, originCode } = this.codeCheck;
      if (code !== originCode || !originCode || !code) {
        this.codeCheck.error = "인증코드가 일치하지 않습니다.";
      } else {
        this.checkedFlags.code = true;
        this.codeCheck.error = null;
      }
    },
    async getCode() {
      if (!this.checkedFlags.email || !this.userData.username) {
        this.codeCheck.error = "이메일을 확인해주세요";
      } else {
        this.codeCheck.show = true;
        this.codeCheck.error = null;
        this.codeCheck.button = "인증번호 재발송";
        try {
          const { data } = await axios.get(
            "/newuser/check_email",
            {
              params: {
                reciver: this.userData.username
              }
            }
          );
          this.codeCheck.originCode = data;
        } catch (e) {
          console.error(e);
        }
      }
    },
    checkEmail() {
      if (!this.chekcingFlags.email) {
        this.chekcingFlags.email = true;
        setTimeout(() => {
          axios
            // 되나 확인해야함
            .get("/newuser/checkid", {
              params: {
                username: this.userData.username
              }
            })
            .then(({ data }) => {
              if (data) {
                this.checkedFlags.email = true;
              } else {
                this.checkedFlags.email = false;
              }
            })
            .catch(err => {
              console.error(err);
            });
          this.chekcingFlags.email = false;
        }, 500);
      }
    },
    checkNickname() {
      if (!this.chekcingFlags.nickname) {
        this.chekcingFlags.nickname = true;
        setTimeout(() => {
          axios
            .get("/newuser/checkNickname", {
              params: {
                nickname: this.userData.nickname
              }
            })
            .then(({ data }) => {
              if (data) {
                this.checkedFlags.nickname = true;
              } else {
                this.checkedFlags.nickname = false;
              }
            })
            .catch(err => {
              console.error(err);
            });
          this.chekcingFlags.nickname = false;
        }, 500);
      }
    },
    async signUp() {
      this.$refs.form.validate();
      if (!this.userData.username) {
        this.errorMsg = "이메일을 입력해주세요";
      } else if (!this.checkedFlags.email) {
        this.errorMsg = "해당 이메일은 이미 사용중입니다.";
      } else if (!this.checkedFlags.code) {
        this.errorMsg = "이메일 인증을 완료해주세요";
      } else if (!this.userData.password || !this.passwdcheck) {
        this.errorMsg = "비밀번호와 확인문자를 입력해주세요";
      } else if (this.userData.password !== this.passwdcheck) {
        this.errorMsg = "비밀번호와 확인문자가 일치하지 않습니다.";
      } else if (!this.userData.nickname) {
        this.errorMsg = "닉네임을 입력해주세요";
      } else if (!this.checkedFlags.nickname) {
        this.errorMsg = "해당 닉네임은 이미 사용중입니다.";
      } else {
        try {
          await this.$store.dispatch("SIGNUP", this.userData);
          this.ShowAlert({
            flag: true,
            msg: "회원가입이 완료되었습니다 :)",
            color: "info"
          });
          setTimeout(() => {
            this.ShowAlert({ flag: false, msg: "" });
          }, 2000);
          this.$router.push({ name: "Home" });
        } catch (e) {
          this.ShowAlert({
            flag: true,
            msg: "오류가 발생했습니다. 다시 접근해주세요",
            color: "error"
          });
          setTimeout(() => {
            this.ShowAlert({ flag: false, msg: "" });
          }, 2000);
          console.error(e);
        }
      }
    }
  },
  computed: {
    nicknameRules() {
      const rules = [];
      const rule = v => !!v || "닉네임을 입력해주세요";
      rules.push(rule);
      const rule2 = this.checkedFlags.nickname || "중복된 닉네임입니다.";
      rules.push(rule2);
      return rules;
    },
    emailRules() {
      const rules = [];
      const rule = v => !!v || "메일주소를 입력해주세요";
      rules.push(rule);
      const rule2 = this.checkedFlags.email || "중복된 메일주소입니다.";
      rules.push(rule2);
      return rules;
    }
  }
};
</script>

<style></style>
