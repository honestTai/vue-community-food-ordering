  <template>
	<view style="height: 100%">
		<scroll-view scroll-x class="bg-white nav fixed">
			<view class="flex text-center">
				<view :class="'cu-item flex-sub ' + (index == tabCur ? 'text-blue cur' : '')" @tap="tabSelect"
					:data-index="index" :data-key="item.key" v-for="(item, index) in orderStatus" :key="index">
					{{ item.value }}
				</view>
			</view>
		</scroll-view>
		<view class="margin-top-bar">
			<view class="cu-card article">
				<view class="cu-item" v-for="(item, index) in orderList" :key="index">
					<navigator hover-class="none" :url="'/pages/order/order-detail/index?id=' + item.order.id">
						<view class="cu-bar bg-white">
							<view class="action">
								<text class="cuIcon-titles text-black"></text>
								{{ item.order.time }}
							</view>
							<view class="action text-red">
								{{
                                    item.order.status == 0
                                        ? '待配送'
                                        : item.order.status == 1
                                        ? '待消费'
                                        : item.order.status == 2
                                        ? '待评价'
                                        : item.order.status == 3
                                        ? '已完成'
                                        : item.order.status == 4
                                        ? '取消'
                                        : '已配送'
                                }}
							</view>
						</view>
						<view class="cu-item padding-bottom">
							<view class="content">
								<image
									:src="item.goods.goodsPhotoList[0] ? item.goods.goodsPhotoList[0] : '/static/public/img/no_pic.png'"
									mode="aspectFill" class="row-img margin-top-xs"></image>
								<view class="desc row-info margin-top-sm">
									<view class="text-black margin-top-sm overflow-2">{{ item.goods.name }}</view>
									<view class="flex justify-between">
										<view class="text-price text-bold text-xl text-blue margin-top-sm">
											{{ item.order.paymentPrice }}
										</view>
										<view class="text-black text-sm margin-top-sm padding-lr-sm">
											x{{ item.order.num }}</view>
									</view>
								</view>
							</view>
						</view>
					</navigator>

					<order-operate class="response" :orderInfo="item" @orderCancel="orderCancel($event, index)"
						@orderReceive="orderCancel($event, index)" @orderDel="orderDel($event, item.order)"
						@orderCv="ordercV($event, item.order)" :data-index="index" />
				</view>
			</view>
			<view :class="'cu-load bg-gray ' + (loadmore ? 'loading' : 'over')"></view>
		</view>
	</view>
</template>

<script>
	import orderOperate from '@/components/order-operate/index';
	const util = require('../../../utils/util.js');
	const app = getApp();
	export default {
		components: {
			orderOperate
		},
		data() {
			return {
				tabCur: 0,
				orderStatus: [{
						value: '全部订单',
						key: ''
					},
					{
						value: '待配送',
						key: '0'
					},
					{
						value: '已配送',
						key: '5'
					},
					{
						value: '待消费',
						key: '1'
					},
					{
						value: '待评价',
						key: '2'
					},
					{
						value: '已完成',
						key: '3'
					}
				],
				page: {
					current: 1,
					size: 10,
					status: -1
				},
				parameter: {
					status: ''
				},
				loadmore: true,
				orderList: []
			};
		},
		onShow() {},
		onLoad: function(options) {
			console.log(options);
			let that = this;
			if (options.status) {
				this.setData({
					['parameter.status']: options.status
				});
				this.orderStatus.forEach(function(status, index) {
					if (status.key == options.status) {
						that.setData({
							tabCur: index
						});
					}
				});
			}
			this.orderPage();

		},
		onReachBottom() {
			if (this.loadmore) {
				this.setData({
					['page.current']: this.page.current + 1
				});
				this.orderPage();
			}
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
		methods: {
			orderPage() {
				app.globalData.api.orderPage(Object.assign({}, this.page, util.filterForm(this.parameter))).then((res) => {
					let orderList = res.data.records;
					this.setData({
						orderList: [...this.orderList, ...orderList]
					});
					if (orderList.length < this.page.size) {
						this.setData({
							loadmore: false
						});
					}
				});
			},

			refresh() {
				this.setData({
					loadmore: true,
					orderList: [],
					['page.current']: 1
				});
				this.orderPage();
			},

			tabSelect(e) {
				let dataset = e.currentTarget.dataset;
				if (dataset.index != this.tabCur) {
					this.setData({
						tabCur: dataset.index,
						['parameter.status']: dataset.key
					});
					this.refresh();
				}
			},

			orderCancel(e, _dataset) {
				/* ---处理dataset begin--- */
				this.handleDataset(e, _dataset);
				/* ---处理dataset end--- */
				let index = e.currentTarget.dataset;
				let orderList = this.orderList;
				console.log(e)
				app.globalData.api.orderGet(orderList[index].order.id).then((res) => {
					this.orderList[index] = res.data;
					this.setData({
						orderList: this.orderList
					});
				});
			},

			orderDel(e, _dataset) {
				console.log(e)
				/* ---处理dataset begin--- */
				this.handleDataset(e, _dataset);
				/* ---处理dataset end--- */
				let index = e.currentTarget.dataset.index;
				this.orderList.splice(index, 1);
				this.setData({
					orderList: this.orderList
				});
			},

			ordercV(e, _dataset) {
				/* ---处理dataset begin--- */
				this.handleDataset(e, _dataset);
				/* ---处理dataset end--- */
			}
		}
	};
</script>
<style>
	.row-img {
		height: 240rpx !important;
		border-radius: 10rpx;
	}

	.row-info {
		display: block !important;
	}
</style>
