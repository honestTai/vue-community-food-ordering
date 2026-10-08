<template>
    <view class="flex justify-end">
       
        <button
            class="cu-btn radius margin-right shadow-blur delete-order"
            @tap="orderDel"
            :loading="loading"
            :disabled="loading"
            type=""
            v-if="orderInfo.order.status == 4 || orderInfo.order.status == 3"
        >
            <text class="text-white">删除订单</text>
        </button>
        <button
            class="cu-btn radius margin-right shadow-blur cancel-order"
            @tap="orderCancel"
            :loading="loading"
            :disabled="loading"
            type=""
            v-if="orderInfo.order.status == 0 || orderInfo.order.status == 1"
        >
            <text class="text-white">取消订单</text>
        </button>
        <button class="cu-btn radius margin-right shadow-blur confirm-goods" @tap="orderReceive" :loading="loading" :disabled="loading" type="" v-if="orderInfo.order.status == 5">
            <text class="text-white">确认收货</text>
        </button>
        <button class="cu-btn radius margin-right shadow-blur evaluation" @tap="ordercV" :loading="loading" :disabled="loading" type="" v-if="orderInfo.order.status == 2">
            <text class="text-white">评价</text>
        </button>
    </view>
</template>

<script>
const app = getApp();
export default {
    data() {
        return {
            loading: false
        };
    },
    props: {
        orderInfo: {
            type: Object,
            default: () => ({})
        },
        callPay: {
            type: Boolean,
            default: false
        },
        contact: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        orderReceive() {
            let that = this;
            uni.showModal({
                content: '是否确认收货吗？',
                cancelText: '我再想想',
                confirmColor: '#ff0000',
                success(res) {
                    if (res.confirm) {
                        let id = that.orderInfo.order.id;
                        app.globalData.api.orderReceive(id).then((res) => {
                            that.$emit('orderReceive', {
                                detail: res
                            });
                        });
                    }
                }
            });
        },

        //1 单评价
        ordercV() {
            let that = this;
            uni.navigateTo({
                url: '/pages/user/user-cv/form/index?goodsId=' + that.orderInfo.goods.id + '&orderId=' + that.orderInfo.order.id
            });
        },

        orderCancel() {
            let that = this;
            uni.showModal({
                content: '确认取消该订单吗？',
                cancelText: '我再想想',
                confirmColor: '#ff0000',
                success(res) {
                    if (res.confirm) {
                        let id = that.orderInfo.order.id;
                        app.globalData.api.orderCancel(id).then((res) => {
                        
                            that.$emit('orderCancel', {
                                detail: res
                            });
                        });
                    }
                }
            });
        },

        orderDel() {
            let that = this;
            uni.showModal({
                content: '确认删除该订单吗？',
                cancelText: '我再想想',
                confirmColor: '#ff0000',
                success(res) {
                    if (res.confirm) {
                        let id = that.orderInfo.order.id;
                        app.globalData.api.orderDel(id).then((res) => {
                            that.$emit('orderDel', {
                                detail: res
                            });
                        });
                    }
                }
            });
        },

        handleContact() {
            console.log('占位：函数 handleContact 未声明');
        }
    },
    created: function () {}
};
</script>
<style>
.service {
    background-color: #13b229 !important;
    width: 160rpx;
}
.delete-order {
    background-color: #2d2d2f !important;
    width: 200rpx;
}
.cancel-order {
    background-color: #2d2d2f !important;
    width: 200rpx;
}
.check-logistics {
    background-color: #2d2d2f !important;
    width: 200rpx;
}
.payment {
    background-color: #2967ff !important;
    width: 200rpx;
}
.confirm-goods {
    background-color: #2967ff !important;
    width: 200rpx;
}
.evaluation {
    background-color: #2d2d2f !important;
    width: 200rpx;
}
</style>
