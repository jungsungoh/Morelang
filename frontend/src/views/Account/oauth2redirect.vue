<template>
    <div>

    </div>
</template>

<script>
import axios from "@/plugins/axios";
export default {
    name: "oauth2redirect",
    created() {
        console.log("create oauth2redirect");
        console.log(this.$route.query)
        const code = this.$route.query.code;
        if (code) {
            const fd = new FormData();
            fd.append("code", code);
            axios
                .post("/newuser/naverlogin", fd)
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
        }
        else {
            this.$router.replace("/")
        }
    }
}
</script>

<style scoped>

</style>