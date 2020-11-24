<template>
  <v-container style="margin-top: 15px">
    <h1>스크랩한 문장</h1>
    <br />
    <v-expansion-panels style="width: 70%">
      <v-expansion-panel
        v-for="(sentence, i) in setences"
        :key="i"
        cols="12"
        md="10"
      >
        <v-expansion-panel-header>
          {{ sentence.sentence }}
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          {{ sentence.memo }}
          <!-- <v-btn > -->
          <v-icon
            medium
            style="float: right; color: red"
            @click="delSentence(sentence.scrapId)"
            >mdi-close</v-icon
          >
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-container>
</template>

<script>
import axios from "@/plugins/axios";

export default {
  data() {
    return {
      setences: [],
    };
  },
  mounted() {
    axios.get(`/user/myscrap?direction=ASC&page=0&size=100000`).then((res) => {
      console.log("여기요!");
      console.log(res.data.content);
      this.setences = res.data.content;
    });
  },
  methods: {
    delSentence(sid) {
      axios.delete(`/user/delete-scrap?scrapId=${sid}`).then((res) => {
        axios
          .get(`/user/myscrap?direction=ASC&page=0&size=100000`)
          .then((res) => {
            console.log("여기요!");
            console.log(res.data.content);
            this.setences = res.data.content;
          });
        console.log(res);
        console.log("삭제완료!");
      });
    },
  },
};
</script>

<style scoped>
.title {
  color: black;
}
</style>