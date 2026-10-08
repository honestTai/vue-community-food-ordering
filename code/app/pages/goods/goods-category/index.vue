<template>
    <view style="height: 100%">
        <view class="cu-bar search bg-white">
            <view class="search-form round">
                <text class="cuIcon-search"></text>
                <navigator class="response" hover-class="none" url="/pages/base/search/index">
                    <input type="text" placeholder="请输入餐品名" />
                </navigator>
            </view>
        </view>
        <view class="VerticalBox margin-top-xs margin-left-xs">
            <scroll-view class="VerticalNav nav" scroll-y scroll-with-animation :scroll-top="VerticalNavTop" style="height: calc(100vh - 100rpx)">
                <view :class="'cu-item ' + (index == TabCur ? 'text-red cur' : '')" @tap="tabSelect" :data-id="index" v-for="(item, index) in goodsCategory" :key="index">
                    {{ item.name }}
                </view>
            </scroll-view>
            <scroll-view class="VerticalMain" scroll-y scroll-with-animation style="height: calc(100vh - 100rpx)" :scroll-into-view="'main-' + MainCur" @scroll="VerticalMain">
                <view class="padding-tb-xs padding-lr-sm" :id="'main-' + index" v-for="(item, index) in goodsCategory" :key="index">
                    <view class="cu-bar solid-bottom bg-white">
                        <view class="action">
                            <text class="cuIcon-titles text-black"></text>
                            {{ item.name }}
                        </view>
                    </view>

                    <view class="cu-bar bg-white solid-bottom">
                        <view class="cate-list">
                            <image v-if="item.picUrl" class="img-banner radius" :src="item.picUrl"></image>
                            <view v-if="item.children.length > 0" class="cate" v-for="(item2, index1) in item.children" :key="index1">
                                <navigator hover-class="none"  :url="'/pages/goods/goods-list/index?categorySecond=' + item2.id + '&title=' + item2.name" >
                                    <image class="cate-img" :src="item2.picUrl ? item2.picUrl : '/static/public/img/no_pic.png'"></image>
                                    <view class="text-sm">{{ item2.name }}</view>
                                </navigator>
                            </view>
                            <view class="padding response text-center" v-if="!item.children">暂无数据</view>
                        </view>
                    </view>
                </view>
            </scroll-view>
        </view>
    </view>
</template>

<script>
const app = getApp();
export default {
    data() {
        return {
            config: app.globalData.config,
            TabCur: 0,
            MainCur: 0,
            VerticalNavTop: 0,
            goodsCategory: [],
            load: true
        };
    },
    onLoad(e) {
	console.log(e)
        this.onLoadClone3389(e.id);
		uni.setNavigationBarTitle({
		    title: e.name
		});
    },
    onShow() {
    },
    methods: {
        onLoadClone3389(e) {

                this.goodsCategoryGet(e);

        },

        goodsCategoryGet(e) {
            app.globalData.api.goodsCategoryGet().then((res) => {
                let goodsCategory = res.data;
                this.setData({
                    goodsCategory: goodsCategory
                });
            });
        },

        tabSelect(e) {
            console.log(e);
            this.setData({
                TabCur: e.currentTarget.dataset.id,
                MainCur: e.currentTarget.dataset.id,
                VerticalNavTop: (e.currentTarget.dataset.id - 1) * 50
            });
        },

        VerticalMain(e) {
            let that = this;
            let list = this.goodsCategory;
            let tabHeight = 0;
            if (this.load) {
                for (let i = 0; i < list.length; i++) {
                    let view = uni
                        .createSelectorQuery()
                        .in(uni)
                        .select('#main-' + i);
                    view.fields(
                        {
                            size: true
                        },
                        (data) => {
                            list[i].top = tabHeight;
                            tabHeight = tabHeight + data.height;
                            list[i].bottom = tabHeight;
                        }
                    ).exec();
                }
                that.setData({
                    load: false,
                    goodsCategory: list
                });
            }
            let scrollTop = e.detail.scrollTop + 20;
            for (let i = 0; i < list.length; i++) {
                if (scrollTop > list[i].top && scrollTop < list[i].bottom) {
                    that.setData({
                        VerticalNavTop: (i - 1) * 50,
                        TabCur: i
                    });
                    return false;
                }
            }
        }
    }
};
</script>
<style>
.VerticalNav.nav {
    width: 220rpx;
    white-space: initial;
}
.VerticalNav.nav .cu-item {
    font-size: 14px;
    font-weight: 300;
    color: #333333;
    width: 100%;
    text-align: center;
    background-color: #f1f1f1;
    margin: 0;
    border: none;
    height: 50px;
    position: relative;
}
.VerticalNav.nav .cu-item.cur {
    font-size: 15px;
    font-weight: 500;
    border-radius: 10rpx 0 0 10rpx;
    background-color: #fff;
}
.VerticalNav.nav .cu-item.cur::after {
    content: '';
    position: absolute;
    background-color: currentColor;
    top: 0;
    right: 0rpx;
    bottom: 0;
    margin: auto;
}
.VerticalBox {
    display: flex;
}
.VerticalMain {
    background-color: #fff;
}
.img-banner {
    width: 94%;
    height: 148rpx;
    margin: auto;
}
.cate-list {
    display: flex;
    flex-wrap: wrap;
    width: 100%;
}
.cate-title {
    width: 100%;
    font-size: 15px;
    font-weight: bold;
    padding-left: 10rpx;
}
.cate {
    width: 150rpx;
    font-size: 14px;
    margin: 15rpx;
    text-align: center;
}
.cate-img {
    width: 140rpx;
    height: 140rpx;
}
.cate-type {
    font-size: 14px;
    color: #000000;
}
</style>
