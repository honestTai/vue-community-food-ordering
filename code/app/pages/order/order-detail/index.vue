<template>
    <view style="height: 100%">
        <view class="margin-bottom-bar">
            <view class="bg-white padding">
                <view class="status-desc text-red margin-left text-bold text-center cuIcon-order">
                    {{
                        orderInfo.order.status == 0
                            ? '待配送'
                            : orderInfo.order.status == 1
                            ? '待消费'
                            : orderInfo.order.status == 2
                            ? '待评价'
                            : orderInfo.order.status == 3
                            ? '已完成'
                            : orderInfo.order.status == 4
                            ? '取消'
                            : '已配送'
                    }}
                </view>
            </view>
            <view class="cu-list cu-card menu-avatar">
                <view class="cu-item">
                    <view class="cu-avatar round cuIcon-location bg-black"></view>
                    <view class="content loc-content">
                        <view class="flex">
                            <view class="text-black">{{ orderInfo.address.name }}</view>
                            <view class="text-gray text-sm margin-left-sm">{{ orderInfo.address.phone }}</view>
                        </view>
                        <view class="text-gray text-sm overflow-2 loc-info">
                            {{ orderInfo.address.address }}
                        </view>
                    </view>
                </view>
            </view>
            <view class="cu-card article mar-top-30">
                <view class="cu-item">
                    <view class="cu-list menu">
                        <view>
                            <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + orderInfo.goods.id" class="cu-item">
                                <view class="align-center">
                                    <view class="flex align-center">
                                        <image
                                            :src="orderInfo.goods.goodsPhotoList[0] ? orderInfo.goods.goodsPhotoList[0] : '/static/public/img/no_pic.png'"
                                            mode="aspectFill"
                                            class="row-img margin-top-xs"
                                        ></image>
                                    </view>
                                    <view class="desc row-info">
                                        <view class="text-black margin-left margin-top-sm overflow-2">{{ orderInfo.goods.name }}</view>
                                        <view class="flex justify-between">
                                            <view class="text-bold text-gray margin-top-sm margin-left-xs padding-lr-sm font-weight">数量</view>
                                            <view class="text-black text-sm margin-top-sm margin-right-xs padding-lr-sm">x{{ orderInfo.order.num }}</view>
                                        </view>
                                    </view>
                                </view>
                            </navigator>
                        </view>
                        <view class="cu-item margin-top-xs">
                            <text class="text-gray font-weight">支付金额</text>
                            <view class="action">
                                <text class="text-price text-xl text-blue text-bold">{{ orderInfo.order.paymentPrice }}</text>
                            </view>
                        </view>
                    </view>
                </view>
            </view>
			<view class="cu-card mar-top-30" v-if="orderInfo.takeUser!=null">
			    <view class="cu-item">
			        <view class="cu-bar bg-white">
			            <view class="action">
			                <text class="cuIcon-titles text-black"></text>
			                配送信息
			            </view>
			        </view>
			        <view class="margin flex">
			            <text class="flex-sub text-gray font-weight">配送小哥名称</text>
			            <view class="action">
			                {{ orderInfo.takeUser.name }}
			            </view>
			        </view>
			        <view class="margin flex">
			            <text class="flex-sub text-gray font-weight">手机号码</text>
			            <view class="action">{{ orderInfo.takeUser.phone }}<button class="cu-btn sm" @tap="copyData" :data-data="orderInfo.takeUser.phone">复制</button></view>
			        </view>
			    </view>
			</view>
            <view class="cu-card mar-top-30">
                <view class="cu-item">
                    <view class="cu-bar bg-white">
                        <view class="action">
                            <text class="cuIcon-titles text-black"></text>
                            订单信息
                        </view>
                    </view>
                    <view class="margin flex">
                        <text class="flex-sub text-gray font-weight">订单编号</text>
                        <view class="action">
                            {{ orderInfo.order.orderString }}
                            <button class="cu-btn sm" @tap="copyData" :data-data="orderInfo.order.orderString">复制</button>
                        </view>
                    </view>
                    <view class="margin flex">
                        <text class="flex-sub text-gray font-weight">创建时间</text>
                        <view class="action">{{ orderInfo.order.time }}</view>
                    </view>
                </view>
            </view>
			<view class="cu-card mar-top-30" v-if="orderInfo.takeUser!=null">
			    <view class="cu-item">
			       <view class="margin flex">
			          
			       </view>
			    </view>
			</view>
        </view>
        <view class="cu-bar tabbar bg-white border foot">
            <order-operate
                class="response"
                :orderInfo="orderInfo"
                :contact="true"
                @orderCancel="orderCancel"
                @orderReceive="orderCancel"
                @orderDel="orderDel"
                @unifiedOrder="unifiedOrder"
            />
        </view>
    </view>
</template>

<script>
import orderOperate from '@/components/order-operate/index';
const app = getApp();
export default {
    components: {
        orderOperate
    },
    data() {
        return {
            orderInfo: null,
            id: null,
            callPay: false,
            status: 0,
            name: '',
            phone: '',
            address: '',
            num: '',
            paymentPrice: '',
            orderString: '',
            time: ''
        };
    },
    onShow() {
        
            this.orderGet(this.id);
       
    },
    onLoad(options) {
        this.setData({
            id: options.id
        });
        if (options.callPay) {
            this.setData({
                callPay: true
            });
        }
    },
    methods: {
        orderGet(id) {
            let that = this;
            app.globalData.api.orderGet(id).then((res) => {
                let orderInfo = res.data;
                if (!orderInfo) {
                    uni.redirectTo({
                        url: '/pages/order/order-list/index'
                    });
                }
                this.setData({
                    orderInfo: orderInfo
                });
                setTimeout(function () {
                    that.setData({
                        callPay: false
                    });
                }, 4000);
            });
        },

        //复制内容
        copyData(e) {
            uni.setClipboardData({
                data: e.currentTarget.dataset.data
            });
        },

        orderCancel() {
            console.log(1);
            let id = this.orderInfo.order.id;
            this.orderGet(id);
        },

        orderDel() {
            uni.navigateBack();
        },

        unifiedOrder() {
            console.log('占位：函数 unifiedOrder 未声明');
        }
    }
};
</script>
<style>
.row-img {
    width: 600rpx !important;
    height: 600rpx !important;
    margin: auto !important;
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
.cu-list.menu > .cu-item:after {
    border-bottom: unset !important;
}
.cu-list.menu > .cu-item {
    min-height: unset !important;
}
.status-desc {
    font-size: 38rpx !important;
}
.font-weight {
    font-weight: 300;
}
</style>
