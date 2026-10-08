<template>
	<view style="height: 100%">
		<view class="product-bg bg-white padding-top">
			<swiper class="screen-swiper square-dot screen" :indicator-dots="true" :circular="true" :autoplay="true"
				interval="5000" duration="500" @change="change">
				<swiper-item v-for="(item, index) in goodsSpu.goods.goodsPhotoList" :key="index">
					<image class="screen-image" :src="item" mode="aspectFill"></image>
				</swiper-item>
			</swiper>

			<view class="page-index cu-tag round">{{ currents }}/{{ goodsSpu.goods.goodsPhotoList.length || 1 }}</view>
		</view>
		<view class="cu-bar bg-white padding-top-xl">
			<view class="text-xxl padding-tb-xs padding-lr-sm">
				<text class="text-price text-blue text-bold">{{ goodsSpu.goods.price }}</text>
				<view class="flex-sub text-sm text-gray text-right margin-right-sm">
					销量{{ goodsSpu.goods.goodsOrderCount }}</view>
			</view>
		</view>
		<view class="cu-bar bg-white">
			<view class="text-lg text-bold padding-lr-sm">
				<text class="text-black">{{ goodsSpu.goods.name }}</text>
			</view>
		</view>
		<view class="cu-bar bg-white">
			<view class="text-sm padding-lr-sm">
				<text class="text-gray">{{ goodsSpu.goods.sort_introduce }}</text>
			</view>
		</view>

		<view class="cu-bar bg-white solid-bottom">
			<view class="text-sm padding-lr-sm text-gray">
				<text v-if="goodsSpu.goods.price">单价：</text>
				<text class="text-price text-gray">{{ goodsSpu.goods.price }}</text>
			</view>
			<view class="text-sm margin-right text-gray">
				<text>库存：{{ goodsSpu.goods.stock }}</text>
			</view>
		</view>

		<view class="cu-bar bg-white">
			<view class="flex response" @tap="showModalService">
				<view class="flex-sub text-sm text-gray text-right margin-right-sm">送货上门 | 到店消费</view>
			</view>
		</view>

		<view class="cu-bar bg-white margin-top-sm solid-bottom">
			<view class="flex response">
				<view class="flex-sub text-df">
					<view class="text-black margin-left-sm">评价（{{ goodsSpu.commentTotal }}）</view>
				</view>
				<navigator :url="'/pages/comment/list/index?goodsId=' + goodsSpu.goods.id" hover-class="none"
					class="flex-sub text-df text-orange text-right margin-right-sm" v-if="goodsSpu.commentTotal > 0">
					查看
					<text class="cuIcon-right"></text>
				</navigator>
			</view>
		</view>

		<view class="cu-bar bg-white margin-top-sm">
			<view class="content">餐品信息</view>
		</view>

		<view class="bg-white">
			<!-- <template is="wxParse" :data="wxParseData:description.nodes"/> -->
			<mp-html :content="article_description"></mp-html>
		</view>

		<view class="cu-load bg-gray to-down">已经到底啦...</view>

		<view class="cu-bar bg-white tabbar border shop foot">
			<view class="btn-group">
				<button class="cu-btn shadow-blur buy-now" @tap="toDo" data-type="2"><text
						class="text-white">点餐</text></button>
			</view>
		</view>
		<!-- html转wxml -->

		<view :class="'cu-modal ' + (goodsSpu.goods.status == '1' ? 'show' : '')">
			<view class="cu-dialog">
				<view class="cu-bar bg-white justify-end">
					<view class="content">提示</view>
				</view>
				<view class="padding-xl">抱歉，该餐品已下架</view>
			</view>
		</view>
	</view>
</template>

