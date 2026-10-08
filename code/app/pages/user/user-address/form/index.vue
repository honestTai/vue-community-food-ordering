<template>
    <view style="height: 100%">
        <form @submit="userAddressSave">
            <view class="cu-form-group">
                <view class="title">姓名</view>
                <input placeholder="请输入姓名" name="name" :value="userAddress.name" />
            </view>
            <view class="cu-form-group">
                <view class="title">联系电话</view>
                <input placeholder="请输入电话" name="phone" :value="userAddress.phone"  maxlength="11"/>
            </view>
            <view class="cu-form-group">
                <view class="title">详细地址</view>
                <input placeholder="请输入详细地址" name="address" :value="userAddress.address" />
            </view>
            <view class="cu-form-group">
                <view class="title">设为默认地址</view>
                <switch class="red sm" :checked="userAddress.isDefault == '1'" @change="isDefaultChange"></switch>
            </view>
            <button class="cu-btn block bg-green margin-sm" formType="submit">立即保存</button>
            <button class="cu-btn block bg-red margin-sm" @tap="userAddressDelete" v-if="userAddress.id">删除</button>
        </form>
    </view>
</template>

<script>
const app = getApp();
export default {
    data() {
        return {
            userAddress: []
        };
    },
    onLoad(options) {
        if (uni.getStorageSync('param-userAddressForm') != []) {
            this.setData({
                userAddress: uni.getStorageSync('param-userAddressForm')
            });
        }
    },
    methods: {
        isDefaultChange(e) {
            if (e.detail.value) {
                this.setData({
                    [`userAddress.isDefault`]: '1'
                });
            } else {
                this.setData({
                    [`userAddress.isDefault`]: '0'
                });
            }
        },

        userAddressSave(e) {
            let value = e.detail.value;
            if (!value.name) {
                uni.showToast({
                    title: '请填写收货人姓名',
                    icon: 'none',
                    duration: 3000
                });
                return;
            }
            if (!value.phone) {
                uni.showToast({
                    title: '请填写联系电话',
                    icon: 'none',
                    duration: 3000
                });
                return;
            }
            if (!/^1(3|4|5|7|8|9|6)\d{9}$/i.test(value.phone)) {
                uni.showToast({
                    title: '请输入正确的手机号码',
                    icon: 'none',
                    duration: 3000
                });
                return;
            }
            if (!value.address) {
                uni.showToast({
                    title: '请填写详细地址',
                    icon: 'none',
                    duration: 3000
                });
                return;
            }
            app.globalData.api
                .userAddressSave({
                    id: this.userAddress.id,
                    name: value.name,
                    phone: value.phone,
                    address: value.address,
                    isDefault: this.userAddress.isDefault
                })
                .then((res) => {
                    uni.navigateBack();
                });
        },

        userAddressDelete() {
            let that = this;
            uni.showModal({
                content: '确认将这个地址删除吗？',
                cancelText: '我再想想',
                confirmColor: '#ff0000',
                success(res) {
                    if (res.confirm) {
                        app.globalData.api.userAddressDel(that.userAddress.id).then((res) => {
                            uni.navigateBack();
                        });
                    }
                }
            });
        },
    }
};
</script>
<style></style>
