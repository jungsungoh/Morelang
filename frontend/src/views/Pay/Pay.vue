<template>
  <div>
    <div style="margin-top: 250px; margin-left: 250px;">
      <h1>{{item_name}} 포인트({{item_name/10}}원) 결제를 진행합니다.'</h1>
    </div>
  </div>
</template>

<script>
import axios from "@/plugins/axios";

export default {
  components: {},
  data() {
    return {
      item_name: '',
      total_amount: 0,
      vid : null,
    };
  },
  mounted() {
    console.log(this.$route)
    console.log(this.$route.params.vid);
    this.vid = this.$route.params.vid;
    this.item_name = this.$route.params.point;
    this.total_amount = this.$route.params.point/10;
    if(this.vid != undefined){
    axios.get(`/user/pay/ready?item_name=${this.item_name}포인트&total_amount=${this.total_amount}&id=${this.vid}`).then(res => {
      this.$store.commit('setPayReady', res.data);
      window.open(res.data.next_redirect_pc_url, "_blank", 'width=450, height=500');
    });
    }else{
      axios.get(`/user/pay/ready?item_name=${this.item_name}포인트&total_amount=${this.total_amount}`).then(res => {
      this.$store.commit('setPayReady', res.data);
      window.open(res.data.next_redirect_pc_url, "_blank", 'width=450, height=500');
    });
    }
  },
  methods: {
  }
};
</script>

<style scoped>
.title {
  color: black;
}
</style>