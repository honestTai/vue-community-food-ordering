<template>
  <view style="height: 100%">
    <view class="cu-bar search bg-white fixed" style="box-shadow: none">
      <view class="search-form round">
        <text class="cuIcon-search"></text>
        <navigator class="response" hover-class="none" url="/pages/base/search/index">
          <input type="text" placeholder="请输入餐品名"/>
        </navigator>
      </view>
    </view>
    <view class="margin-top-bar bg-white">
      <goods-row :goodsList="goodsList"/>
    </view>
  </view>
</template>

<script>
import goodsRow from '@/components/goods-row/index.vue';

const app = getApp();
export default {
  components: {
    goodsRow
  },
  data() {
    return {
      config: app.globalData.config,
      page: {
        searchCount: false,
        current: 1,
        size: 10
      },
      loadmore: true,
      goodsList: []
    };
  },
  onLoad() {
    this.loadData()
  },
  onShow() {

  },
  onShareAppMessage: function () {
    let title = '社区养老服务点餐系统';
    let path = 'pages/home/index';
    return {
      title: title,
      path: path,
      success: function (res) {
        if (res.errMsg == 'shareAppMessage:ok') {
          console.log(res.errMsg);
        }
      },
      fail: function (res) {
        // 转发失败
      }
    };
  },
  onPullDownRefresh() {
    // 显示顶部刷新图标
    uni.showNavigationBarLoading();
    this.refresh();
    // 隐藏导航栏加载框
    uni.hideNavigationBarLoading();
    // 停止下拉动作
    uni.stopPullDownRefresh();
  },
  onReachBottom() {
    if (this.loadmore) {
      this.setData({
        ['page.current']: this.page.current + 1
      });
      this.goodsPage();
    }
  },
  methods: {
    loadData() {
      this.goodsPage();
    },
    goodsPage(e) {
      app.globalData.api.goodsPage(this.page).then((res) => {
        let goodsList = res.data.records;
        this.setData({
          goodsList: [...this.goodsList, ...goodsList]
        });
        if (goodsList.length < this.page.size) {
          this.setData({
            loadmore: false
          });
        }
      });
    },

    refresh() {
      this.setData({
        loadmore: true,
        ['page.current']: 1,
        goodsList: []
      });
      this.loadData();
    },

    jumpPage(e) {
      let page = e.currentTarget.dataset.page;
      if (page) {
        uni.navigateTo({
          url: page
        });
      }
    }
  }
};
</script>
<style>
.list-item {
  background-color: #fff;
}

.wrapper-list {
  white-space: nowrap;
  padding: 0rpx 20rpx 50rpx 0rpx;
}

.wrapper-list .item {
  display: inline-block;
  width: 560rpx;
  height: 800rpx;
  margin: 60rpx 0 60rpx 50rpx;
  padding: 10rpx 30rpx;
  border-radius: 25rpx;
  box-shadow: 0rpx 0rpx 50rpx 10rpx rgba(211, 211, 211, 1);
}

.wrapper-list .item:nth-last-child(1) {
  margin-right: 20rpx;
}

.wrapper-list .item .img-box {
  width: 100%;
  height: 480rpx;
}

.wrapper-list .item .img-box image {
  width: 100%;
  height: 100%;
}

.adsec {
  width: 100%;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  padding: 7rpx 10rpx;
  height: 80rpx;
}

.adsec-icon {
  height: 80rpx;
  line-height: 80rpx;
}

.swiper_container {
  height: 80rpx;
  width: 95%;
  line-height: 80rpx;
}

.screen-swiper {
  height: 600rpx;
  margin-top: 100rpx;
}

.goods-image {
  width: 100%;
  height: 1150rpx;
}

.hot-goods {
  width: auto;
  overflow: hidden;
}

.goods-buy {
  margin-left: 500rpx;
  margin-top: -120rpx;
  padding-bottom: 50rpx;
}

.buy-now {
  background-color: #2967ff !important;
  font-weight: 300;
  width: 220rpx;
}
</style>
