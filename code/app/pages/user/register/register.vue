<template>
	<view>
		<!--账号注册页开始-->
		<form @submit="register">
			<view class="cu-form-group margin-top">
				<view class="title">姓名</view>
				<input placeholder="输入您的名称" name="name" :value="userInfo.name"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">手机号码</view>
				<input placeholder="请输入您的手机号码" name="phone" maxlength="11" :value="userInfo.phone"></input>
				<view class="cu-capsule radius">
					<view class='cu-tag bg-blue '>
						+86
					</view>
					<view class="cu-tag line-blue">
						中国大陆
					</view>
				</view>
			</view>
			<view class="cu-form-group">
				<view class="title">登录账号</view>
				<input placeholder="请输入您的登录账号" name="number" :value="userInfo.number"></input>
				<text class='cuIcon-usefull text-orange'></text>
			</view>
			<view class="cu-form-group">
				<view class="title">登录密码</view>
				<input placeholder="请输入您的登录密码" name="password" type="password" :value="userInfo.password"></input>
				<text class='cuIcon-play_forward_fill text-orange'></text>
			</view>
			<view class="cu-bar bg-white margin-top">
				<view class="action">
					头像
				</view>
				<view class="action">
					{{imgList.length}}/1
				</view>
			</view>
			<view class="cu-form-group">
				<view class="grid col-4 grid-square flex-sub">
					<view class="bg-img" v-for="(item,index) in imgList" :key="index" @tap="ViewImage"
						:data-url="imgList[index]">
						<image :src="imgList[index]" mode="aspectFill"></image>
						<view class="cu-tag bg-red" @tap.stop="DelImg" :data-index="index">
							<text class='cuIcon-close'></text>
						</view>
					</view>
					<view class="solids" @tap="ChooseImage" v-if="imgList.length<1">
						<text class='cuIcon-cameraadd'></text>
					</view>
				</view>
			</view>
			<view class="padding flex flex-direction">
				<button form-type="submit" class="cu-btn bg-cyan lg">确认</button>
			</view>
		</form>
		<!--账号注册页结束-->
	</view>
</template>

<script>
	import __config from '../../../config/env.js';
	const app = getApp();
	export default {
		data() {
			return {
				urlapi: __config.basePath,
				imgList: [],
				headimgUrl: "",
				userInfo: {},
				isRegister:false
			}
		},
		onLoad(e) {
			console.log(e.info == null)
			if (e.info != null) {
				this.userInfo = JSON.parse(e.info)
				this.imgList.push(this.userInfo.headimgUrl)
				this.headimgUrl=this.userInfo.headimgUrl
				this.isRegister = true
			}
			console.log(this.userInfo)
			uni.setNavigationBarTitle({
				title: '用户注册'
			});
		},
		methods: {
			ChooseImage() {
				var that = this
				console.log(this.urlapi)
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album'], //从相册选择
					success: (res) => {

						this.imgList = this.imgList.concat(res.tempFilePaths)
						let url = res.tempFilePaths[0];
						uni.uploadFile({
							url: this.urlapi + 'appNoLogin/uploadMerchantUserHead',
							filePath: url,
							name: 'head',
							success: (res) => {
								var data = JSON.parse(res.data)
								console.log(data)
								this.headimgUrl = data.data
							}
						})
					}

				});
			},
			ViewImage(e) {
				uni.previewImage({
					urls: this.imgList,
					current: e.currentTarget.dataset.url
				});
			},
			DelImg(e) {
				var that = this
				uni.showModal({
					title: that.customerName,
					content: '确定要删除吗？',
					cancelText: '在想想',
					confirmText: '确定',
					success: res => {
						if (res.confirm) {
							this.imgList = [];
							this.headimgUrl = ""
						}
					}
				})
			},
			//确认注册，跳转到登录页面
			register(e) {
				console.log(this.userInfo)
				if (this.isRegister) {
					var that=this
					if (e.detail.value.number.length < 6) {
						uni.showToast({
							icon: 'none',
							title: '账号最短为 6 个字符'
						});
						return;
					}
					if (e.detail.value.password.length < 6) {
						uni.showToast({
							icon: 'none',
							title: '密码最短为 6 个字符'
						});
						return;
					}
					if (e.detail.value.name.length < 2) {
						uni.showToast({
							icon: 'none',
							title: '名称最短为 2 个字符'
						});
						return;
					}
					if (e.detail.value.phone.length < 11) {
						uni.showToast({
							icon: 'none',
							title: '手机最短为 11 个字符'
						});
						return;
					}
					app.globalData.api.updateUserInfo({
						"number": e.detail.value.number,
						"password": e.detail.value.password,
						"name": e.detail.value.name,
						"phone": e.detail.value.phone,
						"headimgUrl": that.headimgUrl,
						"id":that.userInfo.id
					}).then((res) => {
						uni.showToast({
							icon: 'none',
							title: '修改成功'
						});
						localStorage.removeItem("appUser")
						
						uni.navigateTo({
							url: '/pages/user/user-center/index'
						});
					});
				} else {
					var that = this
					console.log(app.globalData.api)
					if (e.detail.value.number.length < 6) {
						uni.showToast({
							icon: 'none',
							title: '账号最短为 6 个字符'
						});
						return;
					}
					if (e.detail.value.password.length < 6) {
						uni.showToast({
							icon: 'none',
							title: '密码最短为 6 个字符'
						});
						return;
					}
					if (e.detail.value.name.length < 2) {
						uni.showToast({
							icon: 'none',
							title: '名称最短为 2 个字符'
						});
						return;
					}
					if (e.detail.value.phone.length < 11) {
						uni.showToast({
							icon: 'none',
							title: '手机最短为 11 个字符'
						});
						return;
					}
					app.globalData.api.register({
						"number": e.detail.value.number,
						"password": e.detail.value.password,
						"name": e.detail.value.name,
						"phone": e.detail.value.phone,
						"headimgUrl": that.headimgUrl
					}).then((res) => {
						uni.showToast({
							icon: 'none',
							title: '注册成功'
						});
						uni.navigateTo({
							url: '/pages/user/user-center/index'
						});
					});
				}
			}
		}
	}
</script>

<style>

</style>
