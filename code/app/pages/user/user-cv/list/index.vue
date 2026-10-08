<template>
    <view class="cu-list menu-avatar comment">
        <view class="cu-item solid-top" v-for="(item, index) in goodsAppraises" :key="index">
            <view class="cu-avatar round" :style="'background-image:url(' + item.headimgUrl + ')'">{{ !item.headimgUrl ? '头' : '' }}</view>

            <view class="content">
                <view class="text-black flex">
                    {{ item.userName }}
                    <view class="text-gray margin-left-sm text-sm">{{ item.time }}</view>
                    <navigator class="cu-item arrow" :url="'/pages/goods/goods-detail/index?id=' + item.goodsId" hover-class="none">
                        <view class="text-gray margin-left-sm text-sm">购买餐品:{{ item.goodsName }}</view>
                    </navigator>
                </view>
                <view class="text-black text-content text-df">
                    {{ item.content }}
                </view>
                <view class="bg-grey padding-sm radius margin-top-sm text-sm" v-if="item.replyStatus == 1">
                    <view class="flex text-sm cuIcon-mark">
                        <view class="text-bold margin-left-xs">商家回复：</view>
                        {{ item.replyTime }}
                    </view>
                    <view class="text-content">{{ item.replyContent }}</view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
const app = getApp();
export default {
    data() {
        return {
            page: {
                current: 1,
                size: 10
            },
            parameter: {
                spuId: ''
            },
            loadmore: true,
            goodsAppraises: []
        };
    },
    onLoad(options) {
        let spuId = options.spuId;
        this.setData({
            ['parameter.spuId']: spuId
        });
        
            this.userCommentPage();
       
    },
    onReachBottom() {
        if (this.loadmore) {
            this.setData({
                ['page.current']: this.page.current + 1
            });
            this.goodsCommentPage();
        }
    },
    methods: {
        userCommentPage() {
            app.globalData.api.userCommentPage(Object.assign({}, this.page)).then((res) => {
                let goodsAppraises = res.data.records;
                this.setData({
                    goodsAppraises: [...this.goodsAppraises, ...goodsAppraises]
                });
                if (goodsAppraises.length < this.page.size) {
                    this.setData({
                        loadmore: false
                    });
                }
            });
        },

        previewImage(e) {
            uni.previewImage({
                urls: e.currentTarget.dataset.url,
                current: e.currentTarget.dataset.url
            });
        }
    }
};
</script>
<style>
/* pages/user/user-cv/list/index.wxss */
</style>
