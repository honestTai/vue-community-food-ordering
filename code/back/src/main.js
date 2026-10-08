import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import VueI18n from 'vue-i18n';
import VueVideoPlayer from 'vue-video-player'
import 'video.js/dist/video-js.css'
import { messages } from './components/common/i18n';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import './assets/css/theme-green/index.css'; // 浅绿色主题
import './assets/css/icon.css';
import './components/common/directives';
import 'babel-polyfill';
import VCharts from 'v-charts'
import Print from 'vue-print-nb'
import operateJs from '../src/components/util/operateJS'

Vue.use(operateJs)
Vue.use(Print);
Vue.config.productionTip = false;
Vue.use(VueI18n);
Vue.use(messages)
Vue.use(ElementUI, {
    size: 'small'
});
Vue.use(VCharts)
const i18n = new VueI18n({
    locale: 'zh',
    messages
});



//使用钩子函数对路由进行权限跳转(订单修改与新能源汽车修改状态判断)
router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | 后台管理`;
    const token = localStorage.getItem('Authorization');
    if (!token && to.path !== '/login' && to.path !== '/registerForm') {
        next('/login');
    } else {
        // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
        if (navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor') {
            Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
                confirmButtonText: '确定'
            });
        } else {
            next();
        }
    }
});

new Vue({
    router,
    i18n,
    render: h => h(App)
}).$mount('#app');
