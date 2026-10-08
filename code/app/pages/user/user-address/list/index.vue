<template>
    <view style="height: 100%">
        <view class="cu-list menu-avatar">
            <view class="cu-item solid-top" v-for="(item, index) in userAddress" :key="index">
                <view class="cu-avatar round bg-red">
                    <text class="avatar-text">{{ item.name }}</text>
                </view>

                <view class="content loc-content" @tap="selectUserAddress" :data-index="index">
                    <view class="flex">
                        <view class="text-black">{{ item.name }}</view>
                        <view class="text-gray text-sm margin-left-sm">{{ item.phone }}</view>
                    </view>
                    <view class="text-black text-sm overflow-2 loc-info">
                        <view class="cu-tag bg-orange sm margin-left-sm" v-if="item.isDefault == '1'">默认</view>
                        {{ item.address }}
                    </view>
                </view>

                <view class="action" @tap="toEdit" :data-index="index">
                    <text class="cuIcon-edit"></text>
                </view>
            </view>
        </view>
        <view :class="'cu-load bg-gray ' + (loadmore ? 'loading' : '')"></view>
        <view class="cu-load bg-gray margin-top-xl" v-if="userAddress.length <= 0 && !loadmore"><text class="text-gray">暂无收货地址，请添加</text></view>
        <button
            class="cu-btn block shadow-blur margin-sm"
            style="background-color: #2967ff; font-weight: 300; height: 88rpx; margin-top: 200rpx"
            v-if="userAddress.length < 10"
            @tap="toAdd"
        >
            <text class="text-white">添加新地址</text>
        </button>
    </view>
</template>

<script>
const app = getApp();
export default {
    data() {
        return {
            page: {
                searchCount: false,
                current: 1,
                size: 10,
                ascs: '',
                //升序字段
                descs: ''
            },
            parameter: {},
            loadmore: true,
            userAddress: [],
            select: false
        };
    },
    onLoad(options) {
        if (options.select) {
            this.setData({
                select: true
            });
        }
    },
    onShow() {

            this.userAddressPage();
        
    },
    methods: {
        userAddressPage() {
            app.globalData.api.userAddressPage(this.page).then((res) => {
                let userAddress = res.data.records;
                this.setData({
                    userAddress: userAddress,
                    loadmore: false
                });
            });
        },

        toAdd() {
            uni.setStorage({
                key: 'param-userAddressForm',
                data: []
            });
            uni.navigateTo({
                url: '/pages/user/user-address/form/index'
            });
        },

        toEdit(e) {
            let index = e.currentTarget.dataset.index;
            let userAddressForm = this.userAddress[index];
            /* 把参数信息异步存储到缓存当中 */
            uni.setStorage({
                key: 'param-userAddressForm',
                data: userAddressForm
            });
            uni.navigateTo({
                url: '/pages/user/user-address/form/index'
            });
        },

        selectUserAddress(e) {
            if (this.select) {
                let index = e.currentTarget.dataset.index;
                let userAddressForm = this.userAddress[index];
                var pages = getCurrentPages(); // 获取页面栈
                var currPage = pages[pages.length - 1]; // 当前页面
                var prevPage = pages[pages.length - 2]; // 上一个页面
                prevPage.setData({
                    userAddress: userAddressForm
                });
                uni.navigateBack();
            }
        }
    }
};
</script>
<style>
.loc-content {
    width: calc(100% - 96rpx - 60rpx - 80rpx) !important;
    left: 126rpx !important;
}
.loc-info {
    line-height: 1.4em;
}
</style>
