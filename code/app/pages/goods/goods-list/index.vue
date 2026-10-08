<template>
    <view style="height: 100%">
        <view class="cu-bar search bg-white fixed">
            <view class="search-form round">
                <text class="cuIcon-search"></text>
                <navigator class="response" hover-class="none" url="/pages/base/search/index">
                    <input type="text" placeholder="请输入餐品名" :value="parameter.name" />
                </navigator>
            </view>
            <view class="action">
                <view class="text-xxl">
                    <text :class="'cuIcon-' + (viewType ? 'list' : 'cascades') + ' text-black'"></text>
                </view>
            </view>
        </view>
        <view class="cu-bar justify-center bg-white fixed" style="margin-top: 80rpx">
            <view class="grid response text-center align-start">
                <view class="flex-sub padding-sm margin-xs radius text-blue text-bold">{{ title }}</view>
            </view>
        </view>
        <view style="margin-top: 160rpx">
            <view>
                <goods-row :goodsList="goodsList" />
            </view>
            <view :class="'cu-load bg-gray ' + (loadmore ? 'loading' : 'over')"></view>
        </view>
    </view>
</template>

<script>
import goodsRow from '@/components/goods-row/index';
const util = require('../../../utils/util.js');
const app = getApp();
export default {
    components: {
        goodsRow
    },
    data() {
        return {
            page: {
                current: 1,
                size: 10,
                descs: '',
                ascs: ''
            },
            parameter: {
                categorySecond: '',
                name: '',
                couponUserId: ''
            },
            loadmore: true,
            goodsList: [],
            viewType: false,
            price: '',
            sales: '',
            createTime: '',
            title: ''
        };
    },
    onLoad(options) {
        let title = options.title ? decodeURI(options.title) : '默认';
        this.setData({
            title: title
        });
        if (options.categorySecond) {
            this.setData({
                ['parameter.categorySecond']: options.categorySecond
            });
        }
        if (options.name) {
            this.setData({
                ['parameter.name']: options.name
            });
        }
        if (options.couponUserId) {
            this.setData({
                ['parameter.couponUserId']: options.couponUserId
            });
        }
       
            this.goodsPage();
        
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
        goodsPage() {
            app.globalData.api.goodsPage(Object.assign({}, this.page, util.filterForm(this.parameter))).then((res) => {
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

        sortHandle(e) {
            let type = e.target.dataset.type;
            switch (type) {
                case 'price':
                    if (this.price == '') {
                        this.setData({
                            price: 'asc',
                            ['page.descs']: '',
                            ['page.ascs']: 'sales_price'
                        });
                    } else if (this.price == 'asc') {
                        this.setData({
                            price: 'desc',
                            ['page.descs']: 'sales_price',
                            ['page.ascs']: ''
                        });
                    } else if (this.price == 'desc') {
                        this.setData({
                            price: '',
                            ['page.ascs']: '',
                            ['page.descs']: ''
                        });
                    }
                    this.setData({
                        sales: '',
                        createTime: ''
                    });
                    break;
                case 'sales':
                    if (this.sales == '') {
                        this.setData({
                            sales: 'desc',
                            ['page.descs']: 'sale_num',
                            ['page.ascs']: ''
                        });
                    } else if (this.sales == 'desc') {
                        this.setData({
                            sales: 'asc',
                            ['page.descs']: '',
                            ['page.ascs']: 'sale_num'
                        });
                    } else if (this.sales == 'asc') {
                        this.setData({
                            sales: '',
                            ['page.ascs']: '',
                            ['page.descs']: ''
                        });
                    }
                    this.setData({
                        price: '',
                        createTime: ''
                    });
                    break;
                case 'createTime':
                    if (this.createTime == '') {
                        this.setData({
                            createTime: 'desc',
                            ['page.descs']: 'create_time',
                            ['page.ascs']: ''
                        });
                    } else if (this.createTime == 'desc') {
                        this.setData({
                            createTime: '',
                            ['page.ascs']: '',
                            ['page.descs']: ''
                        });
                    }
                    this.setData({
                        price: '',
                        sales: ''
                    });
                    break;
            }
            this.relod();
        },

        relod() {
            this.setData({
                loadmore: true,
                goodsList: [],
                ['page.current']: 1
            });
            this.goodsPage();
        }
    }
};
</script>
<style>
.cu-bar {
    min-height: 80rpx !important;
}
.cuIcon-triangledownfill {
    margin-top: -22rpx;
}
</style>
