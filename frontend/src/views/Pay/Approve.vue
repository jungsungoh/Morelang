<template>
<div>
</div>
</template>

<script>
import axios from "@/plugins/axios";


export default {
  components: {},
  data() {
    return {
      pg_token: null,
      payReady: null,
      id: null,
    };
  },
  mounted() {
    this.id = this.$route.query.id;
    this.pg_token = this.$route.query.pg_token;
    this.payReady = this.$store.state.payReady;
    console.log(this.payReady)
    axios.get(`/user/pay/approve?tid=${this.payReady.tid}&pg_token=${this.pg_token}&total_amount=${this.payReady.total_amount}`).then(res => {
      console.log(res)
      if (this.id != null)
        window.opener.location.href = "https://morelang.gq/video/" + this.id;
      else
        window.opener.location.href = "https://morelang.gq/mypage";
      window.close()
    });
  },
  methods: {
  }
}
</script>

<style scoped>
</style>