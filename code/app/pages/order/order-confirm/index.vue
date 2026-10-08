<template>
    <view style="height: 100%">
        <view class="margin-bottom-bar" >
            <view class="cu-list menu-avatar" v-if="type===0">
                <navigator class="cu-item" url="/pages/user/user-address/list/index?select=true" >
                    <view class="cu-avatar round cuIcon-location bg-black"></view>
                    <view class="content loc-content" v-if="userAddress">
                        <view class="flex padding-top-sm">
                            <view class="cu-tag bg-red radius margin-right-sm" v-if="userAddress.isDefault == '1'">默认</view>
                            <view class="text-black">{{ userAddress.name }}</view>
                            <view class="text-gray text-sm margin-left-sm">{{ userAddress.phone }}</view>
                        </view>
                        <view class="text-gray text-sm overflow-2 loc-info padding-bottom-sm address">
                            {{ userAddress.address }}
                        </view>
                    </view>
                    <view class="content loc-content" v-if="!userAddress">请选择收货地址</view>
                    <view class="action">
                        <text class="cuIcon-right"></text>
                    </view>
                </navigator>
            </view>
            <view class="cu-card article">
                <view class="cu-item">
                    <view class="cu-list menu">
                        <view v-for="(item, index) in orderConfirmData" :key="index">
                            <view class="flex align-center">
                                <image :src="item.picUrl ? item.picUrl : '/static/public/img/no_pic.png'" mode="aspectFill" class="row-img margin-top-xs"></image>
                            </view>

                            <view class="row-info margin-left">
                                <view class="text-black margin-top-xl overflow-2">{{ item.goodsName }}</view>

                                <view class="flex margin-top-sm">
                                    <view class="flex-sub">
                                        <text class="text-price text-xl text-blue text-bold margin-top-sm">{{ item.price }}</text>
                                    </view>
                                    <view class="flex-twice text-gray text-sm text-right margin-right">x{{ item.num }}</view>
                                </view>
                            </view>
                        </view>
                        <view class="cu-item margin-top-sm">
                            <text class="text-gray text-sm">订单金额</text>
                            <view class="action">
                                <view class="text-price">{{ salesPrice }}</view>
                            </view>
                        </view>
                        <view class="cu-item margin-top-sm">
                            <text class="text-gray text-sm">消费方式：到店/配送</text>
                            <view class="action">
                                <switch class="red sm" :checked="type == 0" @change="isDefaultChange"></switch>
                            </view>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        <view class="cu-bar tabbar bg-white border foot">
            <view class="flex response">
                <view class="flex-sub"></view>
                <view class="flex-treble bar-rt">
                    <text class="text-sm text-gray">共{{ orderConfirmData.length }}件，</text>
                    <text class="text-sm text-gray">合计：</text>
                    <text class="text-xl text-price text-blue text-bold">{{ salesPrice }}</text>
                    <button
                        class="cu-btn shadow-blur margin-left-sm"
                        style="background-color: #2967ff; font-weight: 300; width: 220rpx"
                        @tap="orderSub"
                        :loading="loading"
                        :disabled="loading"
                        type=""
                    >
                        <text class="text-white">提交订单</text>
                    </button>
                </view>
            </view>
        </view>
        
    </view>
</template>
<script module="numberUtil" lang="wxs" src="@/utils/numberUtil.wxs"></script>
<script>

const app = getApp();
export default {
    
    data() {
        return {
            orderConfirmData: [],
            salesPrice: 0,
            paymentPrice: 0,
            userAddress: null,
            loading: false,
            userInfo: null,
            spuIds: [],
            type: 0,

            
        };
    },
    onShow() {},
    onLoad: function () {
        this.userAddressPage();
        this.userInfoGet();
        this.orderConfirmDo();
    },
    methods: {
        isDefaultChange(e) {
            if (e.detail.value) {
				//配送
                this.setData({
                    type: 0
                });
            } else {
                this.setData({
                    type: 1
                });
            }
        },

        orderConfirmDo() {
            // 本地获取参数信息
            let that = this;
            uni.getStorage({
                key: 'param-orderConfirm',
                success: function (res) {
                    console.log(res);
                    let orderConfirmData = res.data;
                    let salesPrice = 0; //订单金额
                    let spuIds = null;
                    orderConfirmData.forEach((orderConfirm, index) => {
                        if (spuIds) {
                            spuIds = spuIds + ',' + orderConfirm.goodsId;
                        } else {
                            spuIds = orderConfirm.spuId;
                        }
                        salesPrice = (Number(salesPrice) + orderConfirm.price * orderConfirm.num).toFixed(2);
                        orderConfirm.paymentPrice = (orderConfirm.price * orderConfirm.num).toFixed(2);
                    });
                    console.log(salesPrice);
                    that.setData({
                        orderConfirmData: orderConfirmData,
                        salesPrice: salesPrice,
                        paymentPrice: salesPrice,
                        spuIds: spuIds
                    });
                }
            });
        },

        //获取默认收货地址
        userAddressPage() {
            app.globalData.api
                .userAddressPage({
                    searchCount: false,
                    current: 1,
                    size: 1,
                    isDefault: '1'
                })
                .then((res) => {
                    let records = res.data.records;
                    if (records && records.length > 0) {
                        this.setData({
                            userAddress: records[0]
                        });
                    }
                });
        },

        //获取商城用户信息
        userInfoGet() {
            app.globalData.api.appUserGet().then((res) => {
                this.setData({
                    userInfo: res.data
                });
            });
        },

        //提交订单
        orderSub() {
            let that = this;
            let userAddress = that.userAddress;
            console.log(userAddress);
            if (userAddress == null) {
                uni.showToast({
                    title: '请选择收货地址',
                    icon: 'none',
                    duration: 2000
                });
                return;
            }
            that.setData({
                loading: true
            });
            let order = that.order;
            console.log(that.orderConfirmData);
            let orderList = [];
            that.orderConfirmData.forEach(function (item) {
                let orders = {};
                orders.goodsId = item.goodsId;
                orders.paymentPrice = item.paymentPrice;
                orders.addressId = userAddress.id;
                orders.userCatsId = item.userCatsId;
                orders.type = that.type;
                orderList.push(orders);
            });
            order = that.orderConfirmData;
            app.globalData.api
                .orderAdd(orderList)
                .then((res) => {
                    uni.showToast({
                        title: '购买成功',
                        icon: 'success',
                        duration: 2000
                    });
                    uni.switchTab({
                        url: '/pages/user/user-center/index'
                    });
                })
                .catch(() => {
                    that.setData({
                        loading: false
                    });
                });
        }
    }
};
</script>
<style>
.bar-rt {
    text-align: right !important;
    margin-right: 10rpx !important;
}
.row-img {
    width: 600rpx !important;
    height: 600rpx !important;
    margin: 0 auto;
    border-radius: 10rpx;
}
.row-info {
    display: block !important;
}
.loc-content {
    width: calc(100% - 96rpx - 60rpx - 80rpx) !important;
    left: 126rpx !important;
}
.loc-info {
    line-height: 1.4em;
}
.list-item {
    display: block !important;
    padding: 6rpx !important;
}
.cu-list.menu > .cu-item:after {
    border-bottom: unset !important;
}
.cu-list.menu > .cu-item {
    min-height: unset !important;
}
.delivery-way {
    justify-content: unset !important;
}
.address {
    line-height: 160%;
    font-weight: 300;
}
</style>
