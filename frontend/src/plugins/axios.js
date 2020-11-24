import axios from 'axios'
import router from "@/router";
import store from "@/store";
const instance = axios.create({
    baseURL: 'https://morelang.gq/api'
  });

/*
    1. 요청 인터셉터
    2개의 콜백 함수를 받습니다.
*/
instance.interceptors.request.use(
    function (config) {
        // 요청 성공 직전 호출됩니다.
        // axios 설정값을 넣습니다. (사용자 정의 설정도 추가 가능)
        return config;
    }, 
    function (error) {
        // 요청 에러 직전 호출됩니다.
        return Promise.reject(error);
    }
);

/*
    2. 응답 인터셉터
    2개의 콜백 함수를 받습니다.
*/
instance.interceptors.response.use(
    function (response) {
    /*
        http status가 200인 경우
        응답 성공 직전 호출됩니다. 
        .then() 으로 이어집니다.
    */
        return response;
    },

    function (error) {
        console.log(error);
        if (401 === error.response.status) {
            console.log("401 error 발생!")
            window.localStorage.removeItem('vuex');
            axios.post(`/user/out`).then(()=>{
            });
            store.commit("LOGOUT");
            store.commit("ShowAlert", {
                flag: true,
                msg: "로그인이 필요합니다.",
                color: "info",
            });
            setTimeout(() => {
                store.commit("ShowAlert", {
                    flag: false,
                    msg: "",
                });
                window.location.reload();
            }, 1000);
            axios.defaults.headers.common.Authorization = null;
            router.push({name: "Login"})
        }
        return error;
    }
);


// 생성한 인스턴스를 익스포트 합니다.
export default instance;
