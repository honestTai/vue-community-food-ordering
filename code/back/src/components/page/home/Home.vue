<template>
    <div style="width: 100%; height: 100%">
        <div class="container" style="height: 80%">
            <div class="handle-box">

                <el-date-picker
                    v-model="statisticsVto.dateListString"
                    type="daterange"
                    align="right"
                    unlink-panels
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    :picker-options="pickerOptions"
                    @change="handleSearchDate()">
                </el-date-picker>
            </div>
            <ve-chart :data="orderData" :settings="chartSettings"></ve-chart>
        </div>
    </div>
</template>
<script>

import { statisticsOrder } from '@/utils';
import { baseUrl, exportOrder } from '../../../utils';

export default {
    data() {
        return {
            orderData: {
                columns: ['时间', '营业额', '订单'],
                rows: []
            },
            chartSettings: {
                type: 'line'
            },
            statisticsVto: {
                dateListString:""
            },
            pickerOptions: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },

        };
    },
    created() {
        this.getOrderData();
    },
    methods: {
        //数据获取
        getOrderData() {
            statisticsOrder(this.statisticsVto).then(res => {
                let data = res.data;
                let dataList = [];
                data.data.forEach(function(item) {
                    let dataMsg = { '时间': '', '营业额': '', '订单': '' };
                    dataMsg.时间 = item.dateTime;
                    dataMsg.营业额 = item.moneys;
                    dataMsg.订单 = item.orders;
                    dataList.push(dataMsg);
                });
                this.orderData.rows = dataList;
            });
        },
        handleSearchDate(val) {
            this.getOrderData();
        },
    }
};
</script>
