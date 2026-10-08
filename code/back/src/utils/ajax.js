import axios from 'axios';
import { Notification, MessageBox } from 'element-ui';
import router from '@/router';
import { Loading } from 'element-ui';


// 添加请求拦截器
axios.interceptors.request.use(function(config) {

    // axios.defaults.timeout=6000
    let loadingInstance1 = Loading.service({ fullscreen: true });
    const Authorization = localStorage.getItem('Authorization');
    if (Authorization) { // 判断是否存在token，如果存在的话，则每个http header都加上token
        config.headers.Authorization = Authorization;  //请求头加上token
    }
    if (config.url.includes('uploadGoodsPhoto')) {
        // config.headers.contentType = 'multipart/form-data';
    }
    return config;
}, function(error) {
    Notification.error({
        title: '请求失败'
    });
    // 对请求错误做些什么
    return Promise.reject(error);
});


// 添加响应拦截器
axios.interceptors.response.use(function(res) {
    // 对响应数据做点什么
    let loadingInstance2 = Loading.service({ fullscreen: true });
    loadingInstance2.close();
    if (res.status == 200) {

        if (res.data.code == 402) {
            Notification.error({
                title: res.data.message
            });
        }
        if (res.data.code == 411) {
            Notification.info({
                title: res.data.message
            });
        }
        if (res.data.code == 200) {
            Notification.success({
                title: res.data.message
            });
            return res;
        }
    }
}, function(error) {
    let loadingInstance2 = Loading.service({ fullscreen: true });
    loadingInstance2.close();
    // 当响应异常时
    let isTimeout = error.toString().includes('timeout');
    if (isTimeout) {
        Notification.error({
            title: '请求超时'
        });
    }
    let isLogTimeout = error.toString().includes('401');
    if (isLogTimeout) {
        // 登录超时, 跳转至登录界面
        Notification.error({
            title: '请重新登录'
        });
        router.push('/login');

    }
    let noPower = error.toString().includes('403');
    if (noPower) {
        //提示无权操作
        Notification.error({
            title: '无权操作'
        });
    }
    let systemError = error.toString().includes('500');
    if (systemError) {
        // 提示系统错误
        Notification.error({
            title: '系统错误'
        });
    }
    return Promise.reject(error);

    // 对响应错误做点什么
    return Promise.reject(error);
});


export const post = (name = '', data = {}) => {
    const url = `${name}`;

    return axios({
        method: 'POST',
        url,
        data,
        timeout: 300000
    });
};

export const upload = (name = '', data = {}) => {
    const url = `${name}`;

    return axios({
        method: 'POST',
        url,
        data,
        timeout: 300000,
        headers: { 'Content-Type': 'multipart/form-data' }
    });
};

export const get = (url) => {
    return axios({
        method: 'get',
        url: `${url}`,
        timeout: 300000
    });
};


// export default post
