<template>
    <view class="goods-container">
        <view class="goods-box" v-for="(item, index) in goodsList" :key="index">
            <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.goods.id">
                <view class="text-black margin-top text-bold padding-lr overflow-1">{{ item.goods.name }}</view>
                <view class="img-box">
                    <image :src="item.goods.goodsPhotoList[0] ? item.goods.goodsPhotoList[0] : '/static/public/img/no_pic.png'" class="margin-top-xs" />
                </view>
                <view class="text-center margin-top">
                    <view class="cu-tag line-black round text-df text-price text-bold text-black">{{ item.goods.price }}</view>
                    <view class="text-gray text-sm padding-lr-sm margin-top-sm">已售{{ item.goods.orderCount }}</view>
                </view>
            </navigator>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {};
    },
    props: {
        goodsList: {
            type: Array,
            default: () => []
        }
    },
    methods: {},
    created: function () {}
};
</script>
<style>
.goods-container {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    box-sizing: content-box;
    padding: 20rpx;
}
.goods-box {
    width: 336rpx;
    height: 560rpx;
    background-color: #fff;
    overflow: hidden;
    margin-top: 40rpx;
    border-radius: 20rpx;
    box-shadow: 0rpx 0rpx 50rpx 10rpx rgba(211, 211, 211, 1);
}
.goods-box .img-box {
    width: 100%;
    height: 349rpx;
    overflow: hidden;
}
.goods-box .img-box image {
    width: 100%;
    height: 336rpx;
}
</style>
