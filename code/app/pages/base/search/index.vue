<template>
    <view style="height: 100%">
        <view class="cu-bar search bg-white">
            <view class="search-form round">
                <text class="cuIcon-search"></text>
                <input type="text" placeholder="请输入餐品名" confirm-type="search" @confirm="searchHandle" focus />
            </view>
        </view>
        <view v-if="searchHistory.length > 0">
            <view class="cu-bar bg-white">
                <view class="action">
                    <text class="cuIcon-time"></text>
                    历史搜索
                </view>
                <view class="action">
                    <text class="cuIcon-delete lg text-gray" @tap="clearSearchHistory"></text>
                </view>
            </view>
            <view class="padding-sm flex flex-wrap bg-white">
                <view class="padding-xs" v-for="(item, index) in searchHistory" :key="index">
                    <view class="cu-tag round" @tap="searchHandle" :data-name="item.name">{{ item.name }}</view>
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
            searchHistory: [],
            goodsList: []
        };
    },
    onShow() {
        this.setData({
            searchHistory: uni.getStorageSync('searchHistory') ? uni.getStorageSync('searchHistory') : []
        });
    },
    onLoad(options) {
        
            this.goodsPage();
        
    },
    methods: {
        searchHandle(e) {
            let value;
            if (e.detail.value) {
                value = e.detail.value;
            } else if (e.currentTarget.dataset.name) {
                value = e.currentTarget.dataset.name;
            }
            let searchHistory = this.searchHistory;
            searchHistory.forEach(function (item, index) {
                let i = 9; //最多缓存10条
                if (item.name == value) {
                    searchHistory.splice(index, 1);
                    i++;
                }
                if (index >= i) {
                    searchHistory.splice(index, 1);
                }
            });
            searchHistory.unshift({
                name: value
            });
            uni.setStorageSync('searchHistory', searchHistory);
            uni.navigateTo({
                url: '/pages/goods/goods-list/index?name=' + value
            });
        },

        clearSearchHistory() {
            let that = this;
            uni.showModal({
                content: '确认删除全部历史记录？',
                cancelText: '我再想想',
                confirmColor: '#ff0000',
                success(res) {
                    if (res.confirm) {
                        that.setData({
                            searchHistory: []
                        });
                        uni.setStorageSync('searchHistory', []);
                    }
                }
            });
        },

        goodsPage() {
            app.globalData.api
                .goodsPage({
                    searchCount: false,
                    current: 1,
                    size: 10,
                    ascs: '',
                    //升序字段
                    descs: 'sale_num'
                })
                .then((res) => {
                    let goodsList = res.data.records;
                    this.setData({
                        goodsList: goodsList
                    });
                });
        }
    }
};
</script>
<style></style>
