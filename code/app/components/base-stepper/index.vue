<template>
    <view :class="'flex text-center ' + customClass" style="width: 230rpx">
        <view class="flex-sub">
            <button class="cu-btn cuIcon-move sm st-num-bt" :disabled="stNum <= min" @tap="stNumMinus"></button>
        </view>
        <view class="flex-sub">
            <input type="number" class="st-num text-center bg-gray radius" :value="stNum" :disabled="true" @input="numChange" />
        </view>
        <view class="flex-sub">
            <button class="cu-btn cuIcon-add sm st-num-bt" :disabled="max >= 0 ? stNum >= max : false" @tap="stNumAdd"></button>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            stNumClone: 0
        };
    },
    props: {
        max: {
            type: Number,
            default: 0
        },
        min: {
            type: Number,
            default: 0
        },
        stNum: {
            type: Number,
            default: 0
        },
        customClass: {
            type: String,
            default: ''
        }
    },
    methods: {
        stNumMinus() {
            this.setData({
                stNum: this.stNum - 1
            });
            this.$emit('numChange', {
                detail: this.stNum
            });
        },
        stNumAdd() {
            this.setData({
                stNum: this.stNum + 1
            });
            this.$emit('numChange', {
                detail: this.stNum
            });
        },
        numChange(e) {
            this.$emit('numChange', {
                detail: val
            });
        }
    },
    created: function () {}
};
</script>
<style>
.st-num {
    width: 80rpx;
    height: 50rpx;
}
.st-num-bt {
    line-height: 50rpx;
    overflow: hidden;
    font-size: 30rpx !important;
    height: 50rpx !important;
}
</style>
