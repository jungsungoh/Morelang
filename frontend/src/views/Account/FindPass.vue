<template>
  <v-container my-16>
    <v-card flat class="mx-auto" max-width="50vw">
      <v-card-title class="title font-weight-regular">
        <h1>비밀번호 찾기</h1>
      </v-card-title>
      <v-card-text v-if="step === 1" class="pb-0">
        <span class="caption grey--text text--darken-1">가입시 등록한 이메일을 입력해주세요</span>
        <v-text-field
          @keypress.enter="findPassReq()"
          label="이메일"
          v-model="username"
        ></v-text-field>
        <v-progress-linear v-show="loading" indeterminate color="primary"></v-progress-linear>
      </v-card-text>
      <v-card-text v-if="step === 2" class="pb-0">
        <h3>{{ username }} 으로 임시 비밀번호가 전송되었습니다. 다시 로그인해주세요</h3>
      </v-card-text>
      <v-card-text class="py-0">
        <small class="red--text">{{ errorMsg }}</small>
      </v-card-text>
      <v-card-actions class="px-4">
        <v-spacer></v-spacer>
        <v-btn tile v-if="step === 1" color="info" depressed @click="findPassReq()">다음</v-btn>
        <v-btn
          tile
          v-if="step === 2"
          color="primary"
          depressed
          @click="$router.push({ name: 'Home' })"
          >완료</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import axios from "@/plugins/axios";
import { mapMutations } from "vuex";

export default {
  name: "FindPass",
  data() {
    return {
      step: 1,
      username: null,
      errorMsg: null,
      loading: false
    };
  },
  methods: {
    ...mapMutations(["ShowAlert"]),
    async findPassReq() {
      this.errorMsg = null;
      if (!this.username) {
        this.errorMsg = "이메일을 입력해주세요";
      } else {
        try {
          this.loading = true;
          const { data } = await axios.get(`/newuser/find_pw`, {
            params: {
              username: this.username
            }
          });
          if (data) {
            this.step = 2;
          } else {
            this.errorMsg = "유효하지 않은 이메일입니다.";
          }
        } catch (e) {
          console.error(e);
          this.errorMsg = "서버응답이 없습니다. 다시 시도해주세요";
        }
        this.loading = false;
      }
    }
  }
};
</script>

<style></style>
