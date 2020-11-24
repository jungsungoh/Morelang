import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Video from "../views/Video.vue";
import Channel from "../views/Channel.vue";
import Search from "../views/Search.vue";
import Mypage from "../views/Mypage/Mypage.vue";
import SignUp from "@/views/Account/SignUp.vue";
import Login from "@/views/Account/Login.vue";
import FindPass from "@/views/Account/FindPass.vue";
import Oauth2redirect from "@/views/Account/oauth2redirect.vue";
import test from "../components/Video/test.vue";
import Quiz from "../components/Video/Quiz.vue";

import NotFound from '@/components/notFound';

import Pay from "../views/Pay/Pay.vue";
import Approve from "../views/Pay/Approve.vue";
import Fail from "../views/Pay/Fail.vue";

// import Swiper from "../views/Swiper.vue";

Vue.use(VueRouter);

const routes = [{
        path: "/",
        name: "Home",
        component: Home,
    },
    {
        path: "/channel/:id?",
        name: "Channel",
        component: Channel,
    },
    {
        path: "/video/:vid",
        name: "Video",
        component: Video,
    },
    {
        path: "/test",
        name: "test",
        component: test,
    },
    {
        path: "/quiz",
        name: "Quiz",
        component: Quiz,
    },
    {
        path: "/search/:vid?",
        name: "Search",
        component: Search,
    },
    {
        path: "/mypage/:userid?",
        name: "Mypage",
        component: Mypage,
    },
    {
        path: "/pay/pay/:point?",
        name: "Pay",
        component: Pay,
    },
    {
        path: "/pay/approve",
        name: "Approve",
        component: Approve,
    },
    {
        path: "/pay/fail",
        name: "Fail",
        component: Fail,
    },
    {
        path: "/SignUp",
        name: "SignUp",
        component: SignUp
    },
    {
        path: "/Login",
        name: "Login",
        component: Login
    },
    {
        path: '/oauth2/redirect',
        name: 'Oauth2redirect',
        component: Oauth2redirect,
    },
    {
        path: "/FindPass",
        name: "FindPass",
        component: FindPass
    },
    {
        path: '*',
        name: '/NotFound',
        component: NotFound
    }
];

const router = new VueRouter({
    mode: "history",
    base: process.env.BASE_URL,
    routes,
});

export default router;