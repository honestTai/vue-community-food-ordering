import Vue from 'vue';
import Router from 'vue-router';

//路由
Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/home'
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: { title: '首页' },
            children: [
                {
                    path: '/home',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/home/Home.vue'),
                    meta: { title: '统计分析' }
                },
                {
                    path: '/classList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/class/list'),
                    meta: { title: '分类管理' }
                },
                {
                    path: '/goodsList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/goods/goodsList'),
                    meta: { title: '餐品列表' }
                },
                {
                    path: '/goodsOperation',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/goods/goodsOperation'),
                    meta: { title: '餐品编辑' }
                },
                {
                    path: '/userList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/user/userList'),
                    meta: { title: '用户管理' }
                },
                {
                    path: '/orderList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/order/orderList'),
                    meta: { title: '订单列表' }
                }
            ]
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../components/page/login/Login.vue'),
            meta: { title: '登录' }
        }
    ]
});
