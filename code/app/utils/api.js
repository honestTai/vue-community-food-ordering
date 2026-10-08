import __config from '../config/env';
const request = (url, method, data, showLoading) => {
    let _url = __config.basePath + url;
    return new Promise((resolve, reject) => {
        if (showLoading) {
            uni.showLoading({
                title: '加载中'
            });
        }
        uni.request({
            url: _url,
            method: method,
            data: data,
            header: {
                Authorization: localStorage.getItem('thirdSession') != null ? localStorage.getItem('thirdSession') : ''
            },
            success(res) {
                if (res.statusCode == 200) {
                    if (res.data.code != 200) {
                        console.log(res.data);
                        uni.showModal({
                            title: '提示',
                            content: res.data.message ? res.data.message : '没有数据' + '',
                            success() {},
                            complete() {
                                if (res.data.code == 60001) {
                                    //session过期，则清除过期session，并重新加载当前页
                                    getApp().globalData.thirdSession = null;
                                    uni.reLaunch({
                                        url: getApp().globalData.getCurrentPageUrlWithArgs()
                                    });
                                }
                            }
                        });
                        reject(res.data.msg);
                    }
                    resolve(res.data);
                } else if (res.statusCode == 404) {
                    uni.showModal({
                        title: '提示',
                        content: '接口请求出错，请检查手机网络',
                        success(res) {}
                    });
                    reject();
                } else if (res.statusCode == 401) {
					uni.showModal({
					    title: '提示',
					    content: "请登录系统",
					    success(res) {
                            //跳转登录界面
                            uni.navigateTo({
                                url: '/pages/user/user-center/index' // Replace with the actual path to your login page
                            });
                        }
					});
					reject();
                } else {
                    console.log(res);
                    uni.showModal({
                        title: '提示',
                        content: res.errMsg + ':' + res.data.message + ':' + res.data.msg,
                        success(res) {}
                    });
                    reject();
                }
            },
            fail(error) {
                console.log(error);
                uni.showModal({
                    title: '提示',
                    content: '接口请求出错：' + error.errMsg,
                    success(res) {}
                });
                reject(error);
            },
            complete(res) {
                uni.hideLoading();
            }
        });
    });
};
module.exports = {
    request,
    login: (data) => {
        //App登录接口
        return request('appNoLogin/login', 'post', data, false);
    },
	register: (data) => {
	    //App注册
	    return request('appNoLogin/register', 'post', data, false);
	},
    appUserGet: (data) => {
        //微信用户查询
        return request('app/appUser', 'get', null, false);
    },
    appUserSave: (data) => {
        //同步微信用户信息
        return request('app/appUserUpdate', 'post', data, true);
    },
    goodsCategoryGet: (data) => {
        //餐品分类查询
        return request('appNoLogin/tree/', 'get', data, true);
    },
    goodsPage: (data) => {
        //餐品列表
        return request('appNoLogin/goodsPage', 'post', data, false);
    },
    goodsGet: (goodsId) => {
        //餐品查询
        return request('appNoLogin/goodsDetail/' + goodsId, 'get', null, false);
    },
    orderAdd: (data) => {
        //订单提交
        return request('app/orderAdd', 'post', data, true);
    },
    orderPage: (data) => {
        //订单列表
        return request('app/orderPage', 'post', data, false);
    },
    orderGet: (id) => {
        //订单详情查询
        return request('app/orderInfo/' + id, 'get', null, false);
    },
    orderCancel: (id) => {
        //订单确认取消
        return request('app/orderCancel/' + id, 'put', null, true);
    },
    orderReceive: (id) => {
        //订单确认收货
        return request('app/orderReceive/' + id, 'put', null, true);
    },
    orderDel: (id) => {
        //订单删除
        return request('app/orderDel/' + id, 'delete', null, false);
    },
    orderCountAll: (data) => {
        //订单计数
        return request('app/orderCount', 'get', data, false);
    },
    userAddressPage: (data) => {
        //用户收货地址列表
        return request('app/userAddress', 'post', data, false);
    },
    userAddressSave: (data) => {
        //用户收货地址新增
        return request('app/addAddress', 'post', data, true);
    },
    userAddressDel: (id) => {
        //用户收货地址删除
        return request('app/deleteAddress/' + id, 'delete', null, false);
    },
    goodsCart: (id) => {
        return request('app/goodsCart/' + id, 'get', null, false);
    },
    //订单评价
    orderCv: (data) => {
        return request('app/orderCv', 'post', data, true);
    },
    //评价获取
    userCommentPage: (data) => {
        return request('app/userCommentPage', 'post', data, true);
    },
    //查看单个餐品的评价
    goodsCommentPage: (data) => {
        return request('appNoLogin/goodsCommentPage', 'post', data, true);
    },
	loginOut:()=>{
		return request('back/loginOut', 'get', null, false);
	},
	updateUserInfo:(data)=>{
		return request('appNoLogin/updateUserInfo', 'post', data, false);
	}
};
