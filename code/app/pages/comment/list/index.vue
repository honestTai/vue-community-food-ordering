<template>
    <view class="cu-list menu-avatar comment">
        <view class="cu-item solid-top" v-for="(item, index) in goodsAppraises" :key="index">
            <view class="cu-avatar round" :style="'background-image:url(' + item.headimgUrl + ')'">{{ !item.headimgUrl ? '头' : '' }}</view>

            <view class="content">
                <view class="text-black flex">
                    {{ item.userName }}
                    <view class="text-gray margin-left-sm text-sm">{{ item.time }}</view>
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
                size: 10,
                goodsId: ''
            },
            parameter: {
                spuId: ''
            },
            loadmore: true,
            goodsAppraises: []
        };
    },
    onLoad(options) {
        let spuId = options.goodsId;
        console.log(spuId);
        this.setData({
            ['parameter.spuId']: spuId,
            ['page.goodsId']: spuId
        });
        this.goodsCommentPage();
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
        goodsCommentPage() {
            app.globalData.api.goodsCommentPage(Object.assign({}, this.page)).then((res) => {
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
/* pages/comment/list/index.wxss */
</style>