<script>
	import baseRade from "@/components/base-rade/index";
	import poster from "@/components/wxa-plugin-canvas/poster/index";

	const {
		base64src
	} = require('../../../utils/base64src.js');
	const app = getApp();
	export default {
		components: {
			baseRade,
			poster
		},
		data() {
			return {
				config: app.globalData.config,
				goodsSpu: null,
				currents: 1,
				cartNum: 1,
				goodsSpecData: [],
				shoppingCartCount: 0,
				shareShow: '',
				modalService: '',
				shoppingCartId: '',
				id: "",
				modalSku: false,
				posterUrl: "",
				posterShow: false,
				goodsPhotoList: [],
				price: "",
				goodsOrderCount: "",
				name: "",
				sort_introduce: "",
				stock: "",
				phone: "",
				article_description: "",
				status: "",
				posterConfig: "",
				shoppingCartCountFuns: ""
			};
		},
		onLoad(options) {
			let id;
			if (options.scene) {
				//接受二维码中参数
				id = decodeURIComponent(options.scene);
			} else {
				id = options.id;
			}
			this.setData({
				id: id
			});

			this.goodsGet(id);
			this.shoppingCartCountFun();

			//判断用户是否将该商品加入了购物车
			this.goodsCart(id);
		},
		methods: {
			shoppingCartCountFun() {
				app.globalData.api.shoppingCartCount().then(res => {
					let shoppingCartCount = res.data;
					this.setData({
						shoppingCartCount: shoppingCartCount
					});
					//设置TabBar购物车数量
					app.globalData.shoppingCartCount = shoppingCartCount + '';
				});
			},
			goodsGet(id) {
				app.globalData.api.goodsGet(id).then(res => {
					let goodsSpu = res.data;
					this.setData({
						goodsSpu: goodsSpu
					});
					//html转wxml
					this.article_description = this.escape2Html(goodsSpu.goods.introduce);;
				});
			},

			change: function(e) {
				this.setData({
					currents: e.detail.current + 1
				});
			},

			// 购买或加入购物车
			toDo(e) {
				let canDo = true;
				if (canDo) {
					let goodsSpu = this.goodsSpu.goods;
					console.log(goodsSpu);
					if (e.currentTarget.dataset.type == '1') {
						//加购物车
						console.log(this.shoppingCartId);
						if (this.shoppingCartId != 0) {
							app.globalData.api.shoppingCartEdit({
								id: this.shoppingCartId,
								num: this.cartNum,
								goodsId: goodsSpu.id
							}).then(res => {
								uni.showToast({
									title: '修改成功',
									duration: 5000
								});
								this.shoppingCartCountFun();
							});
						} else {
							app.globalData.api.shoppingCartAdd({
								num: this.cartNum,
								goodsId: goodsSpu.id
							}).then(res => {
								uni.showToast({
									title: '添加成功',
									duration: 5000
								});
								this.setData({
									modalSku: false
								});
								this.goodsCart(goodsSpu.id);
								this.shoppingCartCountFun();
							});
						}
					} else {
						//立即购买，前去确认订单
						if (this.goodsSpu.goods.stock <= 0) {
							uni.showToast({
								title: '抱歉，库存不足暂时无法购买',
								icon: 'none',
								duration: 2000
							});
							return;
						}
						/* 把参数信息异步存储到缓存当中 */
						uni.setStorage({
							key: 'param-orderConfirm',
							data: [{
								goodsId: goodsSpu.id,
								num: this.cartNum,
								price: goodsSpu.price,
								goodsName: goodsSpu.name,
								picUrl: goodsSpu.goodsPhotoList[0] ? goodsSpu.goodsPhotoList[0] : '',
								userCatsId: 0
							}]
						});
						uni.navigateTo({
							url: '/pages/order/order-confirm/index'
						});
					}
				}
			},

			goodsCart(id) {
				app.globalData.api.goodsCart(id).then(res => {
					console.log(res.data);
					this.setData({
						shoppingCartId: res.data
					});
				});
			},





			shareShowFun() {
				this.setData({
					shareShow: 'show'
				});
			},

			shareHide() {
				this.setData({
					shareShow: ''
				});
			},

			onPosterSuccess(e, _dataset) {
				/* ---处理dataset begin--- */
				this.handleDataset(e, _dataset)
				/* ---处理dataset end--- */
				const {
					detail
				} = e;
				this.setData({
					posterUrl: detail
				});
			},

			onPosterFail(err, _dataset) {
				/* ---处理dataset begin--- */
				this.handleDataset(err, _dataset)
				/* ---处理dataset end--- */
				console.error(err);
			},

			hidePosterShow() {
				this.setData({
					posterShow: false,
					shareShow: ''
				});
			},

			/**
			 * 异步生成海报
			 */
			onCreatePoster() {},

			//点击保存到相册
			savePoster: function() {},

			handleContact(e) {
				console.log(e);
			},

			showModalService() {
				console.log("占位：函数 showModalService 未声明");
			}
		}
	};
</script>
<style>
	.product-bg {
		width: 100%;
		position: relative;
	}

	.product-bg swiper {
		width: 100%;
		height: calc(100vw);
		position: relative;
	}

	.product-bg .page-index {
		position: absolute;
		right: 30rpx;
		bottom: 30rpx;
	}

	.cu-bar.tabbar.shop .action {
		width: unset;
	}

	.to-down {
		margin-bottom: 100rpx;
	}

	.screen {
		width: 94% !important;
		border-radius: 20rpx;
		min-height: 900rpx;
		margin: auto;
		background-color: #ececec;
	}

	.screen-image {
		padding-top: 80rpx;
		height: 780rpx !important;
	}

	.shopping-cart {
		background-color: #2d2d2f !important;
		font-weight: 300;
		width: 220rpx;
	}

	.buy-now {
		background-color: #2967ff !important;
		font-weight: 300;
		width: 220rpx;
	}
</style>
