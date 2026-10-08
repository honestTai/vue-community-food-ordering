<template>
	<view style="height: 100%">
		<!--登录界面-->
		<view v-if="session!=null">
			<view class="cu-list menu-avatar bg-white padding-bottom">

				<view class="cu-avatar round xl head flex" :style="'background-image:url(' + appUser.headimgUrl + ')'">
					{{ !appUser.headimgUrl ? '头' : '' }}</view>
				<view class="content text-center margin-top-sm">
					<view class="margin-top-xs text-xl" v-if="appUser.name">{{ appUser.name }}</view>
					<button class="cu-btn round sm margin-top-xs" open-type="getUserInfo" @click="updateInfo()"
						lang="zh_CN">
						修改个人信息
					</button>
				</view>
			</view>

			<!--个人中心-->
			<view class="cu-list card-menu radius order-list">
				<view class="cu-bar bg-white solid-bottom">
					<view class="action">
						<text class="cuIcon-titles titles-color"></text>
						我的订单
					</view>
					<navigator class="action" url="/pages/order/order-list/index" hover-class="none">
						全部订单
						<text class="cuIcon-right"></text>
					</navigator>
				</view>
				<view class="cu-list grid col-4 no-border">
					<view class="cu-item">
						<navigator url="/pages/order/order-list/index?status=0" hover-class="none">
							<view class="cuIcon-pay text-red">
								<view v-if="orderCountAll[0] > 0" class="cu-tag badge">{{ orderCountAll[0] }}</view>
							</view>
							<text>待配送</text>
						</navigator>
					</view>
					<view class="cu-item">
						<navigator url="/pages/order/order-list/index?status=5" hover-class="none">
							<view class="cuIcon-pay text-red">
								<view v-if="orderCountAll[1] > 0" class="cu-tag badge">{{ orderCountAll[1] }}</view>
							</view>
							<text>待完成</text>
						</navigator>
					</view>
					<view class="cu-item">
						<navigator url="/pages/order/order-list/index?status=1" hover-class="none">
							<view class="cuIcon-send text-yellow">
								<view v-if="orderCountAll[2] > 0" class="cu-tag badge">{{ orderCountAll[2] }}</view>
							</view>
							<text>待消费</text>
						</navigator>
					</view>
					<view class="cu-item">
						<navigator url="/pages/order/order-list/index?status=2" hover-class="none">
							<view class="cuIcon-send text-blue">
								<view v-if="orderCountAll[3] > 0" class="cu-tag badge">{{ orderCountAll[3] }}</view>
							</view>
							<text>待评价</text>
						</navigator>
					</view>
					<view class="cu-item">
						<navigator url="/pages/order/order-list/index?status=3" hover-class="none">
							<view class="cuIcon-evaluate text-orange">
								<view v-if="orderCountAll[4] > 0" class="cu-tag badge">{{ orderCountAll[4] }}</view>
							</view>
							<text>已完成</text>
						</navigator>
					</view>
				</view>
			</view>
			<view class="cu-list menu card-menu radius address">
				<navigator class="cu-item arrow" url="/pages/user/user-address/list/index" hover-class="none">
					<view class="content">
						<text class="cuIcon-location text-green text-xl"></text>
						<text class="text-grey">收货地址</text>
					</view>
				</navigator>
				<navigator class="cu-item arrow" url="/pages/user/user-cv/list/index" hover-class="none">
					<view class="content">
						<text class="cuIcon-comment text-red text-xl"></text>
						<text class="text-grey">我的评价</text>
					</view>
				</navigator>
				<view class="cu-item arrow" @click="logout()" hover-class="none">
					<view class="content">
						<text class="cuIcon-delete text-red text-xl"></text>
						<text class="text-grey">退出登录</text>
					</view>
				</view>
			</view>
		</view>
		<!--登录-->
		<view v-else>
			<view class="container">
				<view class="  flex-wrap padding ">
					<view class="bg-img bg-mask flex align-center" style="height: 414upx;">
						<view class="padding-xl text-white">
							<view class="padding-xs text-xxl text-bold">
								请登录
							</view>
							<view class="padding-xs text-lg">
								社区养老点餐系统
							</view>
						</view>
					</view>
					<form @submit="login">
						<view class="cu-form-group margin-top">
							<view class="title">账号</view>
							<input placeholder="请输入账号" name="number"></input>
						</view>
						<view class="cu-form-group margin-top">
							<view class="title">密码</view>
							<input required="required" type="password" placeholder="请输入密码" name="password"></input>
						</view>
						<view class="padding flex flex-direction">
							<button form-type="submit" class="cu-btn bg-cyan lg">登录</button>
						</view>
					</form>
					<view class="padding flex flex-direction">
						<button form-type="submit" class="cu-btn bg-cyan lg" @click="register()">注册</button>
					</view>
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
				config: app.globalData.config,
				appUser: null,
				userInfo: null,
				orderCountAll: [],
				session:localStorage.getItem('thirdSession')
			};
		},
		onShow() {
			this.init()
		},
		onLoad() {
			console.log(localStorage.getItem('thirdSession'))
		},
		methods: {

			init(){
				if (this.session != null) {

					let appUser = JSON.parse(localStorage.getItem('appUser'));
					console.log(localStorage.getItem('appUser'))
					this.setData({
						appUser: appUser
					});
					this.appUserGet();
					this.orderCountAllFun();

					uni.setNavigationBarTitle({
					    title: '个人中心'
					});
				}else{
					uni.setNavigationBarTitle({
					    title: '登录'
					});
				}
			},

			settings: function() {
				uni.openSetting({
					success: function(res) {
						console.log(res.authSetting);
					}
				});
			},

			agreeGetUser(e) {
				if (e.detail.errMsg == 'getUserInfo:ok') {
					app.globalData.api.appUserSave(e.detail.userInfo).then((res) => {
						let appUser = res.data;
						this.setData({
							appUser: appUser
						});
						app.globalData.appUser = appUser;
						this.appUserGet();
					});
				}
			},


			//获取用户信息
			appUserGet() {

				app.globalData.api.appUserGet().then((res) => {
					this.setData({
						appUser: res.data,
						userInfo:res.data
					});
				});
			},

			orderCountAllFun() {
				app.globalData.api.orderCountAll().then((res) => {
					this.setData({
						orderCountAll: res.data
					});
				});
			},
			login(e) {
				var formdata = e.detail.value
				var userName = formdata.number
				var userPwd = formdata.password
				if (userName.length < 6) {
					uni.showToast({
						icon: 'none',
						title: '账号最短为 2 个字符'
					});
					return;
				}
				if (userPwd.length < 6) {
					uni.showToast({
						icon: 'none',
						title: '密码最短为 2 个字符'
					});
					return;
				}
				app.globalData.api.login({
					number:userName,
					password:userPwd
				}).then((res) => {
					console.log(res)
					uni.showToast({
						icon: 'success',
						title: '登录成功'
					});
					localStorage.setItem("appUser",JSON.stringify(res.data.user))
					localStorage.setItem("thirdSession",res.data.token)
					this.session=res.data.token
				
					this.init()

				});
			},
			register(e) {
				uni.navigateTo({
					url: '../register/register'
				});
			},
			logout(e){
				app.globalData.api.loginOut().then((res) => {
					uni.showToast({
						icon: 'success',
						title: '退出登录成功'
					});
					localStorage.removeItem("appUser")
					localStorage.removeItem("thirdSession")
					this.session=null
					this.init()
				})
			},
			updateInfo(e){
				uni.navigateTo({
					url: '../register/register?info='+JSON.stringify(this.userInfo)
				});
			}

		}
	};
</script>
<style>
	/**index.wxss**/
	page {
		background-color: white;
	}

	.order-list {
		box-shadow: 0rpx 0rpx 50rpx 10rpx rgba(211, 211, 211, 0.5);
	}

	.userinfo {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.userinfo-avatar {
		width: 128rpx;
		height: 128rpx;
		margin: 20rpx;
		border-radius: 50%;
	}

	.userinfo-nickname {
		color: #aaa;
	}

	.usermotto {
		margin-top: 200px;
	}

	.address {
		margin-top: 40rpx !important;
		box-shadow: 0rpx 0rpx 50rpx 10rpx rgba(211, 211, 211, 0.5);
	}

	.head {
		margin: auto;
		box-shadow: 0rpx 0rpx 50rpx 10rpx rgba(211, 211, 211, 0.5);
	}

	.titles-color {
		color: rgb(107, 107, 107);
	}
</style>
