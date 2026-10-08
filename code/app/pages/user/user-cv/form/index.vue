<template>
    <view style="height: 100%">
        <form @submit="orderCv">
            <view class="cu-form-group">
                <!-- <view class="title">输入评价内容</view> -->
                <textarea maxlength="100" @input="textareaAInput" placeholder="输入评价内容"></textarea>
            </view>
            <button class="cu-btn block bg-green margin-sm" formType="submit">立即保存</button>
        </form>
    </view>
</template>

<script>
// pages/user/user-cv/form/index.js
const app = getApp();
export default {
    data() {
        return {
            content: '',
            goodsId: '',
            orderId: ''
        };
    }
    /**
     * 生命周期函数--监听页面加载
     */,
    onLoad: function (options) {
        console.log(options);
        this.setData({
            goodsId: options.goodsId,
            orderId: options.orderId
        });
    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {},
    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {},
    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {},
    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {},
    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {},
    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {},
    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {},
    methods: {
        textareaAInput(e) {
            this.setData({
                content: e.detail.value
            });
        },

        orderCv(e) {
            app.globalData.api
                .orderCv({
                    content: this.content,
                    goodsId: this.goodsId,
                    orderId: this.orderId
                })
                .then((res) => {
                    uni.switchTab({
                        url: '../../user-center/index'
                    });
                });
        }
    }
};
</script>
<style>
/* pages/user/user-cv/form/index.wxss */
</style>
