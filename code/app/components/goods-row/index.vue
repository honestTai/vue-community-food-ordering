<template>
    <view class="cu-card article no-card">
        <view class="cu-item" v-for="(item, index) in goodsList" :key="index">
            <navigator hover-class="none" :url="'/pages/goods/goods-detail/index?id=' + item.goods.id">
                <view class="content">
                    <image
                        :src="item.goods.goodsPhotoList[0] ? item.goods.goodsPhotoList[0] : '/static/public/img/no_pic.png'"
                        mode="aspectFill"
                        class="row-img margin-top-xs"
                    ></image>
                    <view class="desc solid-bottom row-info">
                        <view class="text-black margin-top-sm overflow-2">{{ item.goods.name }}</view>
                        <view class="text-gray text-sm margin-top-sm overflow-2">所属分类:{{ item.aclass.name }}</view>
                        <view class="flex justify-between margin-top-sm">
                            <view class="text-price text-bold text-red padding-lr-sm">{{ item.goods.price }}</view>
                            <view class="text-gray text-sm padding-lr-sm">已售{{ item.goods.goodsOrderCount }}</view>
                        </view>
                    </view>
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
	//组件的值
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
.row-img {
    width: 280rpx !important;
    height: 280rpx !important;
    border-radius: 10rpx;
}
.row-info {
    display: block !important;
}
</style>
